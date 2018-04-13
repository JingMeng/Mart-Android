package net.coding.mart.activity.reward.detail.coder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;

import net.coding.mart.R;

/**
 * Created by chenchao on 16/10/13.
 * 开发者报名列表
 */

public class CoderHolder extends UltimateRecyclerviewViewHolder {

    public View rootLayout;
    public TextView role;
    public TextView status;
    public ImageView icon;
    public TextView name;
    public TextView time;
    public TextView buttonRefuse;
    public TextView buttonAccept;
    public View bottomLayout;
    public View bottomSpace;

    public CoderHolder(View view) {
        super(view);

        rootLayout = view;
        role = (TextView) view.findViewById(R.id.role);
        status = (TextView) view.findViewById(R.id.status);
        icon = (ImageView) view.findViewById(R.id.icon);
        name = (TextView) view.findViewById(R.id.name);
        time = (TextView) view.findViewById(R.id.time);
        buttonRefuse = (TextView) view.findViewById(R.id.buttonRefuse);
        buttonAccept = (TextView) view.findViewById(R.id.buttonAccept);
        bottomLayout = view.findViewById(R.id.bottomLayout);
        bottomSpace = view.findViewById(R.id.bottomSpace);
    }

    public void setStatus(String statusString, int statusColor, int acceptVisable, int refuseVisable, int bottomVisable) {
        status.setText(statusString);
        status.setTextColor(statusColor);
        buttonAccept.setVisibility(acceptVisable);
        if (acceptVisable == View.VISIBLE) {
            buttonRefuse.setText("不合适");
        } else {
            buttonRefuse.setText("取消合作");
        }
        buttonRefuse.setVisibility(refuseVisable);
        bottomLayout.setVisibility(bottomVisable);
        if (bottomVisable == View.VISIBLE) {
            bottomSpace.setVisibility(View.GONE);
        } else {
            bottomSpace.setVisibility(View.VISIBLE);
        }
    }
}
