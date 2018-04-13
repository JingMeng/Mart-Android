package net.coding.mart.activity;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;

import net.coding.mart.R;
import net.coding.mart.activity.reward.PublishRewardActivity_;
import net.coding.mart.common.BaseActivity;
import net.coding.mart.json.Network;
import net.coding.mart.json.NewBaseObserver;
import net.coding.mart.json.reward.Published;
import net.coding.mart.json.reward.project.ProjectPublish;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by chenchao on 16/7/28.
 */
class MyPublishListAdapter extends UltimateViewAdapter<MyPublishListViewHolder> {

    private List<Published> listData;
    private View.OnClickListener clickItem;
    private View.OnClickListener clickDetail;
    private View.OnClickListener clickPay;

    View.OnClickListener clickEdit = v -> {
        Published published = (Published) v.getTag();

        Network.getRetrofit(v.getContext())
                .getProjectPublishDetail(published.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NewBaseObserver<ProjectPublish>(v.getContext()) {
                    @Override
                    public void onSuccess(ProjectPublish data) {
                        super.onSuccess(data);
                        ((BaseActivity) v.getContext()).showSending(false);
                        PublishRewardActivity_.intent(v.getContext())
                                .projectPublish(data)
                                .start();
                    }

                    @Override
                    public void onFail(int errorCode, @NonNull String error) {
                        super.onFail(errorCode, error);
                        ((BaseActivity) v.getContext()).showSending(false);
                    }
                });
        ((BaseActivity) v.getContext()).showSending(true);
    };

    public MyPublishListAdapter(List<Published> mData, View.OnClickListener clickItem, View.OnClickListener clickDetail, View.OnClickListener clickPay) {
        this.listData = mData;
        this.clickItem = clickItem;
        this.clickDetail = clickDetail;
        this.clickPay = clickPay;
    }

    @Override
    public void onBindViewHolder(MyPublishListViewHolder holder, int position) {
        holder.bind(listData.get(position));
    }

    @Override
    public MyPublishListViewHolder onCreateViewHolder(ViewGroup parent) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_publish_job, parent, false);
        item.setOnClickListener(clickItem);

        item.findViewById(R.id.buttonOk).setOnClickListener(clickDetail);
        item.findViewById(R.id.buttonJump).setOnClickListener(clickPay);
        item.findViewById(R.id.buttonEdit).setOnClickListener(clickEdit);

        return new MyPublishListViewHolder(item);
    }

    @Override
    public int getAdapterItemCount() {
        return listData.size();
    }

    @Override
    public long generateHeaderId(int position) {
        return 0;
    }


    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
    }

    @Override
    public MyPublishListViewHolder newFooterHolder(View view) {
        return null;
    }

    @Override
    public MyPublishListViewHolder newHeaderHolder(View view) {
        return null;
    }
}
