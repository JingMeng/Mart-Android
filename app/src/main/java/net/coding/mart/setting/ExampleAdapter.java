package net.coding.mart.setting;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.coding.mart.R;
import net.coding.mart.common.ImageLoadTool;
import net.coding.mart.json.Example;

import java.util.ArrayList;

/**
 * Created by chenchao on 16/3/3.
 *
 */
public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ListItemExampleHolder>{

    private ArrayList<Example> data;
    private View.OnClickListener click;

    public ExampleAdapter(ArrayList<Example> data, View.OnClickListener click) {
        this.data = data;
        this.click = click;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public ListItemExampleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ListItemExampleHolder itemView = new ListItemExampleHolder(LayoutInflater.from(parent.getContext()), parent);
        itemView.itemView.setOnClickListener(click);
        return itemView;
    }

    @Override
    public void onBindViewHolder(ListItemExampleHolder holder, int position) {
        Example dataItem = data.get(position);
        ImageLoadTool.loadImage(holder.icon, dataItem.getLogo());
        holder.name.setText(String.format("%s (%s)", dataItem.getTitle(), dataItem.getTypeString()));
        setTextView(holder.money, "金额 <font color='#505050'>¥%s</font>", dataItem.getAmount());
        setTextView(holder.time, "项目时间 <font color='#505050'>%s</font> 天", dataItem.getDuration());
        setTextView(holder.role, "参与角色 <font color='#505050'>%s</font>", dataItem.getCharacter());
        holder.itemView.setTag(dataItem);
    }

    private void setTextView(TextView textView, String foramt, Object... textString) {
        textView.setText(Html.fromHtml(String.format(foramt, textString)));
    }

    static class ListItemExampleHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView name;
        TextView money;
        TextView time;
        TextView role;

        ListItemExampleHolder(LayoutInflater inflater, ViewGroup parent) {
            this(inflater.inflate(R.layout.list_item_example, parent, false));

        }

        ListItemExampleHolder(View view) {
            super(view);
            icon = (ImageView) view.findViewById(R.id.icon);
            name = (TextView) view.findViewById(R.id.name);
            money = (TextView) view.findViewById(R.id.money);
            time = (TextView) view.findViewById(R.id.time);
            role = (TextView) view.findViewById(R.id.role);
        }
    }
}
