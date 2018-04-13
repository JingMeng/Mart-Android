package net.coding.mart.activity.user;

import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.TextView;

import net.coding.mart.R;
import net.coding.mart.common.BackActivity;
import net.coding.mart.common.Global;
import net.coding.mart.common.widget.SimpleTextWatcher;
import net.coding.mart.json.Network;
import net.coding.mart.json.SimpleObserver;
import net.coding.mart.json.user.Role;
import net.coding.mart.json.user.Skill;
import net.coding.mart.json.user.UserExtraRole;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@EActivity(R.layout.activity_add_role)
public class AddRoleActivity extends BackActivity {

    @Extra
    UserExtraRole userExtraRole;

    @Extra
    ArrayList<String> allType = new ArrayList<>();

    @Extra
    boolean newRole = false;

    PostParam oldParam, newParam;

    @ViewById
    TextView skills, goodAt;

    @ViewById
    EditText abilities;

    @ViewById
    View sendButton;

    @ViewById
    View abilitiesLayout;

    Set<Skill> pickSkills = new LinkedHashSet<>();
    Set<String> pickGoodAt = new LinkedHashSet<>();

    @AfterViews
    void initAddRoleActivity() {
        oldParam = new PostParam(userExtraRole);
        newParam = new PostParam(userExtraRole);

        if (newParam.isSoftEngineer()) {
            abilitiesLayout.setVisibility(View.VISIBLE);
        } else {
            abilitiesLayout.setVisibility(View.GONE);
        }

        for (Skill item : userExtraRole.skills) {
            if (item.selected) {
                pickSkills.add(item);
            }
        }

        if (userExtraRole.userRole != null) {
            String[] goodAts = userExtraRole.userRole.goodAt.split(",");
            Collections.addAll(pickGoodAt, goodAts);
            abilities.setText(userExtraRole.userRole.abilities);
        }

        uiBindData();

        abilities.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                newParam.abilities = s.toString();
                updateButtonStyle();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!newRole) {
            getMenuInflater().inflate(R.menu.add_role, menu);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @OptionsItem
    void action_delete() {
        new AlertDialog.Builder(this)
                .setMessage("确定删除技能?")
                .setPositiveButton("删除", (dialog, which) -> deleteRole())
                .setNegativeButton("取消", null)
                .show();
    }

    private void deleteRole() {
        ArrayList<String> ids = new ArrayList<>(allType);
        for (int i = 0; i < ids.size(); ++i) {
            int roleId = Integer.valueOf(ids.get(i));
            if (roleId == userExtraRole.userRole.roleId) {
                ids.remove(i);
                break;
            }
        }

        Network.getRetrofit(this)
                .addRoleType(ids)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleObserver(this) {
                    @Override
                    public void onSuccess() {
                        setResult(RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onFail(int errorCode, @NonNull String error) {
                        super.onFail(errorCode, error);
                        showSending(false, "");
                    }
                });
        showSending(true, "删除中...");
    }

    void uiBindData() {
        StringBuilder skillString = new StringBuilder();
        for (Skill item : pickSkills) {
            skillString.append(item.name);
            skillString.append(",");
        }
        if (skillString.length() > 0) {
            skillString.deleteCharAt(skillString.length() - 1);
        }
        skills.setText(skillString.toString());

        StringBuilder goodAtString = new StringBuilder();
        for (String item : pickGoodAt) {
            goodAtString.append(item);
            goodAtString.append(",");
        }

        if (goodAtString.length() > 0) {
            goodAtString.deleteCharAt(goodAtString.length() - 1);
        }
        newParam.goodAt = goodAtString.toString();
        goodAt.setText(newParam.goodAt);

        updateButtonStyle();
    }

    @Click
    void skills() {
        Skill[] SKILLS = new Skill[userExtraRole.skills.size()];
        userExtraRole.skills.toArray(SKILLS);

        final Set<Skill> tempSkill = new LinkedHashSet<>(pickSkills);

        ArrayAdapter adapter = new ArrayAdapter<Skill>(
                this, R.layout.list_item_pick_skill, R.id.text1, SKILLS) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                CheckedTextView checkView = (CheckedTextView) view.findViewById(R.id.text1);
                checkView.setChecked(tempSkill.contains(SKILLS[position]));
                checkView.setText(SKILLS[position].name);

                return view;
            }
        };

        AdapterView.OnItemClickListener clickSkillListView = (parent, view, position, id) -> {
            Skill skill = SKILLS[position];
            if (tempSkill.contains(skill)) {
                tempSkill.remove(skill);
            } else {
                tempSkill.add(skill);
            }
            ((BaseAdapter) parent.getAdapter()).notifyDataSetChanged();
        };

        View.OnClickListener clickOk = v -> {
            pickSkills = tempSkill;
            updateSkills();
        };

        Global.popMulSelectDialog(this, "技能类型", adapter, clickSkillListView, clickOk);
    }

    private void updateSkills() {
        newParam.ids.clear();
        for (Skill item : pickSkills) {
            newParam.ids.add(String.valueOf(item.id));
        }

        uiBindData();
    }

    private void updateGoodAt() {
        uiBindData();
    }

    @Click
    void goodAt() {
        try {
            JSONObject jsonData = new JSONObject(userExtraRole.role.data);
            JSONArray goodAtArray = jsonData.getJSONArray("good_at");
            String[] SKILLS = new String[goodAtArray.length()];
            for (int i = 0; i < goodAtArray.length(); ++i) {
                SKILLS[i] = goodAtArray.getJSONObject(i).getString("name");
            }

            final Set<String> tempSkill = new LinkedHashSet<>(pickGoodAt);

            ArrayAdapter adapter = new ArrayAdapter<String>(this,
                    R.layout.list_item_pick_skill, R.id.text1, SKILLS) {

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);

                    CheckedTextView checkView = (CheckedTextView) view.findViewById(R.id.text1);
                    checkView.setChecked(tempSkill.contains(SKILLS[position]));
                    checkView.setText(SKILLS[position]);

                    return view;
                }
            };

            AdapterView.OnItemClickListener clickListItem = (parent, view, position, id) -> {
                String skill = SKILLS[position];
                if (tempSkill.contains(skill)) {
                    tempSkill.remove(skill);
                } else {
                    if (tempSkill.size() < 10) {
                        tempSkill.add(skill);
                    } else {
                        showMiddleToast("最多能选10个技能");
                    }
                }
                ((BaseAdapter) parent.getAdapter()).notifyDataSetChanged();
            };

            View.OnClickListener clickOk = v -> {
                pickGoodAt = tempSkill;
                updateGoodAt();
            };

            Global.popMulSelectDialog(this, "擅长技术", adapter, clickListItem, clickOk);
        } catch (Exception e) {
            showMiddleToast(e.getMessage());
        }
    }

    @Click
    void sendButton() {
        if (newRole) {
            addRoleType();
        } else {
            modifyRole();
        }

        showSending(true, "");
    }

    private void addRoleType() {
        Network.getRetrofit(this)
                .addRoleType(allType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleObserver(this) {
                    @Override
                    public void onSuccess() {
                        setResult(RESULT_OK);
                        modifyRole();
                    }

                    @Override
                    public void onFail(int errorCode, @NonNull String error) {
                        super.onFail(errorCode, error);
                        showSending(false, "");
                    }
                });
    }

    private void modifyRole() {
        Network.getRetrofit(this)
                .addRole(newParam.roleId, newParam.ids, newParam.abilities, newParam.goodAt)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleObserver(this) {
                    @Override
                    public void onSuccess() {
                        setResult(RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onFail(int errorCode, @NonNull String error) {
                        super.onFail(errorCode, error);
                        showSending(false, "");
                    }
                });
    }

    private void updateButtonStyle() {
        if (newParam.ids.isEmpty()) {
            sendButton.setEnabled(false);
            return;
        }

        if (newParam.isSoftEngineer() && newParam.goodAt.isEmpty()) {
            sendButton.setEnabled(false);
            return;
        }

        if (newParam.equals(oldParam)) {
            sendButton.setEnabled(false);
            return;
        }

        sendButton.setEnabled(true);
    }

    private static class PostParam {
        ArrayList<String> ids = new ArrayList<>();
        int roleId;
        String abilities = "";
        String goodAt = "";

        public PostParam(UserExtraRole role) {
            roleId = role.role.id;
            if (role.userRole != null) {
                for (Skill item : role.skills) {
                    if (item.selected) {
                        ids.add(String.valueOf(item.id));
                    }
                }

                goodAt = role.userRole.goodAt;
                abilities = role.userRole.abilities;
            }
        }

        boolean isSoftEngineer() {
            return roleId == Role.SOFT_ENGINEER_ID;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof PostParam)) return false;

            PostParam postParam = (PostParam) o;

            if (roleId != postParam.roleId) return false;
            if (!ids.equals(postParam.ids)) return false;
            if (!abilities.equals(postParam.abilities)) return false;
            return goodAt.equals(postParam.goodAt);

        }

        @Override
        public int hashCode() {
            int result = ids.hashCode();
            result = 31 * result + roleId;
            result = 31 * result + abilities.hashCode();
            result = 31 * result + goodAt.hashCode();
            return result;
        }
    }
}



