package net.coding.mart.activity.reward.detail;

import android.view.View;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;

import net.coding.mart.R;
import net.coding.mart.common.ImageLoadTool;
import net.coding.mart.databinding.RewardDynamicBinding;
import net.coding.mart.json.reward.RewardDynamic;

/**
 * Created by chenchao on 16/6/20.
 */
public class RewardDynamicHolder extends UltimateRecyclerviewViewHolder {


    private final RewardDynamicBinding binding;

    public RewardDynamicHolder(View itemView) {
        super(itemView);

        binding = RewardDynamicBinding.bind(itemView);
    }

    public void bindData(RewardDynamic data, int position, int sum) {
        binding.setDynamic(data);
        ImageLoadTool.loadImage(binding.userIcon, data.user.avatar);

        if (position == 0) {
            binding.lineTop.setVisibility(View.INVISIBLE);
            binding.point.setImageResource(R.mipmap.reward_dynamic_point_first);
        } else {
            binding.lineTop.setVisibility(View.VISIBLE);
            binding.point.setImageResource(R.mipmap.reward_dynamic_point);
        }

        if (position == sum - 1) {
            binding.lineBottom.setVisibility(View.INVISIBLE);
        } else {
            binding.lineBottom.setVisibility(View.VISIBLE);
        }

        binding.title.setHtmlText(data.actionMsg);

        String content = data.remark;
        if (content.matches("[0-9]*")) { // "" 或纯数字不显示
            binding.content.setVisibility(View.GONE);
        } else {
            binding.content.setVisibility(View.VISIBLE);
            binding.content.setHtmlText(content);
        }
    }
}
