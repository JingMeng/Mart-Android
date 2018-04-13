package net.coding.mart.activity.setting;

import net.coding.mart.R;
import net.coding.mart.common.BackActivity;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;

@EActivity(R.layout.activity_set_mpay_password_main)
public class SetMPayPasswordMainActivity extends BackActivity {

    private static final int RESULT_SET_PASSWORD = 1;

    @Click
    void modifyPassword() {
        ModifyMPayPasswordActivity_.intent(this).startForResult(RESULT_SET_PASSWORD);
    }

    @Click
    void resetPassword() {
        ResetMPayPasswordActivity_.intent(this).startForResult(RESULT_SET_PASSWORD);
    }

    @OnActivityResult(RESULT_SET_PASSWORD)
    void onResult(int result) {
        if (result == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }
}
