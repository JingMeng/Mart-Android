package net.coding.mart.activity.reward.detail;

import net.coding.mart.databinding.RewardDetailStageItemBinding;
import net.coding.mart.databinding.RewardDetailStageItemOldBinding;
import net.coding.mart.json.reward.Stage;

/**
 * Created by chenchao on 16/5/4.
 */
public interface StageModify {
    void stageSubmitFail(Stage stage, RewardDetailStageItemBinding binding);

    void stageSubmitSuccess(Stage stage, RewardDetailStageItemBinding binding);

    void stageSubmit(Stage stage, RewardDetailStageItemBinding binding);

    void stageSubmitCancel(Stage stage, RewardDetailStageItemBinding binding);

    void stageCheckSubmit();  // 查看交付文档

    void stagePay(Stage stage);

// 没有走 开发宝 的项目
    void stageSubmitFail(Stage stage, RewardDetailStageItemOldBinding binding);

    void stageSubmitSuccess(Stage stage, RewardDetailStageItemOldBinding binding);

    void stageSubmit(Stage stage, RewardDetailStageItemOldBinding binding);

    void stageSubmitCancel(Stage stage, RewardDetailStageItemOldBinding binding);
}
