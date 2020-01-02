package tw.noel.sung.com.ztool.tool.sensor.ble;

import android.bluetooth.BluetoothDevice;

public class ZBLEObject {

    private BluetoothDevice bluetoothDevice;
    private short rssi;


    public ZBLEObject(BluetoothDevice bluetoothDevice,short rssi){
        this.bluetoothDevice = bluetoothDevice;
        this.rssi = rssi;
    }

    public BluetoothDevice getBluetoothDevice() {
        return bluetoothDevice;
    }

    public short getRssi() {
        return rssi;
    }

    public void setBluetoothDevice(BluetoothDevice bluetoothDevice) {
        this.bluetoothDevice = bluetoothDevice;
    }

    public void setRssi(short rssi) {
        this.rssi = rssi;
    }
}
