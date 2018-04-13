package net.coding.mart.common;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.umeng.analytics.MobclickAgent;

import net.coding.mart.R;
import net.coding.mart.common.widget.SingleToast;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import rx.Subscription;

/**
 * Created by chenchao on 15/10/7.
 * 放一些公共的 activity 方法
 */
public class BaseActivity extends AppCompatActivity {

    SingleToast mSingleToast;

    private boolean mResume = false;

    private List<WeakReference<Subscription>> manager = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSingleToast = new SingleToast(this);


        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);

        MobclickAgent.openActivityDurationTrack(false);

    }

    protected void bindSubscription(Subscription sub) {
        WeakReference<Subscription> wp = new WeakReference<Subscription>(sub);
        manager.add(wp);
    }

    protected void setActionBarTitle(int title) {
        String s = getResources().getString(title);
        setActionBarTitle(s);
    }

    protected void setActionBarTitle(String title) {
        if (title == null) {
            return;
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }

    @Override
    protected void onDestroy() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }

        for (WeakReference<Subscription> item : manager) {
            Subscription sub = item.get();
            if (sub != null && !sub.isUnsubscribed()) {
                sub.unsubscribe();
            }
        }
        manager.clear();

        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();

        MobclickAgent.onPageStart(getClass().getSimpleName());
        MobclickAgent.onResume(this);

        mResume = true;
    }

    @Override
    public void onPause() {
        super.onPause();

        MobclickAgent.onPageEnd(getClass().getSimpleName());
        MobclickAgent.onPause(this);

        mResume = false;
    }

    public boolean isResume() {
        return mResume;
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
    public void showMiddleToast(String msg, int duration) {
        mSingleToast.showMiddleToast(msg, duration);
    }

    public void showButtomToast(int messageId) {
        mSingleToast.showButtomToast(messageId);
    }

    public void showErrorMsg(int code, JSONObject json) {
        if (code == NetworkImpl.NETWORK_CONNECT_FAIL) {
            showButtomToast(R.string.connect_service_fail);
        } else {
            String msg = Global.getErrorMsg(json);
            if (!msg.isEmpty()) {
                showButtomToast(msg);
            }
        }
    }

    public void showSending(boolean show) {
        showSending(show, "");
    }

    public void showSending(boolean show, String message) {
        if (mProgressDialog == null) {
            return;
        }

        if (show) {
            if (message == null) message = "";
            mProgressDialog.setMessage(message);
            mProgressDialog.show();
        } else {
            mProgressDialog.hide();
        }
    }

    ProgressDialog mProgressDialog;


    protected void umengEvent(String s, String param) {
        MobclickAgent.onEvent(this, s, param);
    }

    /**
     * 返回自身 Context格式
     * @return
     */
    public Context ctx(){return this;}

    /**
     * 返回自身 Activity格式
     * @return
     */
    public Activity act(){return this;}
}
