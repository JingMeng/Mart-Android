package net.coding.mart.developers.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;

import net.coding.mart.developers.FunctionListActivity;
import net.coding.mart.developers.fragment.FunctionFragment;
import net.coding.mart.json.developer.Quotation;

import java.util.List;

/**
 * Created by liu on 16/6/2.
 */
public class FunctionViewAdapter extends FragmentStatePagerAdapter {
//    private List<View> views = null;
    private List<Quotation> data = null;
    private FunctionListActivity activity;

    public FunctionViewAdapter(FunctionListActivity activity, List<Quotation> data) {
        super(activity.getSupportFragmentManager());
        this.activity = activity;
        this.data = data;
//        views = new ArrayList<>();
//        for (int i = 0; i < data.size(); i++) {
//            FunctionFragment fragment = new FunctionFragment(activity, data.get(i));
//            views.add(fragment);
//        }
    }

//    public void setData(List<Quotation> data) {
//        this.data = data;
//        views.clear();
//        for (int i = 0; i < data.size(); i++) {
//            FunctionFragment fragment = new FunctionFragment(activity, data.get(i));
//            views.add(fragment);
//        }
//
//        notifyDataSetChanged();
//    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return data.get(position).title;
    }

    @Override
    public Fragment getItem(int position) {
        FunctionFragment fragment = new FunctionFragment();
//        fragment.setArg(activity, data.get(position));
        fragment.setArg(activity, position);
        return fragment;
    }
}
