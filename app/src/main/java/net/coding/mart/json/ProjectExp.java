
package net.coding.mart.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProjectExp implements Serializable {

    private static final long serialVersionUID = 2679801265479989095L;
    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName(value = "user_id", alternate = {"userId"})
    @Expose
    public int userId;
    @SerializedName(value = "project_name", alternate = {"projectName"})
    @Expose
    public String projectName = "";
    @SerializedName("description")
    @Expose
    public String description = "";
    @SerializedName("duty")
    @Expose
    public String duty = "";
    @SerializedName(value = "description_lfcr_escape", alternate = {"descriptionLfcrEscape"})
    @Expose
    public String descriptionLfcrEscape = "";
    @SerializedName(value = "duty_lfcr_escape", alternate = {"dutyLfcrEscape"})
    @Expose
    public String dutyLfcrEscape = "";
    @SerializedName(value = "start_time", alternate = {"startTime"})
    @Expose
    public String startTime = "";
    @SerializedName(value = "finish_time", alternate = {"finishTime"})
    @Expose
    public String finishTime = "";
    @SerializedName(value = "start_time_chinese", alternate = {"startTimeChinese"})
    @Expose
    public String startTimeChinese = "";
    @SerializedName(value = "finish_time_chinese", alternate = {"finishTimeChinese"})
    @Expose
    public String finishTimeChinese = "";
    @SerializedName(value = "start_time_numerical", alternate = {"startTimeNumerical"})
    @Expose
    public String startTimeNumerical = "";
    @SerializedName(value = "finish_time_numerical", alternate = {"finishTimeNumerical"})
    @Expose
    public String finishTimeNumerical = "";
    @SerializedName(value = "until_now", alternate = {"untilNow"})
    @Expose
    public boolean untilNow;
    @SerializedName("link")
    @Expose
    public String link = "";
    @SerializedName(value = "file_ids", alternate = {"fileIds"})
    @Expose
    public String fileIds = "";
    @SerializedName("files")
    @Expose
    public List<File> files = new ArrayList<File>();
    @SerializedName(value = "markdown_description", alternate = {"markdownDescription"})
    @Expose
    public String markdownDescription = "";

}
