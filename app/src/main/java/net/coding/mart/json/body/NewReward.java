package net.coding.mart.json.body;

import android.text.TextUtils;

import net.coding.mart.common.constant.RewardType;
import net.coding.mart.json.reward.IndustryName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenchao on 16/3/31.
 */
public class NewReward implements Serializable {

    private static final long serialVersionUID = 4192927094231213734L;

    public String id = "";
    public int type = -1;
    public String name = "";
    public String description = "";
    public String contact_email = "";
    public String contact_name = "";
    public String contact_mobile = "";
    public String contact_mobile_code = "";
    public String survey_extra = "";
    public String recommend = "";

    public String phoneCountryCode;
    public String country;

    public String price = "";
    public ArrayList<IndustryName> industrys = new ArrayList<>();

    public boolean bargain = false;
    public int developerRole = 0;
    public DeveloperType developerType = null;
    public String rewardDemand = "";
    public int duration;

//    public String industry = "";


    public Map<String, String> createMap() {
        Map<String, String> map = new HashMap<>();
        if (id != null && !id.isEmpty()) {
            map.put("id", id);
        }

        map.put("type", RewardType.idToEnum(type).newType);
        map.put("name", name);
        map.put("description", description);
        map.put("survey_extra", survey_extra);
        map.put("recommend", recommend);

        map.put("contactName", contact_name);
        map.put("contactEmail", contact_email);
        map.put("countryCode", phoneCountryCode);
        map.put("isoCode", country);

        map.put("contactMobile", contact_mobile);
        if (!TextUtils.isEmpty(contact_mobile_code)) {
            map.put("verifyCode", contact_mobile_code);
        }

        map.put("price", price);
        map.put("bargain", String.valueOf(bargain));

        map.put("industry", IndustryName.createIdString(industrys));

        map.put("developerType", developerType.name());
        map.put("developerRole", String.valueOf(developerRole));

        map.put("rewardDemand", rewardDemand);
        map.put("duration", String.valueOf(duration));

        return map;
    }

    public boolean isModify() {
        return !TextUtils.isEmpty(name) ||
                !TextUtils.isEmpty(description) ||
                !industrys.isEmpty() ||
                !TextUtils.isEmpty(price) ||
                duration > 0 ||
                !TextUtils.isEmpty(rewardDemand);
    }

    public enum DeveloperType {
        PERSONAL,
        TEAM
    }
}
