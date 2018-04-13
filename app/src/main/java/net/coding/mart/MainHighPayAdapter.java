package net.coding.mart;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.coding.mart.common.CommonActivity;
import net.coding.mart.common.CommonActivity_;
import net.coding.mart.common.Global;
import net.coding.mart.common.ImageLoadTool;
import net.coding.mart.json.reward.SimplePublished;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenchao on 16/11/8.
 */
public class MainHighPayAdapter extends MainAdapter {

    private static final int HIGH_TYPE = 10;
    private static final int HIGH_TYPE_POS = 2;
    private static final int HIGH_TYPE_MAX = 4;

    ArrayList<SimplePublished> highPayRewards = new ArrayList<>();

    LayoutInflater inflater;

    public void setHighPayRewards(List<SimplePublished> highPayRewards) {
        this.highPayRewards.clear();

        if (highPayRewards != null) {
            this.highPayRewards.addAll(highPayRewards);
        }
    }

    public MainHighPayAdapter(View.OnClickListener itemClick, List<SimplePublished> list, LayoutInflater inflater) {
        super(itemClick, list);
        this.inflater = inflater;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == HIGH_TYPE_POS) {
            return HIGH_TYPE;
        }

        return super.getItemViewType(position);
    }

    @Override
    public MainRecyclerviewItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HIGH_TYPE) {
            MainHighPayHolder holder = new MainHighPayHolder(LayoutInflater.from(parent.getContext()), parent);
            holder.allHighReward.setOnClickListener(clickAllHighReward);
            holder.itemHolder.rootLayout.setOnClickListener(itemClick);
            return holder;
        }

        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(MainRecyclerviewItemHolder holder, int position) {
        if (getItemViewType(position) == HIGH_TYPE) {
            if (holder instanceof MainHighPayHolder) {
                MainHighPayHolder mainHighPayHolder = (MainHighPayHolder) holder;
                mainHighPayHolder.highRewardLayout.removeAllViews();

                if (highPayRewards.isEmpty()) {
                    mainHighPayHolder.showSection(false);
                } else {
                    mainHighPayHolder.showSection(true);
                    for (int i = 0; i < highPayRewards.size() && i < HIGH_TYPE_MAX; ++i) {
                        SimplePublished itemData = highPayRewards.get(i);

                        View item = inflater.inflate(R.layout.main_recylcer_view_header_item, mainHighPayHolder.highRewardLayout, false);
                        ImageView icon = (ImageView) item.findViewById(R.id.icon);
                        TextView title = (TextView) item.findViewById(R.id.name);
                        TextView price = (TextView) item.findViewById(R.id.price);
                        TextView id = (TextView) item.findViewById(R.id.id);

                        ImageLoadTool.loadImage(icon, itemData.cover);
                        title.setText(itemData.name);
                        price.setText(itemData.getPriceString());
                        id.setText(Global.generateRewardIdString(itemData.id));
                        item.setOnClickListener(itemClick);
                        item.setTag(itemData);

                        mainHighPayHolder.highRewardLayout.addView(item);
                    }
                }

                SimplePublished itemData = getDataForPosition(position);
                bindItem(mainHighPayHolder.itemHolder, itemData);
                mainHighPayHolder.itemHolder.rootLayout.setTag(itemData);
            }
            return;
        }
        super.onBindViewHolder(holder, position);
    }

    private View.OnClickListener clickAllHighReward = v -> {
        CommonActivity_.intent(v.getContext()).type(CommonActivity.Type.HIGH_PAY).start();
    };
}
