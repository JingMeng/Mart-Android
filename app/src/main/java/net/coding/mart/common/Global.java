package net.coding.mart.common;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkBuilder;
import com.tencent.android.tpush.XGPushManager;

import net.coding.mart.R;
import net.coding.mart.WebActivity;
import net.coding.mart.WebActivity_;
import net.coding.mart.json.Network;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Pattern;

/**
 * Created by chenchao on 15/10/8.
 * 放一些全局的静态变量和静态方法
 */
public class Global {

    private static final SimpleDateFormat sFormatToday = new SimpleDateFormat("今天 HH:mm");
    private static final SimpleDateFormat sFormatThisYear = new SimpleDateFormat("MM/dd HH:mm");
    private static final SimpleDateFormat sFormatOtherYear = new SimpleDateFormat("yy/MM/dd HH:mm");
    private static final SimpleDateFormat sFormatMessageToday = new SimpleDateFormat("今天");
    private static final SimpleDateFormat sFormatMessageThisYear = new SimpleDateFormat("MM/dd");
    private static final SimpleDateFormat sFormatMessageOtherYear = new SimpleDateFormat("yy/MM/dd");
    private static SimpleDateFormat sFormatDay = new SimpleDateFormat("yyyy-MM-dd");
    private static final String LOG_PREFIX = "mart_";
    private static final int LOG_PREFIX_LENGTH = LOG_PREFIX.length();
    private static final int MAX_LOG_TAG_LENGTH = 23;
    public static String TEST_HOST = "http://mart.coding.test";
    public static String STAGING_HOST = "http://mart.coding.codingprod.net";
    //    public static String TEST_HOST = "http://192.168.0.115:9020"; // kin 的
    public static String HOST = "https://codemart.com";
    public static String HOST_API = HOST + "/api";
    public static Pattern pattern = Pattern.compile(String.format("%s/(?:(?:project)|(?:p))/(\\w*)", Global.HOST));
    private static SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public static String makeLogTag(Class cls) {
        return makeLogTag(cls.getSimpleName());
    }

    public static String makeLogTag(String str) {
        if (str.length() > MAX_LOG_TAG_LENGTH - LOG_PREFIX_LENGTH) {
            return LOG_PREFIX + str.substring(0, MAX_LOG_TAG_LENGTH - LOG_PREFIX_LENGTH - 1);
        }

        return LOG_PREFIX + str;
    }

    public static String formatDay(long time) {
        return sFormatDay.format(time);
    }

    public static void setMartHost(String host) {
        HOST = host;
        HOST_API = host + "/api";
        Network.BASE_URL = HOST_API + "/";
        pattern = Pattern.compile(String.format("%s/(?:(?:project)|(?:p))/(\\w*)", Global.HOST));
    }

    public static void addLinkCustomerService(Context context, TextView textView) {
        Link link = new Link("联系客服")
                .setTextColor(net.coding.mart.common.Color.font_blue)
                .setUnderlined(false)
                .setOnClickListener(clickedText -> WebActivity_.intent(context)
                        .url(WebActivity.MART_SUPPORT_URL)
                        .start());
        LinkBuilder.on(textView).addLink(link).build();
    }

    public static String generateRewardIdString(int id) {
        return String.format("No.%s", id);
    }

    public static String generateRewardLink(int rewardId) {
        return String.format("%s/project/%s", Global.HOST, rewardId);
    }

    public static Spanned createBlueHtml(String begin, String middle, String end) {
        return createColorHtml(begin, middle, end, "#4289DB");
    }

    public static Spanned createColorHtml(String begin, String middle, String end, String color) {
        return Html.fromHtml(String.format("%s<font color=\"%s\">%s</font>%s", begin, color, middle, end));
    }


    public static String getTimeDetail(long timeInMillis) {
//        String dataString = getDay(timeInMillis, false);
//        if (dataString == null) {
//            dataString = getWeek(timeInMillis);
//            if (dataString == null) {
//                dataString = MonthDayFormatTime.format(timeInMillis);
//            }
//        }
//
//        return String.format("%s %s", dataString, DateFormatTime.format(new Date(timeInMillis)));
        return dayToNow(timeInMillis, true);
    }

    public static String dayToNow(long time, boolean displayHour) {
        long nowMill = System.currentTimeMillis();

        long minute = (nowMill - time) / 60000;
        if (minute < 60) {
            if (minute <= 0) {
                return Math.max((nowMill - time) / 1000, 1) + "秒前"; // 由于手机时间的原因，有时候会为负，这时候显示1秒前
            } else {
                return minute + "分钟前";
            }
        }

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(time);
        int year = calendar.get(GregorianCalendar.YEAR);
        int month = calendar.get(GregorianCalendar.MONTH);
        int day = calendar.get(GregorianCalendar.DAY_OF_MONTH);

        calendar.setTimeInMillis(nowMill);
        Long timeObject = new Long(time);
        if (calendar.get(GregorianCalendar.YEAR) != year) { // 不是今年
            SimpleDateFormat sFormatOtherYear = displayHour ? Global.sFormatOtherYear : Global.sFormatMessageOtherYear;
            return sFormatOtherYear.format(timeObject);
        } else if (calendar.get(GregorianCalendar.MONTH) != month
                || calendar.get(GregorianCalendar.DAY_OF_MONTH) != day) { // 今年
            SimpleDateFormat sFormatThisYear = displayHour ? Global.sFormatThisYear : Global.sFormatMessageThisYear;
            return sFormatThisYear.format(timeObject);
        } else { // 今天
            SimpleDateFormat sFormatToday = displayHour ? Global.sFormatToday : Global.sFormatMessageToday;
            return sFormatToday.format(timeObject);
        }
    }

    public static String getExtraString(Context context) {
        String FEED_EXTRA = "";
        if (FEED_EXTRA.isEmpty()) {
            try {
                PackageInfo pInfo = context.getPackageManager().getPackageInfo("net.coding.mart", 0);
                String appVersion = pInfo.versionName;
                String phoneModel = Build.MODEL;
                FEED_EXTRA = String.format("CodingMart_Android/%s (Android %s; %s)", appVersion, Build.VERSION.SDK_INT, phoneModel);
            } catch (Exception e) {
            }
        }

        return FEED_EXTRA;
    }

    // 转换 "1471251219539" 为 2016-08-15 16:53
    public static String timeToString(String time) {
        if (TextUtils.isEmpty(time)) {
            return "";
        }

        return timeFormat.format(new Date(Long.valueOf(time)));
    }

    public static void initWebView(WebView webView) {
        webView.getSettings().setJavaScriptEnabled(true);

        // 防止webview滚动时背景变成黑色
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            webView.setBackgroundColor(0x00000000);
        } else {
            webView.setBackgroundColor(Color.argb(1, 0, 0, 0));
        }

        webView.getSettings().setDefaultTextEncodingName("UTF-8");
    }

    public static boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activityNetwork = mConnectivityManager.getActiveNetworkInfo();
            return activityNetwork != null && activityNetwork.getType() == ConnectivityManager.TYPE_WIFI;
        }
        return false;
    }

    public static boolean isConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    static public String readTextFile(Context context, String assetFile) throws IOException {
        InputStream inputStream = context.getAssets().open(assetFile);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte buf[] = new byte[1024];
        int len;
        while ((len = inputStream.read(buf)) != -1) {
            outputStream.write(buf, 0, len);
        }
        outputStream.close();
        inputStream.close();

        return outputStream.toString();
    }

    public static void errorLog(Exception e) {
        if (e == null) {
            return;
        }

        e.printStackTrace();
        Log.e("", "" + e);
    }

    public static String getErrorMsg(JSONObject jsonObject) {
        String s = "";
        try {
            JSONObject jsonData = jsonObject.getJSONObject("msg");
            String key = jsonData.keys().next();
            s = jsonData.getString(key);
        } catch (Exception e) {
            Global.errorLog(e);
        }

        return s;
    }

    public static void bindXG(Context context) {
        boolean needPush = MyData.getNeedPush(context);

        if (MyData.getInstance().isLogin() && needPush) {
            String globalKey = MyData.getInstance().getData().getGlobal_key();
            XGPushManager.registerPush(context, globalKey);
        } else {
            XGPushManager.registerPush(context, "*");
        }
    }

    public static void copy(Context context, String url) {
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(url);
    }

    public static void popSoftkeyboard(Context ctx, View view, boolean wantPop) {
        InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (wantPop) {
            view.requestFocus();
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        } else {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;

    }

    public static void popMulSelectDialog(Context context, String title, ArrayAdapter adapter, AdapterView.OnItemClickListener clickListView, View.OnClickListener clickOk) {
        final View layout = LayoutInflater.from(context).inflate(R.layout.list_view_pick_skill, null);
        GridView listView = (GridView) layout.findViewById(R.id.gridView);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(clickListView);

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title).setView(layout);

        final AlertDialog alertDialog = builder.show();
        layout.findViewById(R.id.buttonOk).setOnClickListener(v -> {
            alertDialog.dismiss();
            clickOk.onClick(v);
        });

        layout.findViewById(R.id.buttonCannel).setOnClickListener(v -> alertDialog.dismiss());
    }

    public static void jumpToMarket(Context context) {
        try {
            Uri uri = Uri.parse("market://details?id=net.coding.mart");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(context, "软件市场里暂时没有找到码市", Toast.LENGTH_SHORT).show();
        }
    }
}
