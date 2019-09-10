package tw.noel.sung.com.ztool.tool;


import java.util.Calendar;

/**
 * Created by noel on 2019/3/10.
 */
public class ZTimeTool {


    //----------------

    /***
     *  判斷目前時間是否在時段內
     * @param beginTime  範圍開始時間
     * @param endTime  範圍結束時間
     * @return
     */
    public boolean isRange(long beginTime, long endTime) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        return isRange(currentTime,beginTime,endTime);
    }

    //-------------------

    /***
     * 目前時間是否在指定時間之前
     * @param time
     * @return
     */
    public boolean isBefore(long time) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        return  isBefore(time,currentTime);
    }


    //-------------------

    /***
     *  目前時間是否在指定時間之後
     * @return
     */
    public boolean isAfter(long time) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        return isAfter(time,currentTime);
    }

    //----------------

    /***
     *  判斷目標時間是否在時段內
     * @param beginTime  範圍開始時間
     * @param endTime  範圍結束時間
     * @return
     */
    public boolean isRange(long time,long beginTime, long endTime) {
        return time >= beginTime && time <= endTime;
    }

    //-------------------

    /***
     * 目標時間是否在指定時間之前
     * @param targetTime
     * @return
     */
    public boolean isBefore(long time,long targetTime) {
        return time < targetTime;
    }

    //-------------------

    /***
     *  目標時間是否在指定時間之後
     * @return
     */
    public boolean isAfter(long time,long targetTime) {
        return time > targetTime;
    }
}
