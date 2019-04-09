package tw.noel.sung.com.ztool.tool.google_map.location.implement;

import android.location.Location;

import com.google.android.gms.common.ConnectionResult;

public interface OnLocationGetListener {

    //當取得位置資訊
    void onLocationGet(Location location);

    //當連線中段
    void onSuspended(int i);

    //GoogleLocationAPI連線失敗
    void onFailed(ConnectionResult connectionResult,int errorCode);
}
