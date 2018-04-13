package net.coding.mart.activity.user;

import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;

import net.coding.mart.R;
import net.coding.mart.common.BackActivity;
import net.coding.mart.json.BaseObserver;
import net.coding.mart.json.Network;
import net.coding.mart.setting.AboutActivity_;
import net.coding.mart.setting.HelpCenterActivity_;
import net.coding.mart.setting.NotificationActivity_;
import net.coding.mart.setting.SettingHomeActivity_;
import net.coding.mart.user.UserMainInfoFragment_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.OptionsItem;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@EActivity(R.layout.activity_user_main)
public class UserMainActivity extends BackActivity {

    private MenuItem notifyItem;

    private static final int RESULT_NOTIFY = 10;
    private static final int RESULT_LOGIN_OUT = 11;

    @AfterViews
    void initUserMain2Activity() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, UserMainInfoFragment_.builder().build())
                .commit();
        updateNotifyCount();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_main, menu);
        notifyItem = menu.findItem(R.id.action_notify);
        return super.onCreateOptionsMenu(menu);
    }

    private void updateNotifyCount() {
        Network.getRetrofit(this).getUnreadCount()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<Integer>(this) {
                    @Override
                    public void onSuccess(Integer data) {
                        if (data > 0) {
                            notifyItem.setIcon(R.mipmap.ic_notify_button_new);
                        } else {
                            notifyItem.setIcon(R.mipmap.ic_notify_button);
                        }
                    }

                    @Override
                    public void onFail(int errorCode, @NonNull String error) {
                    }
                });
    }

    @OptionsItem
    void action_notify() {
        NotificationActivity_.intent(this).startForResult(RESULT_NOTIFY);
    }

    @OptionsItem
    void action_help() {
        HelpCenterActivity_.intent(this).start();
    }

    @OptionsItem
    void action_about() {
        AboutActivity_.intent(this).start();
    }

    @OptionsItem
    void action_setting() {
        SettingHomeActivity_.intent(this).startForResult(RESULT_LOGIN_OUT);
    }

    @OnActivityResult(RESULT_NOTIFY)
    void onResultNotify() {
        updateNotifyCount();
    }

    @OnActivityResult(RESULT_LOGIN_OUT)
    void onResultLoginOut(int resultCode, @OnActivityResult.Extra boolean loginOut) {
        if (resultCode == RESULT_OK && loginOut) {
            finish();
        }
    }

}
