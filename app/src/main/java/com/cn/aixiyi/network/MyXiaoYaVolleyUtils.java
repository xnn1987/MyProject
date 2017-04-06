package com.cn.aixiyi.network;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cn.aixiyi.Interface.MyCallBack;
import com.cn.aixiyi.mode.Contanct;
import com.cn.aixiyi.utils.Base64ZipUtil;
import com.cn.aixiyi.utils.DebugLog;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xingdapeng on 2016/10/27.
 */
public class MyXiaoYaVolleyUtils {
    public static MyXiaoYaVolleyUtils Instance = null;
    private Handler handler = new Handler(Looper.getMainLooper());
    private static String header = "";
    private JSONObject jsonObject = null;
    private JSONArray jsonArray = null;
    private static RequestQueue requestQueue;// 请求队列
    private static RequestQueue requestQueueHttps;// 请求队列https
    private Context context;
    private boolean isHttps = false;

    //私有化构造函数，防止被实例化
    private MyXiaoYaVolleyUtils() {

    }

    /**
     * 普通http请求
     *
     * @param context
     * @return
     */
    public static MyXiaoYaVolleyUtils getIntance(final Context context) {
        if (Instance == null) {
            Instance = new MyXiaoYaVolleyUtils();
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
    public static MyXiaoYaVolleyUtils getIntance(final Context context, String httpsKey) {
        if (Instance == null) {
            Instance = new MyXiaoYaVolleyUtils();
        }
        Instance.context = context;
        Instance.isHttps = true;
        if (requestQueueHttps == null) {
            requestQueueHttps = Volley.newRequestQueue(context, new OkHttpStack(context, httpsKey));//HTTPS
        }
        return Instance;
    }

    //Get请求
    public synchronized void OkHttpGet(String url, final MyCallBack callBack) {

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
                getAuthHeader();
                headers.put("Authorization", "Basic " + header);
                return headers;
            }
        };
        addRequestQueue(jsonObjRequest);

    }

    /*
   * Post请求
   * 参数String
   * 两个header
   * X-Id
   * 返回string
   * */
    public synchronized void OkHttpPostStringOne(String url, final String headerOne, final String txt, final MyCallBack callBack) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
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
            public byte[] getBody() {

                try {
                    return txt.toString().getBytes("UTF-8");

                } catch (Exception e) {
                }
                return null;
            }

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                getAuthHeader();
                headers.put("Authorization", "Basic " + header);
                headers.put("X-Id", headerOne);
                return headers;
            }
        };

        addRequestQueue(stringRequest);
    }
    /*
 * Post请求
 * 参数String
 * 两个header,
 * Content_type
 * 返回string
 * */
    public synchronized void OkHttpPostStringTypeOne(String url, final String headerOne, final String txt, final MyCallBack callBack) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
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
            public byte[] getBody() {

                try {
                    return txt.toString().getBytes("UTF-8");

                } catch (Exception e) {
                }
                return null;
            }

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                getAuthHeader();
                headers.put("Authorization", "Basic " + header);
                headers.put("Content-Type", headerOne);
                return headers;
            }
        };

        addRequestQueue(stringRequest);
    }
    /*
   * Post请求
   * 参数String
   * 两个header
   * 返回json
   * */
    public synchronized void OkHttpPostJsonOne(String url, final String headerOne, final String txt, final MyCallBack callBack) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
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
            public byte[] getBody() {

                try {
                    return txt.toString().getBytes("UTF-8");

                } catch (Exception e) {
                }
                return null;
            }

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                getAuthHeader();
                headers.put("Authorization", "Basic " + header);
                headers.put("Content-Type", headerOne);
                return headers;
            }
        };

        addRequestQueue(jsonObjectRequest);
    }

    /*
  * Post请求
  * 参数Json
  * 两个header
  * */
    public synchronized void OkHttpPostTwo(String url, final String headerOne, String json, final MyCallBack callBack) {
        try {
            callBack.onBefore();
            jsonObject = new JSONObject(json);
        } catch (Exception e) {
            DebugLog.e("Response", "Exception=" + e);
        }
        JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
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
                HashMap<String, String> headers = new HashMap<String, String>();
                getAuthHeader();
                headers.put("Authorization", "Basic " + header);
                headers.put("Content-Type", headerOne);
                return headers;
            }
        };
        addRequestQueue(jsonRequest);
    }
    /*
      * Post请求
      * 参数JsonArray
      * 两个header
      * */
    public synchronized void OkHttpPostArrayTwo(String url, final String headerOne, String json, final MyCallBack callBack) {
        try {
            callBack.onBefore();
            jsonArray = new JSONArray(json);
        } catch (Exception e) {
            DebugLog.e("Response", "Exception=" + e);
        }
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, url, jsonArray,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
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
                HashMap<String, String> headers = new HashMap<String, String>();
                getAuthHeader();
                headers.put("Authorization", "Basic " + header);
                headers.put("Content-Type", headerOne);
                return headers;
            }
        };
        addRequestQueue(jsonArrayRequest);
    }
    /*
    * Post请求
    * 参数Json
    * 三个header
    * */
    public synchronized void OkHttpPostThree(String url, final String headerOne, final String headerTwo, String json, final MyCallBack callBack) {
        try {
            callBack.onBefore();
            jsonObject = new JSONObject(json);
        } catch (Exception e) {
            DebugLog.e("Response", "Exception=" + e);
        }
        JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
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
                HashMap<String, String> headers = new HashMap<String, String>();
                getAuthHeader();
                headers.put("Authorization", "Basic " + header);
                headers.put("Content-Type", headerOne);
                headers.put("Accept", headerTwo);
                return headers;
            }
        };
        addRequestQueue(jsonRequest);
    }

     /*
    * Post请求上传文件..........
   * */
    public synchronized void OkHttpPostFiles(String url, final String headerOne, List<File> list, final MyCallBack callBack) {
        Request request = new PostUploadRequest(url, list, headerOne, null,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        DebugLog.e("Response", "JSONObject=" + response);
                        sendSuccessResultCallback(response, callBack);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        DebugLog.e("Response", "VolleyError=" + error);
                        sendFailedStringCallback(error, callBack);

                    }
                });
        addRequestQueue(request);
    }
    public final static String TAG = "HTTP";
    private final static int CONNECT_TIME = 10000;
    private final static int READ_TIME = 10000;
    private static final String LINE_END = "\r\n";
    private String BOUNDARY = "------WebKitFormBoundaryxEYDlDD5QLOIWooS--"; // 数据分隔线
    private String MULTIPART_FORM_DATA = "multipart/form-data";
    /*
 * Post请求上传文件..........
* */
    public synchronized void PostFiles(String urlstr, final String headerOne, File file, final MyCallBack callBack){

        OutputStream outStream=null;
        StringBuffer sb = new StringBuffer();
        sb.append( BOUNDARY);
        sb.append(LINE_END);
        sb.append("Content-Disposition: form-data;");
        sb.append("name=\"");
        sb.append("object\"");
        sb.append(LINE_END);
        sb.append(LINE_END);
        sb.append("{\"name\":\"\"HellyyyyLog\",\"type\":\"text/plain\"}");
        sb.append(LINE_END);
        sb.append( BOUNDARY);
        sb.append(LINE_END);
        sb.append("Content-Disposition: form-data;");
        sb.append("name=\"");
        sb.append("filesize\"");
        sb.append(LINE_END);
        sb.append(LINE_END);
        sb.append(dataToSize(file.getAbsolutePath()));
        sb.append(LINE_END);
        sb.append( BOUNDARY);
        sb.append(LINE_END);
        sb.append("Content-Disposition: form-data;");
        sb.append("name=\"");
        sb.append("file\"");
        sb.append("; filename=\"");
        sb.append("logggg1.txt\"");
        sb.append(LINE_END);
        sb.append("Content-Type: ");
        sb.append("text/plain");
        sb.append(LINE_END);
        sb.append(LINE_END);
        sb.append("{\n" +
                "  \"managedObjects\": [\n" +
                "    {\n" +
                "      \"id\": \"13166\",\n" +
                "      \"lastUpdated\": \"2016-10-27T07:25:08.496Z\",\n" +
                "      \"name\": \"HelloWorld\",\n" +
                "      \"owner\": \"xiaoya\",\n" +
                "      \"self\": \"http://washingmachine.thhc.com.cn/inventory/binaries/13166\",\n" +
                "      \"type\": \"text/plain\",\n" +
                "      \"c8y_IsBinary\": \"\",\n" +
                "      \"length\": 145,\n" +
                "      \"contentType\": \"application/octet-stream\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"statistics\": {\n" +
                "    \"currentPage\": 1,\n" +
                "    \"pageSize\": 5,\n" +
                "    cfxx\"totalPages\": 1\n" +
                "  },\n" +
                "  \"self\": \"http://washingmachine.thhc.com.cn/inventory/binaries?pageSize=5&currentPage=1\"\n" +
                "}");
        sb.append(LINE_END);
        sb.append( BOUNDARY);
        sb.append(LINE_END);
        byte[] entity = sb.toString().getBytes();
        try{
            HttpURLConnection connection = getHttpURLConnection(urlstr, "POST");

            connection.setDoOutput(true);// 允许对外输出数据
             outStream = connection.getOutputStream();
             outStream.write(entity);
//             outStream.write(dataToByteArray(file.getAbsolutePath()));
//             outStream.write(LINE_END.getBytes("utf-8"));
//            	/* 结尾行 */
//            // `"--" + BOUNDARY + "--" + LINE_END`
//            String endLine = "--" + BOUNDARY + "--" + LINE_END;
//            try {
//                outStream.write(endLine.toString().getBytes("utf-8"));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            if (connection.getResponseCode() == 200) {// 成功返回处理数据
//             }
                InputStream inStream = connection.getInputStream();
                byte[] number = read(inStream);
                String json = new String(number);
                DebugLog.e("Response", "JSONObject=" + json);
                sendSuccessResultCallback(json, callBack);

        }
        catch (IOException e){
        }


    }
    /*
    * Put请求
   * 参数Json
   * 两个header
   * */
    public synchronized void OkHttpPutOne(String url, final String headerOne, String json, final MyCallBack callBack) {
        try {
            callBack.onBefore();
            jsonObject = new JSONObject(json);
            DebugLog.e("Response", "JSONObject=" + jsonObject);
        } catch (Exception e) {
            DebugLog.e("Response", "Exception=" + e);
        }
        JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.PUT, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
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
                HashMap<String, String> headers = new HashMap<String, String>();
                getAuthHeader();
                headers.put("Authorization", "Basic " + header);
                headers.put("Content-Type", headerOne);
                return headers;
            }
        };
        addRequestQueue(jsonRequest);
    }

     /*
    * Put请求
    * 参数Json
    * 三个header
    * */
    public synchronized void OkHttpPutTwo(String url, final String headerOne, final String headerTwo, String json, final MyCallBack callBack) {
        try {
            callBack.onBefore();
            jsonObject = new JSONObject(json);
            DebugLog.e("Response", "JSONObject=" + jsonObject);
        } catch (Exception e) {
            DebugLog.e("Response", "Exception=" + e);
        }
        JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.PUT, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
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
                HashMap<String, String> headers = new HashMap<String, String>();
                getAuthHeader();
                headers.put("Authorization", "Basic " + header);
                headers.put("Content-Type", headerOne);
                headers.put("Accept", headerTwo);
                return headers;
            }
        };
        addRequestQueue(jsonRequest);
    }


    /*
    * Delete请求
    * 一个header
    * */
    public synchronized void OkHttpDeleteOne(String url, final MyCallBack callBack) {

        callBack.onBefore();
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url,
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
                getAuthHeader();
                headers.put("Authorization", "Basic " + header);
                return headers;
            }
        };
        addRequestQueue(stringRequest);

    }
    /*
       * Delete请求
       * 两个header
       * */
    public synchronized void OkHttpDeleteTwo(String url, final String headerOne,final MyCallBack callBack) {

        callBack.onBefore();
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url,
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
                getAuthHeader();
                headers.put("Authorization", "Basic " + header);
                headers.put("Content-Type", headerOne);
                return headers;
            }
        };
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
    * 获取头文件Authorization转化为Base64
    * return header
    * */
    private static String getAuthHeader() {
        header = Base64ZipUtil.encodeBase64File(Contanct.USER_NAME);
        header = header.substring(0, header.length() - 1);
        return header;
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

    private HttpURLConnection getHttpURLConnection(String urlstr, String method)
            throws IOException {
        URL url = new URL(urlstr);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(CONNECT_TIME);
        connection.setReadTimeout(READ_TIME);
        connection.setRequestMethod(method);

        // 头字段
        connection.setRequestProperty("Authorization", "Basic " + header);
        connection.setRequestProperty("Content-Type", "multipart/form-data");// 头字段

        return connection;

    }

    /**
     * 读取输入流数据 InputStream
     *
     * @param inStream
     * @return
     * @throws java.io.IOException
     */
    public static byte[] read(InputStream inStream) throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }
    public static byte[] dataToByteArray(String path) {
        String fileContent="";
        try {
            File inFile = new File(path);
            FileInputStream fis = new FileInputStream(inFile);
            byte[] buffer = new byte[1024];
            fis.read(buffer);
            // 对读取的数据进行编码以防止乱码
//            fileContent = EncodingUtils.getString(buffer, "UTF-8");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return fileContent.getBytes();
    }
    public static byte[] bitmapToByteArray(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options); // 此时返回 bm 为空
        options.inJustDecodeBounds = false; // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = (int) (options.outHeight / (float) 320);
        if (be <= 0)
            be = 1;
        options.inSampleSize = be; // 重新读入图片，注意此时已经
        bitmap = BitmapFactory.decodeFile(path, options);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public static byte[] fileToBetyArray(File file) {
        FileInputStream fileInputStream = null;
        byte[] bFile = null;
        try {
            bFile = new byte[(int) file.length()];
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bFile);
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fileInputStream.close();
                bFile.clone();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bFile;
    }
    public static long dataToSize(String path) {
        long size = 0;
        File inFile = new File(path);
        if (inFile.exists()){
            try {
                FileInputStream fis = null;
                fis = new FileInputStream(inFile);
                size = fis.available();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        return size;
    }
}
