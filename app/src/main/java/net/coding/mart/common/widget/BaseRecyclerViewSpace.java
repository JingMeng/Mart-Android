package net.coding.mart.common.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;

import net.coding.mart.R;

/**
 * Created by chenchao on 16/4/20.
 */
public class BaseRecyclerViewSpace extends RecyclerView.ItemDecoration {

    public final int DIVIDE_COLOR;
    public final int DIVIDE_LINE_COLOR;

    protected final int topSpace;
    protected final int lineSpace;
    protected int bottomSpace;
    protected final int bottomShadowSpace;

    protected Paint paintDivideLine;
    protected Drawable shadowTop;
    protected Drawable shadowBottom;
    protected Paint paintDivide;

    public BaseRecyclerViewSpace(Context context) {
        Resources resources = context.getResources();
        topSpace = resources.getDimensionPixelSize(R.dimen.list_header_space);
        lineSpace = resources.getDimensionPixelSize(R.dimen.list_divide_line_height);
        bottomSpace = resources.getDimensionPixelSize(R.dimen.list_footer_space);
        bottomShadowSpace = resources.getDimensionPixelSize(R.dimen.list_footer_shadow_space);

        DIVIDE_COLOR = resources.getColor(R.color.divide);
        DIVIDE_LINE_COLOR = resources.getColor(R.color.divide_line);

        paintDivideLine = new Paint();
        paintDivideLine.setColor(DIVIDE_LINE_COLOR);

        paintDivide = new Paint();
        paintDivide.setColor(DIVIDE_COLOR);

        shadowTop = resources.getDrawable(R.mipmap.shadow_bottom1);
        shadowBottom = resources.getDrawable(R.mipmap.shadow_bottom);
    }
}
