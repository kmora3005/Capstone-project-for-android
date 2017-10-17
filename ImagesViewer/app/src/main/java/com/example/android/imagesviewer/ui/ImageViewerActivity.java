package com.example.android.imagesviewer.ui;

import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.android.imagesviewer.utils.ImageLoadedCallback;
import com.example.android.imagesviewer.utils.OnSwipeTouchListener;
import com.example.android.imagesviewer.R;
import com.example.android.imagesviewer.object.Album;
import com.squareup.picasso.Picasso;

import java.util.Timer;
import java.util.TimerTask;

import static com.example.android.imagesviewer.utils.Constants.ALBUM_KEY;
import static com.example.android.imagesviewer.utils.Constants.INDEX_SELECTED_IMAGE_KEY;

public class ImageViewerActivity extends AppCompatActivity {
    private static final String INDEX_CURRENT_IMAGE_KEY = "index_current_image_key";

    private Album mAlbum;
    private int mIndex;
    private ImageView mPicture;
    private ProgressBar mLoadingImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);

        mPicture = findViewById(R.id.iv_picture);
        mLoadingImage = findViewById(R.id.pb_loading_image);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mIndex = savedInstanceState != null ? savedInstanceState.getInt(INDEX_CURRENT_IMAGE_KEY) :
                    bundle.getInt(INDEX_SELECTED_IMAGE_KEY);
            mAlbum = bundle.getParcelable(ALBUM_KEY);
            assert mAlbum != null;
            final String url =mAlbum.getUrls().get(mIndex);
            loadImage(url);

            mPicture.setOnTouchListener(new OnSwipeTouchListener(ImageViewerActivity.this) {

                public void onSwipeRight() {
                    if (mIndex > 0) {
                        mIndex--;
                    }

                    loadImage(url);
                }

                public void onSwipeLeft() {
                    if (mIndex < mAlbum.getUrls().size() - 1) {
                        mIndex++;
                    }

                    loadImage(url);
                }

            });
        }
        configureChangeAutomaticInImages();

    }

    private void configureChangeAutomaticInImages(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String automaticViewerKey=getResources().getString(R.string.pref_enable_automatic_viewer_key);
        boolean enableAutomaticViewer = sp.getBoolean(automaticViewerKey, false);

        if (enableAutomaticViewer){
            String restartKey=getResources().getString(R.string.pref_restart_viewer_automatically_key);
            final boolean restartViewerAutomatically = sp.getBoolean(restartKey, false);

            String secondsToChangeKey=getResources().getString(R.string.pref_seconds_to_change_image_automatically_key);
            String secondsByDefault= getResources().getString(R.string.pref_seconds_to_change_image_automatically_default);
            String secondsToChange = sp.getString(secondsToChangeKey, secondsByDefault);

            final Handler handler = new Handler();
            Timer timer = new Timer(false);
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (mIndex < mAlbum.getUrls().size() - 1) {
                                mIndex++;
                            }
                            else {
                                if (restartViewerAutomatically){
                                    mIndex=0;
                                }
                            }
                            String url =mAlbum.getUrls().get(mIndex);
                            loadImage(url);
                        }
                    });
                }
            };

            int timeInMilliseconds=Integer.valueOf(secondsToChange)*1000;
            timer.schedule(timerTask, timeInMilliseconds, timeInMilliseconds);
        }

    }

    private void loadImage(String url){
        Picasso.with(ImageViewerActivity.this).load(url).placeholder(R.drawable.ic_photo_black_24dp)
                .into(mPicture, new ImageLoadedCallback(mLoadingImage) {
            @Override
            public void onSuccess() {
                if (this.progressBar != null) {
                    this.progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(INDEX_CURRENT_IMAGE_KEY, mIndex);
    }
}
