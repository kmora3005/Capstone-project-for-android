package com.example.android.imagesviewer.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.imagesviewer.R;
import com.example.android.imagesviewer.object.Album;
import com.example.android.imagesviewer.utils.FirebaseInstance;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import static com.example.android.imagesviewer.utils.Constants.ALBUMS_DATABASE_REFERENCE_KEY;
import static com.example.android.imagesviewer.utils.Constants.ALBUM_KEY;
import static com.example.android.imagesviewer.utils.Constants.IS_NEW_ALBUM_KEY;

/**
 * Project name ImagesViewer
 * Created by kenneth on 15/10/2017.
 */

public class AlbumsGridWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GridRemoteViewsFactory(this.getApplicationContext());
    }
}

class GridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private static final String TAG = GridRemoteViewsFactory.class.getSimpleName();
    private final Context mContext;
    private ArrayList<Album> mAlbums = new ArrayList<>();
    private CountDownLatch mCountDownLatch;

    public GridRemoteViewsFactory(Context applicationContext) {
        mContext = applicationContext;
    }

    private void populateGrid() {
        mAlbums = new ArrayList<>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            DatabaseReference reference = FirebaseInstance.getDatabase().getReference(ALBUMS_DATABASE_REFERENCE_KEY);
            DatabaseReference childRef = reference.child(user.getUid());
            childRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Album album = ds.getValue(Album.class);
                        if (album != null) {
                            album.key = ds.getKey();
                            mAlbums.add(album);
                            Log.d(TAG, "Album added: " + album.title);
                        }

                    }
                    if (mCountDownLatch.getCount() == 0) {
                        Intent updateWidgetIntent = new Intent(mContext, AlbumAppWidget.class);
                        updateWidgetIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                        mContext.sendBroadcast(updateWidgetIntent);
                    } else {
                        mCountDownLatch.countDown();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            });

        }
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        mCountDownLatch = new CountDownLatch(1);
        populateGrid();
        try {
            mCountDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mAlbums.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.item_album_widget);
        Album album = mAlbums.get(i);
        Uri uri = Uri.parse(album.getUrls().get(0));
        views.setImageViewUri(R.id.iv_widget_thumbnail, uri);
        views.setTextViewText(R.id.tv_album_name, album.title);

        Bundle bundle = new Bundle();
        bundle.putParcelable(ALBUM_KEY, album);
        bundle.putBoolean(IS_NEW_ALBUM_KEY, false);

        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(bundle);
        views.setOnClickFillInIntent(R.id.tv_album_name, fillInIntent);

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}