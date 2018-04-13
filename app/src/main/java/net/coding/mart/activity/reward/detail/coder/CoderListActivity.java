package net.coding.mart.activity.reward.detail.coder;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

import net.coding.mart.R;
import net.coding.mart.activity.reward.detail.v2.V2RewardDetailActivity;
import net.coding.mart.common.BackActivity;
import net.coding.mart.common.GlobalData_;
import net.coding.mart.common.space.RecyclerViewSpace;
import net.coding.mart.json.reward.Coder;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.ArrayList;

@EActivity(R.layout.activity_coder_list)
public class CoderListActivity extends BackActivity implements ApplyCallback {

    private static final int RESULT_CODER_DETAIL = 1;

    @Extra
    ArrayList<Coder> coders;

    @Extra
    int rewardId;

    @ViewById
    UltimateRecyclerView recyclerView;

    @Pref
    GlobalData_ globalData;

    CoderAdapter coderAdapter;

    @AfterViews
    void initCoderListActivity() {
        sortCoders();

        setActionBarTitle(coders.get(0).roleType);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new RecyclerViewSpace(this));

        coderAdapter = new CoderAdapter(coders, this, clickListItem,
                clickAccept, clickRefuse);
        recyclerView.setAdapter(coderAdapter);
    }

    private void sortCoders() {
        Coder martDirect = null;
        ArrayList<Coder> collectCoders = new ArrayList<>();
        ArrayList<Coder> normalCoders = new ArrayList<>();
        ArrayList<Coder> refuseCoders = new ArrayList<>();
        final String martEnterprise = globalData.enterpriseGK().get();

        for (Coder item : coders) {
            if (item.isRefuse()) {
                refuseCoders.add(item);
            } else {
                if (item.globalKey.equalsIgnoreCase(martEnterprise)) { // 码市直营排第一位
                    martDirect = item;
                } else if (item.isExcellent()) {
                    collectCoders.add(item);
                } else {
                    normalCoders.add(item);
                }
            }
        }

        coders.clear();
        if (martDirect != null) {
            coders.add(martDirect);
        }
        coders.addAll(collectCoders);
        coders.addAll(normalCoders);
        coders.addAll(refuseCoders);
    }

    View.OnClickListener clickAccept = v -> {
        Coder coder = (Coder) v.getTag();
        CoderInfoActivity.popAcceptDialog(this, this, coder);
    };

    View.OnClickListener clickRefuse = v -> {
        Coder coder = (Coder) v.getTag();
        CoderInfoActivity.popRefuseDialog(this, this, coder);
    };

    View.OnClickListener clickListItem = v -> {
        Coder coder = (Coder) v.getTag();
        jumpToDetail(coder);
    };

    @Override
    public void applyAccept(Coder coder) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(V2RewardDetailActivity.resultExtraRefrush, true);
        setResult(RESULT_OK, resultIntent);

        finish();
    }

    private void jumpToDetail(Coder coder) {
        CoderInfoActivity_.intent(this)
                .rewardId(rewardId)
                .coder(coder)
                .menuShowUserList(false)
                .coders(coders)
                .startForResult(RESULT_CODER_DETAIL);
    }

    @OnActivityResult(RESULT_CODER_DETAIL)
    void onResultCoder(int resultCode, @OnActivityResult.Extra ArrayList<Coder> coders,
                       @OnActivityResult.Extra boolean resultExtraRefrush) {
        if (resultCode == RESULT_OK) {
            if (resultExtraRefrush) {
                Intent intent = new Intent();
                intent.putExtra(V2RewardDetailActivity.resultExtraRefrush, true);
                setResult(RESULT_OK, intent);
                finish();
            } else if (coders != null) {
                this.coders.clear();
                this.coders.addAll(coders);
                sortCoders();
                coderAdapter.notifyDataSetChanged();

                Intent intent = new Intent();
                intent.putExtra("coders", coders);
                setResult(RESULT_OK, intent);
            }
        }
    }

    @Override
    public void applyReject(Coder coder) {
        sortCoders();
        coderAdapter.notifyDataSetChanged();

        Intent resultIntent = new Intent();
        resultIntent.putExtra("coders", coders);
        setResult(RESULT_OK, resultIntent);

    }
}
