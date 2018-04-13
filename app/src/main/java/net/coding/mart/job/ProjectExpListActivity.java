package net.coding.mart.job;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import net.coding.mart.R;
import net.coding.mart.common.BackActivity;
import net.coding.mart.json.BaseObserver;
import net.coding.mart.json.Network;
import net.coding.mart.json.user.UserExtraProjectExp;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@EActivity(R.layout.activity_project_exp_list)
public class ProjectExpListActivity extends BackActivity {

    @Extra
    ArrayList<Integer> selectProjectIds = new ArrayList<>();

    @ViewById
    ListView listView;

    ArrayList<UserExtraProjectExp> selectProjects1 = new ArrayList<>();

    @ViewById
    View sendButton;

    private ArrayAdapter<UserExtraProjectExp> adapter;

    @AfterViews
    void initProjectExpListActivity() {
        loadProjectExpData();
    }

    private void loadProjectExpData() {
        Network.getRetrofit(this)
                .getUserProjectExp()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<List<UserExtraProjectExp>>(this) {
                    @Override
                    public void onSuccess(List<UserExtraProjectExp> data) {
                        selectProjects1.clear();
                        for (UserExtraProjectExp exp : data) {
                            for (Integer id : selectProjectIds) {
                                if (exp.id == id) {
                                    selectProjects1.add(exp);
                                    break;
                                }
                            }
                        }

                        adapter = new ArrayAdapter<UserExtraProjectExp>(ProjectExpListActivity.this,
                                R.layout.project_exp_list_item, data) {
                            @NonNull
                            @Override
                            public View getView(int position, View convertView, ViewGroup parent) {
                                ProjectExpListItemHolder holder;
                                if (convertView == null) {
                                    convertView = LayoutInflater.from(parent.getContext())
                                            .inflate(R.layout.project_exp_list_item, parent, false);
                                    holder = new ProjectExpListItemHolder(convertView);
                                    convertView.setTag(holder);
                                } else {
                                    holder = (ProjectExpListItemHolder) convertView.getTag();
                                }

                                UserExtraProjectExp data = adapter.getItem(position);
                                holder.selectIcon.setVisibility(selectProjects1.contains(data) ? View.VISIBLE : View.INVISIBLE);
                                holder.projectName.setText(data.projectName);
                                holder.projectTime.setText(String.format("项目时间：%s", data.getProjectTimeRange()));
                                return convertView;
                            }
                        };

                        View footer = getLayoutInflater().inflate(R.layout.divide_10_bottom, listView, false);
                        listView.addFooterView(footer, null, false);

                        listView.setAdapter(adapter);
                    }

                });
    }

    @ItemClick(R.id.listView)
    public void listViewItemClick(UserExtraProjectExp projectExp) {
        if (selectProjects1.contains(projectExp)) {
            selectProjects1.remove(projectExp);
        } else {
            selectProjects1.add(projectExp);
        }
        adapter.notifyDataSetChanged();
    }

    @Click
    void sendButton() {
        ArrayList<Integer> result = new ArrayList<>();
        for (UserExtraProjectExp item : selectProjects1) {
            result.add(item.id);
        }
        Intent intent = new Intent();
        intent.putExtra("resultData", result);
        setResult(RESULT_OK, intent);

        finish();
    }

    public class ProjectExpListItemHolder {
        private TextView projectName;
        private TextView projectTime;
        private ImageView selectIcon;

        public ProjectExpListItemHolder(View view) {
            projectName = (TextView) view.findViewById(R.id.projectName);
            projectTime = (TextView) view.findViewById(R.id.projectTime);
            selectIcon = (ImageView) view.findViewById(R.id.selectIcon);
        }
    }

}
