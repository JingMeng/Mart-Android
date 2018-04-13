package net.coding.mart.activity.reward.detail.coder;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.coding.mart.LengthUtil;
import net.coding.mart.R;
import net.coding.mart.activity.mpay.FinalPayOrdersActivity_;
import net.coding.mart.activity.reward.detail.v2.V2RewardDetailActivity;
import net.coding.mart.activity.user.setting.CommonDialog;
import net.coding.mart.common.BackActivity;
import net.coding.mart.common.BaseActivity;
import net.coding.mart.common.Global;
import net.coding.mart.common.ImageLoadTool;
import net.coding.mart.common.event.CallPhoneEvent;
import net.coding.mart.common.widget.EmptyRecyclerView;
import net.coding.mart.json.BaseHttpResult;
import net.coding.mart.json.Network;
import net.coding.mart.json.NewBaseObserver;
import net.coding.mart.json.mpay.Order;
import net.coding.mart.json.reward.Coder;
import net.coding.mart.json.reward.user.ApplyContact;
import net.coding.mart.json.reward.user.ApplyContactResult;
import net.coding.mart.json.reward.user.ApplyResume;
import net.coding.mart.json.reward.user.Attach;
import net.coding.mart.json.reward.user.Project;
import net.coding.mart.json.reward.user.Role;
import net.coding.mart.setting.AboutActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static net.coding.mart.R.id.skills;
import static net.coding.mart.json.reward.Coder.Status.refuse;

@EActivity(R.layout.activity_coder_info)
public class CoderInfoActivity extends BackActivity implements ApplyCallback {

    private static final int RESULT_CODER_LIST = 1;
    private static final int RESULT_PAY = 2;

    @Extra
    boolean menuShowUserList = true;

    @Extra
    int rewardId;

    @Extra
    Coder coder;

    @Extra
    ArrayList<Coder> coders;
//    @ViewById
//    ViewGroup rootLayout;

    ApplyResume applyResume;

    @ViewById
    ImageView userIcon, applyStatus;

    @ViewById
    TextView userName, applyTime;

    @ViewById
    TextView userRoleType, userLocal, userApplyDescripion;

    @ViewById
    TextView userMobile, userQQ, userMail;

    @ViewById
    View rootScrollView;

    @ViewById
    ViewGroup roleListLayout, projectExpListLayout, contactLayout, noContactLayout,
            applyLayout, roleLayout, projectExpLayout;

    @ViewById
    View bottomLayout;

    @ViewById
    TextView buttonRefuse, buttonAccept;

    @ViewById
    EmptyRecyclerView emptyView;
    Intent resultIntent;
    View.OnClickListener clickRequstFree = v -> requestUserContact();
    View.OnClickListener clickRequestContactPay = v -> {
        Network.getRetrofit(this)
                .getApplyContactOrder(coder.applyId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NewBaseObserver<Order>(this) {
                    @Override
                    public void onSuccess(Order data) {
                        super.onSuccess(data);
                        showSending(false);

                        ArrayList<String> ids = new ArrayList<>();
                        ids.add(data.orderId);
                        FinalPayOrdersActivity_.intent(CoderInfoActivity.this)
                                .orderIds(ids)
                                .startForResult(RESULT_PAY);
                    }

                    @Override
                    public void onFail(int errorCode, @NonNull String error) {
                        super.onFail(errorCode, error);
                        showSending(false);
                    }
                });
        showSending(true);
    };

    public static void popRefuseDialog(BaseActivity activity, ApplyCallback applyCallback, Coder coder) {
        new CommonDialog.Builder(activity)
                .setTitle("拒绝合作")
                .setMessage("拒绝与此位开发者进行合作？")
                .setRightButton("确定", (v -> applyReject(activity, applyCallback, coder)))
                .show();
    }

    public static void popAcceptDialog(BaseActivity context, ApplyCallback applyCallback, Coder coder) {
        new CommonDialog.Builder(context)
                .setTitle("选择开发者")
                .setMessage("确定选择此开发者进行项目合作？")
                .setRightButton("确定", v -> applyPass(context, applyCallback, coder))
                .show();
    }

    static void applyReject(BaseActivity activity, ApplyCallback applyCallback, Coder coder) {
        Network.getRetrofit(activity)
                .applyReject(coder.applyId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NewBaseObserver<BaseHttpResult>(activity) {
                    @Override
                    public void onSuccess(BaseHttpResult data) {
                        super.onSuccess(data);
                        activity.showSending(false);

                        coder.setStatus(refuse);
                        applyCallback.applyReject(coder);

                    }

                    @Override
                    public void onFail(int errorCode, @NonNull String error) {
                        super.onFail(errorCode, error);
                        activity.showSending(false);
                    }
                });

        activity.showSending(true);
    }

    public static void applyPass(BaseActivity activity, ApplyCallback applyCallback, Coder coder) {
        Network.getRetrofit(activity)
                .applyPass(coder.applyId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NewBaseObserver<BaseHttpResult>(activity) {
                    @Override
                    public void onSuccess(BaseHttpResult data) {
                        super.onSuccess(data);
                        activity.showSending(false);

                        coder.setStatus(Coder.Status.accept);

                        String title = "已选定开发者";
                        String message = String.format("您已选定 【%s】 的开发者 【%s】\n" +
                                        "请与开发者沟通详细需求，等待开发者提交阶段划分。 " +
                                        "确认阶段划分并支付第一阶段款项后，项目将正式启动。",
                                coder.roleType, coder.name);

                        new CommonDialog.Builder(activity)
                                .setTitle(title)
                                .setMessage(message)
                                .setHideLeftButton(true)
                                .setRightButton("确定", v -> applyCallback.applyAccept(coder))
                                .show();
                    }

                    @Override
                    public void onFail(int errorCode, @NonNull String error) {
                        super.onFail(errorCode, error);
                        activity.showSending(false);
                    }
                });

        activity.showSending(true);
    }

    @AfterViews
    void initCoderInfoActivity() {
        for (Coder item : coders) {
            if (item.applyId == coder.applyId) {
                coder = item;
                break;
            }
        }

        emptyView.setVisibility(View.VISIBLE);
        emptyView.setLoading();

        rootScrollView.setVisibility(View.INVISIBLE);

        loadData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (menuShowUserList) {
            getMenuInflater().inflate(R.menu.coder_info, menu);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @OptionsItem
    void actionCoderList() {
        CoderListActivity_.intent(this)
                .rewardId(rewardId)
                .coders(coders)
                .startForResult(RESULT_CODER_LIST);
    }

    @OnActivityResult(RESULT_CODER_LIST)
    void onResultCoder(int resultCode, @OnActivityResult.Extra ArrayList<Coder> coders,
                       @OnActivityResult.Extra boolean resultExtraRefrush) {
        if (resultCode == RESULT_OK) {
            if (resultExtraRefrush) {
                Intent intent = new Intent();
                intent.putExtra(V2RewardDetailActivity.resultExtraRefrush, true);
                setResult(RESULT_OK, intent);
                finish();
            } else if (coders != null) {
                this.coders.clear();
                this.coders.addAll(coders);

                for (Coder item : coders) {
                    if (this.coder.applyId == item.applyId) {
                        this.coder = item;
                        uiBindData();
                        break;
                    }
                }

                Intent intent = new Intent();
                intent.putExtra("coders", coders);
                setResult(RESULT_OK, intent);
            }
        }
    }

    @OnActivityResult(RESULT_PAY)
    void onResultPayApplyContact(int resultCode) {
        if (resultCode == RESULT_OK) {
            requestUserContact();
        }
    }

    private void loadData() {
        Network.getRetrofit(this)
                .getApplyResume(rewardId, coder.applyId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NewBaseObserver<ApplyResume>(this) {
                    @Override
                    public void onSuccess(ApplyResume data) {
                        super.onSuccess(data);

                        emptyView.setVisibility(View.GONE);
                        rootScrollView.setVisibility(View.VISIBLE);

                        applyResume = data;

                        uiBindData();
                    }

                });
    }

    private void hideBottomBar() {
        bottomLayout.setVisibility(View.GONE);
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) rootScrollView.getLayoutParams();
        lp.bottomMargin = 0;
        rootScrollView.setLayoutParams(lp);
        findViewById(R.id.bottomLine).setVisibility(View.GONE);
    }

    private void uiBindData() {
        if (coder != null) {
            ImageLoadTool.loadImageUser(userIcon, coder.avatar);
            userName.setText(coder.name);
            userName.setCompoundDrawablesWithIntrinsicBounds(null, null, coder.getCardDrawable(this), null);

            applyTime.setText(coder.getCreatedAtFormat());
            switch (coder.getStatus()) {
                case accept:
                    applyStatus.setVisibility(View.VISIBLE);
                    applyStatus.setImageResource(R.mipmap.ic_apply_accept);

                    bottomLayout.setVisibility(View.VISIBLE);
                    buttonRefuse.setVisibility(View.VISIBLE);
                    buttonAccept.setVisibility(View.GONE);
                    buttonRefuse.setText("取消合作");

                    if (coder.stagePayed) {
                        hideBottomBar();
                    }

                    break;
                case refuse:
                    applyStatus.setVisibility(View.VISIBLE);
                    applyStatus.setImageResource(R.mipmap.ic_apply_refuse);

                    hideBottomBar();
                    break;
                default:
                    applyStatus.setVisibility(View.VISIBLE);
                    applyStatus.setImageResource(R.mipmap.ic_apply_default);

                    bottomLayout.setVisibility(View.VISIBLE);
                    buttonRefuse.setVisibility(View.VISIBLE);
                    buttonAccept.setVisibility(View.VISIBLE);
            }

            if (TextUtils.isEmpty(coder.mobile)) {
                noContactLayout.setVisibility(View.VISIBLE);
                contactLayout.setVisibility(View.GONE);
            } else {
                noContactLayout.setVisibility(View.GONE);
                contactLayout.setVisibility(View.VISIBLE);

                userMobile.setText(coder.mobile);
                userQQ.setText(coder.qq);
                userMail.setText(coder.email);
            }
        }

        userRoleType.setText(applyResume.devType);
        userLocal.setText(applyResume.devLocation);
        userApplyDescripion.setText(applyResume.reason);

        if (applyResume.roles.isEmpty()) {
            roleLayout.setVisibility(View.GONE);
        } else {
            roleLayout.setVisibility(View.VISIBLE);

            if (roleListLayout.getChildCount() == 0) {
                for (Role role : applyResume.roles) {
                    View roleLayout = getLayoutInflater().inflate(R.layout.user_skill_list_role, roleListLayout, false);

                    roleLayout.findViewById(R.id.modifyButton).setVisibility(View.GONE);
                    roleLayout.findViewById(R.id.goodAtLayout).setVisibility(View.GONE);

                    StringBuilder skillsBuffer = new StringBuilder();
                    boolean isFirst = true;
                    for (String item : role.roleSkills) {
                        if (isFirst) {
                            isFirst = false;
                        } else {
                            skillsBuffer.append(", ");
                        }
                        skillsBuffer.append(item);
                    }
                    String skillsString = skillsBuffer.toString();

                    setTextItem(roleLayout,
                            new int[]{
                                    R.id.title,
                                    skills,
                                    R.id.abilities},
                            new String[]{
                                    role.roleName,
                                    skillsString,
                                    role.specialSkill
                            });

                    roleListLayout.addView(roleLayout);
                }
            }
        }

        if (applyResume.projects.isEmpty()) {
            projectExpLayout.setVisibility(View.GONE);
        } else {
            projectExpLayout.setVisibility(View.VISIBLE);

            if (projectExpListLayout.getChildCount() == 0) {
                for (Project project : applyResume.projects) {
                    ViewGroup projectView = (ViewGroup) getLayoutInflater().inflate(R.layout.user_skill_list_project_exp, projectExpListLayout, false);

                    projectView.findViewById(R.id.modifyButton).setVisibility(View.GONE);
                    projectView.findViewById(R.id.projectTypeLayout).setVisibility(View.GONE);
                    projectView.findViewById(R.id.projectIndustryLayout).setVisibility(View.GONE);


                    int maxWidth = LengthUtil.getsWidthDp() - 15 * 2 - 140 - 20 * 2;
                    ((TextView) projectView.findViewById(R.id.title)).setMaxWidth(LengthUtil.dpToPx(maxWidth));

                    String timeString = String.format("%s - %s", timeFormat(project.startedAt), timeFormat(project.endedAt));
                    setTextItem(projectView,
                            new int[]{
                                    R.id.title,
                                    R.id.time,
                                    R.id.description,
                                    R.id.duty,
                                    R.id.link},
                            new String[]{
                                    project.projectName,
                                    timeString,
                                    project.description,
                                    project.duty,
                                    project.showUrl
                            });

                    View fileLayout = projectView.findViewById(R.id.fileLayout);
                    if (project.attaches.isEmpty()) {
                        fileLayout.setVisibility(View.GONE);
                    } else {
                        fileLayout.setVisibility(View.VISIBLE);
                        ViewGroup fileLayoutContent = (ViewGroup) projectView.findViewById(R.id.fileLayoutContent);
                        for (Attach item : project.attaches) {
                            View fileItem = getLayoutInflater().inflate(R.layout.project_exp_list_file, fileLayoutContent, false);
                            ((TextView) fileItem.findViewById(R.id.fileName)).setText(item.fileName);
                            fileLayoutContent.addView(fileItem);
                        }
                    }

                    projectExpListLayout.addView(projectView);
                }
            }
        }
    }

    private String timeFormat(String time) {
        int index = time.indexOf(" ");
        if (index != -1) {
            return time.substring(0, index);
        }

        return time;
    }

    private void setTextItem(View root, int[] textViews, String[] texts) {
        for (int i = 0; i < textViews.length; ++i) {
            ((TextView) root.findViewById(textViews[i])).setText(texts[i]);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(CallPhoneEvent event) {
        if (event.type == CallPhoneEvent.Type.QQ) {
            try {
                String url = "mqqwpa://im/chat?chat_type=wpa&uin=" + event.content;
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            } catch (Exception e) {
                Global.errorLog(e);
            }
        } else if (event.type == CallPhoneEvent.Type.Email) {
            AboutActivity.composeEmail(this, new String[]{event.content});
        } else if (event.type == CallPhoneEvent.Type.Phone) {
            AboutActivity.dialPhoneNumber(this, event.content);
        }
    }

    private void saveResultIntent() {
        if (resultIntent == null) {
            resultIntent = new Intent();
        }

        resultIntent.putExtra("coders", coders);
        uiBindData();
    }

    @Override
    public void finish() {
        if (resultIntent != null) {
            setResult(RESULT_OK, resultIntent);
        }

        super.finish();
    }

    @Click
    void contactClick() {
        Network.getRetrofit(this)
                .getApplyContactParam(rewardId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NewBaseObserver<ApplyContactResult>(this) {
                    @Override
                    public void onSuccess(ApplyContactResult data) {
                        super.onSuccess(data);
                        showSending(false);

                        String message = String.format("<font color='#999999'>您可以免费查看 %s 位报名者联系方式。 " +
                                "如果您需要查看更多开发者，需要支付 %s 元/人的服务费，费用会从您的开发宝中扣除。</font><br><br>" +
                                "您当前还可以免费查看 %s 人联系方式", data.freeTotal, data.fee, data.freeRemain);
                        String rightText = "确定";
                        View.OnClickListener clickRight = clickRequstFree;
                        if (data.freeRemain <= 0) {
                            rightText = "支付并查看";
                            clickRight = clickRequestContactPay;
                        }

                        new CommonDialog.Builder(CoderInfoActivity.this)
                                .setMessage(Html.fromHtml(message))
                                .setRightButton(rightText, clickRight)
                                .show();
                    }

                    @Override
                    public void onFail(int errorCode, @NonNull String error) {
                        super.onFail(errorCode, error);
                        showSending(false);
                    }
                });
        showSending(true);
    }

    private void requestUserContact() {
        Network.getRetrofit(this)
                .getApplyContact(coder.applyId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NewBaseObserver<ApplyContact>(this) {
                    @Override
                    public void onSuccess(ApplyContact data) {
                        super.onSuccess(data);
                        coder.updateContact(data);

                        saveResultIntent();
                    }

                });
    }

    @Click
    void userMobile() {
        if (!TextUtils.isEmpty(coder.mobile)) {
            EventBus.getDefault().post(new CallPhoneEvent(coder.mobile));
        }
    }

    @Click
    void userQQ() {
        if (!TextUtils.isEmpty(coder.qq)) {
            EventBus.getDefault().post(new CallPhoneEvent(coder.qq, CallPhoneEvent.Type.QQ));
        }
    }

    @Click
    void userMail() {
        if (!TextUtils.isEmpty(coder.email)) {
            EventBus.getDefault().post(new CallPhoneEvent(coder.email, CallPhoneEvent.Type.Email));
        }
    }

    @Click
    void buttonAccept() {
        popAcceptDialog(this, this, coder);
    }

    @Click
    void buttonRefuse() {
        popRefuseDialog(this, this, coder);
    }

    @Override
    public void applyAccept(Coder coder) {
        if (resultIntent == null) {
            resultIntent = new Intent();
        }
        resultIntent.putExtra(V2RewardDetailActivity.resultExtraRefrush, true);
        uiBindData();
    }

    @Override
    public void applyReject(Coder coder) {
        saveResultIntent();
        uiBindData();
    }

}
