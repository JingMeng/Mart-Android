package net.coding.mart.common.local;

import android.content.Context;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadSampleListener;
import com.liulishuo.filedownloader.FileDownloader;

import net.coding.mart.common.BaseActivity;
import net.coding.mart.json.File;
import net.coding.mart.json.reward.Published;

/**
 * Created by chenchao on 2016/12/7.
 */

public class DownloadFileImp implements DownloadFile {

    BaseActivity activity;
    Published published;

    public DownloadFileImp(BaseActivity activity, Published published) {
        this.activity = activity;
        this.published = published;
    }

    public DownloadFileImp(BaseActivity activity) {
        this.activity = activity;
    }

    public void setPublished(Published published) {
        this.published = published;
    }

    @Override
    public void download(Context context, String url) {
        File file = null;
        for (File item : published.filesToShow) {
            if (url.equals(item.url)) {
                file = item;
                break;
            }
        }

        if (file == null) {
            return;
        }

        String fileNameNoSuffix = file.filename;
        int pointPos = fileNameNoSuffix.lastIndexOf(".");
        if (pointPos != -1) {
            fileNameNoSuffix = file.filename.substring(0, pointPos);
        }

        FileHelp fileHelp = new FileHelp(published.id, published.name, fileNameNoSuffix, url);
        String path = String.format("%s%s/%s", activity.getFilesDir().getPath(), DownloadFile.LOCAL_PATH, fileHelp.getLocalFileName());

        java.io.File targetFile = new java.io.File(path);
        if (targetFile.exists()) {
            FileProvider.openFile(context, new java.io.File(path));
        } else {
            BaseDownloadTask downloadTask = FileDownloader.getImpl()
                    .create(url)
                    .setPath(path)
                    .setListener(new FileDownloadSampleListener() {
                        @Override
                        protected void completed(BaseDownloadTask task) {
                            FileProvider.openFile(context, new java.io.File(path));
                        }

                        @Override
                        protected void error(BaseDownloadTask task, Throwable e) {
                            activity.showButtomToast("下载失败");
                        }
                    });
            downloadTask.start();
            activity.showButtomToast("开始下载");
        }
    }

}
