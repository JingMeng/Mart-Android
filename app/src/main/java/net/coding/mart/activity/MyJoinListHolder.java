package net.coding.mart.activity;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import net.coding.mart.LengthUtil;
import net.coding.mart.R;
import net.coding.mart.common.ImageLoadTool;
import net.coding.mart.common.constant.JoinStatus;
import net.coding.mart.common.constant.Progress;
import net.coding.mart.common.constant.RewardType;
import net.coding.mart.json.RoleType;
import net.coding.mart.json.reward.JoinJob;

import java.util.List;

public class MyJoinListHolder extends RecyclerView.ViewHolder {

    private View rootView;
    private ImageView icon;
    private TextView type;
    private TextView title;
    private TextView price;
    private TextView progress;
    private TextView skillRequest;
    private TextView jobId;
    private TextView time;
    private TextView jobProgress;
    private TextView applyCount;

    private TextView buttonCannel;
    private TextView buttonOk;
    private TextView buttonJump;
    private View buttonProject;

    public MyJoinListHolder(View v) {
        super(v);

        rootView = v;
        title = (TextView) v.findViewById(R.id.title);
        time = (TextView) v.findViewById(R.id.time);
        skillRequest = (TextView) v.findViewById(R.id.skillRequest);
        progress = (TextView) v.findViewById(R.id.progress);
        price = (TextView) v.findViewById(R.id.price);
        title = (TextView) v.findViewById(R.id.title);
        type = (TextView) v.findViewById(R.id.type);
        jobId = (TextView) v.findViewById(R.id.jobId);
        icon = (ImageView) v.findViewById(R.id.icon);
        jobProgress = (TextView) v.findViewById(R.id.jobProgress);
        buttonCannel = (TextView) v.findViewById(R.id.buttonCannel);
        buttonOk = (TextView) v.findViewById(R.id.buttonOk);
        buttonJump = (TextView) v.findViewById(R.id.buttonJump);
        buttonProject = v.findViewById(R.id.buttonReward);
        applyCount = (TextView) v.findViewById(R.id.applyCount);
    }

    private Spanned applyCount2Html(int count) {
        return Html.fromHtml(String.format("<font color='#222222'>%s</font> 人报名", count));
    }

    private void showOneButton(TextView button, String text, JoinJob data) {
        buttonCannel.setVisibility(View.GONE);
        buttonOk.setVisibility(View.GONE);
        buttonJump.setVisibility(View.GONE);

        button.setTag(data);
        button.setVisibility(View.VISIBLE);
        button.setText(text);
    }

    private void showTwoButton(JoinJob data) {
        buttonCannel.setVisibility(View.VISIBLE);
        buttonOk.setVisibility(View.GONE);
        buttonJump.setVisibility(View.VISIBLE);

        buttonJump.setText("编辑申请");
        buttonJump.setTag(data);
        buttonCannel.setTag(data);

        if (data.getRewardStatus().canEditJoinApply()) {
            buttonCannel.setEnabled(true);
            buttonJump.setEnabled(true);
        } else {
            buttonCannel.setEnabled(false);
            buttonJump.setEnabled(false);
        }
    }

    public void setData(JoinJob data) {
        rootView.setTag(R.id.layoutRoot, data);
        ImageLoadTool.loadImagePublishJob(icon, data.cover);

        title.setText(data.title);

        String typeString = data.getTypeString();
        type.setText(typeString);
        Drawable typeIcon = RewardType.iconFromType(type.getContext(), typeString);
        type.setCompoundDrawables(typeIcon, null, null, null);

        price.setText(data.formatPrice);

        Progress jobStatus = data.getRewardStatus();
        jobProgress.setText(jobStatus.text);
        GradientDrawable bg = (GradientDrawable) jobProgress.getBackground();
        bg.setStroke(LengthUtil.dpToPx(1), jobStatus.color);
        jobProgress.setTextColor(jobStatus.color);

        rootView.setClickable(jobStatus.canJumpDetail());

        JoinStatus joinState = data.getApplyStatus();
        progress.setText(joinState.text);
        progress.setTextColor(joinState.bgColor);

        if (joinState.needReApply()) {
            showOneButton(buttonOk, "重新报名", data);
        } else {
            showTwoButton(data);
        }

        buttonOk.setEnabled(jobStatus == Progress.recruit);

        if (joinState.isPass()) {
            buttonProject.setVisibility(View.VISIBLE);
            buttonProject.setEnabled(jobStatus.joinedCanEnterRewardDetail());
            buttonOk.setVisibility(View.GONE);
        } else {
            buttonProject.setVisibility(View.GONE);
        }

        String skill = "";
        List<RoleType> roles = data.roleTypes;
        for (int i = 0; i < roles.size(); ++i) {
            if (i == 0) {
                skill += roles.get(i).name;
            } else {
                skill += "," + roles.get(i).name;
            }
        }
        skillRequest.setText(skill);

        jobId.setText(String.format("No.%d", data.id));
        time.setText(Html.fromHtml(String.format("周期: <font color=\"#F6A823\">%d</font> 天", data.duration)));


        if (jobStatus == Progress.recruit) {
            applyCount.setVisibility(View.VISIBLE);
            applyCount.setText(applyCount2Html(data.applyCount));
        } else {
            applyCount.setVisibility(View.GONE);
        }

        buttonProject.setTag(data);
    }
}
