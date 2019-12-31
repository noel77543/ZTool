package tw.noel.sung.com.ztool.tool.sensor.ble;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;

import androidx.annotation.RequiresPermission;

import java.util.ArrayList;

import tw.noel.sung.com.ztool.tool.ZCheckDeviceTool;
import tw.noel.sung.com.ztool.tool.sensor.ble.callback.ZBLEHandler;
import tw.noel.sung.com.ztool.tool.sensor.ble.util.ZBLEConvertUtil;

public class ZBLETool {

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
    private Runnable scanRunnable;
    private ZBLEBroadcastReceiver zbleBroadcastReceiver;

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
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            bluetoothAdapter.enable();
            zbleBroadcastReceiver = new ZBLEBroadcastReceiver();


            if (bluetoothAdapter == null) {
                isBLEEnable = false;
                zbleHandler.onBLENotHave();
            }

            //持續時間結束  關閉掃描
            scanRunnable = new Runnable() {
                @Override
                public void run() {
                    if (ZBLETool.this.bluetoothAdapter.isDiscovering()) {
                        ZBLETool.this.bluetoothAdapter.cancelDiscovery();
                    }
                    ZBLETool.this.context.unregisterReceiver(ZBLETool.this.zbleBroadcastReceiver);
                    ZBLETool.this.zbleHandler.onScanFinished(bleDevices);
                }
            };
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
    @RequiresPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
    public void scanDevice(long scanMilliSecond, boolean showSelfDevice) {
        if (isBLEEnable) {
            if (ZBLETool.this.bluetoothAdapter.isDiscovering()) {
                ZBLETool.this.bluetoothAdapter.cancelDiscovery();
            }
            ZBLETool.this.context.unregisterReceiver(ZBLETool.this.zbleBroadcastReceiver);

            //註冊廣播
            context.registerReceiver(zbleBroadcastReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
            bleDevices.clear();
            handler.removeCallbacks(scanRunnable);
            handler.postDelayed(scanRunnable, scanMilliSecond);
            bluetoothAdapter.startDiscovery();
            if (showSelfDevice) {
                //可被看見 3600 毫秒
                context.startActivity(new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE).putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 3600));
            }
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


    //-------------

    /***
     * 廣播器
     */
    private class ZBLEBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //當掃描獲得鄰近藍芽裝置
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                //搜尋到的藍芽裝置
                BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (!bleDevices.contains(bluetoothDevice)) {
                    bleDevices.add(bluetoothDevice);
                }
            }
        }
    }
}



