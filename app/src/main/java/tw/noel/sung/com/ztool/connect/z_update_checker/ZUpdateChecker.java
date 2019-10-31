package tw.noel.sung.com.ztool.connect.z_update_checker;

import android.content.Context;
import android.text.TextUtils;
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

/**
 * Created by noel on 2019/6/5.
 */
public class ZUpdateChecker extends ZConnect {

    private final String _GOOGLE_PLAY = "https://play.google.com/store/apps/details?id={0}";
    private final String _PATTERN_CURRENT_VERSION = "<div[^>]*?>Current\\sVersion</div><span[^>]*?>(.*?)><div[^>]*?>(.*?)><span[^>]*?>(.*?)</span>";
    private final String _PATTERN_APP_VERSION_NAME = "htlgb\">([^<]*)</s";

    public ZUpdateChecker(Context context) {
        super(context);

    }

    //-----------------

    /***
     *  進行版本檢查
     * @param packageName
     */
    public void check(String packageName, final ZUpdateHandler zUpdateHandler) {
        get(MessageFormat.format(_GOOGLE_PLAY, packageName), new ZConnectHandler() {
            @Override
            public void OnStringResponse(String response, int code) {
                super.OnStringResponse(response, code);

                String latestVersionName = getLatestVersionName(response);
                if (TextUtils.isEmpty(latestVersionName)) {
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
     *  取得VersionName
     * @param htmlString
     * @return
     */
    private String getLatestVersionName(String htmlString) {

        String latestVersionName = "";
        String versionString = parseHtml(_PATTERN_CURRENT_VERSION, htmlString);

        if (versionString != null) {
            latestVersionName = parseHtml(_PATTERN_APP_VERSION_NAME, versionString);
        }
        return latestVersionName;
    }

    //-----------------

    /***
     *  解析Html
     * @param patternString
     * @param htmlString
     * @return
     */
    private String parseHtml(String patternString, String htmlString) {
        try {
            Pattern pattern = Pattern.compile(patternString);
            if (null == pattern) {
                return null;
            }

            Matcher matcher = pattern.matcher(htmlString);
            if (matcher.find()) {
                return matcher.group(1);
            }

        } catch (PatternSyntaxException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
