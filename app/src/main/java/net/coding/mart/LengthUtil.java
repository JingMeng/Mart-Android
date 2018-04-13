package net.coding.mart;

import android.content.Context;

/**
 * Created by chenchao on 15/10/9.
 * 帮助计算高度
 */
public class LengthUtil {
     static float sScale;
     static int sWidthDp;
     static int sWidthPix;
     static int sHeightPix;

    public static float getsScale() {
        return sScale;
    }

    public static int getsWidthDp() {
        return sWidthDp;
    }

    public static int getsWidthPix() {
        return sWidthPix;
    }

    public static int getsHeightPix() {
        return sHeightPix;
    }

    public static int getDp(Context context, int res) {
        return pxToDp(context.getResources().getDimension(res));
    }

    public static int dpToPx(int dpValue) {
        return (int) (dpValue * sScale + 0.5f);
    }

    public static int dpToPx(double dpValue) {
        return (int) (dpValue * sScale + 0.5f);
    }

    public static int pxToDp(float pxValue) {
        return (int) (pxValue / sScale + 0.5f);
    }

}
