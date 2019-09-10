package tw.noel.sung.com.ztool.tool.permission;

import android.annotation.SuppressLint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;


import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.functions.Consumer;
import tw.noel.sung.com.ztool.tool.permission.implement.OnPermissionCheckListener;

public class ZCheckPermissionTool {
    private RxPermissions rxPermissions;

    public ZCheckPermissionTool(Fragment fragment){
        rxPermissions = new RxPermissions(fragment);
    }

    //------------

    public ZCheckPermissionTool(FragmentActivity fragmentActivity){
        rxPermissions = new RxPermissions(fragmentActivity);
    }


    //------------

    /***
     *  確認權限
     * @param onPermissionCheckListener
     * @param psrmissions
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("CheckResult")
    public void checkPermission(final OnPermissionCheckListener onPermissionCheckListener, final int permissionEvent, String... psrmissions) {
        rxPermissions
                .requestEach(psrmissions)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) {
                        if (onPermissionCheckListener != null) {
                            //當具備權限
                            if (permission.granted) {
                                onPermissionCheckListener.onPermissionAllowed(permissionEvent);
                            }
                            //當拒絕
                            else if (permission.shouldShowRequestPermissionRationale) {
                                onPermissionCheckListener.onPermissionDenied(permissionEvent);
                            }
                            //當不再提醒
                            else {
                                onPermissionCheckListener.onNeverAskAgain(permissionEvent);
                            }
                        }
                    }
                });
    }
}
