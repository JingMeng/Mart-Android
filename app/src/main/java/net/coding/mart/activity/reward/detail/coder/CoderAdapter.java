package net.coding.mart.activity.reward.detail.coder;

import android.content.Context;
import android.view.View;

import com.marshalchen.ultimaterecyclerview.quickAdapter.easyRegularAdapter;

import net.coding.mart.R;
import net.coding.mart.common.ImageLoadTool;
import net.coding.mart.json.reward.Coder;

import java.util.List;

/**
 * Created by chenchao on 16/10/13.
 */

public class CoderAdapter extends easyRegularAdapter<Coder, CoderHolder> {

    Context context;
    View.OnClickListener clickAccept;
    View.OnClickListener clickRefuse;
    View.OnClickListener clickRootLayout;

    public CoderAdapter(List<Coder> list, Context context,
                        View.OnClickListener clickRootLayout,
                        View.OnClickListener clickAccept,
                        View.OnClickListener clickRefuse) {
        super(list);

        this.clickRootLayout = clickRootLayout;
        this.context = context;
        this.clickAccept = clickAccept;
        this.clickRefuse = clickRefuse;
    }

    @Override
    protected int getNormalLayoutResId() {
        return R.layout.recycler_list_coder;
    }

    @Override
    protected CoderHolder newViewHolder(View view) {
        CoderHolder holder = new CoderHolder(view);
        holder.buttonAccept.setOnClickListener(clickAccept);
        holder.buttonRefuse.setOnClickListener(clickRefuse);
        holder.rootLayout.setOnClickListener(clickRootLayout);

        return holder;
    }

    @Override
    protected void withBindHolder(CoderHolder holder, Coder data, int position) {
        ImageLoadTool.loadImageUser(holder.icon, data.avatar);
        holder.name.setText(data.name);
        holder.name.setCompoundDrawablesWithIntrinsicBounds(null, null, data.getCardDrawable(context), null);

        holder.time.setText(data.getCreatedAtFormat());

        int userIcon = data.isTeam() ? R.mipmap.ic_user_team : R.mipmap.ic_user_single;
        holder.role.setText(data.getRewardRole().alics);
        holder.role.setCompoundDrawables(context.getResources().getDrawable(userIcon), null, null, null);

        Coder.Status status = data.getStatus();
        switch (status) {
            case accept:
                holder.setStatus("已通过", 0xFF5BC673, View.GONE, View.VISIBLE, View.VISIBLE);
                if (data.stagePayed) {
                    holder.setStatus("已通过", 0xFF5BC673, View.GONE, View.VISIBLE, View.GONE);
                }
                break;
            case refuse:
                holder.setStatus("已拒绝", 0xFFE84D60, View.VISIBLE, View.GONE, View.GONE);
                break;
            default:
                holder.setStatus("审核中", 0xFF999999, View.VISIBLE, View.VISIBLE, View.VISIBLE);
        }

        holder.buttonAccept.setTag(data);
        holder.buttonRefuse.setTag(data);
        holder.rootLayout.setTag(data);
    }



}
