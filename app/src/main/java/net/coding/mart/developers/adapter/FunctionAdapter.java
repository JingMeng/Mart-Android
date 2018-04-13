package net.coding.mart.developers.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.coding.mart.R;
import net.coding.mart.developers.FunctionListActivity;
import net.coding.mart.developers.fragment.FunctionFragment;
import net.coding.mart.developers.view.PinnedSectionListView;
import net.coding.mart.json.developer.Quotation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liu on 16/5/31.
 */
public class FunctionAdapter extends BaseAdapter implements PinnedSectionListView.PinnedSectionListAdapter {

    private static final int FIRST = 0;
    private static final int SECOND = 1;
    private static final int COUNT = 2;

    private LayoutInflater mInflater;

    private List<Quotation> data = new ArrayList<>();
    private List<Quotation> allData = new ArrayList<>();
    private List<Quotation> pickedMains = new ArrayList<>();

    private FunctionFragment fragment;
    private ItemOnClickListener listener;
    private DelOnClickListener delOnClickListener;
    private AddOnClickListener addOnClickListener;
    private FunctionListActivity activity;

    private List<Quotation> defaultData;

    // Constructors
    public FunctionAdapter(FunctionListActivity activity, FunctionFragment fragment) {
        this.mInflater = LayoutInflater.from(activity);
        this.fragment = fragment;
        this.listener = new ItemOnClickListener();
        this.delOnClickListener = new DelOnClickListener();
        this.addOnClickListener = new AddOnClickListener();
        this.activity = activity;
    }

    public void setData(List<Quotation> data) {
        this.allData.clear();
        if (data != null) {
            allData.addAll(data);
        }

        updateLocalData();
    }

    public void setPickeModels(List<Quotation> list) {
        pickedMains.clear();
        pickedMains.addAll(list);
        updateLocalData();
    }

    private void updateLocalData() {
        data.clear();

        for (int i = 0; i < pickedMains.size(); ++i) {
            Quotation pickedMain = pickedMains.get(i);

            int start = -1;
            int end = -1;
            for (int j = 0; j < allData.size(); ++j) {
                if (allData.get(j) == pickedMain) {
                    start = j;
                    continue;
                }

                if (start != -1) {
                    if (!allData.get(j).isFunction()) {
                        end = j;
                        break;
                    }
                }
            }

            if (start != -1) {
                if (end == -1) {
                    end = allData.size();
                }

                data.addAll(allData.subList(start, end));
            }
        }

        notifyDataSetChanged();
    }

    public int getPosition(Quotation quotation) {
        return data.indexOf(quotation);
    }

    public void setDefaultData(List<Quotation> defaultData) {
        this.defaultData = defaultData;
    }

    @Override
    public int getCount() {
        return data.size();
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
        if (data.get(position).type == 3) {
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
                    convertView = mInflater.inflate(R.layout.list_third_category_head_item, null);
                    holder.tv_head = (TextView) convertView.findViewById(R.id.tv_title);
                    break;

                case SECOND:
                    convertView = mInflater.inflate(R.layout.list_third_category_item, null);
                    holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                    holder.tv_desc = (TextView) convertView.findViewById(R.id.tv_desc);
                    holder.iv_add = (ImageView) convertView.findViewById(R.id.iv_add_car);
                    holder.iv_del = (ImageView) convertView.findViewById(R.id.iv_del_car);
                    holder.tv_num = (TextView) convertView.findViewById(R.id.tv_num);
                    holder.layoutRoot = convertView.findViewById(R.id.layoutRoot);
                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Quotation vo = data.get(position);
        switch (type) {
            case FIRST:
                holder.tv_head.setText(vo.title);
                break;

            case SECOND:
                holder.tv_name.setText(vo.title);
                holder.tv_desc.setText(vo.description);
                if (vo.title.equals("页面数量")) {
                    holder.iv_del.setVisibility(View.VISIBLE);
                    holder.tv_num.setVisibility(View.VISIBLE);
                    holder.iv_add.setImageResource(R.mipmap.developer_car_add);
                    holder.iv_del.setImageResource(R.mipmap.developer_car_cancle);
                    holder.iv_add.setOnClickListener(addOnClickListener);
                    holder.iv_del.setOnClickListener(delOnClickListener);
                    holder.tv_num.setText(String.valueOf(activity.getNum()));
                    holder.layoutRoot.setOnClickListener(null);
                } else {
                    holder.iv_del.setVisibility(View.GONE);
                    holder.tv_num.setVisibility(View.GONE);
                    if (activity.getPickedData().contains(vo)) {
                        holder.iv_add.setImageResource(R.mipmap.developer_car_picked);
                        holder.iv_add.setTag(R.id.action_type, 0);
                        holder.layoutRoot.setTag(R.id.action_type, 0);
                    } else {
                        holder.iv_add.setImageResource(R.mipmap.developer_car_no_pick);
                        holder.iv_add.setTag(R.id.action_type, 1);
                        holder.layoutRoot.setTag(R.id.action_type, 1);
                    }
                    holder.iv_add.setOnClickListener(listener);
                    holder.iv_add.setTag(vo);

                    holder.layoutRoot.setOnClickListener(listener);
                    holder.layoutRoot.setTag(vo);
                }
                break;
        }
        return convertView;
    }

    @Override
    public boolean isItemViewTypePinned(int viewType) {
        return viewType == FIRST;
    }

    private int lastPinned = -1;
    @Override
    public void showPinned(int position) {
        if (lastPinned != position) {
            lastPinned = position;
            fragment.updateCateMark(data.get(position));
        }
    }

    private class ViewHolder {
        TextView tv_name;
        TextView tv_desc;
        TextView tv_head;
        View layoutRoot;
        ImageView iv_add;
        ImageView iv_del;
        TextView tv_num;
    }

    private class ItemOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Quotation quotation = (Quotation) v.getTag();
            int type = (int) v.getTag(R.id.action_type);
            switch (type) {
                case 0:
                    if (defaultData.size() > 0) {
                        fragment.removePickedFunction(quotation);
                        notifyDataSetChanged();
                    }
                    break;
                case 1:
                    fragment.addPickedFunction(quotation);
                    notifyDataSetChanged();
                    break;
            }

        }
    }

    private class DelOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (activity.getNum() < 1) {
                return;
            }
            int num = activity.getNum() - 1;
            activity.setNum(num);
            notifyDataSetChanged();
        }
    }

    private class AddOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int num = activity.getNum() + 1;
            activity.setNum(num);
            notifyDataSetChanged();
        }
    }

}
