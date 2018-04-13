package net.coding.mart.user.identityAuthentication;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import net.coding.mart.R;
import net.coding.mart.activity.mpay.FinalPayOrdersActivity_;
import net.coding.mart.common.BaseFragment;
import net.coding.mart.common.Global;
import net.coding.mart.common.MyData;
import net.coding.mart.common.util.DialogFactory;
import net.coding.mart.common.util.ViewStyleUtil;
import net.coding.mart.json.Network;
import net.coding.mart.json.NewBaseObserver;
import net.coding.mart.json.mart2.user.MartUser;
import net.coding.mart.json.user.identity.IdentityCheck;
import net.coding.mart.login.LoginActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@EFragment(R.layout.fragment_identity_fragment1)
public class IdentityFragment1 extends BaseFragment {

    private static final int RESULT_PAY = 1;

    @ViewById
    TextView sendButton;

    @ViewById
    TextView agreeTerms, contact;

    @ViewById
    EditText name, idCard;

    @ViewById
    View rootLayout;

    IdentityActivity parent;

    String orderId = "";

    @SuppressLint("ClickableViewAccessibility")
    @AfterViews
    void initIdentityFragment1() {
        parent = (IdentityActivity) getActivity();

        agreeTerms.setText(Global.createBlueHtml("点击『提交审核』代表您已仔细阅读上述「认证说明」\n并同意遵守", "《身份认证授权与承诺书》", ""));

        ViewStyleUtil.editTextBindButton(sendButton, name, idCard);

        MartUser userInfo = MyData.getInstance().getData().user;

        idCard.setText(userInfo.identity);

        rootLayout.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                Global.popSoftkeyboard(getActivity(), idCard, false);
            }
            return false;
        });

        if (userInfo.isDeveloperTeam()) {
            contact.setText(R.string.identity_top_tip_enterprise);
        } else {
            contact.setText(R.string.identity_top_tip);
        }

        Global.addLinkCustomerService(getActivity(), contact);
    }

    @Click
    void agreeTerms() {
        parent.popIdentityTip();
    }

    @Click
    void sendButton() {
        String nameString = name.getText().toString();
        String idCardString = idCard.getText().toString();

        Global.popSoftkeyboard(parent, idCard, false);

        Network.getRetrofit(parent)
                .userIdentity(nameString, idCardString)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NewBaseObserver<IdentityCheck>(getActivity()) {
                    @Override
                    public void onSuccess(IdentityCheck data) {
                        super.onSuccess(data);
                        showSending(false);
                        updateIdentityStatus();
                    }

                    @Override
                    public void onFail(int errorCode, @NonNull String error) {
                        super.onFail(errorCode, error);
                        showSending(false);
                    }
                });

        showSending(true);
    }

    private void popPayPrepareOrderDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext(), R.style.Dialog).create();
        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.dialog_tip_identity_weixin_pay);
        window.findViewById(R.id.demo_closebtn).setOnClickListener(v -> alertDialog.dismiss());

        ((TextView) window.findViewById(R.id.name)).setText(name.getText());
        ((TextView) window.findViewById(R.id.idCard)).setText(idCard.getText());
        (window.findViewById(R.id.sendButton)).setOnClickListener(v -> {
            jumpPayActivity();
            alertDialog.dismiss();
        });
    }

    private void jumpPayActivity() {
        ArrayList<String> ids = new ArrayList<>();
        ids.add(orderId);
        FinalPayOrdersActivity_.intent(this)
                .orderIds(ids)
                .startForResult(RESULT_PAY);
    }

    @OnActivityResult(RESULT_PAY)
    void onResultPay(int resultCode) {
        if (resultCode == Activity.RESULT_OK) {
            updateIdentityStatus();
        }
    }

    private void updateIdentityStatus() {
        showSending(true);
        LoginActivity.loadCurrentUser(getActivity(), new LoginActivity.LoadUserCallback() {
            @Override
            public void onSuccess() {
                showSending(false);

                MartUser user = MyData.getInstance().getData().user;
                IdentityActivity activity = (IdentityActivity) getActivity();
                if (user.isPassed()) {
                    activity.jumpFragment3();
                } else if (user.isCheking()) {
                    activity.jumpFragment2();
                } else {
                    popNoCheckedDialog();
                }
            }

            @Override
            public void onFail() {
                showSending(false);
            }
        });
    }

    private void popNoCheckedDialog() {
        DialogFactory.create(parent, "认证失败", "请核对好姓名和身份证号码后重试");
    }
}
