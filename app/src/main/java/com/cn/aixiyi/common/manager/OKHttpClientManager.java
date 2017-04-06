package com.cn.aixiyi.common.manager;

import com.cn.aixiyi.Interface.MyCallBack;


/**
 * Created by sty on 2016/7/5.
 */
public class OKHttpClientManager {
//    private static final String IMGUR_CLIENT_ID = "9199fdef135c122";
//    private static final String TAG = "OKHttpClientManager";
//    private OkHttpClient mClient=new OkHttpClient();
//    private Call call=null;
//    private Request request=null;
//    private  Handler handler=new Handler(Looper.getMainLooper());
//    //私有化构造函数，防止被实例化
//    private OKHttpClientManager(){
//
//    }
//    //单例模式
//
//    private static class SingleHolder{
//        private  static  final OKHttpClientManager instance=new OKHttpClientManager();
//    }
//
//    public  static  final OKHttpClientManager getInstance(){
//        return  SingleHolder.instance;
//    }
    //Get请求
    public void OkHttpGet(String url,final MyCallBack callBack){
//        request=new Request.Builder()
//                .url(url)
//                .build();
//        call=mClient.newCall(request);
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                sendFailedStringCallback(request,e,callBack);
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if(response.isSuccessful()){
//                    String jsonStr=response.body().toString();
//                    sendSuccessResultCallback(jsonStr,callBack);
//                }
//            }
//        });

    }
    //Post请求
//    public void  OkHttpPost(final Activity activity,String url, Map<String,String> params, final MyCallBack callBack){
//        AppUtil.showProgress(activity, Contanct.DIALOG_SHOW);
//        FormEncodingBuilder builder=new FormEncodingBuilder();
//        if (params != null)
//        {
//            for (String key : params.keySet())
//            {
//                builder.add(key, params.get(key));
//            }
//        }
//        RequestBody requestBody=builder.build();
//        request=new Request.Builder()
//                .url(url)
//                .post(requestBody)
//                .build();
//        mClient.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log.i("onResponse","OKHttpClientManageronFailure=="+e.toString());
//                AppUtil.dismissProgress();
//                ShowDialogUtil showDialogUtil = new ShowDialogUtil(activity);
//                showDialogUtil.startWaitingDialog(activity,"请求失败!");
//                sendFailedStringCallback(request,e,callBack);
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                AppUtil.dismissProgress();
//                String string = response.body().string();
//                Log.i("onResponse",string);
//                if (response.isSuccessful()) {
//                    Log.e(TAG, "response ----->" + string);
//                    sendSuccessResultCallback( string,callBack);
//                }
//            }
//
//        });
//    }
//
//    //上传数据
//    public void  OkHttpFilePost(String url, File file,String param, final MyCallBack callBack){
//        RequestBody fileBody=RequestBody.create(MediaType.parse("image/png"),file);
//        RequestBody requestBody=new MultipartBuilder()
//                .type(MultipartBuilder.FORM)
//                .addFormDataPart("title",param)
//                .addFormDataPart("image",file.getName(),fileBody)
//                .build();
//        request=new Request.Builder()
//                .header("Authorization", "Client-ID " + IMGUR_CLIENT_ID)
//                .url(url)
//                .post(requestBody)
//                .build();
//        mClient.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Request request, IOException e) {
//                sendFailedStringCallback(request,e,callBack);
//            }
//
//            @Override
//            public void onResponse(Response response) throws IOException {
//                if(response.isSuccessful()){
//                    String jsonStr=response.body().toString();
//                    sendSuccessResultCallback(jsonStr,callBack);
//                }
//            }
//        });
//    }
//    //下载数据
//    public void  OkHttpDownLoadPost(String url, final File file, final MyCallBack callBack){
//
//        request=new Request.Builder()
//                .url(url)
//                .build();
//        mClient.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Request request, IOException e) {
//                sendFailedStringCallback(request,e,callBack);
//            }
//
//            @Override
//            public void onResponse(Response response) throws IOException {
//                if(response.isSuccessful()){
//                    InputStream is = null;
//                    byte[] buf = new byte[2048];
//                    int len = 0;
//                    int progress=0;
//                    FileOutputStream fos = null;
//                    try {
//                        is=response.body().byteStream();
//                        long total = response.body().contentLength();
//                        if (!file.exists()){
//                            file.mkdir();
//                        }
//                        fos=new FileOutputStream(file);
//                        long sum = 0;
//                        DialogUtil.startWaitingDialog();
//                        while ((len=is.read(buf))!=-1){
//                            fos.write(buf,0,len);
//                            progress = (int) (sum * 1.0f / total * 100);
//                        }
//                        DialogUtil.stop_WaitingDialog();
//                        Log.d(TAG, "progress=" + progress);
//                        sendSuccessResultCallback(String.valueOf(progress),callBack);
//                        fos.flush();
//                        Log.d(TAG, "文件下载成功");
//                    } catch (Exception e) {
//                        Log.d(TAG, "文件下载失败"+e);
//                    } finally {
//                        try {
//                            if (is != null)
//                                is.close();
//                        } catch (IOException e) {
//                        }
//                        try {
//                            if (fos != null)
//                                fos.close();
//                        } catch (IOException e) {
//                        }
//                    }
//
//                }
//            }
//        });
//    }
//    private void sendFailedStringCallback(final Request request, final Exception e, final  MyCallBack callback)
//    {
//
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if(callback!=null){
//                    callback.onError(request,e);
//                }
//            }
//        },500);
//    }
//
//    private void sendSuccessResultCallback(final String object, final MyCallBack callBack)
//    {
//
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (callBack!=null){
//                    callBack.onResponse(object);
//                }
//            }
//        },500);
//    }
}
