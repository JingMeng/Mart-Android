package net.coding.mart.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.EditText;

import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import net.coding.mart.R;
import net.coding.mart.common.BackActivity;
import net.coding.mart.common.BaseActivity;
import net.coding.mart.common.Global;
import net.coding.mart.common.MyData;
import net.coding.mart.common.umeng.UmengEvent;
import net.coding.mart.common.util.RxBus;
import net.coding.mart.common.util.SimpleSHA1;
import net.coding.mart.common.util.ViewStyleUtil;
import net.coding.mart.common.widget.LoginEditText;
import net.coding.mart.json.Network;
import net.coding.mart.json.NewBaseObserver;
import net.coding.mart.json.mart2.user.MartUser;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import java.util.Calendar;
import java.util.List;

import okhttp3.Cookie;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@EActivity(R.layout.activity_login)
public class LoginActivity extends BackActivity {

    public static final int RESULT_REGIST = 1;
    private static final int RESULT_2FA = 2;

    @Extra
    boolean showRegister = true;

    @ViewById
    LoginEditText emailEdit, passwordEdit, captchaEdit;

    @ViewById
    Button loginButton;

    @AfterViews
    final void initLoginActivity() {
        ViewStyleUtil.editTextBindButton(loginButton, emailEdit, passwordEdit, captchaEdit);

        View scroll = findViewById(R.id.layoutScroll);
        if (scroll != null) {
            scroll.setOnTouchListener((v, event) -> {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    clickScroll();
                }
                return false;
            });
        }

        if (!showRegister) {
            View registerLayout = findViewById(R.id.registerLayout);
            if (registerLayout != null) {
                registerLayout.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void needShowCaptch() {
        captchaEdit.requestCaptcha(() -> captchaEdit.setVisibility(View.VISIBLE));
    }

    @Override
    protected void onStart() {
        super.onStart();
        needShowCaptch();
    }

    @Click
    void loginButton() {
        String name = emailEdit.getTextString();
        String password = passwordEdit.getTextString();
        String captcha = captchaEdit.getTextString();
        String passwordSHA1 = SimpleSHA1.sha1(password);

        Network.getRetrofit(this)
                .login(name, passwordSHA1, true, captcha)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NewBaseObserver<MartUser>(this) {
                    @Override
                    public void onSuccess(MartUser data) {
                        super.onSuccess(data);
                        syncWebviewCookie(LoginActivity.this);
                        loadCurrentUser(LoginActivity.this);
                    }

                    @Override
                    public void onFail(int errorCode, @NonNull String error) {
                        super.onFail(errorCode, error);

                        showSending(false, "");
                        needShowCaptch();
                    }
                });
        showSending(true, "登录中...");
        Global.popSoftkeyboard(this, emailEdit, false);
    }

    public static void syncWebviewCookie(Context context) {
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
        cookieManager.setAcceptCookie(true);
        List<Cookie> cookies = new SharedPrefsCookiePersistor(context).loadAll();
        for (Cookie item : cookies) {
            cookieManager.setCookie(Global.HOST, item.toString());
        }
        CookieSyncManager.getInstance().sync();
    }

    @Click
    void registerButton() {
        umengEvent(UmengEvent.ACTION, "登录 _ 注册");
        PhoneRegisterActivity_.intent(this).startForResult(RESULT_REGIST);
    }

    @Click
    void cannotLogin() {
        umengEvent(UmengEvent.ACTION, "登录 _ 找回密码");
//        InputAccountActivity_.intent(LoginActivity.this).start();

        new AlertDialog.Builder(this)
                .setItems(R.array.find_password_list, ((dialog, which) -> {
                    if (which == 1) {
                        EmailSetPasswordActivity_.intent(this).start();
                    } else {
                        PhoneSetPasswordActivity_.intent(this).start();
                    }
                }))
                .show();
    }

    @OnActivityResult(RESULT_REGIST)
    void onResultRegister(int resultCode) {
        if (resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }

    @OnActivityResult(RESULT_2FA)
    void onResult2FA(int resultCode) {
        if (resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }

    private static void loadCurrentUser(BaseActivity context) {
        Network.getRetrofit(context)
                .currentUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NewBaseObserver<MartUser>(context) {
                    @Override
                    public void onSuccess(MartUser data) {
                        super.onSuccess(data);
                        MyData.getInstance().login(context, data);

                        MyData.getInstance().moveDraft(context);
                        RxBus.getInstance().send(new RxBus.UpdateMainEvent());
                        context.setResult(RESULT_OK);
                        context.finish();
                        context.showSending(false, "");
                    }

                    @Override
                    public void onFail(int errorCode, @NonNull String error) {
                        super.onFail(errorCode, error);
                        context.showSending(false, "");
                    }
                });
    }


    public static void loadCurrentUser(Context context, LoadUserCallback callback) {
        Network.getRetrofit(context)
                .currentUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NewBaseObserver<MartUser>(context) {
                    @Override
                    public void onSuccess(MartUser data) {
                        super.onSuccess(data);

                        if (data.id != 0) { // 没有登录会获得一个空对象
                            MyData.getInstance().update(context, data);
                        }

                        if (callback != null) {
                            if (data.id != 0) {
                                callback.onSuccess();
                            } else {
                                callback.onFail();
                            }
                        }
                    }

                    @Override
                    public void onFail(int errorCode, @NonNull String error) {
                        super.onFail(errorCode, error);
                        if (context instanceof BaseActivity) {
                            ((BaseActivity) context).showSending(false, "");
                        }
                        if (callback != null) {
                            callback.onFail();
                        }
                    }
                });
    }

    public interface LoadUserCallback {
        void onSuccess();
        void onFail();
    }

    int clickIconCount = 0;
    long lastClickTime = 0;

    private void clickScroll() {
        long clickTime = Calendar.getInstance().getTimeInMillis();
        long lastTemp = lastClickTime;
        lastClickTime = clickTime;
        if (clickTime - lastTemp < 1000) {
            ++clickIconCount;
        } else {
            clickIconCount = 1;
        }

        if (clickIconCount >= 5) {
            clickIconCount = 0;

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            View content = getLayoutInflater().inflate(R.layout.host_setting, null);
            final EditText editText = (EditText) content.findViewById(R.id.editMart);

            SharedPreferences sharedPref = getSharedPreferences(
                    "login_activity_mart_custom_url", Context.MODE_PRIVATE);

            editText.setText(sharedPref.getString("martHost", ""));
            builder.setTitle("更改服务器 URL")
                    .setView(content)
                    .setPositiveButton("确定", (dialog, which) -> {
                        String hostString = editText.getText().toString();

                        SharedPreferences sharedPref1 = getSharedPreferences(
                                "login_activity_mart_custom_url", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref1.edit();
                        editor.putString("martHost", hostString);
                        editor.apply();

//                        setResult(RESULT_OK);
                        finish();

//                        System.exit(0);
                    })
                    .setNegativeButton("取消", null)
                    .show();
        }
    }
}
