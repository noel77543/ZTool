package tw.noel.sung.com.ztool.connect.util.implement;

/**
 * Created by noel on 2019/1/21.
 */
public interface ZConnectCallback {
    void onSuccess(String response, int code);
    void onFailed();
}
