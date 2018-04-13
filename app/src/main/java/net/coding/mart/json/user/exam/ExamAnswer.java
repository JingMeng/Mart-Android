package net.coding.mart.json.user.exam;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenchao on 16/10/27.
 */

public class ExamAnswer implements Serializable {

    private static final long serialVersionUID = 6262997173358235165L;


    @SerializedName("score")
    @Expose
    public Score score;

    @SerializedName("vos")
    @Expose
    public List<Question> questions = new ArrayList<>();
}
