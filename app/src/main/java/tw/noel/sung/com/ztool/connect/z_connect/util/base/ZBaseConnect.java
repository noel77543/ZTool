package tw.noel.sung.com.ztool.connect.z_connect.util.base;

import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.annotation.IntDef;
import androidx.annotation.Nullable;
import androidx.annotation.StringDef;

import com.google.gson.Gson;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.TlsVersion;
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
    protected WeakReference<Context> context;

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
        this.context = new WeakReference<Context>(context);
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(connectTimeOut, TimeUnit.MILLISECONDS)
                .writeTimeout(writeTimeOut, TimeUnit.MILLISECONDS)
                .readTimeout(readTimeOut, TimeUnit.MILLISECONDS)
                .connectionSpecs(Collections.singletonList(new ConnectionSpec.Builder(ConnectionSpec.COMPATIBLE_TLS)
                        .tlsVersions(TlsVersion.TLS_1_2, TlsVersion.TLS_1_1, TlsVersion.TLS_1_0)
                        .cipherSuites(
                                CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                                CipherSuite.TLS_ECDHE_ECDSA_WITH_CHACHA20_POLY1305_SHA256,
                                CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA,
                                CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA)
                        .build()))
                .build();
        gson = new Gson();
        zLoadingDialog = new ZLoadingDialog(this.context.get());
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
        ConnectivityManager connectivityManager = (ConnectivityManager) context.get().getSystemService(Context.CONNECTIVITY_SERVICE);
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
