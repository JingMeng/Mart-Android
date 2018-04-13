package net.coding.mart.activity.reward.detail.v2.phase;

import net.coding.mart.R;
import net.coding.mart.activity.reward.detail.v2.V2CoderJudegeActivity_;
import net.coding.mart.common.BackActivity;
import net.coding.mart.databinding.PhaseDetailActivityBinding;
import net.coding.mart.json.v2.phase.Phase;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;

/**
 * Created by chenchao on 2017/10/24.
 */
@EActivity(R.layout.phase_detail_activity)
public class PhaseDetailActivity extends BackActivity {

    @Extra
    Phase phase;

    @AfterViews
    void initPhaseDetailActivity() {
        setActionBarTitle(phase.phaseNo);

        PhaseDetailActivityBinding binding = PhaseDetailActivityBinding.bind(findViewById(R.id.rootLayout));
        binding.setData(phase);

        if (phase.evaluation != null && !phase.evaluation.rateIsEmpty()) {
            binding.stars.setRating(Float.valueOf(phase.evaluation.averageRate));
            binding.starsText.setText(phase.evaluation.averageRate);
            binding.starLayout.setOnClickListener(v -> {
                V2CoderJudegeActivity_.intent(this)
                        .data(phase.evaluation)
                        .start();
            });
        } else {
            binding.stars.setRating(0);
            binding.starsText.setText("未评分");
        }
    }
}
