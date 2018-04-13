package net.coding.mart.common;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;

import tourguide.tourguide.Overlay;
import tourguide.tourguide.ToolTip;
import tourguide.tourguide.TourGuide;

/**
 * Created by chenchao on 16/8/17.
 */
public class MaskGuide {

    private static final AlphaAnimation enterAnimation = new AlphaAnimation(1, 1);

    static public TourGuide createTourGuide(Activity activity) {
        return TourGuide.init(activity).with(TourGuide.Technique.Click);
    }

    static public ToolTip createToolTip(ViewGroup v) {
        return new ToolTip()
                .setEnterAnimation(enterAnimation)
                .setCustomView(v)
                .setShadow(false);
    }

    static public Overlay createOverlay(View.OnClickListener click) {
        return new Overlay().setBackgroundColor(0x99000000)
                .disableClick(true)
                .setStyle(Overlay.Style.Rectangle)
                .setOnClickListener(click);
    }

    private static final String MASK_GUIDE_TIP = "MASK_GUIDE_TIP";
    private static final String KEY_APP_LAST_VER = "KEY_APP_LAST_VER";

    public static boolean show(Context ctx, Type type) {
//        if (1 == 1) {
//            return true;
//        }

        SharedPreferences sp = ctx.getSharedPreferences(MASK_GUIDE_TIP, Context.MODE_PRIVATE);
        return !sp.getBoolean(type.name(), false);
    }

    public static void markUsed(Context ctx, Type type) {
        SharedPreferences.Editor edit = ctx.getSharedPreferences(MASK_GUIDE_TIP, Context.MODE_PRIVATE).edit();
        edit.putBoolean(type.name(), true);
        edit.apply();
    }

    public static int getLastVersion(Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(MASK_GUIDE_TIP, Context.MODE_PRIVATE);
        return sp.getInt(KEY_APP_LAST_VER, 0);
    }

    public static void setLastVersion(Context ctx, int version) {
        SharedPreferences.Editor edit = ctx.getSharedPreferences(MASK_GUIDE_TIP, Context.MODE_PRIVATE).edit();
        edit.putInt(KEY_APP_LAST_VER, version);
        edit.apply();
    }

    public static void init(Context context) {
        int versionCode = 0;
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionCode = pInfo.versionCode;
        } catch (Exception e) {
            Global.errorLog(e);
        }

        int lastVersion = getLastVersion(context);
        if (lastVersion == 0 && !MyData.getInstance().isLogin()) { // 全新安装后第一次打开
            Type[] types = new Type[]{
//                    Type.SettingFragmentMPay,
//                    Type.IdentityValide,
//                    Type.MainHuoguo300,
//                    Type.UserMainIdentity300

            };

            for (Type item : types) {
                markUsed(context, item);
            }

        } else if (lastVersion < versionCode) { // 升级安装后第一次打开
//            setLastVersion(context, versionCode);
            Type[] types = new Type[]{
//                    Type.HomeFragmentHead1,
//                    Type.JobShare,
//                    Type.RewardDetailMenuDynamic,
//                    Type.RewardDetailSubway,
//
//                    // 发项目
//                    Type.MainPublishBottom,
//
//                    // 开发者
//                    Type.MainFragmentCategory,
//                    Type.JobJoin,
            };

            for (Type item : types) {
                markUsed(context, item);
            }
        }
    }

    public enum Type {
//        // 公共
//        HomeFragmentHead1,
//        SettingFragmentMPay,
//
//        JobShare,
//        RewardDetailMenuDynamic,
//
//        RewardDetailSubway,
//
//        // 发项目
//        MainPublishBottom,
////        MainHuoguo300,
//
//        // 开发者
//        MainFragmentCategory,
//        JobJoin,
//        IdentityValide,
//        UserMainIdentity300
    }

}
