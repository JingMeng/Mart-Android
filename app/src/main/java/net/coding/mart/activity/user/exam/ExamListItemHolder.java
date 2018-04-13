package net.coding.mart.activity.user.exam;

import android.view.View;
import android.widget.TextView;

import net.coding.mart.R;
import net.coding.mart.common.Color;
import net.coding.mart.json.user.exam.Exam;
import net.coding.mart.json.user.exam.Option;
import net.coding.mart.json.user.exam.Question;

public class ExamListItemHolder {

    private TextView question;
    private TextView answer;

    private OptionHolder[] items;

    public ExamListItemHolder(View view) {
        question = (TextView) view.findViewById(R.id.question);
        OptionHolder itemA = new OptionHolder(view.findViewById(R.id.itemA));
        OptionHolder itemB = new OptionHolder(view.findViewById(R.id.itemB));
        OptionHolder itemC = new OptionHolder(view.findViewById(R.id.itemC));
        OptionHolder itemD = new OptionHolder(view.findViewById(R.id.itemD));

        answer = (TextView) view.findViewById(R.id.answer);
        items = new OptionHolder[]{
                itemA, itemB, itemC, itemD
        };
    }

    public void bind(Question data, Exam.Type examType, View.OnClickListener clickOption) {
        question.setText(String.format("%s.%s", data.sort, data.question));

        for (int i = 0; i < items.length && i < data.options.size(); ++i) {
            Option option = data.options.get(i);
            OptionHolder item = items[i];
            item.bind(option);

            if (examType == Exam.Type.Examing) {
                item.rootLayout.setOnClickListener(clickOption);
                item.rootLayout.setTag(option);
                item.rootLayout.setTag(R.layout.exam_list_item, data);
            } else {
                item.rootLayout.setOnClickListener(null);
            }
        }

        if (examType == Exam.Type.Examing
                || examType == Exam.Type.Passed) {
            answer.setVisibility(View.GONE);
        } else {
            answer.setVisibility(View.VISIBLE);

            StringBuffer sb = new StringBuffer();
            boolean isFirst = true;
            for (String item : data.correctsMark) {
                if (isFirst) {
                    isFirst = false;
                } else {
                    sb.append(",");
                }
                sb.append(item);
            }

            answer.setText(String.format("正确答案：%s", sb.toString()));
        }

        if ((examType == Exam.Type.Examing || examType == Exam.Type.Score)
                && data.lastError) {
            question.setTextColor(Color.font_red);
        } else {
            question.setTextColor(Color.font_2);
        }
    }
    private static class OptionHolder {
        View rootLayout;
        TextView mark;
        TextView content;

        OptionHolder(View view) {
            rootLayout = view;
            mark = (TextView) view.findViewById(R.id.optionMark);
            content = (TextView) view.findViewById(R.id.optoinContent);
        }

        void bind(Option data) {
            mark.setText(String.format("%s. ", data.mark));
            content.setText(data.content);

            if (data.pickError) {
                rootLayout.setBackgroundResource(R.drawable.shape_red);
                setTextColor(Color.white);
            } else if (data.getAnswered()){
                rootLayout.setBackgroundResource(R.drawable.shape_light_blue);
                setTextColor(Color.font_2);
            } else {
                rootLayout.setBackgroundResource(R.drawable.shape_gray_border);
                setTextColor(Color.font_2);
            }

//            if (data.getAnswered()) {
//                if (examType == Exam.Type.Passed) {
//                    rootLayout.setBackgroundResource(R.drawable.shape_light_blue);
//                    setTextColor(Color.font_2);
//                } else {
//                    if (!data.isCorrect()) {
//                        rootLayout.setBackgroundResource(R.drawable.shape_red);
//                        setTextColor(Color.white);
//                    } else {
//                        rootLayout.setBackgroundResource(R.drawable.shape_light_blue);
//                        setTextColor(Color.font_2);
//                    }
//                }
//            } else {
//            }
        }

        private void setTextColor(int color) {
            mark.setTextColor(color);
            content.setTextColor(color);
        }
    }
}
