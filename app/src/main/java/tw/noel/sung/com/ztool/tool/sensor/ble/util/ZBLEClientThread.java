package tw.noel.sung.com.ztool.tool.sensor.ble.util;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.util.UUID;

import tw.noel.sung.com.ztool.tool.sensor.ble.ZBLETool;

public class ZBLEClientThread extends Thread {
    private BluetoothDevice device;

    public ZBLEClientThread(BluetoothDevice device) {
        this.device = device;
    }

    @Override
    public void run() {
        BluetoothSocket bluetoothSocket;

        try {
            bluetoothSocket = device.createRfcommSocketToServiceRecord(UUID.fromString(ZBLETool.MY_UUID));
            bluetoothSocket.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
