package net.coding.mart.activity.user.exam;

import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import net.coding.mart.R;
import net.coding.mart.common.BackActivity;
import net.coding.mart.json.BaseObserver;
import net.coding.mart.json.Network;
import net.coding.mart.json.user.exam.Exam;
import net.coding.mart.json.user.exam.ExamAnswer;
import net.coding.mart.json.user.exam.Option;
import net.coding.mart.json.user.exam.Question;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@EActivity(R.layout.activity_exam)
public class ExamActivity extends BackActivity {

    @Extra
    Exam exam;

    @ViewById
    ListView listView;

    View submitLayout;
    TextView sendButton;

    private HeaderHelp headerHelp;

    @AfterViews
    void initExamActivity() {
        if (exam.isPassed()) {
            exam.setExaming(Exam.Type.Passed);
        } else if (exam.isFirstExam()) {
            exam.setExaming(Exam.Type.Examing);
        } else {
            exam.setExaming(Exam.Type.Score);
        }
        exam.setErrorDisplay();

        View headerView = getLayoutInflater().inflate(R.layout.exam_list_header, listView, false);
        View footerView = getLayoutInflater().inflate(R.layout.exam_list_footer, listView, false);

        submitLayout = footerView.findViewById(R.id.submitLayout);
        sendButton = (TextView) footerView.findViewById(R.id.sendButton);
        sendButton.setOnClickListener(v -> sendButton());

        listView.addHeaderView(headerView, null, false);
        listView.addFooterView(footerView, null, false);

        headerHelp = new HeaderHelp(headerView);

        listView.setAdapter(adapter);

        initHeader();
    }

    private void initHeader() {
        if (exam.isPassed()) {
            submitLayout.setVisibility(View.GONE);
            headerHelp.showPassed();

        } else {
            submitLayout.setVisibility(View.VISIBLE);

            if (exam.isFirstExam()) {
                sendButton.setText("提交答案");
                headerHelp.hideAll();
            } else {
                sendButton.setText("重新答题");
                int errorCount = getErrorAnswerCount();
                headerHelp.showError(errorCount);
            }
        }
    }

    @Click
    void sendButton() {
        if (exam.isExaming() == Exam.Type.Examing) {
            Map<Integer, HashSet<Integer>> answer = new HashMap<>();

            int noCompleteIndex = -1;
            for (int i = 0; i < exam.questions.size(); ++i) {
                Question item = exam.questions.get(i);
                HashSet<Integer> set = new HashSet<>();
                for (Option option : item.options) {
                    if (option.getAnswered()) {
                        set.add(option.id);
                    }
                }

                if (!set.isEmpty()) {
                    answer.put(item.id, set);
                } else {
                    if (noCompleteIndex == -1) {
                        noCompleteIndex = i;
                    }
                }
            }

            final int index = noCompleteIndex;
            int questionCount = exam.questions.size();
            if (answer.size() < questionCount) {
                new AlertDialog.Builder(this)
                        .setTitle("提交失败")
                        .setMessage("您还有 " + (questionCount - answer.size()) + " 道题目未选择答案")
                        .setNegativeButton("继续答题", ((dialog, which) ->
                                listView.smoothScrollToPosition(index + listView.getHeaderViewsCount())))
                        .show();
                return;
            }

            showSending(true);

            listView.smoothScrollToPosition(0);

            Map<String, Map> answerParam = new HashMap<>();
            answerParam.put("answers", answer);

            Network.getRetrofit(this)
                    .postExam(answerParam)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseObserver<ExamAnswer>(this) {
                        @Override
                        public void onSuccess(ExamAnswer data) {
                            super.onSuccess(data);
                            showSending(false);

                            Exam.Type examing = exam.isExaming();
                            exam.questions = data.questions;
                            exam.score = data.score;

                            if (exam.isPassed()) {
                                exam.setExaming(Exam.Type.Passed);
                                submitLayout.setVisibility(View.GONE);
                                headerHelp.showPassed();

                            } else {
                                if (examing == Exam.Type.Examing) {
                                    exam.setExaming(Exam.Type.Score);
                                    sendButton.setText("重新答题");

                                    int errorCount = getErrorAnswerCount();
                                    headerHelp.showError(errorCount);
                                    exam.setErrorDisplay();

                                } else {
                                    exam.setExaming(Exam.Type.Examing);
                                    sendButton.setText("提交答案");
                                    headerHelp.hideAll();
                                }
                            }

                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onFail(int errorCode, @NonNull String error) {
                            super.onFail(errorCode, error);
                            showSending(false);
                        }
                    });
        } else {
            exam.setExaming(Exam.Type.Examing);
            sendButton.setText("提交答案");
            headerHelp.hideAll();

            adapter.notifyDataSetChanged();

            listView.smoothScrollToPosition(0);
            showMiddleToast("开始答题");
        }
    }

    private int getErrorAnswerCount() {
        int errorCount = 0;
        for (Question item : exam.questions) {
            if (!item.isAnswerCorrect()) {
                ++errorCount;
            }
        }
        return errorCount;
    }

    BaseAdapter adapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return exam.questions.size();
        }

        @Override
        public Question getItem(int position) {
            return exam.questions.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean isEnabled(int position) {
            return false;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ExamListItemHolder holder;
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.exam_list_item, parent, false);
                holder = new ExamListItemHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ExamListItemHolder) convertView.getTag();
            }

            holder.bind(getItem(position), exam.isExaming(), clickOption);

            return convertView;
        }
    };

    View.OnClickListener clickOption = v -> {
        Option option = (Option) v.getTag();
        Question question = (Question) v.getTag(R.layout.exam_list_item);

        if (option.pickError) {
            option.pickError = false;
            option.answered = 0;
        } else if (option.getAnswered()) {
            option.answered = 0;
        } else {
            option.answered = 1;
        }

        for (Option item : question.options) {
            if (item != option) {
                item.pickError = false;
                if (question.isSinglePick()) {
                    item.answered = 0;
                }
            }
        }

        adapter.notifyDataSetChanged();
    };

    private static class HeaderHelp {
        TextView passedTip;
        TextView errorTip;

        HeaderHelp(View v) {
            passedTip = (TextView) v.findViewById(R.id.passedTip);
            errorTip = (TextView) v.findViewById(R.id.errorTip);
        }

        void showPassed() {
            passedTip.setVisibility(View.VISIBLE);
            errorTip.setVisibility(View.GONE);
        }

        void hideAll() {
            passedTip.setVisibility(View.GONE);
            errorTip.setVisibility(View.GONE);
        }

        void showError(int errorCount) {
            String msg = String.format("您有%s个问题回答错误，请找到回答错误的问题并查看正确答案，然后重新答题。",
                    errorCount);
            passedTip.setVisibility(View.GONE);
            errorTip.setVisibility(View.VISIBLE);
            errorTip.setText(msg);
        }
    }
}
