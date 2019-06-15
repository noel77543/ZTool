package tw.noel.sung.com.ztool.tool.permission.implement;

public interface OnPermissionCheckListener {


    void onPermissionAllowed(int event);

    void onPermissionDenied(int event);

    void onNeverAskAgain(int event);

}
