package net.coding.mart;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.android.tpush.service.XGPushService;

import net.coding.mart.activity.MainActivity_;
import net.coding.mart.activity.guide.GuideActivity_;
import net.coding.mart.common.BaseActivity;
import net.coding.mart.common.Global;
import net.coding.mart.common.MyData;
import net.coding.mart.common.util.RxBus;
import net.coding.mart.job.JobDetailActivity_;
import net.coding.mart.json.BaseObserver;
import net.coding.mart.json.Network;
import net.coding.mart.json.mpay.OrderMapper;
import net.coding.mart.login.LoginActivity;

import org.json.JSONObject;

import java.util.regex.Matcher;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class EntranceActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrance);

        pushInXiaomi();

        if (MyData.getInstance().isLogin()) {
            LoginActivity.loadCurrentUser(this, new LoginActivity.LoadUserCallback() {
                @Override
                public void onSuccess() {
                    RxBus.getInstance().send(new RxBus.UpdateMainEvent());
                }

                @Override
                public void onFail() {
                }
            });
        }

        Network.getRetrofit(this)
                .getOrderMapper()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<OrderMapper>(this) {
                    @Override
                    public void onSuccess(OrderMapper data) {
                        super.onSuccess(data);
                        MyData.saveMPayOrderMapper(EntranceActivity.this, data);
                    }
                });

        startMainAvtivity();
    }


    private void pushInXiaomi() {
        Context context = getApplicationContext();
        Intent service = new Intent(context, XGPushService.class);
        context.startService(service);
    }

    @Override
    protected void onStart() {
        super.onStart();

        XGPushClickedResult result = XGPushManager.onActivityStarted(this);
        if (result != null) {
            String custom = result.getCustomContent();
            if (custom != null && !custom.isEmpty()) {
                try {
                    JSONObject json = new JSONObject(custom);
                    String url = json.getString("param_url");

                    Matcher matcher = Global.pattern.matcher(url);

                    if (matcher.find()) {
                        JobDetailActivity_.intent(EntranceActivity.this).url(url).start();
                    } else {
                        WebActivity_.intent(EntranceActivity.this).url(url).start();
                    }

                    finish();

                } catch (Exception e) {
                    Global.errorLog(e);
                    finish();
                }
            }
        }
    }

    private void startMainAvtivity() {
        new Handler().postDelayed(() -> {
            if (EntranceActivity.this.isFinishing()) {
                return;
            }

            if (MyData.getInstance().isLogin()) {
                MainActivity_.intent(EntranceActivity.this).start();
            } else {
                GuideActivity_.intent(EntranceActivity.this).start();
            }

            overridePendingTransition(0, 0);
            finish();
        }, 1000);
    }
}
