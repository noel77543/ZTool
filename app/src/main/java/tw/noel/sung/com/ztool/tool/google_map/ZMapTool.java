package tw.noel.sung.com.ztool.tool.google_map;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.util.Xml;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.VisibleRegion;

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


    //--------------

    /***
     * @return 取得可視範圍(googleMap上方經緯度與camera中心點經緯度的距離) 單位公尺
     */
    public double getCameraRadius(GoogleMap googleMap){
        VisibleRegion visibleRegion = googleMap.getProjection().getVisibleRegion();

        float[] distanceWidth = new float[1];
        float[] distanceHeight = new float[1];

        LatLng latLngFarRight = visibleRegion.farRight;
        LatLng latLngFarLeft = visibleRegion.farLeft;
        LatLng latLngNearRight = visibleRegion.nearRight;
        LatLng latLngNearLeft = visibleRegion.nearLeft;

        //distanceWidth 取得鏡頭左至右的直線距離
        Location.distanceBetween(
                (latLngFarLeft.latitude + latLngNearLeft.latitude) / 2,
                latLngFarLeft.longitude,
                (latLngFarRight.latitude + latLngNearRight.latitude) / 2,
                latLngFarRight.longitude,
                distanceWidth
        );

        //distanceHeight 取得鏡頭上至下的直線距離
        Location.distanceBetween(
                latLngFarRight.latitude,
                (latLngFarRight.longitude + latLngFarLeft.longitude) / 2,
                latLngNearRight.latitude,
                (latLngNearRight.longitude + latLngNearLeft.longitude) / 2,
                distanceHeight
        );

        //將寬高都除以二後取得 鏡頭中心點至上或下的距離 以及 鏡頭中心點至左或右的距離   接著用畢氏定理得解第三邊即為半徑
        return Math.sqrt(Math.pow(distanceWidth[0], 2) + Math.pow(distanceHeight[0], 2)) / 2;
    }


}
