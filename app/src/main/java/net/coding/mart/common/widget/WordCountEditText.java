package net.coding.mart.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import net.coding.mart.R;

/**
 * Created by chenchao on 16/9/30.
 * 统计用户输入的字数
 */

public class WordCountEditText extends FrameLayout {

    TextView wordCountTip;
    EditText editTextCotent;
    int minCount;
    int maxCount;
    String hint;

    public WordCountEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        inflate(context, R.layout.edit_text_word_count, this);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.WordCountEditText, 0, 0);
        try {
            minCount = a.getInt(R.styleable.WordCountEditText_wordCountMin, 0);
            maxCount = a.getInt(R.styleable.WordCountEditText_wordCountMax, Integer.MAX_VALUE);
            hint = a.getString(R.styleable.WordCountEditText_wordCountHint);
            if (hint == null) {
                hint = "";
            }
        } finally {
            a.recycle();
        }

        wordCountTip = (TextView) findViewById(R.id.wordCountTip);
        editTextCotent = (EditText) findViewById(R.id.editTextCotent);
        editTextCotent.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                int len = s.length();
                if (len < minCount) {
                    wordCountTip.setText(Html.fromHtml(String.format("还差 <font color='#E0354D'>%s</font> 字", minCount - len)));
                } else if (maxCount < len) {
                    wordCountTip.setText(Html.fromHtml(String.format("已超出 <font color='#E0354D'>%s</font> 字", len - maxCount)));
                } else {
                    wordCountTip.setText(Html.fromHtml(String.format("已输入 %s 字", len)));
                }
            }
        });
        editTextCotent.setText(editTextCotent.getText());
        editTextCotent.setHint(hint);

        setOnClickListener(v -> {
            editTextCotent.performClick();
        });

//        editTextCotent.setOnTouchListener((view, event) -> {
//            if (view == editTextCotent) {
//                view.getParent().requestDisallowInterceptTouchEvent(true);
//                switch (event.getAction()& MotionEvent.ACTION_MASK){
//                    case MotionEvent.ACTION_UP:
//                        view.getParent().requestDisallowInterceptTouchEvent(false);
//                        break;
//                }
//            }
//            return false;
//        });

    }

    public void addTextChangedListener(TextWatcher watcher) {
        editTextCotent.addTextChangedListener(watcher);
    }

    public EditText getEditText() {
        return editTextCotent;
    }

    public String getText() {
        return editTextCotent.getText().toString();
    }

    public void setText(String s) {
        editTextCotent.setText(s);
    }

}
