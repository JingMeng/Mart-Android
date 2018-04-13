package net.coding.mart.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import net.coding.mart.R;
import net.coding.mart.activity.mpay.FinalPayOrdersActivity;
import net.coding.mart.common.share.AllThirdKeys;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wxentry);

        api = WXAPIFactory.createWXAPI(this, AllThirdKeys.WX_APP_ID, true);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }


    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        Log.d("", "weixin result onResp " + resp.errCode);

        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
//            if (resp.errCode == BaseResp.ErrCode.ERR_OK) {
            Intent intent = new Intent(FinalPayOrdersActivity.WEIXIN_PAY_INTENT);
            intent.putExtra(FinalPayOrdersActivity.WEIXIN_PAY_PARAM, 0);
                sendBroadcast(intent);
//            }
        }

        finish();
    }
}
