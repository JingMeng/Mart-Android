package net.coding.mart.activity.reward.detail.v2;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import net.coding.mart.R;
import net.coding.mart.common.BackActivity;
import net.coding.mart.common.ImageLoadTool;
import net.coding.mart.databinding.CoderDemandBinding;
import net.coding.mart.json.mart2.user.MartUser;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;

/**
 * Created by chenchao on 2017/10/24.
 */

@EActivity(R.layout.activity_v2_demand_detail)
public class V2DemandDetailActivity extends BackActivity {

    @Extra
    MartUser owner;

    @AfterViews
    void initV2DemandDetailActivity() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
        setActionBarTitle("需求方资料");

        CoderDemandBinding binding = CoderDemandBinding.bind(findViewById(R.id.rootLayout));
        binding.setData(owner);
        ImageLoadTool.loadImage(binding.userIcon, owner.avatar);
    }
}
