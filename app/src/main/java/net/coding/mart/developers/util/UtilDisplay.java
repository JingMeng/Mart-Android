package net.coding.mart.developers.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;

/**
 * Created by liu on 16/3/29.
 */
public class UtilDisplay {

    /**
     * 获取屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.heightPixels;
    }

    public static int px2dip(Context context, float values) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (values / scale + 0.5f);
    }

    public static int dip2px(Context context, float values) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (values * scale + 0.5);
    }

    public static int px2sp(Context context, float values) {
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (values / scale + 0.5f);
    }

    public static int sp2px(Context context, float values) {
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (values * scale + 0.5f);
    }

    /**
     * 获取状态栏高度
     *
     * @param activity
     * @return
     */
    public static int getBarHeight(Activity activity) {
        Rect rect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        return rect.top;
    }
}
