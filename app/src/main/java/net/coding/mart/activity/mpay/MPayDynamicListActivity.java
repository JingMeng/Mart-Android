package net.coding.mart.activity.mpay;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import net.coding.mart.R;
import net.coding.mart.activity.mpay.freeze.FreezeDynamicListActivity_;
import net.coding.mart.activity.setting.SetMPayPasswordMainActivity_;
import net.coding.mart.common.BackActivity;
import net.coding.mart.common.MyData;
import net.coding.mart.common.constant.DeveloperType;
import net.coding.mart.common.widget.main.DropdownButton;
import net.coding.mart.common.widget.main.DropdownListItemView;
import net.coding.mart.json.Network;
import net.coding.mart.json.NewBaseObserver;
import net.coding.mart.json.PagerData;
import net.coding.mart.json.mpay.Account;
import net.coding.mart.json.mpay.MPayAccount;
import net.coding.mart.json.mpay.Order;
import net.coding.mart.json.mpay.OrderMapper;
import net.coding.mart.json.mpay.OrderMapperTime;
import net.coding.mart.json.mpay.OrderMapperTrade;
import net.coding.mart.json.mpay.OrderPage;
import net.coding.mart.json.mpay.WithdrawRequire;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.AnimationRes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


@EActivity(R.layout.activity_mpay_dynamic_list)
@OptionsMenu(R.menu.mpay_dynamic_list)
public class MPayDynamicListActivity extends BackActivity {

    private static final int RESULT_PAY = 1;
    private static final int RESULT_ORDER_DETAIL = 2;
    final int FilterHeaderPos = 2; // 点击交易记录 head, 列表自动移动上来, 分类列表的位置是 2
    @ViewById
    ListView listView;
    HeadHelp headHelp;
    PagerData<Order> pageData = new PagerData<>();
    ArrayAdapter<Order> adapter;
    @ViewById
    View mask, filterLayout, typeLayout, statusLayout;
    @ViewById
    DropdownButton timeFilter, typeFilter, statusFilter;
    @ViewById
    ListView timeListView, typeListView, statusListView;
    View[] filters;
    @AnimationRes
    Animation dropdown_in, dropdown_out, dropdown_mask_out;
    View listFooter;
    MPayAccount mPayAccount;
    OrderMapper orderMapper;
    AbsListView.OnScrollListener onScroll = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (firstVisibleItem <= 1) {
                filterLayout.setVisibility(View.INVISIBLE);
            } else {
                filterLayout.setVisibility(View.VISIBLE);
            }
        }
    };
    boolean isLoading = false;
    TypeAdapter typeAdapter;
    TimeAdapter timeAdapter;
    StatusAdapter statusAdaper;
    ListView.OnItemClickListener timeItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            OrderMapperTime newPick = timeAdapter.getItem(position);
            if (newPick != timeAdapter.picked && newPick != null) {
                timeAdapter.setPick(position);
                timeFilter.setText(newPick.text);
                headHelp.listHeadTimeFilter.setText(newPick.text);
                tabCollapse();
                loadPageData(true);
            }
        }
    };
    ListView.OnItemClickListener typeItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            OrderMapperTrade clickType = typeAdapter.getItem(position);
            typeAdapter.pickItem(clickType);
        }
    };
    ListView.OnItemClickListener statusItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String clickStatus = statusAdaper.getItem(position);
            statusAdaper.pickItem(clickStatus);
        }
    };

    @AfterViews
    void initMPayDynamicListActivity() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setShowBack();

        orderMapper = MyData.loadMPayOrderMapper(this);

        filters = new View[]{
                timeListView,
                typeLayout,
                statusLayout
        };

        View head = getLayoutInflater().inflate(R.layout.activity_mpay_dynamic_list_head,
                listView, false);
        listView.addHeaderView(head);

        View listHead1 = getLayoutInflater().inflate(R.layout.activity_mpay_dynamic_list_head1,
                listView, false);
        listView.addHeaderView(listHead1);

        View listHead2 = getLayoutInflater().inflate(R.layout.activity_mpay_dynamic_list_head2,
                listView, false);
        listView.addHeaderView(listHead2);

        listFooter = getLayoutInflater().inflate(R.layout.activity_mpay_dynamic_list_footer,
                listView, false);
        listView.addFooterView(listFooter);
        listView.setOnScrollListener(onScroll);
        adapter = new OrderAdapter(this, pageData.data);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Order order = (Order) parent.getItemAtPosition(position);
            if (order != null) {
                MPayDynamicListDetail_.intent(MPayDynamicListActivity.this)
                        .order(order)
                        .startForResult(RESULT_ORDER_DETAIL);
            }
        });

        typeAdapter = new TypeAdapter(this);
        timeAdapter = new TimeAdapter(this);
        statusAdaper = new StatusAdapter(this);

        timeListView.setAdapter(timeAdapter);
        timeListView.setOnItemClickListener(timeItemClick);
        typeListView.setAdapter(typeAdapter);
        typeListView.setOnItemClickListener(typeItemClick);
        statusListView.setAdapter(statusAdaper);
        statusListView.setOnItemClickListener(statusItemClick);

        timeFilter.setText(timeAdapter.picked.text);
        typeFilter.setText("交易类型");
        statusFilter.setText("交易状态");

        headHelp = new HeadHelp(head, listHead1, listHead2);

        tabCollapse();
        filterLayout.setVisibility(View.INVISIBLE);

        loadHeaderData();
        loadPageData(true);
    }

    void loadHeaderData() {
        Network.getRetrofit(this)
                .getMPayAccount()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NewBaseObserver<MPayAccount>(this) {
                    @Override
                    public void onSuccess(MPayAccount data) {
                        super.onSuccess(data);

                        mPayAccount = data;
                        headHelp.bind(data.account);
                    }

                });
    }

    void loadPageData(boolean reload) {
        if (isLoading) {
            return;
        }

        if (reload) {
            pageData.setPageFirst();
        }

        if (pageData.isLoadAll()) {
            return;
        }

        Network.getRetrofit(this)
                .getMPayOrderPage(pageData.page + 1,
                        timeAdapter.getParam(),
                        typeAdapter.getParam(),
                        statusAdaper.getParam(),
                        typeAdapter.getOldParam())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NewBaseObserver<OrderPage>(this) {
                    @Override
                    public void onSuccess(OrderPage data) {
                        super.onSuccess(data);
                        pageData.addData(data.orderList, data.pageInfo);
                        adapter.notifyDataSetChanged();

                        if (pageData.page == 1) {
                            if (pageData.data.size() > 0) {
                                headHelp.showDynamicFilter();
                                int needSapce = listView.getHeight() - headHelp.head2.getHeight()
                                        - adapter.getCount() * getResources().getDimensionPixelSize(R.dimen.mpay_dynamic_list_item_height);
                                if (needSapce < 0) {
                                    needSapce = 0;
                                }

                                modifyFooterHeight(needSapce);
                            } else {
                                if (headHelp.isDynamicFilterVisibility()) {
                                    int needSapce = listView.getHeight() - headHelp.head2.getHeight();
                                    modifyFooterHeight(needSapce);
                                } else {
                                    modifyFooterHeight(0);
                                }
                            }
                        } else {
                            modifyFooterHeight(0);
                        }

                        isLoading = false;
                    }

                    @Override
                    public void onFail(int errorCode, @NonNull String error) {
                        super.onFail(errorCode, error);
                        isLoading = false;
                    }
                });

        isLoading = true;
    }

    private void modifyFooterHeight(int needSapce) {
        ViewGroup.LayoutParams lp = listFooter.getLayoutParams();
        lp.height = needSapce;
        listFooter.setLayoutParams(lp);
    }

    private void tabCollapse() {
        mask.setVisibility(View.INVISIBLE);
        for (View item : filters) {
            item.setVisibility(View.INVISIBLE);
        }

        filtersCheckFalse();
    }

    @Click
    void timeFilter() {
        pickList(timeListView);
        timeFilter.setChecked(true);
    }

    @Click
    void typeFilter() {
        typeAdapter.saveOldPick();
        pickList(typeLayout);
        typeFilter.setChecked(true);
    }

    @Click
    void statusFilter() {
        statusAdaper.saveOldPick();
        pickList(statusLayout);
        statusFilter.setChecked(true);
    }

    private void pickList(View view) {
        if (view.getVisibility() == View.VISIBLE) {
            view.setVisibility(View.INVISIBLE);
            mask.setVisibility(View.INVISIBLE);
        } else {
            view.setVisibility(View.VISIBLE);
            mask.setVisibility(View.VISIBLE);
        }

        for (View item : filters) {
            if (item != view) {
                item.setVisibility(View.INVISIBLE);
            }
        }

        filtersCheckFalse();
    }

    private void filtersCheckFalse() {
        timeFilter.setChecked(false);
        typeFilter.setChecked(false);
        statusFilter.setChecked(false);
    }

    @Click
    void mask() {
        typeCancel();
        statusCancel();
    }

    @Click
    void typeCancel() {
        typeAdapter.cancelPick();
        tabCollapse();
    }

    @Click
    void typeOk() {
        loadPageData(true);
        tabCollapse();
    }

    @Click
    void statusCancel() {
        statusAdaper.cancelPick();
        tabCollapse();
    }

    @Click
    void statusOk() {
        loadPageData(true);
        tabCollapse();
    }

    @OptionsItem
    void actionResetPassword() {
        SetMPayPasswordMainActivity_.intent(this).start();
    }

    @OnActivityResult(RESULT_PAY)
    void onResultPay(int result) {
        if (result == RESULT_OK) {
            loadHeaderData();
            loadPageData(true);
        }
    }

    @OnActivityResult(RESULT_ORDER_DETAIL)
    void onResultOrderDetail(int result, @OnActivityResult.Extra Order resultData) {
        if (result == RESULT_OK && resultData != null) {
            for (int i = 0; i < pageData.data.size(); ++i) {
                Order oldOrder = pageData.data.get(i);
                if (oldOrder.orderId.equals(resultData.orderId)) {
                    pageData.data.set(i, resultData);
                    adapter.notifyDataSetChanged();
                    break;
                }
            }
        }
    }

    class HeadHelp {
        private final DropdownButton listHeadStatusFilter;
        private final DropdownButton listHeadTimeFilter;
        TextView balance;
        TextView freeze;
        TextView sum;
        View head1;
        View head2;
        View head;

        View topWarn;
        TextView withdrawTextView;

        HeadHelp(View head, View head1, View head2) {
            this.head = head;
            head.findViewById(R.id.recharge).setOnClickListener(v -> jumpToRecharge());
            head.findViewById(R.id.enterpriceRecharge).setOnClickListener(v -> jumpToRecharge());
            head.findViewById(R.id.withdraw).setOnClickListener(v -> {

//                if (MyData.getInstance().getData().getRewardRole() == DeveloperType.TEAM) {
//                    topWarn.setVisibility(View.VISIBLE);
//                    return;
//                }

                Network.getRetrofit(MPayDynamicListActivity.this)
                        .getWithdrawRequire()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new NewBaseObserver<WithdrawRequire>(MPayDynamicListActivity.this) {
                            @Override
                            public void onSuccess(WithdrawRequire data) {
                                super.onSuccess(data);
                                showSending(false);

                                if (!data.hasPassword || !data.passIdentity) {
                                    new MPayAlertDialog(MPayDynamicListActivity.this, data).show();
                                } else if (data.accounts.isEmpty()) {
                                    SetWithdrawAccountActivity_.intent(MPayDynamicListActivity.this)
                                            .start();
                                } else {
                                    MPayWithdrawActivity_.intent(MPayDynamicListActivity.this)
                                            .require(data)
                                            .startForResult(RESULT_PAY);
                                }
                            }

                            @Override
                            public void onFail(int errorCode, @NonNull String error) {
                                super.onFail(errorCode, error);
                                showSending(false);
                            }
                        });

                showSending(true);
            });

            topWarn = head.findViewById(R.id.topWarn);
            withdrawTextView = (TextView) head.findViewById(R.id.withdrawTextView);

            if (MyData.getInstance().getData().getRewardRole() == DeveloperType.TEAM) {
                withdrawTextView.setTextColor(0xFFADBBCB);
                withdrawTextView.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.mpay_dynamic_list_withdraw_disable,
                        0, 0, 0);
            }

//            head.findViewById(R.id.withdrawBalanceLayout).setOnClickListener(v1 -> {
//                if (adapter.getCount() > 0) {
//                    listView.setSelection(FilterHeaderPos);
//                }
//            });

            head.findViewById(R.id.freezeBalanceLayout).setOnClickListener(v1 -> {
                if (mPayAccount != null) {
                    FreezeDynamicListActivity_.intent(MPayDynamicListActivity.this)
                            .mPayAccount(mPayAccount)
                            .start();
                } else {
                    loadHeaderData();
                }
            });

            sum = (TextView) head.findViewById(R.id.sum);
            balance = (TextView) head.findViewById(R.id.withdrawBalance);
            freeze = (TextView) head.findViewById(R.id.freezeBalance);

            this.head1 = head1;
            this.head2 = head2;

            listHeadTimeFilter = (DropdownButton) head2.findViewById(R.id.listHeadTimeFilter);
            DropdownButton listHeadTypeFilter = (DropdownButton) head2.findViewById(R.id.listHeadTypeFilter);
            listHeadStatusFilter = (DropdownButton) head2.findViewById(R.id.listHeadStatusFilter);

            listHeadTimeFilter.setText(timeAdapter.picked.text);
            listHeadTypeFilter.setText("交易类型");
            listHeadStatusFilter.setText("交易状态");

            listHeadTimeFilter.setOnClickListener(v -> {
                listView.setSelection(FilterHeaderPos);
                timeFilter.performClick();
            });

            listHeadTypeFilter.setOnClickListener(v -> {
                listView.setSelection(FilterHeaderPos);
                typeFilter.performClick();
            });

            listHeadStatusFilter.setOnClickListener(v -> {
                listView.setSelection(FilterHeaderPos);
                statusFilter.performClick();
            });

            head1.setVisibility(View.INVISIBLE);
            head2.setVisibility(View.INVISIBLE);

            if (MyData.getInstance().getData().isEnterpriseAccout()) {
                head.findViewById(R.id.withdrawRechargeLayout).setVisibility(View.GONE);
                head.findViewById(R.id.balanceFreezeLayout).setVisibility(View.GONE);
            } else {
                head.findViewById(R.id.enterpriceRecharge).setVisibility(View.GONE);
            }
        }

        private void jumpToRecharge() {
            MPayRechargeActivity_.intent(MPayDynamicListActivity.this).startForResult(RESULT_PAY);
        }

        void showDynamicFilter() {
            head1.setVisibility(View.VISIBLE);
            head2.setVisibility(View.VISIBLE);
        }

        boolean isDynamicFilterVisibility() {
            return head2.getVisibility() == View.VISIBLE;
        }

        public void bindHeader(String time) {
            listHeadTimeFilter.setText(time);
        }

        public void bind(Account account) {
            balance.setText(account.balance);
            freeze.setText(account.freeze);
            String sumString = String.format("%s", account.balanceValue.add(account.freezeValue));
            sum.setText(sumString);
        }
    }

    class OrderAdapter extends ArrayAdapter<Order> { //adapter = new ArrayAdapter<Order>(this, R.layout.activity_mpay_dynamic_list_item) {

        OrderAdapter(Context context, List<Order> data) {
            super(context, 0, data);
        }

        @Override
        @NonNull
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            MPayDynamicListItemHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_mpay_dynamic_list_item,
                        parent, false);
                holder = new MPayDynamicListItemHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (MPayDynamicListItemHolder) convertView.getTag();
            }

            Order data = getItem(position);
            holder.bind(data);

            if (position == getCount() - 1) {
                loadPageData(false);
            }

            return convertView;
        }
    }

    class TimeAdapter extends ArrayAdapter<OrderMapperTime> {

        OrderMapperTime picked;

        public TimeAdapter(Context context) {
            super(context, 0, orderMapper.timeOptions);
            picked = orderMapper.timeOptions.get(0);
        }

        public void setPick(int postion) {
            this.picked = getItem(postion);
            notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.dropdown_tab_list_item, parent, false);
            }
            DropdownListItemView itemView = (DropdownListItemView) convertView;
            OrderMapperTime data = getItem(position);
            itemView.bind(data.text, picked == data);

            return convertView;
        }

        public String getParam() {
            return picked.getRequestParam();
        }

    }

    class TypeAdapter extends ArrayAdapter<OrderMapperTrade> {

        Set<OrderMapperTrade> picks = new HashSet<>();
        Set<OrderMapperTrade> oldPicks = new HashSet<>();
        public TypeAdapter(Context context) {
            super(context, 0, orderMapper.tradeOptions);
        }

        public void cancelPick() {
            picks.clear();
            picks.addAll(oldPicks);
            notifyDataSetChanged();
        }

        public void saveOldPick() {
            oldPicks.clear();
            oldPicks.addAll(picks);
        }

        public void pickItem(OrderMapperTrade item) {
            if (picks.contains(item)) {
                picks.remove(item);
            } else {
                picks.add(item);
            }
            notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.dropdown_tab_list_item, parent, false);
            }

            DropdownListItemView itemView = (DropdownListItemView) convertView;
            OrderMapperTrade data = getItem(position);
            if (data != null) {
                itemView.bind(data.title, picks.contains(data));
            }

            return convertView;
        }

        public List<String> getParam() {
            List<String> params = new ArrayList<>();
            for (OrderMapperTrade item : picks) {
                params.add(item.value);
            }
            return params;
        }

        public List<String> getOldParam() {
            List<String> params = new ArrayList<>();
            for (OrderMapperTrade item : picks) {
                params.addAll(item.names);
            }
            return params;
        }
    }

    class StatusAdapter extends ArrayAdapter<String> {

        Map<String, String> statusMap;
        Set<String> picks = new HashSet<>();
        Set<String> oldPicks = new HashSet<>();

        StatusAdapter(Context context) {
            super(context, 0, orderMapper.status.keySet().toArray(new String[0]));
            statusMap = orderMapper.status;
        }

        void cancelPick() {
            picks.clear();
            picks.addAll(oldPicks);
            notifyDataSetChanged();
        }

        void saveOldPick() {
            oldPicks.clear();
            oldPicks.addAll(picks);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.dropdown_tab_list_item, parent, false);
            }
            DropdownListItemView itemView = (DropdownListItemView) convertView;
            String data = getItem(position);
            itemView.bind(statusMap.get(data), picks.contains(data));

            return convertView;
        }

        public void pickItem(String item) {
            if (picks.contains(item)) {
                picks.remove(item);
            } else {
                picks.add(item);
            }
            notifyDataSetChanged();
        }

        public List<String> getParam() {
            List<String> params = new ArrayList<>();
            for (String item : picks) {
                params.add(item);
            }
            return params;
        }
    }
}
