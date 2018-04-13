package net.coding.mart.common;

import android.widget.TextView;

import net.coding.mart.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

/*
 *  显示一些静态页面
 */
@EActivity(R.layout.identity_instruction)
public class CommonBackActivity extends BackActivity {

    @ViewById
    TextView content;

    public enum Type {
        identity,
        mpay
    }

    @Extra
    Type type = Type.identity;

    @AfterViews
    void initCommonStaticActivity() {
        String titleString;
        int contentStringRes;
        if (type == Type.mpay) {
            titleString = "码市开发宝服务协议";
            contentStringRes = R.string.mpay_agreement;
        } else {
            titleString = "认证说明";

            contentStringRes = R.string.identity_authentication_comment;
        }

        setActionBarTitle(titleString);
        content.setText(contentStringRes);
    }
}