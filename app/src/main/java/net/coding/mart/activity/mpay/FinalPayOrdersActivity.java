package net.coding.mart.activity.mpay;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import net.coding.mart.R;
import net.coding.mart.common.BackActivity;
import net.coding.mart.common.event.PaySuccessEvent;
import net.coding.mart.common.share.AllThirdKeys;
import net.coding.mart.common.util.SimpleSHA1;
import net.coding.mart.common.widget.EmptyRecyclerView;
import net.coding.mart.common.widget.ListItem2;
import net.coding.mart.json.BaseHttpResult;
import net.coding.mart.json.BaseObserver;
import net.coding.mart.json.Network;
import net.coding.mart.json.NewBaseObserver;
import net.coding.mart.json.mpay.AlipayRecharge;
import net.coding.mart.json.mpay.Order;
import net.coding.mart.json.mpay.PayOrder;
import net.coding.mart.json.mpay.WeixinAppResult;
import net.coding.mart.json.mpay.WeixinRecharge;
import net.coding.mart.json.mpay.WithdrawRequire;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;
import java.util.ArrayList;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by chenchao on 2017/6/5.
 * 付款页面，所有付款都通过这个页面
 */
@EActivity(R.layout.activity_final_pay_orders)
public class FinalPayOrdersActivity extends BackActivity implements WithdrawInputPasswordDialog.ConfirmPassword {

    public static final String WEIXIN_PAY_INTENT = "NET.CODING.MART.JOB.PAYACTIVTY.WEIXIN_PAY_RESULT";
    public static final String WEIXIN_PAY_PARAM = "NET.CODING.MART.JOB.PAYACTIVTY.WEIXIN_PAY_PARAM";
    private static final int RESULT_PAY = 1;
    @Extra
    ArrayList<String> orderIds;
    @ViewById
    EmptyRecyclerView emptyView;
    @ViewById
    TextView mpayBalance, mulOrderAmount;
    @ViewById
    View mpayLayout, mpayDetailLayout, tipNeedRecharge, rechangeTip;
    @ViewById
    ImageView mpayCheck;
    @ViewById
    ListItem2 zhifubaoPay, weixinPay;
    View pickView;
    @ViewById
    LinearLayout ordersLayout;

    BroadcastReceiver weixinPayReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (FinalPayOrdersActivity.this.isFinishing()) {
                return;
            }

            int result = intent.getIntExtra(WEIXIN_PAY_PARAM, 1000);
            if (result == BaseResp.ErrCode.ERR_OK) {
                checkPayResult();
            }
        }
    };
    private PayOrder payOrder;

    @AfterViews
    void initFinalPayOrdersActivity() {
        emptyView.initFail("获取订单信息失败", v -> reloading());
        reloading();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        正式版去除沙箱模式
//        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);

        super.onCreate(savedInstanceState);

        IntentFilter filter = new IntentFilter(WEIXIN_PAY_INTENT);
        registerReceiver(weixinPayReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(weixinPayReceiver);
        super.onDestroy();
    }

    private void reloading() {
        emptyView.setLoading();
        Network.getRetrofit(this)
                .createPayOrders(orderIds)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NewBaseObserver<PayOrder>(this) {
                    @Override
                    public void onSuccess(PayOrder data) {
                        super.onSuccess(data);
                        payOrder = data;
                        emptyView.setLoadingSuccess(orderIds);

                        loadingSuccess();
                    }

                    @Override
                    public void onFail(int errorCode, @NonNull String error) {
                        super.onFail(errorCode, error);
                        emptyView.setLoadingFail();
                    }
                });
    }

    private void loadingSuccess() {
        ordersLayout.removeAllViews();
        for (Order item : payOrder.orders) {
            addOrderItem(item);
        }

        bindUI();
    }

    private void addOrderItem(Order order) {
        View v = getLayoutInflater().inflate(R.layout.final_pay_order_list_item, ordersLayout, false);
        ordersLayout.addView(v);
        OrderHolder hoder = new OrderHolder(v);
        hoder.bind(order);
    }

    private void bindUI() {
        mpayBalance.setText(String.format("¥%s", payOrder.account.balance));
        mulOrderAmount.setText(String.format("交易总金额 %s 元", calculateSum().toString()));
        int showRechange = needRecharge() ? View.VISIBLE : View.GONE;
        tipNeedRecharge.setVisibility(showRechange);
        rechangeTip.setVisibility(showRechange);
    }

    private boolean needRecharge() {
        return payOrder.account.balanceValue.compareTo(calculateSum()) < 0;
    }

    private BigDecimal calculateSum() {
        BigDecimal payValue = new BigDecimal(0);
        for (Order item : payOrder.orders) {
            payValue = payValue.add(new BigDecimal(item.totalFee));
        }
        return payValue;
    }

    @Click(R.id.mpayLayout)
    void mpayLayout(View v) {
        pickView = v;
        bindCheckLayout();
    }

    @Click(R.id.zhifubaoPay)
    void zhifubaoPay(View v) {
        pickView = v;
        bindCheckLayout();
    }

    @Click(R.id.weixinPay)
    void weixinPay(View v) {
        pickView = v;
        bindCheckLayout();
    }

    @Click
    void rechangeTip() {
        MPayRechargeActivity_.intent(this).startForResult(RESULT_PAY);
    }

    private void bindCheckLayout() {
        zhifubaoPay.setCheck(false);
        weixinPay.setCheck(false);
        mpayCheck.setImageResource(R.mipmap.ic_list_check_false);

        if (pickView instanceof ListItem2) {
            ((ListItem2) pickView).setCheck(true);
            mpayDetailLayout.setVisibility(View.GONE);
        } else {
            mpayCheck.setImageResource(R.mipmap.ic_list_check_true);
            mpayDetailLayout.setVisibility(View.VISIBLE);
        }
    }

    @Click
    void sendButton() {
        if (pickView == null) {
            showButtomToast("请选择一种支付方式");
            return;
        }

        if (pickView == mpayLayout) {
            payByMPay();
        } else if (pickView == weixinPay) {
            payByWeixin();
        } else if (pickView == zhifubaoPay) {
            payByZhifubao();
        }
    }

    private void payByMPay() {
        if (needRecharge()) {
            showButtomToast("开发宝余额不够，请先充值");
            return;
        }

        Network.getRetrofit(this)
                .getWithdrawRequire()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NewBaseObserver<WithdrawRequire>(this) {
                    @Override
                    public void onSuccess(WithdrawRequire data) {
                        super.onSuccess(data);
                        showSending(false);

                        if (!data.hasPassword || !data.passIdentity) {
                            new MPayAlertDialog(FinalPayOrdersActivity.this, data).show();
                        } else {
                            new WithdrawInputPasswordDialog(FinalPayOrdersActivity.this, FinalPayOrdersActivity.this)
                                    .setTitle("支付订单")
                                    .show();
                        }
                    }

                    @Override
                    public void onFail(int errorCode, @NonNull String error) {
                        super.onFail(errorCode, error);
                        showSending(false);
                    }
                });

        showSending(true);
    }

    @Override
    public void confirm(String password) {
        String sha1Password = SimpleSHA1.sha1(password);
        if (orderIds.size() == 1) {
            Network.getRetrofit(this)
                    .payByMPay(orderIds.get(0), sha1Password)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new NewBaseObserver<BaseHttpResult>(this) {

                        @Override
                        public void onSuccess(BaseHttpResult data) {
                            super.onSuccess(data);
                            showSending(false);

                            checkPayResult();
                        }

                        @Override
                        public void onFail(int errorCode, @NonNull String error) {
                            super.onFail(errorCode, error);
                            showSending(false);
                        }
                    });
        } else {
            Network.getRetrofit(this)
                    .payMulStageOrder(orderIds, sha1Password)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new NewBaseObserver<BaseHttpResult>(this) {

                        @Override
                        public void onSuccess(BaseHttpResult data) {
                            super.onSuccess(data);
                            showSending(false);

                            checkPayResult();
                        }

                        @Override
                        public void onFail(int errorCode, @NonNull String error) {
                            super.onFail(errorCode, error);
                            showSending(false);
                        }
                    });
        }

        showSending(true);
    }

    private void payByWeixin() {
        String money = calculateSum().toString();
        Network.getRetrofit(this)
                .payByWeixin(money, orderIds, "Weixin", "App")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NewBaseObserver<WeixinRecharge>(this) {
                    @Override
                    public void onSuccess(WeixinRecharge data) {
                        super.onSuccess(data);
//                        charge_id = data.orderId;
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

    private void paybyWeixinClient(WeixinRecharge recharge) {
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
        mpayLayout.postDelayed(() -> showSending(false), 2 * 1000);
    }

    private void payByZhifubao() {
        String money = calculateSum().toString();
        Network.getRetrofit(this)
                .payByAlipay(money, orderIds, "Alipay", "App")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NewBaseObserver<AlipayRecharge>(this) {
                    @Override
                    public void onSuccess(AlipayRecharge data) {
                        super.onSuccess(data);
//                        charge_id = data.orderId;
                        payByAlipayClient(data.alipayAppResult.order);
                    }

                    @Override
                    public void onFail(int errorCode, @NonNull String error) {
                        super.onFail(errorCode, error);
                        showSending(false);
                    }
                });

        showSending(true);
    }

    @OnActivityResult(RESULT_PAY)
    void onResult() {
        reloading();
    }

    @Background
    void payByAlipayClient(String payInfo) {
        // 构造PayTask 对象
        PayTask alipay = new PayTask(FinalPayOrdersActivity.this);
        // 调用支付接口，获取支付结果
        alipay.payV2(payInfo, false);

        checkPayResult();
    }

    @UiThread
    void checkPayResult() {
        Network.getRetrofit(this)
                .getOrderStatus(orderIds.get(0))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<String>(this) {
                    @Override
                    public void onSuccess(String data) {
                        super.onSuccess(data);
                        setResult(RESULT_OK);
                        showSending(false);

                        String tip = "支付成功";
                        switch (data) {
                            case "Success":
                                EventBus.getDefault().post(new PaySuccessEvent());
                                tip = "支付成功";
                                finish();
                                break;

                            case "Pending":
//                                tip = "支付中";
                                tip = "";
                                break;

                            case "Fail":
                                tip = "支付失败";
                                break;

                            case "Cancel":
                                tip = "中断了支付";
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

    public static class OrderHolder {
        private TextView orderId, orderTitle, orderPrice;

        public OrderHolder(View v) {
            this.orderId = ((TextView) v.findViewById(R.id.orderId));
            this.orderTitle = ((TextView) v.findViewById(R.id.orderTitle));
            this.orderPrice = ((TextView) v.findViewById(R.id.orderPrice));
        }

        public void bind(Order order) {
            orderId.setText(order.orderId);
            orderTitle.setText(order.title);
            orderPrice.setText(String.format("¥%s", order.totalFee));
        }
    }
}
