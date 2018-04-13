package net.coding.mart.developers.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.coding.mart.R;
import net.coding.mart.developers.FunctionListActivity;
import net.coding.mart.developers.fragment.FunctionFragment;
import net.coding.mart.developers.view.PinnedSectionListView;
import net.coding.mart.json.developer.Quotation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liu on 16/5/30.
 */
public class ModuleAdapter extends BaseAdapter implements PinnedSectionListView.PinnedSectionListAdapter {

    private static final int FIRST = 0;
    private static final int SECOND = 1;
    private static final int COUNT = 2;
    private FunctionListActivity activity;

    private List<Quotation> data = new ArrayList<>();
    private Quotation pickedMain = new Quotation();

    private List<Quotation> allData = new ArrayList<>();

    private LayoutInflater mInflater;
    private ItemOnClickListener listener;
    private FunctionFragment fragment;
    private Quotation markCode = new Quotation();

    private boolean twoLine = true;

    public ModuleAdapter(FunctionListActivity activity, FunctionFragment fragment) {
        this.activity = activity;
        mInflater = LayoutInflater.from(activity);
        this.listener = new ItemOnClickListener();
        this.fragment = fragment;
    }

    public void setTwoLine(boolean twoLine) {
        this.twoLine = twoLine;
        notifyDataSetChanged();
    }

    public void setMarkCode(Quotation markCode) {
        this.markCode = markCode;
        notifyDataSetChanged();
    }

    public void setMarkCodeEmpty() {
        this.markCode = new Quotation();
        notifyDataSetChanged();
    }

    public void setPickedCategory(Quotation quotation) {
        pickedMain = quotation;
        updateLocalData();
    }

    public List<Quotation> getPiceDatas() {
        return data;
    }

    public void setData(List<Quotation> data) {
        this.allData.clear();
        if (data != null) {
            pickedMain = data.get(0);
            this.allData.addAll(data);
        }
        updateLocalData();
    }

    private void updateLocalData() {
        data.clear();
        int start = -1;
        int end = -1;
        for (int i = 0; i < allData.size(); ++i) {
            if (allData.get(i) == pickedMain) {
                start = i + 1;
                continue;
            }

            if (start != -1) {
                if (!allData.get(i).isModule()) {
                    end = i;
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

        notifyDataSetChanged();
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
    public int getViewTypeCount() {
        return COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        if (data.get(position).type == 2) {
            return FIRST;
        } else {
            return SECOND;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        int type = getItemViewType(position);
        if (convertView == null) {
            holder = new ViewHolder();
            switch (type) {
                case FIRST:
                    convertView = mInflater.inflate(R.layout.list_child_category_head_item, parent, false);
                    holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);

                    break;

                case SECOND:
                    convertView = mInflater.inflate(R.layout.list_child_category_item, parent, false);
                    holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Quotation vo = data.get(position);
        switch (type) {
            case FIRST:
                holder.tv_title.setText(vo.title);
                break;

            case SECOND:
                if (twoLine) {
                    holder.tv_name.setMaxLines(2);
                } else {
                    holder.tv_name.setMaxLines(1);
                }

                holder.tv_name.setText(vo.title);
                holder.tv_name.setOnClickListener(listener);
                holder.tv_name.setTag(vo);
                if (vo == markCode) {
                    holder.tv_name.setBackgroundColor(0xffffffff);
                } else {
                    holder.tv_name.setBackgroundColor(0);
                }
                break;
        }
        return convertView;
    }


    private class ViewHolder {
        TextView tv_name;
        TextView tv_title;
    }

    @Override
    public boolean isItemViewTypePinned(int viewType) {
        return viewType == FIRST;
    }

    @Override
    public void showPinned(int position) {
        fragment.updateMark(position);
    }


    private class ItemOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Quotation vo = (Quotation) v.getTag();
            if (!fragment.isShowThird()) {
                fragment.showThird();
            }
            markCode = vo;
            fragment.categorySelect(vo);
            notifyDataSetChanged();
        }
    }
}
