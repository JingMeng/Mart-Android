package net.coding.mart.job;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import net.coding.mart.R;
import net.coding.mart.WebActivity;
import net.coding.mart.activity.user.UserMainActivity_;
import net.coding.mart.common.CommonExtra;
import net.coding.mart.common.Global;
import net.coding.mart.common.MyData;
import net.coding.mart.common.constant.JoinStatus;
import net.coding.mart.common.constant.Progress;
import net.coding.mart.common.local.DownloadFile;
import net.coding.mart.common.local.DownloadFileImp;
import net.coding.mart.common.share.CustomShareBoard;
import net.coding.mart.common.umeng.UmengEvent;
import net.coding.mart.databinding.FragmentMainRewardDetailBasicInfoBinding;
import net.coding.mart.json.BaseObserver;
import net.coding.mart.json.JobDetail;
import net.coding.mart.json.MartRequest;
import net.coding.mart.json.Network;
import net.coding.mart.json.RoleType;
import net.coding.mart.json.reward.Published;
import net.coding.mart.json.reward.SimplePublished;
import net.coding.mart.login.LoginActivity;
import net.coding.mart.login.LoginActivity_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import java.util.regex.Matcher;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@EActivity(R.layout.activity_job_detail)
@OptionsMenu(R.menu.menu_job_detail)
public class JobDetailActivity extends WebActivity implements DownloadFile {

    private static final int RESULT_JOIN = 1;
    private final String JOIN_REWARD = "参与项目";

    @Extra
    boolean isPublishJob = false;

    Published mDatum = null;

    @Extra
    SimplePublished simplePublished;

    @Extra(CommonExtra.FinishToJump)
    CommonExtra.JumpParam finishToJump;

    @ViewById(R.id.joinButton)
    TextView mJoinButton;

    @ViewById
    FrameLayout basicInfoLayout;

    int mJobId;
    DownloadFile downloadFile;
    JobDetail mJobDetail;
    private FragmentMainRewardDetailBasicInfoBinding basicInfoBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    void initJobDetailActivity() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            invalidateOptionsMenu();
        }

        Matcher matcher = Global.pattern.matcher(url);
        if (matcher.find()) {
            String group = matcher.group(1);
            mJobId = Integer.valueOf(group);
        }

        basicInfoBinding = FragmentMainRewardDetailBasicInfoBinding.inflate(getLayoutInflater(),
                basicInfoLayout, true);
        updateUIHead();

        updateDataFromService();
    }

    @OptionsItem
    void share() {
        if (mDatum == null) {
            showButtomToast("获取项目详情失败");
            updateDataFromService();
            return;
        }
        CustomShareBoard.ShareData shareData = new CustomShareBoard.ShareData(mDatum);
        CustomShareBoard shareBoard = new CustomShareBoard(this, shareData);
        shareBoard.showAtLocation(this.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

    private void updateUIHead() {
        SimplePublished data = mDatum;
        if (data == null) {
            data = simplePublished;
        }

        if (data != null) {
            int icon = Progress.iconFromId(data.status);
            basicInfoBinding.setInfo(data);
            ImageView rewardProgress = basicInfoBinding.rewardProgress;
            if (icon == 0) {
                rewardProgress.setVisibility(View.GONE);
            } else {
                rewardProgress.setVisibility(View.VISIBLE);
                rewardProgress.setImageResource(icon);
            }

            int visibility = data.isHighPaid() ? View.VISIBLE : View.GONE;
            basicInfoBinding.basicInfoHighPay.setVisibility(visibility);
            basicInfoBinding.iconHighPay.setVisibility(visibility);
        }


        if (url != null) {
            Matcher matcher = Global.pattern.matcher(url);
            if (matcher.find()) {
                String jobIdString = matcher.group(1);
                mJobId = Integer.valueOf(jobIdString);
            }

            if (data != null) {
                updateUserinfo();
            }
        }

    }

    private void updateUI() {
        updateUIHead();

        if (isPublishJob) {
            return;
        }

        View statusButtonBar = findViewById(R.id.statusButtonBar);
        if (mDatum != null && mDatum.getStatus() == Progress.recruit
                && !MyData.isPublishUser()) {
            statusButtonBar.setVisibility(View.VISIBLE);
        } else {
            statusButtonBar.setVisibility(View.GONE);
        }

        if (mJobDetail == null) {
            return;
        }

        switch (mJobDetail.getJoinStatus()) {
            case joinStart:
                setSendButton("");
                break;

            case joinsStartSucess:
                setSendButton("");
                hideSendButton();
                break;

            case joinStartCheck:
                setSendButton("编辑");
                break;

            case josinStartFail:
                setSendButton("重新报名");
                break;
            case joinsStartCancel:
                setSendButton("重新报名");
                break;

            default: // noJoin:
                hideStatus();
                break;
        }
    }

    private void setSendButton(String buttonTitle) {
        JoinStatus joinDisplay = mJobDetail.getJoinStatus();
        String title = joinDisplay.text;
        int color = joinDisplay.bgColor;

        TextView jobStatus = (TextView) findViewById(R.id.jobStatus);
        jobStatus.setTextColor(color);
        jobStatus.setText(title);

        if (!buttonTitle.isEmpty()) {
            mJoinButton.setText(buttonTitle);
        }
    }

    private void hideStatus() {
        findViewById(R.id.jobStatusLayout).setVisibility(View.GONE);
        mJoinButton.setText(JOIN_REWARD);
    }

    private void hideSendButton() {
        mJoinButton.setVisibility(View.INVISIBLE);
    }

    private void updateDataFromService() {
        MartRequest request = Network.getRetrofit(this);
        request.getRewardDetail(mJobId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<JobDetail>(this) {
                    @Override
                    public void onSuccess(JobDetail data) {
                        super.onSuccess(data);
                        mJobDetail = data;
                        if (mJobDetail != null) {
                            mDatum = mJobDetail.reward;

                            downloadFile = new DownloadFileImp(JobDetailActivity.this, mDatum);
                        }
                        updateUI();
                    }

                });
    }

    @Override
    protected void onStart() {
        super.onStart();

        updateUserDisplay();
    }

    private void updateUserDisplay() {
        TextView tip = (TextView) findViewById(R.id.textTip);
        MyData myself = MyData.getInstance();
        tip.setVisibility(View.VISIBLE);
        if (!myself.isLogin()) {
            tip.setText("您还未登录 >>");
        } else if (!myself.canJoin()) {
            tip.setText("未完善个人资料不能参与项目，去完善 >>");
        } else {
            tip.setVisibility(View.GONE);
        }

        if (MyData.getInstance().getData().isDemand()) {
            tip.setVisibility(View.GONE);
        }
    }

    private void updateUserinfo() {
        if (MyData.isPublishUser()) {
            return;
        }

        LoginActivity.loadCurrentUser(this, new LoginActivity.LoadUserCallback() {
            @Override
            public void onSuccess() {
                updateUserDisplay();
            }

            @Override
            public void onFail() {
            }
        });
    }

    @Click(R.id.textTip)
    public void textTip(View v) {
        MyData myself = MyData.getInstance();
        if (!myself.isLogin()) {
            goToLoginActivity();
        } else if (!myself.canJoin()) {
            goToAccountMainActivity();
        }
    }

    @Click(R.id.joinButton)
    public void joinButton(View v) {
        if (mJoinButton.getText().toString().equals(JOIN_REWARD)) {
            umengEvent(UmengEvent.ACTION, "项目详情 _ 点击参与项目按钮");
        } else {
            umengEvent(UmengEvent.ACTION, "项目详情 _ 编辑申请内容");
        }

        MyData data = MyData.getInstance();
        if (!data.isLogin()) {
            goToLoginActivity();
        } else if (data.canJoin()) {
            goToJoinActivity();
        } else {
            LoginActivity.loadCurrentUser(JobDetailActivity.this, new LoginActivity.LoadUserCallback() {
                @Override
                public void onSuccess() {
                    showSending(false);
                    MyData myself = MyData.getInstance();
                    if (myself.canJoin()) {
                        goToJoinActivity();
                    } else {
                        showMiddleToast("请先完善资料");
                        goToAccountMainActivity();
                    }
                }

                @Override
                public void onFail() {
                    showSending(false);
                }
            });
            showSending(true, "");
        }
    }

    private void goToLoginActivity() {
        LoginActivity_.intent(this).start();
    }

    private void goToAccountMainActivity() {
        UserMainActivity_.intent(this).start();
    }

    private void goToJoinActivity() {
        CommonExtra.JumpParam param = finishToJump;

        boolean hasCanJoinRole = false;
        for (RoleType item : mJobDetail.roleTypes) {
            if (!item.completed) {
                hasCanJoinRole = true;
                break;
            }
        }

        if (!hasCanJoinRole) {
            showMiddleToast("角色已经招募完成");
            return;
        }

        JoinActivity_.intent(this)
                .mDatum(mJobDetail)
                .mNeedJump(param)
                .startForResult(RESULT_JOIN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_JOIN) {
            if (resultCode == RESULT_OK) {
                finish();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void download(Context context, String url) {
        if (downloadFile == null) {
            return;
        }

        downloadFile.download(context, url);
    }
}
