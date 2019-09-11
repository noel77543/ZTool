package tw.noel.sung.com.ztool.connect.z_connect;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import tw.noel.sung.com.ztool.R;
import tw.noel.sung.com.ztool.connect.z_connect.util.base.ZBaseConnect;
import tw.noel.sung.com.ztool.connect.z_connect.util.callback.ZConnectHandler;

/**
 * Created by noel on 2019/1/21.
 */
public class ZConnect extends ZBaseConnect {


    public ZConnect(Context context) {
        super(context);
    }


    //---------------

    /***
     * 使用情境:
     * 1.http get
     *  ... ... ... ... ... ...
     * @param apiURL url
     * @param zConnectHandler callback
     */
    public void get(String apiURL, ZConnectHandler zConnectHandler) {
        Context context = this.context.get();
        if (context != null) {
            if (!isNetWorkable()) {
                Toast.makeText(context, context.getString(R.string.z_connect_net_work_not_work), Toast.LENGTH_SHORT).show();
                return;
            }
            request = new Request.Builder()
                    .url(apiURL)
                    .get()
                    .build();
            startConnect(request, zConnectHandler);
        } else {
            throw new NullPointerException("Context 不存在");
        }

    }

    //---------------


    /***
     *  使用情境 :
     *  1. http get
     *  2. params
     *  ... ... ... ... ... ...
     * @param apiURL url
     * @param headers header
     * @param zConnectHandler callback
     */
    public void get(String apiURL, Map<String, String> headers, ZConnectHandler zConnectHandler) {
        Context context = this.context.get();
        if (context != null) {
            if (!isNetWorkable()) {
                Toast.makeText(context, context.getString(R.string.z_connect_net_work_not_work), Toast.LENGTH_SHORT).show();
                return;
            }
            Request.Builder builder = new Request.Builder()
                    .url(apiURL)
                    .get();

            for (String key : headers.keySet()) {
                builder.addHeader(key, headers.get(key));
            }

            request = builder.build();
            startConnect(request, zConnectHandler);
        } else {
            throw new NullPointerException("Context 不存在");
        }
    }


    //-----------------


    /***
     *   使用情境 :
     *   1.http post
     *   2.header
     *   3.params
     *  ... ... ... ... ... ...
     * @param apiURL url
     * @param headers header
     * @param params keyValue 參數
     * @param zConnectHandler callback
     */
    public void post(String apiURL, Map<String, String> headers, Map<String, String> params, ZConnectHandler zConnectHandler) {
        Context context = this.context.get();
        if (context != null) {
            if (!isNetWorkable()) {
                Toast.makeText(context, context.getString(R.string.z_connect_net_work_not_work), Toast.LENGTH_SHORT).show();
                return;
            }
            Request.Builder builder = new Request.Builder()
                    .url(apiURL);

            if (headers != null) {
                for (String key : headers.keySet()) {
                    builder.addHeader(key, headers.get(key));
                }
            }


            FormBody.Builder formBodyBuilder = new FormBody.Builder();
            if (params != null) {
                for (String key : params.keySet()) {
                    formBodyBuilder.add(key, params.get(key));
                }
            }
            requestBody = formBodyBuilder.build();
            request = builder.post(requestBody).build();
            startConnect(request, zConnectHandler);
        } else {
            throw new NullPointerException("Context 不存在");
        }
    }


    //-----------------

    /***
     *  使用情境:
     *  1. http post
     *  2. header
     *  3. json request
     *  ... ... ... ... ... ...
     * @param apiURL url
     * @param headers header
     * @param requestModel  json object model
     * @param zConnectHandler callback
     */
    public void post(String apiURL, Map<String, String> headers, Object requestModel, ZConnectHandler zConnectHandler) {

        Context context = this.context.get();
        if (context != null) {
            if (!isNetWorkable()) {
                Toast.makeText(context, context.getString(R.string.z_connect_net_work_not_work), Toast.LENGTH_SHORT).show();
                return;
            }
            Request.Builder builder = new Request.Builder()
                    .url(apiURL);

            if (headers != null) {
                for (String key : headers.keySet()) {
                    builder.addHeader(key, headers.get(key));
                }
            }

            requestBody = RequestBody.create(MEDIA_TYPE_JSON, gson.toJson(requestModel));
            request = builder.post(requestBody).build();
            startConnect(request, zConnectHandler);
        } else {
            throw new NullPointerException("Context 不存在");
        }
    }


    //-----------------------

    /***
     *  1. http post
     *  2. 上傳檔案
     *  3. params
     *  4. header
     *  ... ... ... ... ... ...
     * @param apiURL url
     * @param headers 如果不需headers 則欄位帶入null即可
     * @param params 如果不需params 則欄位帶入null即可
     * @param fileKey 後台此欄位key值
     * @param fileName 檔名
     * @param file 檔案
     * @param fileType 檔案類型 參考 @uploadFileType
     * @param zConnectHandler callback
     */
    public void post(String apiURL, Map<String, String> headers, Map<String, String> params, String fileKey, String fileName, File file, @uploadFileType String fileType, ZConnectHandler zConnectHandler) {
        Context context = this.context.get();
        if (context != null) {
            if (!isNetWorkable()) {
                Toast.makeText(context, context.getString(R.string.z_connect_net_work_not_work), Toast.LENGTH_SHORT).show();
                return;
            }
            MultipartBody.Builder builder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart(fileKey, fileName, RequestBody.create(MediaType.parse(fileType), file));

            if (params != null) {
                for (String key : params.keySet()) {
                    builder.addFormDataPart(key, params.get(key));
                }
            }

            Request.Builder requestBuilder = new Request.Builder()
                    .url(apiURL).post(builder.build());

            if (headers != null) {
                for (String key : headers.keySet()) {
                    requestBuilder.addHeader(key, headers.get(key));
                }
            }

            request = requestBuilder.build();
            startConnect(request, zConnectHandler);
        } else {
            throw new NullPointerException("Context 不存在");
        }
    }

    //----------------------

    /**
     * 1. http post
     * 2. 上傳檔案
     * 3. json request
     * 4. header
     * ... ... ... ... ... ...
     *
     * @param apiURL          url
     * @param headers         如果不需header 則欄位帶入null即可
     * @param requestModel    如果不需requestModel 則欄位帶入null即可
     * @param fileKey         後台此欄位key值
     * @param fileName        檔名
     * @param file            檔案
     * @param fileType        檔案類型 參考 @uploadFileType
     * @param zConnectHandler callback
     */
    public void post(String apiURL, Map<String, String> headers, Object requestModel, String fileKey, String fileName, File file, @uploadFileType String fileType, ZConnectHandler zConnectHandler) {
        Context context = this.context.get();
        if (context != null) {
            if (!isNetWorkable()) {
                Toast.makeText(context, context.getString(R.string.z_connect_net_work_not_work), Toast.LENGTH_SHORT).show();
                return;
            }

            MultipartBody.Builder builder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart(fileKey, fileName, RequestBody.create(MediaType.parse(fileType), file));

            if (requestModel != null) {
                builder.addPart(RequestBody.create(MEDIA_TYPE_JSON, gson.toJson(requestModel)));
            }


            Request.Builder requestBuilder = new Request.Builder()
                    .url(apiURL).post(builder.build());

            if (headers != null) {
                for (String key : headers.keySet()) {
                    requestBuilder.addHeader(key, headers.get(key));
                }
            }
            request = requestBuilder.build();
            startConnect(request, zConnectHandler);
        } else {
            throw new NullPointerException("Context 不存在");
        }
    }


    //-----------

    /***
     * 進行連線
     * @param request
     */
    private void startConnect(final Request request, final ZConnectHandler zConnectHandler) {

        @SuppressLint("HandlerLeak") final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                int type = msg.what;
                switch (type) {
                    //show loading dialog
                    case SHOW_DIALOG:
                        if (zLoadingDialog != null) {
                            zLoadingDialog.show();
                        }
                        break;
                    //dismiss loading dialog
                    case DISMISS_DIALOG:
                        //當完成連線時 如果沒有其他正在連線中的執行緒才dismiss dialog
                        if (zLoadingDialog != null && okHttpClient.dispatcher().runningCallsCount() == 0) {
                            zLoadingDialog.dismiss();
                        }
                        break;
                    case SUCCESS_INPUTSTREAM:
                        zConnectHandler.OnInputStreamResponse((new BufferedInputStream((InputStream) msg.obj, 1024)), msg.arg1);
                        break;
                    case SUCCESS_STRING:
                        zConnectHandler.OnStringResponse((String) msg.obj, msg.arg1);
                        break;
                    case FAIL:
                        if (msg.obj instanceof String) {
                            zConnectHandler.OnFail((String) msg.obj, msg.arg1);
                        } else {
                            zConnectHandler.OnFail((IOException) msg.obj);
                        }
                        break;
                }
            }
        };

        displayLoadingDialog(SHOW_DIALOG, handler);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = okHttpClient.newCall(request).execute();
                    int code = response.code();
                    if (response.isSuccessful()) {
                        displayLoadingDialog(DISMISS_DIALOG, handler);

                        ResponseBody responseBody = response.body();
                        BufferedSource source = responseBody.source();
                        source.request(Long.MAX_VALUE);
                        Buffer buffer = source.buffer();


                        String responseBodyString = buffer.clone().readString(Charset.forName("UTF-8"));
                        InputStream responseBodyInputStream = buffer.clone().inputStream();

                        displayResponse(SUCCESS_STRING, responseBodyString, code, handler);
                        displayResponse(SUCCESS_INPUTSTREAM, responseBodyInputStream, code, handler);
                        source.close();
                    } else {
                        displayLoadingDialog(DISMISS_DIALOG, handler);
                        displayResponse(FAIL, response.body().string(), code, handler);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    displayLoadingDialog(DISMISS_DIALOG, handler);
                    displayResponse(FAIL, e, 0, handler);
                }
            }
        }).start();

    }


    //----------------

    /***
     *  顯示/隱藏 loading dialog
     * @param status
     */
    private void displayLoadingDialog(@LoadingDialogStatus int status, Handler handler) {
        Message message = Message.obtain();
        message.what = status;
        handler.sendMessage(message);
    }
    //----------------

    /***
     * 事件回傳
     * @param type
     * @param object
     * @param handler
     */
    private void displayResponse(@ConnectResponse int type, Object object, int statusCode, Handler handler) {
        Message message = Message.obtain();
        message.what = type;
        message.obj = object;
        message.arg1 = statusCode;
        handler.sendMessage(message);
    }

}