package net.coding.mart.common.space;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import net.coding.mart.R;
import net.coding.mart.common.widget.BaseRecyclerViewSpace;

/**
 * Created by chenchao on 16/3/4.
 */
public class RecyclerViewSpace extends BaseRecyclerViewSpace {

    public RecyclerViewSpace(Context context) {
        super(context);

        Resources resources = context.getResources();
        bottomSpace = resources.getDimensionPixelSize(R.dimen.list_header_space);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int pos = parent.getChildAdapterPosition(view);
        outRect.top = topSpace;
        outRect.bottom = lineSpace;

        if (pos == parent.getAdapter().getItemCount() - 1) {
            outRect.bottom = bottomSpace;
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; ++i) {
            View view = parent.getChildAt(i);
            int pos = parent.getChildAdapterPosition(view);
            Rect rectTop = new Rect(view.getLeft(), view.getTop() - topSpace, view.getRight(), view.getTop());
            c.drawRect(rectTop, paintDivide);
            shadowBottom.setBounds(rectTop);
            shadowBottom.draw(c);

            Rect rectLine = new Rect(view.getLeft(), view.getBottom(), view.getRight(), view.getBottom() + lineSpace);
            c.drawRect(rectLine, paintDivideLine);
            c.drawRect(rectLine, paintDivideLine);

            if (pos == parent.getAdapter().getItemCount() - 1) {
                Rect rect = new Rect(view.getLeft(), view.getBottom(), view.getRight(), view.getBottom() + bottomSpace);
                c.drawRect(rect, paintDivide);

                Rect rectShadow = new Rect(rect);
                rectShadow.bottom = view.getBottom() + bottomShadowSpace;
                shadowTop.setBounds(rectShadow);
                shadowTop.draw(c);
            }

        }

    }
}
