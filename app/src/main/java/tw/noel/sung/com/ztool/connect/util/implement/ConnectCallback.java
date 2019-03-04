package tw.noel.sung.com.ztool.connect.util.implement;

public interface ConnectCallback {
    void onSuccess(String response, int code);
    void onFailed();
}
