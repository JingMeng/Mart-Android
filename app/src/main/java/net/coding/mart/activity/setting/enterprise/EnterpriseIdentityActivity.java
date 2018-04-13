package net.coding.mart.activity.setting.enterprise;

import android.support.v4.app.Fragment;
import android.view.View;

import net.coding.mart.R;
import net.coding.mart.common.BackActivity;
import net.coding.mart.common.widget.LoadingView;
import net.coding.mart.json.Network;
import net.coding.mart.json.NewBaseObserver;
import net.coding.mart.json.enterprise.EnterpriseCertificate;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@EActivity(R.layout.activity_enterprise_identity)
public class EnterpriseIdentityActivity extends BackActivity {

    @ViewById
    LoadingView loading;

    @AfterViews
    void initEnterpriseIdentityActivity() {
        Network.getRetrofit(this)
                .getEnterpriseCertificate()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NewBaseObserver<EnterpriseCertificate>(this) {

                    @Override
                    public void onSuccess(EnterpriseCertificate data) {
                        super.onSuccess(data);

                        switchFragment(data);
                    }

                });
    }

    public void switchFragment(EnterpriseCertificate data) {
        loading.setVisibility(View.GONE);

        Fragment fragment = null;
        if (data != null) {
            switch (data.status) {
                case "processing":
                case "successful":
                case "failed":
                    fragment = EnterpriceIdentityResultFragment_.builder().info(data).build();
                    break;
            }
        }

        if (fragment == null) {
            fragment = EnterpriceIdentitySubmitFragment_.builder().build();
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

}
