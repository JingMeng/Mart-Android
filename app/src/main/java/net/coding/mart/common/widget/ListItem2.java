package net.coding.mart.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import net.coding.mart.R;

/**
 * Created by chenchao on 15/11/2.
 * 带有 checkbox 的 listitem
 */
public class ListItem2 extends FrameLayout {

    ImageView mIcon;
    TextView mText;
    ImageView mCheckBox;

    /**
     * 右边位置上的 textView 用于显示状态文字
     */
    TextView rightText;

    boolean mIsCheck = false;
    String itemRightText = null;
    int itemRightTextColor = 0xff000000;

    public ListItem2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        View.inflate(context, R.layout.list_item_2, this);
        mIcon = (ImageView) findViewById(R.id.icon);
        mText = (TextView) findViewById(R.id.text1);
        rightText = (TextView) findViewById(R.id.right_text);
        mCheckBox = (ImageView) findViewById(R.id.check1);


        // 取出 xml 参数
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ListItem2);
        String title = array.getString(R.styleable.ListItem2_itemText);
        int icon = array.getResourceId(R.styleable.ListItem2_itemIcon, R.mipmap.ic_list_about);
        boolean showBottomLine = array.getBoolean(R.styleable.ListItem2_itemBottomLine, true);
        mIsCheck = array.getBoolean(R.styleable.ListItem2_itemCheck, false);


        // 取出 itemRightText 参数
        itemRightText = array.getString(R.styleable.ListItem2_itemRightText);
        itemRightTextColor = array.getColor(R.styleable.ListItem2_itemRightTextColor, 0xff000000);
        updateRightText();

        if (array.getBoolean(R.styleable.ListItem2_itemNoPressState, false)) {
            findViewById(R.id.rootLayout).setBackgroundResource(R.color.stand_bg);
        }

        array.recycle();

        if (title == null) title = "";
        mText.setText(title);
        mIcon.setImageResource(icon);

        if (!showBottomLine) {
            findViewById(R.id.bottomLine).setVisibility(GONE);
        }

        updateCheck();
    }

    public void setCheck(boolean check) {
        rightText.setVisibility(GONE);
        mCheckBox.setVisibility(VISIBLE);

        if (mIsCheck == check) {
            return;
        }

        mIsCheck = check;
        updateCheck();
    }

    private void updateCheck() {
        mCheckBox.setImageResource(mIsCheck ? R.mipmap.ic_list_check_true : R.mipmap.ic_list_check_false);
    }

    /**
     * 设置 Right text 的参数值
     * @param text
     * @param colorId
     */
    public void setRightText(String text, int colorId){
        itemRightText = text;
        itemRightTextColor = getResources().getColor(colorId);
        updateRightText();
    }
    /**
     * 更新 right text
     */
    private void updateRightText(){
        if (itemRightText != null){
            // 一旦 rightText 显示出来，那么 checkbox 将隐藏起来
            mCheckBox.setVisibility(GONE);
            rightText.setTextColor(itemRightTextColor);
            rightText.setText(itemRightText);
            rightText.setVisibility(VISIBLE);
        }else {
            mCheckBox.setVisibility(VISIBLE);
            rightText.setVisibility(GONE);
        }

    }

}