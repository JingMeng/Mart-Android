package net.coding.mart.activity.mpay;

import android.view.View;
import android.widget.TextView;

import net.coding.mart.R;
import net.coding.mart.json.mpay.Order;

public class MPayDynamicListItemHolder {

    private TextView title;
    private TextView content;
    private TextView money;
    private TextView mpayBalance;
    private TextView time;

    public MPayDynamicListItemHolder(View view) {
        title = (TextView) view.findViewById(R.id.title);
        content = (TextView) view.findViewById(R.id.content);
        money = (TextView) view.findViewById(R.id.money);
        mpayBalance = (TextView) view.findViewById(R.id.mpayBalance);
        time = (TextView) view.findViewById(R.id.time);
    }

    public void bind(Order order) {
        title.setText(order.title);
        content.setText(order.getTypeString());
        money.setText(order.getMoney());
        mpayBalance.setText(order.getStatusString());
        time.setText(order.getTime());
    }

    public TextView getMpayBalance() {
        return mpayBalance;
    }

    public TextView getTitle() {
        return title;
    }

    public TextView getContent() {
        return content;
    }

    public TextView getMoney() {
        return money;
    }

    public TextView getTime() {
        return time;
    }
}
