package net.coding.mart.user.identityAuthentication;

import android.widget.ImageView;

import net.coding.mart.R;
import net.coding.mart.common.BackActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_scan_qrcode)
public class ScanQRCodeActivity extends BackActivity {

    @Extra
    String qrCode;

    @ViewById
    ImageView qrIcon;

    @AfterViews
    void initScanQRCodeActivity() {
        qrIcon.setImageDrawable(IdentityFragment2.generateBitmapDrawable(this, qrCode));
    }
}
