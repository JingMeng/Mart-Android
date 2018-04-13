/**
 *
 */

package net.coding.mart.common.share;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.utils.DeviceConfig;
import com.umeng.socialize.utils.OauthHelper;
import com.umeng.socialize.weixin.controller.UMWXHandler;

import net.coding.mart.LengthUtil;
import net.coding.mart.R;
import net.coding.mart.common.Global;
import net.coding.mart.common.umeng.UmengEvent;
import net.coding.mart.json.reward.Published;

public class CustomShareBoard extends PopupWindow implements OnClickListener {

    public enum ShareType {
        Job, Mart
    }

    ShareType mShareType = ShareType.Job;

    private static UMSocialService mController = UMServiceFactory.getUMSocialService("net.coding.mart");
    private Activity mActivity;
    private ShareData mShareData;

    private View mBackground;
    private View mButtonsLayout;
    private UMQQSsoHandler mQqSsoHandler;
    private QZoneSsoHandler mQZoneSsoHandler;
    private SinaSsoHandler mSinaSsoHandler;
    private UMWXHandler mWXHandler;
    //    private UMEvernoteHandler mEvernoteHandler;
    private ViewGroup allButtonsLayout;

    public static UMSocialService getShareController() {
        return mController;
    }

    public CustomShareBoard(Activity activity, ShareData shareData) {
        this(activity, shareData, ShareType.Job);
    }

    public CustomShareBoard(Activity activity, ShareData shareData, ShareType type) {
        super(activity);
        mShareType = type;
        this.mActivity = activity;
        initView(activity);
        mController.getConfig().closeToast();

        mShareData = shareData;
    }
    
    private void addQQ() {
        mQqSsoHandler.setTargetUrl(mShareData.link);
        mQqSsoHandler.setTitle(mShareData.name);
        mQqSsoHandler.addToSocialSDK();
    }

    private void addWX() {
        String link = mShareData.link;
        String name = mShareData.name;

        mWXHandler.setTargetUrl(link);
        mWXHandler.setTitle(name);
        mWXHandler.addToSocialSDK();
    }

    private void addWXCircle() {
        mWXHandler.setTargetUrl(mShareData.link);
        mWXHandler.setTitle(mShareData.getWXCircleContent());
        mWXHandler.setToCircle(true);
        mWXHandler.addToSocialSDK();
    }

    private void addQQZone() {
        // 添加QZone平台
        mQZoneSsoHandler.setTargetUrl(mShareData.link);
        mQZoneSsoHandler.addToSocialSDK();
    }

    private void addSinaWeibo() {
        mController.getConfig().setSsoHandler(mSinaSsoHandler);
        mController.getConfig().setSinaCallbackUrl("http://sns.whalecloud.com/mart-coding/phone/callback");
    }

    public static class ShareData implements Parcelable {
        private final String SHARE_MART_CONTENT = "#Coding 码市# 你有想法，我来实现；品质众包，可靠交付 http://codemart.com";
        private final String SHARE_MART_TITLE = "Coding 码市 - 品质众包，可靠交付";
        public String name = "";
        public String link = "";
        private String img = "";
        public String des = "";
        public boolean shareReward = false;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(name);
            dest.writeString(link);
            dest.writeString(img);
            dest.writeString(des);
        }

        private ShareData(Parcel in) {
            name = in.readString();
            link = in.readString();
            img = in.readString();
            des = in.readString();
        }

        public ShareData(Published data) {
            shareReward = true;
            this.name = data.name;
            this.link =  Global.generateRewardLink(data.id);

//            Global.MessageParse parse = HtmlContent.parseMaopao(mMaopaoObject.content);
            this.des = data.plainContent;
//            this.des = HtmlContent.parseToShareText(parse.text);
            String imageUrl = data.cover;
            if (!imageUrl.isEmpty()) {
                this.img = imageUrl;
            }
        }

        public ShareData(String link) {
            this.name = SHARE_MART_TITLE;
            this.link = link;
            this.img = "";
            this.des = SHARE_MART_CONTENT;
        }

        public String getSinaContent() {
            if (name.equals(SHARE_MART_TITLE) && des.equals(SHARE_MART_CONTENT)) {
                return "#coding 码市# 品质众包，可靠交付 " + link;
            }
            return String.format("#Coding 码市# 分享了一个项目《%s》%s", name, link);
        }

        public String getSystemShareContent() {
            return getSinaContent();
        }

        public String getWXCircleContent() {
            if (name.equals(SHARE_MART_TITLE)) {
                return name;
            }

            return String.format("%s - Coding 码市", name);
        }

        public ShareData() {
            this.name = SHARE_MART_TITLE;
            this.link = "https://codemart.com";
            this.img = "";
            this.des = SHARE_MART_CONTENT;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public static final Parcelable.Creator<ShareData> CREATOR = new Parcelable.Creator<ShareData>() {
            @Override
            public ShareData createFromParcel(Parcel in) {
                return new ShareData(in);
            }

            @Override
            public ShareData[] newArray(int size) {
                return new ShareData[size];
            }
        };
    }

    private void addButton(IconTextView.Data data) {
        IconTextView iconTextView = new IconTextView(mActivity, null);
        iconTextView.setData(data);
        iconTextView.setId(data.id);
        iconTextView.setOnClickListener(this);
        allButtonsLayout.addView(iconTextView);
        ViewGroup.LayoutParams lp = iconTextView.getLayoutParams();
        lp.width = LengthUtil.getsWidthPix() / 4;
        iconTextView.setLayoutParams(lp);
    }

    private final int ITEM_WEIXIN = 1;
    private final int ITEM_WEIXIN_CIRCLE = 2;
    private final int ITEM_QQ = 3;
    private final int ITEM_QZONE = 4;
    private final int ITEM_WEIBO = 5;
    private final int ITEM_CODING_FRIEND = 6;
    private final int ITEM_LINK_COPY = 7;
    private final int ITEM_SYSTEM_SHARE = 8;

    @SuppressWarnings("deprecation")
    private void initView(Context context) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.share_custom_board, null);
        final int[] buttns = new int[]{
                R.id.close,
                R.id.rootLayout,
                R.id.buttonsLayout
        };

        for (int id : buttns) {
            rootView.findViewById(id).setOnClickListener(this);
        }

        final IconTextView.Data[] datas;
        if (mShareType == ShareType.Job) {
            datas = new IconTextView.Data[]{
                    new IconTextView.Data(ITEM_WEIXIN, "微信好友", R.mipmap.icon_share_weixin),
                    new IconTextView.Data(ITEM_WEIXIN_CIRCLE, "朋友圈", R.mipmap.icon_share_weixin_friend),
                    new IconTextView.Data(ITEM_QQ, "QQ", R.mipmap.icon_share_qq),
                    new IconTextView.Data(ITEM_QZONE, "QQ空间", R.mipmap.icon_share_qq_zone),
                    new IconTextView.Data(ITEM_WEIBO, "微博", R.mipmap.icon_share_sina),
                    new IconTextView.Data(ITEM_CODING_FRIEND, "Coding冒泡", R.mipmap.icon_share_coding_friend),
                    new IconTextView.Data(ITEM_LINK_COPY, "复制链接", R.mipmap.icon_share_copy_link),
                    new IconTextView.Data(ITEM_SYSTEM_SHARE, "更多", R.mipmap.icon_share_copy_more),
            };
        } else {
            datas = new IconTextView.Data[]{
                    new IconTextView.Data(ITEM_WEIXIN, "微信好友", R.mipmap.icon_share_weixin),
                    new IconTextView.Data(ITEM_WEIXIN_CIRCLE, "朋友圈", R.mipmap.icon_share_weixin_friend),
                    new IconTextView.Data(ITEM_QQ, "QQ", R.mipmap.icon_share_qq),
                    new IconTextView.Data(ITEM_QZONE, "QQ空间", R.mipmap.icon_share_qq_zone),
                    new IconTextView.Data(ITEM_WEIBO, "微博", R.mipmap.icon_share_sina)
            };
        }

        allButtonsLayout = (ViewGroup) rootView.findViewById(R.id.allButtonsLayout);

        try {
            mWXHandler = new UMWXHandler(mActivity, AllThirdKeys.WX_APP_ID, AllThirdKeys.WX_APP_KEY);
            if (mWXHandler.isClientInstalled()) {
                addButton(datas[0]);
                addButton(datas[1]);
            }

            mQqSsoHandler = new UMQQSsoHandler(mActivity, AllThirdKeys.QQ_APP_ID, AllThirdKeys.QQ_APP_KEY);
            if (mQqSsoHandler.isClientInstalled()) {
                addButton(datas[2]);
            }


            mQZoneSsoHandler = new QZoneSsoHandler(mActivity, AllThirdKeys.QQ_APP_ID, AllThirdKeys.QQ_APP_KEY);
            if (mQZoneSsoHandler.isClientInstalled()) {
                addButton(datas[3]);
            }

            mSinaSsoHandler = new SinaSsoHandler();

            addButton(datas[4]);
            addButton(datas[5]);
            addButton(datas[6]);
            addButton(datas[7]);

        } catch (Exception e) {
            Global.errorLog(e);
        }

        setContentView(rootView);
        setWidth(LayoutParams.MATCH_PARENT);
        setHeight(LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        ColorDrawable cd = new ColorDrawable(0xb0000000);
        setBackgroundDrawable(cd);
        setTouchable(true);

        setWindowStatusBarColor(R.color.black);

        mBackground = rootView.findViewById(R.id.rootLayout);
        mBackground.startAnimation(AnimationUtils.loadAnimation(mActivity,
                R.anim.share_dialog_bg_enter));

        mButtonsLayout = rootView.findViewById(R.id.buttonsLayout);
        mButtonsLayout.startAnimation(AnimationUtils.loadAnimation(mActivity,
                R.anim.share_dialog_buttons_layout_enter));
    }


    public void setWindowStatusBarColor(int colorResId) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = mActivity.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(mActivity.getResources().getColor(colorResId));

                //底部导航栏
                //window.setNavigationBarColor(activity.getResources().getColor(colorResId));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dismiss() {
        mBackground.startAnimation(AnimationUtils.loadAnimation(mActivity,
                R.anim.share_dialog_bg_exit));
        mButtonsLayout.startAnimation(AnimationUtils.loadAnimation(mActivity,
                R.anim.share_dialog_buttons_layout_exit));
        setWindowStatusBarColor(R.color.window_bar_background);
        super.dismiss();
    }

    private void umengEvent(String s, String param) {
        MobclickAgent.onEvent(mActivity, s, param);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case ITEM_WEIXIN:
                if (mShareData.shareReward) {
                    umengEvent(UmengEvent.ACTION, "项目详情 _ 更多 _ 分享到微信好友");
                }
                addWX();
                performShare(SHARE_MEDIA.WEIXIN);
                break;
            case ITEM_WEIXIN_CIRCLE:
                if (mShareData.shareReward) {
                    umengEvent(UmengEvent.ACTION, "项目详情 _ 更多 _ 分享到朋友圈");
                }
                addWXCircle();
                performShare(SHARE_MEDIA.WEIXIN_CIRCLE);
                break;
            case ITEM_QQ:
                if (mShareData.shareReward) {
                    umengEvent(UmengEvent.ACTION, "项目详情 _ 更多 _ 分享到QQ好友");
                }
                addQQ();
                performShare(SHARE_MEDIA.QQ);
                break;
            case ITEM_QZONE:
                if (mShareData.shareReward) {
                    umengEvent(UmengEvent.ACTION, "项目详情 _ 更多 _ 分享到QQ 空间");
                }
                addQQZone();
                performShare(SHARE_MEDIA.QZONE);
                break;

            case ITEM_SYSTEM_SHARE:

                if (mShareData.shareReward) {
                    umengEvent(UmengEvent.ACTION, "项目详情 _ 更多 _ 系统分享");
                }

                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, mShareData.getSystemShareContent());
                shareIntent.setType("text/plain");
                mActivity.startActivity(Intent.createChooser(shareIntent, "分享项目"));

                break;

            case ITEM_WEIBO:

                if (mShareData.shareReward) {
                    umengEvent(UmengEvent.ACTION, "项目详情 _ 更多 _ 微博");
                }
                // 未登录的情况下，需要在 Activity 的 onActivityResult 里面判断登录是否成功，所以我在 ShareSinaHelpActivity 做了这个事情
                if (!OauthHelper.isAuthenticatedAndTokenNotExpired(mActivity, SHARE_MEDIA.SINA)) {
                    Intent intent = new Intent(mActivity, ShareSinaHelpActivity.class);
                    intent.putExtra(ShareSinaHelpActivity.EXTRA_SHARE_DATA, mShareData);
                    mActivity.startActivity(intent);
                } else {
                    addSinaWeibo();
                    performShare(SHARE_MEDIA.SINA);
                }
                break;

            case ITEM_CODING_FRIEND:

                if (mShareData.shareReward) {
                    umengEvent(UmengEvent.ACTION, "项目详情 _ 更多 _ 分享到 Coding 冒泡");
                }

                if (DeviceConfig.isAppInstalled("net.coding.program", mActivity)) {
                    Intent codingIntent = new Intent();
                    codingIntent.setAction("android.intent.action.SEND.net.coding.program");
                    codingIntent.putExtra(Intent.EXTRA_TEXT, mShareData.getSystemShareContent());
                    codingIntent.putExtra(Intent.EXTRA_STREAM, mShareData.getImg());
                    codingIntent.setType("image/*");
                    mActivity.startActivity(Intent.createChooser(codingIntent, "分享项目"));
                } else {
                    new AlertDialog.Builder(mActivity)
                            .setTitle("未安装Coding")
                            .setMessage("去本地市场安装？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        Uri uri = Uri.parse("market://details?id=net.coding.program");
                                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        mActivity.startActivity(intent);
                                    } catch (Exception e) {
                                        Toast.makeText(mActivity, "软件市场里暂时没有找到码市", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .setNegativeButton("取消", null)
                            .show();
                }
                break;

            case ITEM_LINK_COPY:
                if (mShareData.shareReward) {
                    umengEvent(UmengEvent.ACTION, "项目详情 _ 更多 _ 复制链接");
                }

                Global.copy(mActivity, mShareData.link);
                Toast.makeText(mActivity, "链接已复制 " + mShareData.link, Toast.LENGTH_SHORT).show();
                break;

            case R.id.buttonsLayout:
                return;

            default:
                break;
        }
        dismiss();
    }

    private void performShare(SHARE_MEDIA platform) {
        performShare(platform, mActivity, mShareData);
    }

    public static void performShare(SHARE_MEDIA platform, Activity mActivity, ShareData mShareData) {
        mController.setShareContent(mShareData.des);

        UMImage umImage;
        if (!mShareData.getImg().isEmpty()) {
            umImage = new UMImage(mActivity, mShareData.getImg());
        } else {
            umImage = new UMImage(mActivity, "https://dn-coding-net-production-static.qbox.me/ic_launcher.png");
        }
        mController.setShareImage(umImage);

        if (platform == SHARE_MEDIA.SINA) {

//            CustomShareBoard.getShareController().getConfig().setSsoHandler(new SinaSsoHandler());

            SinaShareContent sinaContent = new SinaShareContent();
            sinaContent.setShareContent(mShareData.getSinaContent());
            sinaContent.setTargetUrl(mShareData.link);
            sinaContent.setShareImage(umImage);
            sinaContent.setTitle(mShareData.name);
            mController.setShareMedia(sinaContent);
        } else if (platform == SHARE_MEDIA.QZONE) {
            QZoneShareContent qzone = new QZoneShareContent();
            qzone.setShareContent(mShareData.des);
            qzone.setTitle(mShareData.name);
            qzone.setShareImage(umImage);
            mController.setShareMedia(qzone);
        }

        mController.postShare(mActivity, platform, new SocializeListeners.SnsPostListener() {

                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {
                    }
                }
        );
    }

}
