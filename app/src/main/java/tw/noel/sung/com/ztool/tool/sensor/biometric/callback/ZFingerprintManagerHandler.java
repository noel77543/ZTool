package tw.noel.sung.com.ztool.tool.sensor.biometric.callback;

import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.security.keystore.KeyProperties;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Base64;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.spec.X509EncodedKeySpec;

import tw.noel.sung.com.ztool.tool.sensor.biometric.KeyHelper;
import tw.noel.sung.com.ztool.tool.sensor.biometric.VerifyHelper;

/**
 * Created by noel on 2019/4/16.
 */
@RequiresApi(api = Build.VERSION_CODES.M)
public class ZFingerprintManagerHandler extends FingerprintManager.AuthenticationCallback {


    private KeyHelper keyHelper;
    private String keyString;
    private String lockString;

    public ZFingerprintManagerHandler(@Nullable String lockString, @Nullable String keyString) {
        this.lockString = lockString;
        this.keyString = keyString;
    }


    public ZFingerprintManagerHandler() {
    }

    public ZFingerprintManagerHandler setKeyHelper(KeyHelper keyHelper) {
        this.keyHelper = keyHelper;
        return this;
    }

    //---------
    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        super.onAuthenticationSucceeded(result);

        try {
            FingerprintManager.CryptoObject cryptoObject = result.getCryptoObject();

            if (lockString != null && keyString != null) {
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
            //未建立則走向創建與註冊流程
            else {
                onSignedFingerPrint(Base64.encodeToString(cryptoObject.getSignature().sign(), Base64.DEFAULT), new String(Base64.encode(keyHelper.getPublicKey().getEncoded(), Base64.DEFAULT)));
            }
        } catch (SignatureException e) {
            e.printStackTrace();
        }

    }


    //----------------

    /***
     *  當註冊指紋 取得keyString 用於解密
     *   TODO 當需解密時 帶入  ZFingerprintManagerHandler(@Nullable String lockString, @Nullable String keyString)) 即可觸發onVerifiedFingerPrint
     */
    public void onSignedFingerPrint(String lockString, String keyString) {

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
