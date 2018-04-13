package net.coding.mart.common.widget;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by chenchao on 15/12/22.
 */
public interface OnTextChange {
    void addTextChangedListener(TextWatcher watcher);
    boolean isEmpty();
    Editable getText();
    int getVisibility();
}
