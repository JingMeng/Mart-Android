package net.coding.mart;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.orhanobut.logger.Logger;

import net.coding.mart.activity.MainActivity_;
import net.coding.mart.activity.reward.detail.subway.SubwayView;
import net.coding.mart.activity.reward.detail.v2.V2RewardDetailActivity_;
import net.coding.mart.common.Global;
import net.coding.mart.json.BaseObserver;
import net.coding.mart.json.Network;
import net.coding.mart.json.mpay.OrderMapper;
import net.coding.mart.json.reward.Published;
import net.coding.mart.json.reward.RewardDetail;
import net.coding.mart.json.reward.RewardWapper;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

//import net.coding.mart.activity.reward.detail.RewardDetailActivity_;
//import net.coding.mart.developers.dialog.LoginDialog_;

public class TestActivity extends AppCompatActivity {

    private static final String TAG_TEST_ACTIVITY = "TAG_TEST_ACTIVITY";

    public static final String TAG = Global.makeLogTag(TestActivity.class);

    private SubwayView subwayView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

//        LocalFileActivity_.intent(this).start();

        V2RewardDetailActivity_.intent(this)
                .id(3176)
                .start();
        finish();

    }

    private void updateStage() {
        Network.getRetrofit(this)
                .getRewardDetail2(1786)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<RewardDetail>(this) {
                    @Override
                    public void onSuccess(RewardDetail data) {
                        super.onSuccess(data);
//                        subwayView.setMetro(data.metro);
                    }
                });
    }

    public void click0(View v) {
        MainActivity_.intent(this).start();
    }

    public void click1(View v) {
//        Network.getRetrofit(this, Network.CacheType.useCache)
//                .getMyPublishList(0)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new BaseObserver<RewardWapper<Published>>(this) {
//                    @Override
//                    public void onSuccess(RewardWapper<Published> data) {
//                        super.onSuccess(data);
//
//                        Log.d(TAG, "publis ok");
//                    }
//
//                    @Override
//                    public void onFail(int errorCode, @NonNull String error) {
//                        super.onFail(errorCode, error);
//
//                        Log.d(TAG, "publis fail");
//                    }
//                });

//        LocalFileActivity_.intent(this).start();

        Network.getRetrofit(this)
                .getOrderMapper()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<OrderMapper>(this) {
                    @Override
                    public void onSuccess(OrderMapper data) {
                        super.onSuccess(data);

                        Logger.d(String.format("orderType %s, timeOptions  %s", data.orderType.size(), data.timeOptions.size()));
                    }

                    @Override
                    public void onFail(int errorCode, @NonNull String error) {
                        super.onFail(errorCode, error);
                    }
                });
    }

    public void click2(View v) {
//        DialogFactory.create(this, R.layout.dialog_cannel_reward);

        Network.getRetrofit(this, Network.CacheType.onlyCache)
                .getMyPublishList(0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<RewardWapper<Published>>(this) {
                    @Override
                    public void onSuccess(RewardWapper<Published> data) {
                        super.onSuccess(data);

                        Log.d(TAG, "publis ok");
                    }

                    @Override
                    public void onFail(int errorCode, @NonNull String error) {
                        super.onFail(errorCode, error);

                        Log.d(TAG, "publis fail");
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
//            LoginDialog_.intent(this).start();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
