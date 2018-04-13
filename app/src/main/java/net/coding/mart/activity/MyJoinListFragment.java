package net.coding.mart.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

import net.coding.mart.R;
import net.coding.mart.activity.reward.detail.v2.V2RewardDetailActivity_;
import net.coding.mart.common.BaseActivity;
import net.coding.mart.common.BaseFragment;
import net.coding.mart.common.constant.JoinStatus;
import net.coding.mart.common.umeng.UmengEvent;
import net.coding.mart.common.util.ActivityNavigate;
import net.coding.mart.common.widget.CustomPullRecyclerView;
import net.coding.mart.common.widget.EmptyRecyclerView;
import net.coding.mart.common.widget.PublishRecyclerViewSpace;
import net.coding.mart.common.widget.main.DropdownListItemView;
import net.coding.mart.job.JoinActivity_;
import net.coding.mart.json.BaseObserver;
import net.coding.mart.json.Network;
import net.coding.mart.json.PagerData;
import net.coding.mart.json.SimpleObserver;
import net.coding.mart.json.reward.JoinJob;
import net.coding.mart.json.reward.RewardWapper;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@EFragment(R.layout.fragment_my_join_list)
public class MyJoinListFragment extends BaseFragment {

    private static final int RESULT_JOIN = 1;

    @ViewById
    CustomPullRecyclerView recyclerView;

    @ViewById
    View mask;

    @ViewById
    ListView progressTypeList;

    EmptyRecyclerView recyclerEmpty;

    protected MyJoinListAdapter mAdapter;
    protected JoinStateAdapter joinStateAdapter;

    private PagerData<JoinJob> pageData = new PagerData<>();

    @AfterViews
    void initJoinJobsActivity() {
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.addItemDecoration(new PublishRecyclerViewSpace(getActivity()));

        recyclerView.setLayoutManager(manager);
        recyclerView.reenableLoadmore();

        recyclerView.setOnLoadMoreListener(mLoadMoreListener);

        mAdapter = new MyJoinListAdapter(pageData.data, mClickItem, mClickEditJoin, mClickCannelJoin, clickReward);
        initListViewEmpty(recyclerView);
        recyclerEmpty = (EmptyRecyclerView) recyclerView.getEmptyView();
        recyclerView.showEmptyView();
        recyclerEmpty.setLoading();
        recyclerView.getEmptyView().findViewById(R.id.btnRetry).setOnClickListener(v -> getDataFromService(true));
        recyclerView.setAdapter(mAdapter);

        recyclerView.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getDataFromService(true);
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            recyclerView.mRecyclerView.setNestedScrollingEnabled(true);
        }

        joinStateAdapter = new JoinStateAdapter(getActivity());
        progressTypeList.setAdapter(joinStateAdapter);
        mask();

        getDataFromLocal(true);
        getDataFromService(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void filterEvent(String message) {
        if (message.equals(MainActivity.CLICK_ACTION_TITLE)) {
            showFilterList(mask.getVisibility() != View.VISIBLE);
        }
    }

    @Click
    void mask() {
        showFilterList(false);
    }

    private void showFilterList(boolean show) {
        int visibility = show ? View.VISIBLE : View.GONE;
        progressTypeList.setVisibility(visibility);
        mask.setVisibility(visibility);
    }

    @ItemClick
    void progressTypeListItemClicked(JoinStatus clickItem) {
        joinStateAdapter.setPick(clickItem);
        getDataFromService(true);
        mask();
    }

    class JoinStateAdapter extends ArrayAdapter<JoinStatus> {
        public JoinStateAdapter(Context context) {
            super(context, 0, JoinStatus.values());
        }

        JoinStatus picked = JoinStatus.noJoin;

        public void setPick(JoinStatus joinStatus) {
            picked = joinStatus;
            notifyDataSetChanged();
        }

        JoinStatus getPicked() {
            return picked;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dropdown_tab_list_item, parent, false);
            }
            DropdownListItemView itemView = (DropdownListItemView) convertView;
            JoinStatus data = getItem(position);
            String title;
            if (data == JoinStatus.noJoin) {
                title = "所有状态";
            } else {
                title = data.text;
            }
            itemView.bind(title, picked == data);

            return convertView;
        }
    }

    private void initListViewEmpty(UltimateRecyclerView recyclerView) {
        recyclerView.setEmptyView(R.layout.common_recyclerview_empty, R.layout.common_recyclerview_empty);
        EmptyRecyclerView v = (EmptyRecyclerView) recyclerView.getEmptyView();
        v.initSuccessEmpty("您还没有参与的项目", R.mipmap.ic_reward_empty);
        v.initSuccessEmpty("去看看", v1 -> {
            Activity parent = getActivity();
            if (parent instanceof MainActivity) {
                ((MainActivity) parent).clickBottomBarMain();
            }
        });
        v.initFail("刷新", v1 -> getDataFromService(true));
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (!hidden) {
            getDataFromService(true);
        }
    }

    protected void getDataFromService(final boolean isReload) {
        if (isReload) {
            pageData.setPageFirst();
        }

        JoinStatus picked = joinStateAdapter.getPicked();
        String joinStatusString = "";
        if (picked != JoinStatus.noJoin) {
            joinStatusString = String.valueOf(picked.id);
        }

        Network.getRetrofit(getActivity(), Network.CacheType.useCache)
                .getMyJoinList(pageData.page + 1, joinStatusString)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<RewardWapper<JoinJob>>(getActivity()) {
                    @Override
                    public void onSuccess(RewardWapper<JoinJob> data) {
                        super.onSuccess(data);
                        requestSuccess(data);
                    }

                    @Override
                    public void onFail(int errorCode, @NonNull String error) {
                        super.onFail(errorCode, error);
                        requestFail();
                    }
                });
    }

    protected void getDataFromLocal(final boolean isReload) {
        if (isReload) {
            pageData.setPageFirst();
        }

        JoinStatus picked = joinStateAdapter.getPicked();
        String joinStatusString = "";
        if (picked != JoinStatus.noJoin) {
            joinStatusString = String.valueOf(picked.id);
        }

        Network.getRetrofit(getActivity(), Network.CacheType.onlyCache)
                .getMyJoinList(pageData.page + 1, joinStatusString)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<RewardWapper<JoinJob>>(getActivity()) {
                    @Override
                    public void onSuccess(RewardWapper<JoinJob> data) {
                        super.onSuccess(data);
                        requestSuccess(data);
                    }

                    @Override
                    public void onFail(int errorCode, @NonNull String error) {
//                        super.onFail(errorCode, error);
//                        requestFail();
                    }
                });
    }

    private void requestFail() {
        recyclerEmpty.setLoadingFail(pageData.data);
        recyclerView.setRefreshing(false);
    }

    private void requestSuccess(RewardWapper<JoinJob> data) {
        pageData.addData(data.publishedPage);
        if (pageData.isLoadAll()) {
            recyclerView.disableLoadmore();
        } else {
            recyclerView.reenableLoadmore();
        }
        mAdapter.notifyDataSetChanged();

        recyclerEmpty.setLoadingSuccess(pageData.data);
        recyclerView.setRefreshing(false);
    }

    UltimateRecyclerView.OnLoadMoreListener mLoadMoreListener = (itemsCount, maxLastVisiblePosition) -> getDataFromService(false);

    private View.OnClickListener mClickItem = v -> {
        Object data = v.getTag(R.id.layoutRoot);
        if (data instanceof JoinJob) {

            JoinJob joinJob = (JoinJob) data;
            if (!joinJob.canJumpDetail()) {
                return;
            }

            String url = joinJob.getUrl();
            ActivityNavigate.startJobDetail(MyJoinListFragment.this, url);
        }
    };

    private View.OnClickListener mClickEditJoin = v -> {
        JoinJob joinJob = (JoinJob) v.getTag();
        JoinActivity_.intent(this)
                .mJoinJob(joinJob)
                .startForResult(RESULT_JOIN);

        if (joinJob.getApplyStatus().needReApply()) {
            umengEvent(UmengEvent.ACTION, "我参与的项目 _ 重新报名");
        } else {
            umengEvent(UmengEvent.ACTION, "我参与的项目 _ 编辑申请");
        }

    };

    private View.OnClickListener clickReward = v -> {
        JoinJob joinJob = (JoinJob) v.getTag();

        if (joinJob.isNewPhase()) {
            V2RewardDetailActivity_.intent(this).id(joinJob.id).joinJob(joinJob).start();
        } else {
//            RewardDetailActivity_.intent(this).id(joinJob.id).joinJob(joinJob).start();

        }
    };

    private View.OnClickListener mClickCannelJoin = v -> {
        JoinJob joinJob = (JoinJob) v.getTag();
        new AlertDialog.Builder(getActivity())
                .setMessage("确定要取消申请吗？")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", (dialog1, which1) -> {
                    Network.getRetrofit(getActivity())
                            .cancelJoin(joinJob.id)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new SimpleObserver(getActivity()) {
                                @Override
                                public void onSuccess() {
                                    super.onSuccess();
                                    joinJob.setApplyStatus(JoinStatus.joinsStartCancel);
                                    mAdapter.notifyDataSetChanged();
                                    ((BaseActivity) getActivity()).showSending(false, "");
                                }

                                @Override
                                public void onFail(int errorCode, @NonNull String error) {
                                    super.onFail(errorCode, error);
                                    ((BaseActivity) getActivity()).showSending(false, "");
                                }
                            });
                    ((BaseActivity) getActivity()).showSending(true, "");
                    umengEvent(UmengEvent.ACTION, "我参与的项目 _ 取消参与");
                })
                .show();

    };

    @OnActivityResult(RESULT_JOIN)
    void onResult(int resultCode) {
        if (resultCode == Activity.RESULT_OK) {
            getDataFromService(true);
        }
    }

}
