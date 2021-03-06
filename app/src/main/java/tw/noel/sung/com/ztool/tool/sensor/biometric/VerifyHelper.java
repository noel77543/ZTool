package tw.noel.sung.com.ztool.tool.sensor.biometric;

import android.hardware.biometrics.BiometricPrompt;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import androidx.annotation.RequiresApi;

import java.security.InvalidKeyException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;

/**
 * Created by noel on 2019/4/16.
 */
public class VerifyHelper {
    public VerifyHelper() {

    }
    //--------------

    /***
     *   android api 23 ~ 27
     * 以公鑰進行解密
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean verifyCryptoObject(FingerprintManager.CryptoObject cryptoObject, byte[] signByteArray, PublicKey publicKey) {
        try {
            Signature signature = cryptoObject.getSignature();
            signature.initVerify(publicKey);
            return signature.verify(signByteArray);
        } catch (InvalidKeyException | SignatureException e) {
            e.printStackTrace();
        }
        return false;
    }

    //--------------

    /***
     *   android api 28 up
     * 以公鑰進行解密
     */
    @RequiresApi(api = Build.VERSION_CODES.P)
    public boolean verifyCryptoObject(BiometricPrompt.CryptoObject cryptoObject, byte[] signByteArray, PublicKey publicKey) {
        try {
            Signature signature = cryptoObject.getSignature();
            signature.initVerify(publicKey);
            return signature.verify(signByteArray);
        } catch (InvalidKeyException | SignatureException e) {
            e.printStackTrace();
        }
        return false;
    }
}
