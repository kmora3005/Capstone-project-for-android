package com.example.android.imagesviewer.object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchItem {

    @SerializedName("kind")
    @Expose
    public String kind;
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("htmlTitle")
    @Expose
    public String htmlTitle;
    @SerializedName("link")
    @Expose
    public String link;
    @SerializedName("displayLink")
    @Expose
    public String displayLink;
    @SerializedName("snippet")
    @Expose
    public String snippet;
    @SerializedName("htmlSnippet")
    @Expose
    public String htmlSnippet;
    @SerializedName("mime")
    @Expose
    public String mime;
    @SerializedName("image")
    @Expose
    public SearchImage image;

}
