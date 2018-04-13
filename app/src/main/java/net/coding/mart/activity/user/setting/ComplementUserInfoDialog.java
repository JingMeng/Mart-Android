package net.coding.mart.activity.user.setting;

import android.content.Context;
import android.text.Html;
import android.widget.TextView;

import net.coding.mart.R;
import net.coding.mart.common.widget.BaseDialog;

/**
 * Created by chenchao on 16/8/12.
 */
public class ComplementUserInfoDialog extends BaseDialog {

    public ComplementUserInfoDialog(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.dialog_mpay_warn_no_password;
    }

    @Override
    protected void initView() {
        TextView message = (TextView) findViewById(R.id.message);
        TextView leftButton = (TextView) findViewById(R.id.leftButton);
        TextView rightButton = (TextView) findViewById(R.id.rightButton);

        message.setText(Html.fromHtml("您需要完善 <font color='#4289DB'>个人信息</font> 后方可进入开发宝。"));
        leftButton.setText("取消");
        leftButton.setOnClickListener(clickCancel);
        rightButton.setText("个人信息");
        rightButton.setOnClickListener(clickAuthentication);
    }
}
