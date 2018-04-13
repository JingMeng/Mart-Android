package net.coding.mart.common.event;

/**
 * Created by chenchao on 16/10/8.
 */

public class CallPhoneEvent {

    public enum Type {
        Phone, QQ, Email
    }

    public final String content;
    public final Type type;

    public CallPhoneEvent(String content) {
        this(content, Type.Phone);
    }

    public CallPhoneEvent(String content, Type type) {
        this.content = content;
        this.type = type;
    }
}
