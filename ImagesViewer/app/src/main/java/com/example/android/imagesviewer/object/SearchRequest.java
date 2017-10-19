package com.example.android.imagesviewer.object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchRequest {

    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("totalResults")
    @Expose
    public String totalResults;
    @SerializedName("searchTerms")
    @Expose
    public String searchTerms;
    @SerializedName("count")
    @Expose
    public Integer count;
    @SerializedName("startIndex")
    @Expose
    public Integer startIndex;
    @SerializedName("inputEncoding")
    @Expose
    public String inputEncoding;
    @SerializedName("outputEncoding")
    @Expose
    public String outputEncoding;
    @SerializedName("safe")
    @Expose
    public String safe;
    @SerializedName("cx")
    @Expose
    public String cx;
    @SerializedName("searchType")
    @Expose
    public String searchType;

}
