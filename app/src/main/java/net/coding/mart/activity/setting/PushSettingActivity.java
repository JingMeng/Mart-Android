package net.coding.mart.activity.setting;

import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

import net.coding.mart.MyAsyncHttpClient;
import net.coding.mart.R;
import net.coding.mart.common.BackActivity;
import net.coding.mart.common.Global;
import net.coding.mart.common.MyData;
import net.coding.mart.common.MyJsonResponse;
import net.coding.mart.common.umeng.UmengEvent;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

@EActivity(R.layout.activity_push_setting)
public class PushSettingActivity extends BackActivity {

    private final String PUSH_URL = Global.HOST_API + "/app/setting/notification";

    @ViewById(R.id.pushNew)
    CheckBox mCheckPushNew;
    CompoundButton.OnCheckedChangeListener mClickSetting = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            boolean checked = mCheckPushNew.isChecked();
            int pushFresh = checked ? 1 : 0;
            AsyncHttpClient client = MyAsyncHttpClient.createClient(PushSettingActivity.this);
            RequestParams params = new RequestParams();
            params.put("freshPublished", pushFresh);
            client.post(PushSettingActivity.this, PUSH_URL, params, new MyJsonResponse(PushSettingActivity.this) {
                @Override
                public void onMySuccess(JSONObject response) {
                    MyData.setNeedPush(PushSettingActivity.this, mCheckPushNew.isChecked());
                    super.onMySuccess(response);
                }

                @Override
                public void onMyFailure(JSONObject response) {
                    super.onMyFailure(response);
                    uiBindData();
                }
            });

            umengEvent(UmengEvent.USER_CENTER, "消息推送" + (checked ? "打开" : "关闭"));
        }
    };

    @AfterViews
    void initPushSettingActivty() {
        uiBindData();
        if (MyData.getInstance().isLogin()) {
            updateDataFromService();
        }
    }

    private void uiBindData() {
        mCheckPushNew.setOnCheckedChangeListener(null);
        mCheckPushNew.setChecked(MyData.getNeedPush(this));
        mCheckPushNew.setOnCheckedChangeListener(mClickSetting);
    }

    private void updateDataFromService() {
        AsyncHttpClient client = MyAsyncHttpClient.createClient(this);
        client.get(this, PUSH_URL, new MyJsonResponse(this) {
            @Override
            public void onMySuccess(JSONObject response) {
                super.onMySuccess(response);
                try {
                    int fresh = response.optJSONObject("data").optInt("freshPublished");
                    MyData.setNeedPush(PushSettingActivity.this, fresh == 1);
                } catch (Exception e) {
                    Global.errorLog(e);
                }
                uiBindData();
            }

        });
    }

}
