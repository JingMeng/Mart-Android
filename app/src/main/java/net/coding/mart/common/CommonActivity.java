package net.coding.mart.common;

import android.support.v4.app.Fragment;
import android.view.Menu;

import net.coding.mart.R;
import net.coding.mart.activity.MainFragment;
import net.coding.mart.activity.MainFragment_;
import net.coding.mart.common.util.DialogFactory;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;

/*
 *  专门用于包装 fragment，显示一些比较独立的页面
 */
@EActivity(R.layout.activity_common)
public class CommonActivity extends BackActivity {

    public enum Type {
        HIGH_PAY
    }

    @Extra
    Type type = Type.HIGH_PAY;

    @AfterViews
    void initCommonActivity() {
        Fragment fragment = MainFragment_.builder().type(MainFragment.Type.HIGH_PAY).build();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        int menuId = 0;
        if (type == Type.HIGH_PAY) {
            menuId = R.menu.hig_pay;
        }

        if (menuId != 0) {
            getMenuInflater().inflate(menuId, menu);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @OptionsItem
    void actionHighPayTip() {
        DialogFactory.create(this, R.string.high_pay_tip_title1,
                R.string.high_pay_tip_message);
    }
}