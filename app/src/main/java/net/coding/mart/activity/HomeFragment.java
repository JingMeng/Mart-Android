package net.coding.mart.activity;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.Holder;

import net.coding.mart.MainAdapter;
import net.coding.mart.R;
import net.coding.mart.WebActivity;
import net.coding.mart.WebActivity_;
import net.coding.mart.activity.reward.HuoguoActivity_;
import net.coding.mart.common.BaseFragment;
import net.coding.mart.common.Global;
import net.coding.mart.common.ImageLoadTool;
import net.coding.mart.common.constant.Progress;
import net.coding.mart.common.htmltext.URLSpanNoUnderline;
import net.coding.mart.common.umeng.UmengEvent;
import net.coding.mart.common.widget.CustomPullRecyclerView;
import net.coding.mart.common.widget.ExplorerRecyclerViewSpace;
import net.coding.mart.job.showcase.IntroduceActivity_;
import net.coding.mart.json.Banner;
import net.coding.mart.json.BaseObserver;
import net.coding.mart.json.MartRequest;
import net.coding.mart.json.Network;
import net.coding.mart.json.Pager;
import net.coding.mart.json.PagerData;
import net.coding.mart.json.RewardsPreview;
import net.coding.mart.json.reward.SimplePublished;
import net.coding.mart.setting.ExampleActivity_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
@EFragment(R.layout.fragment_home)
public class HomeFragment extends BaseFragment {

    @ViewById
    CustomPullRecyclerView recyclerView;

    @ViewById
    ConvenientBanner<Banner> convenientBanner;

    MainAdapter adapter;

    View recyclerHeader;

    List<Banner> bannersData = new ArrayList<>();

    int bannerScrollState = ViewPager.SCROLL_STATE_IDLE;

//    List<SimplePublished> listData = new ArrayList<>();

    PagerData<SimplePublished> pageData = new PagerData<>();

    @AfterViews
    void initExploreFragment() {
        setActionBarTitle("首页");

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new ExplorerRecyclerViewSpace(getActivity()));

        adapter = new MainAdapter(MainFragment.clickRecyclerViewItem, pageData.data);
        recyclerHeader = LayoutInflater.from(getActivity()).inflate(R.layout.explore_head, recyclerView.mRecyclerView, false);
        recyclerHeader.findViewById(R.id.martIntroduce).setOnClickListener(v -> {
            IntroduceActivity_.intent(this).start();
            umengEvent(UmengEvent.ACTION, "首页 _ 码市介绍");

        });
        recyclerHeader.findViewById(R.id.martExample).setOnClickListener(v -> {
            ExampleActivity_.intent(this).start();
            umengEvent(UmengEvent.ACTION, "首页 _ 经典案例");
        });
        recyclerHeader.findViewById(R.id.coderSay).setOnClickListener(v -> {
            codersay();
            umengEvent(UmengEvent.ACTION, "首页 _ 码士说");
        });
        recyclerHeader.findViewById(R.id.phoneMart).setOnClickListener(v -> {
            HuoguoActivity_.intent(this).start();
        });

        convenientBanner = (ConvenientBanner) recyclerHeader.findViewById(R.id.convenientBanner);
        convenientBanner.getViewPager().addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int state) {
                bannerScrollState = state;
            }
        });

        recyclerView.setNormalHeader(recyclerHeader);
        recyclerView.setAdapter(adapter);
        recyclerView.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                loadDataFromNetwork(true);
                loadRewardPreview();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                if (bannerScrollState == ViewPager.SCROLL_STATE_IDLE) {
                    return super.checkCanDoRefresh(frame, content, header);
                } else {
                    return false;
                }
            }
        });

        recyclerView.setOnLoadMoreListener((itemsCount, maxLastVisiblePosition) -> {
//                Log.e("", "load more -----");
            loadDataFromNetwork(false);
        });

        loadDataFromNetwork(true);
        loadBanners();

        convenientBanner.post(() -> {
            int width = convenientBanner.getWidth();
            int needHeight = width * 350 / 750; // 图片长宽比为 750 : 350
            ViewGroup.LayoutParams lp = convenientBanner.getLayoutParams();
            lp.height = needHeight;
            convenientBanner.setLayoutParams(lp);
        });

        convenientBanner.getViewPager().setOnTouchListener(new View.OnTouchListener() {

            MotionEvent eventDown;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    eventDown = event;
                    recyclerView.setEnabled(false);
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    if (eventDown != null) {
                        if (Math.abs(event.getY() - eventDown.getY()) > Math.abs(event.getX() - eventDown.getX())
                                && Math.abs(event.getY() - eventDown.getY()) > 100f) {
                            recyclerView.setEnabled(true);
                        } else {
                            recyclerView.setEnabled(false);
                        }
                        eventDown = null;
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    recyclerView.setEnabled(true);
                }
                return false;
            }
        });

        loadRewardPreview();

    }

    private void loadRewardPreview() {
        Network.getRetrofit(getActivity(), Network.CacheType.useCache)
                .getRewardsPreview()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<RewardsPreview>(getActivity()) {
                    @Override
                    public void onSuccess(RewardsPreview data) {
                        super.onSuccess(data);
                        setHeadItem(R.id.moneyCount, data.validMoneyCount);
                        setHeadItem(R.id.userCount, String.valueOf(data.rewardUserCount));
                        setHeadItem(R.id.rewardCount, String.valueOf(data.rewardCount));
                    }
                });
    }

    private void setHeadItem(int viewId, String text) {
        TextView textView = (TextView) recyclerHeader.findViewById(viewId);
        textView.setText(text);
    }

    private void loadBanners() {
        Network.getRetrofit(getActivity())
                .getBanners()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<List<Banner>>(getActivity()) {
                    @Override
                    public void onSuccess(List<Banner> data) {
                        super.onSuccess(data);
                        bannersData = data;
                        convenientBanner.setPages(() -> new LocalImageHolderView(), bannersData)
                                .setPageIndicator(new int[]{R.mipmap.ic_page_indicator, R.mipmap.ic_page_indicator_focused})
                                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
                    }
                });
    }

    Subscription lastRequest = null;

    private void loadDataFromNetwork(boolean reload) {
        if (lastRequest != null) {
            lastRequest.unsubscribe();
        }

        if (reload) {
            pageData.setPageFirst();
        }

        MartRequest martRequest = Network.getRetrofit(getActivity(), Network.CacheType.useCache);
        lastRequest = martRequest.getRewardPages(-1,
                Progress.recruit.id, -1, pageData.page + 1, 20, 0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<Pager<SimplePublished>>(getActivity()) {
                    @Override
                    public void onSuccess(Pager<SimplePublished> data) {
                        super.onSuccess(data);

                        pageData.addData(data);
                        if (pageData.isLoadAll()) {
                            recyclerView.disableLoadmore();
                        } else {
                            recyclerView.reenableLoadmore();
                        }

                        adapter.notifyDataSetChanged();
                        recyclerView.setRefreshing(false);
                    }

                    @Override
                    public void onFail(int errorCode, @NonNull String error) {
                        super.onFail(errorCode, error);
                        recyclerView.setRefreshing(false);
                    }
                });
        bindSubscription(lastRequest);
    }

    protected void codersay() {
        Intent intent = new Intent(getActivity(), WebActivity_.class);
        intent.putExtra(WebActivity.EXTRA_URL, Global.HOST + "/codersay");
        intent.putExtra(WebActivity.EXTRA_TITLE, "码士说");
        startActivity(intent);
    }

    public class LocalImageHolderView implements Holder<Banner> {

        private ImageView imageView;

        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, int position, Banner data) {
            ImageLoadTool.loadBannerImage(imageView, data.image);
            imageView.setOnClickListener(v -> {
                URLSpanNoUnderline.openActivityByUri(v.getContext(), data.link, true);
            });
        }
    }
}
