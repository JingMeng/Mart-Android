package net.coding.mart.common.util;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import net.coding.mart.json.BaseHttpResult;
import net.coding.mart.json.Network;
import net.coding.mart.json.NewBaseObserver;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by chenchao on 16/8/1.
 * 旧版的发送短信验证码
 */
public class SendVerifyCodeHelp {

    private CountDownTimer countDownTimer;
    private TextView sendPhoneMessage;
    private View editCode;

    public SendVerifyCodeHelp(TextView sendPhoneMessage, View editCode) {
        this.sendPhoneMessage = sendPhoneMessage;
        this.editCode = editCode;
    }

    public void stop() {
        countDownTimerStop();
    }

    public void begin() {
        sendPhoneMessage.setEnabled(false);
        if (countDownTimer == null) {
            countDownTimer = new CountDownTimer(60000, 1000) {

                public void onTick(long millisUntilFinished) {
                    sendPhoneMessage.setText(millisUntilFinished / 1000 + "秒");
                }

                public void onFinish() {
                    end();
                }
            };
        }
        countDownTimer.start();
        editCode.requestFocus();
    }

    private void end() {
        sendPhoneMessage.setEnabled(true);
        sendPhoneMessage.setText("发送验证码");
    }

    public void begin(Context context, String phone, String countryCode) {
        Network.getRetrofit(context)
                .sendVerifyCode(phone, countryCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NewBaseObserver<BaseHttpResult>(context) {
                    @Override
                    public void onSuccess(BaseHttpResult data) {
                        super.onSuccess(data);
                    }

                    @Override
                    public void onFail(int errorCode, @NonNull String error) {
                        super.onFail(errorCode, error);
                        end();
                    }
                });

        begin();
    }

    private void countDownTimerStop() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer.onFinish();
        }
    }
}
