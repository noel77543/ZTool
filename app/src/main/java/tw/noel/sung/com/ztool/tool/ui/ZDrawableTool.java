package tw.noel.sung.com.ztool.tool.ui;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;

/**
 * Created by noel on 2019/6/5.
 */
public class ZDrawableTool extends GradientDrawable {

    private Context context;

    //---------------

    public ZDrawableTool(Context context) {
        this.context = context;
    }

    //-------------

    /***
     * 塑形
     */
    public ZDrawableTool shape(int type) {
        setShape(type);
        return this;
    }

    //---------------

    /***
     * 填滿色系
     * @param colorRes
     * @return
     */
    public ZDrawableTool solid(int colorRes) {
        setColor(colorRes);
        return this;
    }

    //----------

    /***
     * 必須為長度8的float陣列
     * @param cornerParameters { 左上,左上, 右上, 右上, 右下, 右下, 左下,左下  }
     * @return
     */
    public ZDrawableTool corner(float... cornerParameters) {
        setCornerRadii(cornerParameters);
        return this;
    }


    //----------

    /***
     * 邊際線
     */
    public ZDrawableTool stroke(int width, int colorRes) {
        setStroke(width, colorRes);
        return this;
    }

    //----------

    /***
     * 尺寸
     */
    public ZDrawableTool size(int width, int height) {
        setSize(width, height);
        return this;
    }

    //----------

    /***
     *  透明度
     */
    public ZDrawableTool alpha(int value){
        setAlpha(value);
        return this;
    }
}
