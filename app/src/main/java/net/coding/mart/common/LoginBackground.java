package net.coding.mart.common;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;

import net.coding.mart.LengthUtil;
import net.coding.mart.MyAsyncHttpClient;
import net.coding.mart.common.util.SimpleSHA1;
import net.coding.mart.json.BaseObserver;
import net.coding.mart.json.Network;

import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cz.msebera.android.httpclient.Header;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by chaochen on 15/1/5.
 */
public class LoginBackground {

    static final String TAG = "LoginBackground";
    private Context context;

    public LoginBackground(Context context) {
        this.context = context;
    }

    public void update() {
        if (!Global.isWifiConnected(context)) {
            return;
        }

        if (needUpdate()) {
            Network.getRetrofit(context)
                    .getBanner()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<List<PhotoItem>>(context) {
                        @Override
                        public void onSuccess(List<PhotoItem> data) {
                            super.onSuccess(data);
                            MyData.saveBackgrounds(context, (ArrayList<PhotoItem>)data);
                            downloadPhotos();
                            MyData.setCheckLoginBackground(context);
                        }

                        @Override
                        public void onFail(int errorCode, @NonNull String error) {
                            // 不显示错误提示
                            MyData.setCheckLoginBackground(context);
                        }
                    });
        } else {
            downloadPhotos();
        }
    }

    public PhotoItem getPhoto() {
        ArrayList<PhotoItem> list = MyData.loadBackgrounds(context);
        ArrayList<PhotoItem> cached = new ArrayList<>();
        for (PhotoItem item : list) {
            if (item.isCached(context)) {
                cached.add(item);
            }
        }

        int max = cached.size();
        if (max == 0) {
            return new PhotoItem();
        }

        int index = new Random().nextInt(max);
        return cached.get(index);
    }

    public int getPhotoCount() {
        return MyData.loadBackgrounds(context).size();
    }

    private boolean needUpdate() {
        return true;
    }

    /* 下载图片先判断是否已下载，没有下载就先下载到一个临时目录，下载完后再将临时目录的文件拷贝到放背景图的目录。
     * 因为下载的线程可能被干掉，导致下载的图片文件有问题。
     */
    private void downloadPhotos() {
        if (!Global.isWifiConnected(context)) {
            return;
        }

        ArrayList<PhotoItem> lists = MyData.loadBackgrounds(context);
        for (PhotoItem item : lists) {
            final File fileTaget = item.getCacheFile(context);
            if (!fileTaget.exists()) {
                AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
                final String url = String.format("%s?imageMogr2/thumbnail/!%d", item.getUrl(), LengthUtil.getsWidthPix());
                File sourceFile = item.getCacheTempFile(context);
                if (sourceFile.exists()) {
                    sourceFile.delete();
                }
                Log.d(TAG, url + " " + sourceFile.getPath());

                client.get(context, url, new FileAsyncHttpResponseHandler(sourceFile) {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                        Log.d(TAG, url + " " + file.getPath() + " failure");
                        if (file.exists()) {
                            file.delete();
                        }
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, File file) {
                        Log.d(TAG, url + " " + file.getPath() + " success");
                        file.renameTo(fileTaget);
                    }
                });
                // 图片较大，可能有几兆，超时设长一点
                client.setTimeout(10 * 60 * 1000);
            }
        }
    }

    public static class PhotoItem implements Serializable {
        private static final long serialVersionUID = -686247542381591648L;
        String url = "";
        String link = "";

        public PhotoItem(JSONObject json) {
            link = json.optString("link", "");
            url = json.optString("image", "");
        }

        public PhotoItem() {
        }

        public String getLink() {
            return link;
        }

        public String getUrl() {
            return url;
        }

        private String getCacheName() {
            try {
                return SimpleSHA1.sha1(url);
            } catch (Exception e) {
                Global.errorLog(e);
            }

            return "noname";
        }

        public File getCacheFile(Context ctx) {
            File file = new File(getPhotoDir(ctx), getCacheName());
            return file;
        }

        // 下载的文件先放在这里，下载完成后再放到 getCacheFile 目录下
        public File getCacheTempFile(Context ctx) {
            File fileDir = new File(getPhotoDir(ctx), "temp");
            if (!fileDir.exists() || !fileDir.isDirectory()) {
                fileDir.mkdirs();
            }

            File file = new File(fileDir, getCacheName());
            return file;
        }

        public boolean isCached(Context ctx) {
            return getCacheFile(ctx).exists();
        }

        private File getPhotoDir(Context ctx) {
            final String dirName = "BACKGROUND";
            File root = ctx.getExternalFilesDir(null);
            File dir = new File(root, dirName);
            if (!dir.exists() || !dir.isDirectory()) {
                dir.mkdirs();
            }

            return dir;
        }

    }

}
