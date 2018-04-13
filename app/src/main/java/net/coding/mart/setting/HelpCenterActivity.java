package net.coding.mart.setting;

import android.content.Intent;

import net.coding.mart.FeedbackActivity;
import net.coding.mart.R;
import net.coding.mart.WebActivity;
import net.coding.mart.WebActivity_;
import net.coding.mart.common.BackActivity;
import net.coding.mart.common.Global;
import net.coding.mart.common.umeng.UmengEvent;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_help_center)
public class HelpCenterActivity extends BackActivity {


    @Click
    protected void feedback() {
        Intent intent = new Intent(this, FeedbackActivity.class);
        startActivity(intent);

        umengEvent(UmengEvent.USER_CENTER, "反馈");
    }

    @Click
    protected void faq() {
        startWebActivity("常见问题", "common");
    }

    @Click
    protected void martIntroduce() {
        startWebActivity("码市入门", "guide");

    }

    @Click
    protected void employer() {
        startWebActivity("我是需求方", "customer");

    }

    @Click
    protected void developer() {
        startWebActivity("我是开发者", "developer");

    }

    private void startWebActivity(String title, String path) {
        final String urlHost = Global.HOST + "/help?type=";

        String url = urlHost + path;
        Intent intent = new Intent(this, WebActivity_.class);
        intent.putExtra(WebActivity.EXTRA_TITLE, title);
        intent.putExtra(WebActivity.EXTRA_URL, url);
        startActivity(intent);
    }
}
