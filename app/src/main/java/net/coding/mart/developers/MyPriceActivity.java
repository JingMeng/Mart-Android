package net.coding.mart.developers;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.widget.ListView;

import net.coding.mart.R;
import net.coding.mart.common.BaseActivity;
import net.coding.mart.developers.adapter.MyPriceAdapter;
import net.coding.mart.json.BaseObserver;
import net.coding.mart.json.Network;
import net.coding.mart.json.developer.Datum;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by liu on 16/6/7.
 */
public class MyPriceActivity extends BaseActivity {
    private ListView listView;
    private MyPriceAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_price);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setActionBarTitle("我的报价");
        initView();
        getData(Network.CacheType.onlyCache);
        getData(Network.CacheType.useCache);

    }

    private void getData(Network.CacheType cacheType) {
        Network.getRetrofit(this, cacheType)
                .getPriceList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<List<Datum>>(this) {
                    @Override
                    public void onSuccess(List<Datum> data) {
                        showSending(false);
                        adapter = new MyPriceAdapter(MyPriceActivity.this);
                        adapter.setData(data);
                        listView.setAdapter(adapter);
                    }

                    @Override
                    public void onFail(int errorCode, @NonNull String error) {
                        if (cacheType == Network.CacheType.onlyCache) {
                            return;
                        }

                        super.onFail(errorCode, error);
                        showSending(false);
                    }
                });
        showSending(true);
    }

    private void initView() {
        listView = (ListView) findViewById(R.id.listview);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
