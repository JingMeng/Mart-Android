package net.coding.mart.login;


import android.app.Activity;
import android.support.annotation.NonNull;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import net.coding.mart.R;
import net.coding.mart.common.BaseActivity;
import net.coding.mart.common.BaseFragment;
import net.coding.mart.common.umeng.UmengEvent;
import net.coding.mart.common.util.ActivityNavigate;
import net.coding.mart.common.util.RxBus;
import net.coding.mart.common.util.SimpleSHA1;
import net.coding.mart.common.util.ViewStyleUtil;
import net.coding.mart.common.widget.LoginEditText;
import net.coding.mart.common.widget.SingleToast;
import net.coding.mart.json.Network;
import net.coding.mart.json.NewBaseObserver;
import net.coding.mart.json.mart2.user.MartUser;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@EFragment(R.layout.fragment_phone_register_4)
public class PhoneRegisterFragment4 extends BaseFragment {

    @ViewById
    LoginEditText passwordEdit, repasswordEdit, captchaEdit;

    @ViewById
    TextView loginButton, textClause;

    @AfterViews
    final void initPhoneSetPasswordFragment() {
        ViewStyleUtil.editTextBindButton(loginButton, passwordEdit, repasswordEdit, captchaEdit);
        needShowCaptch();

        textClause.setText(Html.fromHtml(PhoneRegisterActivity.REGIST_TIP));
    }

    private void needShowCaptch() {
        captchaEdit.requestCaptcha(() -> captchaEdit.setVisibility(View.VISIBLE));
    }

    @Click
    void loginButton() {
        String password = passwordEdit.getText().toString();
        String repassword = repasswordEdit.getText().toString();
        String captcha = captchaEdit.getText().toString();
        if (password.length() < 6) {
            SingleToast.showMiddleToast(getActivity(), "密码至少为6位");
            return;
        } else if (64 < password.length()) {
            SingleToast.showMiddleToast(getActivity(), "密码不能大于64位");
            return;
        } else if (!password.equals(repassword)) {
            SingleToast.showMiddleToast(getActivity(), "两次输入的密码不一致");
            return;
        }

        Map<String, String> params = ((RegisterCallback) getActivity()).getRequestParmas();

        if (captchaEdit.getVisibility() == View.VISIBLE) {
            params.put("captcha", captcha);
        }

        String sha1 = SimpleSHA1.sha1(password);
        params.put("password", sha1);
        params.put("rePassword", sha1);
        params.put("protocol", String.valueOf(true));
        params.put("step", String.valueOf(3));

        Network.getRetrofit(getActivity())
                .register(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NewBaseObserver<MartUser>(getActivity()) {
                    @Override
                    public void onSuccess(MartUser data) {
                        super.onSuccess(data);
                        umengEvent(UmengEvent.ACTION, "点击完成注册按钮");
                        LoginActivity.syncWebviewCookie(getActivity());
                        loadCurrentUser();
                    }

                    @Override
                    public void onFail(int errorCode, @NonNull String error) {
                        super.onFail(errorCode, error);
                        needShowCaptch();
                        ((BaseActivity) getActivity()).showSending(false, "");
                    }
                });

        ((BaseActivity) getActivity()).showSending(true, "");
    }

    @Click
    void textClause() {
        ActivityNavigate.startServiceTerm(getActivity());
    }

    protected void loadCurrentUser() {
        LoginActivity.loadCurrentUser(getActivity(), new LoginActivity.LoadUserCallback() {
            @Override
            public void onSuccess() {
                RxBus.getInstance().send(new RxBus.UpdateMainEvent());
                closeActivity();
            }

            @Override
            public void onFail() {
                closeActivity();
            }
        });
    }

    private void closeActivity() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }
}
