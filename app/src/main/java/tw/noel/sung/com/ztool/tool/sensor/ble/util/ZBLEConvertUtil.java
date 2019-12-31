package tw.noel.sung.com.ztool.tool.sensor.ble.util;

public class ZBLEConvertUtil {


    //----------

    /***
     *  byte 轉 雜湊
     * @param hashInBytes
     * @return
     */
    public  String covertByteArrayToHex(byte[] hashInBytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : hashInBytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }

    //-----------

    /***
     *雜湊 轉  byte
     * @param cs
     * @return
     */
    public  byte[] covertHexToByteArray(CharSequence cs){
        byte[] bytes = new byte[cs.length()/2];
        for (int i=0;i<(cs.length()/2);i++)
            bytes[i] = (byte) Integer.parseInt(cs.toString().substring(2*i,2*i+2),16);
        return bytes;
    }
}
