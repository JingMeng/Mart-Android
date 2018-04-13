package net.coding.mart.activity.reward;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkBuilder;

import net.coding.mart.R;
import net.coding.mart.WebActivity_;
import net.coding.mart.activity.mpay.FinalPayOrdersActivity_;
import net.coding.mart.activity.setting.enterprise.EnterpriseMainActivity_;
import net.coding.mart.common.BackActivity;
import net.coding.mart.common.Color;
import net.coding.mart.common.Global;
import net.coding.mart.common.GlobalData_;
import net.coding.mart.common.MyData;
import net.coding.mart.common.constant.ProjectStatus;
import net.coding.mart.common.constant.RewardType;
import net.coding.mart.common.umeng.UmengEvent;
import net.coding.mart.common.util.ActivityNavigate;
import net.coding.mart.common.util.RxBus;
import net.coding.mart.common.util.SendVerifyCodeHelp;
import net.coding.mart.common.util.ViewStyleUtil;
import net.coding.mart.common.widget.SimpleTextWatcher;
import net.coding.mart.common.widget.WordCountEditText;
import net.coding.mart.developers.SavePriceActivity;
import net.coding.mart.json.BaseObserver;
import net.coding.mart.json.CurrentUser;
import net.coding.mart.json.Network;
import net.coding.mart.json.NewBaseObserver;
import net.coding.mart.json.PhoneCountry;
import net.coding.mart.json.RoleType;
import net.coding.mart.json.body.NewReward;
import net.coding.mart.json.mpay.Order;
import net.coding.mart.json.reward.IndustryName;
import net.coding.mart.json.reward.IndustryPager;
import net.coding.mart.json.reward.Project;
import net.coding.mart.json.reward.project.ProjectPublish;
import net.coding.mart.login.CountryPickActivity_;
import net.coding.mart.login.LoginActivity_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@EActivity(R.layout.activity_publish_reward)
public class PublishRewardActivity extends BackActivity {

    private static final int RESULT_PICK_COUNTRY = 1;
    private static final int RESULT_LOGIN = 2;
    private static final int RESULT_INDUSTRY_LIST = 5;
    private static final int REWARD_PRICE_MIN = 1000;
    @Extra
    RewardType type = null;
    @Extra
    ProjectPublish projectPublish;
    @ViewById
    TextView topTip, jobIndustryTextView, jobTypeTextView, countryCode, inputTip,
            tryDeveloper;
    @ViewById
    EditText jobName, jobPrice;
    @ViewById
    WordCountEditText jobDescription;
    @ViewById
    EditText name, email, phone, phoneCode;
    @ViewById
    TextView sendPhoneMessage;
    @ViewById
    CheckBox secretCheckBox;
    @ViewById
    View phoneLayout0, phoneLayout1;
    @ViewById(R.id.sendButton)
    TextView sendButton;
    @ViewById
    TextView nextButtonTip;
    @ViewById(R.id.cardPrice)
    TextView cardPrice;
    @ViewById(R.id.checkBoxBargain)
    CheckBox checkBoxBargain;
    @ViewById(R.id.jobRequest)
    WordCountEditText rewardDemand;
    @ViewById(R.id.jobDuration)
    EditText jobDuration;
    @ViewById(R.id.checkBoxTeam)
    CheckBox checkBoxTeam;
    @ViewById(R.id.checkboxPerson)
    CheckBox checkBoxPerson;
    @ViewById(R.id.personType)
    TextView personType;
    @ViewById(R.id.paymentRedTip)
    TextView paymentRedTip;
    @ViewById(R.id.currentPaymnet)
    TextView currentPaymnet;
    @ViewById(R.id.cardTip)
    TextView cardTip;
    @ViewById(R.id.cardTip2)
    TextView cardTip2;
    @ViewById(R.id.paymentCard)
    View paymentCard;
    @ViewById(R.id.nextButtonTipCheck)
    CheckBox nextButtonTipCheck;

    @Pref
    GlobalData_ globalData;

    SendVerifyCodeHelp sendVerifyCodeHelp;
    private PhoneCountry pickCountry = PhoneCountry.getChina();
    private ArrayList<IndustryName> allIndustrys = new ArrayList<>();
    private NewReward mParam = new NewReward();
    private boolean isLoginMode = false;
    private List<RoleType> roleTypes = new ArrayList<>();

    TextWatcher watcher = new SimpleTextWatcher() {
        @Override
        public void afterTextChanged(Editable s) {
            uiBindNextButton();
        }
    };

    @AfterViews
    void initPublishRewardActivity() {
        CurrentUser me = MyData.getInstance().getData();
        topTip.setVisibility(View.GONE);
        if (me.isEnterpriseAccout()) {
            if (!me.isPassEnterpriceIdentity()) {
                topTip.setVisibility(View.VISIBLE);
                Link link = new Link(" 去认证")
                        .setTextColor(Color.font_blue)
                        .setUnderlined(false);
                LinkBuilder.on(topTip).addLink(link).build();
            } else {
                topTip.setVisibility(View.GONE);
            }
        }

        nextButtonTip.setText(Global.createBlueHtml("", "《码市用户权责条款》", ""));
        tryDeveloper.setText(Global.createBlueHtml("该项目金额不含税，如需发票，请", "查看操作文档", ""));

        isLoginMode = MyData.getInstance().isLogin();

        if (type != null) {
            setType(type);
        }

        CurrentUser user = me;
        name.setText(user.getName());
        phone.setText(user.getPhone());
        email.setText(user.getEmail());

        if (isLoginMode) {
            ViewStyleUtil.editAddTextChange(watcher, jobName, jobPrice, jobDescription.getEditText(), name, email, phone, phoneCode);
        } else {
            ViewStyleUtil.editAddTextChange(watcher, jobName, jobPrice, jobDescription.getEditText(), name, email);
            TextView inputTip = (TextView) findViewById(R.id.inputTip);
            inputTip.setText("姓名与邮箱是让我们联系您的必要信息");
            phoneLayout0.setVisibility(View.GONE);
            phoneLayout1.setVisibility(View.GONE);
        }

        if (projectPublish != null) {
            mParam.industrys.addAll(projectPublish.getIndustrys());
            bindIndustryUI();

            setType(RewardType.newType2Enum(projectPublish.type));
            setBudge(String.valueOf(projectPublish.price));
            jobName.setText(projectPublish.name);
            jobDescription.setText(projectPublish.description);
            name.setText(projectPublish.contactName);
            email.setText(projectPublish.contactEmail);
            phone.setText(projectPublish.contactMobile);
            checkBoxBargain.setChecked(projectPublish.bargain);

            mParam.developerType = projectPublish.developerType;
            mParam.developerRole = projectPublish.developerRole;
            if (projectPublish.developerType == NewReward.DeveloperType.PERSONAL) {
                checkBoxPerson.setOnCheckedChangeListener(null);
                checkBoxPerson.setChecked(true);
                personType.setText(projectPublish.roles);
                checkBoxPerson.setOnCheckedChangeListener((buttonView, isChecked) -> checkPerson(isChecked));
            } else {
                checkBoxTeam.setChecked(true);
            }

            jobDuration.setText(String.valueOf(projectPublish.duration));
            rewardDemand.setText(projectPublish.rewardDemand);

            checkBoxBargain.setChecked(projectPublish.bargain);
            mParam.bargain = projectPublish.bargain;

            paymentCard.setVisibility(View.GONE);
        }

        if (MyData.getInstance().getData().isEnterpriseAccout()) {
            paymentCard.setVisibility(View.GONE);
            sendButton.setText("发布");
        }

        try {
            JSONArray jsonArray = new JSONArray(Global.readTextFile(this, "country"));
            for (int i = 0; i < jsonArray.length(); ++i) {
                PhoneCountry item = new PhoneCountry(jsonArray.optJSONObject(i));
                if (user.getPhoneCountryCode().equals(item.country_code)) {
                    pickCountry = item;
                    break;
                }
            }
        } catch (Exception e) {
            Global.errorLog(e);
        }

        sendVerifyCodeHelp = new SendVerifyCodeHelp(sendPhoneMessage, phoneCode);

        bindCountry();

        loadIndustry(false);

        String priceString = "（原价：<S>¥99/次</S> ）";
        cardPrice.setText(Html.fromHtml(priceString));

        if (!TextUtils.isEmpty(globalData.projectPublishPaymentDeadline().get())) {
            paymentRedTip.setText(globalData.projectPublishPaymentDeadTitle().get());
            String currentPayment = globalData.projectPublishPayment().get();
            currentPaymnet.setText("¥" + currentPayment);
            cardTip.setText(globalData.projectPublishPaymentTip().get());
            cardTip2.setText(globalData.projectPublishPaymentDeadline().get());

            try {
                if (Double.valueOf(currentPayment) == 0) {
                    sendButton.setText("发布");
                }
            } catch (Exception e) {
                Global.errorLog(e);
            }
        }

        if (projectPublish != null &&
                projectPublish.status == ProjectStatus.RECRUITING) {
            sendButton.setText("保存");
        }
    }

    @Click
    void countryCode() {
        CountryPickActivity_.intent(this)
                .startForResult(RESULT_PICK_COUNTRY);
    }

    @Click
    void topTip() {
        EnterpriseMainActivity_.intent(this).start();
    }

    @Click(R.id.teamLayout)
    void teamLayout() {
        checkBoxTeam.setChecked(true);
    }

    @Click(R.id.personLayout)
    void personLayout() {
        if (!checkBoxPerson.isChecked()) {
            checkBoxPerson.setChecked(true);
        } else {
            personType.performClick();
        }
    }

    @CheckedChange(R.id.checkBoxTeam)
    void checkTeam(boolean check) {
        if (check) {
            mParam.developerType = NewReward.DeveloperType.TEAM;
            mParam.developerRole = net.coding.mart.common.constant.RoleType.Team.id;

            checkBoxPerson.setChecked(false);
        }
    }

    @CheckedChange(R.id.checkboxPerson)
    void checkPerson(boolean check) {
        if (check) {
            mParam.developerType = NewReward.DeveloperType.PERSONAL;
            checkBoxTeam.setChecked(false);
            personType.performClick();
        } else {
            personType.setText("");
        }
    }

    @Click(R.id.personType)
    void personType() {
        if (checkBoxPerson.isChecked()) {
            popDialogPersonType();
        } else {
            checkBoxPerson.setChecked(true);
        }
    }

    private void popDialogPersonType() {
        if (roleTypes.isEmpty()) {
            Network.getRetrofit(this)
                    .getPublishRoleTypes()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<List<RoleType>>(this) {
                        @Override
                        public void onSuccess(List<RoleType> data) {
                            super.onSuccess(data);
                            showSending(false);

                            roleTypes.clear();
                            roleTypes.addAll(data);
                            realPopDialogPersonType();
                        }

                        @Override
                        public void onFail(int errorCode, @NonNull String error) {
                            super.onFail(errorCode, error);
                            showSending(false);
                        }
                    });
            showSending(true);
        } else {
            realPopDialogPersonType();
        }
    }

    private void realPopDialogPersonType() {
        String[] types = new String[roleTypes.size()];
        for (int i = 0; i < types.length; ++i) {
            types[i] = roleTypes.get(i).name;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("角色类型")
                .setItems(types, (dialog, which) -> {
                    RoleType pickRole = roleTypes.get(which);
                    personType.setText(pickRole.name);
                    mParam.developerRole = pickRole.id;
                });
        builder.show();
    }

    @OnActivityResult(RESULT_LOGIN)
    void onResultLogin(int resultCode) {
        if (MyData.getInstance().isLogin()) {
            reward();
        }
    }

    @CheckedChange(R.id.checkBoxBargain)
    public void checkBoxBargain(boolean check) {
        mParam.bargain = check;
    }

    @OnActivityResult(RESULT_PICK_COUNTRY)
    void onResultPickCountry(int resultCode, @OnActivityResult.Extra PhoneCountry resultData) {
        if (resultCode == Activity.RESULT_OK && resultData != null) {
            pickCountry = resultData;
            bindCountry();
        }
    }

    private void setBudge(String price) {
        mParam.price = price;
        jobPrice.setText(price);
    }

    private void bindCountry() {
        countryCode.setText(pickCountry.getCountryCode());

        mParam.phoneCountryCode = pickCountry.getCountryCode();
        mParam.country = pickCountry.iso_code;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void clseEvent(String message) {
        if (message.equals(SavePriceActivity.HUOGUO_SAVE_PRICE)) {
            finish();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        sendVerifyCodeHelp.stop();
    }

    private void loadIndustry(boolean jumpToList) {
        Network.getRetrofit(this)
                .getRewardIndustry()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NewBaseObserver<IndustryPager>(this) {
                    @Override
                    public void onSuccess(IndustryPager data) {
                        super.onSuccess(data);

                        allIndustrys.clear();
                        allIndustrys.addAll(data.industryName);

                        if (jumpToList) {
                            jumpToIndustryListActivity();
                        }
                    }

                });
    }

    private void jumpToIndustryListActivity() {
        ArrayList<IndustryName> picked;
        if (projectPublish != null) {
            picked = projectPublish.getIndustrys();
        } else {
            picked = new ArrayList<>();
        }

        IndustryListActivity_.intent(this)
                .allIndustrys(allIndustrys)
                .pickedIndustrys(picked)
                .startForResult(RESULT_INDUSTRY_LIST);
    }

    @OnActivityResult(RESULT_INDUSTRY_LIST)
    void onResultIndustryList(int resultCode, @OnActivityResult.Extra ArrayList<IndustryName> intentData) {
        if (resultCode == RESULT_OK && intentData != null) {
            mParam.industrys.clear();
            mParam.industrys.addAll(intentData);
            bindIndustryUI();
        }
    }

    private void setType(RewardType type) {
        mParam.type = type.id;
        jobTypeTextView.setText(type.alias);
        jobTypeTextView.setTextColor(0xff666666);
    }

    private void bindIndustryUI() {
        String industry = IndustryName.createName(mParam.industrys);
        jobIndustryTextView.setText(industry);
    }

    @Click
    void tryDeveloper() {
//        HuoguoActivity_.intent(this).start();
        String url = "https://codemart.com/help/question/16";
        WebActivity_.intent(this).url(url).start();
    }

    @Click(R.id.requestTip)
    void requestTip() {
        new AlertDialog.Builder(this)
                .setView(R.layout.dialog_publish_request_description)
                .setCancelable(true)
                .show();
    }

    @Click
    void jobDescriptionTip() {
        new AlertDialog.Builder(this)
                .setView(R.layout.dialog_publish_reward_description)
                .setCancelable(true)
                .show();
    }

    @Click
    void jobIndustryTextView() {
        if (allIndustrys.isEmpty()) {
            loadIndustry(true);
        } else {
            jumpToIndustryListActivity();
        }
    }

    @Click
    void jobTypeTextView() {
        final String[] types = new String[]{
                RewardType.web.alias,
                RewardType.mobile.alias,
                RewardType.wechat.alias,
                RewardType.weapp.alias,
                RewardType.html5.alias,
                RewardType.other.alias
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("项目类型")
                .setItems(types, (dialog, which) -> {
                    String title = types[which];
                    setType(RewardType.name2Enum(title));
                    uiBindNextButton();
                });
        builder.show();
    }

    @Click
    void contract() {
        ActivityNavigate.startPublishAgreement(this);
    }

    @Click
    void nextButtonTip() {
        contract();
    }

    @Click
    void sendPhoneMessage() {
        String phoneNumber = phone.getText().toString();
        if (phoneNumber.isEmpty()) {
            showMiddleToast("手机号不能为空");
            return;
        }

        sendVerifyCodeHelp.begin(this, phoneNumber, pickCountry.getCountryCode());
    }

    @CheckedChange
    void secretCheckBox(boolean check) {
        uiBindNextButton();
    }

    @Click
    void sendButton() {
        if (MyData.getInstance().isLogin()) {
            String projectName = jobName.getText().toString();
            String projectDescription = jobDescription.getText();
            String price = jobPrice.getText().toString();

            if (!checkInputName(projectName)
                    || !checkInputDescription(projectDescription)
                    || !checkInputPrice(price)
                    || !checkInputDevType()) {
                return;
            }

            reward();
        } else {
            LoginActivity_.intent(this).startForResult(RESULT_LOGIN);
        }
    }

    private void reward() {
        CurrentUser user = MyData.getInstance().getData();
        if (user.isEnterpriseAccout() && !user.isPassEnterpriceIdentity()) {
            topTip();
            return;
        }

        showSending(false);

        String price = jobPrice.getText().toString();
        String projectName = jobName.getText().toString();
        String projectDescription = jobDescription.getText();
        String userName = name.getText().toString();
        String userEmail = email.getText().toString();
        String phoneString = phone.getText().toString();
        String phoneCodeString = phoneCode.getText().toString();
        String duration = jobDuration.getText().toString();

        if (!checkInputName(projectName)
                || !checkInputDescription(projectDescription)
                || !checkInputPrice(price)
                || !checkInputDevType()) {
            return;
        }

        if (TextUtils.isEmpty(duration)) {
            showMiddleToast("请填写项目周期");
            return;
        }

        if (!nextButtonTipCheck.isChecked()) {
            showMiddleToast("请同意遵守《码市用户权责条款》");
            return;
        }

        mParam.price = price;
        mParam.name = projectName;
        mParam.description = projectDescription;
        mParam.contact_name = userName;
        mParam.contact_email = userEmail;
        mParam.rewardDemand = rewardDemand.getText().toString();
        mParam.duration = Integer.valueOf(duration);

        if (phoneLayout0.getVisibility() == View.VISIBLE) {
            mParam.contact_mobile = phoneString;
            mParam.contact_mobile_code = phoneCodeString;
        } else {
            mParam.contact_mobile = user.getPhone();
            mParam.contact_mobile_code = "";
            mParam.phoneCountryCode = user.getPhoneCountryCode();
            mParam.country = user.getPhoneIsoCode();
        }

        if (projectPublish != null) {
            mParam.id = String.valueOf(projectPublish.id);
        }

        Network.getRetrofit(this)
                .createReward(mParam.createMap())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NewBaseObserver<Project>(PublishRewardActivity.this) {
                    @Override
                    public void onSuccess(Project data) {

                        if (data.status == ProjectStatus.NO_PAYMENT) {
                            jumpPayActivity(data.id);
                        } else {
                            closeActivity();
                        }
                    }

                    @Override
                    public void onFail(int errorCode, @NonNull String error) {
                        super.onFail(errorCode, error);
                        showSending(false, "");
                    }
                });
        showSending(true, "");

        String umengEvent = String.format("发布需求 _ %s _ 点击提交", RewardType.idToName(mParam.type));
        umengEvent(UmengEvent.ACTION, umengEvent);
    }

    public void jumpPayActivity(String id) {
        Network.getRetrofit(this)
                .createRwardOrder(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NewBaseObserver<Order>(this) {
                    @Override
                    public void onSuccess(Order data) {
                        super.onSuccess(data);

                        ArrayList<String> ids = new ArrayList<>();
                        ids.add(data.orderId);
                        FinalPayOrdersActivity_.intent(PublishRewardActivity.this)
                                .orderIds(ids)
                                .start();

                        closeActivity();
                    }

                    @Override
                    public void onFail(int errorCode, @NonNull String error) {
                        super.onFail(errorCode, error);
                        closeActivity();
                    }
                });
    }

    private void closeActivity() {
        RxBus.getInstance().send(new RxBus.RewardPublishSuccess());
        setResult(RESULT_OK);
        finish();
    }

    private boolean checkInputName(String projectName) {
        if (30 < projectName.length()) {
            showMiddleToast("项目名不能多于 30 个字");
            return false;
        }

        return true;
    }

    private boolean checkInputDescription(String descrip) {
        if (descrip.length() < 20) {
            showMiddleToast("项目描述至少需要填写 20 个字");
            return false;
        } else if (1000 < descrip.length()) {
            showMiddleToast("项目描述不能多于 1000 个字");
            return false;
        }

        return true;
    }

    private boolean checkInputPrice(String price) {
        try {
            double value = Double.valueOf(price);
            if (value < REWARD_PRICE_MIN) {
                showMiddleToast("项目金额不能少于 1000");
                return false;
            }
            return true;
        } catch (Exception e) {
            showMiddleToast("请输入有效的项目金额");
            return false;
        }
    }

    public boolean checkInputDevType() {
        if (mParam.developerType == null) {
            showMiddleToast("请选择开发者类型");
            return false;
        }

        if (mParam.developerType == NewReward.DeveloperType.PERSONAL && personType.getText().length() == 0) {
            showMiddleToast("招募个人请选择角色类型");
            return false;
        }

        return true;
    }

    void uiBindNextButton() {
        if (isLoginMode) {
            if (jobPrice.length() > 0
                    && mParam.type != -1
                    && jobName.length() > 0
                    && jobDescription.getEditText().length() > 0
                    && name.length() > 0
                    && phone.length() > 0
                    && phoneCode.length() > 0
                    && email.length() > 0
                    && secretCheckBox.isChecked()) {
                sendButton.setEnabled(true);
            } else {
                sendButton.setEnabled(false);
            }
        } else {
            if (jobPrice.length() > 0
                    && mParam.type != -1
                    && jobName.length() > 0
                    && jobDescription.getEditText().length() > 0
                    && name.length() > 0
                    && email.length() > 0
                    && secretCheckBox.isChecked()) {
                sendButton.setEnabled(true);
            } else {
                sendButton.setEnabled(false);
            }
        }
    }
}
