package net.coding.mart.json.user.exam;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by chenchao on 16/10/27.
 */

public class Option implements Serializable {

    private static final long serialVersionUID = -8179883405547545323L;

    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName(value = "question_id", alternate = {"questionId"})
    @Expose
    public int questionId;
    @SerializedName("mark")
    @Expose
    public String mark;
    @SerializedName("content")
    @Expose
    public String content;
    @SerializedName("correct")
    @Expose
    public int correct;
    @SerializedName("answered")
    @Expose
    public int answered;
    @SerializedName("sort")
    @Expose
    public int sort;
    @SerializedName("hit")
    @Expose
    public int hit;
    @SerializedName(value = "created_at", alternate = {"createdAt"})
    @Expose
    public String createdAt;
    @SerializedName(value = "updated_at", alternate = {"updatedAt"})
    @Expose
    public String updatedAt;

    public boolean pickError = false;
    public boolean loatError = false; // 与 pickError 一样，但是不会改变

    /* 一共4种显示情况，
            选了  未选
    需要选   蓝   普通
    不需要   红   普通
     */
    public boolean getAnswered() {
        return answered == 1;
    }

    public boolean answerCorrect() {
        return answered == correct;
    }

    public boolean isCorrect() {
        return correct == 1;
    }

}
