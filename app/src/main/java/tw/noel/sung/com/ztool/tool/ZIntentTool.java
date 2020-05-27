package tw.noel.sung.com.ztool.tool;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.MessageFormat;

public class ZIntentTool {

    public static final String _PACKAGE_NAME_GOOGLE_MAP = "com.google.android.apps.maps";
    public static final String _PACKAGE_NAME_GOOGLE_PLAY = "com.android.vending";
    public static final String _PACKAGE_NAME_FACEBOOK = "com.facebook.katana";


    @StringDef({_NAVIGATION_MODE_CAR, _NAVIGATION_MODE_MOTOR,_NAVIGATION_MODE_BICYCLE,_NAVIGATION_MODE_WALK})
    @Retention(RetentionPolicy.SOURCE)
    public @interface NavigationMode {
    }

    public static final String _NAVIGATION_MODE_CAR = "d";
    public static final String _NAVIGATION_MODE_MOTOR = "l";
    public static final String _NAVIGATION_MODE_BICYCLE = "b";
    public static final String _NAVIGATION_MODE_WALK = "w";


    private final String _GOOGLE_MAP_SINGLE_POI_NAVIGATION_FORMAT = "google.navigation:q={0},{1}&mode={2}";
    private final String _GOOGLE_MAP_MULTIPLE_POI_NAVIGATION_FORMAT = "https://www.google.co.in/maps/dir/{0}";
    private final String _CALL_PHONE_FORMAT = "tel:{0}";
    private final String _PERMISSION_SETTING_FORMAT = "package:{0}";


    private static ZIntentTool ZIntentTool;
    private Context context;

    //----------------

    public static ZIntentTool getInstance(Context context) {
        if (ZIntentTool == null) {
            ZIntentTool = new ZIntentTool(context);
        }
        return ZIntentTool;
    }

    //---

    public ZIntentTool(Context context) {
        this.context = context;
    }

    //---

    /***
     * 前往 目標App
     */
    public void intentToTargetApp(String targetPackageName,@Nullable OnAppNotFindListener onAppNotFindListener){
        if (isAppExist(targetPackageName)) {
            context.startActivity(context.getPackageManager().getLaunchIntentForPackage(targetPackageName));
        } else {
            if (onAppNotFindListener != null) {
                onAppNotFindListener.onAppNotFind(targetPackageName);
            }
        }
    }


    //---

    /***
     * 前往 GoogleMap
     */
    public void intentToGoogleMap(@Nullable OnAppNotFindListener onAppNotFindListener) {
        intentToTargetApp(_PACKAGE_NAME_GOOGLE_MAP,onAppNotFindListener);
    }

    //---

    /***
     * 前往 GooglePlay
     */
    public void intentToGooglePlay(@Nullable OnAppNotFindListener onAppNotFindListener) {
        intentToTargetApp(_PACKAGE_NAME_GOOGLE_PLAY,onAppNotFindListener);
    }

    //---

    /***
     * 前往 Facebook
     */
    public void intentToFacebook(@Nullable OnAppNotFindListener onAppNotFindListener) {
        intentToTargetApp(_PACKAGE_NAME_FACEBOOK,onAppNotFindListener);
    }

    //---

    /***
     * 前往 電話
     */
    public void intentToCallPhone(String phoneNumber) {
        context.startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(MessageFormat.format(_CALL_PHONE_FORMAT, phoneNumber))));
    }

    //---

    /***
     * 前往 GPS設定
     */
    public void intentToGPSSetting() {
        context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    }

    //---

    /***
     * 前往一般設定
     */
    public void intentToPermissionSetting(String packageName) {
        context.startActivity(new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse(MessageFormat.format(_PERMISSION_SETTING_FORMAT, packageName))));
    }

    //---

    /***
     * 前往上層覆蓋權限
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void intentToOverlaySetting(String packageName) {
        context.startActivity(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse(MessageFormat.format(_PERMISSION_SETTING_FORMAT, packageName))));
    }

    //---

    /***
     * 前往省電達人
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void intentToIgnoreBatteryOptimizations(String packageName) {
        context.startActivity(new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS, Uri.parse(MessageFormat.format(_PERMISSION_SETTING_FORMAT, packageName))));
    }
    //---

    /***
     * 前往 GoogleMapApp進行 單點導航
     */
    public void intentToNavigation(@NavigationMode String mode, String lat, String lng, @Nullable OnAppNotFindListener onAppNotFindListener) {
        if (isAppExist(_PACKAGE_NAME_GOOGLE_MAP)) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(MessageFormat.format(_GOOGLE_MAP_SINGLE_POI_NAVIGATION_FORMAT, lat, lng, mode))));
        } else {
            if (onAppNotFindListener != null) {
                onAppNotFindListener.onAppNotFind(_PACKAGE_NAME_GOOGLE_MAP);
            }
        }
    }
    //---

    /***
     * 前往 GoogleMapApp進行 多點導航
     * [緯度1,經度1,緯度2,經度2....etc]
     */
    public void intentToNavigation(String[] poiArray, @Nullable OnAppNotFindListener onAppNotFindListener) {
        if (isAppExist(_PACKAGE_NAME_GOOGLE_MAP)) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < poiArray.length; i++) {
                //每第偶數個
                if (i % 2 == 0) {
                    stringBuilder.append(poiArray[i] + ",");
                }
                //每第奇數個
                else {
                    stringBuilder.append(poiArray[i] + "/");
                }
            }

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(MessageFormat.format(_GOOGLE_MAP_MULTIPLE_POI_NAVIGATION_FORMAT, stringBuilder.toString())));
            context.startActivity(intent.setPackage(_PACKAGE_NAME_GOOGLE_MAP));

        } else {
            if (onAppNotFindListener != null) {
                onAppNotFindListener.onAppNotFind(_PACKAGE_NAME_GOOGLE_MAP);
            }
        }
    }

    //---

    /***
     * 該App是否存在
     * @return
     */
    public boolean isAppExist(String packageName) {
        try {
            context.getPackageManager().getApplicationInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
        return true;
    }

    //---

    public interface OnAppNotFindListener {
        void onAppNotFind(String whichAppPackageName);
    }
}
