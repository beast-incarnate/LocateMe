package com.example.kunalsingh.locateme;

import android.app.Application;

import com.firebase.client.Firebase;
import android.util.Base64;
import android.util.Log;import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/**
 * Created by kunalsingh on 08/02/17.
 */

public class LocateMe extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Firebase.setAndroidContext(this);

        MessageDigest md = null;
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        Log.i("SecretKey = ",Base64.encodeToString(md.digest(), Base64.DEFAULT));

    }
}
