package net.coding.mart.common.widget;

import android.app.Activity;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import net.coding.mart.R;
import net.coding.mart.common.BaseActivity;
import net.coding.mart.common.BaseFragment;
import net.coding.mart.json.BaseHttpResult;
import net.coding.mart.json.Network;
import net.coding.mart.json.NewBaseObserver;
import net.coding.mart.json.PhoneCountry;
import net.coding.mart.json.SimpleHttpResult;
import net.coding.mart.login.CountryPickActivity_;
import net.coding.mart.login.InputCheck;
import net.coding.mart.login.LoginActivity;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by chenchao on 2017/5/5.
 * 发送验证码
 */
public class SendValidateCodeHelp {

    public static final int RESULT_PICK_COUNTRY = 1001;
    public LoginEditText phone;
    public TextView sendCode;
    public LoginEditText captchaEdit;
    public TextView countryCode;
    public PhoneCountry pickCountry = PhoneCountry.getChina();
    private int resultCode = RESULT_PICK_COUNTRY;
    private CountDownTimer countDownTimer;

    private Action action;

    private BaseActivity activity;

    private BaseFragment fragment;

    public SendValidateCodeHelp(BaseFragment fragment, Action action) {
        this(fragment, fragment.getBaseActivity(), action);
    }

    public SendValidateCodeHelp(BaseActivity activity, Action action) {
        this(null, activity, action);
    }

    public SendValidateCodeHelp(BaseFragment fragment, BaseActivity activity, Action action) {
        this.fragment = fragment;
        this.activity = activity;
        this.action = action;

        View root;
        if (fragment != null) {
            root = fragment.getView();
        } else {
            root = activity.findViewById(android.R.id.content);
        }
        countryCode = (TextView) root.findViewById(R.id.countryCode);
        phone = (LoginEditText) root.findViewById(R.id.emailEdit);
        sendCode = (TextView) root.findViewById(R.id.sendCode);
        captchaEdit = (LoginEditText) root.findViewById(R.id.captchaEdit);

        countryCode.setOnClickListener(v -> {
            if (this.fragment != null) {
                CountryPickActivity_.intent(this.fragment).startForResult(this.resultCode);
            } else {
                CountryPickActivity_.intent(this.activity).startForResult(this.resultCode);
            }
        });

        sendCode.setOnClickListener(v -> {
            String phone = this.phone.getTextString();
            if (!InputCheck.checkPhone(activity, phone)) {
                return;
            }

            Network.getRetrofit(activity)
                    .phoneNoUse(phone, pickCountry.getCountryCode(), pickCountry.iso_code)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .flatMap(simpleHttpResult -> {
                        if (simpleHttpResult.code != SimpleHttpResult.codeFalse
                                && simpleHttpResult.code != 0) {
                            activity.showMiddleToast(simpleHttpResult.getErrorMessage());
                        } else {
                            if (action == Action.REGISTER || action == Action.OLDAPI) {
                                if (simpleHttpResult.code == SimpleHttpResult.codeFalse) {
                                    return getBaseHttpResultObservable();
                                } else {
                                    activity.showMiddleToast("手机号已使用");
                                }

                            } else {
                                if (simpleHttpResult.code == 0) {
                                    return getBaseHttpResultObservable();
                                } else {
                                    activity.showMiddleToast("未注册的手机号");
                                }
                            }
                        }
                        return null;
                    })
                    .subscribe(new NewBaseObserver<BaseHttpResult>(activity) {
                        @Override
                        public void onSuccess(BaseHttpResult data) {
                            super.onSuccess(data);
                            activity.showMiddleToast("已发送短信");
                        }

                        @Override
                        public void onFail(int errorCode, @NonNull String error) {
                            super.onFail(errorCode, error);
                            countDownTimerStop();
                        }
                    });
        });

        countryCode.setText(pickCountry.getCountryCode());
    }

    private Observable<BaseHttpResult> getBaseHttpResultObservable() {
        countDownTimerStart();
        String phone = this.phone.getTextString();
        if (action == Action.OLDAPI) {
            return Network.getRetrofit(activity)
                    .sendVerifyCode(phone, pickCountry.getCountryCode())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        } else {
            return Network.getRetrofit(activity)
                    .sendValidateCodeV2(phone, pickCountry.getCountryCode(), pickCountry.iso_code)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    }

    public void bind(int resultCode, PhoneCountry resultData) {
        if (resultCode == Activity.RESULT_OK && resultData != null) {
            pickCountry = resultData;
            countryCode.setText(pickCountry.getCountryCode());
        }
    }

    public void checkPhoneCode(LoginActivity.LoadUserCallback callback) {
        if (action == Action.OLDAPI) {
            throw new RuntimeException("old api can't use validateCodeV2 ");
        }

        String phone = this.phone.getTextString();
        String code = captchaEdit.getTextString();

        Network.getRetrofit(activity)
                .validateCodeV2(phone,
                        pickCountry.getCountryCode(),
                        pickCountry.iso_code,
                        code,
                        action.name())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NewBaseObserver<BaseHttpResult>(activity) {
                    @Override
                    public void onSuccess(BaseHttpResult data) {
                        super.onSuccess(data);
                        callback.onSuccess();
                    }

                    @Override
                    public void onFail(int errorCode, @NonNull String error) {
                        super.onFail(errorCode, error);
                        callback.onFail();
                    }
                });
    }

    public void countDownTimerStop() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer.onFinish();
        }
    }

    private void countDownTimerStart() {
        sendCode.setEnabled(false);
        if (countDownTimer == null) {
            countDownTimer = new CountDownTimer(60000, 1000) {

                public void onTick(long millisUntilFinished) {
                    sendCode.setText(String.format("%s秒", millisUntilFinished / 1000));
                }

                public void onFinish() {
                    sendCode.setEnabled(true);
                    sendCode.setText("发送验证码");
                }
            };
        }
        countDownTimer.start();
        captchaEdit.editText.requestFocus();
    }

    public enum Action {
        NONE_ACTION,
        LOGIN, // 登录
        LOGOUT, // 登出
        REGISTER, // 注册
        PASSWORD, // 修改密码
        OLDAPI
    }
}
