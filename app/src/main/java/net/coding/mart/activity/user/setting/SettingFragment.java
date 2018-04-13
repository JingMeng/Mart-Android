package net.coding.mart.activity.user.setting;

import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import net.coding.mart.R;
import net.coding.mart.activity.MainActivity;
import net.coding.mart.activity.mpay.MPayDynamicListActivity_;
import net.coding.mart.activity.setting.enterprise.EnterpriseMainActivity_;
import net.coding.mart.activity.user.UserMainActivity_;
import net.coding.mart.common.BaseActivity;
import net.coding.mart.common.BaseFragment;
import net.coding.mart.common.ImageLoadTool;
import net.coding.mart.common.MyData;
import net.coding.mart.common.umeng.UmengEvent;
import net.coding.mart.common.util.RxBus;
import net.coding.mart.common.widget.ListItem1;
import net.coding.mart.json.BaseObserver;
import net.coding.mart.json.CurrentUser;
import net.coding.mart.json.Network;
import net.coding.mart.json.mpay.OrderMapper;
import net.coding.mart.login.LoginActivity;
import net.coding.mart.setting.AboutActivity_;
import net.coding.mart.setting.HelpCenterActivity_;
import net.coding.mart.setting.NotificationActivity_;
import net.coding.mart.setting.SettingHomeActivity_;
import net.coding.mart.user.SetAccountActivity_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by chenchao on 16/7/7.
 * 个人中心
 */

@EFragment(R.layout.fragment_main_setting)
public class SettingFragment extends BaseFragment {

    @ViewById
    ListItem1 userMPay;

    @ViewById
    TextView userInfoText1, userInfoText2;

    @ViewById
    ImageView userInfoIcon1, userInfoIcon2;

    @ViewById
    TextView userName;

    @ViewById
    ImageView notifyMenuItem, userIcon, imageDeposit;

    @ViewById
    View mpayLayout, userInfoLayout;

    @ViewById
    View invoiceLine, invoiceItem;

    private CompositeSubscription subscription;

    @AfterViews
    void initSettingFragment() {
        TextView textView = userMPay.getText2();
        textView.setTextColor(getResources().getColor(R.color.money_color_yellow));
    }

    @Override
    public void onResume() {
        super.onResume();

        LoginActivity.loadCurrentUser(getActivity(), new LoginActivity.LoadUserCallback() {
            @Override
            public void onSuccess() {
                bindUI();
            }

            @Override
            public void onFail() {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        ((BaseActivity) getActivity()).getSupportActionBar().hide();
        bindUI();
        subscription = new CompositeSubscription();
        subscription.add(RxBus.getInstance().toObserverable().subscribe(o -> {
            if (o instanceof RxBus.UpdateBottomBarEvent) {
                if (isResumed()) {
                    bindUI();
                }
            }
        }));
    }

    @Override
    public void onStop() {
        subscription.clear();
        subscription = null;
        super.onStop();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (hidden) {
            ((BaseActivity) getActivity()).getSupportActionBar().show();
        } else {
            ((BaseActivity) getActivity()).getSupportActionBar().hide();
            bindUI();
        }
    }

    @Click
    void userInfoItem() {
        MainActivity activity = (MainActivity) getActivity();
        MyData myData = MyData.getInstance();
        if (myData.isLogin()) {
            CurrentUser user = myData.getData();
            if (user.isDemand()) {
                if (user.isEnterpriseAccout()) {
                    EnterpriseMainActivity_.intent(this).start();
                } else {
                    SetAccountActivity_.intent(this).start();
                }
            } else {
                UserMainActivity_.intent(this).start();
            }
        } else {
            activity.startLogin();
        }
    }

    @Click
    void userMPay() {
        CurrentUser user = MyData.getInstance().getData();
        if (user.isFullInfo()) {
            if (MyData.loadMPayOrderMapper(getActivity()) == null) {
                Network.getRetrofit(getActivity())
                        .getOrderMapper()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new BaseObserver<OrderMapper>(getActivity()) {
                            @Override
                            public void onSuccess(OrderMapper data) {
                                showSending(false);
                                super.onSuccess(data);
                                MyData.saveMPayOrderMapper(getActivity(), data);
                                MPayDynamicListActivity_.intent(SettingFragment.this).start();
                            }

                            @Override
                            public void onFail(int errorCode, @NonNull String error) {
                                super.onFail(errorCode, error);
                                showSending(false);
                            }
                        });
                showSending(true);
            } else {
                MPayDynamicListActivity_.intent(SettingFragment.this).start();
            }
        } else {
//            WithdrawRequire require = new WithdrawRequire();
//            require.passIdentity = false;
//            require.hasPassword = true;
//            new MPayAlertDialog(getActivity(), require).show();

            new AlertDialog.Builder(getActivity())
                    .setMessage("使用开发宝前,请先完善个人信息")
                    .setPositiveButton("完善个人信息", ((dialog, which) -> SetAccountActivity_.intent(getContext()).start()))
                    .setNegativeButton("取消", null)
                    .show();
        }
    }

    @Click
    void userIcon() {
        if (!MyData.getInstance().isLogin()) {
            ((MainActivity) getActivity()).startLogin();
        }
    }

    @Click
    void userName() {
        if (!MyData.getInstance().isLogin()) {
            ((MainActivity) getActivity()).startLogin();
        }
    }

    @Click
    void helpCenter() {
        HelpCenterActivity_.intent(this).start();
        umengEvent(UmengEvent.ACTION, "个人中心 _帮助与反馈 _ 提交意见反馈");
    }

    @Click
    void pushSetting() {
        if (!MyData.getInstance().isLogin()) {
            ((MainActivity) getActivity()).startLogin();
            return;
        }

        SettingHomeActivity_.intent(this).start();
    }

    @Click
    void about() {
        AboutActivity_.intent(this).start();
    }

    @Click
    void notifyMenuItem() {
        NotificationActivity_.intent(this).start();
    }

    @Click
    void invoiceItem() {
        InvoiceMainActivity_.intent(this).start();
    }

    public void bindUI() {
        invoiceItem.setVisibility(View.GONE);
        invoiceLine.setVisibility(View.GONE);

        imageDeposit.setVisibility(View.GONE);

        if (MyData.getInstance().isLogin()) {
            CurrentUser data = MyData.getInstance().getData();

            ImageLoadTool.loadImageUser(userIcon, data.getAvatar(), getResources().getDimensionPixelSize(R.dimen.user_icon_width));
            userName.setText(data.getGlobal_key());

            userInfoLayout.setVisibility(View.VISIBLE);
            mpayLayout.setVisibility(View.VISIBLE);


            if (data.isEnterpriseAccout()) {
                userMPay.setText("企业开发宝");

                int iconId = 0;
                if (data.isPassEnterpriceIdentity()) {
                    iconId = R.mipmap.ic_enterprise_flag_passed;
                }
                setUserInfo("成为认证企业", "", R.mipmap.ic_list_publish, iconId);

                invoiceItem.setVisibility(View.VISIBLE);
                invoiceLine.setVisibility(View.VISIBLE);
            } else {
                userMPay.setText("我的开发宝");

                if (data.user.deposit) {
                    imageDeposit.setVisibility(View.VISIBLE);
                }

                if (data.isDemand()) {
                    setUserInfo("个人信息", "", R.mipmap.ic_list_publish, 0);
                } else {
                    String text2;
                    if (MyData.getInstance().isPassIdentity()) {
                        text2 = "";
                    } else {
                        text2 = "未认证";
                    }

                    int icon2 = 0;
                    if (data.isExcellect()) {
                        icon2 = R.mipmap.identity_id_card_excellent;
                    } else if (data.isIdentityChecked()) {
                        icon2 = R.mipmap.identity_id_card;
                    }
                    setUserInfo("成为认证码士", text2, R.mipmap.ic_list_join, icon2);
                }
            }

            notifyMenuItem.setVisibility(View.VISIBLE);
            notifyMenuItem.setImageResource(((MainActivity) getActivity()).getNotifyMenuRes());

        } else {
            userInfoLayout.setVisibility(View.GONE);
            mpayLayout.setVisibility(View.GONE);

            setUserInfo("成为认证码士", "", R.mipmap.ic_list_join, 0);

            userIcon.setImageResource(R.mipmap.ic_default_user);
            userName.setText("未登录");
            notifyMenuItem.setVisibility(View.INVISIBLE);
        }
    }

    private void setUserInfo(String text1, String text2, int icon1, int icon2) {
        userInfoText1.setText(text1);
        userInfoIcon1.setImageResource(icon1);
        userInfoText2.setText(text2);

        if (icon2 == 0) {
            userInfoIcon2.setVisibility(View.INVISIBLE);
            userInfoText2.setVisibility(View.VISIBLE);
        } else {
            userInfoIcon2.setVisibility(View.VISIBLE);
            userInfoIcon2.setImageResource(icon2);
            userInfoText2.setVisibility(View.INVISIBLE);
        }
    }
}
