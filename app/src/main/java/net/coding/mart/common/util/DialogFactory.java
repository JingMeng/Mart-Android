package net.coding.mart.common.util;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.view.Window;
import android.widget.TextView;

import net.coding.mart.R;

/**
 * Created by chenchao on 16/8/11.
 * 用于创建简单的提示对话框
 */
public class DialogFactory {

    public static void create(Context context, @LayoutRes int layout) {
        AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.Dialog).create();
        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.setContentView(layout);
        window.findViewById(R.id.demo_closebtn).setOnClickListener(v -> alertDialog.dismiss());
    }

    public static void create(Context context, @StringRes int titleId, @StringRes int messageId) {
        String title = context.getString(titleId);
        String message = context.getString(messageId);
        create(context, title, message);
    }

    public static void create(Context context, @NonNull String title, @NonNull  String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.Dialog).create();
        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.dialog_tip_reward_budget);
        window.findViewById(R.id.demo_closebtn).setOnClickListener(v -> alertDialog.dismiss());

        ((TextView) window.findViewById(R.id.title)).setText(title);
        ((TextView) window.findViewById(R.id.message)).setText(message);
    }
}
