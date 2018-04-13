package net.coding.mart.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by pansh on 2015/11/26 0026.
 */
public class ListenScrollView extends ScrollView {

    private ListenScrollListener listenScrollListener;

    public interface ListenScrollListener{
        void onScrollChanged(ListenScrollView scrollView, int x, int y, int oldx, int oldy);
    }


    public ListenScrollView setOnListenScrollListener(ListenScrollListener listenScrollListener) {
        this.listenScrollListener = listenScrollListener;
        return this;
    }

    public ListenScrollView(Context context) {
        super(context);
    }

    public ListenScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListenScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        if (listenScrollListener != null){
            listenScrollListener.onScrollChanged(this, l, t, oldl, oldt);
        }

    }
}
