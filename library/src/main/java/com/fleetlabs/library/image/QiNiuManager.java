package com.fleetlabs.library.image;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Aaron.Wu on 2016/1/19.
 */
public class QiNiuManager {

    public static final String TAG = "QiNiuManager";
    private static Context mContext;

    private static final QiNiuManager INSTANCE = new QiNiuManager();

    public static QiNiuManager get(Context context) {
        mContext = context;
        return INSTANCE;
    }

    public void upload() {
        Toast.makeText(mContext, TAG, Toast.LENGTH_SHORT).show();
    }
}
