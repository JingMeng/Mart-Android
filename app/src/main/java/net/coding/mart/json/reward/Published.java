package net.coding.mart.json.reward;

import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import net.coding.mart.common.Global;
import net.coding.mart.common.MyData;
import net.coding.mart.common.constant.Budget;
import net.coding.mart.common.constant.Progress;
import net.coding.mart.json.File;
import net.coding.mart.json.RoleType;
import net.coding.mart.json.user.Owner;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.Generated;

/**
 * Created by chenchao on 16/7/28.
 */
@Generated("org.jsonschema2pojo")
public class Published extends SimplePublished implements Serializable {

    private static final long serialVersionUID = -1625039673285556631L;

    private static int VERSION_CUSTOM = 1; // 自助发布
    private static int VERSION_QUICK = 0; // 快速发布

    @SerializedName("price")
    @Expose
    public BigDecimal price;
    @SerializedName(value = "service_fee", alternate = {"serviceFee"})
    @Expose
    public BigDecimal serviceFee;
    @SerializedName(value = "service_fee_percent", alternate = {"serviceFeePercent"})
    @Expose
    public BigDecimal serviceFeePercent;
    @SerializedName(value = "format_price", alternate = {"formatPrice"})
    @Expose
    public String formatPrice;
    @SerializedName("content")
    @Expose
    public String content;
    @SerializedName(value = "format_content", alternate = {"formatContent"})
    @Expose
    public String formatContent;
    @SerializedName("banner")
    @Expose
    public String banner;
    @SerializedName(value = "start_time", alternate = {"startTime"})
    @Expose
    public String startTime;
    @SerializedName(value = "finish_time", alternate = {"finishTime"})
    @Expose
    public String finishTime;
    @SerializedName(value = "finish_day", alternate = {"finishDay"})
    @Expose
    public String finishDay;
    @SerializedName(value = "finish_month", alternate = {"finishMonth"})
    @Expose
    public String finishMonth;
    @SerializedName(value = "finish_year", alternate = {"finishYear"})
    @Expose
    public String finishYear;
    @SerializedName("home")
    @Expose
    public String home;
    @SerializedName("progress")
    @Expose
    public int progress;
    @SerializedName("mpay")
    @Expose
    public int mpay; // 1 表示使用了开发宝
    @SerializedName(value = "second_sample", alternate = {"secondSample"})
    @Expose
    public String secondSample;
    @SerializedName(value = "format_second_sample", alternate = {"formatSecondSample"})
    @Expose
    public String formatSecondSample;
    @SerializedName(value = "first_file", alternate = {"firstFile"})
    @Expose
    public int firstFile;
    @SerializedName(value = "second_file", alternate = {"secondFile"})
    @Expose
    public int secondFile;
    @SerializedName(value = "contact_name", alternate = {"contactName"})
    @Expose
    public String contactName;
    @SerializedName(value = "contact_email", alternate = {"contactEmail"})
    @Expose
    public String contactEmail;
    @SerializedName(value = "contact_mobile", alternate = {"contactMobile"})
    @Expose
    public String contactMobile;
    @SerializedName(value = "created_at", alternate = {"createdAt"})
    @Expose
    public String createdAt;
    @SerializedName(value = "updated_at", alternate = {"updatedAt"})
    @Expose
    public String updatedAt;
    @SerializedName(value = "format_created_at", alternate = {"formatCreatedAt"})
    @Expose
    public String formatCreatedAt;
    @SerializedName("budget")
    @Expose
    public int budget;
    @SerializedName(value = "require_clear", alternate = {"requireClear"})
    @Expose
    public int requireClear;
    @SerializedName(value = "require_doc", alternate = {"requireDoc"})
    @Expose
    public int requireDoc;
    @SerializedName(value = "show_require_doc", alternate = {"showRequireDoc"})
    @Expose
    public int showRequireDoc;
    @SerializedName(value = "need_pm", alternate = {"needPm"})
    @Expose
    public int needPm;
    @SerializedName(value = "project_link", alternate = {"projectLink"})
    @Expose
    public String projectLink;
    @SerializedName("recommend")
    @Expose
    public String recommend;
    @SerializedName("balance")
    @Expose
    public BigDecimal balance = new BigDecimal(0);
    @SerializedName("testService")
    @Expose
    public boolean testService;
    @SerializedName(value = "format_balance", alternate = {"formatBalance"})
    @Expose
    public String formatBalance;
    @SerializedName(value = "price_with_fee", alternate = {"priceWithFee"})
    @Expose
    public BigDecimal priceWithFee;
    @SerializedName(value = "format_price_with_fee", alternate = {"formatPriceWithFee"})
    @Expose
    public String formatPriceWithFee;
    @SerializedName(value = "need_maluation", alternate = {"needMaluation"})
    @Expose
    public boolean needMaluation;
    @SerializedName(value = "submit_way", alternate = {"submitWay"})
    @Expose
    public int submitWay;
    @SerializedName("country")
    @Expose
    public String country;
    @SerializedName("phoneCountryCode")
    @Expose
    public String phoneCountryCode;
    @SerializedName("version")
    @Expose
    public int version; // 1 表示自助发布
    @SerializedName("developPlan")
    @Expose
    public String developPlan;
    @SerializedName("rewardDemand")
    @Expose
    public String rewardDemand;
    @SerializedName("bargain")
    @Expose
    public boolean bargain;
    @SerializedName("warranty")
    @Expose
    public int warranty;
    @SerializedName("province")
    @Expose
    public int province;
    @SerializedName("city")
    @Expose
    public int city;
    @SerializedName(value = "service_type", alternate = {"serviceType"})
    @Expose
    public int serviceType;
    @SerializedName("visitCount")
    @Expose
    public int visitCount;
    @SerializedName(value = "need_pay_prepayment", alternate = {"needPayPrepayment"})
    @Expose
    public boolean needPayPrepayment;
    @SerializedName("owner")
    @Expose
    public Owner owner = new Owner();
    @SerializedName("roles")
    @Expose
    public ArrayList<PublishedItemRole> roles;
    @SerializedName("filesToShow")
    @Expose
    public ArrayList<File> filesToShow;
    @SerializedName("phaseType")
    @Expose
    public PhaseType phaseType = PhaseType.STAGE;
    @SerializedName("industry")
    @Expose
    private String industry;
    @SerializedName("industryName")
    @Expose
    private String industryNamed;
    private ArrayList<IndustryName> industrys = null;


//    @SerializedName("duration")
//    @Expose
//    public int duration;
//    @SerializedName("rewardDemand")
//    @Expose
//    public String rewardDemand = "";
//    @SerializedName(value = "submit_way", alternate = {"submit_way"})
//    @Expose
//    public int submitWay;
//    @SerializedName(value = "submit_way", alternate = {"submit_way"})
//    @Expose
//    public int submitWay;

    public boolean isNewPhase() {
        return phaseType == Published.PhaseType.PHASE;
    }

    public boolean isMy() {
        return owner.globalKey.equals(MyData.getInstance().getData().getGlobal_key());
    }

    public ArrayList<IndustryName> getIndustrys() {
        if (industrys == null) {
            industrys = new ArrayList<>();
            String[] ids = industry.split(",");
            String[] names = industryNamed.split(",");

            for (int i = 0; i < ids.length; ++i) {
                try {
                    int id = Integer.valueOf(ids[i]);
                    if (i < names.length) {
                        String name = names[i];
                        IndustryName industryName = new IndustryName();
                        industryName.setData(id, name);
                        industrys.add(industryName);
                    } else {
                        break;
                    }
                } catch (Exception e) {
                    break;
                }
            }
        }

        return industrys;
    }

    public void updateIndustry(ArrayList<IndustryName> industryNames) {
        if (industrys == null) {
            industrys = new ArrayList<>();
        } else {
            industrys.clear();
        }

        industrys.addAll(industryNames);
    }

    public String getIndustrysString() {
        ArrayList<IndustryName> industryNames = getIndustrys();
        return IndustryName.createName(industryNames);
    }

    public String getIndustrysIdString() {
        ArrayList<IndustryName> list = getIndustrys();
        return IndustryName.createIdString(list);
    }

    public String getVersionString() {
        return version == 1 ? "自助发布" : "快速发布";
    }

    public double getBalance() {
        return balance.doubleValue();
    }

    public void setBalance(double balance) {
        this.balance = new BigDecimal(balance);
    }

    public boolean isCustomVersion() {
        return version == VERSION_CUSTOM;
    }

    public double getPrice_with_fee() {
        return priceWithFee.doubleValue();
    }

    public boolean isMPay() { // 现有的项目都是开发宝了
        return mpay == 1;
    }

    public boolean canEdit() {
        Progress progress = getStatus();
        return progress == Progress.waitPay || progress == Progress.recruit;
    }

    public String getWarrantyString() {
        return warranty + " 天";
    }

    public String getTitle() {
        return title;
    }

    public int getType() {
        return type;
    }

    public String getTypeString() {
        return getRewardTypeString();
    }

    public String getFormat_price() {
        return formatPrice;
    }

    public String getDescription() {
        return description;
    }

    public String getContent() {
        return content;
    }

    public String getCover() {
        return cover;
    }

    public String getHome() {
        return home;
    }

    public void setStatusCannel() {
        status = 3;
    }

    public int getProgress() {
        return progress;
    }

    public String getFirst_sample() {
        return firstSample;
    }

    public String getSecond_sample() {
        return secondSample;
    }

    public String getContact_name() {
        return contactName;
    }

    public String getContact_email() {
        return contactEmail;
    }

    public String getContact_mobile() {
        return contactMobile;
    }

    public String getCreated_at() {
        return createdAt;
    }

    public String getUpdated_at() {
        return updatedAt;
    }

    public int getBudget() {
        return budget;
    }

    public List<RoleType> getRoleTypes() {
        return roleTypes;
    }

    public int getRequire_clear() {
        return requireClear;
    }

    public int getNeed_pm() {
        return needPm;
    }

    public boolean noPay() {
        return priceWithFee.equals(balance);
    }

    public void pay(double money) {
        balance = balance.subtract(new BigDecimal(money));
        formatBalance = NumberFormat.getCurrencyInstance(Locale.CHINA).format(balance);
    }

    public String getFormat_balance() {
        return formatBalance;
    }

    public void setFormat_balance(String format_balance) {
        this.formatBalance = format_balance;
    }

    public String getFormat_price_with_fee() {
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(Locale.CHINA);
        if (formatPriceWithFee.isEmpty()) {
            return numberFormat.format(priceWithFee);
        }

        return formatPriceWithFee;
    }

    public double getPrice() {
        return price.doubleValue();
    }

    public String getUrl() {
        return Global.generateRewardLink(getId());
    }

    public boolean isIndependent() {
        return version == 1;
    }

    public int getRoleId(int pos) {
        return roleTypes.get(pos).id;
    }

    public String getSampleLinks() {
        String link = "";
        if (!TextUtils.isEmpty(firstSample)) {
            link = firstSample;
        }

        if (!TextUtils.isEmpty(secondSample)) {
            link += "/n" + secondSample;
        }

        return link;
    }

    public String getFullPhone() {
        return phoneCountryCode + " " + contactMobile;
    }

    public String getIdString() {
        return String.valueOf(getId());
    }

    public String getBudgetString() {
        return Budget.idToName(budget);
    }

    public String getDurationString() {
        return getDuration() > 0 ? String.format("%s 天", getDuration()) : "待商议";
    }

    public String getServiceTypeString() {
        switch (serviceType) {
            case 1:
                return "产品原型";
            case 2:
                return "UI 设计";
            case 3:
                return "整体方案";
            default:
                return "软件开发";
        }
    }

    enum PhaseType {
        UNKNOWN_PHASE_TYPE, //= 0;
        STAGE,  // 1; // 多角色阶段划分 // 旧的
        PHASE;  //= 2; // 单角色阶段划分 // 新的
    }
}