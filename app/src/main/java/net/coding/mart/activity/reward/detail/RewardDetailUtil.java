package net.coding.mart.activity.reward.detail;

import android.view.View;
import android.widget.TextView;

import net.coding.mart.R;
import net.coding.mart.WebActivity_;
import net.coding.mart.common.MyData;
import net.coding.mart.common.constant.StageStatus;
import net.coding.mart.databinding.RewardDetailStageItemBinding;
import net.coding.mart.json.reward.Published;
import net.coding.mart.json.reward.Role;
import net.coding.mart.json.reward.Stage;
import net.coding.mart.json.user.Owner;

/**
 * Created by chenchao on 16/4/29.
 */
public class RewardDetailUtil {

    Stage stage;
    Role role;
    Owner owner;
    Published datum;
    StageModify activity;
    RewardDetailStageItemBinding binding;

    View.OnClickListener onClickButton1;
    View.OnClickListener onClickButton2;

    public static final long ONE_DAY = 1000 * 60 * 60 * 24;

    public RewardDetailUtil(StageModify activity, RewardDetailStageItemBinding binding, Stage stage, Role role, Published datum) {
        this.activity = activity;
        this.binding = binding;
        this.stage = stage;
        this.role = role;
        this.owner = datum.owner;
        this.datum = datum;

//        binding.expandLayout.toggle();

        onClickButton1 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };

        onClickButton2 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        };
    }

    public String getButon1String() {
        String buttonString;
        if (meIsReward()) {
            buttonString = RewardHelp.stage2Help(stage.getStatusEnum()).rewardButton1;
            if (stage.showPayButton()) {
                buttonString = BUTTON_PAY;
            }
        } else if (meIsDev()) {
            buttonString = RewardHelp.stage2Help(stage.getStatusEnum()).devButton1;
        } else {
            buttonString = RewardHelp.stage2Help(stage.getStatusEnum()).otherButton;
        }
        return buttonString;
    }

    private static final String CHECK_SUBMIT = "查看交付文档";
    private static final String CHECK_FAIL = "查看未通过原因";
    private static final String SUBMIT_DOC = "提交交付文档";
    private static final String ABANDON_SUBMIT = "撤销提交";
    private static final String NO_START = "未启动";
    private static final String STOP = "已中止";
    private static final String BUTTON_PAY = "立即支付";

    enum RewardHelp {
        waitSubmit(StageStatus.waitSubmit, "", "", ""),
        waitCheck(StageStatus.waitCheck, CHECK_SUBMIT, CHECK_SUBMIT, CHECK_SUBMIT),
        checkFail(StageStatus.checkFail, CHECK_FAIL, CHECK_FAIL, CHECK_FAIL),
        checkSuccess(StageStatus.checkSuccess, CHECK_SUBMIT, CHECK_SUBMIT, CHECK_SUBMIT),
//        payed(StageStatus., CHECK_SUBMIT, CHECK_SUBMIT, CHECK_SUBMIT),
        notStart(StageStatus.notStart, NO_START, NO_START, NO_START),
        end(StageStatus.end, STOP, STOP, STOP);


        public StageStatus stage;
        public String rewardButton1;
        public String devButton1;
        public String otherButton;

        RewardHelp(StageStatus stage, String reward, String dev, String other) {
            this.stage = stage;
            rewardButton1 = reward;
            devButton1 = dev;
            otherButton = other;
        }

        public static RewardHelp stage2Help(StageStatus stage) {
            for (RewardHelp item : RewardHelp.values()) {
                if (item.stage == stage) {
                    return item;
                }
            }

            return waitSubmit;
        }
    }

    public boolean isFinish() {
        switch (stage.getStatusEnum()) {
            case waitSubmit:
            case waitCheck:
            case checkFail:
                return false;
            default:
                return true;
        }
    }

    public int getMoneyVisable() {
        if (meIsReward() || meIsDev()) {
            return View.VISIBLE;
        }

        return View.GONE;
    }

    public int getButton1Visable() {
        String button1 = getButon1String();
        return (button1.isEmpty() || button1.equals(NO_START) || button1.equals(STOP)) ? View.GONE : View.VISIBLE;
    }

    public String getButton2String() {
        if (!meIsDev()) {
            return "";
        }

//        if (datum.isMPay()) {
//            return "";
//        }

        switch (stage.getStatusEnum()) {
            case waitSubmit:
            case checkFail:
                return SUBMIT_DOC;
            case waitCheck:
                return ABANDON_SUBMIT;
            default:
                return "";
        }
    }

    public int getButton2LayoutVisable() {
        return getButton2String().isEmpty() ? View.GONE : View.VISIBLE;
    }

    public int getButton3LayoutVisable() {
        if (!meIsReward()) {
            return View.GONE;
        }

//        if (datum.isMPay()) {
//            return View.GONE;
//        }

        switch (stage.getStatusEnum()) {
            case waitCheck:
                return View.VISIBLE;
            default:
                return View.GONE;
        }
    }

    public int getTimeTipVisable() {
        if (meIsDev() || meIsReward()) {
            if (stage.showPayButton()) {
                return View.VISIBLE;
            }
        }

        return View.GONE;
    }

    public String getTimeTipString() {
        if (stage.getStatusEnum() != StageStatus.notStart) {
            return "";
        }

        if (meIsDev()) {
            return "该阶段未支付，为保障您的利益，请在需求方支付当前阶段款后再进行阶段开发，请及时联系需求方";
        } else if (meIsReward()) {
            return "支付后才能启动此阶段";
        } else {
            return "";
        }
    }

    private String timeToRewardString(long time) {
        time /= (1000 * 3600);
        long day = time / 24;
        long hour = time % 24;
        if (day <= 0) {
            return String.format("%d小时后自动确认验收", hour);
        }

        return String.format("%d天%d小时后自动确认验收", day, hour);
    }

    private String timeToSubmitString(long time) {
        time /= (1000 * 3600);
        long day = time / 24;
        long hour = time % 24;
        if (day <= 0) {
            return String.format("还剩%d小时", hour);
        }

        return String.format("还剩%d天%d小时", day, hour);
    }

    public boolean meIsReward() {
        return owner.globalKey.equals(MyData.getInstance().getData().getGlobal_key());
    }

    public boolean meIsDev() {
        return role.isMe();
    }

    public void onClickButton1(View v) {
        String buttonText = ((TextView) v).getText().toString();
        clickButton(v, buttonText);
    }

    public void onClickButton2(View v) {
        String buttonText = ((TextView) v).getText().toString();
        clickButton(v, buttonText);
    }

    private void clickButton(View v, String buttonText) {
        if (buttonText.equals(CHECK_SUBMIT)) {
            activity.stageCheckSubmit();
            WebActivity_.intent(v.getContext())
                    .url(stage.stageFile)
                    .start();
        } else if (buttonText.equals(CHECK_FAIL)) {
            WebActivity_.intent(v.getContext())
                    .url( stage.modifyFile)
                    .start();
        } else if (buttonText.equals(SUBMIT_DOC)) {
            activity.stageSubmit(stage, binding);
        } else if (buttonText.equals(ABANDON_SUBMIT)) {
            activity.stageSubmitCancel(stage, binding);
        } else if (buttonText.equals(BUTTON_PAY)) {
            activity.stagePay(stage);
        }
    }

    public void onClickCheckSuccess(View v) {
        activity.stageSubmitSuccess(stage, binding);
    }

    public void onClickCheckFail(View v) {
        activity.stageSubmitFail(stage, binding);
    }

    public void onClickToggle(View v) {
        if (binding.expandLayout.isExpanded()) {
            binding.expandIndicate.setImageResource(R.mipmap.stage_expand_indicate_down);
        } else {
            binding.expandIndicate.setImageResource(R.mipmap.stage_expand_indicate_up);
        }

        binding.expandLayout.toggle();
    }
}
