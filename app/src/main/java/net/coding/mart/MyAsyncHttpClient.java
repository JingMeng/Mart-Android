package net.coding.mart;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.LogInterface;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import net.coding.mart.json.Network;

/**
 * Created by chaochen on 14-10-8.
 * 对 AsyncHttpClient 做了一些公共操作
 */
public class MyAsyncHttpClient {

    public static RequestHandle post(Context context, String url, RequestParams params, ResponseHandlerInterface response) {
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        return client.post(context, url, params, response);
    }

    public static RequestHandle post(Context context, String url, ResponseHandlerInterface response) {
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        return client.post(context, url, new RequestParams(), response);
    }

    public static RequestHandle post(Fragment fragment, String url, RequestParams params, ResponseHandlerInterface response) {
        return post(fragment.getActivity(), url, params, response);
    }

    public static RequestHandle get(Fragment fragment, String url, ResponseHandlerInterface response) {
        return get(fragment.getActivity(), url, response);
    }

    public static RequestHandle get(Context context, String url, ResponseHandlerInterface response) {
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        return client.get(context, url, response);
    }
    public static RequestHandle get(Context context, String url, RequestParams params, ResponseHandlerInterface response) {
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        return client.get(context, url, params, response);
    }

    public static AsyncHttpClient createClient(Context context) {
        AsyncHttpClient client = new AsyncHttpClient();

        if (BuildConfig.DEBUG) {
            client.setLoggingEnabled(true);
            client.setLoggingLevel(LogInterface.VERBOSE);
        }

        PersistentCookieStore cookieStore = new PersistentCookieStore(context);

        client.setCookieStore(cookieStore);
//        setCookie(client, context);

        for (String item : Network.mapHeaders.keySet()) {
            client.addHeader(item, Network.mapHeaders.get(item));
        }

        client.addHeader("Cookie", Network.getCookie(context));

//        client.setTimeout(60 * 1000);
//        client.setMaxConnections(5);
//        client.setTimeout();

        return client;
    }

//     有时候用于本地测试
//    static void setCookie(AsyncHttpClient client, Context context) {
//        String sid = "cc74af4c-d34f-42cc-9ff9-fb6b199ff25f";
//        PersistentCookieStore cookieStore = new PersistentCookieStore(context);
////        cookieStore.clear();
//        List<Cookie> list = cookieStore.getCookies();
//        for (int i = 0; i < list.size(); ++i) {
//            BasicClientCookie cooke = (BasicClientCookie) list.get(i);
//            if (cooke.getName().equals("sid")) {
//                cooke.setValue(sid);
//            }
//        }
//
//        client.setCookieStore(cookieStore);
//
//    }

}
