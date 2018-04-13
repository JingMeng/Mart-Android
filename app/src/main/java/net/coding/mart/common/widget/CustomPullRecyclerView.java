package net.coding.mart.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.marshalchen.ultimaterecyclerview.CustomUltimateRecyclerview;

import net.coding.mart.R;

import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by chenchao on 16/5/18.
 * 带有自定义的下拉刷新控件
 */
public class CustomPullRecyclerView extends CustomUltimateRecyclerview {

    private PullToRefrushLayout pullView;

    boolean needBottomMerge = false;

    public CustomPullRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CustomPullRecyclerView,
                0, 0);

        try {
            needBottomMerge = a.getBoolean(R.styleable.CustomPullRecyclerView_needBottomSapce, false);
        } finally {
            a.recycle();
        }

        setCustomSwipeToRefresh();
    }

    public void setCustomSwipeToRefresh() {
        super.setCustomSwipeToRefresh();

        pullView = new PullToRefrushLayout(getContext());
        if (needBottomMerge) {
            pullView.addTopSpace();
        }

        initPullView();
    }

    private void initPullView() {
        mPtrFrameLayout.setHeaderView(pullView);
        mPtrFrameLayout.setBackgroundResource(R.color.divide);
        mPtrFrameLayout.setRatioOfHeaderHeightToRefresh(0.5f);
//        mPtrFrameLayout.setOffsetToKeepHeaderWhileLoading(65 * 3);
        mPtrFrameLayout.addPtrUIHandler(pullView);
        mPtrFrameLayout.disableWhenHorizontalMove(true);

        findViewById(com.marshalchen.ultimaterecyclerview.R.id.ultimate_list).setBackgroundResource(R.color.divide);

        mPtrFrameLayout.post(() -> {
            int loadingHeight = pullView.getLoadingHeight();
            mPtrFrameLayout.setOffsetToKeepHeaderWhileLoading(loadingHeight);
        });
    }

    public void refreshComplete() {
        mPtrFrameLayout.refreshComplete();
    }

    public void setRefreshing(boolean refreshing) {
        if (!refreshing) {
            refreshComplete();
        }
    }

    public void setPtrHandler(PtrHandler ptrHandler) {
        mPtrFrameLayout.setPtrHandler(ptrHandler);
    }

}
