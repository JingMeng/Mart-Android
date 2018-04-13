package net.coding.mart.user;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.widget.EditText;
import android.widget.TextView;

import net.coding.mart.R;
import net.coding.mart.activity.user.setting.ModifyWorkEmailActivity_;
import net.coding.mart.activity.user.setting.ModifyWorkPhoneActivity_;
import net.coding.mart.common.BackActivity;
import net.coding.mart.common.MyData;
import net.coding.mart.json.BaseObserver;
import net.coding.mart.json.CurrentUser;
import net.coding.mart.json.Network;
import net.coding.mart.json.NewBaseObserver;
import net.coding.mart.json.PhoneCountry;
import net.coding.mart.json.mart2.user.City;
import net.coding.mart.json.mart2.user.MartUser;
import net.coding.mart.login.LoginActivity;

import org.androidannotations.annotations.AfterTextChange;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@EActivity(R.layout.activity_set_account)
public class SetAccountActivity extends BackActivity {

    private static final int RESULT_LOCAL = 1;
    private static final int RESULT_PICK_COUNTRY_PHONE = 2;
    private static final int RESULT_MODIFY_PHONE = 3;
    private static final int RESULT_MODIFY_EMAIL = 4;

    @ViewById
    EditText name, qq;

    @ViewById
    TextView local, countryCode, email, phone, emailValid, phoneValid;

    private PhoneCountry pickCountry = PhoneCountry.getChina();

    @AfterViews
    protected void initSetAccountActivity() {
        CurrentUser mUser = MyData.getInstance().getData();
        mInput = new UserinfoInput(mUser);
        uiBindData();
        loadData();
    }

    @AfterTextChange
    void phone(Editable s) {
        mInput.phone = s.toString();
        updateSendMessageUI();
    }

    @Click
    void email() {
        ModifyWorkEmailActivity_.intent(this).startForResult(RESULT_MODIFY_EMAIL);
    }

    @Click
    void phoneLayout() {
        ModifyWorkPhoneActivity_.intent(this).startForResult(RESULT_MODIFY_PHONE);
    }

    @Click
    void phone() {
        phoneLayout();
    }

    @Click
    void countryCode() {
        phoneLayout();
    }

    @Click
    void sendButton() {
        dataBindUI();

        if (mInput.getLocalString().replaceAll(" ", "").isEmpty()) {
            showMiddleToast("未填写所在地");
            return;
        }

        Map<String, String> map = new HashMap<>();
        map.put("name", mInput.name);
        map.put("qq", mInput.qq);
        Local local = mInput.local;
        map.put("province", String.valueOf(local.provicen.id));
        map.put("city", String.valueOf(local.city.id));
        map.put("district", String.valueOf(local.district.id));

        Network.getRetrofit(this)
                .modifyUser(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NewBaseObserver<MartUser>(this) {
                    @Override
                    public void onSuccess(MartUser data) {
                        super.onSuccess(data);
                        MyData.getInstance().update(SetAccountActivity.this, data);

                        showSending(false, "");
                        showButtomToast("修改个人信息成功");
                        finish();
                    }

                    @Override
                    public void onFail(int errorCode, @NonNull String error) {
                        super.onFail(errorCode, error);
                        showSending(false, "");
                    }
                });

        showSending(true, "正在保存...");
    }

    @Click
    void local() {
        Network.getRetrofit(this)
                .getCity(0, 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<List<City>>(SetAccountActivity.this) {
                    @Override
                    public void onSuccess(List<City> citys) {
                        super.onSuccess(citys);
                        Intent intent = new Intent(SetAccountActivity.this, PickLocalActivity.class);
                        intent.putExtra(PickLocalActivity.EXTRA_LOCAL_POS, 1);
                        intent.putExtra(PickLocalActivity.EXTRA_LIST_DATA, (ArrayList<City>) citys);
                        intent.putExtra(PickLocalActivity.EXTRA_LOCAL, mInput.local);
                        startActivityForResult(intent, RESULT_LOCAL);

                        showSending(false, "");
                    }

                    @Override
                    public void onFail(int errorCode, @NonNull String error) {
                        super.onFail(errorCode, error);
                        showSending(false, "");
                    }
                });

        showSending(true, "");
    }

    @Override
    public void onPause() {
        super.onPause();
        dataBindUI();
    }

    private void updateSendMessageUI() {
        CurrentUser user = MyData.getInstance().getData();
        if (mInput.phone.equals(user.getPhone())) {
//            phoneCode.setVisibility(View.GONE);
//            phoneCode.setText("");
            mInput.code = "";
            if (!user.getPhone().isEmpty()) {
//                phoneValid.setVisibility(View.VISIBLE);
            } else {
//                phoneValid.setVisibility(View.GONE);
            }

//            sendPhoneMessage.setVisibility(View.GONE);
        } else {
//            phoneCode.setVisibility(View.VISIBLE);
//            sendPhoneMessage.setVisibility(View.VISIBLE);
//            phoneValid.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        dataBindUI();
        if (!mInput.isModify()) {
            finish();
        } else {
            new AlertDialog.Builder(SetAccountActivity.this)
                    .setTitle("返回后，修改的数据不会被保存")
                    .setPositiveButton("确定返回", (dialog, which) -> finish())
                    .setNegativeButton("取消", null)
                    .show();
        }
    }

    @OnActivityResult(RESULT_LOCAL)
    void onResultLocal(int resultCode, @OnActivityResult.Extra Local result) {
        if (resultCode == RESULT_OK) {
            mInput.local = result;
            uiBindData();
        }
    }

    @OnActivityResult(RESULT_PICK_COUNTRY_PHONE)
    void onResultPickCountry(int resultCode, @OnActivityResult.Extra PhoneCountry resultData) {
        if (resultCode == Activity.RESULT_OK && resultData != null) {
            pickCountry = resultData;
            mInput.phoneCountryCode = pickCountry.getCountryCode();
            mInput.country = pickCountry.iso_code;
            uiBindData();
        }
    }

    @OnActivityResult(RESULT_MODIFY_PHONE)
    void resultModifyPhone(int resultCode) {
        if (resultCode == RESULT_OK) {
            CurrentUser user = MyData.getInstance().getData();
            mInput.phone = user.getPhone();
//            mInput.phoneCountryCode = user.getPhoneCountryCode().replace("+", "");
            pickCountry = PhoneCountry.createByCountryCode(user.getPhoneCountryCode(), this);

            mInput.phoneCountryCode = pickCountry.country_code;
            mInput.country = pickCountry.iso_code;
            uiBindData();
        }
    }

    @OnActivityResult(RESULT_MODIFY_EMAIL)
    void onResultModifyEmail(int resultCode) {
        if (resultCode == RESULT_OK) {
            CurrentUser user = MyData.getInstance().getData();
            mInput.email = user.getEmail();
            uiBindData();
        }
    }

    UserinfoInput mInput = new UserinfoInput();

    private void uiBindData() {
        email.setText(mInput.getEmail());
        phone.setText(mInput.getPhone());
        name.setText(mInput.getName());
        qq.setText(mInput.getQq());
        local.setText(mInput.getLocalString());
        countryCode.setText(mInput.getPhoneCountryCode());

        int colorBlue = getResources().getColor(R.color.font_blue);
        int colorRed = getResources().getColor(R.color.font_red);

        CurrentUser user = MyData.getInstance().getData();
        if (user.isPhoneValide()) {
            phoneValid.setText("已验证");
            phoneValid.setTextColor(colorBlue);
        } else {
            phoneValid.setText("未验证");
            phoneValid.setTextColor(colorRed);
        }

        if (user.isEmailValide()) {
            emailValid.setText("已验证");
            emailValid.setTextColor(colorBlue);
        } else {
            emailValid.setText("未验证");
            emailValid.setTextColor(colorRed);
        }
    }

    private void dataBindUI() {
        mInput.email = email.getText().toString();
        mInput.phone = phone.getText().toString();
        mInput.name = name.getText().toString();
        mInput.qq = qq.getText().toString();
    }

    private void loadData() {
        LoginActivity.loadCurrentUser(this, new LoginActivity.LoadUserCallback() {
            @Override
            public void onSuccess() {
                mInput = new UserinfoInput(MyData.getInstance().getData());
                uiBindData();
                showSending(false, "");
            }

            @Override
            public void onFail() {
            }
        });
    }

    static public class UserinfoInput {
        String name = "";
        String phone = "";
        String phoneCountryCode = "";
        String email = "";
        String qq = "";
        Local local = new Local();
        String code = "";
        String country = "";

        UserinfoInput(CurrentUser user) {
            name = user.getName();
            phone = user.getPhone();
            email = user.getEmail();
            local = new Local(user.getProvince(), user.getCity(), user.getDistrict());
            qq = user.getQq();
            phoneCountryCode = user.getPhoneCountryCode();
        }

        public UserinfoInput() {
        }

        public boolean isModify() {
            CurrentUser user = MyData.getInstance().getData();
            return !(name.equals(user.getName())
                    && qq.equals(user.getQq())
                    && local.city.id == user.getCity().id
                    && local.provicen.id == user.getProvince().id
                    && local.district.id == user.getDistrict().id);
        }

        public String getName() {
            return name;
        }

        public String getPhone() {
            return phone;
        }

        public String getEmail() {
            return email;
        }

        public String getQq() {
            return qq;
        }

        public String getLocalString() {
            return String.format("%s %s %s", local.provicen.name, local.city.name, local.district.name);
        }

        public String getPhoneCountryCode() {
            return phoneCountryCode;
        }

        public String getCountry() {
            return country;
        }

        public Local getLocal() {
            return local;
        }
    }

    static public class Local implements Serializable {

        private static final long serialVersionUID = 3563970233735846772L;

        City provicen = new City();
        City city = new City();
        City district = new City();

        public Local() {
        }

        public Local(City provicen, City city, City district) {
            this.provicen = provicen;
            this.city = city;
            this.district = district;
        }

    }
}
