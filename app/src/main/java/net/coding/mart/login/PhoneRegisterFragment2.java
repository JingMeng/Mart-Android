package net.coding.mart.login;

import net.coding.mart.R;
import net.coding.mart.common.BaseFragment;
import net.coding.mart.common.constant.DemandType;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;

import java.util.Map;

/**
 * Created by chenchao on 16/3/1.
 * 手机注册的第一个页面
 */

@EFragment(R.layout.fragment_phone_register_2)
public class PhoneRegisterFragment2 extends BaseFragment {

    @AfterViews
    void initPhoneRegisterFragment2() {
    }

    @Click
    void personal() {
        setAccoutType(DemandType.PERSONAL, "注册个人需求方");
    }

    @Click
    void enterprise() {
        setAccoutType(DemandType.ENTERPRISE, "注册企业需求方");
    }

    @Click
    void loginLayout() {
        getActivity().finish();
    }

    private void setAccoutType(DemandType type, String title) {
        RegisterCallback activity = (RegisterCallback) getActivity();
        Map<String, String> map = activity.getRequestParmas();
        map.put("demandType", type.name());

        activity.pop3(title);
    }
}
