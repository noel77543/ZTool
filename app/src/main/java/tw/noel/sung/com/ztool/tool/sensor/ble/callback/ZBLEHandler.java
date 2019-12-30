package tw.noel.sung.com.ztool.tool.sensor.ble.callback;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothProfile;

import java.util.ArrayList;

public class ZBLEHandler {

    private BluetoothGattCallback bluetoothGattCallback = new BluetoothGattCallback() {

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);

            switch (newState){
                //連線成功
                case BluetoothProfile.STATE_CONNECTED:
                    onTargetBLEDeviceConnected();
                    break;
                //解除連線
                case BluetoothProfile.STATE_DISCONNECTED:
                    onTargetBLEDeviceDisconnected();
                    break;
            }
        }

    };

    public ZBLEHandler() {

    }


    public BluetoothGattCallback getBluetoothGattCallback() {
        return bluetoothGattCallback;
    }

    /***
     * 不具備藍芽
     */
    public void onBLENotHave() {

    }

    /***
     *  當搜尋完畢
     */
    public void onScanFinished(ArrayList<BluetoothDevice> bleDevices) {

    }

    /***
     *  在嘗試配對時候  藍芽裝置不存在 或 已關閉配對
     */
    public void onTargetBLEDeviceDisableConnect() {

    }

    /***
     *  成功連接目標藍芽裝置
     */
    public void onTargetBLEDeviceConnected(){

    }

    /***
     *  至目標藍芽裝置解除連接
     */
    public void onTargetBLEDeviceDisconnected(){

    }

}
