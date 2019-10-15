package tw.noel.sung.com.ztool.tool.google_map;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import com.google.android.gms.maps.model.LatLng;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import tw.noel.sung.com.ztool.tool.io.ZFileTool;

public class ZMapTool {
    private ZFileTool zFileTool;
    private XmlPullParser xmlPullParser;
    private Context context;
    private String xmlString;

    //---------
    public ZMapTool(Context context) {
        this.context = context;
    }


    //------------

    /***
     *  依序取得 MyMap所產的kml中的  起點, 路徑群, 終點 的點位
     */
    public ArrayList<LatLng> getKMLPoints(String assetsFileName) {
        if (zFileTool == null) {
            zFileTool = new ZFileTool(context);
        }
        if (xmlPullParser == null) {
            xmlPullParser = Xml.newPullParser();
        }

        ArrayList points = new ArrayList<>();
        xmlString = zFileTool.getAssetsFileToString(assetsFileName);
        try {
            xmlPullParser.setInput(new StringReader(xmlString));
            for (int eventType = xmlPullParser.getEventType(); eventType != XmlPullParser.END_DOCUMENT; eventType = xmlPullParser.next()) {
                if (eventType == XmlPullParser.START_TAG && xmlPullParser.getName().equals("coordinates")) {
                    String point = xmlPullParser.nextText().trim();
                    String[] pointArray = point.split(",|\\ ");
                    for (int i = 0; i < pointArray.length; i += 3) {
                        String latStr = pointArray[i + 1];
                        String lngStr = pointArray[i];
                        if (!latStr.isEmpty() && !lngStr.isEmpty()) {
                            points.add(new LatLng(Double.parseDouble(latStr), Double.parseDouble(lngStr)));
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return points;
    }


    //---------------------

    /***
     *  已知距離換算ZoomLevel
     * @param lat 兩點的中心點緯度
     * @param distanceKm  兩點距離 單位:公里
     * @return
     */
    public double getTwoLocationZoom(double distanceKm, double lat) {
        //倍率
        final double _MAGNIFICATION = 156543.03392f * Math.cos(lat * Math.PI / 180) / distanceKm;
        //偏差值
        float _OFFSET = (float) (Math.log(_MAGNIFICATION)) / 2;
        return Math.log(_MAGNIFICATION * _OFFSET);
    }


    //---------------------

    /***
     *   已知ZoomLevel換算比例尺
     * @param lat 兩點的中心點緯度
     * @param zoomLevel  地圖Level
     */
    public double getLocationScale(double zoomLevel, double lat) {
        return 156543.03392 * Math.cos(lat * Math.PI / 180) / Math.pow(2, zoomLevel);
    }


}
