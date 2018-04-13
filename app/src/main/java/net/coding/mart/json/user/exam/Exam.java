package net.coding.mart.json.user.exam;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenchao on 16/10/27.
 */

public class Exam implements Serializable {

    private static final long serialVersionUID = -6850130532889418114L;
    @SerializedName("score")
    @Expose
    public Score score;
    @SerializedName("questions")
    @Expose
    public List<Question> questions = new ArrayList<>();
    private Type examType = Type.Examing; // 是否考试状态

    public Type isExaming() {
        return examType;
    }

    public void setExaming(Type examing) {
        this.examType = examing;
    }

    // 是否第一次做题
    public boolean isFirstExam() {
        return score == null;
    }

    public boolean isPassed() {
        return score != null && score.total == score.correct;
    }

    public void setErrorDisplay() {
        for (Question item : questions) {
            boolean answerError = false;
            for (Option option : item.options) {
                option.pickError = option.correct == 0 && option.answered == 1;

                if (!option.answerCorrect()) {
                    answerError = true;
                }
            }

            if (isFirstExam()) {
                answerError = false;
            }

            item.lastError = answerError;
        }
    }

    public enum Type {
        Passed,
        Examing,
        Score
    }
}
