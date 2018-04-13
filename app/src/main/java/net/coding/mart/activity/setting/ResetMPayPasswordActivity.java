package net.coding.mart.activity.setting;

import android.support.annotation.NonNull;
import android.widget.EditText;
import android.widget.TextView;

import net.coding.mart.R;
import net.coding.mart.common.BackActivity;
import net.coding.mart.common.MyData;
import net.coding.mart.common.util.SendVerifyCodeHelp;
import net.coding.mart.common.util.SimpleSHA1;
import net.coding.mart.common.util.ViewStyleUtil;
import net.coding.mart.json.CurrentUser;
import net.coding.mart.json.Network;
import net.coding.mart.json.SimpleObserver;
import net.coding.mart.login.InputCheck;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@EActivity(R.layout.activity_reset_mpay_password)
public class ResetMPayPasswordActivity extends BackActivity {

    @ViewById
    EditText phone, password, repassword, phoneCode;

    @ViewById
    TextView sendButton, sendCode;

    private SendVerifyCodeHelp sendVerifyCodeHelp;

    @AfterViews
    void initResetPayPasswordActivity() {
        ViewStyleUtil.editTextBindButton(sendButton, phone, password, repassword, phoneCode);

        CurrentUser user = MyData.getInstance().getData();
        String phoneString = String.format("%s %s", user.getPhoneCountryCode(), user.getPhone());
        phone.setText(phoneString);
        phone.setKeyListener(null);

        sendVerifyCodeHelp = new SendVerifyCodeHelp(sendCode, phoneCode);
    }

    @Click
    void sendCode() {
        CurrentUser user = MyData.getInstance().getData();
        sendVerifyCodeHelp.begin(this, user.getPhone(), user.getPhoneCountryCode());
    }

    @Click
    void sendButton() {
        String passwordString = password.getText().toString();
        String repasswordString = repassword.getText().toString();

        if (!passwordString.equals(repasswordString)) {
            showMiddleToast("确认密码与支付密码不一致");
            return;
        } else if (!InputCheck.checkMPayPassword(this, passwordString)) {
            return;
        }

        String codeString = phoneCode.getText().toString();
        Network.getRetrofit(this)
                .resetMPayPassword(SimpleSHA1.sha1(passwordString), codeString)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleObserver(this) {
                    @Override
                    public void onSuccess() {
                        super.onSuccess();
                        showMiddleToast("设置开发宝密码成功");
                        setResult(RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onFail(int errorCode, @NonNull String error) {
                        super.onFail(errorCode, error);
                        showSending(false);
                    }
                });

        showSending(true);
    }
}
