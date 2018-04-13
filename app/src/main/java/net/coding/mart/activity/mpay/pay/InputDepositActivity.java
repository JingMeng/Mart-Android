package net.coding.mart.activity.mpay.pay;

import android.support.annotation.NonNull;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import net.coding.mart.R;
import net.coding.mart.activity.mpay.FinalPayOrdersActivity_;
import net.coding.mart.common.BackActivity;
import net.coding.mart.common.event.PaySuccessEvent;
import net.coding.mart.common.util.ActivityNavigate;
import net.coding.mart.json.Network;
import net.coding.mart.json.NewBaseObserver;
import net.coding.mart.json.mpay.Order;
import net.coding.mart.json.reward.Published;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

// 没有 500 订金，可以考虑删除这个页面
@Deprecated
@EActivity(R.layout.activity_input_deposit)
public class InputDepositActivity extends BackActivity {

    private static final int RESULT_PAY = 1;

    @Extra
    Published publishJob;

    @ViewById
    EditText money;

    @ViewById
    View sendButton;

    @ViewById
    TextView tip;

    Order order;

    @AfterViews
    void initInputDepositActivity() {
//        ViewStyleUtil.editTextBindButton(sendButton, money);
        sendButton.setEnabled(true);
        tip.setText(Html.fromHtml("点击『下一步』，代表您同意遵守<font color='#4289DB'>《码市用户权责条款》</font>"));
    }

    @Click
    void sendButton() {
        String input = money.getText().toString();

        double inputValue = Double.valueOf(input);
        if (inputValue < 500) {
            showMiddleToast("订金不可低于 500 元");
            return;
        }

        Network.getRetrofit(this)
                .createDepositOrder(publishJob.id, input)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NewBaseObserver<Order>(this) {
                    @Override
                    public void onSuccess(Order data) {
                        super.onSuccess(data);
                        order = data;

                        ArrayList<String> ids = new ArrayList<>();
                        ids.add(order.orderId);
                        FinalPayOrdersActivity_.intent(InputDepositActivity.this)
                                .orderIds(ids)
                                .startForResult(RESULT_PAY);
                        showSending(false);
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
    void onResultPay(int resultCode) {
        if (resultCode == RESULT_OK) {
            setResult(RESULT_OK);

            EventBus.getDefault().post(new PaySuccessEvent());
            finish();
        }
    }

    @Click
    void tip() {
        ActivityNavigate.startPublishAgreement(this);
    }
}
