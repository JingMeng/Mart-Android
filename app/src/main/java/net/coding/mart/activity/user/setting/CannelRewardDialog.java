package net.coding.mart.activity.user.setting;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import net.coding.mart.R;
import net.coding.mart.common.widget.BaseDialog;
import net.coding.mart.common.widget.SingleToast;

/**
 * Created by chenchao on 16/8/12.
 */
public class CannelRewardDialog extends BaseDialog {

    View.OnClickListener clickRight;

    String reason = "";
    private TextView[] texts;

    public CannelRewardDialog(Context context, View.OnClickListener clickRight) {
        super(context);
        this.clickRight = clickRight;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.dialog_cannel_reward;
    }

    @Override
    protected void initView() {
        TextView leftButton = (TextView) findViewById(R.id.leftButton);
        TextView rightButton = (TextView) findViewById(R.id.rightButton);

        int[] single = new int[] {
                R.id.text0,
                R.id.text1,
                R.id.text2,
                R.id.text3,
                R.id.text4,
        };

        texts = new TextView[single.length];
        for (int i = 0; i < single.length; ++i) {
            texts[i] = (TextView) findViewById(single[i]);
            texts[i].setOnClickListener(v -> {
                for (TextView item : texts) {
                    Resources resources = getContext().getResources();
                    if (item == v) {
                        reason = item.getText().toString();
                        item.setTextColor(resources.getColor(R.color.font_blue));
                        item.setCompoundDrawablesWithIntrinsicBounds(null, null, resources.getDrawable(R.mipmap.ic_hook_small), null);
                    } else {
                        item.setTextColor(resources.getColor(R.color.font_black_2));
                        item.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    }
                }
            });
        }

        leftButton.setOnClickListener(clickCancel);
        rightButton.setOnClickListener(clickRight);

        rightButton.setOnClickListener(v -> {
            if (TextUtils.isEmpty(reason)) {
                SingleToast.showMiddleToast(getContext(), "请选择取消理由");
            } else {
                v.setTag(reason);
                clickRight.onClick(v);
                clickCancel.onClick(v);
            }
        });
    }

}
