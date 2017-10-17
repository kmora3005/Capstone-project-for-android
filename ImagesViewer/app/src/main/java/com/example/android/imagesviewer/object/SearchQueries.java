
package com.example.android.imagesviewer.object;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchQueries {

    @SerializedName("request")
    @Expose
    public List<SearchRequest> request = null;
    @SerializedName("nextPage")
    @Expose
    public List<SearchNextPage> nextPage = null;

}
