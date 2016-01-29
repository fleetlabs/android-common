package com.fleetlabs.library.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by alvinzeng on 11/13/15.
 */
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;


public class ViewUtils {

    /**
     * This method converts device specific pixels to device independent pixels.
     *
     * @param px A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent db equivalent to px value
     */
    public static float convertPixelsToDp(Context context, float px) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return px / (metrics.densityDpi / 160f);

    }
    /**
     * This method converts device specific pixels to device independent pixels.
     *
     * @param px A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent db equivalent to px value
     */
    public static int convertPixelsToDp(Context context, int px) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return (int) (px / (metrics.densityDpi / 160f));
    }

    /**
     * Convert dp to pixels (int)
     */
    public static int convertDpToPixelInt(Context context, float dp) {

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return (int) (dp * (metrics.densityDpi / 160f));
    }

    /**
     * Convert dp to pixels (float)
     */
    public static float convertDpToPixel(Context context, float dp) {

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return (dp * (metrics.densityDpi / 160f));
    }

    public static float getScreenWidth(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();

        return metrics.widthPixels;
    }

    public static int getScreenWidthInt(Context context) {
        return (int)getScreenWidth(context);
    }

    public static <T extends View> T findById(View view, int id) {
        return (T) view.findViewById(id);
    }

    public static <T extends View> T findById(Activity activity, int id) {
        return (T) activity.findViewById(id);
    }
}