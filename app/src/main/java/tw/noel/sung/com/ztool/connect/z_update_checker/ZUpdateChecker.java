package tw.noel.sung.com.ztool.connect.z_update_checker;

import android.content.Context;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

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





    private String getPlayStoreAppVersion(String xml) {
        final String currentVersion_PatternSeq = "<div[^>]*?>Current\\sVersion</div><span[^>]*?>(.*?)><div[^>]*?>(.*?)><span[^>]*?>(.*?)</span>";
        final String appVersion_PatternSeq = "htlgb\">([^<]*)</s";
        String playStoreAppVersion ;

        // Get the current version pattern sequence
        String versionString = getAppVersion (currentVersion_PatternSeq, xml.toString());
        if(null == versionString){
            return null;
        }else{
            // get version from "htlgb">X.X.X</span>
            playStoreAppVersion = getAppVersion (appVersion_PatternSeq, versionString);
        }

        return playStoreAppVersion;
    }



    private String getAppVersion(String patternString, String inputString) {
        try{
            //Create a pattern
            Pattern pattern = Pattern.compile(patternString);
            if (null == pattern) {
                return null;
            }

            //Match the pattern string in provided string
            Matcher matcher = pattern.matcher(inputString);
            if (null != matcher && matcher.find()) {
                return matcher.group(1);
            }

        }catch (PatternSyntaxException ex) {

            ex.printStackTrace();
        }

        return null;
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


                String latestVersionName = getPlayStoreAppVersion(response);
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

                String tagName = xmlPullParser.getName();
                switch (eventType) {
                    //節點開始
                    case XmlPullParser.START_TAG:
                        if(tagName != null){

                            if(tagName.equals("htlgb")){
                                Log.e("TTT",xmlPullParser.getText());
                            }
                        }
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
