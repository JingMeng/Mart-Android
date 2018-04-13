package net.coding.mart.activity.reward;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import net.coding.mart.R;
import net.coding.mart.common.BackActivity;
import net.coding.mart.common.umeng.UmengEvent;
import net.coding.mart.developers.HuoguoEntranceFragment_;
import net.coding.mart.developers.MyPriceActivity;
import net.coding.mart.developers.SavePriceActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EActivity(R.layout.activity_huoguo)
@OptionsMenu(R.menu.main2)
public class HuoguoActivity extends BackActivity {

    @AfterViews
    void initHuoguoActivity() {
        Fragment fragment = HuoguoEntranceFragment_.builder().build();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, fragment)
                .commit();
    }

    @OptionsItem
    void actionMyHuoguoList() {
        umengEvent(UmengEvent.ACTION, "估价 _ 我的报价列表");
        Intent intent = new Intent(this, MyPriceActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void clseEvent(String message) {
        if (message.equals(SavePriceActivity.HUOGUO_SAVE_PRICE) && !isFinishing()) {
            finish();
        }
    }
}
