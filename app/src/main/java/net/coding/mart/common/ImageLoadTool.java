package net.coding.mart.common;

import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import net.coding.mart.R;

/**
 * Created by chaochen on 14-9-22.
 */
public class ImageLoadTool {

    //两像素圆角
    public static final DisplayImageOptions optionsRounded = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.mipmap.ic_default_image)
            .showImageForEmptyUri(R.mipmap.ic_default_image)
            .showImageOnFail(R.mipmap.ic_default_image)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true)
            .displayer(new RoundedBitmapDisplayer(2))
            .build();

    public static DisplayImageOptions enterOptions = new DisplayImageOptions
            .Builder()
//            .showImageOnLoading(R.drawable.ic_default_user)
//            .showImageForEmptyUri(R.drawable.ic_default_user)
//            .showImageOnFail(R.drawable.ic_default_user)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true)
            .build();
//    public static DisplayImageOptions options = new DisplayImageOptions
//            .Builder()
//            .showImageOnLoading(R.drawable.ic_default_user)
//            .showImageForEmptyUri(R.drawable.ic_default_user)
//            .showImageOnFail(R.drawable.ic_default_user)
//            .cacheInMemory(true)
//            .cacheOnDisk(true)
//            .considerExifParams(true)
//            .build();
    public static DisplayImageOptions bannerOptions = new DisplayImageOptions
            .Builder()
            .showImageOnLoading(R.drawable.bg_maopao_comment)
            .showImageForEmptyUri(R.drawable.bg_maopao_comment)
            .showImageOnFail(R.drawable.bg_maopao_comment)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true)
            .build();
    public static DisplayImageOptions optionsImage = new DisplayImageOptions
            .Builder()
            .showImageOnLoading(R.mipmap.ic_default_image)
            .showImageForEmptyUri(R.mipmap.ic_default_image)
            .showImageOnFail(R.mipmap.ic_default_image)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true)
            .build();

    public static DisplayImageOptions optionsBannerImage = new DisplayImageOptions
            .Builder()
            .showImageOnLoading(R.mipmap.ic_default_image)
            .showImageForEmptyUri(R.mipmap.ic_default_image)
            .showImageOnFail(R.mipmap.ic_default_image)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true)
            .build();

    public static DisplayImageOptions optionsPublishJob = new DisplayImageOptions
            .Builder()
            .showImageOnLoading(R.mipmap.ic_default_loading_publish_job)
            .showImageForEmptyUri(R.mipmap.ic_default_publish_job)
            .showImageOnFail(R.mipmap.ic_default_publish_job)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true)
            .build();

    public static DisplayImageOptions optionsUser = new DisplayImageOptions
            .Builder()
            .showImageOnLoading(R.mipmap.ic_default_user)
            .showImageForEmptyUri(R.mipmap.ic_default_user)
            .showImageOnFail(R.mipmap.ic_default_user)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true)
            .build();

    public static DisplayImageOptions optionIDCard = new DisplayImageOptions
            .Builder()
            .cacheOnDisk(true)
            .considerExifParams(true)
            .build();

    public static ImageLoader imageLoader = ImageLoader.getInstance();

    public static String makeSmallUrl(ImageView view, String url, int width) {
        if (url.contains("?imageMogr2")) {
            return String.format("%s/thumbnail/!%dx%d", url, width, width);
        } else {
            return String.format("%s?imageMogr2/thumbnail/!%dx%d", url, width, width);
        }
    }

    public static String makeSmallUrl(ImageView view, String url) {
        return url;
    }

    public ImageLoadTool() {
    }

    public static void loadImage(ImageView imageView, String url, DisplayImageOptions options) {
        imageLoader.displayImage(url, imageView, options);
    }

    public static void loadImage(ImageView imageView, String url) {
        imageLoader.displayImage(makeSmallUrl(imageView, url), imageView, optionsImage);
    }

    public static void loadBannerImage(ImageView imageView, String url) {
        imageLoader.displayImage(makeSmallUrl(imageView, url), imageView, optionsBannerImage);
    }

    public static void loadImageUser(ImageView imageView, String url) {
        imageLoader.displayImage(makeSmallUrl(imageView, url), imageView, optionsUser);
    }


    public static void loadImageUser(ImageView imageView, String url, int maxWidth) {
        imageLoader.displayImage(makeSmallUrl(imageView, url, maxWidth), imageView, optionsUser);
    }

    public static void loadImagePublishJob(ImageView imageView, String url) {
        imageLoader.displayImage(makeSmallUrl(imageView, url), imageView, optionsPublishJob);
    }


//
//    public void loadImage(ImageView imageView, String url, SimpleImageLoadingListener animate) {
//        imageLoader.displayImage(makeSmallUrl(imageView, url), imageView, options, animate);
//    }
//
//    public void loadImage(ImageView imageView, String url, DisplayImageOptions imageOptions) {
//        imageLoader.displayImage(makeSmallUrl(imageView, url), imageView, imageOptions);
//    }
//
//    public void loadImage(ImageView imageView, String url, DisplayImageOptions displayImageOptions, SimpleImageLoadingListener animate) {
//        imageLoader.displayImage(url, imageView, displayImageOptions, animate);
//    }
//
//    public void loadImageFromUrl(ImageView imageView, String url) {
//        imageLoader.displayImage(url, imageView, options);
//    }
//
//    public void loadImageFromUrl(ImageView imageView, String url, DisplayImageOptions displayImageOptions) {
//        imageLoader.displayImage(url, imageView, displayImageOptions);
//    }


}

