package net.coding.mart.common;

import com.loopj.android.http.RequestParams;

import net.coding.mart.json.reward.Published;

import java.io.Serializable;

/**
 * Created by chenchao on 15/11/18.
 */
public class IssueJobParam implements Serializable {

    private static final long serialVersionUID = 3206959611787172457L;
    String type = "";
    int budget = -1;
    int need_pm = 0;
    int require_clear = 0;
    String require_doc = "0"; // 永远为空

    String name = "";
    String description = "";
    int duration;

    String first_sample = "";
    String second_sample = "";
    String first_file = "0"; // 永远为空
    String second_file = "0"; // 永远为空

    String contact_name = "";
    String contact_email = "";
    String contact_mobile = "";

    int id = 0;

    public IssueJobParam() {}

    public IssueJobParam(Published data) {
        id = data.getId();
        type = data.getType() + "";
        budget = data.getBudget();
        need_pm = data.getNeed_pm();

        require_clear = data.getRequire_clear();

        name = data.getName();
        description = data.getDescription();
        duration = data.getDuration();

        first_sample = data.getFirst_sample();
        second_sample = data.getSecond_sample();

        contact_name = data.getContact_name();
        contact_email = data.getContact_email();
        contact_mobile = data.getContact_mobile();
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public void setNeed_pm(boolean need_pm) {
        this.need_pm = need_pm ? 1 : 0;
    }

    public void setRequire_clear(int require_clear) {
        this.require_clear = require_clear;
    }

    public void setRequire_doc(String require_doc) {
        this.require_doc = require_doc;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setFirst_sample(String first_sample) {
        this.first_sample = first_sample;
    }

    public void setSecond_sample(String second_sample) {
        this.second_sample = second_sample;
    }

    public void setFirst_file(String first_file) {
        this.first_file = first_file;
    }

    public void setSecond_file(String second_file) {
        this.second_file = second_file;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public void setContact_email(String contact_email) {
        this.contact_email = contact_email;
    }

    public void setContact_mobile(String contact_mobile) {
        this.contact_mobile = contact_mobile;
    }

    public String getType() {
        return type;
    }

    public int getBudget() {
        return budget;
    }

    public boolean isNew() {
        return id == 0;
    }

    public boolean getNeed_pm() {
        return need_pm == 1;
    }

    public int getRequire_clear() {
        return require_clear;
    }

    public String getRequire_doc() {
        return require_doc;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getDuration() {
        return duration;
    }

    public String getFirst_sample() {
        return first_sample;
    }

    public String getSecond_sample() {
        return second_sample;
    }

    public String getFirst_file() {
        return first_file;
    }

    public String getSecond_file() {
        return second_file;
    }

    public String getContact_name() {
        return contact_name;
    }

    public String getContact_email() {
        return contact_email;
    }

    public String getContact_mobile() {
        return contact_mobile;
    }

    public RequestParams createRequestParam() {
        RequestParams params = new RequestParams();

        params.put("type", type);
        params.put("budget", budget);
        params.put("need_pm", need_pm);
        params.put("require_clear", require_clear);
        params.put("require_doc", require_doc); // 永远为空

        params.put("name", name);
        params.put("description", description);
        params.put("duration", duration);

        params.put("first_sample", first_sample);
        params.put("second_sample", second_sample);
        params.put("first_file", first_file); // 永远为空
        params.put("second_file", second_file); // 永远为空

        params.put("contact_name", contact_name);
        params.put("contact_email", contact_email);
        params.put("contact_mobile", contact_mobile);

        params.put("recommend", "");

        params.put("id", id);
//        params.put("id", 0);

        return params;
    }
}
