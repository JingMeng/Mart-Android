package net.coding.mart.login;

import net.coding.mart.R;
import net.coding.mart.common.BaseFragment;
import net.coding.mart.common.constant.AccountType;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;

import java.util.Map;

/**
 * Created by chenchao on 16/3/1.
 * 手机注册的第一个页面
 */

@EFragment(R.layout.fragment_phone_register_1)
public class PhoneRegisterFragment1 extends BaseFragment {

    @AfterViews
    void initPhoneRegisterFragment1() {
        setActionBarTitle("选择身份");
    }

    @Click
    void developer() {
        setAccoutType(AccountType.DEVELOPER).pop3("注册开发者");
    }

    @Click
    void demend() {
        setAccoutType(AccountType.DEMAND).pop2();
    }

    private RegisterCallback setAccoutType(AccountType type) {
        RegisterCallback activity = (RegisterCallback) getActivity();
        Map<String, String> map = activity.getRequestParmas();
        map.clear();
        map.put("accountType", type.name());
        return activity;
    }

    @Click
    void loginLayout() {
        getActivity().finish();
    }
}
