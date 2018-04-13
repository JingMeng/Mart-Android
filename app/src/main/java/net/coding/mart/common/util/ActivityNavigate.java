package net.coding.mart.common.util;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import net.coding.mart.R;
import net.coding.mart.WebActivity_;
import net.coding.mart.job.JobDetailActivity;
import net.coding.mart.job.JobDetailActivity_;

public class ActivityNavigate {

    public static void startJobDetail(Fragment context, String url) {
        Intent resultIntent = new Intent(context.getActivity(), JobDetailActivity_.class);
        resultIntent.putExtra(JobDetailActivity.EXTRA_URL, url);
        context.startActivity(resultIntent);
    }

    public static void startPublishAgreement(Context context) {
        String title = context.getString(R.string.title_activity_mart_issue);
        WebActivity_.intent(context)
                .mTitle(title)
                .url("https://codemart.com/terms.html")
                .start();
    }

    public static void startServiceTerm(Context context) {
        String title = context.getString(R.string.title_activity_terms);
        WebActivity_.intent(context)
                .mTitle(title)
                .url("https://codemart.com/agreement")
                .start();

    }
}
