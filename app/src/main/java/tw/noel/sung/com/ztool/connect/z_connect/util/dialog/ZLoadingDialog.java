package tw.noel.sung.com.ztool.connect.z_connect.util.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import tw.noel.sung.com.ztool.connect.z_connect.util.views.ZLoadingView;
import tw.noel.sung.com.ztool.tool.ZCheckDeviceTool;

/**
 * Created by noel on 2019/1/21.
 */
public class ZLoadingDialog extends Dialog implements DialogInterface.OnShowListener {


    private ZLoadingView zLoadingView;


    public ZLoadingDialog(Context context) {
        super(context);
        zLoadingView = new ZLoadingView(context);
        initWindow(context);
        setContentView(getLayout(context));

        setCancelable(false);
        setOnShowListener(this);
    }

    //----------

    @Override
    public void onShow(DialogInterface dialogInterface) {
        zLoadingView.startRotateAnimation();
    }

    //----------

    private void initWindow(Context context){
        int[] phoneSize = new ZCheckDeviceTool(context).getPhoneSize();
        int phoneWidth = phoneSize[0];
        int phoneHeight = phoneSize[1];
        getWindow().setLayout(phoneWidth, phoneHeight);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    }

    //----------

    private View getLayout(Context context) {
        LinearLayout layout = new LinearLayout(context);
        layout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        layout.setGravity(Gravity.CENTER);
        layout.addView(zLoadingView);
        return layout;
    }

}
