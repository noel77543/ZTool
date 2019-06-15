package tw.noel.sung.com.ztool.tool.sensor.location.implement;

import android.location.Location;

import com.google.android.gms.common.ConnectionResult;

/**
 * Created by noel on 2019/3/26.
 */
public interface OnLocationGetListener {

    //當取得位置資訊
    void onLocationGet(Location location);

    //當連線中段
    void onSuspended(int i);

    //GoogleLocationAPI連線失敗
    void onFailed(ConnectionResult connectionResult,int errorCode);
}
