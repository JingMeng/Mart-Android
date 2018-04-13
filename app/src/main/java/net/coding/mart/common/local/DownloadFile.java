package net.coding.mart.common.local;

import android.content.Context;

/**
 * Created by chenchao on 2016/12/7.
 */

public interface DownloadFile {

    String LOCAL_PATH = "/download/rewards";

    void download(Context context, String url);
}
