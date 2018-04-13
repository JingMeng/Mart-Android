package net.coding.mart.developers;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import net.coding.mart.R;
import net.coding.mart.activity.reward.HuoguoActivity;
import net.coding.mart.common.BaseFragment;
import net.coding.mart.common.constant.Huoguo;
import net.coding.mart.common.widget.HuoguoItem;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by chenchao on 16/11/16.
 * 火锅单选择平台入口
 */
@EFragment(R.layout.fragment_huoguo_entrance)
public class HuoguoEntranceFragment extends BaseFragment {

    Set<String> pickedPlatform = new HashSet<>(8);

    public static final Huoguo[] PLATFORMS = new Huoguo[]{
            Huoguo.web, Huoguo.weixin, Huoguo.ios, Huoguo.android, Huoguo.front, Huoguo.h5, Huoguo.crawler
    };

    @ViewById
    View layoutRoot;

    @AfterViews
    void initHuoguoEntranceFragment() {
        if (getActivity() instanceof HuoguoActivity) {
            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) layoutRoot.getLayoutParams();
            lp.bottomMargin = 0;
            layoutRoot.setLayoutParams(lp);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void clseEvent(String message) {
        if (message.equals(SavePriceActivity.HUOGUO_SAVE_PRICE) && !isDetached()) {
            int[] itemIds = new int[]{
                    R.id.itemWeb, R.id.itemweixin, R.id.itemIos, R.id.itemandroid, R.id.itemFront, R.id.itemH5, R.id.itemCrawler};
            for (int item : itemIds) {
                View v = getView().findViewById(item);
                if (v instanceof HuoguoItem) {
                    HuoguoItem huoguoItem = (HuoguoItem) v;
                    huoguoItem.check(false);
                }
            }
            pickedPlatform.clear();
        }
    }

    @Click({R.id.itemWeb, R.id.itemweixin, R.id.itemIos, R.id.itemandroid, R.id.itemFront, R.id.itemH5, R.id.itemCrawler})
    void clickItem(View v) {
        String platform = getPlatform(v);
        HuoguoItem huoguoItem = (HuoguoItem) v;
        if (pickedPlatform.contains(platform)) {
            pickedPlatform.remove(platform);
            huoguoItem.check(false);
        } else {
            pickedPlatform.add(platform);
            huoguoItem.check(true);
        }
    }

    @Click
    void sendButton() {
        if (pickedPlatform.isEmpty()) {
            showMiddleToast("请选择相应平台");
            return;
        }

        ArrayList<String> pickedList = new ArrayList<>(pickedPlatform);
        Intent intent = new Intent(getActivity(), FunctionListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("ids", pickedList);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private String getPlatform(View v) {
        int[] viewIds = new int[]{
                R.id.itemWeb, R.id.itemweixin, R.id.itemIos, R.id.itemandroid, R.id.itemFront, R.id.itemH5, R.id.itemCrawler
        };

        for (int i = 0; i < viewIds.length; ++i) {
            if (viewIds[i] == v.getId()) {
                return PLATFORMS[i].code;
            }
        }
        return PLATFORMS[0].code;
    }
}
