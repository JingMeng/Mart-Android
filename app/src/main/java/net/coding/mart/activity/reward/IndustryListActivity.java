package net.coding.mart.activity.reward;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import net.coding.mart.R;
import net.coding.mart.common.BackActivity;
import net.coding.mart.json.reward.IndustryName;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

@EActivity(R.layout.activity_industry_list)
public class IndustryListActivity extends BackActivity {

    @Extra
    ArrayList<IndustryName> allIndustrys;

    @Extra
    ArrayList<IndustryName> pickedIndustrys;

    @ViewById
    ListView listView;

    private ArrayAdapter<IndustryName> adapter;

    @AfterViews
    void initIndustryListActivity() {
        ArrayList<IndustryName> oldPick = pickedIndustrys;
        pickedIndustrys = new ArrayList<>();
        for (IndustryName item : allIndustrys) {
            for (IndustryName pick : oldPick) {
                if (pick.id == item.id) {
                    pickedIndustrys.add(item);
                }
            }
        }
        adapter = new ArrayAdapter<IndustryName>(this, R.layout.industry_list_item, allIndustrys) {
            @NonNull
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ViewHolder holder;
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.industry_list_item, parent, false);
                    holder = new ViewHolder(convertView);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }

                IndustryName data = allIndustrys.get(position);
                holder.name.setText(data.name);
                holder.picked.setVisibility(pickedIndustrys.contains(data) ? View.VISIBLE : View.INVISIBLE);

                return convertView;
            }
        };

        View header = getLayoutInflater().inflate(R.layout.divide_10_top, listView, false);
        listView.addHeaderView(header, null, false);
        View footer = getLayoutInflater().inflate(R.layout.divide_10_bottom, listView, false);
        listView.addFooterView(footer, null, false);

        listView.setAdapter(adapter);
    }

    @ItemClick
    public void listViewItemClicked(IndustryName item) {
        if (pickedIndustrys.contains(item)) {
            pickedIndustrys.remove(item);
        } else {
            if (pickedIndustrys.size() >= 3) {
                showMiddleToast("最多选择 3 个");
            } else {
                pickedIndustrys.add(item);
            }
        }
        adapter.notifyDataSetChanged();
    }

    static class ViewHolder {
        TextView name;
        View picked;

        ViewHolder(View view) {
            name = (TextView) view.findViewById(R.id.name);
            picked = view.findViewById(R.id.selectIcon);
        }
    }

    @Click
    void sendButton() {
        Intent intent = new Intent();
        intent.putExtra("intentData", pickedIndustrys);
        setResult(RESULT_OK, intent);
        finish();
    }
}
