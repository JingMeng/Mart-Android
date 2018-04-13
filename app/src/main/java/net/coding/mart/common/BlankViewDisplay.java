package net.coding.mart.common;

import android.view.View;
import android.widget.TextView;

import net.coding.mart.R;

/**
 * Created by chaochen on 14-10-24.
 * 内容为空白时显示的提示语
 */
public class BlankViewDisplay {

    public static void setBlank(int itemSize, Object fragment, boolean request, View v, View.OnClickListener onClick) {
        setBlank(itemSize, fragment, request, v, onClick, "");
    }

    public static void showLoading(View v) {
        v.setVisibility(View.VISIBLE);
        v.findViewById(R.id.loading).setVisibility(View.VISIBLE);
        v.findViewById(R.id.loadFail).setVisibility(View.INVISIBLE);
    }

    public static void setBlank(int itemSize, Object fragment, boolean request, View v, View.OnClickListener onClick, String tipString) {
        // 有些界面不需要显示blank状态
        if (v == null) {
            return;
        }

        View btn = v.findViewById(R.id.btnRetry);
        if (request) {
            btn.setVisibility(View.GONE);
        } else {
            btn.setVisibility(View.VISIBLE);
            btn.setOnClickListener(onClick);
        }

        setBlank(itemSize, fragment, request, v, tipString);
    }

    private static void setBlank(int itemSize, Object fragment, boolean request, View v, String tipString) {
        v.findViewById(R.id.loading).setVisibility(View.INVISIBLE);
        v.findViewById(R.id.loadFail).setVisibility(View.VISIBLE);

        boolean show = (itemSize == 0);
        if (!show) {
            v.setVisibility(View.GONE);
            return;
        }
        v.setVisibility(View.VISIBLE);

        int iconId = R.mipmap.ic_exception_no_network;
        String text = "";

        if (tipString.isEmpty()) {
            if (request) {
//                if (fragment instanceof JobListFragment) {
//                    iconId = R.mipmap.ic_exception_blank_task;
//                    text = "还没有项目";
//                }

            } else {
                iconId = R.mipmap.ic_exception_no_network;
                text = "获取数据失败\n请检查下网络是否通畅";
            }
        } else {
            if (request) {
                iconId = R.mipmap.ic_exception_blank_task;
            } else {
                iconId = R.mipmap.ic_exception_no_network;
            }
            text = tipString;
        }

        v.findViewById(R.id.icon).setBackgroundResource(iconId);
        ((TextView) v.findViewById(R.id.message)).setText(text);
    }

}
