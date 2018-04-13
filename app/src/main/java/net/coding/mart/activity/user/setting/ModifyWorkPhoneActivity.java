package net.coding.mart.activity.user.setting;

import android.support.annotation.NonNull;
import android.widget.TextView;

import net.coding.mart.R;
import net.coding.mart.common.BackActivity;
import net.coding.mart.common.MyData;
import net.coding.mart.common.util.ViewStyleUtil;
import net.coding.mart.common.widget.SendValidateCodeHelp;
import net.coding.mart.json.Network;
import net.coding.mart.json.NewBaseObserver;
import net.coding.mart.json.PhoneCountry;
import net.coding.mart.json.mart2.user.MartUser;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@EActivity(R.layout.activity_modify_work_phone)
public class ModifyWorkPhoneActivity extends BackActivity {

    @ViewById
    TextView loginButton;

    private SendValidateCodeHelp validateHelp;

    @AfterViews
    final void initValidePhoneActivity() {
        setActionBarTitle(R.string.title_activity_modify_work_phone);

        validateHelp = new SendValidateCodeHelp(this, SendValidateCodeHelp.Action.OLDAPI);

        ViewStyleUtil.editTextBindButton(loginButton, validateHelp.phone, validateHelp.captchaEdit);
    }

    @OnActivityResult(SendValidateCodeHelp.RESULT_PICK_COUNTRY)
    void onResultPickCountry(int resultCode, @OnActivityResult.Extra PhoneCountry resultData) {
        validateHelp.bind(resultCode, resultData);
    }

    @Override
    protected void onStop() {
        super.onStop();
        validateHelp.countDownTimerStop();
    }

    @Click
    void loginButton() {
        PhoneCountry pickCountry = validateHelp.pickCountry;
        Network.getRetrofit(this)
                .modifyPhone(validateHelp.phone.getTextString(),
                        pickCountry.getCountryCode(),
                        pickCountry.iso_code,
                        validateHelp.captchaEdit.getTextString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NewBaseObserver<MartUser>(this) {
                    @Override
                    public void onSuccess(MartUser data) {
                        super.onSuccess(data);
                        showSending(false);

                        MyData.getInstance().update(ModifyWorkPhoneActivity.this, data);

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
