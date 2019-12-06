package tw.noel.sung.com.ztool.connect.z_connect.util.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
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

import tw.noel.sung.com.ztool.connect.z_connect.util.views.ZLoadingView;
import tw.noel.sung.com.ztool.tool.ZCheckDeviceTool;

/**
 * Created by noel on 2019/1/21.
 */
public class ZLoadingDialog extends Dialog implements DialogInterface.OnShowListener {
    //圖片縮放筆立
    private final float BITMAP_SCALE = 0.2f;

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
        getWindow().setBackgroundDrawable(getBlurDrawable(25));
        setOnShowListener(this);
    }

    @Override
    public void onShow(DialogInterface dialogInterface) {
        zLoadingView.startRotateAnimation();
    }


    //----------

    /***
     * 將圖片模糊化
     * @return
     */
    private Drawable getBlurDrawable(float blurLevel) {
        Bitmap blurBitmap = getScreenShotAsBitmap();

        //不得為負數
        blurLevel = Math.abs(blurLevel);
        if (blurLevel > 25) {
            blurLevel = 25;
        }


        Bitmap outputBitmap = null;
        try {

            Class.forName("android.renderscript.ScriptIntrinsicBlur");
            // 计算图片缩小后的长宽
            int width = Math.round(blurBitmap.getWidth() * BITMAP_SCALE);
            int height = Math.round(blurBitmap.getHeight() * BITMAP_SCALE);
            if (width < 2 || height < 2) {
                return null;
            }

            // 将缩小后的图片做为预渲染的图片。
            Bitmap inputBitmap = Bitmap.createScaledBitmap(blurBitmap, width, height, false);
            // 创建一张渲染后的输出图片。
            outputBitmap = Bitmap.createBitmap(inputBitmap);

            // 创建RenderScript内核对象
            RenderScript rs = RenderScript.create(activity);
            // 创建一个模糊效果的RenderScript的工具对象
            ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

            // 由于RenderScript并没有使用VM来分配内存,所以需要使用Allocation类来创建和分配内存空间。
            // 创建Allocation对象的时候其实内存是空的,需要使用copyTo()将数据填充进去。
            Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
            Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);

            // 设置渲染的模糊程度, 25f是最大模糊度
            blurScript.setRadius(blurLevel);
            // 设置blurScript对象的输入内存
            blurScript.setInput(tmpIn);
            // 将输出数据保存到输出内存中
            blurScript.forEach(tmpOut);

            // 将数据填充到Allocation中
            tmpOut.copyTo(outputBitmap);

            return new BitmapDrawable(outputBitmap);
        } catch (Exception e) {
            Log.e("Bemboy_Error", "Android版本过低");
            return null;
        }
    }

    //----------

    /***
     *  當前Activity截圖
     */
    public Bitmap getScreenShotAsBitmap() {
        View view = ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
        try {
            view.setDrawingCacheEnabled(true);
            Bitmap catchBitmap = view.getDrawingCache();
            // 複製截圖
            catchBitmap = catchBitmap.createBitmap(catchBitmap);
            //釋放android.R.id.content
            view.setDrawingCacheEnabled(false);
            return catchBitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
