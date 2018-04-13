package net.coding.mart.activity.user;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

import net.coding.mart.R;
import net.coding.mart.common.BackActivity;
import net.coding.mart.common.constant.DeveloperType;
import net.coding.mart.common.widget.EmptyRecyclerView;
import net.coding.mart.json.BaseObserver;
import net.coding.mart.json.Network;
import net.coding.mart.json.user.UserExtraProjectExp;
import net.coding.mart.json.user.UserExtraRole;
import net.coding.mart.login.LoginActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@EActivity(R.layout.activity_user_skill2)
public class UserSkillActivity extends BackActivity implements ModifyDataAction {

    private static final int RESULT_MODIFY_ROLE = 1;
    private static final int RESULT_MODIFY_PROJECT_EXP = 2;

    @ViewById
    UltimateRecyclerView recyclerView;

    @ViewById
    EmptyRecyclerView emptyView;

    UserSkillAdapter adapter;

    List<UserExtraRole> userRoleData = new ArrayList<>();

    List<UserExtraRole> allRoleData = new ArrayList<>();
    private List<UserExtraProjectExp> projectExps;

    boolean loadingUserRoleSuccess = false;
    boolean loadingUserProjectExp = false;
    boolean loadingUser = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateRewardRole(DeveloperType developerType) {
        if (developerType != null) {
            loadUserData();
        }
    }

    @AfterViews
    void initUserSkillActivity() {
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);

        adapter = new UserSkillAdapter(this);
        recyclerView.setAdapter(adapter);

//        recyclerView.addIt

//        loadData(Network.CacheType.onlyCache);
        loadData(Network.CacheType.noCache);

        loadAllRoleTypes(false);

        recyclerView.setVisibility(View.INVISIBLE);
        emptyView.initFail("点击重试", v -> loadData(Network.CacheType.noCache));
        emptyView.setLoading();
    }

    @OnActivityResult(RESULT_MODIFY_ROLE)
    void onResultModify(int resultCode) {
        if (resultCode == Activity.RESULT_OK) {
            loadRoleData(Network.CacheType.useCache);
        }
    }

    @OnActivityResult(RESULT_MODIFY_PROJECT_EXP)
    void onResultModifyProjectExp(int resultCode) {
        if (resultCode == Activity.RESULT_OK) {
            loadProjectExpData(Network.CacheType.useCache);
        }
    }

    private void loadData(Network.CacheType type) {
        loadRoleData(type);
        loadProjectExpData(type);
        loadUserData();
    }

    private void loadRoleData(Network.CacheType type) {
        Network.getRetrofit(this, type)
                .getUserRoles()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<List<UserExtraRole>>(this) {
                    @Override
                    public void onSuccess(List<UserExtraRole> data) {
                        adapter.setUserRoles(data);
                        userRoleData = data;

                        loadingUserRoleSuccess = true;

                        updateLoading();
                    }

                    @Override
                    public void onFail(int errorCode, @NonNull String error) {
                        super.onFail(errorCode, error);

                        updateLoading();
                    }
                });
    }

    private void loadUserData() {
        LoginActivity.loadCurrentUser(this, new LoginActivity.LoadUserCallback() {
            @Override
            public void onSuccess() {

                adapter.setUser();
                loadingUser = true;
                updateLoading();
            }

            @Override
            public void onFail() {

            }
        });

    }

    private void loadProjectExpData(Network.CacheType type) {
        Network.getRetrofit(this, type)
                .getUserProjectExp()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<List<UserExtraProjectExp>>(this) {
                    @Override
                    public void onSuccess(List<UserExtraProjectExp> data) {
                        adapter.setUserProjectExps(data);
                        projectExps = data;

                        loadingUserProjectExp = true;
                        updateLoading();
                    }

                    @Override
                    public void onFail(int errorCode, @NonNull String error) {
                        super.onFail(errorCode, error);

                        updateLoading();
                    }
                });
    }

    private void updateLoading() {
        if (recyclerView.getVisibility() == View.VISIBLE) {
            return;
        }

        if (loadingUserRoleSuccess && loadingUserProjectExp && loadingUser) {
            List<Integer> list = new ArrayList<>();
            list.add(1);
            emptyView.setLoadingSuccess(list);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            emptyView.setLoadingFail(new ArrayList<Integer>());
        }
    }

    private void loadAllRoleTypes(boolean showLoading) {
        Network.getRetrofit(this)
                .getAllRoleTypes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<List<UserExtraRole>>(this) {
                    @Override
                    public void onSuccess(List<UserExtraRole> data) {
                        allRoleData = data;

                        if (showLoading) {
                            showSending(false, "");
                            addOneRole();
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

    private void startAddRoleActivity() {
        if (allRoleData.isEmpty()) {
            loadAllRoleTypes(true);
            return;
        }

        addOneRole();
    }

    private void addOneRole() {
        String[] items = new String[allRoleData.size()];
        for (int i = 0; i < allRoleData.size(); ++i) {
            items[i] = allRoleData.get(i).role.name;
        }

        ArrayList<String> pickTypes = createAllPickTypes();

        RoleAdapter adapter = new RoleAdapter(this, 0, allRoleData);
        View layout = getLayoutInflater().inflate(R.layout.user_skill_pick_role, null);
        ListView listView = (ListView) layout.findViewById(R.id.listView);
        listView.setAdapter(adapter);

        AlertDialog popDialog = new AlertDialog.Builder(this)
                .setView(layout)
                .show();

        listView.setOnItemClickListener((parent, view, position, id) -> {
            popDialog.dismiss();
            UserExtraRole role = allRoleData.get(position);
            pickTypes.add(String.valueOf(role.role.id));
            AddRoleActivity_.intent(this)
                    .userExtraRole(role)
                    .allType(pickTypes)
                    .newRole(true)
                    .startForResult(RESULT_MODIFY_ROLE);
        });
    }

    class RoleAdapter extends ArrayAdapter<UserExtraRole> {

        public RoleAdapter(Context context, int resource, List<UserExtraRole> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.user_skill_list_role_pick, parent, false);
            }

            TextView textView = (TextView) convertView.findViewById(R.id.text1);
            textView.setText(getItem(position).role.name);
            if (isRolePicked(getItem(position))) {
                textView.setTextColor(0xff999999);
            } else {
                textView.setTextColor(0xff666666);
            }

            return convertView;
        }

        @Override
        public boolean isEnabled(int position) {
            return !isRolePicked(getItem(position));
        }
    }

    private boolean isRolePicked(UserExtraRole role) {
        for (UserExtraRole item : userRoleData) {
            if (role.role.id == item.role.id) {
                return true;
            }
        }

        return false;
    }

    @NonNull
    private ArrayList<String> createAllPickTypes() {
        ArrayList<String> pickTypes = new ArrayList<>();
        for (UserExtraRole item : userRoleData) {
            pickTypes.add(String.valueOf(item.userRole.roleId));
        }
        return pickTypes;
    }

    enum Skills {
        RoleHeader, Role, ProjectExpHeader, ProjectExp, UserStatus
    }

    @Override
    public void addRole() {
        startAddRoleActivity();
    }

    @Override
    public void modifyRole(UserExtraRole role) {
        AddRoleActivity_.intent(this)
                .newRole(false)
                .allType(createAllPickTypes())
                .userExtraRole(role)
                .startForResult(RESULT_MODIFY_ROLE);
    }

    @Override
    public void addProjectExp() {
        AddProjectExpActivity_.intent(this).startForResult(RESULT_MODIFY_PROJECT_EXP);
    }

    @Override
    public void modifyProjectExp(UserExtraProjectExp projectExp) {
        AddProjectExpActivity_.intent(this)
                .projectExp(projectExp)
                .startForResult(RESULT_MODIFY_PROJECT_EXP);
    }


}
