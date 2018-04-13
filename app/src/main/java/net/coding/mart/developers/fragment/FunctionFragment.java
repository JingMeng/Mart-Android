package net.coding.mart.developers.fragment;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ListView;
import android.widget.RelativeLayout;

import net.coding.mart.LengthUtil;
import net.coding.mart.R;
import net.coding.mart.developers.FunctionListActivity;
import net.coding.mart.developers.adapter.CategoryAdapter;
import net.coding.mart.developers.adapter.FunctionAdapter;
import net.coding.mart.developers.adapter.ModuleAdapter;
import net.coding.mart.developers.view.PinnedSectionListView;
import net.coding.mart.json.developer.Quotation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liu on 16/5/30.
 */
public class FunctionFragment extends Fragment implements View.OnClickListener {

    private RelativeLayout categoryLayout;
    private ListView categoryListView;
    private View categoryMask;
    private CategoryAdapter categoryAdapter;
    private List<Quotation> categorysData = new ArrayList<>();

    private RelativeLayout moduleLayout;
    private PinnedSectionListView moduleListView;
    private View moduleMask;
    private ModuleAdapter moduleAdapter;
    private List<Quotation> modulesData = new ArrayList<>();

    private RelativeLayout functionLayout;
    private PinnedSectionListView functionListView;
    private View functionMask;
    private FunctionAdapter functionAdapter;
    private List<Quotation> functionsData = new ArrayList<>();

    private View view;
    private Quotation platformQuotaion;
    private FunctionListActivity activity;

    private boolean isBusy = false;
    private boolean isShowThird = false;
    //默认选择集合
    private List<Quotation> defaultData;

    private int SCREEN_WIDTH;
    private int CATEGORY_MIN;
    private int CATEGORY_MAX;
    private int MODULE_WIDTH;
    private int FUNCTION_WIDTH;

    private int functionMove = 0;

    private final int ANIMATION_TIME = 300;

    private static final String TAG = FunctionFragment.class.getName();

    private int fragmentPos;
    public FunctionFragment() {
//        super(activity);
    }

    public void setArg(FunctionListActivity activity, int pos) {
        this.activity = activity;
        fragmentPos = pos;
    }

    public void setArg(FunctionListActivity activity, Quotation vo) {
        this.activity = activity;
        this.platformQuotaion = vo;
        this.defaultData = activity.getPickedData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        CATEGORY_MAX = getResources().getDimensionPixelOffset(R.dimen.category_list_width);
        CATEGORY_MIN = getResources().getDimensionPixelOffset(R.dimen.category_list_width_fold);
        MODULE_WIDTH = getResources().getDimensionPixelOffset(R.dimen.module_list_width);
        FUNCTION_WIDTH = LengthUtil.getsWidthPix() - (CATEGORY_MAX - CATEGORY_MIN) - MODULE_WIDTH;

        this.platformQuotaion = this.activity.getPlatformFromPos(fragmentPos);
        this.defaultData = activity.getPickedData();

        view = inflater.inflate(R.layout.fragment_function, container, false);
        initView();
        initData();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        SCREEN_WIDTH = metrics.widthPixels;

        return view;
    }

    public boolean isShowThird() {
        return isShowThird;
    }

    // 在这里添加或删除默认值可能是有问题的
    public void initData() {
        FunctionListActivity.initDataFromActivity(activity, platformQuotaion,
                categorysData, modulesData, functionsData);

        categoryAdapter = new CategoryAdapter(activity, this);
        categoryListView.setAdapter(categoryAdapter);
        categoryAdapter.setData(categorysData);

        moduleAdapter = new ModuleAdapter(activity, this);
        moduleListView.setAdapter(moduleAdapter);
        moduleAdapter.setData(modulesData);

        functionAdapter = new FunctionAdapter(activity, this);
        functionAdapter.setDefaultData(defaultData);
        functionListView.setAdapter(functionAdapter);
        activity.updatePicked();

        functionAdapter.setData(functionsData);
        functionAdapter.setPickeModels(moduleAdapter.getPiceDatas());
    }

    private void initView() {
        categoryLayout = (RelativeLayout) view.findViewById(R.id.rl_main);
        categoryListView = (ListView) view.findViewById(R.id.listview_main);
        categoryMask = view.findViewById(R.id.view_main);
        moduleLayout = (RelativeLayout) view.findViewById(R.id.rl_child);
        moduleListView = (PinnedSectionListView) view.findViewById(R.id.listview_child);
        moduleMask = view.findViewById(R.id.view_child);
        functionLayout = (RelativeLayout) view.findViewById(R.id.rl_third_category);
        functionListView = (PinnedSectionListView) view.findViewById(R.id.list_view_third);
        functionMask = view.findViewById(R.id.view_third);
        categoryMask.setOnClickListener(this);

        ViewGroup.MarginLayoutParams functionLayoutParam = (ViewGroup.MarginLayoutParams) functionLayout.getLayoutParams();
        functionLayoutParam.width = FUNCTION_WIDTH;
        functionLayoutParam.rightMargin = -FUNCTION_WIDTH;
        functionLayout.setLayoutParams(functionLayoutParam);

        categoryLayout.setOnClickListener(v -> {
            if (isShowThird) {
                hideThird();
            }
        });
    }

    public void updateMark(int postion) {
        int i = categorysData.indexOf(modulesData.get(postion));
        if (i != -1) {
            categoryListView.setSelection(i);
        }
        categoryAdapter.setMark(modulesData.get(postion).code);
        categoryAdapter.notifyDataSetChanged();
    }

    public void updateCateMark(Quotation quotation) {
        if (moduleAdapter != null) {
            moduleAdapter.setMarkCode(quotation);
        }
    }

    public void mainSelect(Quotation vo) {
        int i = modulesData.indexOf(vo);
        if (i != -1) {
//            moduleListView.setSelection(i);
            moduleAdapter.setPickedCategory(vo);
            functionAdapter.setPickeModels(moduleAdapter.getPiceDatas());

            if (isShowThird) {
                hideThird();
            }
        }
    }

    public void categorySelect(Quotation vo) {
//        int i = functionsData.indexOf(vo);
//        if (i != -1) {
//
//            functionListView.setSelection(i);
//
//        }

        int pos = functionAdapter.getPosition(vo);
        functionListView.setSelection(pos);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_main:
                hideThird();
                if (moduleAdapter != null) {
                    moduleAdapter.setMarkCodeEmpty();
                }
                break;
        }
    }

    private void hideThird() {
        if (isBusy) {
            return;
        }

        isShowThird = false;
        categoryMask.setVisibility(View.GONE);
        moduleMask.setVisibility(View.GONE);
        ObjectAnimator animator = ObjectAnimator.ofFloat(functionLayout, "translationX", -FUNCTION_WIDTH, 0);
        animator.setDuration(ANIMATION_TIME);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                functionMove = (int) (float) animation.getAnimatedValue("translationX");
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isBusy = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isBusy = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        animator.start();

        ObjectAnimator animator1 = ObjectAnimator.ofFloat(moduleLayout, "translationX", -CATEGORY_MIN, 0);
        animator1.setDuration(ANIMATION_TIME);
        animator1.setInterpolator(new DecelerateInterpolator());
        animator1.addUpdateListener(animation -> {
            int translationX = (int) (float) animation.getAnimatedValue("translationX");
            int categoryWidth = CATEGORY_MAX + translationX;
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(categoryWidth, RelativeLayout.LayoutParams.MATCH_PARENT);
            categoryLayout.setLayoutParams(params);
            RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(SCREEN_WIDTH - categoryWidth + functionMove, RelativeLayout.LayoutParams.MATCH_PARENT);
            params1.setMargins(CATEGORY_MAX, 0, 0, 0);
            moduleLayout.setLayoutParams(params1);
        });
        animator1.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isBusy = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isBusy = false;
                categoryAdapter.setTwoLine(true);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator1.start();
        categoryAdapter.setCollapse(false);
        functionMove = 0;
        moduleAdapter.setTwoLine(false);
    }

//    private float categoryMove = 0;

    /**
     * 显示第三列表
     */
    public void showThird() {
        if (isBusy) {
            return;
        }
        isShowThird = true;
//        categoryMask.setVisibility(View.VISIBLE);
        moduleMask.setVisibility(View.VISIBLE);
        ObjectAnimator animator = ObjectAnimator.ofFloat(functionLayout, "translationX", 0, -FUNCTION_WIDTH);
        animator.setDuration(ANIMATION_TIME);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                functionMove = (int) (float) animation.getAnimatedValue("translationX");
            }
        });
        animator.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
                isBusy = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isBusy = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        animator.start();
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(moduleLayout, "translationX", 0, -CATEGORY_MIN);
        animator1.setDuration(500);
        animator1.setInterpolator(new DecelerateInterpolator());
        animator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float translationX = (float) animation.getAnimatedValue("translationX");
                int categoryWidth = CATEGORY_MAX - (int) Math.abs(translationX);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(categoryWidth, RelativeLayout.LayoutParams.MATCH_PARENT);
                categoryLayout.setLayoutParams(params);

                RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(SCREEN_WIDTH - categoryWidth + functionMove, RelativeLayout.LayoutParams.MATCH_PARENT);
                params1.setMargins(CATEGORY_MAX, 0, 0, 0);
                moduleLayout.setLayoutParams(params1);
            }
        });
        animator1.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isBusy = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isBusy = false;
                categoryAdapter.setCollapse(true);
                moduleAdapter.setTwoLine(true);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator1.start();

        categoryAdapter.setTwoLine(false);
        functionMove = 0;
    }


    public void clear() {
        List<Quotation> pickedFunctions = platformQuotaion.getExtra();
        for (Quotation item : pickedFunctions) {
            if (item != Quotation.getFrontPageFunction()) {
                defaultData.remove(item);
            }
        }
        platformQuotaion.resetPickedFunctions();

        functionAdapter.notifyDataSetChanged();
    }

    public void updateAdapter() {
        functionAdapter.notifyDataSetChanged();
    }

    public List<Quotation> getDefaultData() {
        return defaultData;
    }

    public void addPickedFunction(Quotation vo) {
        if (vo.isRadioItem()) {
            for (Quotation item : vo.getExtra()) {
                defaultData.remove(item);
            }
        }

        int index = defaultData.indexOf(platformQuotaion);
        if (index != -1) {
            int insertPos = index + 1;
            for (; insertPos < defaultData.size(); ++insertPos) {
                if (!defaultData.get(insertPos).isFunction()) {
                    break;
                }
            }
            defaultData.add(insertPos, vo);
            platformQuotaion.addExtra(vo);

            activity.refreshData();
        }
    }

    public void removePickedFunction(Quotation vo) {
        if (vo.isRadioItem()) {
            return;
        }

        defaultData.remove(vo);
        activity.refreshData();
    }
}
