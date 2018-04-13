package net.coding.mart.activity;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;

import net.coding.mart.R;
import net.coding.mart.json.reward.JoinJob;

import java.util.List;

/**
 * Created by chenchao on 16/7/29.
 */
class MyJoinListAdapter extends UltimateViewAdapter<MyJoinListHolder> {

    List<JoinJob> mData;

    View.OnClickListener mClickItem;
    View.OnClickListener mClickEditJoin;
    View.OnClickListener mClickCannelJoin;
    View.OnClickListener clickReward;

    public MyJoinListAdapter(List<JoinJob> mData, View.OnClickListener clickItem, View.OnClickListener clickOk,
                             View.OnClickListener clickCannel, View.OnClickListener clickReward) {
        this.mData = mData;
        this.mClickItem = clickItem;
        this.mClickEditJoin = clickOk;
        this.mClickCannelJoin = clickCannel;
        this.clickReward = clickReward;
    }

    @Override
    public void onBindViewHolder(MyJoinListHolder holder, int position) {
        holder.setData(mData.get(position));
    }

    @Override
    public MyJoinListHolder onCreateViewHolder(ViewGroup parent) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_join_job, parent, false);
        bindEvent(item);

        return new MyJoinListHolder(item);
    }

    void bindEvent(View rootView) {
        rootView.setOnClickListener(mClickItem);
        rootView.findViewById(R.id.buttonOk).setOnClickListener(mClickEditJoin);
        rootView.findViewById(R.id.buttonJump).setOnClickListener(mClickEditJoin);
        rootView.findViewById(R.id.buttonCannel).setOnClickListener(mClickCannelJoin);
        rootView.findViewById(R.id.buttonReward).setOnClickListener(clickReward);
    }

    @Override
    public int getAdapterItemCount() {
        return mData.size();
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
    public MyJoinListHolder newFooterHolder(View view) {
        return null;
    }

    @Override
    public MyJoinListHolder newHeaderHolder(View view) {
        return null;
    }
}
