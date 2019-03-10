package tw.noel.sung.com.ztool.tool;


import java.util.regex.Pattern;

import static android.util.Patterns.EMAIL_ADDRESS;

/**
 * Created by noel on 2019/3/10.
 */
public class ZCheckFormatTool {


    //-------

    /***
     * 是否都是數字
     */
    public boolean isNumber(String text) {
        return Pattern.compile("^[0-9]+$").matcher(text).matches();
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
}
