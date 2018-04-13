package net.coding.mart;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.multidex.MultiDexApplication;

import com.liulishuo.filedownloader.FileDownloader;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import net.coding.mart.common.Global;
import net.coding.mart.common.MaskGuide;
import net.coding.mart.common.MyData;
import net.coding.mart.common.MyImageDownloader;
import net.coding.mart.common.RedPointTip;
import net.coding.mart.json.Network;
import net.coding.mart.login.LoginActivity;

/**
 * Created by chenchao on 15/10/9.
 * 自定义application，做一些自定义的事
 */
public class MartApplication extends MultiDexApplication {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();

        SharedPreferences sharedPref = getSharedPreferences(
                "login_activity_mart_custom_url", Context.MODE_PRIVATE);
        String martHost = sharedPref.getString("martHost", "");
        if (martHost.isEmpty()) {
            martHost = Global.HOST;
        } else if (martHost.equals("t")) {
            martHost = Global.TEST_HOST;
        } else if (martHost.equals("s")) {
            martHost = Global.STAGING_HOST;
        }

        Global.setMartHost(martHost);

        LengthUtil.sScale = getResources().getDisplayMetrics().density;
        LengthUtil.sWidthPix = getResources().getDisplayMetrics().widthPixels;
        LengthUtil.sHeightPix = getResources().getDisplayMetrics().heightPixels;
        LengthUtil.sWidthDp = (int) (LengthUtil.sWidthPix / LengthUtil.sScale);

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(50 * 1024 * 1024) // 50 Mb
                .diskCacheFileCount(300)
                .imageDownloader(new MyImageDownloader(this))
                .tasksProcessingOrder(QueueProcessingType.LIFO)
//                .writeDebugLogs() // Remove for release app
                .diskCacheExtraOptions(LengthUtil.sWidthPix, LengthUtil.sHeightPix, null)
                .build();
        ImageLoader.getInstance().init(config);

        Network.init(this);

        MyData.getInstance().init(this);

        RedPointTip.init(this);
        MaskGuide.init(this);

        FileDownloader.init(getApplicationContext());

        if (MyData.getInstance().isLogin()) {
            LoginActivity.syncWebviewCookie(this);
        }
    }

    public static Context getContext() {
        return context;
    }

}
