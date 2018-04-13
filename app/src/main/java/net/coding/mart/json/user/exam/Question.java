package net.coding.mart.json.user.exam;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenchao on 16/10/27.
 */

public class Question implements Serializable {

    private static final long serialVersionUID = -5940476262446028151L;

    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("question")
    @Expose
    public String question;
    @SerializedName("options")
    @Expose
    public List<Option> options = new ArrayList<>();
    @SerializedName(value = "options_id", alternate = {"optionsId"})
    @Expose
    public List<Integer> optionsId = new ArrayList<>();
    @SerializedName("corrects")
    @Expose
    public List<Integer> corrects = new ArrayList<>();
    @SerializedName(value = "corrects_mark", alternate = {"correctsMark"})
    @Expose
    public List<String> correctsMark = new ArrayList<>();
    @SerializedName("mark")
    @Expose
    public String mark;
    @SerializedName("type")
    @Expose
    public int type;
    @SerializedName("wrong")
    @Expose
    public int wrong;
    @SerializedName("sort")
    @Expose
    public int sort;
    @SerializedName(value = "created_at", alternate = {"createdAt"})
    @Expose
    public String createdAt;

    public boolean lastError = false;

    public boolean isSinglePick() {
        return type == 0;
    }

    public boolean isAnswerCorrect() {
        for (Option option : options) {
            if (!option.answerCorrect()) {
                return false;
            }
        }

        return true;
    }
}
