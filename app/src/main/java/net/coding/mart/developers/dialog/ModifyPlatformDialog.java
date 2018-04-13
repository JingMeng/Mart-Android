package net.coding.mart.developers.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.CheckedTextView;

import net.coding.mart.R;
import net.coding.mart.developers.FunctionListActivity;
import net.coding.mart.developers.HuoguoEntranceFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liu on 16/6/24.
 * 火锅单平台选择对话框
 */
public class ModifyPlatformDialog extends Dialog {

    private int[] buttonIds = new int[]{
            R.id.btn1,
            R.id.btn4,
            R.id.btn2,
            R.id.btn3,
            R.id.btn5,
            R.id.btn6,
            R.id.btn7,
    };

    private CheckedTextView[] checkButtons = new CheckedTextView[buttonIds.length];

    private FunctionListActivity activity;

    private List<String> ids = new ArrayList<>();

    public ModifyPlatformDialog(FunctionListActivity activity) {
        super(activity, R.style.umeng_socialize_popup_dialog);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_modify_platform);
        setCanceledOnTouchOutside(true);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        initView();
    }

    public void setPicked(List<String> picked) {
        ids.clear();
        if (picked != null) {
            ids.addAll(picked);
        }
    }

    private void initView() {
        for (int i = 0; i < buttonIds.length; ++i) {
            checkButtons[i] = (CheckedTextView) findViewById(buttonIds[i]);

            String tag = HuoguoEntranceFragment.PLATFORMS[i].code;
            checkButtons[i].setTag(tag);
            checkButtons[i].setChecked(ids.contains(tag));
            checkButtons[i].setOnClickListener(v -> {
                CheckedTextView checkedTextView = (CheckedTextView) v;
                boolean checked = !checkedTextView.isChecked();

                checkedTextView.setChecked(checked);
                String tag1 = (String) v.getTag();
                if (checked) {
                    ids.add(tag1);
                } else {
                    ids.remove(tag1);
                }
            });
        }

        findViewById(R.id.btn_cancle).setOnClickListener(v -> dismiss());
        findViewById(R.id.btn_ok).setOnClickListener(v -> {
            FunctionListActivity.sortOutSelectPlatform(ids);
            activity.updateView(ids);
            dismiss();
        });
    }

}
