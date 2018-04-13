package net.coding.mart.activity.reward.detail.v2;

import com.willy.ratingbar.BaseRatingBar;

import net.coding.mart.R;
import net.coding.mart.common.BackActivity;
import net.coding.mart.databinding.CoderDetailJudgeActivityBinding;
import net.coding.mart.json.v2.phase.Evaluation;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;

@EActivity(R.layout.coder_detail_judge_activity)
public class V2CoderJudegeActivity extends BackActivity {

    @Extra
    Evaluation data;

    @AfterViews
    void initV2CoderJudegeActivity() {
        CoderDetailJudgeActivityBinding binding = CoderDetailJudgeActivityBinding.bind(findViewById(R.id.rootLayout));
        binding.setData(data);

        setRate(binding.starsResponsibility, data.responsibilityRate);
        setRate(binding.starsCommunication, data.communicationRate);
        setRate(binding.starsDeliverability, data.deliverabilityRate);
    }

    private void setRate(BaseRatingBar star, String rate) {
        float com = 0;
        try {
            com = Float.valueOf(rate);
        } catch (Exception e) {

        }
        star.setRating(com);
    }
}
