package com.example.android.imagesviewer.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.example.android.imagesviewer.R;
import com.example.android.imagesviewer.adapter.ThumbnailRecyclerAdapter;
import com.example.android.imagesviewer.network.CustomSearchEngineRequest;
import com.example.android.imagesviewer.object.Album;
import com.example.android.imagesviewer.object.SearchItem;
import com.example.android.imagesviewer.object.SearchResponse;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;

import static com.example.android.imagesviewer.utils.Constants.ALBUM_KEY;
import static com.example.android.imagesviewer.utils.Constants.IS_NEW_ALBUM_KEY;
import static com.example.android.imagesviewer.utils.ViewFunctions.numberOfColumns;

public class AlbumEditionActivity extends AppCompatActivity implements ThumbnailRecyclerAdapter.ThumbnailAdapterOnClickHandler {
    private static final String SELECTED_IMAGES_INDICES_KEY = "images_indices_key";
    private static final String IMAGES_URL_KEY = "images_url_key";
    private static final String BUTTON_MORE_KEY = "button_more_key";

    private ArrayList<Integer> mSelectedImagesIndices;
    private String mLastQuery;
    private Album mAlbum;
    private ThumbnailRecyclerAdapter mThumbnailRecyclerAdapter;
    private FirebaseAnalytics mFirebaseAnalytics;

    private CoordinatorLayout mCoordinatorLayout;
    private Button mButtonMore;
    private Button mButtonNext;
    private EditText mEditTextTitle;
    private EditText mEditTextSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_edition);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        mCoordinatorLayout = findViewById(R.id.coordinatorLayout);
        mEditTextTitle = findViewById(R.id.et_title);
        mEditTextSearch = findViewById(R.id.et_search);
        mButtonMore = findViewById(R.id.b_more);
        mButtonNext = findViewById(R.id.b_next);
        RecyclerView rvImages = findViewById(R.id.rv_images);

        setEditTextEvents();
        ArrayList<String>  urls = new ArrayList<>();
        mSelectedImagesIndices= new ArrayList<>();
        if (savedInstanceState != null) {
            urls = savedInstanceState.getStringArrayList(IMAGES_URL_KEY);
            mSelectedImagesIndices  = savedInstanceState.getIntegerArrayList(SELECTED_IMAGES_INDICES_KEY);
            mLastQuery = savedInstanceState.getString(BUTTON_MORE_KEY);
        }

        String title = getResources().getString(R.string.title_album_new_activity);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            mAlbum = bundle.getParcelable(ALBUM_KEY);
            assert mAlbum != null;
            title = getResources().getString(R.string.title_album_edition_activity);
            mEditTextTitle.setText(mAlbum.title);
        }
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(title);

        int numColumns=numberOfColumns(this);
        GridLayoutManager layoutManager= new GridLayoutManager(this, numColumns);
        mThumbnailRecyclerAdapter = new ThumbnailRecyclerAdapter(this, this, urls, mSelectedImagesIndices);
        rvImages.setAdapter(mThumbnailRecyclerAdapter);
        rvImages.setLayoutManager(layoutManager);
    }

    private void setEditTextEvents(){
        mEditTextSearch.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View arg0, boolean gotfocus)
            {
                if(gotfocus)
                {
                    mEditTextSearch.setCompoundDrawables(null, null, null, null);
                }
                else
                {
                    if(mEditTextSearch.getText().length()==0)
                        mEditTextSearch.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search_black_24dp, 0, 0, 0);
                }
            }
        });

        mEditTextSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    mButtonMore.setVisibility(View.VISIBLE);
                    String searchedWord=v.getText().toString();

                    Bundle bundle = new Bundle();
                    bundle.putString(FirebaseAnalytics.Param.SEARCH_TERM, searchedWord);
                    mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SEARCH,bundle);

                    performSearch(searchedWord);
                    return true;
                }
                return false;
            }
        });
    }

    private void performSearch(String query) {
        mLastQuery=query;
        new SearchImagesTask().execute(query);
    }

    @Override
    public void onClickPicture(int position) {
        mButtonNext.setVisibility(View.VISIBLE);
        mThumbnailRecyclerAdapter.setSelectedIndex(position);
        mSelectedImagesIndices= mThumbnailRecyclerAdapter.getSelectedIndices();
    }

    @Override
    public void onClickRemovePicture(int position) {

    }

    private class SearchImagesTask extends AsyncTask<String, Void, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(String... strings) {
            String query=strings[0];
            final int index= mThumbnailRecyclerAdapter.getUrls().size()+1;
            new CustomSearchEngineRequest().searchImages(AlbumEditionActivity.this, new Response.Listener<SearchResponse> () {
                @Override
                public void onResponse(SearchResponse response) {
                    ArrayList<String> urls= new ArrayList<>();
                    for (SearchItem item:response.items){
                        urls.add(item.link);
                    }
                    mThumbnailRecyclerAdapter.setUrls(urls);

                }
            },query,index);
            return null;
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putStringArrayList(IMAGES_URL_KEY, mThumbnailRecyclerAdapter.getUrls());
        outState.putIntegerArrayList(SELECTED_IMAGES_INDICES_KEY, mSelectedImagesIndices);
        outState.putString(BUTTON_MORE_KEY, mLastQuery);
    }

    public void onClickMore(View view) {
        performSearch(mLastQuery);
    }

    public void onClickNext(View view) {
        ArrayList<String> selectedUrls= mThumbnailRecyclerAdapter.getSelectedUrls();
        if (mAlbum!=null){
            for(int index = mAlbum.getUrls().size() - 1; index >= 0; index--){
                String url = mAlbum.getUrls().get(index);

                if (!selectedUrls.contains(url)){
                    selectedUrls.add(0,url);
                }
            }
        }

        String title = mEditTextTitle.getText().toString();

        if (selectedUrls.size()==0){
            showSnackbar(R.string.no_image_selected);
            return;
        }
        if (TextUtils.isEmpty(title)){
            showSnackbar(R.string.empty_title);
            return;
        }

        String key = mAlbum!=null ? mAlbum.key:"";
        mAlbum = new Album(title, selectedUrls, key);
        Intent intent = new Intent(this, AlbumDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(IS_NEW_ALBUM_KEY,true);
        bundle.putParcelable(ALBUM_KEY,mAlbum);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void showSnackbar(int messageCode) {
        String message = getResources().getString(messageCode);
        Snackbar snackbar = Snackbar
                .make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG);

        snackbar.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
