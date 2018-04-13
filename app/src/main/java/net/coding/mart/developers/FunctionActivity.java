package net.coding.mart.developers;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import net.coding.mart.R;
import net.coding.mart.common.BaseActivity;
import net.coding.mart.common.share.CustomShareBoard;
import net.coding.mart.json.BaseObserver;
import net.coding.mart.json.Network;
import net.coding.mart.json.developer.CalculateResult;
import net.coding.mart.json.developer.CategoryVO;
import net.coding.mart.json.developer.FunctionResult;
import net.coding.mart.json.developer.Item;
import net.coding.mart.json.developer.ModuleFront;
import net.coding.mart.json.developer.ModuleVO;
import net.coding.mart.json.developer.PlatformVO;
import net.coding.mart.json.developer.ShareVO;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.util.EncodingUtils;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by liu on 16/5/24.
 */
public class FunctionActivity extends BaseActivity {
    private int id;
    private TextView tvPrice;
    private TextView tvTime;
    private LinearLayout llProjName;
    private TextView tvName;
    private TextView tvDesc;
    private WebView webView;
    private int type = 0;
    private CalculateResult result;
    private String jsonStr = null;
    private String shareLink = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer_function);
        setActionBarTitle("功能清单");
        Bundle bundle = getIntent().getExtras();
        type = bundle.getInt("type", 0);
        if (type == 0) {
            result = (CalculateResult) bundle.getSerializable("vo");
            jsonStr = bundle.getString("jsonStr");
        } else {
            id = bundle.getInt("id");
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        initView();
        if (type == 1) {
            getData();
        }
    }

    private void initView() {
        tvPrice = (TextView) findViewById(R.id.tv_price);
        tvTime = (TextView) findViewById(R.id.tv_time);
        llProjName = (LinearLayout) findViewById(R.id.ll_proj_name);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvDesc = (TextView) findViewById(R.id.tv_desc);
        webView = (WebView) findViewById(R.id.webview);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        if (type == 1) {
            llProjName.setVisibility(View.VISIBLE);
        } else {
            llProjName.setVisibility(View.GONE);
            tvPrice.setText(getString(R.string.developer_money) + " " + result.fromPrice + " - " + result.toPrice + " 元");
            ForegroundColorSpan defSpan = new ForegroundColorSpan(0xff999999);
            ForegroundColorSpan blueSpan = new ForegroundColorSpan(0xff4289DB);
            String timeStr = "预计开发周期： " + result.fromTerm + " - " + result.toTerm + "个工作日";
            SpannableStringBuilder builderTime = new SpannableStringBuilder(timeStr);
            builderTime.setSpan(defSpan, 0, ("预计开发周期： ").length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            builderTime.setSpan(blueSpan, ("预计开发周期： ").length(), ("预计开发周期： " + result.fromTerm + " - " + result.toTerm).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            builderTime.setSpan(defSpan, ("预计开发周期： " + result.fromTerm + " - " + result.toTerm).length(), timeStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvTime.setText(builderTime);
            String temp = "";
            try {
                InputStream in = getResources().getAssets().open("function.html"); // 从Assets中的文件获取输入流
                int length = in.available(); // 获取文件的字节数
                byte[] buffer = new byte[length]; // 创建byte数组
                in.read(buffer); // 将文件中的数据读取到byte数组中
                temp = EncodingUtils.getString(buffer, "UTF-8"); // 将byte数组转换成指定格式的字符串
            } catch (IOException e) {
                e.printStackTrace(); // 捕获异常并打印
            } catch (Exception e) {
                e.printStackTrace();
            }
            temp = temp.replace("${webview_content}", jsonStr);
            webView.loadDataWithBaseURL(null, temp, "text/html", "charset=UTF-8", null);
        }

    }


    private void getData() {
        showSending(true);
        Network.getRetrofit(this)
                .getFunctionResult(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<FunctionResult>(this) {
                    @Override
                    public void onSuccess(FunctionResult data) {
                        showSending(false);
                        tvPrice.setText(getString(R.string.developer_money) + " " + data.quote.fromPrice + " - " + data.quote.toPrice + " 元");
                        ForegroundColorSpan defSpan = new ForegroundColorSpan(0xff999999);
                        ForegroundColorSpan blueSpan = new ForegroundColorSpan(0xff4289DB);
                        String timeStr = "预计开发周期： " + data.quote.fromTerm + " - " + data.quote.toTerm + "个工作日";
                        SpannableStringBuilder builderTime = new SpannableStringBuilder(timeStr);
                        builderTime.setSpan(defSpan, 0, ("预计开发周期： ").length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        builderTime.setSpan(blueSpan, ("预计开发周期： ").length(), ("预计开发周期： " + data.quote.fromTerm + " - " + data.quote.toTerm).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        builderTime.setSpan(defSpan, ("预计开发周期： " + data.quote.fromTerm + " - " + data.quote.toTerm).length(), timeStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        tvTime.setText(builderTime);
                        tvName.setText(data.quote.name);
                        tvDesc.setText(data.quote.description);
                        List<PlatformVO> platformVOList = new ArrayList<>();
                        for (int i = 0; i < data.items.size(); i++) {
                            if (data.items.get(i).type == 1) {
                                Item item = data.items.get(i);
                                PlatformVO vo = new PlatformVO();
                                vo.setPlatform(item.title);
                                List<String> childCodes = getCodes(item.code, data.items);
                                List<Object> categoryVOList = new ArrayList<>();
                                for (int j = 0; j < childCodes.size(); j++) {
                                    CategoryVO categoryVO = new CategoryVO();
                                    Item item1 = getItem(childCodes.get(j), data.items);
                                    categoryVO.setName(item1.title);
                                    List<String> childCodes1 = getCodes(item1.code, data.items);
                                    List<ModuleVO> moduleVOList = new ArrayList<>();
                                    for (int k = 0; k < childCodes1.size(); k++) {
                                        ModuleVO moduleVO = new ModuleVO();
                                        Item item2 = getItem(childCodes1.get(k), data.items);
                                        moduleVO.setName(item2.title);
                                        List<String> childCodes2 = getCodes(childCodes1.get(k), data.items);
                                        List<String> stringList = new ArrayList<>();
                                        for (int n = 0; n < childCodes2.size(); n++) {
                                            Item item3 = getItem(childCodes2.get(n), data.items);
                                            stringList.add(item3.title);
                                        }
                                        if (stringList.size() != 0) {
                                            moduleVO.setFunction(stringList);
                                            moduleVOList.add(moduleVO);
                                        }
                                    }
                                    categoryVO.setModule(moduleVOList);
                                    categoryVOList.add(categoryVO);
                                }
                                vo.setCategory(categoryVOList);
                                platformVOList.add(vo);

                                if (data.items.get(i).isFrontPlatform() && data.quote != null) {
                                    ModuleFront moduleFront = new ModuleFront();
                                    moduleFront.setCount(data.quote.webPageCount + "");
                                    categoryVOList.add(moduleFront);
                                }
                            }
                        }

                        if (data.quote != null) {

                        }
                        String temp = "";
                        try {
                            InputStream in = getResources().getAssets().open("function.html"); // 从Assets中的文件获取输入流
                            int length = in.available(); // 获取文件的字节数
                            byte[] buffer = new byte[length]; // 创建byte数组
                            in.read(buffer); // 将文件中的数据读取到byte数组中
                            temp = EncodingUtils.getString(buffer, "UTF-8"); // 将byte数组转换成指定格式的字符串
                        } catch (IOException e) {
                            e.printStackTrace(); // 捕获异常并打印
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        temp = temp.replace("${webview_content}", new Gson().toJson(platformVOList));
                        webView.loadDataWithBaseURL(null, temp, "text/html", "charset=UTF-8", null);
                    }

                    @Override
                    public void onFail(int errorCode, @NonNull String error) {
                        super.onFail(errorCode, error);
                        showSending(false);
                    }

                });
    }

    private List<String> getCodes(String code, List<Item> data) {
        List<String> codes = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            if (code.equals(data.get(i).parentCode)) {
                codes.add(data.get(i).code);
            }
        }
        return codes;
    }

    private Item getItem(String code, List<Item> data) {
        Item item = null;
        for (int i = 0; i < data.size(); i++) {
            if (code.equals(data.get(i).code)) {
                item = data.get(i);
            }
        }
        return item;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (type == 1) {
            getMenuInflater().inflate(R.menu.menu_developer_function, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            case R.id.share:
                if (!TextUtils.isEmpty(shareLink)) {
                    CustomShareBoard.ShareData shareData = new CustomShareBoard.ShareData(shareLink);
                    CustomShareBoard shareBoard = new CustomShareBoard(this, shareData);
                    shareBoard.showAtLocation(this.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
                } else {
                    showSending(true);
                    Network.getRetrofit(this)
                            .getShareLink(id)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new BaseObserver<ShareVO>(this) {
                                @Override
                                public void onSuccess(ShareVO data) {
                                    showSending(false);
                                    shareLink = data.getShareLink();
                                    CustomShareBoard.ShareData shareData = new CustomShareBoard.ShareData(shareLink);
                                    CustomShareBoard shareBoard = new CustomShareBoard(FunctionActivity.this, shareData);
                                    shareBoard.showAtLocation(FunctionActivity.this.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
                                }

                                @Override
                                public void onFail(int errorCode, @NonNull String error) {
                                    super.onFail(errorCode, error);
                                    showSending(false);
                                }

                            });
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
