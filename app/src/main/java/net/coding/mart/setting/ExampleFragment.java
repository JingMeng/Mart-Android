package net.coding.mart.setting;


import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import net.coding.mart.R;
import net.coding.mart.WebActivity_;
import net.coding.mart.common.BaseFragment;
import net.coding.mart.common.Global;
import net.coding.mart.json.Example;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.DimensionPixelSizeRes;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
@EFragment(R.layout.fragment_example)
public class ExampleFragment extends BaseFragment {


    @FragmentArg
    ArrayList<Example> data;

//    @ViewById
//    UltimateRecyclerView recyclerView;

    //    @ViewById
//    ListView listView;
    @ViewById
    RecyclerView recyclerView;

    @DimensionPixelSizeRes
    int recycler_space;

    @AfterViews
    void initEampleFragment() {
        recyclerView.addItemDecoration(new RecyclerViewSapce(recycler_space));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ExampleAdapter adapter = new ExampleAdapter(data, v -> {
            Example data = (Example) v.getTag();
            String url = String.format("%s/cases/%s", Global.HOST, data.getReward_id());
            WebActivity_.intent(this)
                    .url(url)
                    .start();

        });
        recyclerView.setAdapter(adapter);
    }


//    @AfterViews
//    void initEampleFragment() {
////        listView.setAdapter(adapter);
//    }

//    BaseAdapter adapter = new BaseAdapter() {
//        @Override
//        public int getCount() {
//            return data.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return data.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            if (convertView == null) {
//                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_example_wrap, parent, false);
//                convertView.setOnClickListener(clickListItem);
//            }
//
//            ExampleListItem_ listItem = ((ExampleListItem_) convertView);
//            listItem.setData(data.get(position));
//
//            return convertView;
//        }
//    };
//
//    View.OnClickListener clickListItem = v -> {
//        Example example = (Certificate) v.getTag();
//        showMiddleToast(example.getInner_link().get(0));
//    };


}
