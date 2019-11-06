package tw.noel.sung.com.ztool.tool.ui;

import android.graphics.drawable.GradientDrawable;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by noel on 2019/6/5.
 */
public class ZDrawableTool extends GradientDrawable {

    public static final int SHAPE_OVAL = GradientDrawable.OVAL;
    public static final int SHAPE_LINE = GradientDrawable.LINE;
    public static final int SHAPE_RECTANGLE = GradientDrawable.RECTANGLE;
    public static final int SHAPE_RING = GradientDrawable.RING;

    @IntDef({SHAPE_OVAL, SHAPE_LINE, SHAPE_RECTANGLE, SHAPE_RING})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ZDrawableToolShape {
    }

    @IntDef({ZGradientDrawableBuilder.TOP_TO_BOTTOM, ZGradientDrawableBuilder.TOP_RIGHT_TO_BOTTOM_LEFT,
            ZGradientDrawableBuilder.RIGHT_TO_LEFT, ZGradientDrawableBuilder.BOTTOM_RIGHT_TO_TOP_LEFT,
            ZGradientDrawableBuilder.BOTTOM_TO_TOP, ZGradientDrawableBuilder.BOTTOM_LEFT_TO_TOP_RIGHT,
            ZGradientDrawableBuilder.LEFT_TO_RIGHT, ZGradientDrawableBuilder.TOP_LEFT_TO_BOTTOM_RIGHT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ZGradientDrawableBuilderOrientation {
    }

    //-------------

    /***
     * 塑形
     */
    public ZDrawableTool shape(@ZDrawableToolShape int type) {
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
    public ZDrawableTool alpha(int value) {
        setAlpha(value);
        return this;
    }

    //---------------------

    /***
     *  設置漸層
     */
    public ZGradientDrawableBuilder zGradientDrawableBuilder() {
        return new ZGradientDrawableBuilder();
    }


    //----------------

    /***
     *  漸層相關類
     */
    public class ZGradientDrawableBuilder {
        //正上方至正下方
        public static final int TOP_TO_BOTTOM = 1;
        //右上至左下
        public static final int TOP_RIGHT_TO_BOTTOM_LEFT = 2;
        //正右方至正左方
        public static final int RIGHT_TO_LEFT = 3;
        //右下方至左上方
        public static final int BOTTOM_RIGHT_TO_TOP_LEFT = 4;
        //正下方至正上方
        public static final int BOTTOM_TO_TOP = 5;
        //左下方至右上方
        public static final int BOTTOM_LEFT_TO_TOP_RIGHT = 6;
        //正左方至正右方
        public static final int LEFT_TO_RIGHT = 7;
        //左上方至右下方
        public static final int TOP_LEFT_TO_BOTTOM_RIGHT = 8;


        /***
         *  設置漸層色
         * @param colors
         * @return
         */
        public ZGradientDrawableBuilder setGradientColors(int[] colors) {
            ZDrawableTool.this.setColors(colors);
            return this;
        }

        //--------------------

        /***
         *  設置漸變方向
         * @param orientation
         * @return
         */
        public ZGradientDrawableBuilder setGradientFromTo(@ZGradientDrawableBuilderOrientation int orientation) {
            ZDrawableTool.this.setOrientation(getOrientation(orientation));
            return this;
        }

        //--------------------

        /***
         * 設置漸變中心
         * @param x
         * @param y
         * @return
         */
        public ZGradientDrawableBuilder setGradientCenter(float x, float y) {
            ZDrawableTool.this.setGradientCenter(x, y);
            return this;
        }

        //--------------------

        /***
         * 回調
         * @return
         */
        public ZDrawableTool build() {
            return ZDrawableTool.this;
        }

        //--------------------

        /***
         * 取得對應enum
         * @param orientation
         * @return
         */
        private GradientDrawable.Orientation getOrientation(int orientation) {
            switch (orientation) {
                case TOP_TO_BOTTOM:
                    return Orientation.TOP_BOTTOM;
                case TOP_RIGHT_TO_BOTTOM_LEFT:
                    return Orientation.TR_BL;
                case RIGHT_TO_LEFT:
                    return Orientation.RIGHT_LEFT;
                case BOTTOM_RIGHT_TO_TOP_LEFT:
                    return Orientation.BR_TL;
                case BOTTOM_TO_TOP:
                    return Orientation.BOTTOM_TOP;
                case BOTTOM_LEFT_TO_TOP_RIGHT:
                    return Orientation.BL_TR;
                case TOP_LEFT_TO_BOTTOM_RIGHT:
                    return Orientation.TL_BR;
                default:
                case LEFT_TO_RIGHT:
                    return Orientation.LEFT_RIGHT;
            }
        }
    }
}
