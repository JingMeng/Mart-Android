package net.coding.mart.json;

import net.coding.mart.common.constant.RewardType;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenchao on 16/3/3.
 */
public class Example implements Serializable {

    private static final long serialVersionUID = -475813335929125606L;
    private int reward_id;
    private String title = "";
    private int amount;
    private int duration;
    private String character = "";
    private int type_id;
    private String type = "";
    private String logo = "";
    private String thinking = "";
    private String intro = "";
    private String wechat = "";
    private String color = "";
    private int image_width;
    private List<String> images = new ArrayList<>();
    private List<String> inner_link = new ArrayList<>();

    public Example(JSONObject json) {
        reward_id = json.optInt("reward_id");
        title = json.optString("title", "");
        amount = json.optInt("amount");
        duration = json.optInt("duration");
        character = json.optString("character", "");
        type_id = json.optInt("type_id");
        type = json.optString("type", "");
        logo = json.optString("logo", "");
        thinking = json.optString("thinking", "");
        intro = json.optString("intro", "");
        wechat = json.optString("wechat", "");
        color = json.optString("color", "");
        image_width = json.optInt("image_width");
        JSONArray jsonImages = json.optJSONArray("images");
        if (jsonImages != null) {
            for (int i = 0; i < jsonImages.length(); ++i) {
                images.add(jsonImages.optString(i));
            }
        }

        JSONArray jsonLink = json.optJSONArray("inner_link");
        if (jsonLink != null) {
            for (int i = 0; i < jsonLink.length(); ++i) {
                inner_link.add(jsonLink.optString(i));
            }
        }
    }

    public void setReward_id(int reward_id) {
        this.reward_id = reward_id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public void setThinking(String thinking) {
        this.thinking = thinking;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setImage_width(int image_width) {
        this.image_width = image_width;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public void setInner_link(List<String> inner_link) {
        this.inner_link = inner_link;
    }

    public int getReward_id() {
        return reward_id;
    }

    public String getTitle() {
        return title;
    }

    public int getAmount() {
        return amount;
    }

    public int getDuration() {
        return duration;
    }

    public String getCharacter() {
        return character;
    }

    public String getTypeString() {
        // 因为 probuffer 使用 0 的时候不输出，所以码市案例的 web 类型在这里使用了 1, 但其它地方都是 0，一颗雷啊
        int typeIdCompatible = type_id;
        if (typeIdCompatible == 1) {
            typeIdCompatible = 0;
        }
        return RewardType.idToName(typeIdCompatible);
    }

    public int getType_id() {
        return type_id;
    }

    public String getType() {
        return type;
    }

    public String getLogo() {
        return logo;
    }

    public String getThinking() {
        return thinking;
    }

    public String getIntro() {
        return intro;
    }

    public String getWechat() {
        return wechat;
    }

    public String getColor() {
        return color;
    }

    public int getImage_width() {
        return image_width;
    }

    public List<String> getImages() {
        return images;
    }

    public List<String> getInner_link() {
        return inner_link;
    }
}
