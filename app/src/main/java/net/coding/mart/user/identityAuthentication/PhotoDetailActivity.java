package net.coding.mart.user.identityAuthentication;

import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import net.coding.mart.R;
import net.coding.mart.common.BackActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;

import java.io.File;

@EActivity(R.layout.activity_photo_detail)
public class PhotoDetailActivity extends BackActivity {

    @Extra
    String url = "";

    @Extra
    boolean showDelete = true;

    @Extra
    String title;

    @ViewById
    SubsamplingScaleImageView imageView;

    @AfterViews
    void initPhotoDetailActivity() {
        if (title != null) {
            setActionBarTitle(title);
        }

        imageView.setOnClickListener(v -> finish());

         File file = ImageLoader.getInstance().getDiskCache().get(url);
        if (file != null && file.exists()) {
            imageView.setImage(ImageSource.uri(file.getPath()));
        } else {
            showMiddleToast("显示图片失败");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.phone_detail, menu);
        MenuItem item = menu.findItem(R.id.actionDelete);
        if (!showDelete) {
            item.setVisible(false);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @OptionsItem
    void actionDelete() {
        new AlertDialog.Builder(this)
                .setMessage("删除照片?")
                .setPositiveButton("删除", ((dialog, which) -> {
                    setResult(RESULT_OK);
                    finish();
                }))
                .setNegativeButton("取消", null)
                .show();
    }
}
