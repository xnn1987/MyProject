package com.cn.aixiyi.network;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cn.aixiyi.Interface.MyCallBack;
import com.cn.aixiyi.app.MyApplication;
import com.cn.aixiyi.utils.DebugLog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/3.
 */
public class MyYingShiVolleyUtils {
    public static MyYingShiVolleyUtils Instance = null;
    private Handler handler = new Handler(Looper.getMainLooper());
    private static String header = "";
    private JSONObject jsonObject = null;
    private JSONArray jsonArray = null;
    private static RequestQueue requestQueue;// 请求队列
    private static RequestQueue requestQueueHttps;// 请求队列https
    private Context context;
    private boolean isHttps = false;

    /**
     * 普通http请求
     *
     * @param context
     * @return
     */
    public static MyYingShiVolleyUtils getIntance(final Context context) {
        if (Instance == null) {
            Instance = new MyYingShiVolleyUtils();
        }
        Instance.context = context;
        Instance.isHttps = false;
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        }
        return Instance;
    }


    /**
     * https请求
     *
     * @param context
     * @return
     */
    public static MyYingShiVolleyUtils getIntance(final Context context, String httpsKey) {
        if (Instance == null) {
            Instance = new MyYingShiVolleyUtils();
        }
        Instance.context = context;
        Instance.isHttps = true;
        if (requestQueueHttps == null) {
            //单项认证
            requestQueueHttps = Volley.newRequestQueue(context, new OkHttpStack(context, httpsKey));//HTTPS
        }
        return Instance;
    }

    /*
* Get请求
* 参数String
* Content_type
* 返回JsonObject
* */
    public synchronized void OkHttpGetJson(String url, final MyCallBack callBack) {
        callBack.onBefore();
        JsonObjectRequest jsonObjRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        DebugLog.e("Response", "JSONObject=" + response);
                        sendSuccessResultCallback(response, callBack);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                DebugLog.e("Response", "VolleyError=" + error);
                sendFailedStringCallback(error, callBack);

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                return headers;
            }

        };
        addRequestQueue(jsonObjRequest);

    }
    /*
* Get请求
* 参数String
* Content_type
* 返回String
* */
    public synchronized void OkHttpGetLoadingString(String url, final MyCallBack callBack) {
        StringRequest loadingRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //     DebugLog.e("Response", "JSONObject=" + response);
                        sendSuccessResultCallback(response, callBack);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                DebugLog.e("Response", "VolleyError=" + error);
                sendFailedStringCallback(error, callBack);

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                Log.i("Response","getrawCookies="+MyApplication.cookies+"\n");
                headers.put("Cookie",MyApplication.cookies);
                DebugLog.e("Response", "Cookie=" + MyApplication.cookies);
                return headers;
            }
        };
        loadingRequest.setRetryPolicy( new DefaultRetryPolicy( 50000,//默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,//默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT ) );
        addRequestQueue(loadingRequest);

    }
    /*
  * Get请求
  * 参数String
  * Content_type
  * 返回String
  * */
    public synchronized void OkHttpGetString(String url, final MyCallBack callBack) {
        callBack.onBefore();
        StringRequest jsonObjRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        DebugLog.e("Response", "JSONObject=" + response);
                        sendSuccessResultCallback(response, callBack);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                DebugLog.e("Response", "VolleyError=" + error);
                sendFailedStringCallback(error, callBack);

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                Log.i("Response","getrawCookies="+MyApplication.cookies+"\n");
                headers.put("Cookie",MyApplication.cookies);
                DebugLog.e("Response", "Cookie=" + MyApplication.cookies);
                return headers;
            }
        };
        jsonObjRequest.setRetryPolicy( new DefaultRetryPolicy( 50000,//默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,//默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT ) );
        addRequestQueue(jsonObjRequest);

    }
    /*
* Post请求
* 参数String
* Content_type
* 返回string
* */
    public synchronized void OkHttpPostLoadingStringTypeOne(String url, final String txt, final MyCallBack callBack) {
        StringRequest stringLoadingRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // DebugLog.e("Response", "MainActivity=" + response.toString());
                sendSuccessResultCallback(response, callBack);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                DebugLog.e("Response", "VolleyError=" + error);
                sendFailedStringCallback(error, callBack);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                //在这里设置需要post的参数
                Map<String, String> map = new HashMap<String, String>();
                map.put("data", txt);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                Log.i("Response","postrawCookies="+MyApplication.cookies+"\n");
                headers.put("User-Agent", "aixiyi");
                headers.put("Cookie",MyApplication.cookies);
                return headers;
            }
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {

                    Map<String, String> responseHeaders = response.headers;
                    String rawCookies = responseHeaders.get("Set-Cookie");
                    if (!TextUtils.isEmpty(rawCookies)){
                        if (MyApplication.needcookies){
                            MyApplication.cookies=rawCookies;
                            MyApplication.needcookies=false;
                            Log.i("Response","rawCookies="+rawCookies+"\n");
                        }
                    }
                    String dataString = new String(response.data, "UTF-8");
                    Log.i("Response","dataString="+dataString);
                    return Response.success(dataString, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                }
            }
        };
        stringLoadingRequest.setRetryPolicy( new DefaultRetryPolicy( 50000,//默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,//默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT ) );
        addRequestQueue(stringLoadingRequest);
    }
    /*
* Post请求
* 参数String
* Content_type
* 返回string
* */
    public synchronized void OkHttpPostStringTypeOne(String url, final String txt, final MyCallBack callBack) {
        callBack.onBefore();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               // DebugLog.e("Response", "MainActivity=" + response.toString());
                sendSuccessResultCallback(response, callBack);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                DebugLog.e("Response", "VolleyError=" + error);
                sendFailedStringCallback(error, callBack);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                //在这里设置需要post的参数
                Map<String, String> map = new HashMap<String, String>();
                map.put("data", txt);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                Log.i("Response","postrawCookies="+MyApplication.cookies+"\n");
                headers.put("User-Agent", "aixiyi");
                headers.put("Cookie",MyApplication.cookies);
                return headers;
            }
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {

                    Map<String, String> responseHeaders = response.headers;
                    String rawCookies = responseHeaders.get("Set-Cookie");
                    if (!TextUtils.isEmpty(rawCookies)){
                        if (MyApplication.needcookies){
                            MyApplication.cookies=rawCookies;
                            MyApplication.needcookies=false;
                            Log.i("Response","rawCookies="+rawCookies+"\n");
                        }
                    }
                    String dataString = new String(response.data, "UTF-8");
                    Log.i("Response","dataString="+dataString);
                    return Response.success(dataString, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                }
            }
        };
        stringRequest.setRetryPolicy( new DefaultRetryPolicy( 50000,//默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,//默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT ) );
        addRequestQueue(stringRequest);
    }
    /*
* Post请求
* 参数String
* Content_type
* 返回string
* */
    public synchronized void OkHttpPostStringTypeOne(String url, final MyCallBack callBack) {
        callBack.onBefore();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // DebugLog.e("Response", "MainActivity=" + response.toString());
                sendSuccessResultCallback(response, callBack);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                DebugLog.e("Response", "VolleyError=" + error);
                sendFailedStringCallback(error, callBack);
            }
        }) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                Log.i("Response","postonerawCookies="+MyApplication.cookies+"\n");
                headers.put("User-Agent", "aixiyi");
                headers.put("Cookie",MyApplication.cookies);
                return headers;
            }
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {

                    Map<String, String> responseHeaders = response.headers;
                    String rawCookies = responseHeaders.get("Set-Cookie");
                    if (!TextUtils.isEmpty(rawCookies)){
                        if (MyApplication.needcookies){
                            MyApplication.cookies=rawCookies;
                            MyApplication.needcookies=false;
                            Log.i("Response","rawCookies="+rawCookies+"\n");
                        }
                    }
                    String dataString = new String(response.data, "UTF-8");
                    Log.i("Response","dataString="+dataString);
                    return Response.success(dataString, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                }
            }
        };
        stringRequest.setRetryPolicy( new DefaultRetryPolicy( 50000,//默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,//默认最大尝试次数
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT ) );
        addRequestQueue(stringRequest);
    }

    private void sendFailedStringCallback(final Exception e, final MyCallBack callback) {

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onError(e);
                    callback.onAfter();
                }
            }
        }, 500);
    }

    private void sendSuccessResultCallback(final Object object, final MyCallBack callBack) {

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    callBack.onResponse(object);
                    callBack.onAfter();
                }
            }
        }, 500);
    }

    /*
    * 判断是http请求还是https请求
    * return void
    *  */
    private void addRequestQueue(Request request) {
        if (isHttps) {
            requestQueueHttps.add(request);
        } else {
            requestQueue.add(request);
        }
    }
}
