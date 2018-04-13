package net.coding.mart.activity.setting.enterprise;

import net.coding.mart.R;
import net.coding.mart.common.BackActivity;
import net.coding.mart.common.MyData;
import net.coding.mart.common.util.RxBus;
import net.coding.mart.common.widget.ListItem2;
import net.coding.mart.json.CurrentUser;
import net.coding.mart.login.LoginActivity;
import net.coding.mart.user.SetAccountActivity_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_enterprise_main)
public class EnterpriseMainActivity extends BackActivity {

    @ViewById
    ListItem2 accountInfo, enterpriceInfo;

    @AfterViews
    void initEnterpriseMainActivity() {
        bindUI();
    }

    @Override
    public void onResume() {
        super.onResume();
        LoginActivity.loadCurrentUser(this, new LoginActivity.LoadUserCallback() {
            @Override
            public void onSuccess() {
                RxBus.getInstance().send(new RxBus.UpdateMainEvent());
                bindUI();
            }

            @Override
            public void onFail() {
            }
        });

    }

    private void bindUI() {
        CurrentUser me = MyData.getInstance().getData();
        accountInfo.setCheck(me.isFullInfo());
        enterpriceInfo.setCheck(me.isPassEnterpriceIdentity());
    }

    @Click
    void accountInfo() {
        SetAccountActivity_.intent(this).start();
    }

    @Click
    void enterpriceInfo() {
        if (MyData.getInstance().getData().isFullInfo()) {
            EnterpriseIdentityActivity_.intent(this).start();
        } else {
            showMiddleToast("请先完善账户信息！");
        }
    }

}
