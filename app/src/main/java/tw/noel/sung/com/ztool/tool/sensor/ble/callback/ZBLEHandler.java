package tw.noel.sung.com.ztool.tool.sensor.ble.callback;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Context;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import tw.noel.sung.com.ztool.R;
import tw.noel.sung.com.ztool.tool.sensor.ble.util.ZBLEConvertUtil;

public class ZBLEHandler {

    private BluetoothGatt bluetoothGatt;
    private ZBLEConvertUtil ZBLEConvertUtil;
    private Context context;
    private BluetoothGattCallback bluetoothGattCallback;
    private BluetoothGattService bluetoothGattService;
    private BluetoothGattCharacteristic gattCharacteristic;

    public ZBLEHandler(Context context) {
        this.context = context;
        ZBLEConvertUtil = new ZBLEConvertUtil();
        bluetoothGattCallback = new BluetoothGattCallback() {
            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                super.onConnectionStateChange(gatt, status, newState);

                switch (newState) {
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


            @Override
            public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                onGetMessageFromTargetBLEDevice(new String(characteristic.getValue(), StandardCharsets.UTF_8), ZBLEConvertUtil.covertByteArrayToHex(characteristic.getValue()), characteristic.getValue());
            }


            //發現服務，在藍牙連接的時候會調用
            @Override
            public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                if(bluetoothGatt != null){
                    List<BluetoothGattService> list = bluetoothGatt.getServices();
                    for (BluetoothGattService bluetoothGattService : list) {

                        List<BluetoothGattCharacteristic> gattCharacteristics = bluetoothGattService
                                .getCharacteristics();
                        for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {

                            if ("0000ffe1-0000-1000-8000-00805f9b34fb".equals(gattCharacteristic.getUuid().toString())) {
                                ZBLEHandler.this.bluetoothGattService = bluetoothGattService;
                                ZBLEHandler.this.gattCharacteristic = gattCharacteristic;
                            }
                        }

                    }
                }
            }
        };
    }

    //---------------

    public BluetoothGattCallback getBluetoothGattCallback() {
        return bluetoothGattCallback;
    }

    //---------------

    public BluetoothGattService getBluetoothGattService() {
        return bluetoothGattService;
    }

    //----------------

    public BluetoothGattCharacteristic getGattCharacteristic() {
        return gattCharacteristic;
    }


    //---------------

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
    public void onTargetBLEDeviceConnected() {

    }

    /***
     *  至目標藍芽裝置解除連接
     */
    public void onTargetBLEDeviceDisconnected() {

    }

    /***
     *  接收配對中裝置傳來的訊息
     */
    public void onGetMessageFromTargetBLEDevice(String message, String hex, byte[] bytes) {

    }

    /***
     *  當成功發送訊息給配對中裝置
     */
    public void onSendMessageToTargetBLEDevice(boolean isSuccess, String message) {

    }

    public void setBluetoothGatt(BluetoothGatt bluetoothGatt) {
        this.bluetoothGatt = bluetoothGatt;
    }
}