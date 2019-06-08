package tw.noel.sung.com.ztool.connect.z_update_checker;

import android.content.Context;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.text.MessageFormat;

import tw.noel.sung.com.ztool.connect.z_connect.ZConnect;
import tw.noel.sung.com.ztool.connect.z_connect.util.callback.ZConnectHandler;
import tw.noel.sung.com.ztool.connect.z_update_checker.util.ZUpdateHandler;

public class ZUpdateChecker extends ZConnect {

    private final String GOOGLE_PLAY = "https://play.google.com/store/apps/details?id={0}";

    private XmlPullParserFactory xmlPullParserFactory;
    private XmlPullParser xmlPullParser;

    public ZUpdateChecker(Context context) {
        super(context);

        try {
            xmlPullParserFactory = XmlPullParserFactory.newInstance();
            xmlPullParser = xmlPullParserFactory.newPullParser();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }

    //-----------------

    /***
     *  進行版本檢查
     * @param packageName
     */
    public void check(String packageName, final ZUpdateHandler zUpdateHandler) {
        get(MessageFormat.format(GOOGLE_PLAY, packageName), new ZConnectHandler() {
            @Override
            public void OnStringResponse(String response, int code) {
                super.OnStringResponse(response, code);

                String latestVersionName = getLatestVersionName(response);
                if (latestVersionName.equals("")) {
                    zUpdateHandler.OnFail(response, code);
                } else {
                    zUpdateHandler.OnChecked(latestVersionName);
                }
            }

            @Override
            public void OnFail(IOException e) {
                super.OnFail(e);
                zUpdateHandler.OnFail(e);
            }

            @Override
            public void OnFail(String response, int code) {
                super.OnFail(response, code);
                zUpdateHandler.OnFail(response, code);
            }
        });
    }

    //-----------------

    /***
     *  解析XML 取得VersionName
     * @return
     */
    private String getLatestVersionName(String xml) {
        String latestVersionName = "";
        try {
            xmlPullParser.setInput(new StringReader(xml));
            int eventType = xmlPullParser.getEventType();

            //直到節點為文件結束點
            while (eventType != xmlPullParser.END_DOCUMENT) {
                String nodeName = xmlPullParser.getName();

                Log.e("TTT",nodeName);
                switch (eventType) {
                    //節點開始
                    case XmlPullParser.START_TAG:


                    break;
                    //節點結束
                    case XmlPullParser.END_TAG:


                    break;
                }
                //下一個節點
                eventType = xmlPullParser.next();
            }

        } catch (IOException | XmlPullParserException e) {
            e.printStackTrace();
        }

        return latestVersionName;
    }
}
