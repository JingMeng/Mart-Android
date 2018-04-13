package net.coding.mart.user.identityAuthentication;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;

import net.coding.mart.R;
import net.coding.mart.common.BaseFragment;
import net.coding.mart.common.Color;
import net.coding.mart.common.Global;
import net.coding.mart.common.MyData;
import net.coding.mart.common.local.FileProvider;
import net.coding.mart.json.Network;
import net.coding.mart.json.NewBaseObserver;
import net.coding.mart.json.mart2.user.MartUser;
import net.coding.mart.json.user.identity.IdentitySign;
import net.coding.mart.login.LoginActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@EFragment(R.layout.fragment_identity_fragment2)
public class IdentityFragment2 extends BaseFragment {

    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 1;

    @ViewById
    ImageView qrIcon;

    @ViewById
    TextView identity1, identity2, identityTip, terms1;

    @ViewById
    ImageView circle1, circle2;

    String qrCode;
    Bitmap qrBitmap = null;
    Handler handler;

    @NonNull
    public static BitmapDrawable generateBitmapDrawable(Context context, String qr) {
        byte[] decode = Base64.decode(qr, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length);
        return new BitmapDrawable(context.getResources(), bitmap);
    }

    @AfterViews
    void initIdentityFragment2() {
        circle1.setImageResource(R.mipmap.identity_circle_hook);
        identity1.setTextColor(Color.font_2);
        circle2.setImageResource(R.mipmap.identity_circle_dark_2);
        identity2.setTextColor(Color.font_2);

        terms1.setText(Global.createBlueHtml(
                "1、请使用您实名认证过的微信APP扫描上方二维码，仔细阅读“身份认证授权与承诺书”，并在指定区域完成电子签名。",
                "如何扫描二维码？", ""));

        identityTip.setText(Global.createBlueHtml("查看", "“身份认证授权与承诺书”", ""));

        getDataFromNetwork();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        doNext(requestCode, grantResults);
    }

    private void doNext(int requestCode, int[] grantResults) {
        if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                saveImage2(qrBitmap);
            } else {
                showMiddleToast("申请授权失败");
            }
        }
    }

    private void getDataFromNetwork() {
        Network.getRetrofit(getActivity())
                .getIdentitySign()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NewBaseObserver<IdentitySign>(getActivity()) {
                    @Override
                    public void onSuccess(IdentitySign data) {
                        super.onSuccess(data);

                        int pos = data.signerUriQr.indexOf(",");
                        String qr = data.signerUriQr;
                        if (pos != -1) {
                            qr = qr.substring(pos + 1, qr.length());
                        }
                        qrCode = qr;
                        BitmapDrawable bitmapDrawable = generateBitmapDrawable(getContext(), qrCode);
                        qrIcon.setImageDrawable(bitmapDrawable);

                        qrBitmap = bitmapDrawable.getBitmap();
                        qrIcon.setOnClickListener(v -> popDialogToSaveQrcode());
                        qrIcon.setOnLongClickListener(v -> {
                            popDialogToSaveQrcode();
                            return false;
                        });

                        startCheck();
                    }

                });
    }

    private void popDialogToSaveQrcode() {
        new AlertDialog.Builder(getActivity())
                .setMessage("将二维码保存到相册")
                .setPositiveButton("确定", (dialog, which) -> saveImage2(qrBitmap))
                .setNegativeButton("取消", null)
                .show();
    }

    void saveImage2(Bitmap bitmap) {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
            return;
        }

        String uriString = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bitmap, "title", "description");
        getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(uriString)));

        new AlertDialog.Builder(getActivity())
                .setMessage("已保存二维码到相册的 \"Pictures\" 目录下")
                .setPositiveButton("确定", null)
                .show();
    }

    public void saveImageToGallery(Context context, Bitmap bmp) {
        if (bmp == null) {
            showMiddleToast("保存出错");
            return;
        }
        // 首先保存图片
        File appDir = new File(getContext().getCacheDir(), "ywq");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            showMiddleToast("文件未发现");
            e.printStackTrace();
        } catch (IOException e) {
            showMiddleToast("保存出错了...");
            e.printStackTrace();
        } catch (Exception e) {
            showMiddleToast("保存出错了...");
            e.printStackTrace();
        }

        // 最后通知图库更新
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = FileProvider.makeUri(getActivity(), file);
        intent.setData(uri);
        context.sendBroadcast(intent);
        showMiddleToast("保存成功了");
    }

    @Override
    public void onDestroy() {
        stopCheck();
        super.onDestroy();
    }

    @Click
    void terms1() {
        if (TextUtils.isEmpty(qrCode)) {
            showMiddleToast("请等待验证码载入");
            getDataFromNetwork();

            return;
        }

        ScanQRCodeActivity_.intent(this).qrCode(qrCode).start();
    }

    @Click
    void identityTip() {
        ((IdentityActivity) getActivity()).popIdentityTip();
    }

    private void checkState() {
        LoginActivity.loadCurrentUser(getActivity(), new LoginActivity.LoadUserCallback() {
            @Override
            public void onSuccess() {
                MartUser data = MyData.getInstance().getData().user;
                if (data.isPassed()) {
                    ((IdentityActivity) getActivity()).jumpFragment3();
                } else if (data.isNew()) {
                    ((IdentityActivity) getActivity()).jumpFragment1();
                } else {
                    startCheck();
                }
            }

            @Override
            public void onFail() {

            }
        });
    }

    private void startCheck() {
        if (handler == null) {
            handler = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    checkState();
                }
            };
        }

        handler.sendEmptyMessageDelayed(0, 2000);
    }

    private void stopCheck() {
        if (handler == null) {
            return;
        }

        handler.removeMessages(0);
        handler = null;
    }


}
