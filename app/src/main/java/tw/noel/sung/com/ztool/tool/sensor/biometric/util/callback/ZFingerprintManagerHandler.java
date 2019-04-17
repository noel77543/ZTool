package tw.noel.sung.com.ztool.tool.sensor.biometric.util.callback;

import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.security.PublicKey;
import java.security.SignatureException;

/**
 * Created by noel on 2019/4/16.
 */
@RequiresApi(api = Build.VERSION_CODES.M)
public class ZFingerprintManagerHandler extends FingerprintManager.AuthenticationCallback {


    private PublicKey publicKey;

    public ZFingerprintManagerHandler() {
    }

    public ZFingerprintManagerHandler setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
        return this;
    }

    //---------
    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        super.onAuthenticationSucceeded(result);
        try {
            FingerprintManager.CryptoObject cryptoObject = result.getCryptoObject();
            onSignFingerPrint(cryptoObject, cryptoObject.getSignature().sign(),publicKey);
        } catch (SignatureException e) {
            e.printStackTrace();
        }
    }

    //----------------

    /***
     *  當註冊指紋 取得byte[] 用於解密
     * @param sign
     */
    public void onSignFingerPrint(FingerprintManager.CryptoObject cryptoObject, byte[] sign, PublicKey publicKey) {

    }

    //-------------------
    /***
     * 當取消
     */
    public void onCancelScan(){

    }
    //-----------

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {
        super.onAuthenticationError(errorCode, errString);
        //取消掃描
        if(errorCode == FingerprintManager.FINGERPRINT_ERROR_USER_CANCELED){
            onCancelScan();
        }
    }
}