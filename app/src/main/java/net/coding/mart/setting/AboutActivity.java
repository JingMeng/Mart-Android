package net.coding.mart.setting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.text.Html;
import android.view.Gravity;
import android.widget.TextView;

import net.coding.mart.R;
import net.coding.mart.common.BackActivity;
import net.coding.mart.common.Global;
import net.coding.mart.common.share.CustomShareBoard;
import net.coding.mart.common.umeng.UmengEvent;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_about)
public class AboutActivity extends BackActivity {

    @ViewById
    TextView phone;

    @AfterViews
    void initAbountActivity() {
        String s = "联系电话：<font color='#4289db'>400-992-1001<font>";
        phone.setText(Html.fromHtml(s));

        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String versionName = pInfo.versionName;

            String versionString = String.format("V %s", versionName);
            TextView version = (TextView) findViewById(R.id.version);
            version.setText(versionString);

        } catch (Exception e) {
            Global.errorLog(e);
        }
    }

    @Click
    void phone() {
        callPhone(AboutActivity.this);
    }

    public static void callPhone(Activity activity) {
        dialPhoneNumber(activity, "400-992-1001");
    }

    public static void dialPhoneNumber(Activity activity, String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(intent);
        }
    }

    @Click
    void email() {
        contactByEmail(this);
    }

    public static void contactByEmail(Context context) {
        composeEmail(context, new String[]{"support@codemart.com"});
    }

    public static void composeEmail(Context context, String[]addresses) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }

//    public void composeEmail(String addresses) {
//        Intent intent = new Intent(Intent.ACTION_SEND);
//        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
//        if (intent.resolveActivity(getPackageManager()) != null) {
//            startActivity(intent);
//        }
//    }

    @Click
    void checkUpdate() {
        umengEvent(UmengEvent.ACTION, "个人中心 _关于码市 _ 检查更新");

//        Intent intent = new Intent(AboutActivity.this, UpdateService.class);
//        intent.putExtra(UpdateService.EXTRA_BACKGROUND, false);
//        startService(intent);

        Global.jumpToMarket(this);
    }

    @Click
    void recommendMart() {
        umengEvent(UmengEvent.ACTION, "个人中心 _ 关于码市 _ 推荐码市");

        CustomShareBoard.ShareData shareData = new CustomShareBoard.ShareData();
        CustomShareBoard shareBoard = new CustomShareBoard(this, shareData, CustomShareBoard.ShareType.Mart);
        shareBoard.showAtLocation(this.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

    @Click
    void marketMark() {
        umengEvent(UmengEvent.ACTION, "个人中心 _ 关于码市 _ 去评分");
        Global.jumpToMarket(this);

        umengEvent(UmengEvent.USER_CENTER, "去评分");
    }

}
