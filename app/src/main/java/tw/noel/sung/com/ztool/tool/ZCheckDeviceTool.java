package tw.noel.sung.com.ztool.tool;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;

import java.net.NetworkInterface;
import java.net.SocketException;

/**
 * Created by noel on 2019/3/11.
 */
public class ZCheckDeviceTool {

    private PackageManager packageManager;
    private Context context;

    public ZCheckDeviceTool(Context context) {
        this.context = context;
        packageManager = this.context.getPackageManager();
    }

    //-------------

    /***
     *  取得唯一碼  MacAddress
     *  如果使用MacAddress 時裝置须具有上網功能
     */
    public String getMacAddress() {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            NetworkInterface networkInterface = NetworkInterface.getByName("eth1");
            if (networkInterface == null) {
                networkInterface = NetworkInterface.getByName("wlan0");
            }
            if (networkInterface == null) {
                return "02:00:00:00:00:02";
            }
            byte[] bytes = networkInterface.getHardwareAddress();
            for (byte b : bytes) {
                stringBuilder.append(String.format("%02X:", b));
            }
            if (stringBuilder.length() > 0) {
                stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            }
        } catch (SocketException e) {
            e.printStackTrace();
            return "02:00:00:00:00:02";
        }
        return stringBuilder.toString();
    }

    //--------------

    /***
     *  取得唯一碼 Android ID
     *  如果手機恢復原廠設定 此ID將改變
     */
    public String getAndroidID(){
        return Settings.System.getString(context.getContentResolver(), Settings.System.ANDROID_ID);
    }


    //--------------

    /***
     *  是否具備後鏡頭
     */
    public boolean isHasBehindCamera() {
        return packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    //------------

    /***
     *  是否具備前鏡頭
     */
    public boolean isHasFrontCamera() {
        return packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT);
    }

    //-----------

    /***
     *  是否具備閃光燈
     */
    public boolean isHasFlash() {
        return packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    //----------

    /***
     * 是否具備錄音麥克風
     */
    public boolean isHasMicrophone() {
        return packageManager.hasSystemFeature(PackageManager.FEATURE_MICROPHONE);
    }

    //------------

    /***
     *  是否具備GPS
     */
    public boolean isHasGPS() {
        return packageManager.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS);
    }

    //--------------

    /***
     *  是否具備指紋辨識
     *   Android 系統版本 API Level 23以上開始支援指紋辨識 以下的版本都取得false
     */
    public boolean isHasFingerPrint() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT);
        } else {
            return false;
        }
    }

    //----------------

    /***
     *  是否具備氣壓傳感器
     */
    public boolean isHasBarometer() {
        return packageManager.hasSystemFeature(PackageManager.FEATURE_SENSOR_BAROMETER);
    }

    //----------------

    /***
     * 是否具備溫度傳感器
     *   Android 系統版本 API Level 21以上開始支援指紋辨識 以下的版本都取得false
     */
    public boolean isHasTemperature() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return packageManager.hasSystemFeature(PackageManager.FEATURE_SENSOR_AMBIENT_TEMPERATURE);
        } else {
            return false;
        }
    }

    //----------------

    /***
     * 是否具備指南針
     */
    public boolean isHasCompass() {
        return packageManager.hasSystemFeature(PackageManager.FEATURE_SENSOR_COMPASS);
    }

    //---------------

    /***
     * 是否具備陀螺儀
     */
    public boolean isHasGyroscope() {
        return packageManager.hasSystemFeature(PackageManager.FEATURE_SENSOR_GYROSCOPE);
    }

    //----------------

    /***
     *  是否具備藍芽
     */
    public boolean isHasBlueTooth(){
        return packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH);
    }

    //---------------

    /***
     * 是否具備低功耗藍芽
     */
    public boolean isHasBLE(){
        return packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
    }

}
