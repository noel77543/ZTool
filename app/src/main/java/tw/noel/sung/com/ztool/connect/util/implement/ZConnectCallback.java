package tw.noel.sung.com.ztool.connect.util.implement;

import java.io.InputStream;

/**
 * Created by noel on 2019/1/21.
 */
public interface ZConnectCallback {
    void onSuccess(String response, InputStream inputStream, int code);

    void onFailed();
}
