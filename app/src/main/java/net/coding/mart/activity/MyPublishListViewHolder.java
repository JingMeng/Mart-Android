package net.coding.mart.activity;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import net.coding.mart.R;
import net.coding.mart.common.ImageLoadTool;
import net.coding.mart.common.constant.Progress;
import net.coding.mart.common.constant.RewardType;
import net.coding.mart.json.RoleType;
import net.coding.mart.json.reward.Published;

import java.util.List;

/**
 * Created by chenchao on 16/7/28.
 */
public class MyPublishListViewHolder extends RecyclerView.ViewHolder {

    private View rootView;
    private ImageView icon;
    private TextView type;
    private TextView title;
    private TextView price;
    private TextView skillRequest;
    private TextView jobId;
    private TextView time;
    private TextView jobProgress;
    private TextView visit;

    private TextView developerDay;

    private TextView buttonCannel;
    private TextView buttonOk;
    private TextView buttonJump;

    private TextView payInfo;
    private View buttonEdit;

    public MyPublishListViewHolder(View v) {
        super(v);

        rootView = v;
        title = (TextView) v.findViewById(R.id.title);
        time = (TextView) v.findViewById(R.id.time);
        skillRequest = (TextView) v.findViewById(R.id.skillRequest);
        price = (TextView) v.findViewById(R.id.price);
        title = (TextView) v.findViewById(R.id.title);
        type = (TextView) v.findViewById(R.id.type);
        jobId = (TextView) v.findViewById(R.id.jobId);
        icon = (ImageView) v.findViewById(R.id.icon);
        jobProgress = (TextView) v.findViewById(R.id.jobProgress);
        buttonCannel = (TextView) v.findViewById(R.id.buttonCannel);
        buttonOk = (TextView) v.findViewById(R.id.buttonOk);
        buttonJump = (TextView) v.findViewById(R.id.buttonJump);
        visit = (TextView) v.findViewById(R.id.visit);
        View payInfoView = v.findViewById(R.id.payInfo);
        developerDay = (TextView) v.findViewById(R.id.developerDay);
        if (payInfoView instanceof TextView) {
            payInfo = (TextView) payInfoView;
        }
        buttonEdit = v.findViewById(R.id.buttonEdit);
    }

    public void bind(Published data) {
        rootView.setTag(R.id.layoutRoot, data);

        ImageLoadTool.loadImagePublishJob(icon, data.getCover());

        title.setText(data.getName());

        String typeString = data.getTypeString();
        type.setText(typeString);

        Drawable typeIcon = RewardType.iconFromType(type.getContext(), typeString);
        type.setCompoundDrawables(typeIcon, null, null, null);

        price.setText(data.getFormat_price());

        Progress jobStatus = data.getStatus();
        jobProgress.setTextColor(jobStatus.color);
        jobProgress.setText(jobStatus.text);

        rootView.setClickable(jobStatus.canJumpDetail());

        if (data.canEdit()) {
            buttonEdit.setVisibility(View.VISIBLE);
            buttonEdit.setTag(data);
        } else {
            buttonEdit.setVisibility(View.GONE);
        }

        if (data.isMPay()) {
            buttonJump.setText("支付发布费");
            buttonJump.setVisibility(data.needPayPrepayment ? View.VISIBLE : View.GONE);
        } else {
            buttonJump.setText("立即付款");
            View payInfoLayout = (View) payInfo.getParent();
            //                已支付金额 < 项目总金额
//                项目状态：待审核，审核中，招募中，开发中
//                若某项目多次（一次以上）付款未付清时，立即付款按钮旁边显示未付金额
            if (jobStatus == Progress.waitCheck ||
                    jobStatus == Progress.checking ||
                    jobStatus == Progress.recruit ||
                    jobStatus == Progress.doing) {

                buttonJump.setVisibility(0 < data.getBalance() ? View.VISIBLE : View.GONE);

                if (!data.noPay() && 0 < data.getBalance()) { // 付过钱,但未付清
                    payInfoLayout.setVisibility(View.VISIBLE);
                    payInfo.setText(Html.fromHtml(String.format("温馨提示：还剩 <font color='#FF497F'>%s</font> 未支付，请尽快支付！", data.getFormat_balance())));
                } else {
                    payInfoLayout.setVisibility(View.GONE);
                }
            } else {
                payInfoLayout.setVisibility(View.GONE);
                buttonJump.setVisibility(View.GONE);
            }
        }

        buttonJump.setTag(data);
        buttonOk.setTag(data);

        buttonOk.setVisibility(!data.needPayPrepayment ? View.VISIBLE : View.GONE);
        buttonCannel.setVisibility(View.GONE);

        String skill = "";
        List<RoleType> roles = data.getRoleTypes();
        for (int i = 0; i < roles.size(); ++i) {
            if (i == 0) {
                skill += roles.get(i).name;
            } else {
                skill += "," + roles.get(i).name;
            }
        }

        skillRequest.setText(skill);

        visit.setText(String.format("%s人报名，%s人浏览", data.applyCount, data.visitCount));
        jobId.setText(String.format("No.%d", data.getId()));

        if (data.duration > 0) {
            time.setText(Html.fromHtml(String.format("周期: <font color=\"#F6A823\">%d</font> 天", data.getDuration())));
        } else {
            time.setText("周期: 待商议");
        }

        String dayString = data.duration > 0 ? String.format("%s 天", data.duration) : "周期待商议";
        developerDay.setText(dayString);
    }

    private Spanned applyCount2Html(int count) {
        return Html.fromHtml(String.format("<font color='#222222'>%s</font> 人报名", count));
    }
}
