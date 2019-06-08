package tw.noel.sung.com.ztool.connect.z_connect.util.callback;

/**
 * Created by noel on 2019/1/21.
 */

import java.io.IOException;
import java.io.InputStream;

public class ZConnectHandler {


    //-----------

    /***
     *  適用inputstream接口
     * @param inputStream
     * @param code
     */
    public void OnInputStreamResponse(InputStream inputStream, int code) {

    }
    //-----------

    /***
     * 適用 jsonString 接口
     * @param response
     * @param code
     */
    public void OnStringResponse(String response, int code) {

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
