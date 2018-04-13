package net.coding.mart.activity.mpay;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import net.coding.mart.R;
import net.coding.mart.activity.setting.ResetMPayPasswordActivity_;

/**
 * Created by chenchao on 16/8/5.
 */
public class WithdrawInputPasswordDialog extends Dialog {

    Activity activity;
    EditText password;

    ConfirmPassword confirmPassword;

    String title = "";
    String message = "";

    public WithdrawInputPasswordDialog(Activity activity, ConfirmPassword confirmPassword) {
        super(activity, R.style.Dialog);

        this.activity = activity;
        this.confirmPassword = confirmPassword;
    }

    public WithdrawInputPasswordDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public WithdrawInputPasswordDialog setMessage(String title) {
        this.message = title;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_mpay_input_password);
        initView();
    }

    private void initView() {
        if (!TextUtils.isEmpty(title)) {
            TextView titleView = (TextView) findViewById(R.id.title);
            titleView.setText(title);
        }

        if (!TextUtils.isEmpty(message)) {
            TextView messageView = (TextView) findViewById(R.id.message);
            messageView.setText(message);
            messageView.setVisibility(View.VISIBLE);
        }

        password = (EditText) findViewById(R.id.password);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        findViewById(R.id.leftButton).setOnClickListener(v -> dismiss());
        findViewById(R.id.rightButton).setOnClickListener(v -> {
            String passwordString = password.getText().toString();
            if (passwordString.isEmpty()) {
                return;
            }
            confirmPassword.confirm(passwordString);
            dismiss();
        });
        findViewById(R.id.forgetPassword).setOnClickListener(v -> {
            ResetMPayPasswordActivity_.intent(activity).start();
            dismiss();
        });
    }

    public interface ConfirmPassword {
        void confirm(String password);
    }
}
