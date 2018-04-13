package net.coding.mart.login;

import android.support.annotation.NonNull;
import android.view.View;

import net.coding.mart.R;
import net.coding.mart.common.BaseFragment;
import net.coding.mart.common.constant.DemandType;
import net.coding.mart.common.util.ViewStyleUtil;
import net.coding.mart.common.widget.LoginEditText;
import net.coding.mart.common.widget.SendValidateCodeHelp;
import net.coding.mart.json.BaseObserver;
import net.coding.mart.json.Network;
import net.coding.mart.json.PhoneCountry;
import net.coding.mart.json.SimpleHttpResult;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import java.util.Map;
import java.util.regex.Pattern;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by chenchao on 16/3/1.
 * 手机注册的第一个页面
 */
@EFragment(R.layout.fragment_phone_register_3)
public class PhoneRegisterFragment3 extends BaseFragment {

    @ViewById
    LoginEditText globalKeyEdit, enterpriseEdit;

    @ViewById
    View loginButton, enterpriseLine;

    private SendValidateCodeHelp validateHelp;

    @AfterViews
    void initPhoneRegisterFirstFragment() {
        RegisterCallback activity = (RegisterCallback) getActivity();
        Map<String, String> map = activity.getRequestParmas();
        if (DemandType.ENTERPRISE.name().equals(map.get("demandType"))) {
            enterpriseEdit.setVisibility(View.VISIBLE);
            enterpriseLine.setVisibility(View.VISIBLE);
        } else {
            enterpriseEdit.setVisibility(View.GONE);
            enterpriseLine.setVisibility(View.GONE);
        }

        validateHelp = new SendValidateCodeHelp(this, SendValidateCodeHelp.Action.REGISTER);

        ViewStyleUtil.editTextBindButton(loginButton, validateHelp.phone, validateHelp.captchaEdit);
    }

    @Click
    void loginButton() {
        checkGlobalKey();
    }

    @OnActivityResult(SendValidateCodeHelp.RESULT_PICK_COUNTRY)
    void onResultPickCountry(int resultCode, @OnActivityResult.Extra PhoneCountry resultData) {
        validateHelp.bind(resultCode, resultData);
    }

    private void checkGlobalKey() {
        String globayKey = globalKeyEdit.getTextString();

        if (globayKey.length() < 3) {
            showMiddleToast("用户名至少为3位字符");
            return;
        }

        Pattern pattern = Pattern.compile("^[a-zA-Z]{1}[a-zA-Z0-9_-]{2,32}$");
        if (!pattern.matcher(globayKey).find()) {
            showMiddleToast("用户名须以字母开头，且只能包含字母、数字、横线及下划线");
            return;
        }

        pattern = Pattern.compile("^\\d{11}$");
        if (pattern.matcher(globayKey).find()) {
            showMiddleToast("用户名不支持连续 11 位数字");
            return;
        }

        Network.getRetrofit(getActivity())
                .gkNoUse(globayKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<Boolean>(getActivity()) {
                    @Override
                    public void onSuccess(Boolean data) {
                        super.onSuccess(data);
                        showMiddleToast("用户名已存在");
                    }

                    @Override
                    public void onFail(int errorCode, @NonNull String error) {
                        if (errorCode == SimpleHttpResult.codeFalse) {
                            checkPhoneCode();
                        } else {
                            super.onFail(errorCode, error);
                        }
                    }
                });
    }

    private void checkPhoneCode() {
        validateHelp.checkPhoneCode(new LoginActivity.LoadUserCallback() {
            @Override
            public void onSuccess() {
                RegisterCallback parent = (RegisterCallback) getActivity();
                Map<String, String> map = parent.getRequestParmas();
                map.put("phone", validateHelp.phone.getTextString());
                map.put("verificationCode", validateHelp.captchaEdit.getTextString());
                map.put("isoCode", validateHelp.pickCountry.iso_code);
                map.put("countryCode", validateHelp.pickCountry.getCountryCode());
                map.put("username", globalKeyEdit.getTextString());
                if (enterpriseEdit.getVisibility() == View.VISIBLE) {
                    map.put("name", enterpriseEdit.getTextString());
                }

                parent.pop4();
            }

            @Override
            public void onFail() {
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        validateHelp.countDownTimerStop();
    }
}
