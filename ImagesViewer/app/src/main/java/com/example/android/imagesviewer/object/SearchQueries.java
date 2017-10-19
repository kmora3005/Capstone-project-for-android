package com.example.android.imagesviewer.object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchQueries {

    @SerializedName("request")
    @Expose
    public List<SearchRequest> request = null;
    @SerializedName("nextPage")
    @Expose
    public List<SearchNextPage> nextPage = null;

}
