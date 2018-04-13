package net.coding.mart.common;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.umeng.analytics.MobclickAgent;

import net.coding.mart.common.widget.SingleToast;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import rx.Subscription;

/**
 * Created by chenchao on 15/10/7.
 * 放一些公共的操作
 */
public class BaseFragment extends Fragment {

    SingleToast mSingleToast;

    private List<WeakReference<Subscription>> manager = new ArrayList<>();

    protected void setActionBarTitle(@StringRes int title) {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }

    protected void setActionBarTitle(String title) {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }

    protected void bindSubscription(Subscription sub) {
        WeakReference<Subscription> wp = new WeakReference<Subscription>(sub);
        manager.add(wp);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (useEventBus()) {
            EventBus.getDefault().register(this);
        }
    }

    protected boolean useEventBus() {
        return false;
    }

    @Override
    public void onDestroyView() {
        for (WeakReference<Subscription> item : manager) {
            Subscription sub = item.get();
            if (sub != null && !sub.isUnsubscribed()) {
                sub.unsubscribe();
            }
        }
        manager.clear();

        super.onDestroyView();

        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mSingleToast = new SingleToast(activity);
    }

    protected void bindClick(View view, int id, View.OnClickListener click) {
        view.findViewById(id).setOnClickListener(click);
    }


    public void showButtomToast(String msg) {
        mSingleToast.showButtomToast(msg);
    }

    public void showMiddleToast(int id) {
        mSingleToast.showMiddleToast(id);
    }

    public void showMiddleToast(String msg) {
        mSingleToast.showMiddleToast(msg);
    }

    public void showButtomToast(int messageId) {
        mSingleToast.showButtomToast(messageId);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getClass().getSimpleName());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getClass().getSimpleName());
    }

    protected void umengEvent(String s, String param) {
        if (getActivity() != null) {
            MobclickAgent.onEvent(getActivity(), s, param);
        }
    }

    public void showSending(boolean show) {
        Activity activity = getActivity();
        if (activity instanceof BaseActivity) {
            ((BaseActivity) activity).showSending(show);
        }
    }

    public void showSending(boolean show, String message) {
        Activity activity = getActivity();
        if (activity instanceof BaseActivity) {
            ((BaseActivity) activity).showSending(show, message);
        }
    }

    public BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }



}
