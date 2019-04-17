package tw.noel.sung.com.ztool.tool.sensor.location;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;

import java.util.List;

import tw.noel.sung.com.ztool.tool.sensor.location.callback.OnLocationChangeListener;
import tw.noel.sung.com.ztool.tool.sensor.location.callback.OnLocationGetListener;

/**
 * Created by noel on 2019/3/26.
 */
public class ZLocationTool implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    // Google API用戶端物件
    private GoogleApiClient googleApiClient;
    // Location請求物件
    private LocationRequest locationRequest;
    private Context context;
    //是否為持續監聽
    private boolean isKeepListen = false;
    private OnLocationChangeListener onLocationChangeListener;
    private OnLocationGetListener onLocationGetListener;

    public ZLocationTool(Context context) {
        this.context = context;
    }

    //----------------

    /***
     *  前者至後者的直線距離 單位公尺
     * @param location1
     * @param location2
     * @return
     */
    public float getStraightDistance(Location location1, Location location2) {
        return location1.distanceTo(location2);
    }

    //-------------

    /***
     *  取得基於poly line 線條的起點至終點距離  單位公尺
     */
    public float getPolyLineDistance(Polyline polyline) {
        Location location1 = new Location("location");
        Location location2 = new Location("location");

        float totalDistance = 0;
        List<LatLng> points = polyline.getPoints();
        location1.setLatitude(points.get(0).latitude);
        location1.setLongitude(points.get(0).longitude);
        for (int i = 1; i < points.size(); i++) {
            location2.setLatitude(points.get(i).latitude);
            location2.setLongitude(points.get(i).longitude);
            totalDistance += getStraightDistance(location1, location2);
            location1 = location2;
        }
        return totalDistance;
    }

    //--------------------

    /***
     *  取得目前裝置所在經緯度
     */
    public void setCurrentLocationListener(OnLocationGetListener onLocationGetListener) {
        this.onLocationGetListener = onLocationGetListener;
        isKeepListen = false;
        init();
    }

    //--------------------

    /***
     * 持續監聽目前所在位置
     */
    public void setCurrentLocationChangeListener(OnLocationChangeListener onLocationChangeListener) {
        this.onLocationChangeListener = onLocationChangeListener;
        isKeepListen = true;
        init();
    }

    //-------------------

    /***
     * 初始化經緯度取得工具
     */
    private void init() {
        locationRequest = new LocationRequest();
        locationRequest
                // 設定讀取位置資訊的間隔時間為一秒（1000ms）
                .setInterval(1000)
                // 設定讀取位置資訊最快的間隔時間為一秒（1000ms）
                .setFastestInterval(1000)
                // 設定優先讀取高精確度的位置資訊（GPS）
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        googleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        if (!googleApiClient.isConnected()) {
            googleApiClient.connect();
        }
    }

    //----------

    /***
     *  移除經位度取得工具
     */
    public void clear() {
        if (locationRequest != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
            locationRequest = null;
        }
        if (googleApiClient != null) {
            if (googleApiClient.isConnected()) {
                googleApiClient.disconnect();
            }
            googleApiClient = null;
        }
    }
    //-------------------

    /**
     * 當成功連線至googlemap server
     */
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        //Location位置變動設置
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }
    //-------------------

    /***
     *  Google Services連線中斷
     * @param i 連線中斷的代號
     */
    @Override
    public void onConnectionSuspended(int i) {
        if (isKeepListen) {
            onLocationChangeListener.onSuspended(i);
        } else {
            onLocationGetListener.onSuspended(i);
            clear();
        }
    }


    //-------------

    /**
     * 每當位置改變
     */
    @Override
    public void onLocationChanged(Location location) {
        //持續監聽
        if (isKeepListen) {
            onLocationChangeListener.onLocationChanged(location);
        }
        //非持續監聽
        else {
            onLocationGetListener.onLocationGet(location);
            clear();
        }
    }
    //-------------

    /**
     * GoogleLocationAPI連線失敗
     *
     * @param connectionResult 連線失敗的資訊
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        int errorCode = connectionResult.getErrorCode();
        if (isKeepListen) {
            onLocationChangeListener.onFailed(connectionResult, errorCode);
        } else {
            onLocationGetListener.onFailed(connectionResult, errorCode);
            clear();
        }
    }
}
