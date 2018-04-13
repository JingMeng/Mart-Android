package net.coding.mart.activity.mpay;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;

import net.coding.mart.R;
import net.coding.mart.common.BackActivity;
import net.coding.mart.common.util.ViewStyleUtil;
import net.coding.mart.json.Network;
import net.coding.mart.json.NewBaseObserver;
import net.coding.mart.json.mpay.SingleWithdrawAccount;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@EActivity(R.layout.activity_set_withdraw_account)
public class SetWithdrawAccountActivity extends BackActivity {

    @ViewById
    EditText name, alipay;

    @ViewById
    View sendButton;

    @AfterViews
    void initSetWithdrawAccountActivity() {
        ViewStyleUtil.editTextBindButton(sendButton, name, alipay);


        Network.getRetrofit(this)
                .getWithdrawAccount()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

                .subscribe(new NewBaseObserver<SingleWithdrawAccount>(this) {
                    @Override
                    public void onSuccess(SingleWithdrawAccount data) {
                        super.onSuccess(data);

                        if (data.account != null) {
                            name.setText(data.account.name);
                            alipay.setText(data.account.account);
                        }
                    }
                });
    }

    @Click
    void sendButton() {
        String nameString = name.getText().toString();
        String alipayString = alipay.getText().toString();

        Network.getRetrofit(this)
                .setWithdrawAccount(nameString, alipayString)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NewBaseObserver<SingleWithdrawAccount>(this) {
                    @Override
                    public void onSuccess(SingleWithdrawAccount data) {
                        super.onSuccess(data);
                        showSending(false);

                        setResult(RESULT_OK);

                        showMiddleToast("提现账户绑定提交成功");
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
