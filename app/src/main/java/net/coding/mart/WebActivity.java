package net.coding.mart;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import net.coding.mart.activity.reward.detail.RewardDetailWebActivity;
import net.coding.mart.common.BackActivity;
import net.coding.mart.common.Global;
import net.coding.mart.common.local.DownloadFile;
import net.coding.mart.job.JobDetailActivity;
import net.coding.mart.json.Network;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_web)
public class WebActivity extends BackActivity {

    public static final String EXTRA_URL = "EXTRA_URL";
    public static final String EXTRA_TITLE = "EXTRA_TITLE";
    public static final String MART_SUPPORT_URL = "https://codemart.kf5.com/supportbox/index";
    final int MAX_PROGRESS = 100;
    // http://coding-net-public-file.qiniudn.com/e2ad9e73-9960-4966-8c93-f508fe314de2.docx
    private final String MART_FILE = "http://coding-net-public-file.qiniudn.com";
    @Extra(EXTRA_URL)
    protected String url = Global.HOST;
    @Extra(EXTRA_TITLE)
    protected String mTitle = "";
    @ViewById
    protected WebView webView;
    @ViewById
    protected ProgressBar progressBar;
    Handler mHandler;
    int mProgress = 0;

    @AfterViews
    protected void initWebActivity() {
        setActionBarTitle(mTitle);

        progressBar.setMax(MAX_PROGRESS);

        String useAgent = Network.getMapHeaders().get("User-Agent");
        WebSettings settings = webView.getSettings();
        settings.setUserAgentString(useAgent);
        // 使我们的客户咨询可用
        settings.setDomStorageEnabled(true);

        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                int newProgress = msg.what;
                int nextProgress = newProgress + 1;

                if (mProgress == 0) {
                    mProgress = 1;
                    newProgress = 0;
                    nextProgress = 0;
                } else if (mProgress < 80 && newProgress < 80) {

                } else if (mProgress < 80) {
                    nextProgress = newProgress;
                } else {
                    nextProgress = mProgress;
                }

                progressBar.setProgress(newProgress);
                if (newProgress >= MAX_PROGRESS) {
                    progressBar.setVisibility(View.INVISIBLE);
                    AlphaAnimation animation = new AlphaAnimation(1.0f, 0.0f);
                    animation.setDuration(500);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            progressBar.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }
                    });
                    progressBar.startAnimation(animation);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    mHandler.sendEmptyMessageDelayed(nextProgress, 20);
                }
            }
        };

        if (BuildConfig.DEBUG) {
            Log.d("", "WebActivity " + url);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (0 != (getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE)) {
                WebView.setWebContentsDebuggingEnabled(true);
            }
        }

        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient() {

                                       @Override
                                       public void onProgressChanged(WebView view, int newProgress) {
                                           mProgress = newProgress;
                                       }

                                       @Override
                                       public void onReceivedTitle(WebView view, String title) {
                                           ActionBar actionBar = getSupportActionBar();
                                           if (actionBar != null) {
                                               CharSequence actionBarTitle = actionBar.getTitle();
                                               if (actionBarTitle == null || actionBarTitle.length() == 0) {
                                                   setActionBarTitle(title);
                                               }
                                           }
                                       }
                                   }
        );

        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        webView.setWebViewClient(new CustomWebViewClient(this));
//        String useAgent = MyAsyncHttpClient.getMapHeaders().get("User-Agent");
//        webView.getSettings().setUserAgentString(useAgent);
//        webView.loadUrl(url, MyAsyncHttpClient.getMapHeaders());

        webView.loadUrl(url);
        mHandler.sendEmptyMessage(0);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            finish();
        }
    }

    private void popDownloadDialog(String url) {
        if (this instanceof DownloadFile) {
            ((DownloadFile) this).download(this, url);
        }
    }

    public class CustomWebViewClient extends WebViewClient {

        Context mContext;

        public CustomWebViewClient(Context context) {
            mContext = context;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith(MART_SUPPORT_URL)) {
                WebActivity_.intent(WebActivity.this).url(url).start();
                return true;
            } else if (url.startsWith(MART_FILE)) {
                popDownloadDialog(url);
                return true;
            }

            if (mContext instanceof JobDetailActivity
                    || mContext instanceof RewardDetailWebActivity) {
                WebActivity_.intent(mContext).url(url).start();
                return true;
            }

            if (!url.startsWith("http")) {
                try {
                    Intent intent = new Intent();
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                } catch (Exception e) {
                    Global.errorLog(e);
                }

                return true;
            }

            view.loadUrl(url);
            mProgress = 0;
            progressBar.setProgress(0);
            return true;
        }
    }
}
