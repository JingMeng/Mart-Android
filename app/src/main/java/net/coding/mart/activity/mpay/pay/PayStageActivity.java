package net.coding.mart.activity.mpay.pay;

import android.widget.TextView;

import net.coding.mart.R;
import net.coding.mart.activity.mpay.FinalPayOrdersActivity_;
import net.coding.mart.common.BackActivity;
import net.coding.mart.json.mpay.Order;
import net.coding.mart.json.reward.MulStageOrder;
import net.coding.mart.json.reward.Published;
import net.coding.mart.json.reward.Stage;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

@EActivity(R.layout.activity_pay_stage)
public class PayStageActivity extends BackActivity {

    private static final int RESULT_PAY = 1;

    @Extra
    Stage stage;

    @Extra
    String present = "10";

    @Extra
    Published published;

    @Extra
    Order order;

    @Extra
    MulStageOrder mulStageOrder;

    @Extra
    boolean isAllPay = true;

    @ViewById
    TextView title, payDetail, payMoney;

    @AfterViews
    void initPayStageActivity() {
        if (mulStageOrder != null) {
            String allPayString = isAllPay ? "全部" : "剩余";
            String titleString = String.format("项目［%s］的%s阶段款项", published.id, allPayString);
            title.setText(titleString);
            String payDetailString = String.format("包含 %s 元阶段款 + %s%% 平台服务费", mulStageOrder.stageAmount, present);
            payDetail.setText(payDetailString);
            String payMoneyString = String.format("%s 元", mulStageOrder.orderAmount);
            payMoney.setText(payMoneyString);
        } else {
            String titleString = String.format("项目［%s］的阶段［%s］的款项", published.id, stage.stageNo);
            title.setText(titleString);
            String payDetailString = String.format("包含 %s 元阶段款 + %s%% 平台服务费", stage.formatPrice, present);
            payDetail.setText(payDetailString);
            String payMoneyString = String.format("%s 元", order.totalFee);
            payMoney.setText(payMoneyString);
        }
    }

    @Click
    void sendButton() {
//        if (mulStageOrder != null) {
//            PayOrderActivity_.intent(this)
//                    .mulStageOrder(mulStageOrder)
//                    .order(order)
//                    .publishJob(published)
//                    .startForResult(RESULT_PAY);
//        } else {
//            PayOrderActivity_.intent(this)
//                    .publishJob(published)
//                    .order(order)
//                    .startForResult(RESULT_PAY);
//        }

        ArrayList<String> ids = new ArrayList<>();
        if (mulStageOrder != null) {
            for (Order item : mulStageOrder.order) {
                ids.add(item.orderId);
            }
        } else {
            ids.add(order.orderId);
        }
        FinalPayOrdersActivity_.intent(this)
                .orderIds(ids)
                .startForResult(RESULT_PAY);

    }

    @OnActivityResult(RESULT_PAY)
    void onResultPay(int result) {
        if (result == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }
}
