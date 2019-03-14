package tw.noel.sung.com.ztool.connect.util.base;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;

import com.google.gson.Gson;

import java.io.InputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import tw.noel.sung.com.ztool.connect.util.dialog.ZLoadingDialog;
import tw.noel.sung.com.ztool.connect.util.implement.ZConnectHandler;

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


    private final int DEFAULT_TIME_OUT = 15 * 1000;
    private int connectTimeOut = DEFAULT_TIME_OUT;
    private int writeTimeOut = DEFAULT_TIME_OUT;
    private int readTimeOut = DEFAULT_TIME_OUT;


    protected static OkHttpClient okHttpClient;
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

    public static final int SUCCESS_STRING = 79;
    public static final int SUCCESS_INPUT_STREAM = 80;
    public static final int FAIL = 81;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({SUCCESS_STRING, SUCCESS_INPUT_STREAM, FAIL})
    public @interface ConnectResult {
    }

    protected ZConnectHandler zConnectHandler;


    public ZBaseConnect(Context context) {
        this.context = context;
        gson = new Gson();
        zLoadingDialog = new ZLoadingDialog(context);
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(connectTimeOut, TimeUnit.MILLISECONDS)
                .writeTimeout(writeTimeOut, TimeUnit.MILLISECONDS)
                .readTimeout(readTimeOut, TimeUnit.MILLISECONDS)
                .build();
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
        okHttpClient = okHttpClient.newBuilder()
                .connectTimeout(connectTimeOut, TimeUnit.MILLISECONDS)
                .writeTimeout(writeTimeOut, TimeUnit.MILLISECONDS)
                .readTimeout(readTimeOut, TimeUnit.MILLISECONDS)
                .build();
    }

    //------------------

    /***
     * 寫入 time out
     */
    public void setWriteTimeOut(int writeTimeOut) {
        this.writeTimeOut = writeTimeOut;
        okHttpClient = okHttpClient.newBuilder()
                .connectTimeout(connectTimeOut, TimeUnit.MILLISECONDS)
                .writeTimeout(writeTimeOut, TimeUnit.MILLISECONDS)
                .readTimeout(readTimeOut, TimeUnit.MILLISECONDS)
                .build();
    }
    //----------------

    /***
     * 讀取time out
     */
    public void setReadTimeOut(int readTimeOut) {
        this.readTimeOut = readTimeOut;
        okHttpClient = okHttpClient.newBuilder()
                .connectTimeout(connectTimeOut, TimeUnit.MILLISECONDS)
                .writeTimeout(writeTimeOut, TimeUnit.MILLISECONDS)
                .readTimeout(readTimeOut, TimeUnit.MILLISECONDS)
                .build();
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


    //----------------

    /***
     *  顯示/隱藏 loading dialog
     * @param status
     */
    protected void displayLoadingDialog(@LoadingDialogStatus int status) {
        if (zLoadingDialog != null) {
            Message message = Message.obtain();
            message.what = status;
            handler.sendMessage(message);
        }
    }

    //----------------

    /***
     * 傳遞 連線結果  成功  inputstream
     */
    protected void success(InputStream inputStream, int statusCode) {
        Message message = Message.obtain();
        message.what = SUCCESS_INPUT_STREAM;
        message.obj = inputStream;
        message.arg1 = statusCode;
        handler.sendMessage(message);
    }

    //---------------


    /***
     * 傳遞 連線結果  成功  inputstream
     */
    protected void success(String string, int statusCode) {
        Message message = Message.obtain();
        message.what = SUCCESS_STRING;
        message.obj = string;
        message.arg1 = statusCode;
        handler.sendMessage(message);
    }


    //----------------

    /***
     * 傳遞 連線結果 失敗
     */
    protected void fail() {
        Message message = Message.obtain();
        message.what = FAIL;
        handler.sendMessage(message);
    }

    //-----------------

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int type = msg.what;
            switch (type) {
                //show loading dialog
                case SHOW_DIALOG:
                    zLoadingDialog.show();
                    break;
                //dismiss loading dialog
                case DISMISS_DIALOG:
                    zLoadingDialog.dismiss();
                    break;
                //連線成功 String
                case SUCCESS_STRING:
                    zConnectHandler.OnStringResponse((String) msg.obj, msg.arg1);
                    break;
                //連線成功 inputstream
                case SUCCESS_INPUT_STREAM:
                    zConnectHandler.OnInputStreamResponse((InputStream) msg.obj, msg.arg1);
                    break;
                //連線失敗
                case FAIL:
                    zConnectHandler.OnFail();
                    break;
            }

        }
    };

    //----------------
}
