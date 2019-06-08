package tw.noel.sung.com.ztool.connect.z_connect.util.base;

import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;

import com.google.gson.Gson;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import tw.noel.sung.com.ztool.connect.z_connect.util.dialog.ZLoadingDialog;

/**
 * Created by noel on 2019/1/21.
 */
public class ZBaseConnect {


    public final static MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

    public final static String _FILE_TYPE_JPG = "image/jpg";
    public final static String _FILE_TYPE_PNG = "image/png";
    public final static String _FILE_TYPE_CSV = "text/csv";


    @Retention(RetentionPolicy.SOURCE)
    @StringDef({_FILE_TYPE_JPG, _FILE_TYPE_PNG, _FILE_TYPE_CSV})
    public @interface uploadFileType {
    }

    protected OkHttpClient okHttpClient;
    private final int DEFAULT_TIME_OUT = 15 * 1000;
    protected int connectTimeOut = DEFAULT_TIME_OUT;
    protected int writeTimeOut = DEFAULT_TIME_OUT;
    protected int readTimeOut = DEFAULT_TIME_OUT;
    protected Request request;
    protected Gson gson;
    protected RequestBody requestBody;
    protected Context context;

    protected Dialog zLoadingDialog;


    public static final int SHOW_DIALOG = 77;
    public static final int DISMISS_DIALOG = 78;


    @Retention(RetentionPolicy.SOURCE)
    @IntDef({SHOW_DIALOG, DISMISS_DIALOG})
    public @interface LoadingDialogStatus {
    }

    public static final int SUCCESS_STRING = 80;
    public static final int SUCCESS_INPUTSTREAM = 81;
    public static final int FAIL = 82;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({SUCCESS_STRING, SUCCESS_INPUTSTREAM, FAIL})
    public @interface ConnectResponse {
    }


    public ZBaseConnect(Context context) {
        this.context = context;
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(connectTimeOut, TimeUnit.MILLISECONDS)
                .writeTimeout(writeTimeOut, TimeUnit.MILLISECONDS)
                .readTimeout(readTimeOut, TimeUnit.MILLISECONDS)
                .build();
        gson = new Gson();
        zLoadingDialog = new ZLoadingDialog(context);

    }


    //------------------

    /***
     *  自訂客製化dialog
     */
    public void setCustomLoadingDialog(@Nullable Dialog dialog) {
        zLoadingDialog = dialog;
    }

    //------------------

    /***
     * 連線time out
     */
    public void setConnectTimeOut(int connectTimeOut) {
        this.connectTimeOut = connectTimeOut;
        newClient();
    }

    //------------------

    /***
     * 寫入 time out
     */
    public void setWriteTimeOut(int writeTimeOut) {
        this.writeTimeOut = writeTimeOut;
        newClient();
    }
    //----------------

    /***
     * 讀取time out
     */
    public void setReadTimeOut(int readTimeOut) {
        this.readTimeOut = readTimeOut;
        newClient();
    }

    //-----------------

    /***
     * 加入攔截器
     */
    public void setInterceptor(Interceptor... interceptors) {
        for (Interceptor interceptor : interceptors) {
            okHttpClient.interceptors().add(interceptor);

        }
    }

    //------------------

    /**
     * 確認網路功能可用
     */
    protected boolean isNetWorkable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable();
    }

    //------------------

    /***
     * 再次建立client
     * @return
     */
    private void newClient() {
        okHttpClient = okHttpClient.newBuilder()
                .connectTimeout(connectTimeOut, TimeUnit.MILLISECONDS)
                .writeTimeout(writeTimeOut, TimeUnit.MILLISECONDS)
                .readTimeout(readTimeOut, TimeUnit.MILLISECONDS)
                .build();
    }
}
