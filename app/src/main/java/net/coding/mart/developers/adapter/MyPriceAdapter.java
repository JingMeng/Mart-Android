package net.coding.mart.developers.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.coding.mart.R;
import net.coding.mart.common.ImageLoadTool;
import net.coding.mart.developers.FunctionActivity;
import net.coding.mart.developers.MyPriceActivity;
import net.coding.mart.json.developer.Datum;

import java.util.List;

/**
 * Created by liu on 16/6/7.
 */
public class MyPriceAdapter extends BaseAdapter {
    private MyPriceActivity activity;
    private List<Datum> data;
    private ItemOnClickListener listener;

    public MyPriceAdapter(MyPriceActivity activity) {
        this.activity = activity;
        this.listener = new ItemOnClickListener();
    }

    public void setData(List<Datum> data) {
        this.data = data;
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

    private static class ViewHolder {
        public final View rootView;
        public final TextView tvNum;
        public final ImageView ivImg;
        public final TextView tvName;
        public final TextView tvPrice;
        public final TextView tvTime;
        public final TextView btnOk;

        private ViewHolder(View rootView) {
            this.rootView = rootView;
            this.tvNum = (TextView) rootView.findViewById(R.id.tv_num);
            this.ivImg = (ImageView) rootView.findViewById(R.id.iv_img);
            this.tvName = (TextView) rootView.findViewById(R.id.tv_name);
            this.tvPrice = (TextView) rootView.findViewById(R.id.tv_price);
            this.tvTime = (TextView) rootView.findViewById(R.id.tv_time);
            this.btnOk = (TextView) rootView.findViewById(R.id.btn_ok);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        if (convertView == null) {
            View view = LayoutInflater.from(activity).inflate(R.layout.list_myprice_item, parent, false);
            vh = new ViewHolder(view);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        Datum vo = data.get(position);
        vh.tvNum.setText(vo.platformCount + "个平台， " + vo.moduleCount + "个功能模块");
        vh.tvName.setText(vo.name);
        String priceStr = "预估报价：" + vo.fromPrice + " - " + vo.toPrice + " 元";
        ForegroundColorSpan defSpan = new ForegroundColorSpan(0xff999999);
        ForegroundColorSpan blueSpan = new ForegroundColorSpan(0xffF5A623);
        SpannableStringBuilder builderTime = new SpannableStringBuilder(priceStr);
        builderTime.setSpan(defSpan, 0, ("预估报价：").length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builderTime.setSpan(blueSpan, ("预估报价：").length(), priceStr.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builderTime.setSpan(defSpan, priceStr.length() - 1, priceStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        vh.tvPrice.setText(builderTime);
        String timeStr = "预估周期： " + vo.fromTerm + " - " + vo.toTerm + " 工作日";
        vh.tvTime.setText(timeStr);
        vh.btnOk.setTag(vo);
        vh.btnOk.setOnClickListener(listener);
        return vh.rootView;
    }

    private class ItemOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Datum vo = (Datum) v.getTag();
            int id = vo.id;
            Intent intent = new Intent(activity, FunctionActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("id", id);
            bundle.putInt("type", 1);
            intent.putExtras(bundle);
            activity.startActivity(intent);
        }
    }
}
