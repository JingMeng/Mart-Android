package net.coding.mart.job;

import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

import net.coding.mart.R;
import net.coding.mart.common.BackActivity;
import net.coding.mart.common.CommonExtra;
import net.coding.mart.common.Global;
import net.coding.mart.common.MyData;
import net.coding.mart.common.util.RxBus;
import net.coding.mart.common.widget.WordCountEditText;
import net.coding.mart.json.BaseHttpResult;
import net.coding.mart.json.BaseObserver;
import net.coding.mart.json.JobDetail;
import net.coding.mart.json.Network;
import net.coding.mart.json.NewBaseObserver;
import net.coding.mart.json.RoleType;
import net.coding.mart.json.reward.ApplyResumeList;
import net.coding.mart.json.reward.JoinJob;
import net.coding.mart.json.reward.RewardApply;
import net.coding.mart.json.user.UserExtraProjectExp;
import net.coding.mart.json.user.UserExtraRole;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@EActivity(R.layout.activity_join)
public class JoinActivity extends BackActivity {

    private final int RESULT_SELECT_PROJECT = 1;

    @Extra
    JobDetail mDatum;

    @Extra
    CommonExtra.JumpParam mNeedJump;

    @Extra
    JoinJob mJoinJob;

    @ViewById
    TextView roleTypes, projects;

    @ViewById
    WordCountEditText content;
    String[] TYPES;
    private JumpParam1 mJumpParam;
    private List<UserExtraRole> allRole = new ArrayList<>();
    private List<UserExtraRole> selectRole = new ArrayList<>();
    private List<UserExtraRole> tempPickRole = new ArrayList<>();
    private ArrayList<Integer> selectProjects = new ArrayList<>();
    //    EditText mEditText;
    private TextView jobRole;
    private RewardApply mRewardApply;
    private Param mParam = new Param(-1, "");
    View.OnClickListener mClickType = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            AlertDialog.Builder builder = new AlertDialog.Builder(JoinActivity.this);
            builder.setTitle("请选择")
                    .setItems(TYPES, (dialog, which) -> {
                        mParam.setRolePos(which);
                        jobRole.setText(TYPES[mParam.getRolePos()]);
                    });
            builder.show();
        }
    };

    @AfterViews
    protected void initJoinActivity() {
        if (mDatum != null) {
            mJumpParam = new JumpParam1(mDatum.reward.id, mDatum.roleTypes);
        } else if (mJoinJob != null) {
            mJumpParam = new JumpParam1(mJoinJob.id, mJoinJob.roleTypes);
        }

        TYPES = new String[mJumpParam.getRoleTypes().size()];
        for (int i = 0; i < TYPES.length; ++i) {
            TYPES[i] = mJumpParam.getRoleTypes().get(i).name;
        }

        jobRole = (TextView) findViewById(R.id.jobRole);
        jobRole.setOnClickListener(mClickType);

        loadDataFromService();
    }

    private void loadDataFromService() {
        Network.getRetrofit(this)
                .getRewardApply(mJumpParam.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<RewardApply>(this) {
                    @Override
                    public void onSuccess(RewardApply data) {
                        super.onSuccess(data);

                        mRewardApply = data;
                        int pos = -1;
                        ArrayList<RoleType> roleTypes = mJumpParam.getRoleTypes();
                        for (int i = 0; i < roleTypes.size(); ++i) {
                            if (mRewardApply.roleTypeId == roleTypes.get(i).id) {
                                pos = i;
                            }
                        }

                        for (ApplyResumeList item : mRewardApply.applyResumeList) {
                            if (item.isProject()) {
                                selectProjects.add(item.targetId);
                            }
                        }
                        updateSelctProjects();

                        mParam = new Param(pos, mRewardApply.message);
                        uiBindData();
                        loadAllRoleTypes(false);

                    }

                    @Override
                    public void onFail(int errorCode, @NonNull String error) {
                        if (errorCode == 1) { // 没有填写过会返回1
                            loadProjectExpData();
                        } else {
                            super.onFail(errorCode, error);
                        }
                    }
                });
    }

    private void uiBindData() {
        int rolePos = mParam.getRolePos();
        if (0 <= rolePos && rolePos < TYPES.length) {
            jobRole.setText(TYPES[rolePos]);
        }

        content.setText(mParam.getMessage());
    }

    private void updateSelectRole() {
        StringBuffer buffer = new StringBuffer();
        boolean isFirst = true;
        for (UserExtraRole item : selectRole) {
            if (!isFirst) {
                isFirst = false;
                buffer.append(", ");
            }

            buffer.append(item.role.name);
        }
        roleTypes.setText(buffer);
    }

    private void updateSelctProjects() {
        int projectCount = selectProjects.size();
        String projectCountString = "";
        if (projectCount > 0) {
            projectCountString = String.format("%s 个项目", projectCount);
        }
        projects.setText(projectCountString);
    }

    @Click
    void roleTypes() {
        if (allRole.isEmpty()) {
            loadAllRoleTypes(true);
        } else {
            popSelectRoleDialog();
        }
    }

    @Click
    void projects() {
        ProjectExpListActivity_.intent(this).selectProjectIds(selectProjects)
                .startForResult(RESULT_SELECT_PROJECT);
    }


    private void loadProjectExpData() {
        if (mParam != null && !TextUtils.isEmpty(mParam.getMessage())) { // 说明是新建，默认选择所有项目†
            return;
        }

        Network.getRetrofit(this)
                .getUserProjectExp()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<List<UserExtraProjectExp>>(this) {
                    @Override
                    public void onSuccess(List<UserExtraProjectExp> data) {
                        super.onSuccess(data);

                        for (UserExtraProjectExp item : data) {
                            selectProjects.add(item.id);
                        }
                        updateSelctProjects();
                    }

                });
    }


    @Click
    void joinButton() {
        dataBindUI();
        String inputString = mParam.getMessage();
        if (inputString.length() < 10) {
            showMiddleToast("胜任原因不能少于 10 个字");
            return;
        } else if (300 < inputString.length()) {
            showMiddleToast("胜任原因不能多于 300 个字");
            return;
        }

        if (!mParam.isPickType()) {
            showMiddleToast("请选择你的报名角色");
            return;
        }

        showSending(true, "保存中...");

        ArrayList<Integer> roles = new ArrayList<>();
        for (UserExtraRole item : selectRole) {
            roles.add(item.role.id);
        }

        ArrayList<Integer> projects = new ArrayList<>();
        for (Integer item : selectProjects) {
            projects.add(item);
        }

        Network.getRetrofit(JoinActivity.this)
                .joinReward(mJumpParam.getId(),
                        inputString,
                        1,
                        mJumpParam.getRoleId(mParam.getRolePos()),
                        roles,
                        projects)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NewBaseObserver<BaseHttpResult>(JoinActivity.this) {
                    @Override
                    public void onSuccess(BaseHttpResult data) {
                        super.onSuccess(data);
                        showButtomToast("参与成功，审核中");
                        MyData myData = MyData.getInstance();
                        if (myData.getData().getJoinCount() <= 0) {
                            myData.getData().setJoinCount(1);
                            myData.save(JoinActivity.this);

                            RxBus.getInstance().send(new RxBus.RewardPublishSuccess());
                        }

                        setResult(RESULT_OK);
                        finish();

                        showSending(false, "");
                    }

                    @Override
                    public void onFail(int errorCode, @NonNull String error) {
                        super.onFail(errorCode, error);
                        showSending(false, "");
                    }
                });
    }

    @OnActivityResult(RESULT_SELECT_PROJECT)
    void onResultSelectProject(int resultCode, @OnActivityResult.Extra ArrayList<Integer> resultData) {
        if (resultCode == RESULT_OK && resultData != null) {
            selectProjects.clear();
            selectProjects.addAll(resultData);
            updateSelctProjects();
        }
    }

    private void popSelectRoleDialog() {
        tempPickRole.clear();
        tempPickRole.addAll(selectRole);
        ArrayAdapter skillAdapter = new ArrayAdapter<UserExtraRole>(this, R.layout.list_item_pick_skill,
                R.id.text1, allRole) {

            @NonNull
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                CheckedTextView checkedTextView = (CheckedTextView) v.findViewById(R.id.text1);
                UserExtraRole data = allRole.get(position);
                checkedTextView.setChecked(tempPickRole.contains(data));
                checkedTextView.setText(data.role.name);

                return v;
            }
        };

        AdapterView.OnItemClickListener clickListItem = (parent, view, position, id) -> {
            UserExtraRole data = allRole.get(position);
            if (tempPickRole.contains(data)) {
                tempPickRole.remove(data);
            } else {
                tempPickRole.add(data);
            }

            ((BaseAdapter) parent.getAdapter()).notifyDataSetChanged();
        };

        View.OnClickListener clickOk = v -> {
            selectRole.clear();
            selectRole.addAll(tempPickRole);
            updateSelectRole();
        };

        Global.popMulSelectDialog(this, "选择要显示角色", skillAdapter, clickListItem, clickOk);
    }

    private void loadAllRoleTypes(boolean showLoading) {
        Network.getRetrofit(this)
                .getAllRoleTypes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<List<UserExtraRole>>(this) {
                    @Override
                    public void onSuccess(List<UserExtraRole> data) {
                        allRole.clear();
                        for (UserExtraRole item : data) {
                            if (item.role.selected) {
                                allRole.add(item);
                            }
                        }

                        selectRole.clear();
                        if (mRewardApply != null) {
                            for (UserExtraRole item : allRole) {
                                for (ApplyResumeList apply : mRewardApply.applyResumeList) {
                                    if (apply.isRole() && apply.targetId == item.role.id) {
                                        selectRole.add(item);
                                        break;
                                    }
                                }
                            }
                        }
                        updateSelectRole();

                        if (showLoading) {
                            showSending(false, "");
                            popSelectRoleDialog();
                        }

                    }

                    @Override
                    public void onFail(int errorCode, @NonNull String error) {
                        if (showLoading) {
                            super.onFail(errorCode, error);
                            showSending(false, "");
                        }
                    }
                });
        if (showLoading) {
            showSending(true, "");
        }
    }

    private void dataBindUI() {
        mParam.setMessage(content.getText());
    }

    private static class Param {
        private String message;
        private int rolePos = -1;

        Param(int roleType, String message) {
            this.rolePos = roleType;
            this.message = message;
        }

        boolean isPickType() {
            return rolePos != -1;
        }

        String getMessage() {
            return message;
        }

        void setMessage(String message) {
            this.message = message;
        }

        int getRolePos() {
            return rolePos;
        }

        void setRolePos(int rolePos) {
            this.rolePos = rolePos;
        }
    }

    private static class JumpParam1 {

        int mId;
        ArrayList<RoleType> roleTypes = new ArrayList<>();

        JumpParam1(int id, List<RoleType> list) {
            mId = id;
            roleTypes.clear();
            for (RoleType item : list) {
                if (!item.completed) {
                    roleTypes.add(item);
                }
            }
        }

        public ArrayList<RoleType> getRoleTypes() {
            return roleTypes;
        }

        int getRoleId(int pos) {
            return getRoleTypes().get(pos).id;
        }

        int getId() {
            return mId;
        }
    }
}
