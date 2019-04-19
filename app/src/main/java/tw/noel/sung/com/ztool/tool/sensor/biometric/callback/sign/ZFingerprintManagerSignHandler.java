package tw.noel.sung.com.ztool.tool.sensor.biometric.callback.sign;

import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Base64;

import java.security.SignatureException;
import tw.noel.sung.com.ztool.tool.sensor.biometric.KeyHelper;

/**
 * Created by noel on 2019/4/16.
 */

@RequiresApi(api = Build.VERSION_CODES.M)
public abstract class ZFingerprintManagerSignHandler extends FingerprintManager.AuthenticationCallback{

    private KeyHelper keyHelper;

    public ZFingerprintManagerSignHandler setKeyHelper(KeyHelper keyHelper) {
        this.keyHelper = keyHelper;
        return this;
    }
    //---------
    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        super.onAuthenticationSucceeded(result);

        try {
            FingerprintManager.CryptoObject cryptoObject = result.getCryptoObject();
            onSignedFingerPrint(Base64.encodeToString(cryptoObject.getSignature().sign(), Base64.DEFAULT), new String(Base64.encode(keyHelper.getPublicKey().getEncoded(), Base64.DEFAULT)));

        } catch (SignatureException e) {
            e.printStackTrace();
        }

    }

    //----------------

    /***
     *  當註冊指紋 取得keyString 用於解密
     */
    public abstract void onSignedFingerPrint(String lockString, String keyString) ;


    //-------------------
    /***
     * 當取消
     */
    public abstract void onCancelScan();
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
