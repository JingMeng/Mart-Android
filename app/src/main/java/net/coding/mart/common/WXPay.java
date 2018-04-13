package net.coding.mart.common;

import android.content.Context;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import net.coding.mart.common.share.AllThirdKeys;

/**
 * Created by chenchao on 16/5/10.
 */
public final class WXPay {

    public void regToWeixin(Context context) {
        IWXAPI api = WXAPIFactory.createWXAPI(context, AllThirdKeys.WX_APP_ID, false);
        api.registerApp(AllThirdKeys.WX_APP_ID);
    }

    public void pay(String s) {


    }

    private static WXPay wxPay;

    private WXPay() {
    }

    public static WXPay getInstance() {
        if (wxPay == null) {
            wxPay = new WXPay();
        }

        return wxPay;
    }

}
