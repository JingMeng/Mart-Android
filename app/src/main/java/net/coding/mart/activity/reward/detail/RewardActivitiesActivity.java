package net.coding.mart.activity.reward.detail;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

import net.coding.mart.R;
import net.coding.mart.common.BackActivity;
import net.coding.mart.common.widget.EmptyRecyclerView;
import net.coding.mart.json.BaseObserver;
import net.coding.mart.json.Network;
import net.coding.mart.json.Pager;
import net.coding.mart.json.PagerData;
import net.coding.mart.json.reward.RewardDynamic;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@EActivity(R.layout.activity_reward_activities)
public class RewardActivitiesActivity extends BackActivity {

    @Extra
    int id;

    @ViewById
    UltimateRecyclerView recyclerView;

    @ViewById
    EmptyRecyclerView emptyView;

    RewordDynamicAdapter adapter;

    PagerData<RewardDynamic> pageData = new PagerData<>();


    private boolean isLoadAll() {
        return pageData.isLoadAll();
    }

    @AfterViews
    void initRewardActivitiesActivity() {
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.reenableLoadmore();
        recyclerView.setOnLoadMoreListener(loadMoreListener);
        recyclerView.setEmptyView(R.layout.empty, 0);
        loadMore();

        recyclerView.setEmptyView(R.layout.empty, 0);
        emptyView.initSuccessEmpty("", R.mipmap.ic_notify_empty);
        emptyView.initFail("获取通知失败", v -> loadMore());

        adapter = new RewordDynamicAdapter(pageData.data);

        recyclerView.setAdapter(adapter);
    }

    UltimateRecyclerView.OnLoadMoreListener loadMoreListener = (itemsCount, maxVisiblePos) -> {
        if (itemsCount == pageData.data.size()) {
            loadMore();
        }
    };


    private void loadMore() {
        if (isLoadAll()) {
            return;
        }

        Network.getRetrofit(this)
                .getRewardDynamic(id, pageData.page + 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<Pager<RewardDynamic>>(this) {
                    @Override
                    public void onSuccess(Pager<RewardDynamic> data) {
                        super.onSuccess(data);

                        pageData.addData(data);

                        if (pageData.isLoadAll()) {
                            recyclerView.disableLoadmore();
                        } else {
                            recyclerView.reenableLoadmore();
                        }

                        emptyView.setLoadingSuccess(pageData.data);
                        adapter.notifyDataSetChanged();
                    }
                });
    }

}
