package net.coding.mart.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import net.coding.mart.R;
import net.coding.mart.common.BackActivity;
import net.coding.mart.json.BaseObserver;
import net.coding.mart.json.Network;
import net.coding.mart.json.mart2.user.City;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PickLocalActivity extends BackActivity {

    public static final String EXTRA_LOCAL = "EXTRA_LOCAL";
    public static final String EXTRA_LIST_DATA = "EXTRA_LIST_DATA";
    public static final String EXTRA_LOCAL_POS = "EXTRA_LOCAL_POS";

    private static final int RESULT_LOCAL = 1;

    ListView listView;

    SetAccountActivity.Local mLocal;
    private int mPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_local);

        listView = (ListView) findViewById(R.id.listView);
        mLocal = (SetAccountActivity.Local) getIntent().getSerializableExtra(EXTRA_LOCAL);
        ArrayList<City> listData = (ArrayList<City>) getIntent().getSerializableExtra(EXTRA_LIST_DATA);
        mPos = getIntent().getIntExtra(EXTRA_LOCAL_POS, 1);
        ListAdapter mAdapter = new CityAdapter(this, R.layout.list_item_city, listData);
        View head = LayoutInflater.from(this).inflate(R.layout.list_item_city_head, null);
        listView.addHeaderView(head, null, false);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(mItemClick);
    }

    private void pickFinish() {
        if (mPos == 2) {
            mLocal.district.clear();
        } else if (mPos == 1) {
            mLocal.district.clear();
            mLocal.city.clear();
        }

        Intent intent = new Intent();
        intent.putExtra("result", mLocal);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_LOCAL) {
            if (resultCode == RESULT_OK) {
                setResult(resultCode, data);
                finish();
            }
        }
    }

    AdapterView.OnItemClickListener mItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            City data = (City) parent.getItemAtPosition(position);
            if (mPos >= 3) {
                mLocal.district = data;
                pickFinish();
            } else {
                if (mPos == 1) {
                    mLocal.provicen = data;
                } else if (mPos == 2) {
                    mLocal.city = data;
                }

//                AsyncHttpClient client = MyAsyncHttpClient.createClient(PickLocalActivity.this);
//                String host = String.format(Global.HOST_API + "/region?parent=%d&level=%d", data.getId(), mPos + 1);
//                client.get(host, new MyJsonResponse(PickLocalActivity.this) {
//                    @Override
//                    public void onMySuccess(JSONObject response) {
//                        super.onMySuccess(response);
//                        ArrayList<SetAccountActivity.City> citys = PickLocalActivity.citysFromJson(response);
//
//                    }
//
//                    @Override
//                    public void onMyFailure(JSONObject response) {
//                        super.onMyFailure(response);
//                    }
//                });

                Network.getRetrofit(PickLocalActivity.this)
                        .getCity(data.id, mPos + 1)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new BaseObserver<List<City>>(PickLocalActivity.this) {
                            @Override
                            public void onSuccess(List<City> data) {
                                super.onSuccess(data);
                                if (data.isEmpty()) {
                                    pickFinish();
                                } else {
                                    Intent intent = new Intent(PickLocalActivity.this, PickLocalActivity.class);
                                    intent.putExtra(PickLocalActivity.EXTRA_LOCAL_POS, mPos + 1);
                                    intent.putExtra(PickLocalActivity.EXTRA_LIST_DATA, (ArrayList<City>)data);
                                    intent.putExtra(PickLocalActivity.EXTRA_LOCAL, mLocal);
                                    startActivityForResult(intent, RESULT_LOCAL);
                                }

                                showSending(false, "");
                            }

                            @Override
                            public void onFail(int errorCode, @NonNull String error) {
                                super.onFail(errorCode, error);
                                showSending(false, "");
                            }
                        });

                showSending(true, "");


            }
        }
    };

    static class CityAdapter extends ArrayAdapter<City> {
        public CityAdapter(Context context, int resource, ArrayList<City> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_city, null);
            }

            City data = getItem(position);
            TextView tv = (TextView) convertView.findViewById(R.id.text1);
            tv.setText(data.name);

            return convertView;
        }
    }
}
