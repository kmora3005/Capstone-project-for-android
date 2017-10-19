package com.example.android.imagesviewer.object;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.List;

/**
 * Project name ImagesViewer
 * Created by kenneth on 07/10/2017.
 */

public class Album implements Parcelable {
    public static final Creator<Album> CREATOR = new Creator<Album>() {
        @Override
        public Album createFromParcel(Parcel in) {
            return new Album(in);
        }

        @Override
        public Album[] newArray(int size) {
            return new Album[size];
        }
    };
    public String title;
    @Exclude
    public String key;
    public List<AlbumImage> images;

    public Album() {
        images = new ArrayList<>();
    }

    public Album(String title, ArrayList<String> urls) {
        this();
        this.title = title;
        for (String url : urls) {
            AlbumImage albumImage = new AlbumImage(url);
            images.add(albumImage);
        }
    }

    public Album(String title, ArrayList<String> urls, String key) {
        this(title, urls);
        this.key = key;
    }

    protected Album(Parcel in) {
        title = in.readString();
        key = in.readString();
        images = in.createTypedArrayList(AlbumImage.CREATOR);
    }

    @Exclude
    public ArrayList<String> getUrls() {
        ArrayList<String> urls = new ArrayList<>();
        for (AlbumImage imageAlbum : images) {
            urls.add(imageAlbum.url);
        }
        return urls;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(key);
        parcel.writeTypedList(images);
    }

}
