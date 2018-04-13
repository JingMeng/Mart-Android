package net.coding.mart;

import android.content.pm.PackageInfo;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import net.coding.mart.activity.user.setting.CommonDialog;
import net.coding.mart.common.BackActivity;
import net.coding.mart.common.Global;
import net.coding.mart.common.MyData;
import net.coding.mart.common.widget.SimpleTextWatcher;
import net.coding.mart.json.BaseHttpResult;
import net.coding.mart.json.Network;
import net.coding.mart.json.NewBaseObserver;
import net.coding.mart.json.login.Captcha;

import java.util.ArrayList;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class FeedbackActivity extends BackActivity {

    String[] TYPES = new String[]{
            "SUGGESTION",
            "COMPLAINT",
            "PROJECT",
            "BUG",
            "OTHER"};
    String FEED_EXTRA = "";
    private EditText name;
    private EditText email;
    private EditText content;
    private EditText editValify, globalKey;
    private CheckBox[] types;
    View.OnClickListener mClickSendFeedback = v -> sendFeedback();
    private android.widget.ImageView imageValify;
    View.OnClickListener mClickImage = v -> downloadValifyPhoto();
    private Button sendFeedback;
    TextWatcher mTextWatch = new SimpleTextWatcher() {
        @Override
        public void afterTextChanged(Editable s) {
            updateSendButton();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        imageValify = (ImageView) findViewById(R.id.imageValify);
        editValify = (EditText) findViewById(R.id.editValify);
        content = (EditText) findViewById(R.id.content);
        email = (EditText) findViewById(R.id.email);
        name = (EditText) findViewById(R.id.name);
        globalKey = (EditText) findViewById(R.id.globalKey);
        sendFeedback = (Button) findViewById(R.id.sendFeedback);

        int typeIds[] = new int[]{
                R.id.type0,
                R.id.type1,
                R.id.type2,
                R.id.type3,
                R.id.type4,
        };
        types = new CheckBox[typeIds.length];
        for (int i = 0; i < typeIds.length; ++i) {
            types[i] = (CheckBox) findViewById(typeIds[i]);
            types[i].setOnClickListener(v -> updateSendButton());
        }

        name.addTextChangedListener(mTextWatch);
        email.addTextChangedListener(mTextWatch);
        content.addTextChangedListener(mTextWatch);
        editValify.addTextChangedListener(mTextWatch);
        sendFeedback.setOnClickListener(mClickSendFeedback);
        imageValify.setOnClickListener(mClickImage);

        MyData.FeedbackInfo info = MyData.getInstance().loadFeedbackInfo(this);
        name.setText(info.name);
        email.setText(info.email);
        if (MyData.getInstance().isLogin()) {
            globalKey.setText(MyData.getInstance().getData().getGlobal_key());
        }

        if (info.name.isEmpty()) {
            name.requestFocus();
        } else if (info.email.isEmpty()) {
            email.requestFocus();
        } else {
            content.requestFocus();
        }

        downloadValifyPhoto();
    }

    private void downloadValifyPhoto() {
        Network.getRetrofit(this)
                .getFeedbackCaptcha()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NewBaseObserver<Captcha>(this) {
                    @Override
                    public void onSuccess(Captcha data) {
                        super.onSuccess(data);

                        int pos = data.image.indexOf(',');
                        if (pos != -1) {
                            byte[] imageData = Base64.decode(data.image.substring(pos + 1, data.image.length()), Base64.DEFAULT);
                            imageValify.setImageBitmap(BitmapFactory.decodeByteArray(imageData, 0, imageData.length));
                            editValify.setText("");
                        }
                    }

                    @Override
                    public void onFail(int errorCode, @NonNull String error) {
                        super.onFail(errorCode, error);
                        showMiddleToast("获取验证码失败");
                    }
                });
    }

    private void updateSendButton() {
        sendFeedback.setEnabled(false);
        String nameString = name.getText().toString();
        if (nameString.isEmpty()) {
            return;
        }
        String phoneString = globalKey.getText().toString();
        if (phoneString.isEmpty()) {
            return;
        }
        String emailString = email.getText().toString();
        if (emailString.isEmpty()) {
            return;
        }
        String contentString = content.getText().toString();
        if (contentString.isEmpty()) {
            return;
        }
        String valiftyString = editValify.getText().toString();
        if (valiftyString.isEmpty()) {
            return;
        }

        boolean picked = false;
        for (CheckBox item : types) {
            if (item.isChecked()) {
                picked = true;
                break;
            }
        }
        if (!picked) {
            return;
        }

        sendFeedback.setEnabled(true);
    }

    private String getFeedExtra() {
        if (FEED_EXTRA.isEmpty()) {
            try {
                PackageInfo pInfo = getPackageManager().getPackageInfo("net.coding.mart", 0);
                String appVersion = pInfo.versionName;
                String phoneModel = Build.MODEL;
                int androidVersion = Build.VERSION.SDK_INT;
                FEED_EXTRA = String.format("\n码市 Android %s %s (%s)", appVersion, phoneModel, androidVersion);
            } catch (Exception e) {
                Global.errorLog(e);
            }
        }

        return FEED_EXTRA;
    }

    private void sendFeedback() {
        String gkString = globalKey.getText().toString();
        String nameString = name.getText().toString();
        String emailString = email.getText().toString();
        String contentString = content.getText().toString();
        String valiftyString = editValify.getText().toString();

        contentString += getFeedExtra();

        MyData.FeedbackInfo info = new MyData.FeedbackInfo(nameString, gkString, emailString);
        MyData.getInstance().saveFeedbackInfo(this, info);

        ArrayList<String> pickTypes = new ArrayList<>();
        for (int i = 0; i < types.length; ++i) {
            if (types[i].isChecked()) {
                pickTypes.add(TYPES[i]);
            }
        }

        Network.getRetrofit(this)
                .feedback(gkString, contentString, nameString, emailString, valiftyString, pickTypes)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NewBaseObserver<BaseHttpResult>(this) {
                    @Override
                    public void onSuccess(BaseHttpResult data) {
                        super.onSuccess(data);

                        showSending(false);

                        String title = "提交成功";
                        String message = "非常感谢您的反馈！码市顾问将会在 1-2 个工作日内与你联系。";

                        new CommonDialog.Builder(FeedbackActivity.this)
                                .setTitle(title)
                                .setMessage(message)
                                .setHideLeftButton(true)
                                .setCanceledOnTouchOutside(false)
                                .setRightButton("确定", v -> finish())
                                .show();
                    }

                    @Override
                    public void onFail(int errorCode, @NonNull String error) {
                        super.onFail(errorCode, error);
                        showSending(false);
                    }
                });

        showSending(true, "正在发送反馈...");
    }
}
