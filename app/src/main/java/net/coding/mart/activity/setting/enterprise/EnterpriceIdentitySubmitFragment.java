package net.coding.mart.activity.setting.enterprise;


import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tbruyelle.rxpermissions.RxPermissions;

import net.coding.mart.R;
import net.coding.mart.common.BaseFragment;
import net.coding.mart.common.ImageLoadTool;
import net.coding.mart.common.local.FileProvider;
import net.coding.mart.json.BaseObserver;
import net.coding.mart.json.Network;
import net.coding.mart.json.NewBaseObserver;
import net.coding.mart.json.enterprise.Attachment;
import net.coding.mart.json.enterprise.EnterpriseCertificate;
import net.coding.mart.user.identityAuthentication.PhotoDetailActivity_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@EFragment(R.layout.fragment_enterprice_identity_submit)
public class EnterpriceIdentitySubmitFragment extends BaseFragment {

    public static final File PHOTO_DIR = new File(Environment.getExternalStorageDirectory() + "/Coding/Photo");
    private static final int CAMERA_WITH_DATA = 3023;
    /*用来标识请求gallery的activity*/
    private static final int PHOTO_PICKED_WITH_DATA = 3021;
    private static final int RESULT_REMOVE_FORE = 1;

    File mCurrentPhotoFile;
    ImageView mCurrentImageView;

    //    String positiveURL = null;
    Attachment licenceAttachment;

    public static File positiveFile = null;

    @FragmentArg
    EnterpriseCertificate info;

    @ViewById
    ImageView businessLicence;

    @ViewById
    View sendButton;

    @ViewById
    EditText name, icard;

    @AfterViews
    void initEnterpriceIdentitySubmitFragment() {

        try {
            PHOTO_DIR.mkdirs();
        } catch (Exception e) {
            showMiddleToast("创建文件目录失败");
        }
    }

    private Context ctx() {
        return getActivity();
    }

    private Activity act() {
        return getActivity();
    }

    @Click
    void businessLicence() {
        getPhoto(businessLicence, licenceAttachment, RESULT_REMOVE_FORE);
    }

    @Click
    void sendButton() {
        String nameString = name.getText().toString();
        String cardString = icard.getText().toString();

        if (TextUtils.isEmpty(nameString)) {
            showMiddleToast("企业名称不能为空！");
            return;
        }

        if (TextUtils.isEmpty(cardString)) {
            showMiddleToast("企业执照编号不能为空！");
            return;
        }

        // 验证上传
        if (licenceAttachment == null || TextUtils.isEmpty(licenceAttachment.url)) {
            showMiddleToast("请上传营业执照扫描件！");
            return;
        }

        Network.getRetrofit(getActivity())
                .postEnterpriseCertificate(nameString, cardString, licenceAttachment.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NewBaseObserver<EnterpriseCertificate>(getActivity()) {
                    @Override
                    public void onSuccess(EnterpriseCertificate data) {
                        super.onSuccess(data);
                        showSending(false);

                        ((EnterpriseIdentityActivity) getActivity()).switchFragment(data);
                    }

                    @Override
                    public void onFail(int errorCode, @NonNull String error) {
                        super.onFail(errorCode, error);
                        showSending(false);
                    }
                });
        showSending(true, "正在提交数据...");
    }

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private void getPhoto(ImageView view, Attachment attachment, int result) {
        if (attachment != null && !TextUtils.isEmpty(attachment.url)) {
            PhotoDetailActivity_.intent(this).url(attachment.url).startForResult(result);
            return;
        }

        verifyStoragePermissions(getActivity());
        int permission = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            showMiddleToast("请授予SD卡访问权限！");
            return;
        }

        String status = Environment.getExternalStorageState();
        if (!status.equals(Environment.MEDIA_MOUNTED)) { // 没有SD卡
            showMiddleToast("没有检测到SD卡，无法上传照片！");
            return;
        }
        mCurrentImageView = view;

        // 显示选择项
        new AlertDialog.Builder(ctx())
                .setItems(new String[]{"拍照", "从相册中选择"}, (dialog, which) -> {
                    if (which == 0) {// 拍照
                        switchPhotograph();
                    } else { // 从相册选择
                        switchFromAlbum();
                    }
                })
                .show();
    }

    private void switchPhotograph() {
        // 1.生成File路径
        PHOTO_DIR.mkdirs();// 创建照片的存储目录
        mCurrentPhotoFile = new File(PHOTO_DIR, getPhotoFileName());// 给新照的照片文件命名

        Log.e("TEST_TAG", "switchPhotograph mCurrentPhotoFile = " + mCurrentPhotoFile);

        verifyStoragePermissions(getActivity());

        RxPermissions rxPermissions = new RxPermissions(getActivity());
        rxPermissions.request(Manifest.permission.CAMERA)
                .subscribe(granted -> {
                    if (granted) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.makeUri(getActivity(), mCurrentPhotoFile));
                        startActivityForResult(intent, CAMERA_WITH_DATA);
                    } else {
                        showMiddleToast("请授予\"相机\"权限！");
                    }
                });
    }

    private void switchFromAlbum() {
        try {
            // Launch picker to choose photo for selected contact
            final Intent intent = getPhotoPickIntent();
            startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getActivity(), "打开相册失败！",
                    Toast.LENGTH_LONG).show();
        }
    }

    // 封装请求Gallery的intent
    public static Intent getPhotoPickIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
//        intent.putExtra("crop", "false");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 80);
        intent.putExtra("outputY", 80);
        intent.putExtra("return-data", true);
        return intent;
    }


    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        switch (requestCode) {
            case PHOTO_PICKED_WITH_DATA: {// 调用Gallery返回的
                if (resultCode == Activity.RESULT_OK) {
                    showSending(true, "正在压缩图片...");

                    final Intent finalData = data;

                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            Bitmap photo = ImageLoader.getInstance().loadImageSync(finalData.getData().toString());
                            // 下面就是显示照片了
                            // 压缩图片
                            photo = comp(photo);

                            mCurrentPhotoFile = new File(PHOTO_DIR, getPhotoFileName());// 给新照的照片文件命名

                            // 写入文件
                            saveBitmapToFile(photo, mCurrentPhotoFile);

                            final Bitmap finalPhoto = photo;
                            getActivity().runOnUiThread(() -> {
                                showSending(false, "");
                                setPhotoToImageView(finalPhoto);
                            });

                        }
                    }).start();
                }
                break;
            }

            case CAMERA_WITH_DATA: {
                if (resultCode == Activity.RESULT_OK) {
                    showSending(true, "正在压缩图片...");
                    new Thread(() -> {
                        Log.e("TEST_TAG", "CAMERA_WITH_DATA mCurrentPhotoFile = " + mCurrentPhotoFile);

                        // 拍照返回，不裁剪，直接用 取出bitmap
                        Bitmap photo = ImageLoader.getInstance().loadImageSync("file://" + mCurrentPhotoFile.getPath());
                        // 压缩图片
                        photo = comp(photo);
                        // 写入文件
                        saveBitmapToFile(photo, mCurrentPhotoFile);

                        final Bitmap finalPhoto = photo;
                        getActivity().runOnUiThread(() -> {
                            showSending(false);
                            setPhotoToImageView(finalPhoto);
                        });
                    }).start();

                }
                break;
            }

            case RESULT_REMOVE_FORE:
                if (resultCode == Activity.RESULT_OK) {
                    clearUploadState(businessLicence);
                }
                break;


            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void clearUploadState(ImageView imageView) {
        // 清除对应的图片
        // 取得 对应的imageView
        imageView.setImageDrawable(null);
        imageView.setTag(null);

        View parent = (View) imageView.getParent();
        AsyncHttpClient client = (AsyncHttpClient) parent.getTag();
        if (client != null) {
            client.cancelRequests(ctx(), true);
            parent.setTag(null);
        }

        if (imageView == businessLicence) {
            licenceAttachment = null;
        }
    }


    /**
     * 将bitmap 保存到file
     */
    private void saveBitmapToFile(Bitmap bitmap, File file) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 用当前时间给取得的图片命名
     */
    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'yyyyMMddHHmmss");
        return dateFormat.format(date) + ".jpg";
    }

    private Bitmap comp(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if (baos.toByteArray().length / 1024 > 2048) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 80, baos);//这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 1024f; //这里设置高度为800f
        float ww = 1024f; //这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
    }

    private Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 2048) {    //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    private void setPhotoToImageView(Bitmap photo) {
        //缓存用户选择的图片
//        mCurrentImageView.setImageBitmap(photo);
//        ImageLoadTool.loadImage(mCurrentImageView, photo, ImageLoadTool.optionIDCard);
//        mCurrentImageView.setTag(mCurrentPhotoFile);

        // 启动上传
        uploadImage(mCurrentImageView, mCurrentPhotoFile);
    }

    private void uploadImage(final ImageView imageView, final File file) {
        MediaType type = MediaType.parse("image");
        RequestBody body = RequestBody.create(type, file);

        MultipartBody.Part part = MultipartBody.Part.createFormData("files", file.getName(), body);

        Network.getRetrofit(getActivity())
                .postImage(part)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<Attachment>(getActivity()) {
                               @Override
                               public void onSuccess(Attachment data) {
                                   super.onSuccess(data);
                                   showSending(false);

                                   licenceAttachment = data;
                                   positiveFile = file;
                                   ImageLoadTool.loadImage(imageView, data.url, ImageLoadTool.optionIDCard);
                                   imageView.setTag(file);
                               }

                               @Override
                               public void onFail(int errorCode, @NonNull String error) {
                                   super.onFail(errorCode, error);

                                   showSending(false);
                                   new AlertDialog.Builder(act())
                                           .setMessage("上传图片失败，重新上传？")
                                           .setPositiveButton("重新上传", ((dialog, which) -> {
                                               uploadImage(imageView, file);
                                           }))
                                           .setNegativeButton("取消", null)
                                           .show();
                               }
                           }

                );

        showSending(true, "上传图片...");
    }

}
