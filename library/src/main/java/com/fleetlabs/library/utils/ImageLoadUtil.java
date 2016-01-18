package com.fleetlabs.library.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by Aaron.Wu on 2016/1/18.
 */
public class ImageLoadUtil {

    public static void load(Context mContext, String url, ImageView view) {
        if (!StringUtil.isEmpty(url)) {
            Glide.with(mContext).load(url).into(view);
        }
    }

    public static Bitmap convertBase64ToImage(String string) {
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(string, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
