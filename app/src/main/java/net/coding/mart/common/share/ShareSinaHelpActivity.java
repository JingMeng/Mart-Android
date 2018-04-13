package net.coding.mart.common.share;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;

public class ShareSinaHelpActivity extends Activity {

    public static final String EXTRA_SHARE_DATA = "EXTRA_SHARE_DATA";

    private CustomShareBoard.ShareData mShareData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_share_sina_help);

        mShareData = getIntent().getParcelableExtra(EXTRA_SHARE_DATA);

        addSinaWeibo();
        CustomShareBoard.performShare(SHARE_MEDIA.SINA, this, mShareData);
    }

    private void addSinaWeibo() {
//        SinaSsoHandler sinaSsoHandler = new SinaSsoHandler();
//        sinaSsoHandler.setTargetUrl(mShareData.link);
//        sinaSsoHandler.addToSocialSDK();
        UMSocialService mController = CustomShareBoard.getShareController();
        mController.getConfig().setSsoHandler(new SinaSsoHandler());
        mController.getConfig().setSinaCallbackUrl("http://sns.whalecloud.com/mart-coding/phone/callback");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMSsoHandler ssoHandler = CustomShareBoard.getShareController().getConfig().getSsoHandler(
                requestCode);
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }

        finish();
    }

}
