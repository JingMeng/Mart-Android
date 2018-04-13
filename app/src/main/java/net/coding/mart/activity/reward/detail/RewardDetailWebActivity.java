package net.coding.mart.activity.reward.detail;

import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import net.coding.mart.R;
import net.coding.mart.WebActivity;
import net.coding.mart.activity.user.setting.CannelRewardDialog;
import net.coding.mart.common.umeng.UmengEvent;
import net.coding.mart.json.BaseHttpResult;
import net.coding.mart.json.Network;
import net.coding.mart.json.NewBaseObserver;
import net.coding.mart.json.reward.Published;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@EActivity(R.layout.activity_web)
public class RewardDetailWebActivity extends WebActivity {

    @Extra
    int id;

    @Extra
    Published publishJob;

    MenuItem menuEdit;

    @AfterViews
    void initRewardDetailActivity() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.reward_detail, menu);
        menuEdit = menu.findItem(R.id.action_reward_edit);
        menuEdit.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @OptionsItem
    void action_reward_dynamic() {
        umengEvent(UmengEvent.ACTION, "项目详情 _ 动态");
        RewardActivitiesActivity_.intent(this).id(id).start();
    }

    @OptionsItem
    void action_reward_cancel() {
        new CannelRewardDialog(this, click).show();
    }

    View.OnClickListener click = v -> {
        umengEvent(UmengEvent.ACTION, "项目详情 _ 取消发布");
        String resonString = "";
        Object tag = v.getTag();
        if (tag instanceof String) {
            resonString = (String) tag;
        }
        Network.getRetrofit(this)
                .cancelReward(publishJob.getId(), resonString)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NewBaseObserver<BaseHttpResult>(this) {
                    @Override
                    public void onSuccess(BaseHttpResult data) {
                        super.onSuccess(data);
                        showSending(false, "");
                        publishJob.setStatusCannel();
                        setResult(RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onFail(int errorCode, @NonNull String error) {
                        super.onFail(errorCode, error);
                        showSending(false, "");
                    }
                });

        showSending(true, "取消中...");
    };

}
