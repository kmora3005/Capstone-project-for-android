package com.example.android.imagesviewer.object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchImage {

    @SerializedName("contextLink")
    @Expose
    public String contextLink;
    @SerializedName("height")
    @Expose
    public Integer height;
    @SerializedName("width")
    @Expose
    public Integer width;
    @SerializedName("byteSize")
    @Expose
    public Integer byteSize;
    @SerializedName("thumbnailLink")
    @Expose
    public String thumbnailLink;
    @SerializedName("thumbnailHeight")
    @Expose
    public Integer thumbnailHeight;
    @SerializedName("thumbnailWidth")
    @Expose
    public Integer thumbnailWidth;

}
