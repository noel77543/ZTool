package tw.noel.sung.com.ztool.tool.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;

/**
 * Created by noel on 2019/6/5.
 */
public class ZSelectorTool extends StateListDrawable {

    private Context context;

    public ZSelectorTool(Context context) {
        this.context = context;
    }


    /***
     * 自定義多事件
     */
    public ZSelectorTool addStates(int[] attrs, Drawable drawable) {
        addState(attrs, drawable);
        return this;
    }

    //--------------


    /***
     * 當手指DOWN
     * @param drawable
     */
    public ZSelectorTool addPressState(Drawable drawable) {
        addState(new int[]{android.R.attr.state_pressed}, drawable);
        return this;
    }

    //----------

    /***
     * 當focus
     * @param drawable
     */
    public ZSelectorTool addFocusState(Drawable drawable) {
        addState(new int[]{android.R.attr.state_focused}, drawable);
        return this;
    }


    //----------

    /***
     * 當select
     * @param drawable
     */
    public ZSelectorTool addSelectState(Drawable drawable) {
        addState(new int[]{android.R.attr.state_selected}, drawable);
        return this;
    }

    //----------

    /***
     * 當enable
     * @param drawable
     */
    public ZSelectorTool addDisableState(Drawable drawable) {
        addState(new int[]{-android.R.attr.state_enabled}, drawable);
        return this;
    }

    //----------

    /***
     * 當check
     * @param drawable
     */
    public ZSelectorTool addCheckState(Drawable drawable) {
        addState(new int[]{android.R.attr.state_checked}, drawable);
        return this;
    }

    //--------------


    /***
     *  一般狀態
     * @param drawable
     */
    public ZSelectorTool addNormalState(Drawable drawable) {
        addState(new int[]{}, drawable);
        return this;
    }
}