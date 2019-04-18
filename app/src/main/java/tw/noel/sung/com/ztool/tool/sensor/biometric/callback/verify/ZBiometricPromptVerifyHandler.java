package tw.noel.sung.com.ztool.tool.sensor.biometric.callback.verify;

import android.hardware.biometrics.BiometricPrompt;
import android.os.Build;
import android.security.keystore.KeyProperties;
import android.support.annotation.RequiresApi;
import android.util.Base64;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

import tw.noel.sung.com.ztool.tool.sensor.biometric.VerifyHelper;

/**
 * Created by noel on 2019/4/16.
 */
@RequiresApi(api = Build.VERSION_CODES.O)
public class ZBiometricPromptVerifyHandler extends BiometricPrompt.AuthenticationCallback {
    private String keyString;
    private String lockString;

    public ZBiometricPromptVerifyHandler(String lockString, String keyString) {
        this.lockString = lockString;
        this.keyString = keyString;
    }

    //-------------
    @Override
    public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
        super.onAuthenticationSucceeded(result);

        BiometricPrompt.CryptoObject cryptoObject = result.getCryptoObject();

        try {

            byte[] byteKey = Base64.decode(keyString, Base64.DEFAULT);
            X509EncodedKeySpec X509publicKey = new X509EncodedKeySpec(byteKey);
            KeyFactory keyFactory = KeyFactory.getInstance(KeyProperties.KEY_ALGORITHM_EC);
            PublicKey key = keyFactory.generatePublic(X509publicKey);
            VerifyHelper verifyHelper = new VerifyHelper();
            onVerifiedFingerPrint(verifyHelper.verifyCryptoObject(cryptoObject, Base64.decode(lockString, Base64.DEFAULT), key));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //----------------------

    /***
     * 解密指紋
     */
    public void onVerifiedFingerPrint(boolean isSuccess) {

    }

    //-------------------

    /***
     * 當取消
     */
    public void onCancelScan() {

    }
    //-------------------

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {
        super.onAuthenticationError(errorCode, errString);
        //取消掃描
        if (errorCode == BiometricPrompt.BIOMETRIC_ERROR_USER_CANCELED) {
            onCancelScan();
        }
    }
}
