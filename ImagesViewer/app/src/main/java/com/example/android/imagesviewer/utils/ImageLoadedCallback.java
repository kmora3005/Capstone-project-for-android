package com.example.android.imagesviewer.utils;

import android.widget.ProgressBar;

import com.squareup.picasso.Callback;

/**
 * Project name ImagesViewer
 * Created by kenneth on 16/10/2017.
 */

public class ImageLoadedCallback implements Callback {
    public final ProgressBar progressBar;

    public  ImageLoadedCallback(ProgressBar progressBar){
        this.progressBar = progressBar;
    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onError() {

    }
}
