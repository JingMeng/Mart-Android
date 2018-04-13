package net.coding.mart.common.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import net.coding.mart.R;

/**
 * Created by chenchao on 16/4/11.
 */
public class LoadingView extends FrameLayout {

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.loading_view, this);

        ImageView loadingView = (ImageView) findViewById(R.id.loadingCircle);
        ObjectAnimator animator = ObjectAnimator.ofFloat(loadingView, "rotation", 360f, 0f);
        animator.setDuration(2000);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.setRepeatCount(2000);
        animator.start();

        ImageView loadingIcon = (ImageView) findViewById(R.id.loadingIcon);
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(loadingIcon, "alpha", 1f, 0f);
        animator1.setDuration(2000);
        animator1.setRepeatMode(ValueAnimator.REVERSE);
        animator1.setInterpolator(new DecelerateInterpolator());
        animator1.setRepeatCount(2000);
        animator1.start();
    }
}
