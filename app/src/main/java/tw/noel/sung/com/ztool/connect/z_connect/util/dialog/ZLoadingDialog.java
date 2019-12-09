package tw.noel.sung.com.ztool.connect.z_connect.util.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import tw.noel.sung.com.ztool.R;
import tw.noel.sung.com.ztool.connect.z_connect.util.views.ZLoadingView;
import tw.noel.sung.com.ztool.tool.ZCheckDeviceTool;

/**
 * Created by noel on 2019/1/21.
 */
public class ZLoadingDialog extends Dialog implements DialogInterface.OnShowListener {


    private LinearLayout layout;
    private ZLoadingView zLoadingView;
    private Activity activity;
    private ZCheckDeviceTool zCheckDeviceTool;
    private int phoneWidth;
    private int phoneHeight;

    public ZLoadingDialog(Context context) {
        super(context);
        activity = (Activity) context;
        zCheckDeviceTool = new ZCheckDeviceTool(activity);
        int[] phoneSize = zCheckDeviceTool.getPhoneSize();
        phoneWidth = phoneSize[0];
        phoneHeight = phoneSize[1];

        layout = new LinearLayout(activity);
        layout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        layout.setGravity(Gravity.CENTER);
        zLoadingView = new ZLoadingView(activity);
        layout.addView(zLoadingView);

        setContentView(layout);
        setCancelable(false);

        getWindow().setLayout(phoneWidth, phoneHeight);

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setOnShowListener(this);
    }

    @Override
    public void onShow(DialogInterface dialogInterface) {
        zLoadingView.startRotateAnimation();
    }

}
