package net.coding.mart;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MainRecyclerviewItemHolder extends RecyclerView.ViewHolder {
    public ImageView icon;
    public TextView id;
    public TextView name;
    public TextView progress;
    public TextView type;
    public TextView roly;
    public TextView money;
    public TextView signUp;
    public View rootLayout;
    public View iconHighPay;
    public View highPayText;

    public MainRecyclerviewItemHolder(LayoutInflater inflater, ViewGroup parent) {
        this(inflater.inflate(R.layout.main_recyclerview_item, parent, false));
    }

    public MainRecyclerviewItemHolder(View v) {
        super(v);
        rootLayout = v;
        icon = (ImageView) v.findViewById(R.id.icon);
        id = (TextView) v.findViewById(R.id.id);
        name = (TextView) v.findViewById(R.id.name);
        progress = (TextView) v.findViewById(R.id.progress);
        type = (TextView) v.findViewById(R.id.type);
        roly = (TextView) v.findViewById(R.id.roly);
        money = (TextView) v.findViewById(R.id.money);
        signUp = (TextView) v.findViewById(R.id.signUp);
        iconHighPay = v.findViewById(R.id.iconHighPay);
        highPayText = v.findViewById(R.id.highPayText);
    }
}
