package net.coding.mart.login;

import android.view.View;

import net.coding.mart.R;
import net.coding.mart.common.BaseFragment;
import net.coding.mart.common.util.ActivityNavigate;
import net.coding.mart.common.util.ViewStyleUtil;
import net.coding.mart.common.widget.SendValidateCodeHelp;
import net.coding.mart.json.PhoneCountry;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import java.util.Map;

@EFragment(R.layout.fragment_phone_set_password)
public class PhoneVerifyFragment extends BaseFragment {

    private static final int RESULT_PICK_COUNTRY = 1;

    @FragmentArg
    String account = "";

    private SendValidateCodeHelp validateHelp;

    @ViewById
    View loginButton;

    @AfterViews
    void initPhoneVerifyFragment() {
        validateHelp = new SendValidateCodeHelp(this, SendValidateCodeHelp.Action.PASSWORD);
        ViewStyleUtil.editTextBindButton(loginButton, validateHelp.phone, validateHelp.captchaEdit);
    }

    @Click
    void loginButton() {
        chekcPhoneCode();
    }

    @Click
    void countryCode() {
        CountryPickActivity_.intent(this)
                .startForResult(RESULT_PICK_COUNTRY);
    }

    protected void chekcPhoneCode() {
        validateHelp.checkPhoneCode(new LoginActivity.LoadUserCallback() {
            @Override
            public void onSuccess() {

                SetPasswordCallback parent = (SetPasswordCallback) getActivity();
                Map<String, String> map = parent.getRequestParmas();
                map.put("phone", validateHelp.phone.getTextString());
                map.put("verificationCode", validateHelp.captchaEdit.getTextString());
                map.put("isoCode", validateHelp.pickCountry.iso_code);
                map.put("countryCode", validateHelp.pickCountry.getCountryCode());

                parent.next();
            }

            @Override
            public void onFail() {
            }
        });

    }

    @Override
    public void onStop() {
        super.onStop();
        validateHelp.countDownTimerStop();
    }

    @OnActivityResult(RESULT_PICK_COUNTRY)
    void onResultPickCountry(int resultCode, @OnActivityResult.Extra PhoneCountry resultData) {
        validateHelp.bind(resultCode, resultData);
    }

    @Click
    void textClause() {
        ActivityNavigate.startServiceTerm(getActivity());
    }
}
