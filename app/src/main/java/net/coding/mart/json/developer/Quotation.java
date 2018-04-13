package net.coding.mart.json.developer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenchao on 16/5/27.
 */
public class Quotation implements Serializable {

    private static final long serialVersionUID = 8320064692680599780L;
    @SerializedName("code")
    @Expose
    public String code = "";
    @SerializedName("title")
    @Expose
    public String title = "";
    @SerializedName("description")
    @Expose
    public String description = "";
    @SerializedName("price")
    @Expose
    public BigDecimal price;
    @SerializedName("type")
    @Expose
    public int type; // 层级类型 1 平台 platform 2 分类 category 3 模块 module 4 功能 function
    @SerializedName("children")
    @Expose
    public String children = "";
    @SerializedName(value = "created_at", alternate = {"createdAt"})
    @Expose
    public String createdAt = "";
    @SerializedName(value = "deleted_at", alternate = {"deletedAt"})
    @Expose
    public String deletedAt = "";
    @SerializedName(value = "updated_at", alternate = {"updatedAt"})
    @Expose
    public String updatedAt = "";
    @SerializedName(value = "dom_type", alternate = {"domType"})
    @Expose
    public int domType; // 0 无 1 checkbox 2 radioGroup
    @SerializedName(value = "value_type", alternate = {"valueType"})
    @Expose
    public int valueType;
    @SerializedName(value = "is_default", alternate = {"isDefault"})
    @Expose
    public int isDefault; // 是否默认选项 1 是 0 否
    @SerializedName("id")
    @Expose
    public int id;

    public String platformCode = "";
    public String mainCode = "";
    public String categoryCode = "";

    public boolean isDefault() {
        return isDefault == 1;
    }

    private List<Quotation> extra; // 用来记录单选按钮组

    public List<Quotation> getExtra() {
        if (extra == null) {
            extra = new ArrayList<>(0);
        }
        return extra;
    }

    public void resetExtra() {
        if (extra == null) {
            extra = new ArrayList<>();
        } else {
            extra.clear();
        }
    }

    public void resetPickedFunctions() {
        if (extra == null) {
            extra = new ArrayList<>();
        }

        boolean containFrontPage = extra.contains(getFrontPageFunction());
        extra.clear();
        if (containFrontPage) {
            extra.add(getFrontPageFunction());
        }
    }

    public void addExtra(Quotation item) {
        extra.add(item);
    }

    public void setExtra(List<Quotation> extra) {
        if (extra == null) {
            extra = new ArrayList<>(0);
        }
        this.extra = extra;
    }

    public boolean isRadioGroup() {
        return domType == 2;
    }

    public boolean isRadioItem() {
        return extra != null && extra.size() > 0;
    }

    public String[] getChildrenCode() {
        if (children == null || children.isEmpty()) {
            return new String[0];
        }

        return children.split(",");
    }

    public void setDefault() {
        isDefault = 1;
    }

    public boolean isFunction() {
        return type == 4;
    }

    public boolean isModule() {
        return type == 3;
    }

    public boolean isCategory() {
        return type == 2;
    }

    public boolean isPlatform() {
        return type == 1;
    }

    private static Quotation frontPageFunction;

    public static Quotation getFrontPageFunction() {
        if (frontPageFunction == null) {
            frontPageFunction = new Quotation();
            frontPageFunction.title = "页面数量";
            frontPageFunction.description = "前端项目页面数量";
            frontPageFunction.type = 4;
        }

        return frontPageFunction;
    }

    private static Quotation frontPageModule;

    public static Quotation getFrontPageModule() {
        if (frontPageModule == null) {
            frontPageModule = new Quotation();
            frontPageModule.title = "页面数量";
            frontPageModule.type = 3;
        }

        return frontPageModule;
    }

    public boolean isFrontPlatform() {
        return code.equals("P005");
    }


}