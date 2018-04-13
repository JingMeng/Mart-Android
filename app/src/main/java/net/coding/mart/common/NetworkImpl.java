package net.coding.mart.common;

public class NetworkImpl {
    public static final int NETWORK_CONNECT_FAIL = -2; // 联网失败
    public static final String ERROR_MSG_CONNECT_FAIL = "连接服务器失败，请检查网络或稍后重试";
    public static final int NETWORK_ERROR_SERVICE = -3; // 服务器返回了非 json 格式数据
    public static final String ERROR_MSG_SERVICE_ERROR = "服务器内部错误，请稍后重试";
}