package tw.noel.sung.com.ztool.tool.sensor.biometric;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.biometrics.BiometricPrompt;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

import java.security.InvalidKeyException;
import java.util.concurrent.Executor;

import tw.noel.sung.com.ztool.R;
import tw.noel.sung.com.ztool.tool.sensor.biometric.util.BiometricHelper;
import tw.noel.sung.com.ztool.tool.sensor.biometric.util.KeyHelper;
import tw.noel.sung.com.ztool.tool.sensor.biometric.util.callback.ZBiometricPromptHandler;
import tw.noel.sung.com.ztool.tool.sensor.biometric.util.callback.ZFingerprintManagerHandler;


/**
 * Created by noel on 2019/1/21.
 */
@RequiresApi(api = Build.VERSION_CODES.M)
public class ZBiometricTool {
    /***
     * 指紋辨識
     * 在api 23 時 出現
     * 在api 28 時 歸類於BiometricPrompt類 之生物辨識應用
     */
    //生物驗證類 android api 28 以上
    private BiometricPrompt biometricPrompt;
    //指紋辨識類 android api 23 - 27
    private FingerprintManager fingerprintManager;

    private Context context;
    private Executor executor;
    private CancellationSignal cancellationSignal;

    private BiometricHelper biometricHelper;
    private KeyHelper keyHelper;

    public ZBiometricTool(Context context) {
        this.context = context;

        keyHelper = new KeyHelper(context);
        biometricHelper = new BiometricHelper(context);
        cancellationSignal = new CancellationSignal();
    }

    //--------

    /***
     * 進行辨識
     *  android api 23 - 27 之間
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void startScanFinger(final ZFingerprintManagerHandler zFingerprintManagerHandler) {
        try {
            if (biometricHelper.isCanFingerPrint()) {
                fingerprintManager = (FingerprintManager) context.getSystemService(Activity.FINGERPRINT_SERVICE);
                fingerprintManager.authenticate(keyHelper.getFingerprintManagerCompatCryptoObject(), cancellationSignal, 0, zFingerprintManagerHandler.setPublicKey(keyHelper.getPublicKey()), null);
            } else {
                Toast.makeText(context, context.getString(R.string.not_finger_print), Toast.LENGTH_SHORT).show();
            }
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
    }

    //----------

    /***
     * 進行辨識
     *  android api 28 以上
     */
    @RequiresApi(api = Build.VERSION_CODES.P)
    public void startScanFinger(final ZBiometricPromptHandler zBiometricPromptHandler) {

        try {
            if (biometricHelper.isCanBioMetricAuthentication()) {
                executor = context.getMainExecutor();
                biometricPrompt = new BiometricPrompt
                        .Builder(context)
                        .setTitle(context.getString(R.string.finger_print))
                        .setDescription(context.getString(R.string.finger_print_description))
                        .setNegativeButton(context.getString(R.string.cancel), executor, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                zBiometricPromptHandler.onCancelScan();
                            }
                        })
                        .build();
                biometricPrompt.authenticate(keyHelper.getBiometricPromptCryptoObject(), cancellationSignal, executor, zBiometricPromptHandler.setPublicKey(keyHelper.getPublicKey()));


            } else {
                Toast.makeText(context, context.getString(R.string.not_finger_print), Toast.LENGTH_SHORT).show();
            }
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
    }

    //-------------------

    /***
     * 停止掃描
     */
    public void stopScan(){
        cancellationSignal.cancel();
    }
}