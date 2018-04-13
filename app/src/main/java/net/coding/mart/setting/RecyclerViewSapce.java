package net.coding.mart.setting;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by chenchao on 16/3/4.
 */
public class RecyclerViewSapce extends RecyclerView.ItemDecoration {

    private final int height;

    public RecyclerViewSapce(int height) {
        this.height = height;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//        outRect.bottom = height;
//        outRect.left = 50;
//        outRect.right = 100;
//        outRect.top = 150;
//        outRect.bottom = 200;

        int pos = parent.getChildAdapterPosition(view);
        if (pos == 0) {
            outRect.top = height;
        }

        outRect.bottom = height;
        outRect.left = height;
        outRect.right = height;
//        super.getItemOffsets(outRect, view, parent, state);
    }
}
