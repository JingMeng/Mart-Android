package net.coding.mart.json.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import net.coding.mart.json.BaseHttpResult;

import java.io.Serializable;

/**
 * Created by chenchao on 16/4/1.
 * 验证码图片
 */
public class Captcha extends BaseHttpResult implements Serializable {

    private static final long serialVersionUID = 8778104201637275326L;

    @SerializedName("image")
    @Expose
    public String image = "";
}
