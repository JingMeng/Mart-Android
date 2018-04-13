package net.coding.mart.login;

import java.util.Map;

/**
 * Created by chenchao on 15/12/18.
 */
public interface SetPasswordCallback {
    void next();
    Map<String, String> getRequestParmas();
}
