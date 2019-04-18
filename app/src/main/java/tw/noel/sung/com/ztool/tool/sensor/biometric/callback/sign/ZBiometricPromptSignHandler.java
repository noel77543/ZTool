package tw.noel.sung.com.ztool.tool.sensor.biometric.callback.sign;

import android.hardware.biometrics.BiometricPrompt;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Base64;
import java.security.SignatureException;
import tw.noel.sung.com.ztool.tool.sensor.biometric.KeyHelper;

/**
 * Created by noel on 2019/4/16.
 */
@RequiresApi(api = Build.VERSION_CODES.O)
public class ZBiometricPromptSignHandler extends BiometricPrompt.AuthenticationCallback {

    private KeyHelper keyHelper;

    //----------

    public ZBiometricPromptSignHandler setKeyHelper(KeyHelper keyHelper) {
        this.keyHelper = keyHelper;
        return this;
    }
    //-------------
    @Override
    public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
        super.onAuthenticationSucceeded(result);

        try {
            BiometricPrompt.CryptoObject cryptoObject = result.getCryptoObject();
            onSignedFingerPrint(Base64.encodeToString(cryptoObject.getSignature().sign(), Base64.DEFAULT), new String(Base64.encode(keyHelper.getPublicKey().getEncoded(), Base64.DEFAULT)));
        } catch (SignatureException e) {
            e.printStackTrace();
        }
    }
    //----------------

    /***
     *  當註冊指紋 取得keyString 用於解密
     */
    public void onSignedFingerPrint(String lockString, String keyString) {

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
