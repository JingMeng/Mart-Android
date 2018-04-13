package net.coding.mart.job.showcase;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;

import net.coding.mart.R;
import net.coding.mart.common.BackActivity;
import net.coding.mart.setting.AboutActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringArrayRes;

@EActivity(R.layout.activity_introduce)
public class IntroduceActivity extends BackActivity {

    @StringArrayRes
    String[] INTRODUCE_PAGES_TITLE;

    @ViewById
    ViewPager viewPager;

    @ViewById
    PagerSlidingTabStrip tabs;

    @AfterViews
    void initIntroduceActivity() {
        PagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                final int[] layoutIds = new int[]{
                        R.layout.introduce_page_0,
                        R.layout.introduce_page_1,
                        R.layout.introduce_page_2
                };
                Fragment fragment = new IntroduceFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("data", layoutIds[position]);
                fragment.setArguments(bundle);
                return fragment;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return INTRODUCE_PAGES_TITLE[position];
            }

            @Override
            public int getCount() {
                return INTRODUCE_PAGES_TITLE.length;
            }
        };
        viewPager.setAdapter(adapter);
        tabs.setViewPager(viewPager);
    }

    public static class IntroduceFragment extends Fragment {
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            int resId = getArguments().getInt("data");
            View v = inflater.inflate(resId, container, false);
            if (resId == R.layout.introduce_page_2) {
                v.findViewById(R.id.phone).setOnClickListener(v1 -> AboutActivity.callPhone(getActivity()));
                v.findViewById(R.id.email).setOnClickListener(v1 -> AboutActivity.contactByEmail(getActivity()));
            }

            return v;
        }
    }
}
