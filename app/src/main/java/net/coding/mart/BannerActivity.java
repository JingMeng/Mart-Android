package net.coding.mart;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.assist.ImageSize;

import net.coding.mart.common.BaseActivity;
import net.coding.mart.common.ImageLoadTool;
import net.coding.mart.common.LoginBackground;

import java.io.File;

public class BannerActivity extends BaseActivity {

    private String mLink = "https://codemart.com/tuijian";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                close();
            }
        }, 1000 * 5);

        settingBackground();


    }

    public void clickJumpBanner(View v) {
        finish();
    }

    private void close() {
        if (!isFinishing()) {
            finish();
        }
    }

    private void settingBackground() {
        LoginBackground loginBackground = new LoginBackground(this);
        loginBackground.update();

        LoginBackground.PhotoItem photoItem = loginBackground.getPhoto();

        File file = photoItem.getCacheFile(this);
        ImageLoadTool.imageLoader.clearMemoryCache();

        ImageView image = (ImageView) findViewById(R.id.backgroundImage);

        if (image != null) {
            if (file.exists()) {
                mLink = photoItem.getLink();
                image.setImageBitmap(ImageLoadTool.imageLoader.loadImageSync("file://" + file.getPath(), ImageLoadTool.enterOptions));
            } else {
                ImageSize imageSize = new ImageSize(LengthUtil.sWidthPix, LengthUtil.sHeightPix);
                image.setImageBitmap(ImageLoadTool.imageLoader.loadImageSync("drawable://" + R.drawable.banner_main_default, imageSize));
            }

            image.setOnClickListener(v -> {
                WebActivity_.intent(BannerActivity.this).url(mLink).start();
                close();
            });
        }
    }
}
