package net.coding.mart.activity.guide;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.coding.mart.R;


public class LayoutAdapter extends RecyclerView.Adapter<LayoutAdapter.SimpleViewHolder> {

    private static final int[] GUIDE_LAYOUTS = new int[] {
            R.layout.fragment_guide_0,
            R.layout.fragment_guide_1,
            R.layout.fragment_guide_2,
            R.layout.fragment_guide_3,
    };

    public static int getPagerCount() {
        return GUIDE_LAYOUTS.length;
    }

    private final Context mContext;

    public LayoutAdapter(Context context) {
        mContext = context;
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = GUIDE_LAYOUTS[0];
        if (0 <= viewType && viewType < GUIDE_LAYOUTS.length) {
            layoutId = GUIDE_LAYOUTS[viewType];
        }

        final View view = LayoutInflater.from(mContext).inflate(layoutId, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return GUIDE_LAYOUTS.length;
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, int position) {
    }

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        public SimpleViewHolder(View view) {
            super(view);
        }
    }
}
