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
import net.coding.mart.json.user.UserExtraRole;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenchao on 16/4/13.
 */
public class RoleTitleBinder extends DataBinder<RoleTitleBinder.ViewHolder> {

    List<UserExtraRole> mData = new ArrayList<>();
    ModifyDataAction mAction;

    public void setmData(List<UserExtraRole> mData) {
        this.mData = mData;
        notifyDataSetChanged();
    }

    public RoleTitleBinder(UltimateDifferentViewTypeAdapter dataBindAdapter,
                           List<UserExtraRole> mData,
                           ModifyDataAction action) {
        super(dataBindAdapter);
        this.mData = mData;
        mAction = action;
    }

    @Override
    public void bindViewHolder(ViewHolder holder, int position) {
        if (mData.size() > 0) {
            holder.addRole.setVisibility(View.GONE);
            holder.addRoleSmall.setVisibility(View.VISIBLE);
        } else {
            holder.addRole.setVisibility(View.VISIBLE);
            holder.addRoleSmall.setVisibility(View.GONE);
        }
    }

    @Override
    public ViewHolder newViewHolder(ViewGroup parent) {
        View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_skill_list_role_head, parent, false);
        ViewHolder holder = new ViewHolder(viewItem);
        holder.addRole.setOnClickListener(v -> mAction.addRole());
        holder.addRoleSmall.setOnClickListener(v -> mAction.addRole());
        return holder;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class ViewHolder extends UltimateRecyclerviewViewHolder {

        TextView addRoleSmall;
        FrameLayout addRole;

        public ViewHolder(View view) {
            super(view);
            addRoleSmall = (TextView) view.findViewById(R.id.addRoleSmall);
            addRole = (FrameLayout) view.findViewById(R.id.addRole);
        }
    }
}
