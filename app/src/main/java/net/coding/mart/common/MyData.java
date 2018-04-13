package net.coding.mart.common;

import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.loopj.android.http.PersistentCookieStore;
import com.tencent.TIMManager;
import com.tencent.android.tpush.XGPushManager;

import net.coding.mart.json.CurrentUser;
import net.coding.mart.json.Network;
import net.coding.mart.json.body.NewReward;
import net.coding.mart.json.mart2.user.MartUser;
import net.coding.mart.json.message.IMUser;
import net.coding.mart.json.mpay.OrderMapper;
import net.coding.mart.setting.SettingHomeActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by chenchao on 15/10/9.
 * 保存当前账户的单例变量
 */
public class MyData {

    CurrentUser mCurrentUser = new CurrentUser();

    private static final String ACCOUNT = "ACCOUNT";
    private static final String ACCOUNT_DIR = "ACCOUNT_DIR";
    private static final String LOGIN_INFO_DIR = "LOGIN_INFO_DIR";
    private static final String ACCOUNT_SIG = "ACCOUNT_SIG";

    private static MyData ourInstance = new MyData();

    public static MyData getInstance() {
        return ourInstance;
    }

    private MyData() {
    }

    public static boolean isPublishUser() {
        return MyData.getInstance().isLogin() && MyData.getInstance().getData().isDemand();
    }

    public boolean isLogin() {
        return mCurrentUser != null && !TextUtils.isEmpty(mCurrentUser.getGlobal_key());
    }

    private void setData(CurrentUser currentUser) {
        mCurrentUser = currentUser;
    }

    public final CurrentUser getData() {
        return mCurrentUser;
    }

    public void init(Context context) {
        CurrentUser currentUser = MyData.loadAccount(context);
        setData(currentUser);
    }

    public void login(Context context, CurrentUser data) {
        setData(data);
        saveAccount(context, data);
        saveFeedbackInfo(context, new FeedbackInfo(data.getName(), data.getPhone(), data.getEmail()));
        Global.bindXG(context);
    }

    public void login(Context context, MartUser data) {
        CurrentUser user = new CurrentUser(data);
        login(context, user);
    }

    public void update(Context context, MartUser user) {
        mCurrentUser.updateData(user);
        saveAccount(context, mCurrentUser);
        Global.bindXG(context);
    }

    public void save(Context context) {
        saveAccount(context, mCurrentUser);
    }

    public static File getAccountDir(Context context) {
        File accoutDir = new File(context.getFilesDir(), ACCOUNT_DIR);
        if (accoutDir.exists()) {
            if (accoutDir.isDirectory()) {
                return accoutDir;
            } else {
                accoutDir.delete();
            }
        }

        accoutDir.mkdirs();
        return accoutDir;
    }

    public void loginOut(Context context) {
        File dir = context.getFilesDir();
        String[] fileNameList = dir.list();
        for (String item : fileNameList) {
            File file = new File(dir, item);
            if (file.exists() && !file.isDirectory()) {
                file.delete();
            }
        }

        TIMManager.getInstance().logout();

        File file = new File(context.getFilesDir(), ACCOUNT);
        if (file.exists()) {
            file.delete();
        }

        File accoutDir = new File(context.getFilesDir(), ACCOUNT_DIR);
        if (accoutDir.exists()) {
            SettingHomeActivity.deleteFiles(accoutDir);
        }

        deleteLoginInfo(context);

        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();

        PersistentCookieStore cookieStore = new PersistentCookieStore(context);
        cookieStore.clear();

        Network.removeCookie(context);

        XGPushManager.unregisterPush(context.getApplicationContext());

        setData(new CurrentUser());
    }

    private void deleteLoginInfo(Context ctx) {
        File loginDir = new File(ctx.getFilesDir(), LOGIN_INFO_DIR);
        if (loginDir.exists() && loginDir.isDirectory()) {
            String[] loginInfos = loginDir.list();
            for (String item : loginInfos) {
                File loginFile = new File(loginDir, item);
                if (loginFile.exists() && !loginFile.isDirectory()) {
                    loginFile.delete();
                }
            }
        }
    }

    private static final String DRAFT_ISSUE_JOB = "DRAFT_ISSUE_JOB";

    public void saveDraft(Context ctx, IssueJobParam issueJob) {
        File dir = makeDir(ctx);

        File draftFile = new File(dir, DRAFT_ISSUE_JOB);
        if (draftFile.exists()) {
            draftFile.delete();
        }

        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(draftFile));
            oos.writeObject(issueJob);
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private File makeDir(Context ctx) {
        File dir;
        if (isLogin()) {
            dir = new File(ctx.getFilesDir() + "/" + mCurrentUser.getGlobal_key() + "/");
            dir.mkdirs();
        } else {
            dir = ctx.getFilesDir();
        }
        return dir;
    }

    public IssueJobParam loadDraft(Context ctx) {
        File dir = makeDir(ctx);
        File draftFile = new File(dir, DRAFT_ISSUE_JOB);
        IssueJobParam data = null;
        if (draftFile.exists()) {
            try {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(draftFile));
                data = (IssueJobParam) ois.readObject();
                ois.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (data == null) {
            data = new IssueJobParam();
        }

        return data;
    }

    public void deleteIssueJobDraft(Context ctx) {
        File dir = makeDir(ctx);
        File draftFile = new File(dir, DRAFT_ISSUE_JOB);
        if (draftFile.exists()) {
            draftFile.delete();
        }
    }

    private static void saveAccount(Context ctx, CurrentUser data) {
        if (data == null || TextUtils.isEmpty(data.getGlobal_key())) {
            return;
        }

        File file = new File(ctx.getFilesDir(), ACCOUNT);
        if (file.exists()) {
            file.delete();
        }

        try {
            ObjectOutputStream oos = new ObjectOutputStream(ctx.openFileOutput(ACCOUNT, Context.MODE_PRIVATE));
            oos.writeObject(data);
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void moveDraft(Context ctx) {
        if (!isLogin()) {
            return;
        }

        File srcDir = ctx.getFilesDir();
        File srcDraftFile = new File(srcDir, DRAFT_ISSUE_JOB);

        File destDir = makeDir(ctx);
        File destDraftFile = new File(destDir, DRAFT_ISSUE_JOB);

        srcDraftFile.renameTo(destDraftFile);
    }

    private static final String FILE_PUSH = "FILE_PUSH";
    private static final String KEY_NEED_PUSH = "KEY_NEED_PUSH";

    public static boolean getNeedPush(Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(FILE_PUSH, Context.MODE_PRIVATE);
        return sp.getBoolean(KEY_NEED_PUSH, true);
    }

    public static void setNeedPush(Context ctx, boolean push) {
        SharedPreferences sp = ctx.getSharedPreferences(FILE_PUSH, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(KEY_NEED_PUSH, push);
        editor.commit();
    }

    public static boolean hasLoginFile(Context context) {
        File file = new File(context.getFilesDir(), ACCOUNT);
        return file.exists();
    }

    private static CurrentUser loadAccount(Context ctx) {
        CurrentUser data = null;
        File file = new File(ctx.getFilesDir(), ACCOUNT);
        if (file.exists()) {
            try {
                ObjectInputStream ois = new ObjectInputStream(ctx.openFileInput(ACCOUNT));
                data = (CurrentUser) ois.readObject();
                ois.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (data == null) {
            data = new CurrentUser();
        }

        return data;
    }

    public static void saveSig(Context ctx, IMUser user) {
        new DataCache<IMUser>().save(ctx, user, ACCOUNT_SIG, ACCOUNT_DIR);
    }

    public static IMUser loadSig(Context ctx) {
        IMUser user = new DataCache<IMUser>().loadObject(ctx, ACCOUNT_SIG, ACCOUNT_DIR);
        if (user == null) {
            user = new IMUser();
        }

        return user;
    }


    public static final String FEEDBACK_INFO = "FEEDBACK_INFO";

    public static final String USER_SELF = "USER_SELF";

    public void saveFeedbackInfo(Context ctx, FeedbackInfo data) {
        new DataCache<FeedbackInfo>().save(ctx, data, FEEDBACK_INFO, USER_SELF);
    }

    public FeedbackInfo loadFeedbackInfo(Context ctx) {
        FeedbackInfo info = new DataCache<FeedbackInfo>().loadObject(ctx, FEEDBACK_INFO, USER_SELF);
        if (info == null) {
            info = new FeedbackInfo("", "", "");
        }

        return info;
    }

    public static final String PROJECT_PUBLISH = "PROJECT_PUBLISH";

    public void saveRewardDraft(Context ctx, NewReward data) {
        new DataCache<>().save(ctx, data, PROJECT_PUBLISH, USER_SELF);
    }

    public NewReward loadRewardDraft(Context ctx) {
        NewReward info = new DataCache<NewReward>().loadObject(ctx, PROJECT_PUBLISH, USER_SELF);
        if (info == null) {
            info = new NewReward();
        }

        return info;
    }

    public static class FeedbackInfo implements Serializable {
        private static final long serialVersionUID = 5569525509069637744L;
        public String name = "";
        public String phone = "";
        public String email = "";

        public FeedbackInfo(String name, String phone, String email) {
            this.name = name;
            this.phone = phone;
            this.email = email;
        }
    }

    static class DataCache<T> {

        public final static String FILDER_GLOBAL = "FILDER_GLOBAL";

        public void save(Context ctx, ArrayList<T> data, String name) {
            save(ctx, data, name, "");
        }

        public void saveGlobal(Context ctx, Object data, String name) {
            save(ctx, data, name, FILDER_GLOBAL);
        }

        private void deleteFile(File folder, String name) {
            File file = new File(folder, name);
            if (file.exists()) {
                file.delete();
            }
        }

        public void delete(Context ctx, String name) {
            deleteFile(ctx.getFilesDir(), name);
        }

        public void deleteGlobal(Context ctx, String name) {
            File globalFolder = new File(ctx.getFilesDir(), FILDER_GLOBAL);
            if (!globalFolder.exists()) {
                return;
            }

            deleteFile(globalFolder, name);
        }

        private void save(Context ctx, Object data, String name, String folder) {
            if (ctx == null) {
                return;
            }

            File file;
            if (!folder.isEmpty()) {
                File fileDir = new File(ctx.getFilesDir(), folder);
                if (!fileDir.exists() || !fileDir.isDirectory()) {
                    fileDir.mkdirs();
                }
                file = new File(fileDir, name);
            } else {
                file = new File(ctx.getFilesDir(), name);
            }

            if (file.exists()) {
                file.delete();
            }

            try {
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
                oos.writeObject(data);
                oos.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public ArrayList<T> load(Context ctx, String name) {
            return load(ctx, name, "");
        }

        public ArrayList<T> loadGlobal(Context ctx, String name) {
            return load(ctx, name, FILDER_GLOBAL);
        }

        @SuppressWarnings("unchecked")
        public T loadGlobalObject(Context ctx, String name) {
            String folder = FILDER_GLOBAL;
            T data = null;

            File file;
            if (!folder.isEmpty()) {
                File fileDir = new File(ctx.getFilesDir(), folder);
                if (!fileDir.exists() || !fileDir.isDirectory()) {
                    fileDir.mkdir();
                }
                file = new File(fileDir, name);
            } else {
                file = new File(ctx.getFilesDir(), name);
            }

            if (file.exists()) {
                try {
                    ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
                    data = (T) ois.readObject();
                    ois.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return data;
        }

        @SuppressWarnings("unchecked")
        private T loadObject(Context ctx, String name, String folder) {
            T data = null;
            File file;
            if (!folder.isEmpty()) {
                File fileDir = new File(ctx.getFilesDir(), folder);
                if (!fileDir.exists() || !fileDir.isDirectory()) {
                    fileDir.mkdir();
                }
                file = new File(fileDir, name);
            } else {
                file = new File(ctx.getFilesDir(), name);
            }

            if (file.exists()) {
                try {
                    ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
                    data = (T) ois.readObject();
                    ois.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return data;
        }

        @SuppressWarnings("unchecked")
        private ArrayList<T> load(Context ctx, String name, String folder) {
            ArrayList<T> data = null;

            File file;
            if (!folder.isEmpty()) {
                File fileDir = new File(ctx.getFilesDir(), folder);
                if (!fileDir.exists() || !fileDir.isDirectory()) {
                    fileDir.mkdir();
                }
                file = new File(fileDir, name);
            } else {
                file = new File(ctx.getFilesDir(), name);
            }

            if (file.exists()) {
                try {
                    ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
                    data = (ArrayList<T>) ois.readObject();
                    ois.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (data == null) {
                data = new ArrayList<>();
            }

            return data;
        }
    }

    public boolean canJoin() {
        return isLogin() && mCurrentUser.isFullInfo()
                && mCurrentUser.isFullSkills()
                && mCurrentUser.isPassingSurvey()
                && mCurrentUser.isIdentityChecked();
    }

    public boolean isPassIdentity() {
        return mCurrentUser.isIdentityChecked();
    }

    private static final String BACKGROUNDS = "BACKGROUNDS";
    private static final String MPAY_ORDER_MAPPER = "MPAY_ORDER_MAPPER";
    private static final String GLOBAL_SETTING = "GLOBAL_SETTING";
    private static final String GLOBAL_SETTING_BACKGROUND = "GLOBAL_SETTING_BACKGROUND";

    public static ArrayList<LoginBackground.PhotoItem> loadBackgrounds(Context ctx) {
        return new DataCache<LoginBackground.PhotoItem>().loadGlobal(ctx, BACKGROUNDS);
    }

    public static void saveBackgrounds(Context ctx, ArrayList<LoginBackground.PhotoItem> data) {
        new DataCache<LoginBackground.PhotoItem>().saveGlobal(ctx, data, BACKGROUNDS);
    }

    public static OrderMapper loadMPayOrderMapper(Context ctx) {
        return new DataCache<OrderMapper>().loadGlobalObject(ctx, MPAY_ORDER_MAPPER);
    }

    public static void saveMPayOrderMapper(Context ctx, OrderMapper orderMapper) {
        new DataCache<OrderMapper>().saveGlobal(ctx, orderMapper, MPAY_ORDER_MAPPER);
    }

    public static void setCheckLoginBackground(Context ctx) {
        Calendar calendar = Calendar.getInstance();
        SharedPreferences.Editor editor = ctx.getSharedPreferences(GLOBAL_SETTING, Context.MODE_PRIVATE).edit();
        editor.putLong(GLOBAL_SETTING_BACKGROUND, calendar.getTimeInMillis());
        editor.commit();
    }
}
