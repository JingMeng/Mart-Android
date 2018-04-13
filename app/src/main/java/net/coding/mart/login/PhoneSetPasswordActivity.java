package net.coding.mart.login;

import android.support.v4.app.Fragment;

import net.coding.mart.R;
import net.coding.mart.common.BackActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;

import java.util.HashMap;
import java.util.Map;

@EActivity(R.layout.activity_phone_set_password)
public class PhoneSetPasswordActivity extends BackActivity implements SetPasswordCallback {

    @Extra
    String account = "";

    private Map<String, String> requestParams = new HashMap<>();

    @AfterViews
    final void initPhoneSetPasswordActivity() {
        Fragment fragment = PhoneVerifyFragment_.builder().account(account).build();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, fragment)
                .commit();
    }

    @Override
    public Map<String, String> getRequestParmas() {
        return requestParams;
    }

    @Override
    public void next() {
        Fragment fragment = PhoneSetPasswordFragment_.builder().build();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStackImmediate();
        } else {
            finish();
        }
    }
}
