package net.coding.mart.setting;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.astuetz.PagerSlidingTabStrip;

import net.coding.mart.MyAsyncHttpClient;
import net.coding.mart.R;
import net.coding.mart.common.BackActivity;
import net.coding.mart.common.Global;
import net.coding.mart.common.MyJsonResponse;
import net.coding.mart.common.constant.Constant;
import net.coding.mart.common.widget.EmptyRecyclerView;
import net.coding.mart.json.Example;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringArrayRes;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

@EActivity(R.layout.activity_example)
public class ExampleActivity extends BackActivity {

    @StringArrayRes
    String[] EXAMPLE_TYPES;

    @ViewById
    ViewPager viewPager;

    @ViewById
    PagerSlidingTabStrip tabs;

    @ViewById
    EmptyRecyclerView emptyView;

    private ArrayList<Example> data = new ArrayList<>();
    private ExampleAdapter adapter;

    @AfterViews
    void initExampleActivity() {
        adapter = new ExampleAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabs.setViewPager(viewPager);

        getData();

        emptyView.initFail("点击重试", v -> getData());
        emptyView.setLoading();
    }

    public void getData() {
        String url = Global.HOST_API + "/cases?v=2";
        MyAsyncHttpClient.get(this, url, new MyJsonResponse(this) {
            @Override
            public void onMySuccess(JSONObject response) {
                super.onMySuccess(response);

                JSONArray jsonArray = response.optJSONArray("data");
                for (int i = 0; i < jsonArray.length(); ++i) {
                    data.add(new Example(jsonArray.optJSONObject(i)));
                }

                adapter.notifyDataSetChanged();
                emptyView.setLoadingSuccess(data);
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                emptyView.setLoadingFail(data);
            }
        });
    }

    private class ExampleAdapter extends FragmentStatePagerAdapter {

        public ExampleAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            ArrayList<Example> classifyData = new ArrayList<>();
            if (position == 0) {
                for (Example item : data) {
                    classifyData.add(item);
                }
            } else {
                if (position < Constant.sAllJobTypesId.length) {
                    String idString = Constant.sAllJobTypesId[position];
                    int pickId = Integer.valueOf(idString);
                    for (Example item : data) {
                        if (item.getType_id() == pickId) {
                            classifyData.add(item);
                        }
                    }
                }
            }
            return ExampleFragment_.builder().data(classifyData).build();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return EXAMPLE_TYPES[position];
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            return EXAMPLE_TYPES.length;
        }
    }

}
