package net.coding.mart.activity.user;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import net.coding.mart.R;
import net.coding.mart.activity.reward.IndustryListActivity_;
import net.coding.mart.common.BackActivity;
import net.coding.mart.common.widget.DatePickerFragment;
import net.coding.mart.common.widget.SimpleTextWatcher;
import net.coding.mart.common.widget.WordCountEditText;
import net.coding.mart.json.BaseHttpResult;
import net.coding.mart.json.File;
import net.coding.mart.json.Network;
import net.coding.mart.json.NewBaseObserver;
import net.coding.mart.json.SimpleObserver;
import net.coding.mart.json.reward.IndustryName;
import net.coding.mart.json.reward.IndustryPager;
import net.coding.mart.json.user.UserExtraProjectExp;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@EActivity(R.layout.activity_add_project_exp)
public class AddProjectExpActivity extends BackActivity implements DatePickerFragment.DateSet {

    private static final int RESULT_INDUSTRY = 1;
    final String[] ProjectTypes = new String[]{
            "Web 网站",
            "APP 开发",
            "微信公众号",
            "HTML5 应用",
            "其它",
    };
    @Extra
    UserExtraProjectExp projectExp;
    PostParam newParam;
    PostParam oldParam;
    @ViewById
    EditText name, link;
    @ViewById
    WordCountEditText duty, description;
    @ViewById
    TextView startTime, endTime, jobIndustryTextView, jobTypeTextView;
    @ViewById
    View sendButton;
    @ViewById
    ViewGroup fileLayout;
    private ArrayList<IndustryName> allIndustrys = new ArrayList<>();

    @AfterViews
    void initAddProjectExpActivity() {
        if (projectExp != null) {
            newParam = new PostParam(projectExp);
            oldParam = new PostParam(projectExp);

            addFileItem();
        } else {
            newParam = new PostParam();
            oldParam = new PostParam();
        }

        description.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                newParam.description = s.toString();
            }
        });

        duty.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                newParam.duty = s.toString();
            }
        });

        uiBindData();
        loadIndustry(false);
    }

    private void addFileItem() {
        if (projectExp == null || projectExp.files == null) {
            return;
        }

        fileLayout.setVisibility(View.VISIBLE);
        for (File item : projectExp.files) {
            View view = getLayoutInflater().inflate(R.layout.project_exp_file, fileLayout, false);
            ((TextView) view.findViewById(R.id.fileName)).setText(item.filename);
            fileLayout.addView(view);
        }
    }

    @Click
    void startTime() {
        DialogFragment newFragment = new DatePickerFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(DatePickerFragment.PARAM_MAX_TODYA, false);
        bundle.putBoolean(DatePickerFragment.PARAM_START, true);
        bundle.putString(DatePickerFragment.PARAM_DATA, newParam.startTime);
        newFragment.setArguments(bundle);
        newFragment.setCancelable(true);
        newFragment.show(getSupportFragmentManager(), "datePicker");
        getSupportFragmentManager().executePendingTransactions();
    }

    private void jumpToIndustryListActivity() {
        IndustryListActivity_.intent(this)
                .allIndustrys(allIndustrys)
                .pickedIndustrys(newParam.industrys)
                .startForResult(RESULT_INDUSTRY);
    }

    private void loadIndustry(boolean jumpToList) {
        Network.getRetrofit(this)
                .getRewardIndustry()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NewBaseObserver<IndustryPager>(this) {
                    @Override
                    public void onSuccess(IndustryPager data) {
                        super.onSuccess(data);

                        allIndustrys.clear();
                        allIndustrys.addAll(data.industryName);

                        if (jumpToList) {
                            jumpToIndustryListActivity();
                        }
                    }

                });
    }

    @Click
    void endTime() {
        DialogFragment newFragment = new DatePickerFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(DatePickerFragment.PARAM_MAX_TODYA, false);
        bundle.putString(DatePickerFragment.PARAM_DATA, newParam.finishTime);
        bundle.putBoolean(DatePickerFragment.PARAM_START, false);
        bundle.putBoolean(DatePickerFragment.PARAM_UNTIL_NOW, newParam.until_now);
        newFragment.setArguments(bundle);
        newFragment.setCancelable(true);
        newFragment.show(getSupportFragmentManager(), "datePicker");
        getSupportFragmentManager().executePendingTransactions();
    }

    @Click
    void jobIndustryTextView() {
        if (allIndustrys.isEmpty()) {
            loadIndustry(true);
        } else {
            IndustryListActivity_.intent(this)
                    .allIndustrys(allIndustrys)
                    .pickedIndustrys(newParam.industrys)
                    .startForResult(RESULT_INDUSTRY);

        }
    }

    void bindIndustryUI() {
        jobIndustryTextView.setText(IndustryName.createName(newParam.industrys));
    }

    @OnActivityResult(RESULT_INDUSTRY)
    void onResultIndustry(int resultCode, @OnActivityResult.Extra ArrayList<IndustryName> intentData) {
        if (resultCode == RESULT_OK && intentData != null) {
            newParam.industrys.clear();
            newParam.industrys.addAll(intentData);

            bindIndustryUI();
        }
    }

    @TextChange
    void name(TextView editText) {
        newParam.projectName = editText.getText().toString();
    }

    @TextChange
    void link(TextView text) {
        newParam.link = text.getText().toString();
    }

    private void uiBindData() {
        name.setText(newParam.projectName);
        startTime.setText(newParam.startTime);
        if (newParam.until_now) {
            endTime.setText("至今");
        } else {
            endTime.setText(newParam.finishTime);
        }
        description.setText(newParam.description);
        duty.setText(newParam.duty);
        link.setText(newParam.link);

        bindProjectType();

        bindIndustryUI();
    }

    private void bindProjectType() {
        String project = "";
        int realPos = newParam.jobType - 1;
        if (0 <= realPos && realPos < ProjectTypes.length) {
            project = ProjectTypes[realPos];
        }
        jobTypeTextView.setText(project);
    }

    @Override
    public void dateSetResult(String date, boolean isStart, boolean untilNow) {
        if (isStart) {
            newParam.startTime = date;
        } else {
            newParam.until_now = untilNow;
            newParam.finishTime = date;
        }

        uiBindData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (projectExp != null) {
            getMenuInflater().inflate(R.menu.add_role, menu);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @OptionsItem
    void action_delete() {
        new AlertDialog.Builder(this)
                .setMessage("确定删除项目经验?删除后无法恢复。")
                .setPositiveButton("删除", (dialog, which) -> deleteProjectExp())
                .setNegativeButton("取消", null)
                .show();
    }

    private void deleteProjectExp() {
        Network.getRetrofit(this)
                .deleteProjectExp(projectExp.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleObserver(this) {
                    @Override
                    public void onSuccess() {
                        super.onSuccess();
                        setResult(RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onFail(int errorCode, @NonNull String error) {
                        super.onFail(errorCode, error);
                        showSending(false, "");
                    }
                });
        showSending(true, "删除中.. .");
    }

    @Click
    void jobTypeTextView() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("项目类型")
                .setItems(ProjectTypes, (dialog, which) -> {
                    String title = ProjectTypes[which];
                    newParam.jobType = which + 1;
                    bindProjectType();
                });
        builder.show();
    }

    @Click
    void sendButton() {
        int reelProjectTypeParam = newParam.jobType - 1;

        if (newParam.projectName.length() < 1) {
            showMiddleToast("请填写项目名称");
            return;
        } else if (30 < newParam.projectName.length()) {
            showMiddleToast("项目名称不能多于 30 个字");
            return;
        } else if (reelProjectTypeParam < 0 || ProjectTypes.length <= reelProjectTypeParam) {
            showMiddleToast("请选择项目类型");
            return;
        } else if (newParam.startTime.isEmpty()) {
            showMiddleToast("请选择项目起始时间");
            return;
        } else if (newParam.finishTime.isEmpty() && !newParam.until_now) {
            showMiddleToast("请选择项目结束时间");
            return;
        } else if (!newParam.until_now && 0 > newParam.finishTime.compareTo(newParam.startTime)) {
            showMiddleToast("结束时间不能早于开始时间");
            return;
        } else if (newParam.description.length() < 100) {
            showMiddleToast("项目描述不能少于 100 字");
            return;
        } else if (newParam.industrys.isEmpty()) {
            showMiddleToast("请选择行业");
            return;
        } else if (2000 < newParam.description.length()) {
            showMiddleToast("项目描述不能多于 2000 字");
            return;
        } else if (newParam.duty.length() < 50) {
            showMiddleToast("我的职责不能少于 50 字");
            return;
        } else if (1000 < newParam.duty.length()) {
            showMiddleToast("我的职责不能多于 1000 字");
            return;
        }

        Network.getRetrofit(this)
                .addProjectExp(newParam.getParamMap(), newParam.getParamFile())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NewBaseObserver<BaseHttpResult>(this) {

                    @Override
                    public void onSuccess(BaseHttpResult data) {
                        super.onSuccess(data);
                        setResult(RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onFail(int errorCode, @NonNull String error) {
                        super.onFail(errorCode, error);
                        showSending(false, "");
                    }
                });

        showSending(true, "");
    }

    private static class PostParam {
        String projectName; //aaa
        String startTime = ""; //2016-03-30
        String finishTime = ""; //2016-04-14
        boolean until_now;
        String description = ""; //bbbbbabbbbbabbbbbabbbbbabbbbbabbbbbabbbbbabbbbbabbbbbabbbbbabbbbbabbbbbabbbbbabbbbbabbbbbabbbbbabbbbbabbbbbabbbbbabbbbbabbbbbabbbbbabbbbbabbbbbabbbbbabbbbbabbbbbabbbbbabbbbbabbbbbabbbbbabbbbba
        String duty = ""; //aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
        String link = ""; //llllllll
        int id;
        ArrayList<File> ids = new ArrayList<>();
        ArrayList<IndustryName> industrys = new ArrayList<>();
        int jobType = -1;


        public PostParam(UserExtraProjectExp exp) {
            id = exp.id;
            projectName = exp.projectName;
            startTime = exp.startTimeNumerical;
            finishTime = exp.finishTimeNumerical;
            until_now = exp.untilNow;
            description = exp.description;
            duty = exp.duty;
            link = exp.link;
            for (File file : exp.files) {
                ids.add(file);
            }

            industrys.addAll(exp.getIndustrys());
            jobType = exp.projectType;
        }

        public PostParam() {
        }

        Map<String, String> getParamMap() {
            HashMap<String, String> map = new HashMap<>();
            if (id != 0) {
                map.put("id", String.valueOf(id));
            }
            map.put("project_name", projectName);
            map.put("start_time", startTime);
            map.put("finish_time", finishTime);
            map.put("until_now", String.valueOf(until_now));
            map.put("description", description);
            map.put("duty", duty);
            map.put("link", link);
            map.put("project_type", String.valueOf(jobType));
            map.put("industry", IndustryName.createIdString(industrys));

            return map;
        }

        ArrayList<String> getParamFile() {
            ArrayList<String> list = new ArrayList<>();
            for (File item : ids) {
                list.add(String.valueOf(item.id));
            }
            return list;
        }
    }


}
