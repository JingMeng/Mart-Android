package net.coding.mart.setting;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;

import net.coding.mart.json.Notification;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by chenchao on 16/3/7.
 * 通知列表的 adapter
 */
public class NotificationAdapter extends UltimateViewAdapter<RecyclerItemNotifyHolder> {

    public static SimpleDateFormat timeFormat =
            new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);

    ArrayList<Notification> data;
    View.OnClickListener itemClick;

    public NotificationAdapter(ArrayList<Notification> data, View.OnClickListener itemClick) {
        this.data = data;
        this.itemClick = itemClick;
    }

    @Override
    public void onBindViewHolder(RecyclerItemNotifyHolder holder, int position) {
        Notification itemData = data.get(position);
        holder.rootLayout.setTag(itemData);

        holder.redPoint.setVisibility(itemData.isRead() ? View.INVISIBLE : View.VISIBLE);
        holder.time.setText(timeFormat.format(itemData.getCreated_at()));
        if (itemData.isRead()) {
            holder.title.setTextColor(0xFF999999);
            holder.descript.setTextColor(0xFF999999);
        } else {
            holder.title.setTextColor(0xFF222222);
            holder.descript.setTextColor(0xFF666666);
        }

        String content = itemData.getContent();
        int pos = content.indexOf("原因：");
        if (pos != -1) {
            String titleString = content.substring(0, pos);
            String descripString = content.substring(pos, content.length());

            holder.title.setHtmlText(titleString);
            holder.descript.setVisibility(View.VISIBLE);
            holder.descript.setHtmlText(descripString);
        } else {
            holder.title.setHtmlText(content);
            holder.descript.setVisibility(View.GONE);
        }

    }

    @Override
    public long generateHeaderId(int position) {
        return 0;
    }

    @Override
    public RecyclerItemNotifyHolder onCreateViewHolder(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RecyclerItemNotifyHolder holder = new RecyclerItemNotifyHolder(inflater, parent);
        holder.rootLayout.setOnClickListener(itemClick);
        holder.title.setOnClickListener(v -> holder.rootLayout.callOnClick());
        holder.descript.setOnClickListener(v -> holder.rootLayout.callOnClick());
        return holder;
    }

    @Override
    public int getAdapterItemCount() {
        return data.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
    }

    @Override
    public RecyclerItemNotifyHolder newFooterHolder(View view) {
        return null;
    }

    @Override
    public RecyclerItemNotifyHolder newHeaderHolder(View view) {
        return null;
    }
}
