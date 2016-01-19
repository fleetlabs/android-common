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
        config.put("endpoint", "http://oss-cn-shanghai.aliyuncs.com");
        config.put("accessKeyId", "LVDUaIkwk9ul7mL7");
        config.put("accessKeySecret", "l03usWb8e5LG96VXJXQi8PYYoxeLqN");
        UploaderManager.getInstance().initConfig(mContext, OSSType.Ali, config);
    }
}
