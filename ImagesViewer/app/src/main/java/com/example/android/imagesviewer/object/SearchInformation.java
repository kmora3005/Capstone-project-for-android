
package com.example.android.imagesviewer.object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchInformation {

    @SerializedName("searchTime")
    @Expose
    public Double searchTime;
    @SerializedName("formattedSearchTime")
    @Expose
    public String formattedSearchTime;
    @SerializedName("totalResults")
    @Expose
    public String totalResults;
    @SerializedName("formattedTotalResults")
    @Expose
    public String formattedTotalResults;

}
