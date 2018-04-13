package net.coding.mart.developers.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.coding.mart.R;
import net.coding.mart.developers.FunctionListActivity;
import net.coding.mart.developers.fragment.FunctionFragment;
import net.coding.mart.json.developer.Quotation;

import java.util.List;

/**
 * Created by liu on 16/5/30.
 */
public class CategoryAdapter extends BaseAdapter {
    private List<Quotation> data;
    private FunctionListActivity activity;
    private String mark;
    private ItemOnClickListener listener;
    private FunctionFragment fragment;
    private boolean collapse = false;
    private boolean showTowLine = true;

    public CategoryAdapter(FunctionListActivity activity, FunctionFragment fragment) {
        this.activity = activity;
        this.listener = new ItemOnClickListener();
        this.fragment = fragment;
    }

    public void setData(List<Quotation> data) {
        this.data = data;
        this.mark = data.get(0).code;
        notifyDataSetChanged();
    }

    public void setCollapse(boolean collapse) {
        this.collapse = collapse;
        notifyDataSetChanged();
    }

    public void setTwoLine(boolean showTowLine) {
        this.showTowLine = showTowLine;
        notifyDataSetChanged();
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(activity).inflate(R.layout.list_main_category_item, null);
            holder = new ViewHolder();
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (showTowLine) {
            holder.tv_name.setMaxLines(2);
        } else {
            holder.tv_name.setMaxLines(1);
        }

        Quotation vo = data.get(position);
        String title = data.get(position).title;
        if (collapse) {
            title = title.substring(0, 1);
        }
        holder.tv_name.setText(title);

        if (data.get(position).code.equals(mark)) {
            holder.tv_name.setBackgroundResource(R.drawable.developer_function_list_main_bg);
            holder.tv_name.setTextColor(0xff8796A8);
        } else {
            holder.tv_name.setBackgroundResource(0);
            holder.tv_name.setTextColor(0xffffffff);
        }
        holder.tv_name.setOnClickListener(listener);
        holder.tv_name.setTag(vo);
        return convertView;
    }

    private class ViewHolder {
        TextView tv_name;
    }

    private class ItemOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Quotation vo = (Quotation) v.getTag();
            mark = vo.code;
            notifyDataSetChanged();
            fragment.mainSelect(vo);
        }
    }
}
