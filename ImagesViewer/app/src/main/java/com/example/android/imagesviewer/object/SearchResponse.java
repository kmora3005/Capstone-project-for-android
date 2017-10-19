package com.example.android.imagesviewer.object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchResponse {

    @SerializedName("kind")
    @Expose
    public String kind;
    @SerializedName("url")
    @Expose
    public SearchUrl url;
    @SerializedName("queries")
    @Expose
    public SearchQueries queries;
    @SerializedName("context")
    @Expose
    public SearchContext context;
    @SerializedName("searchInformation")
    @Expose
    public SearchInformation searchInformation;
    @SerializedName("items")
    @Expose
    public List<SearchItem> items = null;

}
