package net.coding.mart.common;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

/**
 * Created by chenchao on 15/10/8.
 * 后退的activity
 */
public class BackActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setShowBack();
    }

    protected void setShowBack() {
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}
