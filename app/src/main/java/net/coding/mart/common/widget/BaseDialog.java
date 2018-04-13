package net.coding.mart.common.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.view.View;

import net.coding.mart.R;
import net.coding.mart.activity.setting.ResetMPayPasswordActivity_;
import net.coding.mart.user.SetAccountActivity_;

/**
 * Created by chenchao on 16/8/12.
 */
public abstract class BaseDialog extends Dialog {

    abstract protected void initView();

    abstract protected
    @LayoutRes
    int getLayoutResource();

    protected View.OnClickListener clickCancel = v -> {
        dismiss();
    };

    protected View.OnClickListener clickAuthentication = v -> {
        SetAccountActivity_.intent(getContext()).start();
        dismiss();
    };

    protected View.OnClickListener clickSetMPayPassword = v -> {
        ResetMPayPasswordActivity_.intent(getContext()).start();
        dismiss();
    };

    public BaseDialog(Context context) {
        super(context, R.style.Dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayoutResource());
        initView();
    }

}
