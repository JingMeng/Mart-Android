package net.coding.mart.common.constant;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

/**
 * Created by chaochen on 16/7/11.
 */
public class Constant {
    public static final String ARGUMENT_TYPE = "ARGUMENT_TYPE";
    public static final String ARGUMENT_PROGRESS = "ARGUMENT_PROGRESS";
    public static final BiMap<String, String> sJobTypes = HashBiMap.create();
    public static final BiMap<String, String> sJobProgresses = HashBiMap.create();
    public static final String[] sAllJobTypes = new String[]{
            "所有类型",
            "网站",
            "APP 开发",
            "微信公众号",
            "HTML5 应用",
            "其它",
    };
    public static final String[] sAllJobTypesId = new String[]{
            "",
            "1",
            "5",
            "2",
            "3",
            "4",
    };

    public static String[] getJobTypes() {
        return sAllJobTypes;
    }

    public static String getTypeFromTitle(String title) {
        String type = sJobTypes.get(title);
        if (type == null) {
            type = "";
        }

        return type;
    }

    public static String getTitleFromType(String type) {
        String title = sJobTypes.inverse().get(type);
        if (title == null) {
            title = "";
        }

        return title;
    }

    public static String[] getJobProgresses() {
        String[] titles = new String[sJobProgresses.size()];
        return sJobProgresses.keySet().toArray(titles);
    }

//    public static class ViewHold {
//        private View rootView;
//        private ImageView icon;
//        private TextView type;
//        private TextView title;
//        private TextView price;
//        private TextView progress;
//        private TextView skillRequest;
//        private TextView jobId;
//        private TextView time;
//        private TextView jobProgress;
//        private View flag0;
//
//        private TextView buttonCannel;
//        private TextView buttonOk;
//        private TextView buttonJump;
//        private View buttonProject;
//
//        private TextView payInfo;
//
//        final int[] progressBg = new int[]{
//                0xffEEEEEE,
//                0xff3BBD79,
//                0xff2FAEEA,
//                0xffCCCCCC
//        };
//
//        final int[] progressFont = new int[]{
//                0xff666666,
//                0xffFFFFFF,
//                0xffFFFFFF,
//                0xffFFFFFF,
//        };
//
//        public ViewHold(View v, boolean resizePhoto) {
//            rootView = v;
//            title = (TextView) v.findViewById(R.id.title);
//            time = (TextView) v.findViewById(R.id.time);
//            skillRequest = (TextView) v.findViewById(R.id.skillRequest);
//            progress = (TextView) v.findViewById(R.id.progress);
//            price = (TextView) v.findViewById(R.id.price);
//            title = (TextView) v.findViewById(R.id.title);
//            type = (TextView) v.findViewById(R.id.type);
//            jobId = (TextView) v.findViewById(R.id.jobId);
//            icon = (ImageView) v.findViewById(R.id.icon);
//            jobProgress = (TextView) v.findViewById(R.id.jobProgress);
//            buttonCannel = (TextView) v.findViewById(R.id.buttonCannel);
//            buttonOk = (TextView) v.findViewById(R.id.buttonOk);
//            buttonJump = (TextView) v.findViewById(R.id.buttonJump);
//            buttonProject = v.findViewById(R.id.buttonReward);
//            flag0 = v.findViewById(R.id.payedAllFlag);
//            View payInfoView = v.findViewById(R.id.payInfo);
//            if (payInfoView instanceof TextView) {
//                payInfo = (TextView) payInfoView;
//            }
//
//            if (resizePhoto) {
//                ViewGroup.LayoutParams lp = icon.getLayoutParams();
//                int iconWidthDp = LengthUtil.getsWidthDp() - LengthUtil.getDp(v.getContext(), R.dimen.list_item_padding_h) * 2;
//                int iconWidthPix = LengthUtil.dpToPx(iconWidthDp);
//                int iconHeightPix = iconWidthPix / 2;
//                lp.width = iconWidthPix;
//                lp.height = iconHeightPix;
//                icon.setLayoutParams(lp);
//            }
//        }
//
//        public ViewHold(View v) {
//            this(v, true);
//        }
//
//        public void setData(Published data) {
//            rootView.setTag(R.id.layoutRoot, data);
//
//
//            ImageLoadTool.loadImagePublishJob(icon, data.cover);
//
//            title.setText(data.name);
//
//            String typeString = valueToKey(data.getType(), sJobTypes);
//            type.setText(typeString);
//
//            Drawable typeIcon = RewardType.iconFromType(type.getContext(), typeString);
//            type.setCompoundDrawables(typeIcon, null, null, null);
//
//            price.setText(data.getFormat_price());
//
//            Progress jobStatus = data.getStatus();
//            jobProgress.setTextColor(jobStatus.color);
//            jobProgress.setText(jobStatus.text);
//
//            rootView.setClickable(jobStatus.canJumpDetail());
//
//            //                已支付金额 < 项目总金额
////                项目状态：待审核，审核中，招募中，开发中
////                若某项目多次（一次以上）付款未付清时，立即付款按钮旁边显示未付金额
//            if (jobStatus == Progress.waitCheck ||
//                    jobStatus == Progress.checking ||
//                    jobStatus == Progress.recruit ||
//                    jobStatus == Progress.doing) {
//
//                if (0 < data.getBalance()) {
//                    buttonJump.setVisibility(View.VISIBLE);
//                } else {
//                    buttonJump.setVisibility(View.GONE);
//                }
//
//                if (!data.noPay() && 0 < data.getBalance()) { // 付过钱,但未付清
//                    payInfo.setVisibility(View.VISIBLE);
//                    payInfo.setText(Html.fromHtml(String.format("温馨提示：还剩 <font color='#FF497F'>%s</font> 未支付，请尽快支付！", data.getFormat_balance())));
//                } else {
//                    payInfo.setVisibility(View.GONE);
//                }
//            } else {
//                payInfo.setVisibility(View.GONE);
//                buttonJump.setVisibility(View.GONE);
//            }
//
//            if (0 < data.getPrice() && 0 == data.getBalance()) { // 码市经理已设置金额, 用户未付金额等于0
//                flag0.setVisibility(View.VISIBLE);
//            } else {
//                flag0.setVisibility(View.INVISIBLE);
//            }
//
//            buttonJump.setTag(data);
//            buttonOk.setTag(data);
//
//            buttonOk.setVisibility(View.VISIBLE);
//            buttonCannel.setVisibility(View.GONE);
//
//            String skill = "";
//            List<net.coding.mart.json.RoleType> roles = data.getRoleTypes();
//            for (
//                    int i = 0;
//                    i < roles.size(); ++i)
//
//            {
//                if (i == 0) {
//                    skill += roles.get(i).name;
//                } else {
//                    skill += "," + roles.get(i).name;
//                }
//            }
//
//            skillRequest.setText(skill);
//
//            jobId.setText(String.format("No.%d", data.getId()));
//            time.setText(Html.fromHtml(String.format("周期: <font color=\"#F6A823\">%d</font> 天", data.getDuration())));
//        }
//
//        private void showOneButton(TextView button, String text, JoinJob data) {
//            buttonCannel.setVisibility(View.GONE);
//            buttonOk.setVisibility(View.GONE);
//            buttonJump.setVisibility(View.GONE);
//
//            button.setTag(data);
//            button.setVisibility(View.VISIBLE);
//            button.setText(text);
//        }
//
//        private void showTwoButton(JoinJob data) {
//            buttonCannel.setVisibility(View.VISIBLE);
//            buttonOk.setVisibility(View.GONE);
//            buttonJump.setVisibility(View.VISIBLE);
//
//            buttonJump.setText("编辑");
//            buttonJump.setTag(data);
//            buttonCannel.setTag(data);
//        }
//
//        public void setData(JoinJob data) {
//            rootView.setTag(R.id.layoutRoot, data);
//            ImageLoadTool.loadImagePublishJob(icon, data.cover);
//
//            title.setText(data.title);
//
//            String typeString = valueToKey(data.type, sJobTypes);
//            type.setText(typeString);
//            Drawable typeIcon = RewardType.iconFromType(type.getContext(), typeString);
//            type.setCompoundDrawables(typeIcon, null, null, null);
//
//            price.setText(data.formatPrice);
//
//            Progress jobStatus = data.getRewardStatus();
//            final String jobTemplate = "项目状态：%s";
//            jobProgress.setText(Html.fromHtml(String.format(jobTemplate, jobStatus.text)));
//
//            rootView.setClickable(jobStatus.canJumpDetail());
//
//            JoinStatus joinState = data.getApplyStatus();
//            progress.setText(joinState.text);
//            progress.setTextColor(joinState.bgColor);
//
////            if (joinState.isSignUp()) {
////                showTwoButton(data);
////            } else if (joinState) {
////
////            }
//
//            switch (joinState) {
//                case joinStart:
//                case joinStartCheck:
//                    showTwoButton(data);
//                    break;
//
//                default:
//                    showOneButton(buttonOk, "重新报名", data);
//            }
//
//            buttonOk.setEnabled(jobStatus == Progress.recruit);
//
//
//            if (joinState.isPass()) {
//                buttonProject.setVisibility(View.VISIBLE);
//                buttonOk.setVisibility(View.GONE);
//            } else {
//                buttonProject.setVisibility(View.GONE);
//            }
//
//            String skill = "";
//            List<net.coding.mart.json.RoleType> roles = data.roleTypes;
//            for (int i = 0; i < roles.size(); ++i) {
//                if (i == 0) {
//                    skill += roles.get(i).name;
//                } else {
//                    skill += "," + roles.get(i).name;
//                }
//            }
//            skillRequest.setText(skill);
//
//            jobId.setText(String.format("No.%d", data.id));
//            time.setText(Html.fromHtml(String.format("周期: <font color=\"#F6A823\">%d</font> 天", data.duration)));
//
//
//            buttonProject.setTag(data);
//        }
//
//        public void setData(Published data) {
//            ImageLoadTool.loadImage(icon, data.cover);
//
//            title.setText(data.name);
//
//            String typeString = valueToKey(data.type, sJobTypes);
//            type.setText(typeString);
//            Drawable typeIcon = RewardType.iconFromType(type.getContext(), typeString);
//            type.setCompoundDrawables(typeIcon, null, null, null);
//
//            price.setText(data.formatPrice);
//
//            Integer status = data.getStatus().id;
//            String progressString = valueToKey(status, sJobProgresses);
//            progress.setText(progressString);
//            GradientDrawable bg = (GradientDrawable) progress.getBackground();
//            int pos = status - 4;
//            if (0 <= pos && pos <= progressFont.length) {
//                bg.setColor(progressBg[(pos)]);
//                progress.setTextColor(progressFont[(pos)]);
//            }
//
//            String skill = "";
//            List<net.coding.mart.json.RoleType> roles = data.roleTypes;
//            for (int i = 0; i < roles.size(); ++i) {
//                if (i == 0) {
//                    skill += roles.get(i).name;
//                } else {
//                    skill += "," + roles.get(i).name;
//                }
//            }
//            skillRequest.setText(skill);
//
//            jobId.setText(String.format("No.%d", data.id));
//            time.setText(Html.fromHtml(String.format("交付周期: <font color=\"#F6A823\">%d天</font>", data.duration)));
//        }
//
//        private String valueToKey(int value, Map<String, String> map) {
//            String typeString = String.valueOf(value);
//            for (String item : map.keySet()) {
//                if (typeString.equals(map.get(item))) {
//                    return item;
//                }
//            }
//
//            return "";
//        }
//
//    }
}
