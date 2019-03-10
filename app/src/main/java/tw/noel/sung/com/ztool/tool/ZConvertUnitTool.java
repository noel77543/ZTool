package tw.noel.sung.com.ztool.tool;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;

public class ZConvertUnitTool {
    private Resources resources;

    public ZConvertUnitTool(Context context) {
        resources = context.getResources();
    }


    //----------

    /***
     * dp 轉 px
     */
    public float convertDpToPx(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
    }

    //--------------

    /***
     * px 轉 dp
     */
    public float convertPxToDp(float px) {
        return px / ((float) resources.getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    //--------

    /***
     *  sp 轉 px
     */
    public float convertSpToPx(float sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, resources.getDisplayMetrics());
    }

    //----------

    /***
     * px 轉 sp
     */
    public float convertPxToSp(float px) {
        return px / resources.getDisplayMetrics().scaledDensity;
    }

    //-----------

    /***
     *  sp 轉 dp
     */
    public float convertSpToDp(float sp) {
        float px = convertSpToPx(sp);
        return convertPxToDp(px);
    }

    //---------

    /***
     *  dp 轉 sp
     */
    public float convertDpToSp(float dp) {
        float px = convertDpToPx(dp);
        return convertPxToSp(px);
    }

}
