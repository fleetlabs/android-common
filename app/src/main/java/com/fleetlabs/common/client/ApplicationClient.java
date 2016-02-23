package com.fleetlabs.common.client;

import android.app.Application;
import android.content.Context;

import com.fleetlabs.library.upload.OSSType;
import com.fleetlabs.library.upload.UploaderManager;

import java.util.HashMap;

/**
 * Created by Aaron.Wu on 2016/1/19.
 */
public class ApplicationClient extends Application {

    private Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();

        HashMap<String, String> config = new HashMap<>();
        //config.put("endpoint", "http://192.168.244.67/test.php");
        /*
        config.put("bucket", "fleetlabs");
        config.put("AccessKey", "VHCDeUtV6hPUp_GsgbnLslVnodmqaDJ7e2R-BCT2");
        config.put("SecretKey", "U2wd64q4hZZ4QW3JlHApOiF2Ou4aVOmSj3Zih2Gx");
        */

        config.put("endpoint", "http://yearsii.oss-cn-shanghai.aliyuncs.com");
        config.put("accessKeyId", "jYrAZ23K34PhU1m2");
        config.put("accessKeySecret", "Z1DnY9ngbSsS4n7NJwHimidMOdi1Wy");
        //config.put("bucket", "yearsii");
        /*config.put("key_url", "http://115.231.183.102:9090/api/simple_upload/with_key_upload_token.php");*/

        UploaderManager.getInstance().initConfig(mContext, OSSType.Ali, config);
    }
}
