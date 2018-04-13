package net.coding.mart.login;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import net.coding.mart.R;
import net.coding.mart.common.BackActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

@EActivity(R.layout.activity_phone_register)
public class PhoneRegisterActivity extends BackActivity implements RegisterCallback {

    public static final String REGIST_TIP = "点击注册，即表示同意<font color=\"#4289db\">《码市用户服务协议》</font>";
    private final String firstTitle = "选择身份";
    private Map<String, String> requestParams = new HashMap<>();

    Stack<String> titleStatk = new Stack<>();

    @AfterViews
    final void initPhoneRegisterActivity() {
        titleStatk.push(firstTitle);
        setActionBarTitle(firstTitle);
        popFragment(PhoneRegisterFragment1_.builder().build());
    }

    @Override
    public Map<String, String> getRequestParmas() {
        return requestParams;
    }

    @Override
    public void pop2() {
        saveTitle();

        String title = "选择需求方类型";
        setActionBarTitle(title);
        popFragment(PhoneRegisterFragment2_.builder().build(), true);
    }

    private String saveTitle() {
        return titleStatk.push(getSupportActionBar().getTitle().toString());
    }

    @Override
    public void pop3(String title) {
        saveTitle();
        setActionBarTitle(title);
        popFragment(PhoneRegisterFragment3_.builder().build());
    }

    @Override
    public void pop4() {
        saveTitle();

        popFragment(PhoneRegisterFragment4_.builder().build());
    }

    private void popFragment(Fragment fragment, boolean showAnima) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (showAnima) {
//            transaction.setCustomAnimations(R.anim.activity_in_from_right, 0, 0, R.anim.activity_out_to_right);
            transaction.setCustomAnimations(R.anim.umeng_socialize_fade_in, 0, 0, R.anim.umeng_socialize_fade_out);
//            transaction.setTransition(FragmentTransaction.TRANSIT_ENTER_MASK);
        }

        transaction.replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void popFragment(Fragment fragment) {
        popFragment(fragment, false);
    }

    @Override
    public void onBackPressed() {
        int fragmentCount = getSupportFragmentManager().getBackStackEntryCount();
        if (fragmentCount <= 1) {
            finish();
        } else {
            if (!titleStatk.isEmpty()) {
                String title = titleStatk.pop();
                setActionBarTitle(title);
            }

            super.onBackPressed();
        }
    }
}
