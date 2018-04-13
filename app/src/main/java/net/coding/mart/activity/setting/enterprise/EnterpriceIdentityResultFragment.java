package net.coding.mart.activity.setting.enterprise;


import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkBuilder;

import net.coding.mart.R;
import net.coding.mart.common.Color;
import net.coding.mart.common.Global;
import net.coding.mart.common.ImageLoadTool;
import net.coding.mart.json.enterprise.EnterpriseCertificate;
import net.coding.mart.user.identityAuthentication.PhotoDetailActivity_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_enterprice_identity_result)
public class EnterpriceIdentityResultFragment extends Fragment {

    @FragmentArg
    EnterpriseCertificate info;

    @ViewById
    View icon;

    @ViewById
    TextView title, tipContent;

    @ViewById
    TextView name, idCard;

    @ViewById
    ImageView businessLicence;

    @ViewById
    TextView sendButton;

    @AfterViews
    void initEnterpriceIdentityWaitFragment() {
        String failContent;
        switch (info.status) {
            case "processing":
                icon.setBackgroundResource(R.mipmap.ic_enterprise_process);
                title.setText("认证审核中");
                tipContent.setText("认证审核需要 1 - 3 个工作日，如需修改认证信息请 联系客服");
                break;

            case "failed":
                icon.setBackgroundResource(R.mipmap.ic_enterprise_fail);
                title.setText("认证未通过！");
                failContent = String.format("很遗憾，您的企业认证失败。\n原因：%s。\n请重新提交认证，如有疑问请 联系客服", info.rejectReason);
                tipContent.setText(failContent);
                sendButton.setVisibility(View.VISIBLE);
                break;

            default: // "successful":
                icon.setBackgroundResource(R.mipmap.ic_enterprise_success);
                title.setText("认证通过！");
                name.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.mipmap.ic_enterprise_flag_passed, 0);
                tipContent.setText("验证通过后无法自行修改。如果需要修改认证信息请 联系客服");
                break;
        }

        Global.addLinkCustomerService(getActivity(), tipContent);

        name.setText(info.legalRepresentative);

        Link idCardLink = new Link(info.businessLicenceNo).setUnderlined(false).setTextColor(Color.font_2);
        idCard.setText(String.format("营业执照编号：%s", info.businessLicenceNo));
        LinkBuilder.on(idCard).addLink(idCardLink).build();

        ImageLoadTool.loadImage(businessLicence, info.attachment.url);
    }

    @Click
    void businessLicence() {
        PhotoDetailActivity_.intent(this).url(info.attachment.url).showDelete(false).start();
    }

    @Click
    void sendButton() {
        ((EnterpriseIdentityActivity) getActivity()).switchFragment(null);
    }
}
