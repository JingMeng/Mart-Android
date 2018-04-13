package net.coding.mart.activity.mpay.freeze;

import android.view.View;
import android.widget.TextView;

import net.coding.mart.R;
import net.coding.mart.common.Global;
import net.coding.mart.json.mpay.FreezeDynamic;

public class FreezeDynamicItemHolder {
    private TextView title;
    private TextView money;
    private TextView time;

    public FreezeDynamicItemHolder(View view) {
        title = (TextView) view.findViewById(R.id.title);
        money = (TextView) view.findViewById(R.id.money);
        time = (TextView) view.findViewById(R.id.time);
    }

    public void bind(FreezeDynamic data) {
        title.setText(data.source);
        money.setText(data.amount);
        time.setText(Global.timeToString(data.updatedAt));
    }

    public TextView getMoney() {
        return money;
    }

    public TextView getTitle() {
        return title;
    }

    public TextView getTime() {
        return time;
    }
}
