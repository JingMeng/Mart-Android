package net.coding.mart.developers.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatButton;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jauker.widget.BadgeView;

import net.coding.mart.R;
import net.coding.mart.developers.FunctionListActivity;
import net.coding.mart.developers.adapter.DialogShopCarAdapter;
import net.coding.mart.json.developer.Quotation;

import java.util.List;

/**
 * Created by liu on 16/6/2.
 */
public class ShopCarDialog extends Dialog implements View.OnClickListener {
    private TextView tvClear;
    private TextView tvReset;
    private ImageView ivFunction;
    private AppCompatButton btnOk;
    private ListView listview;
    private TextView tvState;
    private FunctionListActivity activity;
    private BadgeView badgeView;
    private DialogShopCarAdapter adapter;

    private List<Quotation> pickedFunctions;

    public ShopCarDialog(@NonNull FunctionListActivity activity) {
        super(activity, R.style.umeng_socialize_popup_dialog_anim);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_developer_shop_car);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.BOTTOM);
        initView();
    }

    public void setPickedFunctions(List<Quotation> pickedFunctions) {
        this.pickedFunctions = pickedFunctions;
    }

    private void initView() {
        tvClear = (TextView) findViewById(R.id.tv_clear);
        tvReset = (TextView) findViewById(R.id.tv_reset);
        ivFunction = (ImageView) findViewById(R.id.iv_function);
        btnOk = (AppCompatButton) findViewById(R.id.btn_ok);
        listview = (ListView) findViewById(R.id.listview);
        tvState = (TextView) findViewById(R.id.tv_state);
        tvClear.setOnClickListener(this);
        tvReset.setOnClickListener(this);
        btnOk.setOnClickListener(this);
        ivFunction.setOnClickListener(this);

        findViewById(R.id.layoutRoot).setOnClickListener(v -> dismiss());
        findViewById(R.id.contentLayout).setClickable(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_clear:
                adapter.getData().clear();
                adapter.notifyDataSetChanged();
                updateNum();
                break;

            case R.id.tv_reset:
                pickedFunctions = activity.getPickedData();
                adapter.setData(activity.getPickedData());
                adapter.notifyDataSetChanged();
                updateNum();
                break;

            case R.id.btn_ok:
                activity.calculate();
                dismiss();
                break;

            case R.id.iv_function:
                dismiss();
                break;
        }
    }

    @Override
    public void show() {
        super.show();
        adapter = new DialogShopCarAdapter(activity, this);
        adapter.setData(pickedFunctions);
        listview.setAdapter(adapter);
        updateNum();

    }

    public void updateNum() {
        int num = 0;

        if (pickedFunctions != null) {
            for (int i = 0; i < pickedFunctions.size(); i++) {
                if (pickedFunctions.get(i).type == 4 && !pickedFunctions.get(i).title.equals("页面数量")) {
                    num++;
                }
            }
        }

        if (activity.frontCondition()) {
            if (activity.noDefaultCountCondition()) {
                enableCalculate();
            } else {
                showCountShort();
            }
        } else {
            showFrontShort();
        }

        if (badgeView == null) {
            badgeView = new BadgeView(activity);
        }
        badgeView.setTargetView(ivFunction);
        badgeView.setBackground(9, Color.parseColor("#f5a623"));
        badgeView.setBadgeGravity(Gravity.TOP | Gravity.RIGHT);
        badgeView.setBadgeCount(num);
        activity.notifyDataSetChanged();
    }

    // 显示 前端 页面不够
    private void showFrontShort() {
        tvState.setVisibility(View.VISIBLE);
        tvState.setText(R.string.huoguo_tip_select_p005_pages);
        btnOk.setTextColor(0x7fffffff);
        btnOk.setEnabled(false);
    }

    // 显示 没有选够 5 个非默认
    private void showCountShort() {
        tvState.setVisibility(View.VISIBLE);
        tvState.setText(R.string.huoguo_tip_font_count);
        btnOk.setTextColor(0xff999999);
        btnOk.setEnabled(false);
    }


    private void updateByFrontCount() {
        if (activity.getNum() < 1) {
        } else {
            enableCalculate();
        }
    }

    private void enableCalculate() {
        tvState.setVisibility(View.GONE);
        btnOk.setTextColor(0xffffffff);
        btnOk.setEnabled(true);
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

}
