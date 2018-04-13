package net.coding.mart.json;

import net.coding.mart.common.constant.AccountType;
import net.coding.mart.common.constant.DemandType;
import net.coding.mart.common.constant.DeveloperType;
import net.coding.mart.json.mart2.user.City;
import net.coding.mart.json.mart2.user.MartUser;

import java.io.Serializable;

public class CurrentUser implements Serializable {

    private static final long serialVersionUID = 4061924466657853948L;

    public MartUser user = new MartUser();

    public CurrentUser() {
    }

    public CurrentUser(MartUser user) {
        updateData(user);
    }

    public void updateData(MartUser user) {
        this.user = user;
    }

    public boolean isDemand() {
        return user.accountType == AccountType.DEMAND;
    }


    public String getPhoneCountryCode() {
        return user.countryCode;
    }

    public String getPhoneIsoCode() {
        return user.isoCode;
    }

    public int getId() {
        return user.id;
    }

    public String getGlobal_key() {
        return user.username;
    }

    public String getAvatar() {
        return user.avatar;
    }

    public String getName() {
        return user.name;
    }

    public boolean getPassingSurvey() {
        return user.surveyComplete;
    }

    public String getPhone() {
        return user.phone;
    }

    public String getEmail() {
        return user.email;
    }

    public void setEmail(String email) {
        user.email = email;
        user.emailValidation = true;
    }

    public City getProvince() {
        return user.province;
    }

    public City getDistrict() {
        return user.district;
    }

    public City getCity() {
        return user.city;
    }

    public String getDescription() {
        return user.description;
    }

    public boolean isPhoneValide() {
        return user.phoneValidation;
    }

    public boolean isEmailValide() {
        return user.emailValidation;
    }

    public String getQq() {
        return user.qq;
    }

    public boolean isFullInfo() {
        return user.infoComplete;
    }

    public boolean isFullSkills() {
        return user.skillComplete;
    }

    public boolean isIdentityChecked() {
        return user.identityPassed;
    }

    public boolean isExcellect() {
        return user.excellent;
    }

    public boolean isPassingSurvey() {
        return user.surveyComplete;
    }

    public boolean isEnterpriseAccout() {
        return user.isEnterpriseAccount();
    }

    public DeveloperType getRewardRole() {
        return user.developerType;
    }

    public boolean isPassEnterpriceIdentity() {
        return user.identityPassed && user.demandType == DemandType.ENTERPRISE && user.accountType == AccountType.DEMAND;
    }

    public int getJoinCount() {
        return user.counter.joined;
    }

    public int setJoinCount(int count) {
        return user.counter.joined = count;
    }

    public int getPublishedCount() {
        return user.counter.published;
    }



}
