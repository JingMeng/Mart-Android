package net.coding.mart;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by chenchao on 16/11/8.
 */

public class MainHighPayHolder extends MainRecyclerviewItemHolder {

    public MainRecyclerviewItemHolder itemHolder;

    ViewGroup highRewardLayout;
    View allHighReward;

    View secion;
    View sectionTop;
    View sectionBottom;

    public MainHighPayHolder(LayoutInflater inflater, ViewGroup parent) {
        this(inflater.inflate(R.layout.main_recycler_view_header, parent, false));
    }

    public MainHighPayHolder(View v) {
        super(v);

        highRewardLayout = (ViewGroup) v.findViewById(R.id.highRewardLayout);
        allHighReward = v.findViewById(R.id.allHighReward);

        secion = v.findViewById(R.id.section);
        sectionTop = v.findViewById(R.id.sectionTop);
        sectionBottom = v.findViewById(R.id.sectionBottom);

        View item = v.findViewById(R.id.item);
        itemHolder = new MainRecyclerviewItemHolder(item);
    }

    public void showSection(boolean show) {
        int visable = show ? View.VISIBLE : View.GONE;
        secion.setVisibility(visable);
        sectionTop.setVisibility(visable);
        sectionBottom.setVisibility(visable);
    }
}
