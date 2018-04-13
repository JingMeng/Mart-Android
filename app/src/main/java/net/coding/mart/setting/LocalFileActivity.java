package net.coding.mart.setting;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.marshalchen.ultimaterecyclerview.SwipeableUltimateViewAdapter;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.layoutmanagers.ScrollSmoothLineaerLayoutManager;
import com.marshalchen.ultimaterecyclerview.swipe.SwipeItemManagerInterface;

import net.coding.mart.R;
import net.coding.mart.common.BackActivity;
import net.coding.mart.common.local.DownloadFile;
import net.coding.mart.common.local.FileHelp;
import net.coding.mart.common.local.FileProvider;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EActivity(R.layout.activity_local_file)
public class LocalFileActivity extends BackActivity {

    public static final String EVENT_REFRUSH = "LocalFileActivity-EVENT_REFRUSH";

    private static final int RESULT_ENTER_FILE = 1;

    @ViewById
    UltimateRecyclerView recyclerView;

    @Extra
    ArrayList<FileHelp> fileLists;

    @Extra
    String actionTitle;
    private MenuItem menuItem;
    private SwipeAdapter adapter;

    @AfterViews
    void initLocalFileActivity() {
        if (!TextUtils.isEmpty(actionTitle)) {
            setActionBarTitle(actionTitle);
        }

        recyclerView.setEmptyView(R.layout.empty_view, UltimateRecyclerView.EMPTY_KEEP_HEADER_AND_LOARMORE);

        if (fileLists == null) {
            reloadDirData();
        } else {
            loadFileData();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void undateList(String message) {
        if (message.equals(EVENT_REFRUSH) && fileLists == null) {
            reloadDirData();
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editer, menu);
        menuItem = menu.findItem(R.id.actionEdit);

        return super.onCreateOptionsMenu(menu);
    }

    @OptionsItem
    void actionEdit() {
        if (adapter.isCheckMode()) {
            adapter.setCheckMode(false);
            menuItem.setTitle("编辑");
        } else {
            adapter.setCheckMode(true);
            menuItem.setTitle("完成");
        }
    }

    private void reloadDirData() {
        File file = new File(getFilesDir(), DownloadFile.LOCAL_PATH);
        HashMap<String, ArrayList<FileHelp>> fileMap = new HashMap<>();
        for (String item : file.list()) {
            FileHelp fileItem = new FileHelp(item);
            if (!fileItem.isEmpty()) {
                String rewardIdString = String.valueOf(fileItem.rewardId);
                if (fileMap.containsKey(rewardIdString)) {
                    ArrayList<FileHelp> values = fileMap.get(rewardIdString);
                    values.add(fileItem);
                } else {
                    ArrayList<FileHelp> values = new ArrayList<>();
                    values.add(fileItem);
                    fileMap.put(rewardIdString, values);
                }
            }
        }

        ArrayList<FileHelp> dirs = new ArrayList<>();
        for (String item : fileMap.keySet()) {
            FileHelp fileHelp = fileMap.get(item).get(0);
            dirs.add(fileHelp);
        }

        adapter = new SwipeAdapter(dirs, fileMap);
        adapter.setMode(SwipeItemManagerInterface.Mode.Single);

        ScrollSmoothLineaerLayoutManager mLayoutManager = new ScrollSmoothLineaerLayoutManager(this, LinearLayoutManager.VERTICAL, false, 500);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void loadFileData() {
        adapter = new SwipeAdapter(fileLists);
        adapter.setMode(SwipeItemManagerInterface.Mode.Single);

        ScrollSmoothLineaerLayoutManager mLayoutManager = new ScrollSmoothLineaerLayoutManager(this, LinearLayoutManager.VERTICAL, false, 500);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    @OnActivityResult(RESULT_ENTER_FILE)
    void onResultFiles(int result) {
        if (result == RESULT_OK) {
            reloadDirData();
        }
    }

    static class SwipHolder extends UltimateRecyclerviewViewHolder {
        public static final int LAYOUT_ID = R.layout.activity_local_file_item;

        public View delete;
        public TextView title;
        public ImageView checked;
        public ImageView icon;
        public View rootLayout;

        public SwipHolder(View v, boolean bind) {
            super(v);
            if (bind) {
                rootLayout = v.findViewById(R.id.rootLayout);
                checked = (ImageView) v.findViewById(R.id.checkButton);
                icon = (ImageView) v.findViewById(R.id.icon);
                title = (TextView) v.findViewById(R.id.title);
                delete = v.findViewById(R.id.delete);
            }
        }

        public SwipHolder(View v) {
            this(v, true);
        }
    }

//    private View.OnClickListener clickDeleteItem = v -> {
//        FileHelp fileHelp = (FileHelp) v.getTag();
//        Object object = v.getTag(R.id.rootLayout);
//        if (object != null) {
//
//        } else {
//
//        }
//        Map<String, ArrayList<FileHelp>> map = ( v.getTag(R.id.rootLayout);
//        Map<String, ArrayList<FileHelp>> map = (Map<String, ArrayList<FileHelp>>)
//    }

    static class SwipeAdapter extends SwipeableUltimateViewAdapter<FileHelp> {

        Map<String, ArrayList<FileHelp>> mapData;
        View.OnClickListener clickChecked = v -> {
            FileHelp data = (FileHelp) v.getTag();
            data.checked = !data.checked;
            ((ImageView) v).setImageResource(data.getCheckImage());
        };
        private boolean checkMode = false;
        private View.OnClickListener clickedDelete = v -> {
            FileHelp item = (FileHelp) v.getTag();
            if (mapData != null) {
                for (FileHelp fileHelp : mapData.get(String.valueOf(item.rewardId))) {
                    File file = new File(v.getContext().getFilesDir(), DownloadFile.LOCAL_PATH + "/" + fileHelp.getLocalFileName());
                    if (file.exists()) {
                        file.delete();
                    }
                }
                int pos = source.indexOf(item);
                removeAt(pos);
                notifyDataSetChanged();
            } else {
                File file = new File(v.getContext().getFilesDir(), DownloadFile.LOCAL_PATH + "/" + item.getLocalFileName());
                if (file.exists()) {
                    file.delete();
                }
                int pos = source.indexOf(item);
                removeAt(pos);
                notifyDataSetChanged();
                EventBus.getDefault().post(EVENT_REFRUSH);
            }
        };
        private View.OnClickListener clickItem = v -> {
            FileHelp fileHelp = (FileHelp) v.getTag();
            if (checkMode) {
                fileHelp.checked = !fileHelp.checked;
                notifyDataSetChanged();
                return;
            }

            if (mapData != null) {
                Map<String, ArrayList<FileHelp>> map = (Map<String, ArrayList<FileHelp>>) v.getTag(R.id.rootLayout);
                ArrayList<FileHelp> files = map.get(String.valueOf(fileHelp.rewardId));
                LocalFileActivity_.intent(v.getContext()).fileLists(files).actionTitle(fileHelp.rewardName).start();
            } else {
                File file = new File(v.getContext().getFilesDir(), DownloadFile.LOCAL_PATH + "/" + fileHelp.getLocalFileName());
                FileProvider.openFile(v.getContext(), file);
            }
        };

        public SwipeAdapter(List<FileHelp> list) {
            super(list);
        }

        public SwipeAdapter(List<FileHelp> list, Map<String, ArrayList<FileHelp>> mapData) {
            super(list);
            this.mapData = mapData;
        }

        public boolean isCheckMode() {
            return checkMode;
        }

        public void setCheckMode(boolean checkMode) {
            this.checkMode = checkMode;
            notifyDataSetChanged();
        }

        @Override
        protected int getNormalLayoutResId() {
            return SwipHolder.LAYOUT_ID;
        }

        @Override
        protected UltimateRecyclerviewViewHolder newViewHolder(View view) {
            SwipHolder holder = new SwipHolder(view);
            holder.delete.setOnClickListener(clickedDelete);
            holder.checked.setOnClickListener(clickChecked);
            holder.rootLayout.setOnClickListener(clickItem);
            return holder;
        }

        @Override
        protected void withBindHolder(UltimateRecyclerviewViewHolder viewHolder, FileHelp data, int position) {
            super.withBindHolder(viewHolder, data, position);
            SwipHolder holder = (SwipHolder) viewHolder;
            holder.delete.setTag(data);
            holder.checked.setImageResource(data.getCheckImage());

            holder.rootLayout.setTag(data);

            if (mapData != null) { // 文件夹
                holder.icon.setImageResource(R.mipmap.ic_file_folder);
                ArrayList files = mapData.get(String.valueOf(data.rewardId));
                holder.title.setText(String.format("%s（%s）", data.rewardName, files.size()));
                holder.rootLayout.setTag(R.id.rootLayout, mapData);
                holder.rootLayout.setOnClickListener(clickItem);
            } else {
                holder.icon.setImageResource(data.getTypeImage());
                holder.title.setText(data.getNameContainSuffix());
            }

            if (checkMode) {
                holder.checked.setVisibility(View.VISIBLE);
            } else {
                holder.checked.setVisibility(View.GONE);
            }

            holder.checked.setTag(data);
            holder.delete.setTag(data);
        }

        @Override
        public SwipHolder newFooterHolder(View view) {
            return new SwipHolder(view, false);
        }

        @Override
        public SwipHolder newHeaderHolder(View view) {
            return new SwipHolder(view, false);
        }

        @Override
        public long generateHeaderId(int position) {
            return 0;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        protected void removeNotifyExternal(int pos) {
            closeItem(pos);
        }

    }
}
