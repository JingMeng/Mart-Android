package net.coding.mart.activity.mpay;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import net.coding.mart.R;
import net.coding.mart.common.BackActivity;
import net.coding.mart.common.Color;
import net.coding.mart.common.Global;
import net.coding.mart.common.constant.OrderStatus;
import net.coding.mart.databinding.ActivityMpayDynamicListDetailBinding;
import net.coding.mart.json.Network;
import net.coding.mart.json.NewBaseObserver;
import net.coding.mart.json.mpay.Order;
import net.coding.mart.json.mpay.WithdrawResult;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OnActivityResult;

import java.util.ArrayList;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@EActivity(R.layout.activity_mpay_dynamic_list_detail)
public class MPayDynamicListDetail extends BackActivity {

    private static final int RESULT_PAY = 1;

    @Extra
    Order order;

    private ActivityMpayDynamicListDetailBinding binding;

    @AfterViews
    void initMPayDynamicListDetail() {
        binding = ActivityMpayDynamicListDetailBinding.bind(findViewById(R.id.layoutRoot));
        binding.setData(order);

        if (order.isRewardPrepaymentPending()) {
            binding.sendButton.setVisibility(View.VISIBLE);
            ArrayList<String> orderIds = new ArrayList<>();
            orderIds.add(order.orderId);
            binding.sendButton.setOnClickListener(v -> FinalPayOrdersActivity_.intent(this)
                    .orderIds(orderIds)
                    .startForResult(RESULT_PAY));
        } else {
            binding.sendButton.setVisibility(View.GONE);
        }

        if (order.isWithDrawNoSystem()) {
            loadWithdraw();
        }
    }

    @OnActivityResult(RESULT_PAY)
    void onResultPay(int resultCode) {
        if (resultCode == RESULT_OK) {
            setResult(RESULT_OK);

            order.status = OrderStatus.Success.name();
            binding.setData(order);
            binding.sendButton.setVisibility(View.GONE);

            Intent intent = new Intent();
            intent.putExtra("resultData", order);
            setResult(RESULT_OK, intent);
        }
    }

    void loadWithdraw() {
        Network.getRetrofit(this)
                .orderDetail(order.orderId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NewBaseObserver<WithdrawResult>(this) {
                    @Override
                    public void onSuccess(WithdrawResult data) {
                        super.onSuccess(data);

                        if (data.order == null) { // 系统提现是没有进度的
                            return;
                        }

                        binding.withdrawLayout.setVisibility(View.VISIBLE);
                        View v = getLayoutInflater().inflate(R.layout.activity_mpay_dynamic_list_detail_withdraw,
                                binding.withdrawLayout, false);
                        Holder holder = new Holder(v);

                        String timeCreateString = Global.timeToString(data.order.createdAt);
                        holder.time1.setText(timeCreateString);
                        holder.time2.setText(timeCreateString);
                        holder.time3.setVisibility(View.GONE);

                        switch (data.order.getStatus()) {
                            case Success:
                                holder.setColor(R.mipmap.withdraw_small_success_10,
                                        R.mipmap.withdraw_small_success_20,
                                        R.mipmap.withdraw_small_success_30,
                                        Color.LINE_WITHDRAW_SUCCESS,
                                        Color.LINE_WITHDRAW_SUCCESS);
                                holder.time3.setVisibility(View.VISIBLE);
                                holder.time3.setText(Global.timeToString(data.order.updatedAt));
                                break;
                            case Pending:
                                holder.setColor(R.mipmap.withdraw_small_success_10,
                                        R.mipmap.withdraw_small_success_20,
                                        R.mipmap.withdraw_small_30,
                                        Color.LINE_WITHDRAW_SUCCESS,
                                        Color.LINE_WITHDRAW);
                                break;

                            default: // fail cancel
                                holder.setColor(R.mipmap.withdraw_small_fail_10,
                                        R.mipmap.withdraw_small_fail_20,
                                        R.mipmap.withdraw_small_fail_30,
                                        Color.LINE_WITHDRAW_FAIL,
                                        Color.LINE_WITHDRAW_FAIL);
                                holder.title3.setText(data.order.getStatusString());
                                holder.time3.setVisibility(View.VISIBLE);
                                holder.time3.setText(Global.timeToString(data.order.updatedAt));

                                break;
                        }

                        if (data.account != null) {
                            holder.zhifubaoAccount.setText(String.format("%s (%s)", data.account.account, data.account.accountName));
                            holder.typeAccount.setText("支付宝账号");
                        } else if (data.invoice != null) {
                            holder.typeAccount.setText("发票编号");
                            holder.zhifubaoAccount.setText(data.invoice.invoiceNo);
                        } else {
                            holder.typeAccount.setText("");
                            holder.zhifubaoAccount.setText("");
                        }
                        binding.withdrawLayout.addView(v);
                    }

                });
    }

    private static class Holder {
        View line1;
        View line2;
        ImageView circle1;
        ImageView circle2;
        ImageView circle3;
        TextView title1;
        TextView time1;
        TextView title2;
        TextView time2;
        TextView title3;
        TextView time3;
        TextView zhifubaoAccount;
        TextView typeAccount;

        public Holder(View view) {
            line1 = view.findViewById(R.id.line1);
            line2 = view.findViewById(R.id.line2);
            circle1 = (ImageView) view.findViewById(R.id.circle1);
            circle2 = (ImageView) view.findViewById(R.id.circle2);
            circle3 = (ImageView) view.findViewById(R.id.circle3);
            title1 = (TextView) view.findViewById(R.id.title1);
            time1 = (TextView) view.findViewById(R.id.time1);
            title2 = (TextView) view.findViewById(R.id.title2);
            time2 = (TextView) view.findViewById(R.id.time2);
            title3 = (TextView) view.findViewById(R.id.title3);
            time3 = (TextView) view.findViewById(R.id.time3);
            zhifubaoAccount = (TextView) view.findViewById(R.id.zhifubaoAccount);
            typeAccount = (TextView) view.findViewById(R.id.typeAccount);
        }

        public void setColor(int d1, int d2, int d3, int c1, int c2) {
            circle1.setImageResource(d1);
            circle2.setImageResource(d2);
            circle3.setImageResource(d3);
            line1.setBackgroundColor(c1);
            line2.setBackgroundColor(c2);
        }
    }
}
