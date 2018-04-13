package net.coding.mart.common.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import net.coding.mart.common.widget.InputRequest;
import net.coding.mart.common.widget.OnTextChange;
import net.coding.mart.common.widget.SimpleTextWatcher;

/**
 * Created by chenchao on 15/12/22.
 */
public class ViewStyleUtil {

    private static InputRequest sNoEmptyRequest = s -> s.length() > 0;

    // 只有所有 EditText 都填写了，Button 才是可点击状态
    public static void editTextBindButton(View button, OnTextChange... edits) {
        editTextBindButton(button, sNoEmptyRequest, edits);
    }

    public static void editTextBindButton(View button, EditText... edits) {
        for (EditText edit : edits) {
            edit.addTextChangedListener(new SimpleTextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                    button.setEnabled(true);
                    for (EditText item : edits) {
                        if (item.getText().length() <= 0) {
                            button.setEnabled(false);
                            break;
                        }
                    }
                }
            });
        }

        button.setEnabled(false);
    }

    public static void editTextBindButton(View button, InputRequest request, OnTextChange... edits) {
        for (OnTextChange edit : edits) {
            edit.addTextChangedListener(new SimpleTextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                    updateStyle(button, request, edits);
                }
            });
        }

        updateStyle(button, request, edits);
    }

    private static void updateStyle(View button, InputRequest request, OnTextChange[] edits) {
        for (OnTextChange item : edits) {
            if (item.getVisibility() == View.VISIBLE) {
                String input = item.getText().toString();
                if (!request.isCurrectFormat(input)) {
                    button.setEnabled(false);
                    return;
                }
            }
        }
        button.setEnabled(true);
    }

    public static void editAddTextChange(TextWatcher textWatcher, EditText... edits) {
        for (EditText item : edits) {
            item.addTextChangedListener(textWatcher);
        }
    }
}
