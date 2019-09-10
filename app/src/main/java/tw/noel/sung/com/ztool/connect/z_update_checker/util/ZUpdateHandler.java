package tw.noel.sung.com.ztool.connect.z_update_checker.util;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by noel on 2019/6/5.
 */
public class ZUpdateHandler {

    /***
     * 當進行檢測
     * @param latestVersionName  架上目前最新版
     */
    public void OnChecked( String latestVersionName) {

    }
    //-----------

    /***
     *  連線失敗接口
     */
    public void OnFail(IOException e) {

    }

    //----------

    /***
     *  連線成功但回覆失敗接口
     */
    public void OnFail(String response, int code) {

    }
}
