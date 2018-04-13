package net.coding.mart.json.reward.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenchao on 16/10/13.
 */

public class Project implements Serializable {

    private static final long serialVersionUID = 6809650569211528292L;

    @SerializedName("projectName")
    @Expose
    public String projectName;
    @SerializedName("startedAt")
    @Expose
    public String startedAt;
    @SerializedName("endedAt")
    @Expose
    public String endedAt;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("duty")
    @Expose
    public String duty = "";
    @SerializedName("untilNow")
    @Expose
    public boolean untilNow;
    @SerializedName("industryName")
    @Expose
    public String industryName = "";
    @SerializedName("showUrl")
    @Expose
    public String showUrl = "";
    @SerializedName("startTime")
    @Expose
    public long startTime;
    @SerializedName("finishTime")
    @Expose
    public long finishTime;
    @SerializedName("attaches")
    @Expose
    public List<Attach> attaches = new ArrayList<>();

    public String getDetailTitle() {
        return String.format("%s", projectName);
    }

    public String getProjectTimeRange() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        String timeStart = sdf.format(startTime);
        String timeFinish = "至今";
        if (!untilNow) {
            timeFinish = sdf.format(finishTime);
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
