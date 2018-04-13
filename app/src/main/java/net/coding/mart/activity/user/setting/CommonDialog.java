package net.coding.mart.activity.user.setting;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import net.coding.mart.R;
import net.coding.mart.common.widget.BaseDialog;

/**
 * Created by chenchao on 16/8/12.
 */
public class CommonDialog extends BaseDialog {

    Builder builder;

    public CommonDialog(Context context, Builder builder) {
        super(context);
        this.builder = builder;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.dialog_mpay_warn_no_password;
    }

    @Override
    protected void initView() {
        TextView title = (TextView) findViewById(R.id.title);
        TextView message = (TextView) findViewById(R.id.message);
        TextView leftButton = (TextView) findViewById(R.id.leftButton);
        TextView rightButton = (TextView) findViewById(R.id.rightButton);

        title.setText(builder.title);
        message.setText(builder.message);

        if (!builder.hideLeftButton) {
            leftButton.setText(builder.left);
            leftButton.setOnClickListener(builder.leftClick == null ? clickCancel : v -> {
                builder.leftClick.onClick(v);
                clickCancel.onClick(v);
            });
        } else {
            findViewById(R.id.buttonDivide0).setVisibility(View.GONE);
            leftButton.setVisibility(View.GONE);
        }

        rightButton.setText(builder.right);
        rightButton.setOnClickListener(builder.rightClick == null ? clickCancel : v -> {
            builder.rightClick.onClick(v);
            clickCancel.onClick(v);
        });

        setCanceledOnTouchOutside(builder.canceledOnTouchOutside);
    }

    public static class Builder {

        String left = "取消";
        String right = "确定";
        String title = "提示";
        CharSequence message = "";
        boolean hideLeftButton = false;
        Context context;
        boolean canceledOnTouchOutside = true;

        View.OnClickListener leftClick;
        View.OnClickListener rightClick;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setLeftButton(String left, View.OnClickListener leftClick) {
            this.left = left;
            this.leftClick = leftClick;
            return this;
        }

        public Builder setRightButton(String right, View.OnClickListener rightClick) {
            this.right = right;
            this.rightClick = rightClick;
            return this;
        }

        public Builder setHideLeftButton(boolean hide) {
            this.hideLeftButton = hide;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setMessage(CharSequence message) {
            this.message = message;
            return this;
        }

        public Builder setCanceledOnTouchOutside(boolean canceledOnTouchOutside) {
            this.canceledOnTouchOutside = canceledOnTouchOutside;
            return this;
        }

        public void show() {
            new CommonDialog(context, this).show();
        }
    }
}
