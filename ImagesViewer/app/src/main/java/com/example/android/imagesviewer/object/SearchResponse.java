
package com.example.android.imagesviewer.object;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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
