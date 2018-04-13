package net.coding.mart.setting;

import android.content.Intent;
import android.support.v7.app.AlertDialog;

import net.coding.mart.R;
import net.coding.mart.activity.setting.PushSettingActivity_;
import net.coding.mart.common.BackActivity;
import net.coding.mart.common.MyData;
import net.coding.mart.common.umeng.UmengEvent;
import net.coding.mart.common.util.RxBus;
import net.coding.mart.common.widget.ListItem1;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.File;

@EActivity(R.layout.activity_setting_push)
public class SettingHomeActivity extends BackActivity {

    @ViewById
    ListItem1 cacheSize;

    @AfterViews
    void initSettingHomeActivity() {
        updateCacheSize();
    }

    @Click
    void pushSetting() {
        PushSettingActivity_.intent(this).start();
    }

    @Click
    void loginOutLayout() {
        new AlertDialog.Builder(this)
                .setTitle("退出登录？")
                .setPositiveButton("确定", (dialog, which) -> {
                    umengEvent(UmengEvent.ACTION, "设置_ 退出登录");

                    MyData.getInstance().loginOut(this);
                    RxBus.getInstance().send(new RxBus.UpdateMainEvent());

                    Intent intent = new Intent();
                    intent.putExtra("loginOut", true);
                    setResult(RESULT_OK, intent);
                    finish();

                })
                .setNegativeButton("取消", null)
                .show();
    }

    @Click
    void cacheSize() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.clear_cache_message)
                .setPositiveButton("确定", ((dialog, which) -> {
                    File[] cacheDir = getAllCacheDir();
                    for (File item : cacheDir) {
                        deleteFiles(item);
                    }
                    showMiddleToast("清除缓存成功");

                    updateCacheSize();
                }))
                .setNegativeButton("取消", null)
                .show();
    }

    @Background
    void updateCacheSize() {
        File[] cacheDir = getAllCacheDir();

        long size = 0;
        for (File dir : cacheDir) {
            size += getFileSize(dir);
        }
        String sizeString = String.format("%.2f MB", (double) size / 1024 /1024);

        dispayCacheSize(sizeString);
    }

    @UiThread
    void dispayCacheSize(String size) {
        cacheSize.setText2(size);
    }

    private File[] getAllCacheDir() {
        return new File[] {
                getCacheDir(),
                getExternalCacheDir()
        };
    }

    private long getFileSize(File file) {
        if (file == null) {
            return 0;
        }

        if (file.isDirectory()) {
            long size = 0;
            for (File item : file.listFiles()) {
                size += getFileSize(item);
            }
            return size;
        } else {
            return file.length();
        }
    }

    public static void deleteFiles(File file) {
        if (file == null || !file.exists()) {
            return;
        }

        if (file.isDirectory()) {
            for (File item : file.listFiles()) {
                deleteFiles(item);
            }
            file.delete();
        } else if (file.isFile()) {
            file.delete();
        }
    }
}
