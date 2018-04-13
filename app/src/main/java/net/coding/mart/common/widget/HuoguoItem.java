package net.coding.mart.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import net.coding.mart.R;

/**
 * Created by chenchao on 16/11/15.
 * 火锅单的 item
 */

public class HuoguoItem extends FrameLayout {

    TextView text;
    ImageView icon;

    int iconResource;
    int iconResourceChecked;

    public HuoguoItem(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.HuoguoItem);
        String title = array.getString(R.styleable.HuoguoItem_huoguoText);
        iconResource = array.getResourceId(R.styleable.HuoguoItem_huoguoIcon, 0);
        iconResourceChecked = array.getResourceId(R.styleable.HuoguoItem_huoguoIconChecked, 0);
        boolean direction = array.getBoolean(R.styleable.HuoguoItem_huoguoDirection, true);
        array.recycle();

        int layout = direction ? R.layout.fragment_huoguo_entrance_item_big : R.layout.fragment_huoguo_entrance_item_small;
        inflate(context, layout, this);

        text = (TextView) findViewById(R.id.text);
        icon = (ImageView) findViewById(R.id.icon);
        text.setText(title);

        check(false);
    }

    public void check(boolean check) {
        icon.setImageResource(check ? iconResourceChecked : iconResource);
        setBackgroundResource(check ? R.drawable.rect_blue_fill : R.drawable.rect_white);
        text.setTextColor(check ? 0xFFFFFFFF : 0xFF434A54);
    }
}
