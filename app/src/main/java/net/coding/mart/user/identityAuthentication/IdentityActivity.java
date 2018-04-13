package net.coding.mart.user.identityAuthentication;

import android.support.v4.app.Fragment;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadSampleListener;
import com.liulishuo.filedownloader.FileDownloader;

import net.coding.mart.R;
import net.coding.mart.common.BackActivity;
import net.coding.mart.common.Global;
import net.coding.mart.common.MyData;
import net.coding.mart.common.local.FileProvider;
import net.coding.mart.json.mart2.user.MartUser;
import net.coding.mart.login.LoginActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import java.io.File;


@EActivity(R.layout.activity_identity)
public class IdentityActivity extends BackActivity {

    public static final String IDENTITY_FILE_NAME = "签署身份认证授权与承诺书.pdf";
    public static final String IDENTITY_SIGN_FILE_NAME = "身份认证授权与承诺书.pdf";

    @AfterViews
    void initIdentityActivity() {
        LoginActivity.loadCurrentUser(this, new LoginActivity.LoadUserCallback() {
            @Override
            public void onSuccess() {
                MartUser user = MyData.getInstance().getData().user;
                if (user.isPassed()) {
                    jumpFragment3();
                } else if (user.isCheking()) {
                    jumpFragment2();
                } else {
                    jumpFragment1();
                }
            }

            @Override
            public void onFail() {

            }
        });
    }

    public void jumpFragment1() {
        Fragment fragment = IdentityFragment1_.builder().build();
        switchFragment(fragment);
    }

    public void jumpFragment2() {
        Fragment fragment = IdentityFragment2_.builder().build();
        switchFragment(fragment);
    }

    public void jumpFragment3() {
        Fragment fragment = IdentityFragment3_.builder().build();
        switchFragment(fragment);
    }

    private void switchFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    public void popIdentityTip() {
        String url = "https://dn-coding-net-production-public-file.qbox.me/%E8%BA%AB%E4%BB%BD%E8%AE%A4%E8%AF%81%E6%8E%88%E6%9D%83%E4%B8%8E%E6%89%BF%E8%AF%BA%E4%B9%A6_160816.pdf";
        downloadAuthorizationTemplate(url, IDENTITY_FILE_NAME);
    }

    private void downloadAuthorizationTemplate(String url, String fileName) {
        File downloadFile = new File(MyData.getAccountDir(this), fileName);
        if (downloadFile.exists()) {
            FileProvider.openFile(IdentityActivity.this, downloadFile);
            return;
        }

        showSending(true, "正在下载授权文件...");

        final String AGREE_TERMS = url;
        // 2.启动下载
//        AsyncHttpClient client = MyAsyncHttpClient.createClient(IdentityActivity.this);
//        client.setMaxRetriesAndTimeout(3, 3000);
//        client.get(AGREE_TERMS, new FileAsyncHttpResponseHandler(downloadFile) {
//            @Override
//            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
//                showMiddleToast("下载失败，请稍后再试！");
//            }
//
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, File file) {
//                boolean renameTo = file.renameTo(downloadFile);
//
//                if (renameTo) {
//                    // 下载成功 rename 成功 show dialog!
//                    FileProvider.openFile(IdentityActivity.this, downloadFile);
//                } else {
//                    Log.e("onSuccess", "renameTo false");
//                    showMiddleToast("下载失败，请稍后再试！");
//                }
//            }
//
//            @Override
//            public void onFinish() {
//                super.onFinish();
//            }
//        });

        FileDownloader.getImpl().create(AGREE_TERMS)
                .setPath(downloadFile.getPath())
                .setListener(new FileDownloadSampleListener() {
                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        super.error(task, e);
                        showSending(false, null);

                        showMiddleToast("下载失败，请稍后再试！");
                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        super.completed(task);
                        showSending(false, null);

                        // 下载成功 rename 成功 show dialog!
                        FileProvider.openFile(IdentityActivity.this, downloadFile);
                    }
                })
                .start();
    }

    void downloadIdentitySignFile(String fileId) {
        String url = String.format("%s/api/user/identity/sign/%s", Global.HOST, fileId);
        downloadAuthorizationTemplate(url, IDENTITY_SIGN_FILE_NAME);
    }

}
