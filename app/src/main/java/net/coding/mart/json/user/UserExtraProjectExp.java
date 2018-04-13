package net.coding.mart.json.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import net.coding.mart.json.ProjectExp;
import net.coding.mart.json.reward.IndustryName;

import java.io.Serializable;
import java.util.ArrayList;

public class UserExtraProjectExp extends ProjectExp implements Serializable {

    private static final long serialVersionUID = -264218958879161630L;

    @SerializedName("industry")
    @Expose
    private String industry;
    @SerializedName("industryName")
    @Expose
    private String industryNamed;
    @SerializedName("projectTypeName")
    @Expose
    public String projectTypeName;
    @SerializedName("projectType")
    @Expose
    public int projectType;

    private ArrayList<IndustryName> industrys = null;

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


    public String getProjectTimeRange() {
        String timeStart = timeString(startTimeNumerical);
        String timeFinish = "至今";
        if (!untilNow) {
            timeFinish = timeString(finishTimeNumerical);
        }
        return String.format("%s - %s", timeStart, timeFinish);
    }


    private String timeString(String time) {
        if (time == null) {
            return "";
        }

        time = time.replace('-', '.');
        if (8 <= time.length()) {
            time = time.substring(0, 7);
        }
        return time;
    }
}
