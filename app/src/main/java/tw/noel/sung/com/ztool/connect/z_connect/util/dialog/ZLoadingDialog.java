package tw.noel.sung.com.ztool.connect.z_connect.util.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.LinearLayout;

import tw.noel.sung.com.ztool.connect.z_connect.util.views.ZLoadingView;

/**
 * Created by noel on 2019/1/21.
 */
public class ZLoadingDialog extends Dialog {

    private LinearLayout layout;
    private ZLoadingView zLoadingView;

    public ZLoadingDialog(Context context) {
        super(context);
        layout = new LinearLayout(context);
        zLoadingView = new ZLoadingView(context);
        layout.addView(zLoadingView);
        setContentView(layout);
        setCancelable(false);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
}
