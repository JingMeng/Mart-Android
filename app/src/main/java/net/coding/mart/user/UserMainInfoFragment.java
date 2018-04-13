package net.coding.mart.user;


import android.support.annotation.NonNull;

import net.coding.mart.R;
import net.coding.mart.activity.user.UserSkillActivity_;
import net.coding.mart.activity.user.exam.ExamActivity_;
import net.coding.mart.activity.user.exam.ExamEntranceActivity_;
import net.coding.mart.common.BaseFragment;
import net.coding.mart.common.MyData;
import net.coding.mart.common.widget.ListItem2;
import net.coding.mart.json.BaseObserver;
import net.coding.mart.json.CurrentUser;
import net.coding.mart.json.Network;
import net.coding.mart.json.user.exam.Exam;
import net.coding.mart.login.LoginActivity;
import net.coding.mart.user.identityAuthentication.IdentityActivity_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@EFragment(R.layout.fragment_user_main_info)
public class UserMainInfoFragment extends BaseFragment {

    private static final int RESULT_ACCOUNT = 1;
    private static final int RESULT_SKILL = 2;
    private static final int RESULT_EXAMINATION = 3;
    private static final int RESULT_IDENTITY = 4;

    @ViewById
    ListItem2 accountInfo, skillInfo, identityAuthentication, examination;

    @AfterViews
    void initUserMainInfoFragment() {
        uiBindData();
//        identityAuthentication.post(() -> popGuideShare());
    }

    @Override
    public void onResume() {
        super.onResume();
        loadIdentityInfo();
    }

    @Click
    void accountInfo() {
        SetAccountActivity_.intent(this).startForResult(RESULT_ACCOUNT);
    }

    @Click
    void skillInfo() {
        CurrentUser user = MyData.getInstance().getData();
        if (!user.isFullInfo()) {
            showMiddleToast("请先完善个人信息");
            return;
        }

        UserSkillActivity_.intent(this).startForResult(RESULT_SKILL);
    }

    @Click
    void identityAuthentication() {
//        clickToolTip.onClick(null);

        CurrentUser user = MyData.getInstance().getData();
        if (!user.isFullInfo()) {
            showMiddleToast("请先完善个人信息");
            return;
        }

        IdentityActivity_.intent(this).startForResult(RESULT_IDENTITY);
    }

    @Click
    void examination() {
        // 码市测试 点击事件
//        Intent intent = new Intent(getActivity(), ConfirmBeginExmActivity.class);
//        startActivityForResult(intent, RESULT_EXAMINATION);

        Network.getRetrofit(getActivity())
                .getExam()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<Exam>(getActivity()) {
                    @Override
                    public void onSuccess(Exam data) {
                        super.onSuccess(data);
                        showSending(false);

                        if (data.isFirstExam()) {
                            ExamEntranceActivity_.intent(UserMainInfoFragment.this)
                                    .exam(data)
                                    .startForResult(RESULT_EXAMINATION);
                        } else {
                            ExamActivity_.intent(UserMainInfoFragment.this)
                                    .exam(data)
                                    .startForResult(RESULT_EXAMINATION);
                        }
                    }

                    @Override
                    public void onFail(int errorCode, @NonNull String error) {
                        super.onFail(errorCode, error);
                        showSending(false);
                    }
                });
        showSending(true);
    }

    void uiBindData() {
        CurrentUser user = MyData.getInstance().getData();
        accountInfo.setCheck(user.isFullInfo());
        skillInfo.setCheck(user.isFullSkills());
        examination.setCheck(user.getPassingSurvey());

        switch (user.user.identityStatus) {
            case CHECKED:
                // 身份认证已经通过...
                identityAuthentication.setCheck(true);
                break;
            case REJECTED:
                identityAuthentication.setRightText("认证失败", R.color.examination_result_failed_font_color);
                break;
            case CHECKING:
                identityAuthentication.setRightText("认证中", R.color.warn_or_waiting_font_color);
                break;
            default: // UNCHECKED:
                identityAuthentication.setCheck(false);
        }
    }

    private void loadIdentityInfo() {
        LoginActivity.loadCurrentUser(getActivity(), new LoginActivity.LoadUserCallback() {
            @Override
            public void onSuccess() {
                uiBindData();
            }

            @Override
            public void onFail() {
            }
        });
    }

}
