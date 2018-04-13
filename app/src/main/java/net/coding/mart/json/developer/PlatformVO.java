package net.coding.mart.json.developer;

import java.util.List;

/**
 * Created by liu on 16/6/21.
 */
public class PlatformVO {
    private String platform;
    private List<Object> category;
    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public List<Object> getCategory() {
        return category;
    }

    public void setCategory(List<Object> category) {
        this.category = category;
    }
}
