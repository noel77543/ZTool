package tw.noel.sung.com.ztool.tool.sensor.location.callback;

import android.location.Location;

import com.google.android.gms.common.ConnectionResult;

/**
 * Created by noel on 2019/3/26.
 */
public interface OnLocationChangeListener {
    //當取得位置資訊
    void onLocationChanged(Location location);

    //當連線中段
    void onSuspended(int i);

    //GoogleLocationAPI連線失敗
    void onFailed(ConnectionResult connectionResult,int errorCode);

}
