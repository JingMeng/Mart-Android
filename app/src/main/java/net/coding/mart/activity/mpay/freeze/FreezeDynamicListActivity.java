package net.coding.mart.activity.mpay.freeze;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import net.coding.mart.R;
import net.coding.mart.common.BackActivity;
import net.coding.mart.common.util.DialogFactory;
import net.coding.mart.json.Network;
import net.coding.mart.json.NewBaseObserver;
import net.coding.mart.json.PagerData;
import net.coding.mart.json.mpay.FreezeDynamic;
import net.coding.mart.json.mpay.FreezeDynamiicPager;
import net.coding.mart.json.mpay.MPayAccount;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@EActivity(R.layout.activity_freeze_dynamic_list)
public class FreezeDynamicListActivity extends BackActivity {

    @Extra
    MPayAccount mPayAccount;

    @ViewById
    ListView listView;

    View headSection;

    PagerData<FreezeDynamic> pageData = new PagerData<>();
    ArrayAdapter<FreezeDynamic> adapter;

    boolean isLoading = false;

    @AfterViews
    void initFreezeDynamicListActivity() {
        View header = getLayoutInflater().inflate(R.layout.activity_freeze_dynamic_list_header, listView, false);
        TextView freezeBalance = (TextView) header.findViewById(R.id.freezeBalance);
        freezeBalance.setText(mPayAccount.account.freeze);
        header.findViewById(R.id.freezeTip).setOnClickListener(v -> {
            DialogFactory.create(FreezeDynamicListActivity.this, R.layout.dialog_tip_freeze);
        });

        headSection = header.findViewById(R.id.headerSection);
        headSection.setVisibility(View.GONE);
        listView.addHeaderView(header, null, false);

        adapter = new ListAdapter(this, pageData.data);
        listView.setAdapter(adapter);

        loadPageData(true);
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
                .getFreezeDynamics(pageData.page + 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NewBaseObserver<FreezeDynamiicPager>(this) {
                    @Override
                    public void onSuccess(FreezeDynamiicPager data) {
                        super.onSuccess(data);
                        pageData.addData(data.freezeDynamics, data.pageInfo);
                        adapter.notifyDataSetChanged();

                        if (data.pageInfo.page == 1) {
                            if (data.freezeDynamics.size() == 0) {
                                headSection.setVisibility(View.GONE);
                            } else {
                                headSection.setVisibility(View.VISIBLE);
                            }
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

    class ListAdapter extends ArrayAdapter<FreezeDynamic> {

        public ListAdapter(Context context, List<FreezeDynamic> data) {
            super(context, 0, data);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            FreezeDynamicItemHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_freeze_dynamic_list_item,
                        parent, false);
                holder = new FreezeDynamicItemHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (FreezeDynamicItemHolder) convertView.getTag();
            }

            FreezeDynamic data = getItem(position);
            holder.bind(data);

            if (position == getCount() - 1) {
                loadPageData(false);
            }

            return convertView;
        }
    }

}
