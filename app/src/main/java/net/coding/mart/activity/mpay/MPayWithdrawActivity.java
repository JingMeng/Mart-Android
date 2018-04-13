package net.coding.mart.activity.mpay;

import android.support.annotation.NonNull;
import android.widget.EditText;
import android.widget.TextView;

import net.coding.mart.R;
import net.coding.mart.common.BackActivity;
import net.coding.mart.common.CommonBackActivity;
import net.coding.mart.common.CommonBackActivity_;
import net.coding.mart.common.Global;
import net.coding.mart.common.util.SimpleSHA1;
import net.coding.mart.common.util.ViewStyleUtil;
import net.coding.mart.json.Network;
import net.coding.mart.json.NewBaseObserver;
import net.coding.mart.json.mpay.WithdrawAccount;
import net.coding.mart.json.mpay.WithdrawRequire;
import net.coding.mart.json.mpay.WithdrawResult;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.HashMap;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@EActivity(R.layout.activity_mpay_with_draw)
public class MPayWithdrawActivity extends BackActivity implements WithdrawInputPasswordDialog.ConfirmPassword {

    @Extra
    WithdrawRequire require;

    @ViewById
    TextView accountName, sendButton, topTip;

    @ViewById
    EditText money, remark;

    @AfterViews
    void initMPayWithdrawActivity() {
        ViewStyleUtil.editTextBindButton(sendButton, money);
        WithdrawAccount account = require.accounts.get(0);
        accountName.setText(String.format("%s（%s）", account.account, account.name));
        topTip.setText(Global.createBlueHtml("提醒：请您认真阅读", "《码市开发宝服务协议》", "，了解提现风险。"));
    }

    @Click
    void sendButton() {
        new WithdrawInputPasswordDialog(this, this).show();
    }

    @Click
    void topTip() {
        CommonBackActivity_.intent(this).type(CommonBackActivity.Type.mpay).start();
    }

    @Override
    public void confirm(String password) {
        String moneyString = money.getText().toString();

        WithdrawAccount account = require.accounts.get(0);
        Map<String, String> map = new HashMap<>();
        map.put("account", account.account);
        map.put("accountType", account.accountType);
        map.put("name", account.name);
        map.put("accountId", String.valueOf(account.id));
        map.put("price", moneyString);
        map.put("description", remark.getText().toString()); //测试
        map.put("password", SimpleSHA1.sha1(password)); //

        Network.getRetrofit(this)
                .mpayWithDraw(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NewBaseObserver<WithdrawResult>(this) {
                    @Override
                    public void onSuccess(WithdrawResult data) {
                        super.onSuccess(data);

                        setResult(RESULT_OK);
                        MPayWithdrawDynamicActivity_.intent(MPayWithdrawActivity.this)
                                .withdrawResult(data)
                                .start();

                        showSending(false);
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
