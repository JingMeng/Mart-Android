package net.coding.mart.json.reward;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import net.coding.mart.json.Pager;

import java.io.Serializable;

/**
 * Created by chenchao on 16/7/29.
 */
public class RewardWapper<T> implements Serializable {

    private static final long serialVersionUID = -8267345229836938180L;

    @SerializedName("rewards")
    @Expose
    public Pager<T> publishedPage;
}
