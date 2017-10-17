package com.example.android.imagesviewer.utils;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Project name ImagesViewer
 * Created by kenneth on 14/10/2017.
 */

public class ViewFunctions {
    public static int numberOfColumns(Context context){
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        float screenWidth = dm.widthPixels / dm.density;
        return screenWidth>=600 ? 3:2;
    }
}
