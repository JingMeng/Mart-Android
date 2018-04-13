package net.coding.mart.developers;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.AppCompatButton;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jauker.widget.BadgeView;

import net.coding.mart.R;
import net.coding.mart.common.BaseActivity;
import net.coding.mart.common.MyData;
import net.coding.mart.common.constant.Huoguo;
import net.coding.mart.common.umeng.UmengEvent;
import net.coding.mart.developers.adapter.FunctionViewAdapter;
import net.coding.mart.developers.dialog.ModifyPlatformDialog;
import net.coding.mart.developers.dialog.ShopCarDialog;
import net.coding.mart.json.BaseObserver;
import net.coding.mart.json.Network;
import net.coding.mart.json.developer.CalculateResult;
import net.coding.mart.json.developer.CategoryVO;
import net.coding.mart.json.developer.Functions;
import net.coding.mart.json.developer.ModuleFront;
import net.coding.mart.json.developer.ModuleVO;
import net.coding.mart.json.developer.PlatformVO;
import net.coding.mart.json.developer.Quotation;
import net.coding.mart.login.LoginActivity_;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by liu on 16/5/27.
 */
public class FunctionListActivity extends BaseActivity implements View.OnClickListener {

    private TabLayout tabLayout;
    private ViewPager pager;
    private ImageView ivFunction;
    private TextView tvState;
    private AppCompatButton btnOk;
    private List<String> ids = null;
    private HashMap<String, Quotation> quotations = null;
    private FunctionViewAdapter adapter;
    //html页面数量
    private int num = 0;

    private static final int RESULT_LOGIN = 1;

    private List<Quotation> pickedData = new ArrayList<>();

    private BadgeView badgeView;
    private final List<Quotation> pickedPlatformIds = new ArrayList<>();

    public static void initDataFromActivity(FunctionListActivity activity,
                                            Quotation platformQuotaion,
                                            List<Quotation> categorysData,
                                            List<Quotation> modulesData,
                                            List<Quotation> functionsData) {
        HashMap<String, Quotation> allQuotations = activity.getQuotations();

        String[] categoryCodes = platformQuotaion.getChildrenCode();
        for (String categoryCode : categoryCodes) {
            Quotation categoryQuotaion = allQuotations.get(categoryCode);
            categorysData.add(categoryQuotaion);
            modulesData.add(categoryQuotaion);

            String[] category = categoryQuotaion.getChildrenCode();
            for (int j = 0; j < category.length; j++) {
                Quotation moduleQuotation = allQuotations.get(category[j]);

                modulesData.add(moduleQuotation);
                String[] thirdCategory = moduleQuotation.children.split(",");
                functionsData.add(moduleQuotation);

                List<Quotation> radioGroup = null;
                if (moduleQuotation.isRadioGroup()) {
                    radioGroup = new ArrayList<>(3);
                }

                for (int k = 0; k < thirdCategory.length; k++) {
                    Quotation functionQuotation = allQuotations.get(thirdCategory[k]);

                    if (radioGroup != null) {
                        functionQuotation.setExtra(radioGroup);
                        radioGroup.add(functionQuotation);
                        if (k == 0 && !functionQuotation.code.equals("7010101")) {
                            functionQuotation.setDefault();
                        }
                    }

                    functionQuotation.platformCode = platformQuotaion.code;
                    functionQuotation.mainCode = categoryQuotaion.code;
                    functionQuotation.categoryCode = moduleQuotation.code;

                    functionsData.add(functionQuotation);
                }
            }
        }

        // 直接在原始数据里面加是不是更好?
        if (platformQuotaion.isFrontPlatform()) {
            Quotation quotation = Quotation.getFrontPageModule();
            modulesData.add(quotation);
            functionsData.add(quotation);

            Quotation frontPageFunction = Quotation.getFrontPageFunction();
            functionsData.add(frontPageFunction);
        }
    }


    public static void initDataFromActivity(FunctionListActivity activity,
                                            Quotation platformQuotaion,
                                            List<Quotation> defaultData) {
        List<Quotation> categorysData = new ArrayList<>();
        List<Quotation> modulesData = new ArrayList<>();
        List<Quotation> functionsData = new ArrayList<>();

        HashMap<String, Quotation> allQuotations = activity.getQuotations();

        platformQuotaion.resetPickedFunctions();
        int platformPos = defaultData.indexOf(platformQuotaion);
        if (platformPos != -1) {
            int start = platformPos;
            int end;
            for (end = start + 1; end < defaultData.size(); ++end) {
                if (!defaultData.get(end).isFunction()) {
                    break;
                }
            }

            defaultData.removeAll(new ArrayList<>(defaultData.subList(start, end)));
        }

        int insertDefaultPos = 0;
        defaultData.add(insertDefaultPos++, platformQuotaion);

        String[] categoryCodes = platformQuotaion.getChildrenCode();
        for (String categoryCode : categoryCodes) {
            Quotation categoryQuotaion = allQuotations.get(categoryCode);
            categorysData.add(categoryQuotaion);
            modulesData.add(categoryQuotaion);

            String[] category = categoryQuotaion.getChildrenCode();
            for (int j = 0; j < category.length; j++) {
                Quotation moduleQuotation = allQuotations.get(category[j]);

                modulesData.add(moduleQuotation);
                String[] thirdCategory = moduleQuotation.children.split(",");
                functionsData.add(moduleQuotation);

                List<Quotation> radioGroup = null;
                if (moduleQuotation.isRadioGroup()) {
                    radioGroup = new ArrayList<>(3);
                }


                for (int k = 0; k < thirdCategory.length; k++) {
                    Quotation functionQuotation = allQuotations.get(thirdCategory[k]);

                    if (radioGroup != null) {
                        functionQuotation.setExtra(radioGroup);
                        radioGroup.add(functionQuotation);
                        if (k == 0 && !functionQuotation.code.equals("7010101")) {
                            functionQuotation.setDefault();
                        }
                    }

                    functionQuotation.platformCode = platformQuotaion.code;
                    functionQuotation.mainCode = categoryQuotaion.code;
                    functionQuotation.categoryCode = moduleQuotation.code;

                    functionsData.add(functionQuotation);
                    if (functionQuotation.isDefault == 1 && defaultData != null) {
                        defaultData.add(insertDefaultPos++, functionQuotation);
                        platformQuotaion.addExtra(functionQuotation);
                    }
                }
            }
        }
    }

    public HashMap<String, Quotation> getQuotations() {
        return quotations;
    }

    public void setQuotations(HashMap<String, Quotation> quotations) {
        this.quotations = quotations;
    }

    private void sortIds() {
        Collections.sort(ids, new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                return getPos(lhs) - getPos(rhs);
            }

            private int getPos(String s) {
                for (int i = 0; i < Huoguo.idsSort.length; ++i) {
                    if (s.equals(Huoguo.idsSort[i])) {
                        return i;
                    }
                }

                return Huoguo.idsSort.length;
            }
        });
    }

    // 根据选择的平台类型，添加或删除 "管理后台"
    public static void sortOutSelectPlatform(List<String> ids) {
        String backgroundItem = null;
        for (String item : ids) {
            if (item.equals(Huoguo.background.code)) {
                backgroundItem = item;
            }
        }

        Huoguo[] bg = {
                Huoguo.web,
                Huoguo.weixin,
                Huoguo.ios,
                Huoguo.android,
        };

        boolean needAddBackground = false;
        for (String item : ids) {
            for (Huoguo type : bg) {
                if (item.equals(type.code)) {
                    needAddBackground = true;
                    break;
                }
            }

            if (needAddBackground) {
                break;
            }
        }

        if (backgroundItem == null) {
            if (needAddBackground) {
                ids.add(Huoguo.background.code);
            }
        } else {
            if (!needAddBackground) {
                ids.remove(backgroundItem);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function_list);
        setActionBarTitle("功能评估");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        initView();
        Bundle bundle = getIntent().getExtras();
        ids = bundle.getStringArrayList("ids");

        sortOutSelectPlatform(ids);

        sortIds();

        Network.getRetrofit(this)
                .getFunctions()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<Functions>(this) {
                    @Override
                    public void onSuccess(Functions data) {
                        super.onSuccess(data);
                        for (int i = 0; i < ids.size(); i++) {
                            if (data.quotations != null) {
                                FunctionListActivity.this.setQuotations(data.quotations);
                                Quotation item = data.quotations.get(ids.get(i));
                                pickedPlatformIds.add(item);
                                addPlatformDefault(item);
                            }
                        }

                        updateNum();

                        final String[] needReplaces = new String[]{
                                "3040101",
                                "2040101",
                        };
                        for (String item : needReplaces) {
                            Quotation quotation = data.quotations.get(item);
                            quotation.description = quotation.description.replaceAll("<br>", "\n");
                        }

//                        Quotation manageBackground = quotations.get(SelectGridAdapter.MANAGE_BACKGROUND);
//                        addPlatformDefault(manageBackground);
//                        addManageBackground(pickedPlatformIds);
                        if (pickedPlatformIds.size() > 0) {
                            adapter = new FunctionViewAdapter(FunctionListActivity.this, pickedPlatformIds);
                            pager.setAdapter(adapter);

                            pager.setOffscreenPageLimit(HuoguoEntranceFragment.PLATFORMS.length);
                            tabLayout.setupWithViewPager(pager);
                            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
                        }
                    }
                });

        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void clseEvent(String message) {
        if (message.equals(SavePriceActivity.HUOGUO_SAVE_PRICE) && !isFinishing()) {
            finish();
        }
    }

    private boolean isContainString(List<String> list, String s) {
        for (String item : list) {
            if (item.equals(s)) {
                return true;
            }
        }

        return false;
    }

    private int getContainStringPos(String[] list, String s) {
        for (int i = 0; i < list.length; ++i) {
            if (list[i].equals(s)) {
                return i;
            }
        }

        return 0;
    }

    //    返回 other 里面有, 而 self 里面没有的元素列表
    private List<String> getNoContains(List<String> self, List<String> other) {
        List<String> noContainList = new ArrayList<>();
        for (String item : other) {
            if (!isContainString(self, item)) {
                noContainList.add(item);
            }
        }

        return noContainList;
    }

    public void updateView(List<String> ids) {
        List<String> needRemove = getNoContains(ids, this.ids);
        List<String> needAdd = getNoContains(this.ids, ids);
        this.ids = ids;
//        ids.add(SelectGridAdapter.MANAGE_BACKGROUND);
        sortIds();

        List<Quotation> quotationList = new ArrayList<>();
        for (int i = 0; i < this.ids.size(); i++) {
            if (quotations != null) {
                quotationList.add(quotations.get(this.ids.get(i)));
            }
        }
        if (quotationList.size() > 0) {
            pickedPlatformIds.clear();
            pickedPlatformIds.addAll(quotationList);
            adapter.notifyDataSetChanged();
        }

        for (String item : needRemove) {
            Quotation removePlatform = quotations.get(item);
            int start = pickedData.indexOf(removePlatform);
            int end = -1;
            if (start != -1) {
                for (int i = start + 1; i < pickedData.size(); ++i) {
                    if (!pickedData.get(i).isFunction()) {
                        end = i;
                        break;
                    }
                }
                if (end == -1) {
                    end = pickedData.size();
                }

                pickedData.removeAll(new ArrayList<>(pickedData.subList(start, end)));
            }
        }

        for (String item : needAdd) {
            Quotation addPlatform = quotations.get(item);
            addPlatformDefault(addPlatform);
        }
        updateNum();
    }

    private void initView() {
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        pager = (ViewPager) findViewById(R.id.pager);
        ivFunction = (ImageView) findViewById(R.id.iv_function);
        tvState = (TextView) findViewById(R.id.tv_state);
        btnOk = (AppCompatButton) findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(this);
        ivFunction.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_function_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            case R.id.action_modify:
                ModifyPlatformDialog dialog = new ModifyPlatformDialog(FunctionListActivity.this);
                dialog.setPicked(ids);
                dialog.show();
                umengEvent(UmengEvent.ACTION, "估价 _ 修改平台");
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public void updatePicked() {
        updateNum();
    }


    public List<Quotation> getPickedData() {
        return pickedData;
    }

//    /**
//     * 恢复默认
//     */
//    public void reset() {
//        List<View> views = adapter.getViews();
//        for (int i = 0; i < views.size(); i++) {
//            FunctionFragment fragment = (FunctionFragment) views.get(i);
//            fragment.initData();
//        }
//    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
        updateNum();
    }

    public void notifyDataSetChanged() {
        adapter.notifyDataSetChanged();
        updateNum();
    }

    public void refreshData() {
        updateNum();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_function:
                ShopCarDialog dialog = new ShopCarDialog(FunctionListActivity.this);
                dialog.setPickedFunctions(getPickedData());
                dialog.show();
                dialog.setOnDismissListener(dialog1 -> adapter.notifyDataSetChanged());
                break;

            case R.id.btn_ok:
                calculate();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RESULT_LOGIN:
                    realCalculate();
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void calculate() {
        if (MyData.getInstance().isLogin()) {
            realCalculate();
        } else {
            LoginActivity_.intent(this).startForResult(RESULT_LOGIN);
        }
    }

    public void realCalculate() {
        showSending(true);
        Map<String, String> map = new HashMap<>();
        StringBuilder buffer = new StringBuilder();

        for (int i = 0; i < pickedData.size(); i++) {
            Quotation item = pickedData.get(i);
            if (item.isFunction()) {
                if (!item.title.equals("页面数量")) {
                    if (buffer.length() > 0) {
                        buffer.append(",");
                    }
                    buffer.append(item.platformCode);
                    buffer.append(">");
                    buffer.append(item.mainCode);
                    buffer.append(">");
                    buffer.append(item.categoryCode);
                    buffer.append(">");
                    buffer.append(item.code);
                } else {
                    map.put("webPageCount", getNum() + "");
                }
            }
        }

        map.put("codes", buffer.toString());
        if (!map.containsKey("webPageCount")) {
            map.put("webPageCount", "0");
        }

        Network.getRetrofit(FunctionListActivity.this)
                .getCalResult(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<CalculateResult>(FunctionListActivity.this) {
                    @Override
                    public void onSuccess(CalculateResult data) {
                        showSending(false);
                        Intent intent = new Intent(FunctionListActivity.this, CalculateResultActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("vo", data);
                        bundle.putString("codes", map.get("codes"));
                        bundle.putString("webPageCount", map.get("webPageCount"));
                        bundle.putInt("id", data.id);
                        String jsonStr = getJsonStr();
                        bundle.putString("jsonStr", jsonStr);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }

                    @Override
                    public void onFail(int errorCode, @NonNull String error) {
                        super.onFail(errorCode, error);
                        showSending(false);
                    }

                });
    }


    private Quotation getQuotation(String code) {
        Quotation quotation = null;
        for (int i = 0; i < pickedData.size(); i++) {
            if (pickedData.get(i).code.equals(code)) {
                quotation = pickedData.get(i);
            }
        }
        return quotation;
    }

    private String getJsonStr() {
        List<PlatformVO> data = new ArrayList<>();
        for (int i = 0; i < pickedData.size(); i++) {
            if (pickedData.get(i).isPlatform() &&
                    (i + 1) < pickedData.size() &&
                    pickedData.get(i + 1).isFunction()) { // 平台类型必须选择了功能才能算

                PlatformVO platformVO = new PlatformVO();
                platformVO.setPlatform(pickedData.get(i).title);
                String[] childCodes = pickedData.get(i).children.split(",");
                List<Object> categoryVOList = new ArrayList<>();
                for (int j = 0; j < childCodes.length; j++) {
                    CategoryVO categoryVO = new CategoryVO();
                    Quotation quotation = quotations.get(childCodes[j]);
                    categoryVO.setName(quotation.title);
                    String[] childCodes_arr = quotation.children.split(",");
                    List<ModuleVO> moduleVOList = new ArrayList<>();
                    for (int k = 0; k < childCodes_arr.length; k++) {
                        ModuleVO moduleVO = new ModuleVO();
                        Quotation quotation1 = quotations.get(childCodes_arr[k]);
                        moduleVO.setName(quotation1.title);
                        String[] str_arr = quotation1.children.split(",");
                        List<String> stringList = new ArrayList<>();
                        for (int n = 0; n < str_arr.length; n++) {
                            Quotation quotation2 = getQuotation(str_arr[n]);
                            if (quotation2 != null) {
                                stringList.add(quotation2.title);
                            }
                        }
                        if (stringList.size() != 0) {
                            moduleVO.setFunction(stringList);
                            moduleVOList.add(moduleVO);
                        }
                    }
                    categoryVO.setModule(moduleVOList);
                    categoryVOList.add(categoryVO);
                }
                platformVO.setCategory(categoryVOList);
                data.add(platformVO);

                if (pickedData.get(i).isFrontPlatform() && this.num > 0) {
                    ModuleFront moduleFront = new ModuleFront();
                    moduleFront.setCount(this.num + "");
                    categoryVOList.add(moduleFront);
                }

            }
        }

        return new Gson().toJson(data);
    }

    //    用来判断是否只选择了前端项目 或者 前端项目 和 管理后台
    public boolean onlyPickFont() {
        return this.ids.size() == 1 && this.ids.get(0).equals(Huoguo.front.code);
    }

    public boolean frontCondition() {
        boolean containFront = false;
        for (String item : ids) {
            if (item.equals(Huoguo.front.code)) {
                containFront = true;
                break;
            }
        }

        return !containFront || this.num >= 1;
    }

    public boolean noDefaultCountCondition() {
        boolean containEnought = false;
        Huoguo[] huoguos = new Huoguo[]{
                Huoguo.web,
                Huoguo.weixin,
                Huoguo.ios,
                Huoguo.android
        };

        for (String item : ids) {
            for (Huoguo huoguo : huoguos) {
                if (item.equals(huoguo.code)) {
                    containEnought = true;
                    break;
                }
            }

            if (containEnought) {
                break;
            }
        }

        if (!containEnought) {
            return true;
        }

        int noDefaultNum = 0;
        if (pickedData != null) {
            for (int i = 0; i < pickedData.size(); i++) {
                if (pickedData.get(i).type == 4 && !pickedData.get(i).title.equals("页面数量") && pickedData.get(i).isDefault != 1) {
                    noDefaultNum++;
                }
            }
        }

        return noDefaultNum >= 5;
    }

    public void updateNum() {
        int num = 0;
        if (pickedData != null) {
            for (int i = 0; i < pickedData.size(); i++) {
                if (pickedData.get(i).type == 4 && !pickedData.get(i).title.equals("页面数量")) {
                    num++;
                }
            }
        }

        if (frontCondition()) {
            if (noDefaultCountCondition()) {
                enableCalculate();
            } else {
                showCountShort();
            }
        } else {
            showFrontShort();
        }

        if (badgeView == null) {
            badgeView = new BadgeView(this);
        }
        badgeView.setTargetView(ivFunction);
        badgeView.setBackground(9, Color.parseColor("#f5a623"));
        badgeView.setBadgeGravity(Gravity.TOP | Gravity.RIGHT);
        badgeView.setBadgeCount(num);
    }

    private void showFrontShort() {
        tvState.setVisibility(View.VISIBLE);
        tvState.setText(R.string.huoguo_tip_select_p005_pages);
        btnOk.setTextColor(0x7fffffff);
        btnOk.setEnabled(false);
    }

    private void showCountShort() {
        tvState.setVisibility(View.VISIBLE);
        tvState.setText(R.string.huoguo_tip_font_count);
        btnOk.setTextColor(0x7fffffff);
        btnOk.setEnabled(false);
    }

    private void enableCalculate() {
        tvState.setVisibility(View.GONE);
        btnOk.setTextColor(0xffffffff);
        btnOk.setEnabled(true);
    }

    public void clearPickedFunction(Quotation tag) {
        int start = pickedData.indexOf(tag);
        if (start != -1) {

            ArrayList<Quotation> removes = new ArrayList<>();
            ++start;
            for (int i = start; i < pickedData.size(); ++i) {
                Quotation item = pickedData.get(i);
                if (!item.isFunction()) {
                    break;
                }

                if (!item.isRadioItem()) {
                    removes.add(item);
                }
            }

            boolean isFront = tag.isFrontPlatform();

            pickedData.removeAll(removes);
            tag.getExtra().removeAll(removes);

            if (isFront) {
                Quotation frontPageFunction = Quotation.getFrontPageFunction();
                pickedData.add(start, frontPageFunction);
                setNum(0);
            }
        }
    }


    public void addPlatformDefault(Quotation addPlatform) {
        String item = addPlatform.code;
        int pos = getContainStringPos(Huoguo.idsSort, item);
        int insertPos = -1;
        for (int i = pos + 1; i < Huoguo.idsSort.length; ++i) {
            String plateformString = Huoguo.idsSort[i];
            Quotation platform = quotations.get(plateformString);
            int sectionPos = pickedData.indexOf(platform);
            if (sectionPos != -1) {
                insertPos = sectionPos;
                break;
            }
        }
        if (insertPos == -1) {
            insertPos = pickedData.size();
        }

        pickedData.add(insertPos, addPlatform);
        addPlatform.resetExtra();
        resetPickedFunction(addPlatform);
    }

    public void resetPickedFunction(Quotation tag) {
        List<Quotation> tagDefault = new ArrayList<>();
        initDataFromActivity(this, tag, tagDefault);
        tagDefault.remove(tag);

        int start = pickedData.indexOf(tag);
        if (start != -1) {
            ++start;
            int end = -1;
            for (int i = start; i < pickedData.size(); ++i) {
                if (!pickedData.get(i).isFunction()) {
                    end = i;
                    break;
                }
            }
            if (end == -1) {
                end = pickedData.size();
            }

            boolean isFront = tag.isFrontPlatform();
            pickedData.removeAll(new ArrayList<>(pickedData.subList(start, end)));
            pickedData.addAll(start, tagDefault);
            if (isFront) {
                Quotation frontPageFunction = Quotation.getFrontPageFunction();
                pickedData.add(start, frontPageFunction);
                setNum(0);
            }
        }
    }

    public Quotation getPlatformFromPos(int pos) {
        return pickedPlatformIds.get(pos);
    }
}
