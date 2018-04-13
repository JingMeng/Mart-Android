package net.coding.mart.user.identityAuthentication;


import android.support.annotation.NonNull;
import android.text.Html;
import android.text.TextUtils;
import android.widget.TextView;

import net.coding.mart.R;
import net.coding.mart.common.BaseFragment;
import net.coding.mart.common.CommonBackActivity_;
import net.coding.mart.common.Global;
import net.coding.mart.common.MyData;
import net.coding.mart.json.Network;
import net.coding.mart.json.NewBaseObserver;
import net.coding.mart.json.mart2.user.MartUser;
import net.coding.mart.json.user.identity.IdentitySign;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@EFragment(R.layout.fragment_identity_fragment3)
public class IdentityFragment3 extends BaseFragment {

    @ViewById
    TextView name, idType, idCard, tipContent;

    IdentitySign identitySign;

    @AfterViews
    void initIdentityFragment3() {
        tipContent.setText(Global.createBlueHtml("验证通过后无法自行修改。如果需要修改认证信息请 ", "联系客服", ""));
        Global.addLinkCustomerService(getActivity(), tipContent);

        uiBindData();
    }

    private void uiBindData() {
        MartUser userInfo = MyData.getInstance().getData().user;
        name.setText(generateHideString(userInfo.name, 1, 0));
        idType.setText(Html.fromHtml("证件类型: <font color='#222222'>身份证</fon>"));
        idCard.setText(Html.fromHtml(String.format("证件号码: <font color='#222222'>%s</font>",
                generateHideString(userInfo.identity, 3, 3))));
    }

    private String generateHideString(String s, int beginCount, int endCount) {
        StringBuilder sb = new StringBuilder(s.length());

        int beginPos = beginCount;
        int endPos = s.length() - endCount;
        for (int i = 0; i < s.length(); ++i) {
            if (beginPos <= i && i < endPos) {
                sb.append("*");
            } else {
                sb.append(s.charAt(i));
            }
        }

        return sb.toString();
    }

    @Click
    void identityTip() {
        CommonBackActivity_.intent(this)
                .start();
    }

    @Click
    void download() {
        if (identitySign != null && !TextUtils.isEmpty(identitySign.documentId)) {
            downloadFile();
        } else {
            Network.getRetrofit(getActivity())
                    .getIdentitySign()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new NewBaseObserver<IdentitySign>(getActivity()) {
                        @Override
                        public void onSuccess(IdentitySign data) {
                            super.onSuccess(data);
                            showSending(false);

                            identitySign = data;
                            downloadFile();
                        }

                        @Override
                        public void onFail(int errorCode, @NonNull String error) {
                            super.onFail(errorCode, error);
                            showSending(false);
                        }
                    });
            showSending(true);
        }
    }

    private void downloadFile() {
        ((IdentityActivity) getActivity()).downloadIdentitySignFile(identitySign.documentId);
    }
}
