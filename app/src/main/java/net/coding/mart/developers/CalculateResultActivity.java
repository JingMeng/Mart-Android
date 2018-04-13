package net.coding.mart.developers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.AppCompatButton;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import net.coding.mart.R;
import net.coding.mart.common.BaseActivity;
import net.coding.mart.common.Global;
import net.coding.mart.common.umeng.UmengEvent;
import net.coding.mart.developers.dialog.ExpectPriceSaveSuccessDialog;
import net.coding.mart.json.developer.CalculateResult;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by liu on 16/5/24.
 */
public class CalculateResultActivity extends BaseActivity implements View.OnClickListener {
    private CalculateResult result;
    private String codes = "";
    private String webPageCount = "";
    private String jsonStr = "";
    private int orderId;

    private static final int RESULT_SAVE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setActionBarTitle("计算结果");
        Bundle bundle = getIntent().getExtras();
        result = (CalculateResult) bundle.getSerializable("vo");
        codes = bundle.getString("codes");
        webPageCount = bundle.getString("webPageCount");
        jsonStr = bundle.getString("jsonStr");
        orderId = bundle.getInt("id");
        initView();
    }

    private void initView() {
        TextView tvPrice = (TextView) findViewById(R.id.tv_price);
        TextView tvNum = (TextView) findViewById(R.id.tv_num);
        TextView tvTime = (TextView) findViewById(R.id.tv_time);
        AppCompatButton btnSave = (AppCompatButton) findViewById(R.id.btn_save);
        AppCompatButton btnCount = (AppCompatButton) findViewById(R.id.btn_count);

        btnSave.setOnClickListener(this);
        btnCount.setOnClickListener(this);
        tvNum.setText("共有" + result.platformCount + "个平台, " + result.moduleCount + "个功能模块");
        String temp = "共有" + result.platformCount;
        ForegroundColorSpan defSpan = new ForegroundColorSpan(0xff999999);
        ForegroundColorSpan blueSpan = new ForegroundColorSpan(0xff4289DB);
        SpannableStringBuilder builder = new SpannableStringBuilder(tvNum.getText().toString());
        builder.setSpan(defSpan, 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(blueSpan, 2, temp.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(defSpan, temp.length(), (temp + "个平台, ").length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(blueSpan, (temp + "个平台, ").length(), ("共有" + result.platformCount + "个平台, " + result.moduleCount).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(defSpan, ("共有" + result.platformCount + "个平台, " + result.moduleCount).length(), ("共有" + result.platformCount + "个平台, " + result.moduleCount + "个功能模块").length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvNum.setText(builder);
        String timeStr = "预计开发周期： " + result.fromTerm + "-" + result.toTerm + "个工作日";
        SpannableStringBuilder builderTime = new SpannableStringBuilder(timeStr);
        builderTime.setSpan(defSpan, 0, ("预计开发周期： ").length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builderTime.setSpan(blueSpan, ("预计开发周期： ").length(), ("预计开发周期： " + result.fromTerm + "-" + result.toTerm).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builderTime.setSpan(defSpan, ("预计开发周期： " + result.fromTerm + "-" + result.toTerm).length(), timeStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvTime.setText(builderTime);
        tvPrice.setText(getString(R.string.developer_money) + result.fromPrice + " - " + result.toPrice);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save:
                SavePriceActivity_.intent(this)
                        .codes(codes)
                        .id(orderId)
                        .webPageCount(webPageCount)
                        .startForResult(RESULT_SAVE);
                break;

            case R.id.btn_count:
                finish();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_developer_result, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            case R.id.function:
                Intent intent = new Intent(CalculateResultActivity.this, FunctionActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("jsonStr", jsonStr);
                bundle.putSerializable("vo", result);
                bundle.putInt("type", 0);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_SAVE) {
            if (resultCode == RESULT_OK) {
                saveSuccess();

                ExpectPriceSaveSuccessDialog dialog = new ExpectPriceSaveSuccessDialog(this,
                        v -> finish());
                dialog.show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void saveSuccess() {
        try {
            JSONArray jsonArray = new JSONArray(jsonStr);
            for (int i = 0; i < jsonArray.length(); ++i) {
                JSONObject json = jsonArray.optJSONObject(i);
                String platform = json.optString("platform", "");
                String umentEvent = String.format("估价 _ %s _ 保存报价", platform);
                umengEvent(UmengEvent.ACTION, umentEvent);
            }
            umengEvent(UmengEvent.ACTION, "");
        } catch (Exception e) {
            Global.errorLog(e);
        }
    }
}
