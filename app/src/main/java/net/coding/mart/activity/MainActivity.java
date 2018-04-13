package net.coding.mart.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.orhanobut.logger.Logger;
import com.tencent.TIMMessageListener;

import net.coding.mart.LengthUtil;
import net.coding.mart.R;
import net.coding.mart.activity.reward.PublishRewardActivity_;
import net.coding.mart.activity.user.setting.SettingFragment;
import net.coding.mart.activity.user.setting.SettingFragment_;
import net.coding.mart.common.BaseActivity;
import net.coding.mart.common.Global;
import net.coding.mart.common.GlobalData_;
import net.coding.mart.common.MyData;
import net.coding.mart.common.WXPay;
import net.coding.mart.common.umeng.UmengEvent;
import net.coding.mart.common.util.RxBus;
import net.coding.mart.developers.HuoguoEntranceFragment_;
import net.coding.mart.json.BaseObserver;
import net.coding.mart.json.CurrentUser;
import net.coding.mart.json.Network;
import net.coding.mart.json.NewBaseObserver;
import net.coding.mart.json.global.Setting;
import net.coding.mart.json.global.Settings;
import net.coding.mart.json.message.IMUser;
import net.coding.mart.login.LoginActivity_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.androidannotations.api.builder.FragmentBuilder;
import org.greenrobot.eventbus.EventBus;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/*
V3.5
- 更新登录/注册流程，区分开发者身份与需求方身份
 */

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity implements AppBarLayout.OnOffsetChangedListener {

    public static final String CLICK_ACTION_TITLE = "clickActionTitle";

    @ViewById
    AppBarLayout appbar;

    @ViewById
    AHBottomNavigation bottomNavigation;

    @ViewById
    FloatingActionButton floatButton;

    @Pref
    GlobalData_ globalData;
    AHBottomNavigationItem navigationItemHome = new AHBottomNavigationItem(R.string.tag_home, R.mipmap.ic_navigation_home_normal, R.color.text_pink);
    AHBottomNavigationItem navigationItemPublishList = new AHBottomNavigationItem(R.string.tag_publish_list, R.mipmap.ic_navigation_publish_list_normal, R.color.bottom_bar_color);
    AHBottomNavigationItem navigationItemMe = new AHBottomNavigationItem(R.string.tag_me, R.mipmap.ic_navigation_me_normal, R.color.bottom_bar_color);
    AHBottomNavigationItem navigationItemMyJoin = new AHBottomNavigationItem(R.string.tag_my_join, R.mipmap.ic_navigation_my_publish_normal, R.color.bottom_bar_color);
    AHBottomNavigationItem navigationItemHuoguo = new AHBottomNavigationItem(R.string.tag_huoguo, R.mipmap.ic_navigation_huoguo_normal, R.color.bottom_bar_color);
    AHBottomNavigationItem navigationItemPublish = new AHBottomNavigationItem(R.string.tag_publish, R.mipmap.ic_navigation_publish_normal, R.color.font_green);
    AHBottomNavigationItem navigationItemMyPublish = new AHBottomNavigationItem(R.string.tag_my_publish, R.mipmap.ic_navigation_my_publish_normal, R.color.bottom_bar_color);
    AHBottomNavigationItem[] bottomBarItems;
    IMUser imUser = null;
    private boolean needUpdateBottomBar = true;
    private AHBottomNavigationItem pickBottomBar = null;

    private CompositeSubscription subscription = new CompositeSubscription();
    private Toolbar toolbar;
    private View myJoinedListTitle;
    private int notifyMenuResource = R.mipmap.ic_notify_button;
    private TIMMessageListener timMessageListener = list -> {
        bottomBarShowRedPoint(true);
        return false;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WXPay.getInstance().regToWeixin(this);

        subscription.add(RxBus.getInstance().toObserverable().subscribe(o -> {
            if (o instanceof RxBus.UpdateMainEvent) {
                if (isResume()) {
                    switchBottomBar(null);
                } else {
                    needUpdateBottomBar = true;
                }
            } else if (o instanceof RxBus.RewardPublishSuccess) {
                MyData myData = MyData.getInstance();
                myData.getData().user.counter.published++;
                myData.save(MainActivity.this);

                if (isResume()) {
                    switchBottomBar(navigationItemMyPublish);
                } else {
                    needUpdateBottomBar = true;
                    pickBottomBar = navigationItemMyPublish;
                }
            } else if (o instanceof RxBus.UpdateBottomBarMessageEvent) {
                updateNoreadMessage();
            }
        }));

        Network.getRetrofit(this)
                .getMartEnterpriseGK()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<String>(this) {
                    @Override
                    public void onSuccess(String data) {
                        super.onSuccess(data);
                        globalData.edit()
                                .enterpriseGK()
                                .put(data)
                                .apply();
                    }

                    @Override
                    public void onFail(int errorCode, @NonNull String error) {
                    }
                });


        Network.getRetrofit(this)
                .getGlobalSettings()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NewBaseObserver<Settings>(this) {
                    @Override
                    public void onSuccess(Settings data) {
                        super.onSuccess(data);

                        GlobalData_.GlobalDataEditor_ edit = globalData.edit();

                        for (Setting item : data.settings) {
                            String code = item.code;
                            if (code.equals("project_publish_payment_tip")) {
                                edit.projectPublishPaymentTip()
                                        .put(item.value)
                                        .apply();
                            } else if (code.equals("project_publish_payment_deadline")) {
                                edit.projectPublishPaymentDeadline()
                                        .put(item.value)
                                        .apply();
                            } else if (code.equals("project_publish_payment_color")) {
                                edit.projectPublishPaymentDeadTitle()
                                        .put(item.value)
                                        .apply();
                            } else if (code.equals("project_publish_payment")) {
                                edit.projectPublishPayment()
                                        .put(item.value)
                                        .apply();
                            }
                        }
                    }

                    @Override
                    public void onFail(int errorCode, @NonNull String error) {
                    }
                });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscription.clear();
    }

    @Override
    public void onResume() {
        super.onResume();
        appbar.addOnOffsetChangedListener(this);

        if (needUpdateBottomBar) {
            switchBottomBar(pickBottomBar);
            pickBottomBar = null;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        appbar.removeOnOffsetChangedListener(this);
    }

    @AfterViews
    void initMainActivity() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        myJoinedListTitle = toolbar.findViewById(R.id.myJoinedListTitle);

        initBottomNavigation();
    }

    private void initBottomNavigation() {
        bottomNavigation.setDefaultBackgroundColor(0xE1F9f9f9);
        bottomNavigation.setInactiveColor(0xffadbbcb);
        bottomNavigation.setForceTitlesDisplay(true);
        bottomNavigation.setBehaviorTranslationEnabled(false);
        bottomNavigation.setAccentColor(getResources().getColor(R.color.font_blue));
        bottomNavigation.setOnTabSelectedListener((position, wasSelected) -> {
            if (!wasSelected) {
                return setSelectFragment(position);
            }
            return true;
        });

    }

    private boolean setSelectFragment(int position) {
        if (bottomBarItems == null || position >= bottomBarItems.length) {
            return false;
        }

        AHBottomNavigationItem selectItem = bottomBarItems[position];
        if (selectItem != navigationItemMe) {
            setActionBarTitle(selectItem.getTitle(this));
        }

        if (toolbar != null) {
            toolbar.setOnClickListener(null);
        }

        if (myJoinedListTitle != null) {
            myJoinedListTitle.setVisibility(View.GONE);
        }

        if (selectItem == navigationItemHome) {
            switchHome();
            if (MyData.isPublishUser()) {
                floatButton.setVisibility(View.VISIBLE);
            } else {
                floatButton.setVisibility(View.GONE);
            }
        } else if (selectItem == navigationItemPublishList) {
            switchFragment(MainFragment_.FragmentBuilder_.class);
            floatButton.setVisibility(View.GONE);
        } else if (selectItem == navigationItemMe) {
            switchSetting();
            floatButton.setVisibility(View.GONE);
        } else if (selectItem == navigationItemMyJoin) {
            switchMyJoin();
            floatButton.setVisibility(View.GONE);
        } else if (selectItem == navigationItemHuoguo) {
            switchHuoguo();
            floatButton.setVisibility(View.GONE);
        } else if (selectItem == navigationItemPublish) {
            switchPublish();
            floatButton.setVisibility(View.GONE);
        } else if (selectItem == navigationItemMyPublish) {
            switchMyPublish();
            floatButton.setVisibility(View.VISIBLE);
        }

        updateNoreadMessage();

        return true;
    }

    public void switchHome() {
        switchFragment(HomeFragment_.FragmentBuilder_.class);
    }

    private void switchBottomBar(AHBottomNavigationItem picked) {
        Logger.d("switchto " + (picked == null ? "null" : picked));
        needUpdateBottomBar = false;

        CurrentUser user = MyData.getInstance().getData();

        if (MyData.getInstance().isLogin()) {
            if (user.isDemand()) {
                switchBottomBarPublish(user, picked);
            } else {
                switchBottomBarJoin(user, picked);
            }
        } else {
            switchBottomBarNoLogin(picked);
        }
    }

    private void switchBottomBarJoin(CurrentUser user, AHBottomNavigationItem picked) {
        bottomNavigation.removeAllItems();
        setBottomBarItems(
                new AHBottomNavigationItem[]{
                        navigationItemPublishList,
                        navigationItemMyJoin,
                        navigationItemMe},
                picked);
    }

    private void switchBottomBarNoLogin(AHBottomNavigationItem picked) {
        bottomNavigation.removeAllItems();
        setBottomBarItems(
                new AHBottomNavigationItem[]{
                        navigationItemHome,
                        navigationItemPublishList,
                        navigationItemPublish,
                        navigationItemMe
                },
                picked);
    }

    private void switchBottomBarPublish(CurrentUser user, AHBottomNavigationItem picked) {
        bottomNavigation.removeAllItems();
        setBottomBarItems(
                new AHBottomNavigationItem[]{
                        navigationItemHome,
                        navigationItemMyPublish,
                        navigationItemMe},
                picked);
    }

    private void updateNoreadMessage() {
//        List<TIMConversation> list = TIMManager.getInstance().getConversionList();
//        boolean hasUnreadMessage = false;
//        for (TIMConversation item : list) {
//            if (item.getUnreadMessageNum() > 0) {
//                hasUnreadMessage = true;
//                bottomBarShowRedPoint(true);
//                break;
//            }
//        }

//        if (!hasUnreadMessage) { // 如果没有未读对话，就再简单是否有未读通知
        updateNoreadNotify();
//        }
    }

    private void updateNoreadNotify() {
        Network.getRetrofit(this)
                .getUnreadCount()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<Integer>(this) {
                    @Override
                    public void onSuccess(Integer data) {
                        bottomBarShowRedPoint(data > 0);
                    }

                    @Override
                    public void onFail(int errorCode, @NonNull String error) {
                    }
                });
    }

    private void bottomBarShowRedPoint(boolean show) {
        notifyMenuResource = show ? R.mipmap.ic_notify_button_new : R.mipmap.ic_notify_button;

        ViewGroup group0 = (ViewGroup) bottomNavigation.getChildAt(1);
        if (group0 == null) {
            return;
        }
        ViewGroup group1 = (ViewGroup) group0.getChildAt(2);
        if (group1 == null) {
            return;
        }

        int pointId = R.id.icon;
        View point = group1.findViewById(pointId);

        if (point == null) {
            point = new View(this);
            point.setId(pointId);
            group1.addView(point);
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) point.getLayoutParams();
            lp.gravity = Gravity.CENTER_HORIZONTAL;
            lp.leftMargin = LengthUtil.dpToPx(11);
            lp.topMargin = LengthUtil.dpToPx(4);
            lp.width = LengthUtil.dpToPx(10);
            lp.height = LengthUtil.dpToPx(10);
            point.setLayoutParams(lp);
            point.setBackgroundDrawable(getResources().getDrawable(R.drawable.red_point));
        }

        point.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    private void setBottomBarItems(AHBottomNavigationItem[] items, AHBottomNavigationItem picked) {
        if (items == null || items.length < 1) {
            return;
        }

        bottomBarItems = items;
        bottomNavigation.removeAllItems();
        for (AHBottomNavigationItem item : items) {
            bottomNavigation.addItem(item);
        }

        if (picked == null) {
            bottomNavigation.setCurrentItem(0);
            setSelectFragment(0);
        } else {
            int pos = -1;
            for (int i = 0; i < items.length; ++i) {
                if (items[i] == picked) {
                    pos = i;
                    break;
                }
            }

            if (pos != -1) {
                bottomNavigation.setCurrentItem(pos);
                setSelectFragment(pos);

                RxBus.getInstance().send(new RxBus.UpdateBottomBarEvent());
            }
        }
    }

    public void clickBottomBarMain() {
        bottomNavigation.setCurrentItem(0);
        setSelectFragment(0);
    }

    @Click
    void floatButton() {
        popAddReward();
    }

    private void switchSetting() {
        switchFragment(SettingFragment_.FragmentBuilder_.class);
        setActionBarTitle("");
    }

    private void switchMyJoin() {
        switchFragment(MyJoinListFragment_.FragmentBuilder_.class);
        setActionBarTitle("");
        myJoinedListTitle.setVisibility(View.VISIBLE);
        toolbar.setOnClickListener(v -> EventBus.getDefault().post(CLICK_ACTION_TITLE));
    }

    public void switchHuoguo() {
        setActionBarTitle("选择项目类型");
        switchFragment(HuoguoEntranceFragment_.FragmentBuilder_.class);

//        clickToolTip300.onClick(null);
    }

    private void switchPublish() {
        switchFragment(PublishRewardTypeFragment_.FragmentBuilder_.class);
        setActionBarTitle("发布");
    }

    private void switchMyPublish() {
        switchFragment(MyPublishListFragment_.FragmentBuilder_.class);
        setActionBarTitle("我发布的项目");
    }

    public int getNotifyMenuRes() {
        return notifyMenuResource;
    }


    private void switchFragment(Class<?> cls) {
        appbar.setExpanded(true, true);

        String tag = cls.getName();
        Fragment showFragment = getSupportFragmentManager().findFragmentByTag(tag);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (showFragment == null) {
            try {
                showFragment = (Fragment) ((FragmentBuilder) cls.newInstance()).build();
                fragmentTransaction.add(R.id.container, showFragment, tag);
            } catch (Exception e) {
                Global.errorLog(e);
            }
        } else {
            fragmentTransaction.show(showFragment);
        }

        List<Fragment> allFragments = getSupportFragmentManager().getFragments();
        if (allFragments != null) {
            for (Fragment item : allFragments) {
                if (item != showFragment) {
                    fragmentTransaction.hide(item);
                }
            }
        }

        fragmentTransaction.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();

        getSupportActionBar().show();
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment item : fragments) {
                if (item.isVisible() && item instanceof SettingFragment) {
                    getSupportActionBar().hide();
                    break;
                }
            }
        }
    }

    public void startLogin() {
        LoginActivity_.intent(this).start();
        umengEvent(UmengEvent.ACTION, "个人中心 _ 请登录");
    }

    public void popAddReward() {
        if (!MyData.getInstance().isLogin()) {
            LoginActivity_.intent(this).start();
            return;
        }

        PublishRewardActivity_.intent(this).start();
        overridePendingTransition(0, 0);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
    }

}
