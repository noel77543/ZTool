package tw.noel.sung.com.ztool.connect;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
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
import tw.noel.sung.com.ztool.connect.util.base.ZBaseConnect;
import tw.noel.sung.com.ztool.connect.util.implement.ZConnectHandler;

/**
 * Created by noel on 2019/1/21.
 */
public class ZConnect extends ZBaseConnect implements Callback {

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
        if (!isNetWorkable()) {
            Toast.makeText(context, context.getString(R.string.z_connect_net_work_not_work), Toast.LENGTH_SHORT).show();
            return;
        }
        this.zConnectHandler = zConnectHandler;
        displayLoadingDialog(SHOW_DIALOG);
        request = new Request.Builder()
                .url(apiURL)
                .get()
                .build();
        okHttpClient.newCall(request).enqueue(this);

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

        if (!isNetWorkable()) {
            Toast.makeText(context, context.getString(R.string.z_connect_net_work_not_work), Toast.LENGTH_SHORT).show();
            return;
        }
        this.zConnectHandler = zConnectHandler;
        displayLoadingDialog(SHOW_DIALOG);

        Request.Builder builder = new Request.Builder()
                .url(apiURL)
                .get();

        for (String key : headers.keySet()) {
            builder.addHeader(key, headers.get(key));
        }

        request = builder.build();
        okHttpClient.newCall(request).enqueue(this);

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
    public void post(String apiURL, @Nullable Map<String, String> headers, @Nullable Map<String, String> params, ZConnectHandler zConnectHandler) {
        if (!isNetWorkable()) {
            Toast.makeText(context, context.getString(R.string.z_connect_net_work_not_work), Toast.LENGTH_SHORT).show();
            return;
        }
        this.zConnectHandler = zConnectHandler;
        displayLoadingDialog(SHOW_DIALOG);

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

        okHttpClient.newCall(request).enqueue(this);

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
    public void post(String apiURL, @Nullable Map<String, String> headers, Object requestModel, ZConnectHandler zConnectHandler) {
        if (!isNetWorkable()) {
            Toast.makeText(context, context.getString(R.string.z_connect_net_work_not_work), Toast.LENGTH_SHORT).show();
            return;
        }
        this.zConnectHandler = zConnectHandler;

        displayLoadingDialog(SHOW_DIALOG);

        Request.Builder builder = new Request.Builder()
                .url(apiURL);

        if (headers != null) {
            for (String key : headers.keySet()) {
                builder.addHeader(key, headers.get(key));
            }
        }

        requestBody = RequestBody.create(MEDIA_TYPE_JSON, gson.toJson(requestModel));
        request = builder.post(requestBody).build();
        okHttpClient.newCall(request).enqueue(this);
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
    public void post(String apiURL, @Nullable Map<String, String> headers, @Nullable Map<String, String> params, String fileKey, String fileName, File file, @uploadFileType String fileType, ZConnectHandler zConnectHandler) {
        if (!isNetWorkable()) {
            Toast.makeText(context, context.getString(R.string.z_connect_net_work_not_work), Toast.LENGTH_SHORT).show();
            return;
        }
        this.zConnectHandler = zConnectHandler;

        displayLoadingDialog(SHOW_DIALOG);

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
        okHttpClient.newCall(request).enqueue(this);

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
    public void post(String apiURL, @Nullable Map<String, String> headers, @Nullable Object requestModel, String fileKey, String fileName, File file, @uploadFileType String fileType, ZConnectHandler zConnectHandler) {
        if (!isNetWorkable()) {
            Toast.makeText(context, context.getString(R.string.z_connect_net_work_not_work), Toast.LENGTH_SHORT).show();
            return;
        }
        this.zConnectHandler = zConnectHandler;

        displayLoadingDialog(SHOW_DIALOG);


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
        okHttpClient.newCall(request).enqueue(this);
    }


    //-----------------

    /***
     * 連線失敗
     * @param call
     * @param e
     */
    @Override
    public void onFailure(Call call, IOException e) {
        displayLoadingDialog(DISMISS_DIALOG);
        fail();
    }

    //----------

    /***
     * 連線成功
     * @param call
     * @param response
     */
    @Override
    public void onResponse(Call call, Response response) {
        displayLoadingDialog(DISMISS_DIALOG);


        try {
            ResponseBody responseBody = response.body();
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE);
            Buffer buffer = source.buffer();


            String responseBodyString = buffer.clone().readString(Charset.forName("UTF-8"));
            InputStream responseBodyInputStream = buffer.clone().inputStream();
            int code = response.code();

            success(responseBodyString, code);
            success(new BufferedInputStream(responseBodyInputStream,1024), code);
            source.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
