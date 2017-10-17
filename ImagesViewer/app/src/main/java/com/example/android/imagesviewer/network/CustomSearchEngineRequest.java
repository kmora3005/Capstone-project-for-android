package com.example.android.imagesviewer.network;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.android.imagesviewer.R;
import com.example.android.imagesviewer.object.SearchResponse;

/**
 * Project name ImagesViewer
 * Created by kenneth on 01/10/2017.
 */

public class CustomSearchEngineRequest {
    private static final String TAG = CustomSearchEngineRequest.class.getSimpleName();
    private final static String PATH_GOOGLE_CUSTOM_SEARCH = "https://www.googleapis.com/customsearch/v1?";
    private final static String PARAM_QUERY = "q";
    private final static String PARAM_CX = "cx";
    private final static String PARAM_SEARCH_TYPE = "searchType";
    private final static String PARAM_START = "start";
    private final static String PARAM_KEY = "key";

    public void searchImages(Context context, Response.Listener<SearchResponse> requestSuccessListener, String query, int index) {
        String key = context.getResources().getString(R.string.google_custom_search_api_key);
        String cx = context.getResources().getString(R.string.google_custom_search_api_cx);
        Uri builtUri = Uri.parse(PATH_GOOGLE_CUSTOM_SEARCH)
                .buildUpon()
                .appendQueryParameter(PARAM_QUERY, query)
                .appendQueryParameter(PARAM_CX, cx)
                .appendQueryParameter(PARAM_SEARCH_TYPE, "image")
                .appendQueryParameter(PARAM_START, String.valueOf(index))
                .appendQueryParameter(PARAM_KEY, key)
                .build();

        String stringURL = builtUri.toString();
        RequestQueue queue = Volley.newRequestQueue(context);
        GsonRequest<SearchResponse> myReq = new GsonRequest<>(
                SearchResponse.class,
                requestSuccessListener,
                requestErrorListener(),
                stringURL);

        queue.add(myReq);
    }


    private Response.ErrorListener requestErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.getMessage());
            }
        };
    }
}
