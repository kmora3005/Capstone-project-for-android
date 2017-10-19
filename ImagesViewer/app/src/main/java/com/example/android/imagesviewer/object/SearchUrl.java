package com.example.android.imagesviewer.object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchUrl {

    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("template")
    @Expose
    public String template;

}
