package net.coding.mart.login;

import android.support.annotation.NonNull;
import android.view.View;

import net.coding.mart.R;
import net.coding.mart.common.BackActivity;
import net.coding.mart.common.util.ViewStyleUtil;
import net.coding.mart.common.widget.LoginEditText;
import net.coding.mart.json.BaseObserver;
import net.coding.mart.json.Network;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.HashMap;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@EActivity(R.layout.activity_email_set_password)
public class EmailSetPasswordActivity extends BackActivity {

    @Extra
    String account = "";

    @ViewById
    LoginEditText emailEdit, captchaEdit;

    @ViewById
    View loginButton;

    @AfterViews
    void initEmailSetPasswordActivity() {
        emailEdit.setText(account);

        ViewStyleUtil.editTextBindButton(loginButton, emailEdit);

        requestCaptcha();
    }

    @Click
    void loginButton() {
        String emailString = emailEdit.getTextString();
        String captchaString = captchaEdit.getTextString();

        Map<String, String> map = new HashMap<>();
        map.put("email", emailString);
        if (captchaEdit.getVisibility() == View.VISIBLE) {
            map.put("captcha", captchaString);
        }

        Network.getRetrofit(this)
                .sendVerificationEmail(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<Boolean>(this) {
                    @Override
                    public void onSuccess(Boolean data) {
                        super.onSuccess(data);
                        setResult(RESULT_OK);
                        showMiddleToast("邮件已发送");
                        finish();
                    }

                    @Override
                    public void onFail(int errorCode, @NonNull String error) {
                        super.onFail(errorCode, error);
                        requestCaptcha();
                        showSending(false, "");
                    }
                });

        showSending(true, "");
    }

    private void requestCaptcha() {
        captchaEdit.requestCaptcha(() -> captchaEdit.setVisibility(View.VISIBLE));
    }

}
