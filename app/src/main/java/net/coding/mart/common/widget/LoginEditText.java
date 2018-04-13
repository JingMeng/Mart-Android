package net.coding.mart.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import net.coding.mart.R;
import net.coding.mart.common.Global;
import net.coding.mart.json.Network;
import net.coding.mart.json.NewBaseObserver;
import net.coding.mart.json.login.Captcha;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by chenchao on 15/12/17.
 */
public class LoginEditText extends FrameLayout implements OnTextChange {
    private static final String TAG = "LoginEditText";

    public EditText editText;
    private View topLine;
    private ImageView imageValidfy;

    public LoginEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(final Context context, AttributeSet attrs) {
        inflate(context, R.layout.login_edit_text, this);
        editText = (EditText) findViewById(R.id.editText);
        topLine = findViewById(R.id.topLine);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.LoginEditText, 0, 0);
        try {
            boolean showTopLine = a.getBoolean(R.styleable.LoginEditText_topLine, true);
            topLine.setVisibility(showTopLine ? VISIBLE : GONE);

            String hint = a.getString(R.styleable.LoginEditText_hint);
            if (hint == null) {
                hint = "";
            }
            editText.setHint(hint);

            boolean showCaptcha = a.getBoolean(R.styleable.LoginEditText_captcha, false);
            if (showCaptcha) {
                imageValidfy = (ImageView) findViewById(R.id.imageValify);
                imageValidfy.setVisibility(VISIBLE);
                imageValidfy.setOnClickListener(v -> requestCaptcha());
                requestCaptcha();
            }

            int inputType = a.getInt(R.styleable.LoginEditText_loginInput, 0);
            if (inputType == 1) {
                editText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            } else if (inputType == 2) {
                editText.setInputType(InputType.TYPE_CLASS_PHONE);
            }

        } finally {
            a.recycle();
        }
    }

    @Override
    public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
        boolean result = super.requestFocus(direction, previouslyFocusedRect);
        editText.requestFocus();
        return result;
    }

    public ImageView getCaptcha() {
        return imageValidfy;
    }

    public void requestCaptcha(CaptchCallback callback) {
        editText.setText("");

        Network.getRetrofit(editText.getContext())
                .getCaptcha()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NewBaseObserver<Captcha>(editText.getContext()) {
                    @Override
                    public void onSuccess(Captcha data) {
                        super.onSuccess(data);

                        int pos = data.image.indexOf(',');
                        if (pos != -1) {
                            byte[] imageData = Base64.decode(data.image.substring(pos + 1, data.image.length()), Base64.DEFAULT);
                            imageValidfy.setImageBitmap(BitmapFactory.decodeByteArray(imageData, 0, imageData.length));

                            if (callback != null) {
                                callback.needShowCaptch();
                            }
                        }
                    }
                });

    }

    public void requestCaptcha() {
        requestCaptcha(null);
    }

    public void hideKeyBoard() {
        Global.popSoftkeyboard(getContext(), editText, false);
    }

    @Override
    public boolean isEmpty() {
        return editText.getText().length() == 0;
    }

    @Override
    public void addTextChangedListener(TextWatcher watcher) {
        editText.addTextChangedListener(watcher);
    }

    @Override
    public Editable getText() {
        return editText.getText();
    }

    public void setText(String text) {
        if (text == null) {
            return;
        }

        editText.setText(text);
    }

    public String getTextString() {
        return editText.getText().toString();
    }

    public interface CaptchCallback {
        void needShowCaptch();
    }
}
