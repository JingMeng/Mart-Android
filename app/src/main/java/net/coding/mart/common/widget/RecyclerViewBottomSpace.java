package net.coding.mart.common.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import net.coding.mart.LengthUtil;
import net.coding.mart.R;

/**
 * Created by chenchao on 16/3/4.
 */
public class RecyclerViewBottomSpace extends BaseRecyclerViewSpace {

    int leftSpace = 0;
    Paint paintBg;

    public RecyclerViewBottomSpace(Context context, int leftSapce) {
        super(context);
        this.leftSpace = LengthUtil.dpToPx(leftSapce);
        paintBg = new Paint();
        paintBg.setColor(context.getResources().getColor(R.color.stand_bg));
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int pos = parent.getChildAdapterPosition(view);

        if (pos == parent.getAdapter().getItemCount() - 1) {
            outRect.bottom = bottomSpace;
        } else {
            outRect.bottom = lineSpace;
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; ++i) {
            View view = parent.getChildAt(i);
            int pos = parent.getChildAdapterPosition(view);

            if (pos == parent.getAdapter().getItemCount() - 1) {
                Rect rect = new Rect(view.getLeft(), view.getBottom() , view.getRight(), view.getBottom() + bottomSpace);
                c.drawRect(rect, paintDivide);

                Rect rectShadow = new Rect(rect);
                rectShadow.bottom = view.getBottom() + bottomShadowSpace;
                shadowTop.setBounds(rectShadow);
                shadowTop.draw(c);
            } else {
                Rect rectDivideLine = new Rect(view.getLeft(), view.getBottom(), view.getRight(), view.getBottom() + lineSpace);
                c.drawRect(rectDivideLine, paintBg);
                rectDivideLine.left += leftSpace;
                c.drawRect(rectDivideLine, paintDivideLine);
            }

        }

    }
}
