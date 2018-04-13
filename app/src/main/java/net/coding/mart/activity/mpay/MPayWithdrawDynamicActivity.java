package net.coding.mart.activity.mpay;

import android.text.Html;
import android.view.View;
import android.widget.TextView;

import net.coding.mart.R;
import net.coding.mart.common.BackActivity;
import net.coding.mart.common.Global;
import net.coding.mart.json.mpay.Order;
import net.coding.mart.json.mpay.WithdrawAccountSimple;
import net.coding.mart.json.mpay.WithdrawResult;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;


@EActivity(R.layout.activity_mpay_withdraw_dynamic)
public class MPayWithdrawDynamicActivity extends BackActivity {

    @Extra
    WithdrawResult withdrawResult;

    @ViewById
    TextView money, zhifubaoAccount;

    @AfterViews
    void initMPayWithdrawDynamicActivity() {
        Order order = withdrawResult.order;
        String moneyString  = String.format("提现金额：<font color='#F6A823'>¥%s</font>",
                order.totalFee);
        money.setText(Html.fromHtml(moneyString));

        WithdrawAccountSimple account = withdrawResult.account;
        String accountString = String.format("支付宝帐号：<font color='#666666'>%s（%s）</font>",
                account.account, account.accountName);
        zhifubaoAccount.setText(Html.fromHtml(accountString));

        String timeString = Global.timeToString(order.createdAt);
        bindTime(R.id.layout10, timeString);
        bindTime(R.id.layout20, timeString);

    }

    private void bindTime(int layoutId, String time) {
        View layout = findViewById(layoutId);
        if (layout != null) {
            TextView textView = (TextView) layout.findViewById(R.id.time);
            textView.setText(time);
        }
    }

}
