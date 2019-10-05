package tw.noel.sung.com.ztool.tool;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by noel on 2019/10/5.
 */
public class ZImageTool {


    /***
     *  取得圖片的寬高  -File
     *  size[0] = 寬
     *  size[1] = 高
     */
    public Object getBitmapFromFile(boolean isOnlyGetInformation, String filePath) {
        BitmapFactory.Options options = getBitmapOptions(isOnlyGetInformation);
        //進行量測
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
        return isOnlyGetInformation ? new int[]{options.outWidth, options.outHeight} : bitmap;
    }

    //---------

    /***
     *  取得圖片的寬高  -Resource
     *  size[0] = 寬
     *  size[1] = 高
     */
    public Object getBitmapFromResource(boolean isOnlyGetInformation, Resources resources, int resourceId) {
        BitmapFactory.Options options = getBitmapOptions(isOnlyGetInformation);
        //進行量測
        Bitmap bitmap = BitmapFactory.decodeResource(resources, resourceId, options);
        return isOnlyGetInformation ? new int[]{options.outWidth, options.outHeight} : bitmap;
    }

    //---------

    /***
     *  取得圖片的寬高  -ByteArray
     *  size[0] = 寬
     *  size[1] = 高
     */
    public Object getBitmapFromByteArray(boolean isOnlyGetInformation, byte[] data, int offset, int length) {
        BitmapFactory.Options options = getBitmapOptions(isOnlyGetInformation);
        //進行量測
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, offset, length, options);
        return isOnlyGetInformation ? new int[]{options.outWidth, options.outHeight} : bitmap;
    }

    //---------

    /***
     *  取得圖片的寬高  -ByteArray
     *  size[0] = 寬
     *  size[1] = 高
     */
    public Object getBitmapFromStream(boolean isOnlyGetInformation, InputStream inputStream, Rect rect) throws IOException {
        BitmapFactory.Options options = getBitmapOptions(isOnlyGetInformation);
        //進行量測
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream, rect, options);
        inputStream.close();
        return isOnlyGetInformation ? new int[]{options.outWidth, options.outHeight} : bitmap;
    }

    //---------

    /***
     *  取得圖片的寬高  -ByteArray
     *  size[0] = 寬
     *  size[1] = 高
     */
    public Object getBitmapSizeFromStream(boolean isOnlyGetInformation, FileDescriptor fileDescriptor, Rect rect) {
        BitmapFactory.Options options = getBitmapOptions(isOnlyGetInformation);
        //進行量測
        Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor, rect, options);
        return isOnlyGetInformation ? new int[]{options.outWidth, options.outHeight} : bitmap;
    }

    //-------------

    private BitmapFactory.Options getBitmapOptions(boolean isOnlyGetInformation) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        //只取得圖片資訊而不讀取
        options.inJustDecodeBounds = isOnlyGetInformation;
        return options;
    }
}
