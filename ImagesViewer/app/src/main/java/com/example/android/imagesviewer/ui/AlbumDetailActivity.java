package com.example.android.imagesviewer.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.android.imagesviewer.R;
import com.example.android.imagesviewer.adapter.ThumbnailRecyclerAdapter;
import com.example.android.imagesviewer.object.Album;
import com.example.android.imagesviewer.utils.FirebaseInstance;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import static com.example.android.imagesviewer.utils.Constants.ALBUMS_DATABASE_REFERENCE_KEY;
import static com.example.android.imagesviewer.utils.Constants.ALBUM_KEY;
import static com.example.android.imagesviewer.utils.Constants.INDEX_SELECTED_IMAGE_KEY;
import static com.example.android.imagesviewer.utils.Constants.IS_NEW_ALBUM_KEY;
import static com.example.android.imagesviewer.utils.ViewFunctions.numberOfColumns;

public class AlbumDetailActivity extends AppCompatActivity implements ThumbnailRecyclerAdapter.ThumbnailAdapterOnClickHandler {

    private ThumbnailRecyclerAdapter mThumbnailRecyclerAdapter;
    private Album mAlbum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_detail);

        RecyclerView rvImages = findViewById(R.id.rv_images);
        Button btnSave = findViewById(R.id.b_save);
        FloatingActionButton fabEdit = findViewById(R.id.fab_edit_album);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Boolean isNewAlbum = bundle.getBoolean(IS_NEW_ALBUM_KEY);
            if (isNewAlbum) {
                btnSave.setVisibility(View.VISIBLE);
            } else {
                fabEdit.setVisibility(View.VISIBLE);
            }

            mAlbum = bundle.getParcelable(ALBUM_KEY);
            assert mAlbum != null;
            ActionBar actionBar = getSupportActionBar();
            assert actionBar != null;
            actionBar.setTitle(mAlbum.title);

            int numColumns = numberOfColumns(this);
            StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(numColumns, StaggeredGridLayoutManager.VERTICAL);
            mThumbnailRecyclerAdapter = new ThumbnailRecyclerAdapter(this, this, mAlbum.getUrls());
            rvImages.setAdapter(mThumbnailRecyclerAdapter);
            rvImages.setLayoutManager(layoutManager);

        }
    }

    public void onClickSave(View view) {
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        String title = actionBar.getTitle().toString();
        Album album = mThumbnailRecyclerAdapter.getAlbumObject(title, "");
        Boolean isNew = TextUtils.isEmpty(mAlbum != null ? mAlbum.key : "");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            DatabaseReference reference = FirebaseInstance.getDatabase().getReference(ALBUMS_DATABASE_REFERENCE_KEY);
            if (isNew) {
                reference.child(user.getUid()).push().setValue(album);
            } else {
                reference.child(user.getUid()).child(mAlbum.key).setValue(album);
            }
        }

        startActivity(new Intent(this, UserAlbumsActivity.class));
    }

    public void onClickEditAlbum(View view) {
        Intent intent = new Intent(this, AlbumEditionActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(ALBUM_KEY, mAlbum);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onClickPicture(int position) {
        Intent intent = new Intent(AlbumDetailActivity.this, ImageViewerActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(ALBUM_KEY, mAlbum);
        bundle.putInt(INDEX_SELECTED_IMAGE_KEY, position);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onClickRemovePicture(int position) {
        Boolean isNew = TextUtils.isEmpty(mAlbum != null ? mAlbum.key : "");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            DatabaseReference reference = FirebaseInstance.getDatabase().getReference(ALBUMS_DATABASE_REFERENCE_KEY);
            mThumbnailRecyclerAdapter.removeUrl(position);
            if (!isNew) {
                ActionBar actionBar = getSupportActionBar();
                assert actionBar != null;
                String title = actionBar.getTitle().toString();
                Album album = mThumbnailRecyclerAdapter.getAlbumObject(title, mAlbum.key);
                reference.child(user.getUid()).child(mAlbum.key).setValue(album);
                mAlbum = album;
            }
        }
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
