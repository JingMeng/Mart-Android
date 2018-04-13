package net.coding.mart.activity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

import net.coding.mart.R;
import net.coding.mart.activity.mpay.FinalPayOrdersActivity_;
import net.coding.mart.activity.reward.PublishRewardActivity_;
import net.coding.mart.activity.reward.detail.v2.V2RewardDetailActivity_;
import net.coding.mart.common.BaseFragment;
import net.coding.mart.common.event.PaySuccessEvent;
import net.coding.mart.common.util.RxBus;
import net.coding.mart.common.widget.CustomPullRecyclerView;
import net.coding.mart.common.widget.EmptyRecyclerView;
import net.coding.mart.common.widget.PublishRecyclerViewSpace;
import net.coding.mart.job.JobDetailActivity_;
import net.coding.mart.json.BaseObserver;
import net.coding.mart.json.Network;
import net.coding.mart.json.NewBaseObserver;
import net.coding.mart.json.PagerData;
import net.coding.mart.json.mpay.Order;
import net.coding.mart.json.reward.Published;
import net.coding.mart.json.reward.RewardWapper;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

@EFragment(R.layout.common_recycler_view)
public class MyPublishListFragment extends BaseFragment {

    private static final int RESULT_ADD = 1;
    private static final int RESULT_EDIT = 2;
    private static final int RESULT_PAY = 3;
    private static final int RESULT_REFRUSH = 4;
    protected MyPublishListAdapter mAdapter;
    @ViewById
    CustomPullRecyclerView recyclerView;
    EmptyRecyclerView recyclerEmpty;
    private CompositeSubscription subscription = new CompositeSubscription();
    private PagerData<Published> pageData = new PagerData<>();
    UltimateRecyclerView.OnLoadMoreListener mLoadMoreListener = (itemsCount, maxLastVisiblePosition) -> getDataFromService(false);
    private View.OnClickListener clickListItem = v -> {
        Object data = v.getTag(R.id.layoutRoot);
        if (data instanceof Published) {
            Published pub = (Published) data;
            if (!pub.canJumpDetail()) {
                return;
            }

            String url = pub.getUrl();
            JobDetailActivity_.intent(getActivity())
                    .url(url)
                    .isPublishJob(true)
                    .start();
        }
    };
    private View.OnClickListener clickDetail = v -> {
        Object data = v.getTag();
        if (data instanceof Published) {
            Published publishJob = (Published) data;

            if (publishJob.isNewPhase()) {
                V2RewardDetailActivity_.intent(this)
                        .id(publishJob.getId())
                        .publishJob(publishJob)
                        .startForResult(RESULT_REFRUSH);
            } else {
//                RewardDetailActivity_.intent(this)
//                        .id(publishJob.getId())
//                        .publishJob(publishJob)
//                        .startForResult(RESULT_REFRUSH);
            }
        }
    };

    private View.OnClickListener clickPay = v -> {
        Object data = v.getTag();
        if (data instanceof Published) {
            Published publishJob = (Published) data;
            Network.getRetrofit(getActivity())
                    .createRwardOrder(String.valueOf(publishJob.id))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new NewBaseObserver<Order>(getActivity()) {
                        @Override
                        public void onSuccess(Order data) {
                            super.onSuccess(data);
                            showSending(false);

                            ArrayList<String> ids = new ArrayList<>();
                            ids.add(data.orderId);
                            FinalPayOrdersActivity_.intent(MyPublishListFragment.this)
                                    .orderIds(ids)
                                    .start();

                        }

                        @Override
                        public void onFail(int errorCode, @NonNull String error) {
                            super.onFail(errorCode, error);
                            showSending(false);
                        }
                    });
            showSending(true);
        }
    };

    @AfterViews
    void initMyPublishListFragment() {
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.addItemDecoration(new PublishRecyclerViewSpace(getActivity()));

        recyclerView.setLayoutManager(manager);
        recyclerView.reenableLoadmore();

        recyclerView.setOnLoadMoreListener(mLoadMoreListener);

        mAdapter = new MyPublishListAdapter(pageData.data, clickListItem, clickDetail, clickPay);
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

        getDataFromService(true, Network.CacheType.onlyCache);
        getDataFromService(true);
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateEvent(PaySuccessEvent event) {
        getDataFromService(true);
    }

    private void initListViewEmpty(UltimateRecyclerView recyclerView) {
        recyclerView.setEmptyView(R.layout.common_recyclerview_empty, R.layout.common_recyclerview_empty); //R.layout.common_recyclerview_empty);
        EmptyRecyclerView v = (EmptyRecyclerView) recyclerView.getEmptyView();
        v.initSuccessEmpty("您还没有发布的项目", R.mipmap.ic_reward_empty);
        v.initSuccessEmpty("去试试", v1 -> PublishRewardActivity_.intent(this).start());
        v.initFail("刷新", v1 -> getDataFromService(true));
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (!hidden) {
            getDataFromService(true);
        }
    }

    protected void getDataFromService(boolean reload) {
        getDataFromService(reload, Network.CacheType.useCache);
    }

    protected void getDataFromService(boolean reload, Network.CacheType cacheType) {
        if (cacheType == Network.CacheType.onlyCache) {

        } else {
            if (reload) {
                pageData.setPageFirst();
            }
        }

        Network.getRetrofit(getActivity(), cacheType)
                .getMyPublishList(pageData.page + 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<RewardWapper<Published>>(getActivity()) {
                    @Override
                    public void onSuccess(RewardWapper<Published> data) {
                        super.onSuccess(data);
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

                    @Override
                    public void onFail(int errorCode, @NonNull String error) {
                        if (cacheType == Network.CacheType.onlyCache) {
                            return;
                        }

                        super.onFail(errorCode, error);
                        recyclerEmpty.setLoadingFail(pageData.data);
                        recyclerView.setRefreshing(false);
                    }
                });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        subscription.add(RxBus.getInstance().toObserverable().subscribe(o -> {
            if (o instanceof RxBus.RewardPublishSuccess) {
                getDataFromService(true);
            }
        }));
    }

    @Override
    public void onDestroyView() {
        subscription.clear();
        super.onDestroyView();
    }

    @OnActivityResult(RESULT_ADD)
    void onResultAdd(int resultCode, @OnActivityResult.Extra Published resultData) {
        if (resultCode == Activity.RESULT_OK) {
            getDataFromService(true);
            mAdapter.notifyDataSetChanged();
        }
    }

    @OnActivityResult(RESULT_EDIT)
    void onResult(int resultCode, @OnActivityResult.Extra Published resultData) {
        if (resultCode == Activity.RESULT_OK) {
            List<Published> listData = pageData.data;
            for (int i = 0; i < listData.size(); ++i) {
                Published item = listData.get(i);
                if (item.getId() == resultData.getId()) {
                    listData.set(i, resultData);
                    mAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    @OnActivityResult(RESULT_REFRUSH)
    void onResultRefrush(int resultCode) {
        if (resultCode == Activity.RESULT_OK) {
            getDataFromService(true);
        }
    }

}
