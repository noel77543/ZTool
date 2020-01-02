package tw.noel.sung.com.ztool.tool.sensor.ble;

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
import android.util.Log;

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
    //所有的藍芽技術聯盟定義UUID共用了一個基本的UUID
    public static String MY_UUID = "0x0000xxxx-0000-1000-8000-00805F9B34FB";


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
     * 搜尋附近藍芽裝置  有限制時間
     */
    public void scanDevice(long second) {
        if (isBLEEnable) {
            if (!ZBLETool.this.bluetoothAdapter.isDiscovering()) {

                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
                intentFilter.addAction(BluetoothDevice.ACTION_PAIRING_REQUEST);
                intentFilter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);

                //註冊廣播
                context.registerReceiver(zbleBroadcastReceiver, intentFilter);
                bleDevices.clear();
                handler.removeCallbacks(scanRunnable);
                //如有設定時間開啟則開始倒數 時間到後關閉掃描
                if (second > 0) {
                    handler.postDelayed(scanRunnable, second * 1000);
                }
                bluetoothAdapter.startDiscovery();
            }
        }
    }

    //--------------------

    /***
     * 搜尋附近藍芽裝置 無限制時間
     */
    public void scanDevice() {
        this.scanDevice(0);
    }

    //--------------------

    /***
     *  使裝置可被搜尋  最多3600秒
     */
    public void showDevice(long second) {
        context.startActivity(new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE).putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, second));
    }
    //--------------------

    /***
     *  使裝置可被搜尋  持續顯示
     */
    public void showDevice() {
        this.showDevice(0);
    }

    //----------------

    /***
     *  使裝置為client端 向server建立連線
     * @param deviceAddress  自onLeScan接收的BluetoothDevice中的address
     */
    public void connectDevice(String deviceAddress) {
        if (isBLEEnable) {
            bluetoothAdapter.cancelDiscovery();

            BluetoothDevice currentConnectDevice = bluetoothAdapter.getRemoteDevice(deviceAddress);
            if (currentConnectDevice != null) {


                bluetoothGatt = currentConnectDevice.connectGatt(context, false, zbleHandler.getBluetoothGattCallback());
                zbleHandler.setBluetoothGatt(bluetoothGatt);
            }
            //裝置不存在 或已關閉藍芽配對 或未結束掃描
            else {
                zbleHandler.onTargetBLEDeviceDisableConnect();
            }
        }
    }

    //-----------------

    /***
     * 使裝置為 server端  等待client訪問 建立連線
     */
    public void waitDevice() {
//         final BluetoothServerSocket mmServerSocket;
//        BluetoothServerSocket tmp = null;
//        try {
//            // MY_UUID is the app's UUID string, also used by the client code
//            tmp = bluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
//        } catch (IOException e) {
//        }
//        mmServerSocket = tmp;
//
//
//            BluetoothSocket socket = null;
//            // Keep listening until exception occurs or a socket is returned
//            while (true) {
//                try {
//                    socket = mmServerSocket.accept();
//                } catch (IOException e) {
//                    break;
//                }
//                // If a connection was accepted
//                if (socket != null) {
//                    // Do work to manage the connection (in a separate thread)
//                    manageConnectedSocket(socket);
//                    mmServerSocket.close();
//                    break;
//                }
//            }

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

                short rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);
                Log.e("T", rssi + "");

                String uuid = intent.getStringExtra(BluetoothDevice.EXTRA_UUID);
                Log.e("TT", uuid);
                
                //搜尋到的藍芽裝置
                BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Log.e("TTT", bluetoothDevice.getName() + "");


                if (!bleDevices.contains(bluetoothDevice)) {
                    bleDevices.add(bluetoothDevice);
                }
            }
        }
    }
}



