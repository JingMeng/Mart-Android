package net.coding.mart.login;

import java.util.Map;

/**
 * Created by chenchao on 15/12/18.
 */
public interface RegisterCallback {
    Map<String, String> getRequestParmas();

    void pop2();

    void pop3(String title);

    void pop4();
}
