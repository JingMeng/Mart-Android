package net.coding.mart.activity.user.setting;

import android.support.annotation.NonNull;
import android.widget.TextView;

import net.coding.mart.R;
import net.coding.mart.common.BackActivity;
import net.coding.mart.common.MyData;
import net.coding.mart.common.util.ViewStyleUtil;
import net.coding.mart.common.widget.LoginEditText;
import net.coding.mart.json.BaseHttpResult;
import net.coding.mart.json.CurrentUser;
import net.coding.mart.json.Network;
import net.coding.mart.json.NewBaseObserver;
import net.coding.mart.json.mart2.user.MartUser;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@EActivity(R.layout.activity_modify_work_email)
public class ModifyWorkEmailActivity extends BackActivity {

    @ViewById
    LoginEditText editEmail, editCode;

    @ViewById
    TextView loginButton;

    @ViewById
    TextView sendPhoneMessage;

    @AfterViews
    final void initModifyWorkEmailActivity() {
        ViewStyleUtil.editTextBindButton(loginButton, editEmail, editCode);
        ViewStyleUtil.editTextBindButton(sendPhoneMessage, editEmail);
    }

    @Click
    void sendPhoneMessage() {
        String phone = editEmail.getTextString();
        Network.getRetrofit(this)
                .sendValidEmail(phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NewBaseObserver<BaseHttpResult>(this) {
                    @Override
                    public void onSuccess(BaseHttpResult data) {
                        super.onSuccess(data);
                        showSending(false);
                        showMiddleToast("已发送邮件");
                    }

                    @Override
                    public void onFail(int errorCode, @NonNull String error) {
                        super.onFail(errorCode, error);
                        showSending(false);
                    }
                });
        showSending(true);
    }

    @Click
    void loginButton() {
        String email = editEmail.getTextString();
        String code = editCode.getTextString();
        Network.getRetrofit(this)
                .modifyWorkEmail(email, code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NewBaseObserver<MartUser>(this) {
                    @Override
                    public void onSuccess(MartUser data) {
                        super.onSuccess(data);
                        showSending(false);
                        CurrentUser user = MyData.getInstance().getData();
                        user.setEmail(editEmail.getText().toString());
                        MyData.getInstance().save(ModifyWorkEmailActivity.this);
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
