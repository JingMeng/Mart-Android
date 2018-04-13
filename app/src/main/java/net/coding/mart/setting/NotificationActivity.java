package net.coding.mart.setting;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

import net.coding.mart.R;
import net.coding.mart.common.BackActivity;
import net.coding.mart.common.util.RxBus;
import net.coding.mart.common.widget.CustomPullRecyclerView;
import net.coding.mart.common.widget.EmptyRecyclerView;
import net.coding.mart.json.BaseHttpResult;
import net.coding.mart.json.BaseObserver;
import net.coding.mart.json.Network;
import net.coding.mart.json.NewBaseObserver;
import net.coding.mart.json.Notification;
import net.coding.mart.json.Pager;
import net.coding.mart.json.SimpleObserver;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@EActivity(R.layout.activity_notification)
public class NotificationActivity extends BackActivity {

    public static final int PAGE_SIZE = 20;
    @ViewById
    CustomPullRecyclerView recyclerView;
    @ViewById
    EmptyRecyclerView emptyView;
    ArrayList<Notification> data = new ArrayList<>();
    private MenuItem displayNoRead;
    private MenuItem displayAll;
    private NotificationAdapter adapter;
    View.OnClickListener recyclerItemClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Notification itemData = (Notification) v.getTag();
            if (!itemData.isRead()) {
                Network.getRetrofit(v.getContext())
                        .markNotification(itemData.id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new NewBaseObserver<BaseHttpResult>(v.getContext()) {
                            @Override
                            public void onSuccess(BaseHttpResult data) {
                                super.onSuccess(data);
                                itemData.setStatusRead();
                                adapter.notifyDataSetChanged();
                            }
                        });
            }
        }
    };
    private int dataPage = 1;
    private boolean isEndPage = false;
    private Type listType = Type.all;
    UltimateRecyclerView.OnLoadMoreListener loadMoreListener = (itemsCount, maxVisiblePos) -> {
        if (itemsCount == data.size()) {
            loadMore();
        }
    };

    @AfterViews
    void initNotifycationActivity() {
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.reenableLoadmore();
        recyclerView.setOnLoadMoreListener(loadMoreListener);
        recyclerView.setEmptyView(R.layout.empty, 0);

        newRequest();

        adapter = new NotificationAdapter(data, recyclerItemClick);
        adapter.setCustomLoadMoreView(LayoutInflater.from(this).inflate(R.layout.recycler_view_load_more, null));
        recyclerView.setAdapter(adapter);
        recyclerView.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                if (displayNoRead != null && displayNoRead.isVisible()) {
                    listType = Type.all;
                } else {
                    listType = Type.unread;
                }
                newRequest();
            }
        });

        emptyView.initSuccessEmpty("", R.mipmap.ic_notify_empty);
        emptyView.initFail("获取通知失败", v -> newRequest());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.notification, menu);
        displayAll = menu.findItem(R.id.action_all);
        displayAll.setVisible(false);
        displayNoRead = menu.findItem(R.id.action_no_read);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void finish() {
        RxBus.getInstance().send(new RxBus.UpdateBottomBarMessageEvent());
        super.finish();
    }

    @OptionsItem
    void action_no_read() {
        displayAll.setVisible(true);
        displayNoRead.setVisible(false);

        listType = Type.unread;
        newRequest();
    }

    private void newRequest() {
        dataPage = 1;
        isEndPage = false;
        recyclerView.reenableLoadmore();

        loadDataFromNetwork();
    }

    private void loadMore() {
        if (isEndPage) {
            return;
        }

        loadDataFromNetwork();
    }

    private void loadDataFromNetwork() {
        Network.getRetrofit(this)
                .getNotification(listType.name(), dataPage, PAGE_SIZE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<Pager<Notification>>(this) {

                    @Override
                    public void onSuccess(Pager<Notification> pager) {
                        super.onSuccess(pager);

                        if (pager.page == 1) {
                            data.clear();
                        }
                        data.addAll(pager.list);

                        if (pager.list.isEmpty()) {
                            isEndPage = true;
                            recyclerView.disableLoadmore();
                        } else {
                            isEndPage = false;
                            ++dataPage;
                            recyclerView.reenableLoadmore();
                        }

                        String emptyMessage = (listType == Type.all) ?
                                "您还没有收到任何通知" : "无未读通知";
                        emptyView.setLoadingSuccess(data, emptyMessage);

                        adapter.data = data;
                        adapter.notifyDataSetChanged();
                        recyclerView.refreshComplete();
                    }

                    @Override
                    public void onFail(int errorCode, @NonNull String error) {
                        super.onFail(errorCode, error);
                        emptyView.setLoadingFail(data);
                        recyclerView.refreshComplete();
                    }

                });
    }

    @OptionsItem
    void action_all() {
        displayAll.setVisible(false);
        displayNoRead.setVisible(true);

        listType = Type.all;
        newRequest();
    }

    @OptionsItem
    void action_mark_all_read() {
        Network.getRetrofit(this)
                .markAllNotify()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleObserver(this) {
                    @Override
                    public void onSuccess() {
                        super.onSuccess();
                        for (Notification item : data) {
                            item.setStatusRead();
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    enum Type {
        all,
        unread
    }
}
