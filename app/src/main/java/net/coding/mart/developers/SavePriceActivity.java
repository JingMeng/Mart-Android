package net.coding.mart.developers;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import net.coding.mart.R;
import net.coding.mart.common.BackActivity;
import net.coding.mart.common.MyData;
import net.coding.mart.common.util.ViewStyleUtil;
import net.coding.mart.json.BaseObserver;
import net.coding.mart.json.Network;
import net.coding.mart.login.LoginActivity_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@EActivity(R.layout.activity_save_price)
public class SavePriceActivity extends BackActivity {

    public static final String HUOGUO_SAVE_PRICE = "HUOGUO_SAVE_PRICE";

    private static final int RESULT_LOGIN = 1;

    @Extra
    String codes;

    @Extra
    String webPageCount;

    @Extra
    int id;

    @ViewById
    EditText name, description;

    @ViewById
    View sendButton;

    @AfterViews
    void initSavePriceActivity() {
        ViewStyleUtil.editTextBindButton(sendButton, name);
    }

    @Click
    void sendButton() {
        if (MyData.getInstance().isLogin()) {
            savePrice();
        } else {
            LoginActivity_.intent(this).startForResult(RESULT_LOGIN);
        }
    }

    private void savePrice() {
        String nameString = name.getText().toString().trim();
        String descriptionString = description.getText().toString().trim();

        if (TextUtils.isEmpty(nameString)) {
            showMiddleToast("请输入项目名称");
            return;
        }

        Network.getRetrofit(this)
                .saveCalResult(id, nameString, descriptionString)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<String>(this) {
                    @Override
                    public void onSuccess(String data) {
                        showSending(false);

                        EventBus.getDefault().post(HUOGUO_SAVE_PRICE);

                        setResult(RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onFail(int errorCode, @NonNull String error) {
                        super.onFail(errorCode, error);
                        showSending(false);
                    }
                });
        showSending(true);
    }

    @OnActivityResult(RESULT_LOGIN)
    void onResultLogin(int result) {
        if (result == RESULT_OK && MyData.getInstance().isLogin()) {
            savePrice();
        }
    }
}
