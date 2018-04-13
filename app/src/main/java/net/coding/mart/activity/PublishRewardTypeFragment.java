package net.coding.mart.activity;


import android.view.View;

import net.coding.mart.R;
import net.coding.mart.activity.reward.PublishRewardActivity_;
import net.coding.mart.common.BaseFragment;
import net.coding.mart.common.MyData;
import net.coding.mart.common.constant.RewardType;
import net.coding.mart.login.LoginActivity_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;

import java.util.HashMap;


@EFragment(R.layout.fragment_publish_reward_type2)
public class PublishRewardTypeFragment extends BaseFragment {

    @AfterViews
    void initPublishRewardTypeFragment() {
    }

    @Click({R.id.web, R.id.app, R.id.weixin, R.id.html5, R.id.weixinApp, R.id.other})
    void clickType(View button) {
        if (!MyData.getInstance().isLogin()) {
            LoginActivity_.intent(this).start();
            return;
        }

        HashMap<Integer, RewardType> map = new HashMap<Integer, RewardType>() {{
            put(R.id.web, RewardType.web);
            put(R.id.app, RewardType.mobile);
            put(R.id.weixin, RewardType.wechat);
            put(R.id.html5, RewardType.html5);
            put(R.id.weixinApp, RewardType.weapp);
            put(R.id.other, RewardType.other);
        }};
        PublishRewardActivity_.intent(this)
                .type(map.get(button.getId()))
                .start();
    }
}
