package net.coding.mart.activity.guide;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;

import net.coding.mart.LengthUtil;
import net.coding.mart.R;
import net.coding.mart.activity.MainActivity_;
import net.coding.mart.common.BaseActivity;
import net.coding.mart.common.umeng.UmengEvent;
import net.coding.mart.login.LoginActivity_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.OnActivityResult;


@EActivity(R.layout.demo_single_fling_pager)
public class GuideActivity extends BaseActivity {

    private static final int RESULT_LOGIN = 1;
    final float SCAL = 0.85f;
    final float SCAL_EDGE = 1 - SCAL;
    protected RecyclerViewPager mRecyclerView;
    ViewGroup emojiKeyboardIndicator;
    @InstanceState
    int pagePointPos = 0;

    @AfterViews
    void initSingleFlngPapActivity() {
        emojiKeyboardIndicator = (ViewGroup) findViewById(R.id.emojiKeyboardIndicator);
        setIndicatorCount(LayoutAdapter.getPagerCount());

        mRecyclerView = (RecyclerViewPager) findViewById(R.id.viewpager);
        LinearLayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,
                false);
        mRecyclerView.setLayoutManager(layout);
        mRecyclerView.setAdapter(new LayoutAdapter(this));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLongClickable(true);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int scrollState) {
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int i, int i2) {
                int childCount = mRecyclerView.getChildCount();
                int width = mRecyclerView.getChildAt(0).getWidth();
                int padding = (mRecyclerView.getWidth() - width) / 2;

                for (int j = 0; j < childCount; j++) {
                    View v = recyclerView.getChildAt(j);
                    //往左 从 padding 到 -(v.getWidth()-padding) 的过程中，由大到小
                    float rate = 0;
                    if (v.getLeft() <= padding) {
                        if (v.getLeft() >= padding - v.getWidth()) {
                            rate = (padding - v.getLeft()) * 1f / v.getWidth();
                        } else {
                            rate = 1;
                        }
                        v.setScaleY(1 - rate * SCAL_EDGE);
                        v.setScaleX(1 - rate * SCAL_EDGE);

                    } else {
                        //往右 从 padding 到 recyclerView.getWidth()-padding 的过程中，由大到小
                        if (v.getLeft() <= recyclerView.getWidth() - padding) {
                            rate = (recyclerView.getWidth() - padding - v.getLeft()) * 1f / v.getWidth();
                        }
                        v.setScaleY(SCAL + rate * SCAL_EDGE);
                        v.setScaleX(SCAL + rate * SCAL_EDGE);
                    }
                }
            }
        });

        mRecyclerView.addOnPageChangedListener((oldPosition, newPosition) -> {
            pagePointPos = newPosition;
            updatePagePointsUI(oldPosition);
        });
        updatePagePointsUI(0);

        mRecyclerView.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            if (mRecyclerView.getChildCount() < 3) {
                if (mRecyclerView.getChildAt(1) != null) {
                    if (mRecyclerView.getCurrentPosition() == 0) {
                        View v1 = mRecyclerView.getChildAt(1);
                        v1.setScaleY(SCAL);
                        v1.setScaleX(SCAL);
                    } else {
                        View v1 = mRecyclerView.getChildAt(0);
                        v1.setScaleY(SCAL);
                        v1.setScaleX(SCAL);
                    }
                }
            } else {
                if (mRecyclerView.getChildAt(0) != null) {
                    View v0 = mRecyclerView.getChildAt(0);
                    v0.setScaleY(SCAL);
                    v0.setScaleX(SCAL);
                }
                if (mRecyclerView.getChildAt(2) != null) {
                    View v2 = mRecyclerView.getChildAt(2);
                    v2.setScaleY(SCAL);
                    v2.setScaleX(SCAL);
                }
            }

        });
    }

    @Click
    void login() {
        umengEvent(UmengEvent.ACTION, "欢迎页 _ 登录码市");
        LoginActivity_.intent(this).startForResult(RESULT_LOGIN);
    }

    @Click
    void entrance() {
        umengEvent(UmengEvent.ACTION, "欢迎页 _ 先用用看");
        MainActivity_.intent(this).start();
        finish();
        overridePendingTransition(0, 0);
    }

    @OnActivityResult(RESULT_LOGIN)
    void onResultLogin(int resultCode) {
        if (resultCode == RESULT_OK) {
            entrance();
        }
    }

    private void updatePagePointsUI(int oldPos) {
        View oldPoint = emojiKeyboardIndicator.getChildAt(oldPos);
        if (oldPoint != null) {
            oldPoint.setBackgroundResource(R.mipmap.guide_poin_dark);
        }

        View newPoint = emojiKeyboardIndicator.getChildAt(pagePointPos);
        if (newPoint != null) {
            newPoint.setBackgroundResource(R.mipmap.guide_poin_light);
        }
    }

    private void setIndicatorCount(int count) {
        if (count <= 1) {
            emojiKeyboardIndicator.setVisibility(View.INVISIBLE);
            return;
        } else {
            emojiKeyboardIndicator.setVisibility(View.VISIBLE);
        }

        emojiKeyboardIndicator.removeAllViews();
        int pointWidth = LengthUtil.dpToPx(8);
        int pointHeight = pointWidth;
        int pointMargin = LengthUtil.dpToPx(6);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(pointWidth, pointHeight);
        lp.leftMargin = pointWidth;
        lp.rightMargin = pointMargin;
        for (int i = 0; i < count; ++i) {
            View pointView = new View(this);
            pointView.setBackgroundResource(R.mipmap.guide_poin_dark);
            emojiKeyboardIndicator.addView(pointView, lp);
        }
        emojiKeyboardIndicator.getChildAt(0).setBackgroundResource(R.mipmap.guide_poin_light);
    }
}
