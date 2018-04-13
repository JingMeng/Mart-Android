package net.coding.mart;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;

import net.coding.mart.activity.MainHeaderItemHolder;
import net.coding.mart.common.Global;
import net.coding.mart.common.ImageLoadTool;
import net.coding.mart.common.constant.Progress;
import net.coding.mart.common.constant.RewardType;
import net.coding.mart.json.RoleType;
import net.coding.mart.json.reward.SimplePublished;

import java.util.List;

/**
 * Created by chenchao on 16/3/19.
 */
public class MainAdapter extends UltimateViewAdapter<MainRecyclerviewItemHolder> {

    protected View.OnClickListener itemClick;
    private List<SimplePublished> datums;

    public MainAdapter(View.OnClickListener itemClick, List<SimplePublished> list) {
        this.itemClick = itemClick;
        this.datums = list;
    }

    @Override
    public long generateHeaderId(int position) {
        return 0;
    }

    @Override
    public MainRecyclerviewItemHolder onCreateViewHolder(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        MainRecyclerviewItemHolder holder = new MainRecyclerviewItemHolder(inflater, parent);
        holder.rootLayout.setOnClickListener(itemClick);
        return holder;
    }

    @Override
    public int getAdapterItemCount() {
        return datums.size();
    }

    @Override
    public void onBindViewHolder(MainRecyclerviewItemHolder holder, int position) {
        if (hasHeaderView()) {
            --position;
        }

        if (position < 0) {
            return; // 有
        }

        SimplePublished data = datums.get(position);

        bindItem(holder, data);
    }

    protected void bindItem(MainRecyclerviewItemHolder holder, SimplePublished data) {
        holder.rootLayout.setTag(data);
        ImageLoadTool.loadImage(holder.icon, data.cover);

        holder.name.setText(data.name);

        String typeString = RewardType.idToName(data.type);
        holder.type.setText(typeString);
        Drawable typeIcon = RewardType.iconFromType(holder.type.getContext(), typeString);
        holder.type.setCompoundDrawables(typeIcon, null, null, null);

        holder.money.setText(data.getPriceString());

        Integer status = data.getStatus().id;
        String progressString = Progress.id2Name(status);
        holder.progress.setText(progressString);
        GradientDrawable bg = (GradientDrawable) holder.progress.getBackground();
        int progressColor = data.getStatus().color;
        holder.progress.setTextColor(progressColor);
        bg.setStroke(LengthUtil.dpToPx(1), progressColor);
        holder.progress.setBackground(bg);

        String skill = "";
        List<RoleType> roles = data.roleTypes;
        for (int i = 0; i < roles.size(); ++i) {
            if (i == 0) {
                skill += roles.get(i).name;
            } else {
                skill += "," + roles.get(i).name;
            }
        }
        holder.roly.setText(skill);

        holder.id.setText(Global.generateRewardIdString(data.id));
//        time.setText(Html.fromHtml(String.format("交付周期: <font color=\"#ff497f\">%d天</font>", data.duration)))
        holder.signUp.setText(String.format("%d人报名", data.applyCount));

        int visibility = data.isHighPaid() ? View.VISIBLE : View.GONE;
        holder.iconHighPay.setVisibility(visibility);
        holder.highPayText.setVisibility(visibility);
    }

    protected SimplePublished getDataForPosition(int pos) {
        return datums.get(pos);
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        MainHeaderItemHolder holder = new MainHeaderItemHolder(inflater, parent);
        return holder;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
    }

    @Override
    public MainRecyclerviewItemHolder newFooterHolder(View view) {
        return null;
    }

    @Override
    public MainRecyclerviewItemHolder newHeaderHolder(View view) {
        return new MainRecyclerviewItemHolder(view);
    }

}
