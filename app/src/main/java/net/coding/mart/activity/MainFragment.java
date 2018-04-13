package net.coding.mart.activity;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

import net.coding.mart.MainHighPayAdapter;
import net.coding.mart.R;
import net.coding.mart.common.BaseFragment;
import net.coding.mart.common.CommonActivity;
import net.coding.mart.common.CommonExtra;
import net.coding.mart.common.Global;
import net.coding.mart.common.constant.Progress;
import net.coding.mart.common.constant.RewardType;
import net.coding.mart.common.constant.RoleType;
import net.coding.mart.common.umeng.UmengEvent;
import net.coding.mart.common.widget.CustomPullRecyclerView;
import net.coding.mart.common.widget.EmptyRecyclerView;
import net.coding.mart.common.widget.MainRecyclerViewSpace;
import net.coding.mart.common.widget.main.DropItem;
import net.coding.mart.common.widget.main.DropdownButton;
import net.coding.mart.common.widget.main.DropdownListView;
import net.coding.mart.job.JobDetailActivity_;
import net.coding.mart.json.BaseObserver;
import net.coding.mart.json.MartRequest;
import net.coding.mart.json.Network;
import net.coding.mart.json.Pager;
import net.coding.mart.json.PagerData;
import net.coding.mart.json.reward.SimplePublished;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.AnimationRes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@EFragment(R.layout.app_bar_main2)
public class MainFragment extends BaseFragment {

    static View.OnClickListener clickRecyclerViewItem = v -> {
        SimplePublished data = (SimplePublished) v.getTag();
        String url = String.format("%s/project/%s", Global.HOST, data.id);
        JobDetailActivity_.intent(v.getContext())
                .mTitle(data.name)
                .url(url)
                .simplePublished(data)
                .finishToJump(CommonExtra.JumpParam.FinishToList)
                .start();
    };
    private final int DEFAULT_QUERY = -1;
    @FragmentArg
    Type type = Type.MAIN;
    @ViewById
    CustomPullRecyclerView recyclerView;
    @ViewById
    View mask;
    @ViewById
    DropdownButton chooseType, chooseLabel, chooseOrder;
    @ViewById
    DropdownListView dropdownType, dropdownLabel, dropdownOrder;
    @ViewById
    View tabs;
    @AnimationRes
    Animation dropdown_in, dropdown_out, dropdown_mask_out;
    DropdownButtonsController dropdownButtonsController = new DropdownButtonsController();
    EmptyRecyclerView recyclerEmpty;
    MainHighPayAdapter adapter;
    int roleId = DEFAULT_QUERY;
    int progressId = DEFAULT_QUERY;
    int rewardTypeId = DEFAULT_QUERY;

    PagerData<SimplePublished> pageData = new PagerData<>();
    Pager<SimplePublished> pageHigPay = new Pager<>();
    Subscription lastRequest = null;
    UltimateRecyclerView.OnLoadMoreListener loadMoreListener = (itemsCount, maxVisiblePos) -> {
        if (itemsCount == pageData.data.size()) {
            loadDataFromNetwork(false);
        }
    };

    private boolean isLoadAll() {
        return pageData.isLoadAll();
    }

    @AfterViews
    void initMainFragment() {
        if (getActivity() instanceof CommonActivity) {
            setActionBarTitle(R.string.high_pay_tip_title1);
        } else {
            setActionBarTitle("项目");
        }

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);

        recyclerView.addItemDecoration(new MainRecyclerViewSpace(getActivity()));
        recyclerView.setEmptyView(R.layout.common_recyclerview_empty, R.layout.common_recyclerview_empty);
        recyclerEmpty = (EmptyRecyclerView) recyclerView.getEmptyView();
        recyclerEmpty.initSuccessEmpty("没有符合条件的项目", R.mipmap.ic_reward_home_empt);
        recyclerEmpty.initFail("连接网络失败", v -> loadDataFromNetwork(true));
        recyclerEmpty.setLoading();
        adapter = new MainHighPayAdapter(clickRecyclerViewItem, pageData.data, LayoutInflater.from(getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.reenableLoadmore();
        recyclerView.setOnLoadMoreListener(loadMoreListener);
//        recyclerView.setDefaultOnRefreshListener(() -> loadDataFromNetwork());{
        recyclerView.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                frame.postDelayed(() -> reload(), 0);
            }
        });

        dropdownButtonsController.init();

        reload();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            recyclerView.mRecyclerView.setNestedScrollingEnabled(true);
        }
    }

    private void reload() {
//        if (type == Type.MAIN) {
//            loadHighPayRewardPreview();
//        } else {
            loadDataFromNetwork(true);
//        }
    }

    private void initPullToRefrush() {
        recyclerView.setPtrHandler(new PtrDefaultHandler() {

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout ptrFrameLayout, View view, View view2) {
                boolean canbePullDown = PtrDefaultHandler.checkContentCanBePulledDown(ptrFrameLayout, view, view2);
                return canbePullDown;
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
                ptrFrameLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadDataFromNetwork(true);
                    }
                }, 1800);
            }
        });
    }

    @Click
    void mask() {
        dropdownButtonsController.hide();
    }

    private void loadHighPayRewardPreview() {
        Network.getRetrofit(getActivity())
                .getHighPayReward()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<Pager<SimplePublished>>(getContext()) {
                    @Override
                    public void onSuccess(Pager<SimplePublished> data) {
                        super.onSuccess(data);

                        pageHigPay = data;
                        adapter.setHighPayRewards(pageHigPay.list);
                        loadDataFromNetwork(true);
                    }

                    @Override
                    public void onFail(int errorCode, @NonNull String error) {
                        super.onFail(errorCode, error);

                        recyclerEmpty.setLoadingFail(pageData.data);
                        recyclerView.setRefreshing(false);
                    }
                });
    }

    private void loadDataFromNetwork(boolean reload) {
        if (lastRequest != null) {
            lastRequest.unsubscribe();
        }

        if (reload) {
            pageData.setPageFirst();
        }

        MartRequest martRequest = Network.getRetrofit(getActivity(), Network.CacheType.noCache);

        if (type == Type.MAIN) {
            if (reload) {
                adapter.setHighPayRewards(pageHigPay.list);
            } else {
                adapter.setHighPayRewards(null);
            }
        }

        int highPaid = (type == Type.MAIN ? 0 : 1);

        lastRequest = martRequest.getRewardPages(rewardTypeId,
                progressId, roleId, pageData.page + 1, 20, highPaid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<Pager<SimplePublished>>(getActivity()) {
                    @Override
                    public void onSuccess(Pager<SimplePublished> data) {
                        super.onSuccess(data);

                        pageData.addData(data);
                        if (pageData.isLoadAll()) {
                            recyclerView.disableLoadmore();
                        } else {
                            recyclerView.reenableLoadmore();
                        }

                        adapter.notifyDataSetChanged();

                        recyclerEmpty.setLoadingSuccess(pageData.data);
                        recyclerView.setRefreshing(false);
                    }

                    @Override
                    public void onFail(int errorCode, @NonNull String error) {
                        super.onFail(errorCode, error);
                        recyclerEmpty.setLoadingFail(pageData.data);
                        recyclerView.setRefreshing(false);
                    }
                });
        bindSubscription(lastRequest);
    }


    public enum Type {
        MAIN,
        HIGH_PAY
    }

    private class DropdownButtonsController implements DropdownListView.Container {

        private DropdownListView currentDropdownList;
        private List<DropItem> dataType = new ArrayList<>();
        private List<DropItem> dataProgess = new ArrayList<>();
        private List<DropItem> dataRole = new ArrayList<>();

        @Override
        public void show(DropdownListView view) {
            if (currentDropdownList != null) {
                currentDropdownList.clearAnimation();
                currentDropdownList.startAnimation(dropdown_out);
                currentDropdownList.setVisibility(View.GONE);
                currentDropdownList.button.setChecked(false);
            }
            currentDropdownList = view;
            mask.clearAnimation();
            mask.setVisibility(View.VISIBLE);
            currentDropdownList.clearAnimation();
            currentDropdownList.startAnimation(dropdown_in);
            currentDropdownList.setVisibility(View.VISIBLE);
            currentDropdownList.button.setChecked(true);
        }

        @Override
        public void hide() {
            if (currentDropdownList != null) {
                currentDropdownList.clearAnimation();
                currentDropdownList.startAnimation(dropdown_out);
                currentDropdownList.button.setChecked(false);
                mask.clearAnimation();
                mask.startAnimation(dropdown_mask_out);
            }
            currentDropdownList = null;
        }

        @Override
        public void onSelectionChanged(DropdownListView view) {
            DropItem dropItem = view.current;
            if (dropItem instanceof RewardType) {
                rewardTypeId = dropItem.getId();
                umengEvent(UmengEvent.ACTION, String.format("项目 _ 类型 _ %s", dropItem.getAlics()));
            } else if (dropItem instanceof Progress) {
                progressId = dropItem.getId();
                umengEvent(UmengEvent.ACTION, String.format("项目 _ 进度 _ %s", dropItem.getAlics()));
            } else if (dropItem instanceof RoleType) {
                roleId = dropItem.getId();
                umengEvent(UmengEvent.ACTION, String.format("项目 _ 角色 _ %s", dropItem.getAlics()));
            }

            loadDataFromNetwork(true);
        }

        void reset() {
            chooseType.setChecked(false);
            chooseLabel.setChecked(false);
            chooseOrder.setChecked(false);

            dropdownType.setVisibility(View.GONE);
            dropdownLabel.setVisibility(View.GONE);
            dropdownOrder.setVisibility(View.GONE);
            mask.setVisibility(View.GONE);

            dropdownType.clearAnimation();
            dropdownLabel.clearAnimation();
            dropdownOrder.clearAnimation();
            mask.clearAnimation();
        }

        void init() {
            reset();

            Collections.addAll(dataType, RewardType.values());
            dropdownType.bind(dataType, chooseType, this);

            final Progress filterProgress[] = new Progress[]{
                    Progress.all,
                    Progress.recruit,
                    Progress.doing,
                    Progress.finish
            };
            Collections.addAll(dataProgess, filterProgress);
            dropdownLabel.bind(dataProgess, chooseLabel, this);

            Collections.addAll(dataRole, RoleType.values());
            dropdownOrder.bind(dataRole, chooseOrder, this);

            dropdown_mask_out.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (currentDropdownList == null) {
                        reset();
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
        }

    }
}
