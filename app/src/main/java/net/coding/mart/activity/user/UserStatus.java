package net.coding.mart.activity.user;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.marshalchen.ultimaterecyclerview.UltimateDifferentViewTypeAdapter;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.multiViewTypes.DataBinder;

import net.coding.mart.R;
import net.coding.mart.common.BaseActivity;
import net.coding.mart.common.MyData;
import net.coding.mart.json.Network;
import net.coding.mart.json.NewBaseObserver;
import net.coding.mart.json.mart2.user.MartUser;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by chenchao on 16/8/19.
 */
public class UserStatus extends DataBinder<UserStatus.ViewHolder> {

    public UserStatus(UltimateDifferentViewTypeAdapter dataBindAdapter) {
        super(dataBindAdapter);
    }

    public void setData() {
        notifyDataSetChanged();
    }

    @Override
    public void bindViewHolder(ViewHolder holder, int position) {
        holder.bind();
    }

    @Override
    public ViewHolder newViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_account_join, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    static class Params {
        boolean acceptNewRewardAllNotification;
        int freeTime;

        public void set() {
            MartUser user = MyData.getInstance().getData().user;
            acceptNewRewardAllNotification = user.acceptNewRewardAllNotification;
            freeTime = user.freeTime.ordinal();
        }
    }

    static class ViewHolder extends UltimateRecyclerviewViewHolder {

        final String[] free_time_options = {
                "较少时间兼职",
                "较多时间兼职",
                "全职 SOHO"
        };

        SwitchCompat accetpNotify;
        TextView freeTime;
        View freeTimeLayout;

        Params params = new Params();

        Context context;

        View layoutRoot;

        public ViewHolder(View v) {
            super(v);

            layoutRoot = v;

            context = v.getContext();

            accetpNotify = (SwitchCompat) v.findViewById(R.id.accetpNotify);
            freeTime = (TextView) v.findViewById(R.id.freeTime);
            freeTimeLayout = v.findViewById(R.id.freeTimeLayout);

            accetpNotify.setOnCheckedChangeListener((buttonView, isChecked) -> accetpNotify(isChecked));
            freeTime.setOnClickListener(v2 -> freeTime());

//            BaseActivity activity = (BaseActivity) context;
//            sendButton.setOnClickListener(v1 -> {
//                Network.getRetrofit(activity)
//                        .setJoinState(params.acceptNewRewardAllNotification, params.freeTime, params.developerType.id)
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(new BaseObserver<JoinState>(activity) {
//                            @Override
//                            public void onSuccess(JoinState data) {
//                                super.onSuccess(data);
//                                activity.showSending(false);
//                                activity.showMiddleToast("开发者信息更新成功");
//                                activity.finish();
//                            }
//
//                            @Override
//                            public void onFail(int errorCode, @NonNull String error) {
//                                super.onFail(errorCode, error);
//                                activity.showSending(false);
//                            }
//                        });
//                activity.showSending(true);
//            });

        }

        void accetpNotify(boolean checked) {
            params.acceptNewRewardAllNotification = checked;
            freeTimeLayout.setVisibility(checked ? View.VISIBLE : View.GONE);
            setFreeTime();
        }

        void freeTime() {
            new AlertDialog.Builder(context)
                    .setItems(free_time_options, (dialog, which) -> {
                        params.freeTime = which;
                        freeTime.setText(free_time_options[which]);
                        setFreeTime();
                    })
                    .show();
        }

        private void setFreeTime() {
            BaseActivity activity = (BaseActivity) context;
            Network.getRetrofit(activity)
                    .setFreeTime(params.freeTime, params.acceptNewRewardAllNotification)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new NewBaseObserver<MartUser>(activity) {
                        @Override
                        public void onSuccess(MartUser data) {
                            super.onSuccess(data);
                            MyData.getInstance().update(context, data);
                            activity.showSending(false);
                        }

                        @Override
                        public void onFail(int errorCode, @NonNull String error) {
                            super.onFail(errorCode, error);
                            activity.showSending(false);
                        }
                    });
            activity.showSending(true);
        }

        public void bind() {
            layoutRoot.setVisibility(View.VISIBLE);

            params.set();

            accetpNotify.setOnCheckedChangeListener(null);
            accetpNotify.setChecked(params.acceptNewRewardAllNotification);
            accetpNotify.setOnCheckedChangeListener((buttonView, isChecked) -> accetpNotify(isChecked));

            freeTimeLayout.setVisibility(accetpNotify.isChecked() ? View.VISIBLE : View.GONE);

            if (0 <= params.freeTime) {
                freeTime.setText(free_time_options[params.freeTime]);
            }
        }


    }
}
