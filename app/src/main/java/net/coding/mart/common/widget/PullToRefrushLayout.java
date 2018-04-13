package net.coding.mart.common.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.coding.mart.LengthUtil;
import net.coding.mart.R;

import java.util.Random;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.indicator.PtrIndicator;

/**
 * Created by chenchao on 16/5/18.
 * 下拉刷新控件
 */
public class PullToRefrushLayout extends FrameLayout implements PtrUIHandler {

    private TextView refrushTitle;
    private TextView refrushContent;
    private View refrushCircle;
    private LinearLayout loadingCircleLayout;

    ObjectAnimator animator;

    final String[] REFRUSH_TITLE = new String[]{
            "海量认证开发者，精简 IT 建设成本",
            "云端开发工具，过程全透明，专属项目顾问",
            "专业监管，双向协议保障，纠纷仲裁"
    };

    public enum Status {
        reset,
        prepare,
        begin,
        complete
    }

    private Status status = Status.reset;

    public PullToRefrushLayout(Context context) {
        super(context);

        init(context);
    }

    public PullToRefrushLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    public int getLoadingHeight() {
        return getHeight() - findViewById(R.id.pullFixHeader).getHeight();
    }

    private void init(Context context) {
        inflate(context, R.layout.pull_to_refrush, this);

        refrushTitle = (TextView) findViewById(R.id.refrushTitle);
        refrushContent = (TextView) findViewById(R.id.refrushContent);
        refrushCircle = findViewById(R.id.refrushCircle);
        loadingCircleLayout = (LinearLayout) findViewById(R.id.loadingCircleLayout);

        animator = ObjectAnimator.ofFloat(refrushCircle, "rotation", 360f, 0f);
        animator.setDuration(1000);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(2000);}

    float lastMovePx = 0;

    private final int PULL_HEIGHT = LengthUtil.dpToPx(130); // 下拉大于这个值就可以更新了, 单位是 px;

    public void addCircleRotation(float movePx) {
        lastMovePx += movePx;
        if (status == Status.prepare) {
            if (lastMovePx <= PULL_HEIGHT) {
                float degree = 360 * lastMovePx / PULL_HEIGHT;
                refrushCircle.setRotation(degree);
            } else {
                refrushContent.setText("松开刷新");
            }
        }

        float alpha = (lastMovePx - PULL_HEIGHT / 2) / (PULL_HEIGHT / 2);
        refrushTitle.setAlpha(alpha);
    }

    public void addTopSpace() {
        int headerSpace = getResources().getDimensionPixelSize(R.dimen.list_header_space);
        MarginLayoutParams params = (MarginLayoutParams) loadingCircleLayout.getLayoutParams();
        params.bottomMargin +=  headerSpace;
        loadingCircleLayout.setLayoutParams(params);
    }

    public void setStatus(Status status) {
        this.status = status;
        switch (status) {
            case prepare:
                lastMovePx = 0;
                refrushContent.setText("下拉刷新");
                break;
            case begin:
                refrushContent.setText("正在刷新");
                animator.start();
                break;
            case complete:
//                refrushTitle.setText("");
                break;
            default: // reset
                refrushTitle.setText(REFRUSH_TITLE[new Random().nextInt(REFRUSH_TITLE.length)]);
                animator.cancel();
                break;
        }

        Log.d("eeeeee", "" + status.name());
    }

    @Override
    public void onUIReset(PtrFrameLayout frame) {
        setStatus(PullToRefrushLayout.Status.reset);
    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {
        setStatus(PullToRefrushLayout.Status.prepare);
    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {
        setStatus(PullToRefrushLayout.Status.begin);
    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {
        setStatus(PullToRefrushLayout.Status.complete);
    }

    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {
//        setStatus(PullToRefrushLayout.Status.prepare);
        addCircleRotation((int) ptrIndicator.getOffsetY());
    }
}
