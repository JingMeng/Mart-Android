package net.coding.mart.activity.user;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.marshalchen.ultimaterecyclerview.UltimateDifferentViewTypeAdapter;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.multiViewTypes.DataBinder;

import net.coding.mart.R;
import net.coding.mart.json.user.UserExtraProjectExp;
import net.coding.mart.json.user.UserExtraRole;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenchao on 16/4/13.
 */
public class ProjectExpHeaderBinder extends DataBinder<ProjectExpHeaderBinder.ViewHolder> {

    List<UserExtraProjectExp> mData = new ArrayList<>();
    int roleCount = 0;
    ModifyDataAction mAction;

    public void setmData(List<UserExtraProjectExp> mData, List<UserExtraRole> roles) {
        this.mData = mData;
        roleCount = roles.size();
        notifyDataSetChanged();
    }

    public ProjectExpHeaderBinder(UltimateDifferentViewTypeAdapter dataBindAdapter,
                                  List<UserExtraProjectExp> mData,
                                  List<UserExtraRole> roles,
                                  ModifyDataAction action) {
        super(dataBindAdapter);
        this.mData = mData;
        roleCount = roles.size();
        mAction = action;
    }

    @Override
    public void bindViewHolder(ViewHolder holder, int listPosition) {
        if (mData.size() > 0) {
            holder.addProjectExp.setVisibility(View.GONE);
            holder.addProjectExpSmall.setVisibility(View.VISIBLE);
        } else {
            holder.addProjectExp.setVisibility(View.VISIBLE);
            holder.addProjectExpSmall.setVisibility(View.GONE);
        }
    }

    @Override
    public ViewHolder newViewHolder(ViewGroup parent) {
        View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_skill_list_project_exp_header, parent, false);
        ViewHolder holder = new ViewHolder(viewItem);
        holder.addProjectExpSmall.setOnClickListener(v -> mAction.addProjectExp());
        holder.addProjectExp.setOnClickListener(v -> mAction.addProjectExp());
        return holder;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class ViewHolder extends UltimateRecyclerviewViewHolder {

        TextView addProjectExpSmall;
        FrameLayout addProjectExp;

        public ViewHolder(View view) {
            super(view);
            addProjectExpSmall = (TextView) view.findViewById(R.id.addProjectExpSmall);
            addProjectExp = (FrameLayout) view.findViewById(R.id.addProjectExp);
        }
    }
}
