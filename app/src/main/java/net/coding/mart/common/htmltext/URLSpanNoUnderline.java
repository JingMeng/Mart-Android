package net.coding.mart.common.htmltext;

import android.content.Context;
import android.content.Intent;
import android.text.TextPaint;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.Toast;

import net.coding.mart.WebActivity;
import net.coding.mart.WebActivity_;
import net.coding.mart.activity.reward.PublishRewardActivity_;
import net.coding.mart.activity.user.UserMainActivity_;
import net.coding.mart.common.Global;

/**
 * Created by chaochen on 15/1/12.
 * 用来解析 url 以跳转到不同的界面
 */
public class URLSpanNoUnderline extends URLSpan {

    private int color;

    public URLSpanNoUnderline(String url, int color) {
        super(url);
        this.color = color;
    }

    public static void openActivityByUri(Context context, String uriString, boolean newTask) {
        openActivityByUri(context, uriString, newTask, true);
    }


    public static boolean openActivityByUri(Context context, String uriString, boolean newTask, boolean defaultIntent) {
        return openActivityByUri(context, uriString, newTask, defaultIntent, false);
    }

    public static boolean openActivityByUri(Context context, String uriString, boolean newTask, boolean defaultIntent, boolean share) {
        Intent intent = new Intent();
        if (newTask) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        if (uriString.startsWith("/userinfo")) {
            UserMainActivity_.intent(context).start();
            return true;
        }

//                data.link = "https://codemart.com/publish#question-1";
        String publishUrl = String.format("%s/publish", Global.HOST);
        if (uriString.startsWith(publishUrl)) {
            PublishRewardActivity_.intent(context).start();
            return true;
        }

        if (uriString.startsWith("/")) {
            String url = Global.HOST + uriString;
            WebActivity_.intent(context).url(url).start();
            return true;
        }

//        final String uriPath = uriString.replace(Global.HOST, "");
//
//        final String projectPattern = String.format("^?/reward/(\\w*)$");
//        Pattern pattern = Pattern.compile(projectPattern);
//        Matcher matcher = pattern.matcher(uriPath);
//        if (matcher.find()) {
//            String url = Global.HOST + uriPath;
//            WebActivity_.intent(context).url(url).start();
//            return true;
//        }
//        if (matcher.find()) {
//            String user = matcher.group(1);
//            String project = matcher.group(2);
//            String simplePath = matcher.group(3); // 去除了 /u/*/p/* 的路径
//            final String projectPath = String.format("/user/%s/project/%s", user, project);
//
//            // 代码中的文件 https://coding.net/u/8206503/p/TestPrivate/git/blob/master/jumpto
//            final String gitFile = String.format("^/git/blob/%s/(.*)$", NAME);
//            pattern = Pattern.compile(gitFile);
//            matcher = pattern.matcher(simplePath);
//            if (matcher.find()) {
//                String version = matcher.group(1);
//                String path = matcher.group(2);
//
//                intent.setClass(context, GitViewActivity_.class);
//                intent.putExtra("mProjectPath", projectPath);
//                intent.putExtra("mVersion", version);
//                intent.putExtra("mGitFileInfoObject", new GitFileInfoObject(path));
//                context.startActivity(intent);
//                return true;
//            }
//        }
//
//        // 用户名
//        final String atSomeOne = "^(?:https://[\\w.]*)?/u/([\\w.-]+)$";
//        pattern = Pattern.compile(atSomeOne);
//        matcher = pattern.matcher(uriString);
//        if (matcher.find()) {
//            String global = matcher.group(1);
//            intent.setClass(context, UserDetailActivity_.class);
//            intent.putExtra("globalKey", global);
//            context.startActivity(intent);
//            return true;
//        }
//
//        // 项目讨论列表
//        // https://coding.net/u/8206503/p/TestIt2/topic/mine
//        final String topicList = "^(?:https://[\\w.]*)?/u/([\\w.-]+)/p/([\\w.-]+)/topic/(mine|all)$";
//        pattern = Pattern.compile(topicList);
//        matcher = pattern.matcher(uriString);
//        if (matcher.find()) {
//            intent.setClass(context, ProjectActivity_.class);
//            ProjectActivity.ProjectJumpParam param = new ProjectActivity.ProjectJumpParam(
//                    matcher.group(1), matcher.group(2)
//            );
//            intent.putExtra("mJumpParam", param);
//            intent.putExtra("mJumpType", ProjectActivity.ProjectJumpParam.JumpType.typeTopic);
//            context.startActivity(intent);
//            return true;
//        }

        try {
            if (defaultIntent) {
                intent = new Intent(context, WebActivity_.class);

                if (newTask) {
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                if (uriString.startsWith("/u/")) {
                    uriString = Global.HOST + uriString;
                }

                if (share) {
                    intent.putExtra("share", true);
                }

                intent.putExtra(WebActivity.EXTRA_URL, uriString);
                context.startActivity(intent);
            }
        } catch (Exception e) {
            Toast.makeText(context, "" + uriString, Toast.LENGTH_LONG).show();
            Global.errorLog(e);
        }

        return false;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setUnderlineText(false);
        ds.setColor(color);
    }

    @Override
    public void onClick(View widget) {
        openActivityByUri(widget.getContext(), getURL(), false);
    }
}
