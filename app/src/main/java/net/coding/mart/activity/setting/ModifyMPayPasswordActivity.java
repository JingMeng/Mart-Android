package net.coding.mart.activity.setting;

import android.support.annotation.NonNull;
import android.widget.EditText;
import android.widget.TextView;

import net.coding.mart.R;
import net.coding.mart.common.BackActivity;
import net.coding.mart.common.util.SimpleSHA1;
import net.coding.mart.common.util.ViewStyleUtil;
import net.coding.mart.json.Network;
import net.coding.mart.json.SimpleObserver;
import net.coding.mart.login.InputCheck;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@EActivity(R.layout.activity_modify_mpay_password)
public class ModifyMPayPasswordActivity extends BackActivity {

    @ViewById
    EditText oldPassword, password, repassword;

    @ViewById
    TextView sendButton;

    @AfterViews
    void initModifyPayPasswordActivity() {
        ViewStyleUtil.editTextBindButton(sendButton, oldPassword, password, repassword);
    }

    @Click
    void sendButton() {
        String oldPaswordString = oldPassword.getText().toString();
        String passwordString = password.getText().toString();
        String repasswordString = repassword.getText().toString();

        if (!passwordString.equals(repasswordString)) {
            showMiddleToast("确认密码与支付密码不一致");
            return;
        } else if (!InputCheck.checkMPayPassword(this, passwordString)) {
            return;
        }

        Network.getRetrofit(this)
                .modifyPayPassword(SimpleSHA1.sha1(oldPaswordString), SimpleSHA1.sha1(passwordString))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleObserver(this) {
                    @Override
                    public void onSuccess() {
                        super.onSuccess();
                        showMiddleToast("修改支付密码成功");
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
