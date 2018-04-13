package net.coding.mart.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import net.coding.mart.R;

/**
 * Created by chenchao on 15/10/30.
 */
public class ListItem1 extends FrameLayout {

    ImageView mIcon;
    TextView mText;
    View redPoint;
    TextView mText2;

    public ListItem1(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        View.inflate(context, R.layout.list_item_1, this);
        mIcon = (ImageView) findViewById(R.id.icon);
        mText = (TextView) findViewById(R.id.text1);
        redPoint = findViewById(R.id.badge);
        mText2 = (TextView) findViewById(R.id.text2);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ListItem1);
        String title = array.getString(R.styleable.ListItem1_itemText);
        int icon = array.getResourceId(R.styleable.ListItem1_itemIcon, 0);
        boolean showBottomLine = array.getBoolean(R.styleable.ListItem1_itemBottomLine, true);
        array.recycle();

        if (title == null) title = "";
        mText.setText(title);

        if (icon == 0) {
            mIcon.setVisibility(GONE);
        } else {
            mIcon.setImageResource(icon);
        }

        if (!showBottomLine) {
            findViewById(R.id.bottomLine).setVisibility(GONE);
        }
    }

    public void showRedPoint(boolean show) {
        redPoint.setVisibility(show ? VISIBLE : INVISIBLE);
    }

    public void setText(String s) {
        mText.setText(s);
    }

    public void setText2(String s) {
        mText2.setText(s);
    }

    public TextView getText2() {
        return mText2;
    }

    public void setIcon(@DrawableRes int id) {
        mIcon.setImageResource(id);
    }

    public void setTextIcon(String s, @DrawableRes int id) {
        setText(s);
        setIcon(id);
    }

    public void setTextColor(int color) {
        mText.setTextColor(color);
    }

}
