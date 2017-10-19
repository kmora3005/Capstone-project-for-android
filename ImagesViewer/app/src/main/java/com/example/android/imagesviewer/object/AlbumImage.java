package com.example.android.imagesviewer.object;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Project name ImagesViewer
 * Created by kenneth on 07/10/2017.
 */

public class AlbumImage implements Parcelable {
    public static final Creator<AlbumImage> CREATOR = new Creator<AlbumImage>() {
        @Override
        public AlbumImage createFromParcel(Parcel in) {
            return new AlbumImage(in);
        }

        @Override
        public AlbumImage[] newArray(int size) {
            return new AlbumImage[size];
        }
    };
    public String url;

    public AlbumImage() {

    }

    public AlbumImage(String url) {
        this.url = url;
    }

    protected AlbumImage(Parcel in) {
        url = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(url);
    }
}
