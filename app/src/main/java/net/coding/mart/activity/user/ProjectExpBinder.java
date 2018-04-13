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
import net.coding.mart.json.File;
import net.coding.mart.json.reward.IndustryName;
import net.coding.mart.json.user.UserExtraProjectExp;
import net.coding.mart.json.user.UserExtraRole;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenchao on 16/4/13.
 */
public class ProjectExpBinder extends DataBinder<ProjectExpBinder.ViewHolder> {

    ModifyDataAction mAction;
    List<UserExtraProjectExp> mData = new ArrayList<>();
    int roleCount = 0;

    public void setmData(List<UserExtraProjectExp> mData, List<UserExtraRole> roles) {
        this.mData = mData;
        roleCount = roles.size();
        notifyDataSetChanged();
    }

    public ProjectExpBinder(UltimateDifferentViewTypeAdapter dataBindAdapter,
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
        int position = listPosition - roleCount - 2;
        UserExtraProjectExp data = mData.get(position);

        holder.title.setText(data.projectName);
        holder.description.setText(data.description);
        holder.industrys.setText(IndustryName.createName(data.getIndustrys()));
        holder.duty.setText(data.duty);
        String linkString = data.link;
        holder.link.setText(linkString);
        holder.projectType.setText(data.projectTypeName);

        holder.time.setText(data.getProjectTimeRange());

        if (data.files.isEmpty()) {
            holder.fileLayout.setVisibility(View.GONE);
        } else {
            holder.fileLayout.setVisibility(View.VISIBLE);
            holder.fileLayoutContent.removeAllViews();
            LayoutInflater inflater = LayoutInflater.from(holder.fileLayoutContent.getContext());
            for (File item : data.files) {
                View fileItem = inflater.inflate(R.layout.project_exp_list_file, holder.fileLayoutContent, false);
                ((TextView) fileItem.findViewById(R.id.fileName)).setText(item.filename);
                holder.fileLayoutContent.addView(fileItem);
            }
        }

        holder.modifyButton.setTag(data);
    }

    @Override
    public ViewHolder newViewHolder(ViewGroup parent) {
        View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_skill_list_project_exp, parent, false);
        ViewHolder holder = new ViewHolder(viewItem);
        holder.modifyButton.setOnClickListener(v -> {
            UserExtraProjectExp projectExp = (UserExtraProjectExp) v.getTag();
            mAction.modifyProjectExp(projectExp);
        });
        return holder;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class ViewHolder extends UltimateRecyclerviewViewHolder {

        TextView title;
        TextView time;
        ImageView modifyButton;
        TextView description;
        TextView industrys;
        TextView duty;
        TextView link;
        TextView fileName;
        TextView downloadButton;
        TextView projectType;
        ViewGroup fileLayout;
        ViewGroup fileLayoutContent;

        public ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            time = (TextView) view.findViewById(R.id.time);
            modifyButton = (ImageView) view.findViewById(R.id.modifyButton);
            description = (TextView) view.findViewById(R.id.description);
            industrys = (TextView) view.findViewById(R.id.industrys);
            duty = (TextView) view.findViewById(R.id.duty);
            link = (TextView) view.findViewById(R.id.link);
            fileName = (TextView) view.findViewById(R.id.fileName);
            downloadButton = (TextView) view.findViewById(R.id.downloadButton);
            fileLayout = (ViewGroup) view.findViewById(R.id.fileLayout);
            fileLayoutContent = (ViewGroup) view.findViewById(R.id.fileLayoutContent);
            projectType = (TextView) view.findViewById(R.id.projectType);
        }
    }
}
