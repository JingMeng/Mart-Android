package net.coding.mart.activity.user;

import net.coding.mart.json.user.UserExtraProjectExp;
import net.coding.mart.json.user.UserExtraRole;

/**
 * Created by chenchao on 16/4/16.
 */
interface ModifyDataAction {
    void addRole();

    void modifyRole(UserExtraRole role);

    void addProjectExp();

    void modifyProjectExp(UserExtraProjectExp projectExp);
}
