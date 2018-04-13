package net.coding.mart.setting;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.coding.mart.R;
import net.coding.mart.common.widget.NotifyHtmlTextView;

public class RecyclerItemNotifyHolder extends RecyclerView.ViewHolder {
    public NotifyHtmlTextView title;
    public NotifyHtmlTextView descript;
    public TextView time;
    public View redPoint;
    public ViewGroup rootLayout;

    public RecyclerItemNotifyHolder(LayoutInflater inflater, ViewGroup parent) {
        this(inflater.inflate(R.layout.recycler_item_notify, parent, false));
    }

    public RecyclerItemNotifyHolder(View view) {
        super(view);
        rootLayout = (ViewGroup) view;
        title = (NotifyHtmlTextView) view.findViewById(R.id.title);
        descript = (NotifyHtmlTextView) view.findViewById(R.id.descript);

        time = (TextView) view.findViewById(R.id.time);
        redPoint = view.findViewById(R.id.redPoint);
    }
}
