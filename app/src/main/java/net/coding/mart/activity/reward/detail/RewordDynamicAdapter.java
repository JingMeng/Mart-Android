package net.coding.mart.activity.reward.detail;

import android.view.View;

import com.marshalchen.ultimaterecyclerview.quickAdapter.easyRegularAdapter;

import net.coding.mart.R;
import net.coding.mart.json.reward.RewardDynamic;

import java.util.List;

/**
 * Created by chenchao on 16/6/20.
 */
public class RewordDynamicAdapter extends easyRegularAdapter<RewardDynamic, RewardDynamicHolder> {

    public RewordDynamicAdapter(List<RewardDynamic> list) {
        super(list);
    }

    @Override
    protected int getNormalLayoutResId() {
        return R.layout.reward_dynamic;
    }

    @Override
    protected RewardDynamicHolder newViewHolder(View view) {
        return new RewardDynamicHolder(view);
    }

    @Override
    protected void withBindHolder(RewardDynamicHolder holder, RewardDynamic data, int position) {
        int sum = getAdapterItemCount();
        holder.bindData(data, position, sum);
    }
}
