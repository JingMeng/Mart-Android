package net.coding.mart.login;


import android.app.Activity;
import android.support.annotation.NonNull;
import android.widget.TextView;
import android.widget.Toast;

import net.coding.mart.R;
import net.coding.mart.common.BaseFragment;
import net.coding.mart.common.util.ActivityNavigate;
import net.coding.mart.common.util.SimpleSHA1;
import net.coding.mart.common.util.ViewStyleUtil;
import net.coding.mart.common.widget.LoginEditText;
import net.coding.mart.common.widget.SingleToast;
import net.coding.mart.json.BaseObserver;
import net.coding.mart.json.Network;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@EFragment(R.layout.fragment_phone_set_password2)
public class PhoneSetPasswordFragment extends BaseFragment {

    @ViewById
    LoginEditText passwordEdit, repasswordEdit;

    @ViewById
    TextView loginButton, textClause;

    @AfterViews
    final void initPhoneSetPasswordFragment() {
        ViewStyleUtil.editTextBindButton(loginButton, passwordEdit, repasswordEdit);
    }

    @Click
    void loginButton() {
        String password = passwordEdit.getText().toString();
        String repassword = repasswordEdit.getText().toString();
        if (password.length() < 6) {
            SingleToast.showMiddleToast(getActivity(), "密码至少为6位");
            return;
        } else if (64 < password.length()) {
            SingleToast.showMiddleToast(getActivity(), "密码不能大于64位");
            return;
        } else if (!password.equals(repassword)) {
            SingleToast.showMiddleToast(getActivity(), "两次输入的密码不一致");
            return;
        }

        Map<String, String> params = ((SetPasswordCallback) getActivity()).getRequestParmas();
        String sha1 = SimpleSHA1.sha1(password);
        params.put("password", sha1);
        params.put("rePassword", sha1);

        Network.getRetrofit(getActivity())
                .setPassword(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<Boolean>(getActivity()) {
                    @Override
                    public void onSuccess(Boolean data) {
                        super.onSuccess(data);
                        closeActivity();
                    }

                    @Override
                    public void onFail(int errorCode, @NonNull String error) {
                        super.onFail(errorCode, error);
                        showSending(false, "");
                    }
                });

        showSending(true, "");
    }

    @Click
    void textClause() {
        ActivityNavigate.startServiceTerm(getActivity());
    }

    private void closeActivity() {
        Toast.makeText(getActivity(), "重置密码成功", Toast.LENGTH_SHORT).show();
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }
}
