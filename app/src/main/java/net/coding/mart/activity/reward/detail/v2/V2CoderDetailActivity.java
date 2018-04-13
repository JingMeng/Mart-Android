package net.coding.mart.activity.reward.detail.v2;

import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.coding.mart.R;
import net.coding.mart.activity.mpay.FinalPayOrdersActivity_;
import net.coding.mart.activity.user.setting.CommonDialog;
import net.coding.mart.common.BackActivity;
import net.coding.mart.common.ImageLoadTool;
import net.coding.mart.common.constant.ApplyStatus;
import net.coding.mart.databinding.CoderDetailBinding;
import net.coding.mart.databinding.CoderDetailProjectBinding;
import net.coding.mart.databinding.CoderDetailSkillBinding;
import net.coding.mart.json.BaseHttpResult;
import net.coding.mart.json.Network;
import net.coding.mart.json.NewBaseObserver;
import net.coding.mart.json.mart2.user.MartUser;
import net.coding.mart.json.mpay.Order;
import net.coding.mart.json.reward.user.ApplyContact;
import net.coding.mart.json.reward.user.ApplyContactResult;
import net.coding.mart.json.reward.user.ApplyResume;
import net.coding.mart.json.reward.user.Attach;
import net.coding.mart.json.reward.user.Project;
import net.coding.mart.json.reward.user.Role;
import net.coding.mart.json.v2.V2Apply;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OnActivityResult;

import java.util.ArrayList;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by chenchao on 2017/10/20.
 */
@EActivity(R.layout.activity_v2_coder_detail)
public class V2CoderDetailActivity extends BackActivity {

    private static final int RESULT_PAY = 2;

    ApplyResume applyResume;

    CoderDetailBinding binding;

    @Extra
    V2Apply apply;

    @AfterViews
    void initV2CoderDetailActivity() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        binding = CoderDetailBinding.bind(findViewById(R.id.rootLayout));
        binding.emptyView.setVisibility(View.VISIBLE);
        binding.emptyView.setLoading();
        binding.rootScrollView.setVisibility(View.INVISIBLE);
        binding.bottomLayout.setVisibility(View.INVISIBLE);

        loadData();
    }

    private void loadData() {
        Network.getRetrofit(this)
                .getApplyResume(apply.rewardId, apply.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NewBaseObserver<ApplyResume>(this) {
                    @Override
                    public void onSuccess(ApplyResume data) {
                        super.onSuccess(data);

                        applyResume = data;

                        uiBindData();
                    }

                });
    }

    private void uiBindData() {
        binding.emptyView.setVisibility(View.GONE);
        binding.bottomLayout.setVisibility(View.VISIBLE);
        binding.rootScrollView.setVisibility(View.VISIBLE);

        binding.setData(apply);
        binding.stars.setRating(Float.valueOf(apply.user.evaluation));

        ImageLoadTool.loadImage(binding.userIcon, apply.user.avatar);

        if (apply.status != ApplyStatus.CHECKING) {
            hideBottomLayout();
        } else {
            if (apply.marked) {
                binding.buttonMarked.setText("取消候选人");
            }
        }

        if (!applyResume.roles.isEmpty()) {
            for (Role role : applyResume.roles) {
                CoderDetailSkillBinding skillBinding = CoderDetailSkillBinding.inflate(getLayoutInflater(),
                        binding.skillItemLayout, true);
                skillBinding.setData(role);

                for (String roleSkill : role.roleSkills) {
                    TextView round = (TextView) getLayoutInflater().inflate(R.layout.round_item_1, skillBinding.skillsLayout, false);
                    round.setText(roleSkill);
                    skillBinding.skillsLayout.addView(round);
                }

            }
        } else {
            binding.skillMainLayout.setVisibility(View.GONE);
        }

        if (!applyResume.projects.isEmpty()) {
            for (Project project : applyResume.projects) {
                CoderDetailProjectBinding projectBinding = CoderDetailProjectBinding.inflate(getLayoutInflater(),
                        binding.projectItemLayout, true);
                projectBinding.setData(project);

                for (Attach attach : project.attaches) {
                    TextView fileView = (TextView) getLayoutInflater().inflate(R.layout.file_text, projectBinding.filesLayout, false);
                    fileView.setText(attach.fileName);
                    projectBinding.filesLayout.addView(fileView);
                }
            }
        } else {
            binding.projectMainLayout.setVisibility(View.GONE);
        }

        binding.buttonMarked.setOnClickListener(v -> {
            Network.getRetrofit(V2CoderDetailActivity.this)
                    .markCoder(apply.id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new NewBaseObserver<BaseHttpResult>(V2CoderDetailActivity.this) {
                        @Override
                        public void onSuccess(BaseHttpResult data) {
                            super.onSuccess(data);
                            apply.marked = !apply.marked;

                            if (apply.marked) {
                                showButtomToast("设置候选人成功");
                                binding.buttonMarked.setText("取消候选人");
                            } else {
                                showButtomToast("取消候选人成功");
                                binding.buttonMarked.setText("设为候选人");
                            }

                            setResult(RESULT_OK);
                        }
                    });
        });
        binding.buttonReject.setOnClickListener(v -> applyReject());
        binding.buttonPick.setOnClickListener(v -> applyPass());

        binding.noContactLayout.setOnClickListener(v -> getContact());
    }

    private void hideBottomLayout() {
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) binding.rootScrollView.getLayoutParams();
        lp.bottomMargin = 0;
        binding.rootScrollView.setLayoutParams(lp);
        binding.bottomLayout.setVisibility(View.GONE);
    }

    private void getContact() {
        Network.getRetrofit(this)
                .getApplyContactParam(apply.rewardId)
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

                        new CommonDialog.Builder(V2CoderDetailActivity.this)
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

    View.OnClickListener clickRequstFree = v -> requestUserContact();

    View.OnClickListener clickRequestContactPay = v -> {
        Network.getRetrofit(this)
                .getApplyContactOrder(apply.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NewBaseObserver<Order>(this) {
                    @Override
                    public void onSuccess(Order data) {
                        super.onSuccess(data);
                        showSending(false);

                        ArrayList<String> ids = new ArrayList<>();
                        ids.add(data.orderId);
                        FinalPayOrdersActivity_.intent(V2CoderDetailActivity.this)
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

    void applyReject() {
        String title = "拒绝候选人";
        String message = "确定拒绝这位开发者？";

        new CommonDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setRightButton("确定", v -> applyRejectReal())
                .show();
    }

    void applyRejectReal() {
        Network.getRetrofit(this)
                .applyReject(apply.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NewBaseObserver<BaseHttpResult>(this) {
                    @Override
                    public void onSuccess(BaseHttpResult data) {
                        super.onSuccess(data);
                        showSending(false);
                        apply.status = ApplyStatus.REJECTED;
                        setResult(RESULT_OK);
                        hideBottomLayout();
                    }

                    @Override
                    public void onFail(int errorCode, @NonNull String error) {
                        super.onFail(errorCode, error);
                        showSending(false);
                    }
                });

        showSending(true);
    }

    void applyPass() {
        String title = "选择开发者";
        String message = "确定选择此开发者进行项目合作？\n\n" +
                "选定开发者后，需要与开发者沟通详细需求并等待开发者提交阶段划分。 确认阶段划分并支付第一阶段款项后，项目将正式启动。";

        new CommonDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setRightButton("确定", v -> realApplyPass())
                .show();
    }

    void realApplyPass() {
        Network.getRetrofit(this)
                .applyPass(apply.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NewBaseObserver<BaseHttpResult>(this) {
                    @Override
                    public void onSuccess(BaseHttpResult data) {
                        super.onSuccess(data);
                        showSending(false);
                        apply.status = ApplyStatus.PASSED;

                        setResult(RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onFail(int errorCode, @NonNull String error) {
                        super.onFail(errorCode, error);
                        showSending(false);
                    }
                });

        showSending(true);
    }

    @OnActivityResult(RESULT_PAY)
    void onResultPayApplyContact(int resultCode) {
        if (resultCode == RESULT_OK) {
            requestUserContact();
        }
    }

    private void requestUserContact() {
        Network.getRetrofit(this)
                .getApplyContact(apply.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NewBaseObserver<ApplyContact>(this) {
                    @Override
                    public void onSuccess(ApplyContact data) {
                        super.onSuccess(data);
                        MartUser user = apply.user;
                        user.phone = data.phone;
                        user.email = data.email;
                        user.qq = data.qq;

                        setResult(RESULT_OK);

                        binding.setData(apply);
                    }
                });
    }

}
