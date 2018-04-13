package net.coding.mart.activity.user;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.marshalchen.ultimaterecyclerview.UltimateDifferentViewTypeAdapter;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.multiViewTypes.DataBinder;

import net.coding.mart.R;
import net.coding.mart.json.user.Skill;
import net.coding.mart.json.user.UserExtraRole;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenchao on 16/4/13.
 */
public class RoleBinder extends DataBinder<RoleBinder.ViewHolder> {

    List<UserExtraRole> mData = new ArrayList<>();

    ModifyDataAction mAction;

    public void setmData(List<UserExtraRole> mData) {
        this.mData = mData;
        notifyDataSetChanged();
    }

    public RoleBinder(UltimateDifferentViewTypeAdapter dataBindAdapter,
                      List<UserExtraRole> mData,
                      ModifyDataAction action) {
        super(dataBindAdapter);
        this.mData = mData;
        mAction = action;
    }

    @Override
    public void bindViewHolder(ViewHolder holder, int positionList) {
        int position = positionList - 1;
        UserExtraRole data = mData.get(position);
        holder.title.setText(data.role.name);

        StringBuilder skillString = new StringBuilder();
        for (Skill item : data.skills) {
            if (item.selected) {
                skillString.append(item.name);
                skillString.append(",");
            }
        }
        if (skillString.length() > 0) {
            skillString.deleteCharAt(skillString.length() - 1);
        }
        holder.skills.setText(skillString.toString());

        if (data.isSoftEngineer()) {
            holder.goodAtLayout.setVisibility(View.VISIBLE);
        } else {
            holder.goodAtLayout.setVisibility(View.GONE);
        }

        holder.goodAt.setText(data.userRole.goodAt);
        holder.abilities.setText(data.userRole.abilities);
        holder.modifyButton.setTag(data);
    }

    @Override
    public ViewHolder newViewHolder(ViewGroup parent) {
        View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_skill_list_role, parent, false);
        ViewHolder holder = new ViewHolder(viewItem);
        holder.modifyButton.setOnClickListener(v -> {
            UserExtraRole role = (UserExtraRole) v.getTag();
            mAction.modifyRole(role);
        });
        return holder;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class ViewHolder extends UltimateRecyclerviewViewHolder {

        TextView title;
        ImageView modifyButton;
        TextView skills;
        TextView goodAt;
        TextView abilities;
        View goodAtLayout;

        public ViewHolder(View view) {
            super(view);

            title = (TextView) view.findViewById(R.id.title);
            modifyButton = (ImageView) view.findViewById(R.id.modifyButton);
            skills = (TextView) view.findViewById(R.id.skills);
            goodAt = (TextView) view.findViewById(R.id.goodAt);
            abilities = (TextView) view.findViewById(R.id.abilities);
            goodAtLayout = view.findViewById(R.id.goodAtLayout);
        }
    }
}
