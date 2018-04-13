package net.coding.mart.json;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import net.coding.mart.common.Global;
import net.coding.mart.common.NetworkImpl;
import net.coding.mart.common.widget.SingleToast;

import org.json.JSONObject;

import rx.Observer;

/**
 * Created by chenchao on 16/3/21.
 */
public abstract class SimpleObserver implements Observer<SimpleHttpResult> {

    // 主要是为了方便用 Toast 提示出错信息,
    private Context mActivity;

    public SimpleObserver(Context mActivity) {
        this.mActivity = mActivity;
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
                Log.e("", sb.toString());

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
    public void onNext(SimpleHttpResult t1HttpResult) {
        try {
            if (t1HttpResult == null) {
                onFail(NetworkImpl.NETWORK_ERROR_SERVICE, NetworkImpl.ERROR_MSG_SERVICE_ERROR);
                return;
            }

            if (t1HttpResult.code != 0) {
                onFail(t1HttpResult.code, t1HttpResult.getErrorMessage());
                return;
            }

            onSuccess();

            if (t1HttpResult.data == null) {
                t1HttpResult.data = true;
            }
            onSuccess(t1HttpResult.data);
        } catch (Exception exception) {
            Global.errorLog(exception);
        }
    }

    public void onSuccess(boolean result) {
    }

    public void onSuccess() {
    }

    public void onFail(int errorCode, @NonNull String error) {
        SingleToast.showErrorMsg(mActivity, error);
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
