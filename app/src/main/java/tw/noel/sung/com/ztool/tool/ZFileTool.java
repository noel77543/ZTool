package tw.noel.sung.com.ztool.tool;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by noel on 2019/3/10.
 */
public class ZFileTool {


    private Context context;

    public ZFileTool(Context context) {
        this.context = context;
    }


    //-------------

    /***
     * 將圖片檔以指定格式存取於內部資料夾(須包含副檔名)
     * @param imageType  欲儲存的圖片檔之副檔名(需一致)
     */
    public void saveImageToInternalStorage(Bitmap bitmap, Bitmap.CompressFormat imageType, String imageName) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(imageType, 100, stream);
        saveFileToInternalStorage(imageName, stream.toByteArray());
    }

    //--------------

    /***
     *  將存於內部資料夾的圖片檔以Bitmap取出(須包含副檔名)
     */
    public Bitmap getImageFromInternalStorage(String fileName) {
        File file = new File(context.getCacheDir(), fileName);
        try {
            return BitmapFactory.decodeStream(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }


    //-------------

    /***
     * 存取檔案於內部資料夾(須包含副檔名)
     */
    public void saveFileToInternalStorage(String fileName, byte[] data) {
        try {
            File file = new File(context.getCacheDir(), fileName);

            FileOutputStream output = new FileOutputStream(file);
            output.write(data);
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //------------

    /***
     * 讀取內部資料夾中的檔案(須包含副檔名)
     */
    public File getFIleFromInternalStorage(String fileName) {
        File file = new File(context.getCacheDir(), fileName);
        if (file.exists()) {
            return file;
        } else {
            Toast.makeText(context, context.getString(tw.noel.sung.com.ztool.R.string.z_file_util_file_not_found), Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    //-------------

    /***
     * 讀取 assets中的檔案
     */
    public String getAssetsFileToString(String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(context.getAssets().open(fileName), "UTF-8"));

            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

}
