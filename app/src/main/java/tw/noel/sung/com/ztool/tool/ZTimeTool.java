package tw.noel.sung.com.ztool.tool;


import java.util.Calendar;

/**
 * Created by noel on 2019/3/10.
 */
public class ZTimeTool {

    public ZTimeTool() {

    }

    //----------------

    /***
     *  判斷目前時間是否在時段內
     * @param beginTime  範圍開始時間
     * @param endTime  範圍結束時間
     * @return
     */
    public boolean isRange(long beginTime, long endTime) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        return currentTime >= beginTime && currentTime <= endTime;
    }

    //-------------------

    /***
     * 目前時間是否在指定時間之前
     * @param targetTime
     * @return
     */
    public boolean isBefore(long targetTime) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        return currentTime < targetTime;
    }


    //-------------------

    /***
     *  目前時間是否在指定時間之後
     * @return
     */
    public boolean isAfter(long targetTime) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        return currentTime > targetTime;
    }

}
