package tw.noel.sung.com.ztool.tool.sensor.ble;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;
import java.util.UUID;

import tw.noel.sung.com.ztool.tool.ZCheckDeviceTool;
import tw.noel.sung.com.ztool.tool.sensor.ble.callback.ZBLEHandler;
import tw.noel.sung.com.ztool.tool.sensor.ble.util.ZBLEConvertUtil;

public class ZBLETool implements BluetoothAdapter.LeScanCallback, Runnable {

    private Context context;
    private ZCheckDeviceTool zCheckDeviceTool;
    private ZBLEHandler zbleHandler;
    private BluetoothManager bluetoothManager;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothGatt bluetoothGatt;

    private Handler handler = new Handler();
    private ArrayList<BluetoothDevice> bleDevices = new ArrayList<>();
    private boolean isBLEEnable = true;
    private ZBLEConvertUtil zbleConvertUtil;

    public ZBLETool(Context context, ZBLEHandler zbleHandler) {
        this.context = context;
        this.zbleHandler = zbleHandler;
        zbleConvertUtil = new ZBLEConvertUtil();
        zCheckDeviceTool = new ZCheckDeviceTool(context);
        if (!zCheckDeviceTool.isHasBLE()) {
            isBLEEnable = false;
            zbleHandler.onBLENotHave();
        } else {

            bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
            bluetoothAdapter = bluetoothManager.getAdapter();
            if (bluetoothAdapter == null) {
                isBLEEnable = false;
                zbleHandler.onBLENotHave();
            }
        }
    }

    //--------------------

    /***
     *  發送訊息至目標裝置
     */
    public void sendMessage(String message) {
        BluetoothGattCharacteristic bluetoothGattCharacteristic = zbleHandler.getGattCharacteristic();
        boolean isSendSuccess = false;
        if (isBLEEnable && bluetoothGattCharacteristic != null) {
            byte[] messageBytes = zbleConvertUtil.covertHexToByteArray(message);
            byte[] sendData = new byte[messageBytes.length + 2];
            sendData[0] = (byte) 0xaa;
            sendData[sendData.length - 1] = (byte) 0xff;
            for (int i = 1; i < sendData.length - 1; i++) {
                sendData[i] = messageBytes[i - 1];
            }
            bluetoothGattCharacteristic.setValue(sendData);
            isSendSuccess = bluetoothGatt.writeCharacteristic(bluetoothGattCharacteristic);
        }
        zbleHandler.onSendMessageToTargetBLEDevice(isSendSuccess, message);
    }


    //--------------------

    /***
     * 搜尋附近藍芽裝置
     */
    public void scanDevice(long scanMilliSecond) {
        if (isBLEEnable) {
            bleDevices.clear();
            handler.removeCallbacks(this);
            handler.postDelayed(this, scanMilliSecond);
            bluetoothAdapter.startLeScan(this);
        }
    }


    //----------------

    /***
     *  配對
     * @param deviceAddress  自onLeScan接收的BluetoothDevice中的address
     */
    public void connectDevice(String deviceAddress) {
        if (isBLEEnable) {
            BluetoothDevice currentConnectDevice = bluetoothAdapter.getRemoteDevice(deviceAddress);
            if (currentConnectDevice != null) {
                bluetoothGatt = currentConnectDevice.connectGatt(context, false, zbleHandler.getBluetoothGattCallback());
                zbleHandler.setBluetoothGatt(bluetoothGatt);
            }
            //裝置不存在 或已關閉藍芽配對
            else {
                zbleHandler.onTargetBLEDeviceDisableConnect();
            }
        }
    }


    //-----------------

    /***
     *  當掃描獲得鄰近藍芽裝置
     * @param bluetoothDevice
     * @param i
     * @param bytes
     */
    @Override
    public void onLeScan(BluetoothDevice bluetoothDevice, int i, byte[] bytes) {
        Log.e("TTT",bluetoothDevice.getAddress());
        if (!bleDevices.contains(bluetoothDevice)) {
            bleDevices.add(bluetoothDevice);
        }
    }

    //-----------

    /***
     *  持續時間結束  關閉掃描
     */
    @Override
    public void run() {
        bluetoothAdapter.stopLeScan(this);
        zbleHandler.onScanFinished(bleDevices);
    }
}
