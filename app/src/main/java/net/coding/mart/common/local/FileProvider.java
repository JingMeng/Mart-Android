package net.coding.mart.common.local;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import net.coding.mart.common.util.DialogFactory;
import net.coding.mart.common.widget.SingleToast;

import java.io.File;
import java.util.List;

/**
 * Created by chenchao on 16/11/14.
 * 用于向第三方共享文件
 */

public class FileProvider {

    public static final String AUTHOR = "net.coding.mart.fileprovider";

    public static Uri makeUri(Context context, File shareFile) {
        return android.support.v4.content.FileProvider.getUriForFile(context, AUTHOR, shareFile);
    }

    public static void openFile(Context context, File shareFile) {
        try {
            if (!shareFile.exists()) {
                return;
            }

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri uri = makeUri(context, shareFile);
            intent.setData(uri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            if (hasAppsStartIntent(context, intent)) {
                context.startActivity(intent);
            } else {
                DialogFactory.create(context, "打不开文件", "请到应用市场下载 WPS 来打开文件");
            }

        } catch (Exception e) {
            e.printStackTrace();
            SingleToast.showMiddleToast(context, String.format("打开 %s 失败", shareFile.getName()));
        }
    }

    /**
     * 获取能启动intent的app信息
     *
     * @param context
     * @param intent
     * @return
     */
    public static List<ResolveInfo> getAppsForIntent(Context context,
                                                     Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        // 属性
        List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(
                intent, PackageManager.MATCH_DEFAULT_ONLY);

//        for (ResolveInfo info : resolveInfo) {
//            Log.d("resolve info:" + info.activityInfo.packageName);
//        }

        return resolveInfo;
    }

    /**
     * 是否有启动intent的app
     *
     * @param context
     * @param intent
     * @return
     */
    public static boolean hasAppsStartIntent(Context context, Intent intent) {
        List<ResolveInfo> appInfos = getAppsForIntent(context, intent);
        return appInfos != null && appInfos.size() > 0;
    }

    public enum Type {
        Cache
    }
}
