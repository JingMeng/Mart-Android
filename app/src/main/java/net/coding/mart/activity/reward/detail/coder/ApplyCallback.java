package net.coding.mart.activity.reward.detail.coder;

import net.coding.mart.json.reward.Coder;

/**
 * Created by chenchao on 16/10/17.
 */
public interface ApplyCallback {
    void applyAccept(Coder coder);

    void applyReject(Coder coder);
}
