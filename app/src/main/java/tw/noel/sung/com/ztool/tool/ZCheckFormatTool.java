package tw.noel.sung.com.ztool.tool;


import java.util.regex.Pattern;

import static android.util.Patterns.EMAIL_ADDRESS;

/**
 * Created by noel on 2019/3/10.
 */
public class ZCheckFormatTool {


    //-------

    /***
     * 是否都是整數
     */
    public boolean isInteger(String text) {
        return Pattern.compile("^[0-9]+$").matcher(text).matches();
    }

    //-------

    /***
     * 是否是浮點數
     */
    public boolean isFloat(String text) {
        return Pattern.compile("^[0-9]+[.]{1}+[0-9]+$").matcher(text).matches();
    }

    //-------

    /***
     * 是否是浮點數
     * 可指定小數點後 afterPointStart 至 afterPointEnd位
     */
    public boolean isFloat(String text, int afterPointStart, int afterPointEnd) {
        return Pattern.compile("^[0-9]+(.[0-9]{" + afterPointStart + "," + afterPointEnd + "})?+$").matcher(text).matches();
    }
    //-------

    /***
     * 是否是整數或者浮點數
     */
    public boolean isNumber(String text) {
        return Pattern.compile("^[0-9]+([.]{1}[0-9]+){0,1}+$").matcher(text).matches();
    }


    //-------

    /***
     * 是否都是中文
     */
    public boolean isChinese(String text) {
        return Pattern.compile("^[\\u4e00-\\u9fa5]+$").matcher(text).matches();
    }

    //-----------

    /***
     * 是否都是英文
     */
    public boolean isEnglish(String text) {
        return Pattern.compile("^[A-Za-z]+$").matcher(text).matches();
    }

    //-----------

    /***
     * 是否都是 大寫英文
     */
    public boolean isUpperEnglish(String text) {
        return Pattern.compile("^[A-Z]+$").matcher(text).matches();
    }

    //-----------

    /***
     * 是否都是 小寫英文
     */
    public boolean isLetterEnglish(String text) {
        return Pattern.compile("^[a-z]+$").matcher(text).matches();
    }

    //-----------

    /***
     * 是否為Email
     */
    public boolean isEmail(String text) {
        return EMAIL_ADDRESS.matcher(text).matches();
    }

    //-----------

    /***
     *  是否為手機格式
     *   isIncludeInternational = false
     *   1.   09xxxxxxxx
     *
     *   isIncludeInternational = true
     *   1.   +886xxxxxxxxxx
     *   2.   09xxxxxxxx
     */
    public boolean isTaiwanCellPhone(String text,boolean isIncludeInternational) {
        return Pattern.compile( isIncludeInternational?"[+-]?\\d{10,12}":"^09[0-9]{8}$").matcher(text).matches();
    }

    //------------

    /***
     *  是否為英數混合(常見密碼)
     *  1，不能全部是數字
     * 2，不能全部是字母
     * 3，必須是數字或字母
     */
    public boolean isPassword(String text){
        return Pattern.compile("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]+$").matcher(text).matches();
    }

    //-----------
    /***
     *  是否為Code39格式
     *  A-Z,0-9,+,-,.
     */
    public boolean isCode39(String text){
        return Pattern.compile("^[A-Z0-9.+-]+$").matcher(text).matches();
    }
}
