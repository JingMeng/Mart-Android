package net.coding.mart.developers.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import net.coding.mart.R;
import net.coding.mart.developers.MyPriceActivity;

/**
 * Created by liu on 16/5/24.
 */
public class ExpectPriceSaveSuccessDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private View.OnClickListener afterClick;

    public ExpectPriceSaveSuccessDialog(Context context, View.OnClickListener afterClick) {
        super(context, R.style.umeng_socialize_popup_dialog);
        this.context = context;
        this.afterClick = afterClick;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_expect_price_save_success);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        initView();
    }

    private void initView() {
        ImageView ivClose = (ImageView) findViewById(R.id.iv_close);
        AppCompatButton btnLook = (AppCompatButton) findViewById(R.id.btn_look);
        ivClose.setOnClickListener(this);
        btnLook.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                dismiss();
                afterClick.onClick(v);
                break;

            case R.id.btn_look:
                Intent intent = new Intent(context, MyPriceActivity.class);
                context.startActivity(intent);
                dismiss();
                afterClick.onClick(v);
                break;
        }
    }
}
