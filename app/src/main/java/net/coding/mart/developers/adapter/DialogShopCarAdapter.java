package net.coding.mart.developers.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.coding.mart.R;
import net.coding.mart.developers.FunctionListActivity;
import net.coding.mart.developers.dialog.ShopCarDialog;
import net.coding.mart.json.developer.Quotation;

import java.util.List;

/**
 * Created by liu on 16/6/2.
 */
public class DialogShopCarAdapter extends BaseAdapter {
    private static final int FIRST = 0;
    private static final int SECOND = 1;
    private static final int COUNT = 2;
    private FunctionListActivity activity;
    private List<Quotation> data;
    private LayoutInflater mInflater;

    private DelOnClickListener delOnClickListener;
    private AddDelOnClickListener addDelOnClickListener;
    private ShopCarDialog dialog;

    public DialogShopCarAdapter(FunctionListActivity activity, ShopCarDialog dialog) {
        this.activity = activity;
        mInflater = LayoutInflater.from(activity);
        this.dialog = dialog;
        this.delOnClickListener = new DelOnClickListener();
        this.addDelOnClickListener = new AddDelOnClickListener();
    }

    public void setData(List<Quotation> data) {
        this.data = data;
    }

    public List<Quotation> getData() {
        return data;
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
    public int getItemViewType(int position) {
        if (data.get(position).type == 1) {
            return FIRST;
        } else {
            return SECOND;
        }
    }

    @Override
    public int getViewTypeCount() {
        return COUNT;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        int type = getItemViewType(position);
        if (convertView == null) {
            holder = new ViewHolder();
            switch (type) {
                case FIRST:
                    convertView = mInflater.inflate(R.layout.list_shop_car_head_item, null);
                    holder.tv_head = (TextView) convertView.findViewById(R.id.tv_head);
                    holder.clearAll = convertView.findViewById(R.id.tv_clear);
                    holder.reset = convertView.findViewById(R.id.tv_reset);
                    holder.clearAll.setOnClickListener(clickClear);
                    holder.reset.setOnClickListener(clickReset);
                    break;

                case SECOND:
                    convertView = mInflater.inflate(R.layout.list_shop_car_item, null);
                    holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                    holder.ivAddCar = (ImageView) convertView.findViewById(R.id.iv_add_car);
                    holder.ivDelCar = (ImageView) convertView.findViewById(R.id.iv_del_car);
                    holder.tvNum = (TextView) convertView.findViewById(R.id.tv_num);
                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Quotation quotation = data.get(position);
        switch (type) {
            case FIRST:
                String sectionTitle = String.format("%s . %d", quotation.title, quotation.getExtra().size());
                holder.tv_head.setText(sectionTitle);
                holder.clearAll.setTag(quotation);
                holder.reset.setTag(quotation);
                break;

            case SECOND:
                holder.tv_name.setText(quotation.title);
                if (quotation.title.equals("页面数量")) {
                    holder.ivDelCar.setVisibility(View.VISIBLE);
                    holder.tvNum.setVisibility(View.VISIBLE);
                    holder.ivAddCar.setImageResource(R.mipmap.developer_car_add);
                    holder.ivDelCar.setImageResource(R.mipmap.developer_car_cancle);
                    holder.tvNum.setText(activity.getNum() + "");
                    //加数量
                    holder.ivAddCar.setTag(R.id.action_type, 0);
                    holder.ivAddCar.setOnClickListener(addDelOnClickListener);
                    //减数量
                    holder.ivDelCar.setTag(R.id.action_type, 1);
                    holder.ivDelCar.setOnClickListener(addDelOnClickListener);
                } else {
                    holder.ivDelCar.setVisibility(View.GONE);
                    holder.tvNum.setVisibility(View.GONE);
                    holder.ivAddCar.setImageResource(R.mipmap.developer_car_picked);
                    holder.ivAddCar.setTag(quotation);
                    holder.ivAddCar.setOnClickListener(delOnClickListener);
                }

                break;
        }

        return convertView;
    }

    private class ViewHolder {
        TextView tv_head;

        View reset;
        View clearAll;

        TextView tv_name;
        ImageView ivAddCar;
        ImageView ivDelCar;
        TextView tvNum;
    }

    private View.OnClickListener clickClear = v -> {
        Quotation tag = (Quotation) v.getTag();
        activity.clearPickedFunction(tag);

        dialog.updateNum();
        notifyDataSetChanged();
    };

    private View.OnClickListener clickReset = v -> {
        Quotation tag = (Quotation) v.getTag();
        activity.resetPickedFunction(tag);

        dialog.updateNum();
        notifyDataSetChanged();
    };

    private class DelOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Quotation quotation = (Quotation) v.getTag();
            if (quotation.isRadioItem()) {
                return;
            }

            int pos = data.indexOf(quotation);
            if (pos != -1) {
                data.remove(quotation);
                Quotation platform = null;
                for (int i = pos - 1; i >= 0; --i) {
                    Quotation quotation1 = data.get(i);
                    if (quotation1.isPlatform()) {
                        platform = quotation1;
                        break;
                    }
                }

                if (platform != null) {
                    platform.getExtra().remove(quotation);
                }

                dialog.updateNum();
                notifyDataSetChanged();
            }
        }
    }

    private class AddDelOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int type = (int) v.getTag(R.id.action_type);
            switch (type) {
                case 0: {
                    int num = activity.getNum();
                    num++;
                    activity.setNum(num);
                    dialog.updateNum();
                    notifyDataSetChanged();
                }
                break;

                case 1: {
                    int num = activity.getNum();
                    if (num < 1) {
                        return;
                    }
                    num--;
                    activity.setNum(num);
                    dialog.updateNum();
                    notifyDataSetChanged();
                }
                break;
            }
        }
    }


}
