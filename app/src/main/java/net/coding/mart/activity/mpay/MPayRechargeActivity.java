package net.coding.mart.activity.mpay;

import com.google.gson.Gson;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ScrollView;

import com.alipay.sdk.app.PayTask;
import com.orhanobut.logger.Logger;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import net.coding.mart.R;
import net.coding.mart.common.BackActivity;
import net.coding.mart.common.Global;
import net.coding.mart.common.constant.PayType;
import net.coding.mart.common.share.AllThirdKeys;
import net.coding.mart.common.util.ViewStyleUtil;
import net.coding.mart.common.widget.ListItem2;
import net.coding.mart.json.BaseObserver;
import net.coding.mart.json.Network;
import net.coding.mart.json.NewBaseObserver;
import net.coding.mart.json.mpay.AlipayRecharge;
import net.coding.mart.json.mpay.WeixinAppResult;
import net.coding.mart.json.mpay.WeixinRecharge;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.math.BigDecimal;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@EActivity(R.layout.activity_mpay_recharge)
public class MPayRechargeActivity extends BackActivity {

    @ViewById
    ListItem2 zhifubaoPay, weixinPay;

    @ViewById
    View paymentLayout;

    @ViewById
    View nextButton;

    @ViewById
    EditText paymentAmount;

    @ViewById
    ScrollView rootView;

    private PayType payType = PayType.Zhifubao;
    private String charge_id = "";
    BroadcastReceiver weixinPayReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (MPayRechargeActivity.this.isFinishing()) {
                return;
            }

            int result = intent.getIntExtra(FinalPayOrdersActivity.WEIXIN_PAY_PARAM, 1000);
            if (result == BaseResp.ErrCode.ERR_OK) {
                checkPayResult();
            }
        }
    };

    @AfterViews
    void initMPayRechargeActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        沙箱模式
//        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);

        super.onCreate(savedInstanceState);

        IntentFilter filter = new IntentFilter(FinalPayOrdersActivity.WEIXIN_PAY_INTENT);
        registerReceiver(weixinPayReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(weixinPayReceiver);
        super.onDestroy();
    }

    @AfterViews
    void initPayActivity() {
        ViewStyleUtil.editTextBindButton(nextButton, paymentAmount);

        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            int last;

            @Override
            public void onGlobalLayout() {
                int current = rootView.getHeight();
                if (last > current) {
                    rootView.fullScroll(View.FOCUS_DOWN);
                }

                last = current;
            }
        });

        zhifubaoPay();
    }

    @Click
    void zhifubaoPay() {
        payType = PayType.Zhifubao;
        uiBindData();
    }

    @Click
    void weixinPay() {
        payType = PayType.Weixin;
        uiBindData();
    }

    @Click
    void nextButton() {
        Global.popSoftkeyboard(this, paymentAmount, false);

        String money = paymentAmount.getText().toString();
        BigDecimal input = new BigDecimal(money);
        double minChange = 1;
        if (input.compareTo(new BigDecimal(minChange)) < 0
                || 0 < input.compareTo(new BigDecimal(100000))) {
            showMiddleToast("充值金额在 1元 ~ 100,000元之间, 最多保留两位小数!");
            return;
        }

        if (payType == PayType.Zhifubao) {
            payToZhifubao();
        } else if (payType == PayType.Weixin) {
            payToWeixin();
        }

    }

    private void payToWeixin() {
        String money = paymentAmount.getText().toString();
        Network.getRetrofit(this)
                .weixinRecharge(money, "Weixin", "App")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NewBaseObserver<WeixinRecharge>(this) {
                    @Override
                    public void onSuccess(WeixinRecharge data) {
                        super.onSuccess(data);
                        charge_id = data.orderId;
                        paybyWeixinClient(data);

                    }

                    @Override
                    public void onFail(int errorCode, @NonNull String error) {
                        super.onFail(errorCode, error);
                        showSending(false);
                    }
                });

        showSending(true);
    }

    private void payToZhifubao() {
        String money = paymentAmount.getText().toString();
        Network.getRetrofit(this)
                .alipayRecharge(money, "Alipay", "App")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NewBaseObserver<AlipayRecharge>(this) {
                    @Override
                    public void onSuccess(AlipayRecharge data) {
                        super.onSuccess(data);
                        charge_id = data.orderId;
                        payByClient(data.alipayAppResult.order);
                    }

                    @Override
                    public void onFail(int errorCode, @NonNull String error) {
                        super.onFail(errorCode, error);
                        showSending(false);
                    }
                });

        showSending(true);
    }

    void paybyWeixinClient(WeixinRecharge recharge) {
        IWXAPI api = WXAPIFactory.createWXAPI(this, AllThirdKeys.WX_APP_ID, false);
        PayReq request = new PayReq();

        WeixinAppResult weixinAppResult = recharge.weixinAppResult;
        request.packageValue = weixinAppResult._package;
        request.appId = weixinAppResult.appId;
        request.sign = weixinAppResult.sign;
        request.partnerId = weixinAppResult.partnerId;
        request.prepayId = weixinAppResult.prepayId;
        request.nonceStr = weixinAppResult.nonceStr;
        request.timeStamp = weixinAppResult.timestamp;
        api.sendReq(request);

        // 微信在5.0或以下的手机上不发通知, 干脆在这里关掉
        paymentAmount.postDelayed(() -> showSending(false), 2 * 1000);
    }

    private void uiBindData() {
        nextButton.setVisibility(View.VISIBLE);
        if (payType == PayType.Weixin) {
            checkType(weixinPay);
        } else { // PayType.Zhifubao
            checkType(zhifubaoPay);
        }
        paymentLayout.setVisibility(View.VISIBLE);
    }

    private void checkType(ListItem2 listItem) {
        zhifubaoPay.setCheck(false);
        weixinPay.setCheck(false);

        listItem.setCheck(true);
    }

    @Background
    void payByClient(String payInfo) {
        // 构造PayTask 对象
        PayTask alipay = new PayTask(MPayRechargeActivity.this);
        // 调用支付接口，获取支付结果
        Map<String, String> result = alipay.payV2(payInfo, false);
        Gson gson = new Gson();
        Logger.d(gson.toJson(result));

        checkPayResult();
    }

    @UiThread
    void checkPayResult() {
        Network.getRetrofit(this)
                .getOrderStatus(charge_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<String>(this) {
                    @Override
                    public void onSuccess(String data) {
                        super.onSuccess(data);
                        setResult(RESULT_OK);
                        showSending(false);

                        String tip = "充值成功";
                        switch (data) {
                            case "Success":
                                tip = "充值成功";
                                finish();
                                break;

                            case "Pending":
//                                tip = "充值中";
                                tip = "";
                                break;

                            case "Fail":
                                tip = "充值失败";
                                break;

                            case "Cancel":
                                tip = "中断了充值";
                                break;
                            default:
                                break;
                        }

                        if (!TextUtils.isEmpty(tip)) {
                            showMiddleToast(tip);
                        }
                    }

                    @Override
                    public void onFail(int errorCode, @NonNull String error) {
//                        super.onFail(errorCode, error);
//                        showMiddleToast("充值失败");
                        showSending(false);
                    }
                });

        showSending(true);
    }
}
