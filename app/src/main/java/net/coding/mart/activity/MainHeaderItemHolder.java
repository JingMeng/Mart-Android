package net.coding.mart.activity;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.coding.mart.R;

public class MainHeaderItemHolder extends RecyclerView.ViewHolder {
    public ImageView icon;
    public TextView name;
    public TextView price;

    public MainHeaderItemHolder(LayoutInflater inflater, ViewGroup parent) {
        this(inflater.inflate(R.layout.main_recylcer_view_header_item, parent, false));
    }

    public MainHeaderItemHolder(View view) {
        super(view);
        icon = (ImageView) view.findViewById(R.id.icon);
        name = (TextView) view.findViewById(R.id.name);
        price = (TextView) view.findViewById(R.id.price);
    }

}
