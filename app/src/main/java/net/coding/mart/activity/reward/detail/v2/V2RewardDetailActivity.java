package net.coding.mart.activity.reward.detail.v2;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyco.roundview.RoundTextView;

import net.coding.mart.R;
import net.coding.mart.activity.reward.detail.RewardActivitiesActivity_;
import net.coding.mart.activity.reward.detail.coder.CoderInfoActivity_;
import net.coding.mart.activity.reward.detail.coder.CoderListActivity_;
import net.coding.mart.activity.reward.detail.v2.phase.PhaseDetailActivity_;
import net.coding.mart.common.BackActivity;
import net.coding.mart.common.Color;
import net.coding.mart.common.GlobalData_;
import net.coding.mart.common.ImageLoadTool;
import net.coding.mart.common.MyData;
import net.coding.mart.common.constant.ApplyStatus;
import net.coding.mart.common.constant.DeveloperType;
import net.coding.mart.common.constant.ProjectStatus;
import net.coding.mart.common.umeng.UmengEvent;
import net.coding.mart.common.util.DialogFactory;
import net.coding.mart.common.widget.EmptyRecyclerView;
import net.coding.mart.databinding.FragmentRewardDetailBasicInfoBinding;
import net.coding.mart.databinding.RewardDetailCancelBinding;
import net.coding.mart.databinding.RewardDetailCoderBinding;
import net.coding.mart.databinding.RewardDetailPhaseItemBinding;
import net.coding.mart.json.BaseHttpResult;
import net.coding.mart.json.File;
import net.coding.mart.json.Network;
import net.coding.mart.json.NewBaseObserver;
import net.coding.mart.json.mart2.user.MartUser;
import net.coding.mart.json.reward.Coder;
import net.coding.mart.json.reward.JoinJob;
import net.coding.mart.json.reward.Published;
import net.coding.mart.json.reward.project.ProjectPublish;
import net.coding.mart.json.v2.V2Apply;
import net.coding.mart.json.v2.V2ApplyPager;
import net.coding.mart.json.v2.V2Owners;
import net.coding.mart.json.v2.phase.Phase;
import net.coding.mart.json.v2.phase.Phases;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@EActivity(R.layout.activity_v2_reward_detail)
public class V2RewardDetailActivity extends BackActivity {

    public static final String resultExtraRefrush = "resultExtraRefrush";
    static final String NO_PICK = "待定";
    private static final int RESULT_REPUBLISH = 1;
    private static final int RESULT_PAY_STAGE = 3;
    private static final int RESULT_CODER = 4;
    @Extra
    int id;
    @Extra
    Published publishJob;
    @Extra
    JoinJob joinJob;
    @ViewById
    ViewGroup rootLayout;
    @ViewById
    EmptyRecyclerView emptyView;
    @Pref
    GlobalData_ globalData;

    ViewGroup codersLayout;
    int maxMulOrderPay = 10;
    View.OnClickListener clickCancel = v -> {
        umengEvent(UmengEvent.ACTION, "项目详情 _ 取消发布");
        String resonString = "";
        Object tag = v.getTag();
        if (tag instanceof String) {
            resonString = (String) tag;
        }
//        V2KRewardDetailActivity_


        Network.getRetrofit(this)
                .cancelReward(publishJob.getId(), resonString)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NewBaseObserver<BaseHttpResult>(this) {
                    @Override
                    public void onSuccess(BaseHttpResult data) {
                        super.onSuccess(data);
                        showSending(false, "");
                        publishJob.setStatusCannel();
                        setResult(RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onFail(int errorCode, @NonNull String error) {
                        super.onFail(errorCode, error);
                        showSending(false, "");
                    }
                });

        showSending(true, "取消中...");
    };
    View.OnClickListener clickStagePayTip = v -> {
        DialogFactory.create(V2RewardDetailActivity.this,
                R.string.stage_pay_title,
                R.string.stage_pay_message);
    };
    boolean firstCoderItemIsCreate = false;

    private ProjectPublish rewardDetail;
    private List<V2Apply> applys = new ArrayList<>();
    private V2Apply pickApply = new V2Apply();
    private MartUser owner = new MartUser();
    private List<Phase> phases = new ArrayList<>();

    //    private Published datum;
    private View.OnClickListener clickCoder = v -> {
        Object tag = v.getTag();
        if (tag instanceof Coder) {
            ArrayList<Coder> coderGroup = (ArrayList<Coder>) v.getTag(R.id.codersLayout);
            Coder coder = (Coder) tag;
            CoderInfoActivity_.intent(this)
                    .coder(coder)
                    .coders(coderGroup)
                    .rewardId(rewardDetail.id)
                    .startForResult(RESULT_CODER);
        } else if (tag instanceof List) {
            ArrayList<Coder> coders = (ArrayList<Coder>) tag;
            if (coders.isEmpty()) {
                showMiddleToast("还没有开发者报名");
            } else {
                CoderListActivity_.intent(this)
                        .coders(coders)
                        .rewardId(rewardDetail.id)
                        .startForResult(RESULT_CODER);
            }
        }
    };

    @AfterViews
    void initRewardDetailActivity() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        emptyView.setLoading();
        emptyView.initFail("获取详情失败", v -> rewardDetail());

        getMaxMulOrderPay();
    }

    private void getMaxMulOrderPay() {
        maxMulOrderPay = globalData.maxMulPayCount().get();
        rewardDetail();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.v2_reward_detail, menu);
//        menuEdit = menu.findItem(R.id.action_reward_edit);
//        menuCancel = menu.findItem(R.id.action_reward_cancel);

        return super.onCreateOptionsMenu(menu);
    }

    @OptionsItem
    void action_reward_dynamic() {
        umengEvent(UmengEvent.ACTION, "项目详情 _ 动态");
        RewardActivitiesActivity_.intent(this).id(id).start();
    }

    private void displayRewardDetail() {
        rootLayout.removeAllViews();

        emptyView.hide();
        addBasicInfo();

        switch (rewardDetail.status) {
            case CANCELED:
                addRewardDetail();
//                addDocument();
                break;
            case RECRUITING:
                addCoders();
                break;

            case DEVELOPING:
            case FINISHED:
                addUser();
                addPhase();
//                addDocument();
                break;

            default: // doing finish warranty
                break;
        }


    }


//    private boolean isDeveloper() {
//        return joinJob != null;
//    }

    private void addBasicInfo() {
        FragmentRewardDetailBasicInfoBinding binding = FragmentRewardDetailBasicInfoBinding.inflate(getLayoutInflater(),
                rootLayout, true);
        binding.setInfo(rewardDetail);

        List<RoundTextView> points = new ArrayList<>();
        List<View> lines = new ArrayList<>();
        List<TextView> texts = new ArrayList<>();

        points.add(binding.point0);
        texts.add(binding.text0);
        binding.subwayLayout.setVisibility(View.VISIBLE);

        switch (rewardDetail.status) {
            case RECRUITING:
                points.add(binding.point1);
                lines.add(binding.line1);
                texts.add(binding.text1);
                break;
            case DEVELOPING:
                points.add(binding.point1);
                points.add(binding.point2);
                lines.add(binding.line1);
                lines.add(binding.line2);
                texts.add(binding.text1);
                texts.add(binding.text2);
                break;
            case FINISHED:
                points.add(binding.point1);
                points.add(binding.point2);
                points.add(binding.point3);
                lines.add(binding.line1);
                lines.add(binding.line2);
                lines.add(binding.line3);
                texts.add(binding.text1);
                texts.add(binding.text2);
                texts.add(binding.text3);
                break;
            default:
                // do nothing
                binding.subwayLayout.setVisibility(View.GONE);
                return;
        }

        setSubway(binding, points, lines, texts);
    }

    private void setSubway(FragmentRewardDetailBasicInfoBinding binding,
                           List<RoundTextView> points,
                           List<View> lines,
                           List<TextView> texts) {
        RoundTextView allPoints[] = new RoundTextView[]{
                binding.point0,
                binding.point1,
                binding.point2,
                binding.point3,
        };

        for (RoundTextView item : allPoints) {
            item.getDelegate().setBackgroundColor(points.contains(item) ? Color.font_blue : 0xFFDEE4EB);
        }

        View allLines[] = new View[]{
                binding.line1,
                binding.line2,
                binding.line3
        };
        for (View item : allLines) {
            item.setBackgroundColor(lines.contains(item) ? Color.font_blue : 0xFFDEE4EB);
        }

        TextView allTexts[] = new TextView[]{
                binding.text0,
                binding.text1,
                binding.text2,
                binding.text3,
        };
        for (TextView item : allTexts) {
            item.setTextColor(texts.contains(item) ? 0xFF3C4858 : 0xFFDDE3EB);
        }
    }


    private void uiBindCoders() {
        if (codersLayout == null) {
            return;
        }

        codersLayout.removeAllViews();
        fillingCoderLayout();
    }

    // 开发中，添加用户
    private void addUser() {
        View v = getLayoutInflater().inflate(R.layout.reward_detail_user, rootLayout, false);
        rootLayout.addView(v);

        String type;
        MartUser user;
        if (MyData.getInstance().getData().isDemand()) {
            user = pickApply.user;
            type = "开发者";
        } else {
            user = owner;
            type = "需求方";
        }

        ImageLoadTool.loadImage((ImageView) v.findViewById(R.id.userIcon), user.avatar);
        ((TextView) v.findViewById(R.id.name)).setText(user.name);
        ((TextView) v.findViewById(R.id.type)).setText(type);
        v.findViewById(R.id.itemRootLayout).setOnClickListener(v1 -> {
            if (MyData.getInstance().getData().isDemand()) {
                V2CoderDetailActivity_.intent(V2RewardDetailActivity.this)
                        .apply(pickApply)
                        .start();
            } else {
                V2DemandDetailActivity_.intent(V2RewardDetailActivity.this)
                        .owner(owner)
                        .start();
            }
        });
    }

    private void addPhase() {
        View v = getLayoutInflater().inflate(R.layout.reward_detail_phase, rootLayout, false);
        rootLayout.addView(v);

        if (phases.isEmpty()) {
            TextView tip = (TextView) v.findViewById(R.id.phaseEmpty);
            View actionToWeb = v.findViewById(R.id.actionToWeb);
            if (MyData.getInstance().getData().isDemand()) {
                tip.setText(R.string.tip_demand_empty_phase);
                actionToWeb.setVisibility(View.GONE);
            } else {
                tip.setText(R.string.tip_dev_empty_phase);
                actionToWeb.setVisibility(View.VISIBLE);
            }
        } else {
            ViewGroup rootLayout = (ViewGroup) v.findViewById(R.id.phaseLayout);
            rootLayout.removeAllViews();

            for (Phase phase : phases) {
                RewardDetailPhaseItemBinding binding = RewardDetailPhaseItemBinding.inflate(getLayoutInflater(), rootLayout, true);
                binding.setData(phase);
                binding.rootLayout.setOnClickListener(v1 -> {
                    PhaseDetailActivity_.intent(V2RewardDetailActivity.this)
                            .phase(phase)
                            .start();
                });
            }
        }

    }

    private void addCoders() {
        View coderLayoutRoot = getLayoutInflater().inflate(R.layout.reward_detail_coders, rootLayout, false);
        rootLayout.addView(coderLayoutRoot);
        codersLayout = (ViewGroup) coderLayoutRoot.findViewById(R.id.codersLayout);

        fillingCoderLayout();
    }

    private void fillingCoderLayout() {
        codersLayout.removeAllViews();
        if (applys.isEmpty()) {
            getLayoutInflater().inflate(R.layout.coder_empty, codersLayout, true);
            return;
        }

        for (V2Apply item : applys) {
            RewardDetailCoderBinding binding = RewardDetailCoderBinding.inflate(getLayoutInflater(), codersLayout, true);
            binding.setData(item);
            binding.stars.setRating(Float.valueOf(item.user.evaluation));
            if (item.user.developerType == DeveloperType.TEAM) {
                addFlagIcon(item, binding, item.user.vipType.icon);
            } else {
                if (item.user.identityPassed) {
                    addFlagIcon(item, binding, R.mipmap.identity_id_card_small);
                }
                if (item.user.excellent) {
                    addFlagIcon(item, binding, R.mipmap.identity_id_card_excellent_small);
                }
                if (item.user.deposit) {
                    addFlagIcon(item, binding, R.mipmap.flag_cash_deposit);
                }
            }

            binding.coderRootLayout.setOnClickListener(v -> {
                V2CoderDetailActivity_.intent(V2RewardDetailActivity.this)
                        .apply(item)
                        .startForResult(RESULT_CODER);
            });
        }
    }

    private void addFlagIcon(V2Apply item, RewardDetailCoderBinding binding, int icon) {
        ImageView v = (ImageView) getLayoutInflater().inflate(R.layout.image, binding.flagLayout, false);
        v.setImageResource(icon);
        binding.flagLayout.addView(v);
    }

//    View.OnClickListener clickFile = v -> {
//        Object tag = v.getTag();
//
//        if (tag instanceof File) {
//            File file = (File) tag;
//            WebActivity_.intent(this).url(file.url).start();
//
//            Intent intent = new Intent(Intent.ACTION_VIEW);
//            intent.setData(Uri.parse(file.url));
//            startActivity(intent);
//        }
//    };

    private void addDocument() {
        List<File> fils = rewardDetail.files;
        if (fils.isEmpty()) {
            return;
        }

        getLayoutInflater().inflate(R.layout.reward_detail_files, rootLayout, true);
        ViewGroup filesLayout = (ViewGroup) rootLayout.findViewById(R.id.filesLayout);

        for (File file : fils) {
            if (file != null) {
                View v = getLayoutInflater().inflate(R.layout.reward_detail_file, filesLayout, false);
                ((TextView) v.findViewById(R.id.fileName)).setText(file.filename);
                filesLayout.addView(v);

//                v.setTag(file);
//                v.setOnClickListener(clickFile);
            }
        }
    }

    private void rewardDetail() {
//        Network.getRetrofit(this)
//                .getPublishRewardDetail(id)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new BaseObserver<Published>(this) {
//                    @Override
//                    public void onSuccess(Published data) {
//                        super.onSuccess(data);
//                        datum = data;
//
//                        rewardBasic();
////                        addRewardNoPass(data);
//                    }
//
//                    @Override
//                    public void onFail(int errorCode, @NonNull String error) {
//                        super.onFail(errorCode, error);
//                        emptyView.setLoadingFail(null);
//                    }
//                });
        emptyView.setLoading();

        Observable<ProjectPublish> projectRequest = Network.getRetrofit(this)
                .getProjectPublishDetail(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        projectRequest.subscribe(new NewBaseObserver<ProjectPublish>(this) {
            @Override
            public void onSuccess(ProjectPublish data) {
                super.onSuccess(data);
                rewardDetail = data;
                if (rewardDetail != null) {
                    if (rewardDetail.status == ProjectStatus.DEVELOPING
                            || rewardDetail.status == ProjectStatus.FINISHED) {
                        nextRequest1();
                    } else {
                        nextRequest();
                    }
                } else {
                    emptyView.setLoadingFail();
                }
            }

            @Override
            public void onFail(int errorCode, @NonNull String error) {
                super.onFail(errorCode, error);

                emptyView.setLoadingFail();
            }
        });
    }

    private void nextRequest() {
        Observable<V2ApplyPager> applysRequest = Network.getRetrofit(this)
                .getAllApplys(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        Observable<V2Owners> ownerRequest = Network.getRetrofit(this)
                .getProjectOwner(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        Observable.zip(applysRequest, ownerRequest, (ar, or) -> {
            applys = ar.applys;
            for (V2Apply apply : applys) {
                if (apply.status == ApplyStatus.PASSED) {
                    pickApply = apply;
                    break;
                }
            }
            if (!or.owners.isEmpty()) {
                owner = or.owners.get(0);
            } else {
                owner = new MartUser();
            }

            return true;
        }).subscribe(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {
                if (rewardDetail != null) {
                    displayRewardDetail();
                } else {
                    emptyView.setLoadingFail();
                }
            }

            @Override
            public void onError(Throwable e) {
                emptyView.setLoadingFail();
            }

            @Override
            public void onNext(Boolean aBoolean) {

            }
        });
    }


    private void nextRequest1() {
        Observable<V2ApplyPager> applysRequest = Network.getRetrofit(this)
                .getAllApplys(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        Observable<V2Owners> ownerRequest = Network.getRetrofit(this)
                .getProjectOwner(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        Observable<Phases> phasesRequest = Network.getRetrofit(this)
                .getProjectPhase(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        Observable[] t = new Observable[]{
                applysRequest, ownerRequest, phasesRequest
        };

        Observable.zip(applysRequest, ownerRequest, phasesRequest, (ar, or, pr1) -> {
            applys = ar.applys;
            for (V2Apply apply : applys) {
                if (apply.status == ApplyStatus.PASSED) {
                    pickApply = apply;
                    break;
                }
            }
            if (!or.owners.isEmpty()) {
                owner = or.owners.get(0);
            } else {
                owner = new MartUser();
            }

            phases.clear();
            if (pr1.phases != null) {
                for (Phase item : pr1.phases) {
                    if (item.status != Phase.Status.DELETED) {
                        phases.add(item);
                    }
                }
            }

            return true;
        }).subscribe(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {
                if (rewardDetail != null) {
                    displayRewardDetail();
                } else {
                    emptyView.setLoadingFail();
                }
            }

            @Override
            public void onError(Throwable e) {
                emptyView.setLoadingFail();
            }

            @Override
            public void onNext(Boolean aBoolean) {

            }
        });
    }

    private void addRewardDetail() {
        RewardDetailCancelBinding despBind = RewardDetailCancelBinding.inflate(getLayoutInflater(), rootLayout, true);
        despBind.setData(rewardDetail);
    }

    @OnActivityResult(RESULT_REPUBLISH)
    void onResultRepublish(int resultCode) {
        if (resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }

    @OnActivityResult(RESULT_CODER)
    void onResultCoder(int resultCode) {
        if (resultCode == RESULT_OK) {
            reloadAll();
        }
    }

    @OnActivityResult(RESULT_PAY_STAGE)
    void onResultPayStage(int resultCode) {
        if (resultCode == Activity.RESULT_OK) {
            reloadAll();
        }
    }

    private void reloadAll() {
        rewardDetail();
    }

}

