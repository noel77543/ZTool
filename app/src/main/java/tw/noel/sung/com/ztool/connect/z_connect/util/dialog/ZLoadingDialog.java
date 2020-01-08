package tw.noel.sung.com.ztool.connect.z_connect.util.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import tw.noel.sung.com.ztool.connect.z_connect.util.views.ZLoadingView;
import tw.noel.sung.com.ztool.tool.ZCheckDeviceTool;

/**
 * Created by noel on 2019/1/21.
 */
public class ZLoadingDialog extends Dialog implements DialogInterface.OnShowListener {


    private LinearLayout layout;
    private ZLoadingView zLoadingView;
    private Context context;
    private ZCheckDeviceTool zCheckDeviceTool;
    private int phoneWidth;
    private int phoneHeight;

    public ZLoadingDialog(Context context) {
        super(context);
        this.context = context;
        zCheckDeviceTool = new ZCheckDeviceTool(context);
        int[] phoneSize = zCheckDeviceTool.getPhoneSize();
        phoneWidth = phoneSize[0];
        phoneHeight = phoneSize[1];

        layout = new LinearLayout(context);
        layout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        layout.setGravity(Gravity.CENTER);
        zLoadingView = new ZLoadingView(context);
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
