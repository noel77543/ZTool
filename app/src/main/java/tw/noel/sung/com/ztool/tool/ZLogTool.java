package tw.noel.sung.com.ztool.tool;

import android.support.annotation.IntDef;
import android.util.Log;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by noel on 2019/9/10.
 */
public class ZLogTool {


    /***
     *  Log最大字符數
     */
    private static final int _MAX_MESSAGE_LENGTH_OF_ONCE = 4000;

    @IntDef({V, I, E, W, D, WTF})
    @Retention(RetentionPolicy.SOURCE)
    public @interface LogType {
    }

    public static final int V = 0;
    public static final int D = 1;
    public static final int I = 2;
    public static final int W = 3;
    public static final int E = 4;
    public static final int WTF = 5;


    /***
     *  打印所有訊息
     * @param logType Log種類
     * @param tag 標題
     * @param message 訊息
     */
    public static void log(@LogType int logType, String tag, String message) {

        message = message.trim();
        int messageLength = message.length();
        if (messageLength < _MAX_MESSAGE_LENGTH_OF_ONCE) {
            logByType(logType, tag, message);
        } else {
            //打印次數
            int times = messageLength / _MAX_MESSAGE_LENGTH_OF_ONCE;
            for (int i = 0; i <= times; i++) {
                int start = i * _MAX_MESSAGE_LENGTH_OF_ONCE;
                int end = start + _MAX_MESSAGE_LENGTH_OF_ONCE;
                if (end > messageLength) {
                    end = messageLength;
                }

                Log.d("AAA", start + "");
                Log.d("BBB", end + "");

                logByType(logType, tag, message.substring(start, end));
            }
        }
    }


    //-----------------

    private static void logByType(@LogType int logType, String tag, String message) {
        switch (logType) {
            //Verbose
            case V:
                Log.v(tag, message);
                break;
            //Debug
            case D:
                Log.d(tag, message);
                break;
            //Info
            case I:
                Log.i(tag, message);
                break;
            //Warning
            case W:
                Log.w(tag, message);
                break;
            //Error
            case E:
                Log.e(tag, message);
                break;
            //ASSERT
            case WTF:
                Log.wtf(tag, message);
                break;
        }
    }
}
