package net.coding.mart.json;

import java.io.Serializable;

/**
 * Created by chenchao on 16/3/31.
 * 返回结果为 { data="true" code=0}
 */
public class SimpleHttpResult extends HttpResult<Boolean> implements Serializable {

    private static final long serialVersionUID = -6798759681837634498L;

    public static final int codeFalse = 1;

}
