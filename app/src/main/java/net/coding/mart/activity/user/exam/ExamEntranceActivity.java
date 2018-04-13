package net.coding.mart.activity.user.exam;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import net.coding.mart.R;
import net.coding.mart.WebActivity_;
import net.coding.mart.common.BackActivity;
import net.coding.mart.json.user.exam.Exam;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_exam_entrance)
public class ExamEntranceActivity extends BackActivity {

    @ViewById
    TextView content;

    @Extra
    Exam exam;

    @AfterViews
    void initExamEntranceActivity() {
        SpannableStringBuilder builder = new SpannableStringBuilder(getResources().getText(R.string.examination_begin_tips));
        ForegroundColorSpan greenSpan = new ForegroundColorSpan(getResources().getColor(R.color.font_green));
        builder.setSpan(greenSpan, 6, 15, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        content.setText(builder);
    }

    @Click
    void startExam() {
        ExamActivity_.intent(this).exam(exam).start();
        finish();
    }

    @Click
    void clickProtocol() {
        WebActivity_.intent(this)
                .url("https://dn-coding-net-production-static.qbox.me/Developer_Service_Guide_v1.html")
                .mTitle("开发者服务指南")
                .start();
    }

}
