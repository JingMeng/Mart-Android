package net.coding.mart.json;

import android.content.Context;
import android.support.annotation.NonNull;

import com.orhanobut.logger.Logger;

import net.coding.mart.common.Global;
import net.coding.mart.common.NetworkImpl;
import net.coding.mart.common.widget.SingleToast;

import org.json.JSONObject;

import java.util.Map;

import rx.Observer;

/**
 * Created by chenchao on 16/3/21.
 */
public abstract class NewBaseObserver<T1 extends BaseHttpResult> implements Observer<T1> {

    // 主要是为了方便用 Toast 提示出错信息,
    private Context mActivity;
    private boolean showErrorTip = true;

    public NewBaseObserver(Context mActivity) {
        this(mActivity, Network.CacheType.noCache);
    }

    public NewBaseObserver(Context context, Network.CacheType cacheType) {
        this.mActivity = context;

        if (cacheType == Network.CacheType.onlyCache) {
            showErrorTip = false;
        }
    }

    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(Throwable e) {
        try {
            String errorMsg = NetworkImpl.ERROR_MSG_CONNECT_FAIL;
            if (e != null) {
                StackTraceElement[] stackTrace = e.getStackTrace();
                StringBuilder sb = new StringBuilder();
                sb.append(e.getMessage());
                sb.append("\n");
                for (StackTraceElement item : stackTrace) {
                    sb.append(item.toString());
                    sb.append("\n");
                }
                Logger.e(sb.toString());

                String error = e.getMessage();
                if (error != null && !error.isEmpty()) {
                    errorMsg = error;
                }
            }

            onFail(NetworkImpl.NETWORK_CONNECT_FAIL, errorMsg);
        } catch (Exception exception) {
            Global.errorLog(exception);
        }
    }

    @Override
    public void onNext(T1 t1HttpResult) {
        try {
            if (t1HttpResult == null) {
                onFail(NetworkImpl.NETWORK_ERROR_SERVICE, NetworkImpl.ERROR_MSG_SERVICE_ERROR);
                return;
            }

            if (t1HttpResult.code != 0) {
                String showErrorMessage = "未知错误";
                Map errorMessage = t1HttpResult.msg;
                if (errorMessage != null) {
                    showErrorMessage = (String) errorMessage.values().iterator().next();
                }
                onFail(t1HttpResult.code, showErrorMessage);
                return;
            }

            onSuccess(t1HttpResult);
        } catch (Exception exception) {
            Global.errorLog(exception);
        }
    }

    public void onSuccess(T1 data) {
    }

    public void onFail(int errorCode, @NonNull String error) {
        if (showErrorTip) {
            if (error.startsWith("Unable to resolve host")) {
                error = "网络链接不可用";
            }
            SingleToast.showErrorMsg(mActivity, error);
        }
    }

    static JSONObject sNetworkError;
    static JSONObject sServiceError;

    static {
        try {
            String connectFailString = String.format("{\"code\":%d,\"msg\":{\"error\":\"%s\"}}",
                    NetworkImpl.NETWORK_CONNECT_FAIL, NetworkImpl.ERROR_MSG_CONNECT_FAIL);
            sNetworkError = new JSONObject(connectFailString);


            String serviceFailString = String.format("{\"code\":%d,\"msg\":{\"error\":\"%s\"}}",
                    NetworkImpl.NETWORK_ERROR_SERVICE, NetworkImpl.ERROR_MSG_SERVICE_ERROR);
            sServiceError = new JSONObject(serviceFailString);

        } catch (Exception e) {
            Global.errorLog(e);
        }

    }
}
