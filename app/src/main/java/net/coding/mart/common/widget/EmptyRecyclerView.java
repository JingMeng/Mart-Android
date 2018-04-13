package net.coding.mart.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import net.coding.mart.R;

import java.util.List;

/**
 * Created by chenchao on 16/3/14.
 */
public class EmptyRecyclerView extends FrameLayout {

    View loadFailLayout;
    View loadingLayout;
    View emptyLayout;

    public EmptyRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        View.inflate(context, R.layout.empty_recycler_view, this);

        loadFailLayout = findViewById(R.id.loadFail);
        loadingLayout = findViewById(R.id.loading);
        emptyLayout = findViewById(R.id.listEmpty);
    }

    public void initFail(String text, OnClickListener click) {
        ((TextView) loadFailLayout.findViewById(R.id.message)).setText(text);
        loadFailLayout.findViewById(R.id.btnRetry).setOnClickListener(click);
    }

    public void initSuccessEmpty(String message, int iconRes) {
        ((TextView) emptyLayout.findViewById(R.id.emptyTitle)).setText(message);
        if (iconRes != 0) {
            ((ImageView) emptyLayout.findViewById(R.id.iconEmpty)).setImageResource(iconRes);
        }
    }

    public void initSuccessEmpty(String buttonText, OnClickListener buttonClick) {
        View retryButton = emptyLayout.findViewById(R.id.emptyButton);
        if (buttonClick != null && !buttonText.isEmpty()) {
            retryButton.setVisibility(VISIBLE);
            retryButton.setOnClickListener(buttonClick);
        } else {
            retryButton.setVisibility(INVISIBLE);
        }
    }

    public void setLoading() {
        setVisibility(VISIBLE);
        loadingLayout.setVisibility(VISIBLE);
        loadFailLayout.setVisibility(INVISIBLE);
        emptyLayout.setVisibility(INVISIBLE);
    }

    public void hide() {
        setVisibility(GONE);
    }

    public void setLoadingSuccess(List list) {
        setLoadingSuccess(list, "");
    }

    public void setLoadingSuccess(List list, String message) {
        if (list == null || list.isEmpty()) {
            setVisibility(VISIBLE);

            loadingLayout.setVisibility(INVISIBLE);
            loadFailLayout.setVisibility(INVISIBLE);
            emptyLayout.setVisibility(VISIBLE);

            if (message != null && !message.isEmpty()) {
                ((TextView) emptyLayout.findViewById(R.id.emptyTitle)).setText(message);
            }

        } else {
            setVisibility(INVISIBLE);
        }
    }

    public void setLoadingFail() {
        setVisibility(VISIBLE);
        loadingLayout.setVisibility(INVISIBLE);
        loadFailLayout.setVisibility(VISIBLE);
        emptyLayout.setVisibility(INVISIBLE);
    }

    public void setLoadingFail(List list) {
        if (list == null || list.isEmpty()) {
            setLoadingFail();
        } else {
            setVisibility(View.INVISIBLE);
        }
    }

}
