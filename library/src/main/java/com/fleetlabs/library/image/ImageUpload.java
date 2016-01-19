package com.fleetlabs.library.image;

import android.content.Context;

/**
 * Created by Aaron.Wu on 2016/1/18.
 */
public class ImageUpload {

    public static AliManager withAli(Context context) {
        context = context.getApplicationContext();
        return AliManager.get(context);
    }

    public static QiNiuManager withQiNiu(Context context) {
        context = context.getApplicationContext();
        return QiNiuManager.get(context);
    }

}
