package net.coding.mart.activity.user;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marshalchen.ultimaterecyclerview.UltimateDifferentViewTypeAdapter;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;

import net.coding.mart.R;
import net.coding.mart.json.user.UserExtraProjectExp;
import net.coding.mart.json.user.UserExtraRole;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by chenchao on 16/4/13.
 */
public class UserSkillAdapter extends UltimateDifferentViewTypeAdapter<UserSkillActivity.Skills> {

    private final RoleTitleBinder roleTitleBinder;
    private final RoleBinder roleBinder;

    private final ProjectExpHeaderBinder projectExpHeaderBinder;
    private final ProjectExpBinder projectExpBinder;

    private final UserStatus userStatus;

    List<UserExtraRole> mRoles = new ArrayList<>();
    List<UserExtraProjectExp> mProjectExps = new ArrayList<>();

    public void setUserRoles(List<UserExtraRole> roles) {
        mRoles = roles;
        roleBinder.setmData(roles);
        roleTitleBinder.setmData(roles);
        projectExpHeaderBinder.setmData(mProjectExps, mRoles);
        projectExpBinder.setmData(mProjectExps, mRoles);
        notifyDataSetChanged();
    }

    public void setUserProjectExps(List<UserExtraProjectExp> projectExps) {
        mProjectExps = projectExps;
        projectExpBinder.setmData(mProjectExps, mRoles);
        projectExpHeaderBinder.setmData(mProjectExps, mRoles);
        notifyDataSetChanged();
    }

    public void setUser() {
        userStatus.setData();
    }

    public UserSkillAdapter(ModifyDataAction modifyDataAction) {
        roleTitleBinder = new RoleTitleBinder(this, mRoles, modifyDataAction);
        putBinder(UserSkillActivity.Skills.RoleHeader, roleTitleBinder);
        roleBinder = new RoleBinder(this, mRoles, modifyDataAction);
        putBinder(UserSkillActivity.Skills.Role, roleBinder);
        projectExpHeaderBinder = new ProjectExpHeaderBinder(this, mProjectExps, mRoles, modifyDataAction);
        putBinder(UserSkillActivity.Skills.ProjectExpHeader, projectExpHeaderBinder);
        projectExpBinder = new ProjectExpBinder(this, mProjectExps, mRoles, modifyDataAction);
        putBinder(UserSkillActivity.Skills.ProjectExp, projectExpBinder);
        userStatus = new UserStatus(this);
        putBinder(UserSkillActivity.Skills.UserStatus, userStatus);
    }

    @Override
    public UserSkillActivity.Skills getEnumFromOrdinal(int ordinal) {
        if (ordinal == UserSkillActivity.Skills.Role.ordinal()) {
            return UserSkillActivity.Skills.Role;
        } else if (ordinal == UserSkillActivity.Skills.RoleHeader.ordinal()) {
            return UserSkillActivity.Skills.RoleHeader;
        } else if (ordinal == UserSkillActivity.Skills.ProjectExpHeader.ordinal()) {
            return UserSkillActivity.Skills.ProjectExpHeader;
        } else if (ordinal == UserSkillActivity.Skills.UserStatus.ordinal()) {
            return UserSkillActivity.Skills.UserStatus;
        } else {
            return UserSkillActivity.Skills.ProjectExp;
        }
    }

    @Override
    public UserSkillActivity.Skills getEnumFromPosition(int position) {
        if (position == 0) {
            return UserSkillActivity.Skills.RoleHeader;
        } else if (position < mRoles.size() + 1) {
            return UserSkillActivity.Skills.Role;
        } else if (position < mRoles.size() + 2) {
            return UserSkillActivity.Skills.ProjectExpHeader;
        } else if (position < mRoles.size() + 2 + mProjectExps.size()) {
            return UserSkillActivity.Skills.ProjectExp;
        } else {
            return UserSkillActivity.Skills.UserStatus;
        }
    }

    @Override
    public UltimateRecyclerviewViewHolder onCreateViewHolder(ViewGroup parent) {
        View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_skill_list_role, parent, false);
        return new ViewHolder(viewItem);
    }

//    @Override
//    public UltimateRecyclerviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View viewItem;
//        if (viewType == ROLE_TYPE) {
//            viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_skill_list_role, parent, false);
//        } else { //if (viewType == PROJECT_EXP_TYPE) {
//            viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_skill_list_project_exp, parent, false);
//        }
//
//        return new ViewHolder(viewItem);
//
//        super.onCreateHeaderViewHolder()
//    }

    @Override
    public int getItemCount() {
        return mRoles.size() + mProjectExps.size() + 3;
//        if (mRoles.size() > 0) {
//            return 1;
//        } else {
//            return 0;
//        }
    }

    @Override
    public int getAdapterItemCount() {
//        return mRoles.size() + mProjectExps.size();
//        return mRoles.size() + mProjectExps.size() + 1;
        return 1;
    }

    @Override
    public long generateHeaderId(int position) {
        return 100;
    }

//    @Override
//    public boolean hasHeaderView() {
//        return true;
//    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View viewItem;
        viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_skill_list_section_project_exp, parent, false);

        return new ViewHolder(viewItem);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    public static class ViewHolder extends UltimateRecyclerviewViewHolder {
        // each data item is just a string in this case
        public View mView;

        public ViewHolder(View v) {
            super(v);
            mView = v;
        }
    }

    @Override
    public UltimateRecyclerviewViewHolder newFooterHolder(View view) {
        return null;
    }

    @Override
    public UltimateRecyclerviewViewHolder newHeaderHolder(View view) {
        return null;
    }
}

