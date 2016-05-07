package com.ablanco.tonsofdamage.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by Álvaro Blanco on 30/03/2016.
 * TonsOfDamage
 */
public class SizeUtils {

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static int convertDpToPixel(int dp){
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return (int) (dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px A value in px (pixels) unit. Which we need to convert into db
     * @return A float value to represent dp equivalent to px value
     */
    public static int convertPixelsToDp(int px){
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return (int) (px / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static int getScreenWidth(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point size = new Point();
        wm.getDefaultDisplay().getSize(size);
        return size.x;
    }

    public static int getScreenHeight(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point size = new Point();
        wm.getDefaultDisplay().getSize(size);
        return size.y;
    }
}
