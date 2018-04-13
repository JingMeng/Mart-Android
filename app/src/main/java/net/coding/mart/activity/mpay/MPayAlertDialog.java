package net.coding.mart.activity.mpay;

import android.content.Context;
import android.text.Html;
import android.widget.TextView;

import net.coding.mart.R;
import net.coding.mart.common.widget.BaseDialog;
import net.coding.mart.json.mpay.WithdrawRequire;

/**
 * Created by chenchao on 16/8/5.
 */
public class MPayAlertDialog extends BaseDialog {

    WithdrawRequire require;

    public MPayAlertDialog(Context context, WithdrawRequire require) {
        super(context);
        this.require = require;
    }

    @Override
    protected void initView() {
        TextView message = (TextView) findViewById(R.id.message);
        TextView leftButton = (TextView) findViewById(R.id.leftButton);
        TextView rightButton = (TextView) findViewById(R.id.rightButton);

        if (!require.hasPassword && !require.passIdentity) {
            message.setText(Html.fromHtml("为了您的资金安全，您需要完成 <font color='#4289DB'>个人信息</font> 并 <font color='#4289DB'>设置交易密码</font> 后方可申请提现。"));
            leftButton.setText("个人信息");
            leftButton.setOnClickListener(clickAuthentication);
            rightButton.setText("设置交易密码");
            rightButton.setOnClickListener(clickSetMPayPassword);
        } else if (!require.hasPassword) {
            message.setText(Html.fromHtml("为了您的资金安全，您需要完成 <font color='#4289DB'>设置交易密码</font> 后方可申请提现。"));
            leftButton.setText("取消");
            leftButton.setOnClickListener(clickCancel);
            rightButton.setText("设置交易密码");
            rightButton.setOnClickListener(clickSetMPayPassword);
        } else {
            message.setText(Html.fromHtml("为了您的资金安全，您需要完善 <font color='#4289DB'>个人信息</font> 后方可申请提现。"));
            leftButton.setText("取消");
            leftButton.setOnClickListener(clickCancel);
            rightButton.setText("个人信息");
            rightButton.setOnClickListener(clickAuthentication);
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.dialog_mpay_warn_no_password;
    }
}
