package net.coding.mart.json;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Build;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import net.coding.mart.common.Global;
import net.coding.mart.common.network.MartPersistentCookieJar;
import net.coding.mart.common.network.converter.GsonConverterFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Cookie;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * Created by chenchao on 16/3/19.
 * 网络请求库
 */
public class Network {

    private static final int MAX_STALE = 60 * 60 * 24 * 28; // 无网络时，设置超时为4周
    public static String BASE_URL = Global.HOST_API + "/";
    public static HashMap<String, String> mapHeaders = new HashMap<>();

    public static String getCookie(Context context) {
        StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        for (Cookie item : new SharedPrefsCookiePersistor(context).loadAll()) {
            if (isFirst) {
                isFirst = false;
            } else {
                sb.append("; ");
            }
            sb.append(item.toString());
        }
        return sb.toString();
    }

    public static String getCookieMid(Context context) {
        for (Cookie item : new SharedPrefsCookiePersistor(context).loadAll()) {
            if (item.name().equalsIgnoreCase("mid")) {
                return String.format("%s=%s", "mid", item.value());
            }
        }

        return "";
    }

    public static void removeCookie(Context context) {
        new SharedPrefsCookiePersistor(context).clear();
    }

    public static void init(Context context) {
        mapHeaders.clear();

        String versionName = "";
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionName = pInfo.versionName;
        } catch (Exception e) {
            Global.errorLog(e);
        }

        String userAgentValue = String.format("CodingMart_Android/%s (%s)", versionName, Build.VERSION.SDK_INT);
        mapHeaders.put("User-Agent", userAgentValue);

        mapHeaders.put("Accept", "*/*");

        String split = "//";
        int startPos = Global.HOST.indexOf(split);

        String tempHost = Global.HOST;
        if (startPos != -1) {
            tempHost = Global.HOST.substring(startPos + split.length(), Global.HOST.length());
        }
        mapHeaders.put("Host", tempHost);
        mapHeaders.put("Origin", Global.HOST);
        mapHeaders.put("Accept", "application/json");
    }

    public static HashMap<String, String> getMapHeaders() {
        return mapHeaders;
    }

    public static MartRequest getRetrofit(Context context) {
        return getRetrofit(context, CacheType.noCache, Type.JSON);
    }

    public static MartRequest getByteRetrofit(Context context) {
        return getRetrofit(context, CacheType.noCache, Type.Photo);
    }

    private static String inputStream2String(InputStream is) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        StringBuilder buffer = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            buffer.append(line);
        }
        return buffer.toString();
    }

    public static MartRequest getRetrofit(Context context, CacheType cacheType) {
        return getRetrofit(context, cacheType, Type.JSON);
    }

    public static MartRequest getRetrofit(Context context, CacheType cacheType, Type type) {
        Interceptor interceptorCookie = chain -> {
            Request request = chain.request();

            Request.Builder accept = request.newBuilder();
            Map<String, String> common = getMapHeaders();
            for (String item : common.keySet()) {
//                临时去除 header 中的客户端标识
//                String urlString = request.url().toString();
//                if (urlString.endsWith("user/identity") && common.get(item).startsWith("CodingMart_Android")) {
//                    Logger.d("urlString " + urlString);
//                    continue;
//                }

                if (item.equalsIgnoreCase("Accept") && type == Type.Photo) {
                    accept.addHeader(item, "image/png,image/svg+xml,image/*;q=0.8,*/*;q=0.5");
                } else {
                    accept.addHeader(item, common.get(item));
                }
            }

            request = accept.build();
//            String url = request.url().toString();
            // 不是 coding 和 mart 就不添加 cookie
//            if (true) {
//                String sid = getCookie(context, url);
//                Request.Builder builder = request.newBuilder();
////                        .addHeader("Cookie", sid);
//
//                HashMap<String, String> headers = MyAsyncHttpClient.getMapHeaders();
//                for (String key : headers.keySet()) {
//                    if (!key.equals("Referer")) {
//                        builder.addHeader(key, headers.get(key));
//                    }
//                }
//
//                if (url.startsWith(Global.CODING_HOST)) {
//                    builder.addHeader("Referer", Global.CODING_HOST);
//                } else if (url.startsWith(Global.HOST)) {
//                    builder.addHeader("Referer", Global.HOST);
//                }
//
//                request = builder.build();
//            }

            Response proceed = chain.proceed(request);
            if (request.method().equals("GET")) {
                if (cacheType == CacheType.useCache) {
                    return proceed.newBuilder()
                            .removeHeader("Pragma")
                            .removeHeader("Cache-Control")
                            .header("Cache-Control", "public, max-age=" + 0)
                            .build();
                }
            }

            return proceed;
        };

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(generateClient(context, interceptorCookie, cacheType))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        return retrofit.create(MartRequest.class);
    }

    static OkHttpClient generateClient(Context context, Interceptor interceptorCookie, CacheType cacheType) {
        File httpCacheDirectory = new File(context.getCacheDir(), "HttpCache");
        Cache cache = new Cache(httpCacheDirectory, 100 * 1024 * 1024);

        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        ClearableCookieJar cookieJar =
                new MartPersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(context));
        return okHttpClient
                .cookieJar(cookieJar)
                .addInterceptor(interceptor)
                .addInterceptor(interceptorCookie)
//                .addNetworkInterceptor(chain -> {
//                    Request request = chain.request();
//
//                    if (request.method().equals("GET")) {
//                        if (cacheType == CacheType.onlyCache) {
//                            request = request.newBuilder()
//                                    .removeHeader("Cache-Control")
//                                    .header("Cache-Control", "public, only-if-cached, max-stale=" + MAX_STALE)
//                                    .build();
//                        } else if (cacheType == CacheType.useCache) {
//                            request = request.newBuilder()
//                                    .removeHeader("Cache-Control")
//                                    .header("Cache-Control", "public, max-age=0")
//                                    .build();
//                        }
//                    }
//
//                    Response proceed = chain.proceed(request);
//                    if (request.method().equals("GET")) {
//                        if (cacheType == CacheType.useCache) {
//                            return proceed.newBuilder()
//                                    .removeHeader("Pragma")
//                                    .removeHeader("Cache-Control")
//                                    .header("Cache-Control", "public, max-age=" + 0)
//                                    .build();
//                        }
//                    }
//
//                    return proceed;
//                })
//                .addInterceptor(interceptorCookie)
//                .addInterceptor(chain -> {
//                    Request request = chain.request();
//
//                    if (request.method().equals("GET")) {
//                        if (cacheType == CacheType.onlyCache) {
//                            request = request.newBuilder()
//                                    .removeHeader("Cache-Control")
//                                    .addHeader("Cache-Control", "public, only-if-cached, max-stale=" + MAX_STALE)
//                                    .build();
//                        }
//                    }
//
//                    Response proceed = chain.proceed(request);
////                    if (request.method().equals("GET")) {
////                        if (cacheType == CacheType.useCache) {
////                            return proceed.newBuilder()
////                                    .removeHeader("Pragma")
////                                    .removeHeader("Cache-Control")
////                                    .addHeader("Cache-Control", "public, max-age=" + 0)
////                                    .build();
////                        } else if (cacheType == CacheType.noCache) {
////                            return proceed.newBuilder()
////                                    .removeHeader("Pragma")
////                                    .removeHeader("Cache-Control")
////                                    .addHeader("Cache-Control", "public, only-if-cached, max-stale=" + MAX_STALE)
////                                    .build();
////                        }
////                    }
//
//                    if (request.method().equals("GET")) {
//                        if (cacheType == CacheType.onlyCache) {
//                            proceed = proceed.newBuilder()
//                                    .removeHeader("Pragma")
//                                    .removeHeader("Cache-Control")
//                                    .addHeader("Cache-Control", "public, only-if-cached, max-stale=" + MAX_STALE)
//                                    .build();
//                        }
//                    }
//
//                    return proceed;
//                })
                .cache(cache)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    public enum CacheType {
        noCache, // 不缓存数据, 仅使用网络
        useCache, // 有网络就用网络取到的数据, 没有就用 cache
        onlyCache // 只使用 cache
    }

    enum Type {
        JSON, // default
        Photo
    }


}
