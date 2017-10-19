package com.example.android.imagesviewer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.android.imagesviewer.R;
import com.example.android.imagesviewer.adapter.AlbumRecyclerAdapter;
import com.example.android.imagesviewer.object.Album;
import com.example.android.imagesviewer.utils.FirebaseInstance;
import com.example.android.imagesviewer.widget.AlbumsIntentService;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import static com.example.android.imagesviewer.utils.Constants.ALBUMS_DATABASE_REFERENCE_KEY;
import static com.example.android.imagesviewer.utils.Constants.ALBUM_KEY;
import static com.example.android.imagesviewer.utils.Constants.IS_NEW_ALBUM_KEY;
import static com.example.android.imagesviewer.utils.ViewFunctions.numberOfColumns;

public class UserAlbumsActivity extends AppCompatActivity implements AlbumRecyclerAdapter.AlbumAdapterOnClickHandler {
    private static final String TAG = UserAlbumsActivity.class.getSimpleName();

    private RecyclerView mAlbumRecyclerView;
    private StaggeredGridLayoutManager mLayoutManager;
    private AlbumRecyclerAdapter mAlbumAdapter;
    private TextView mNoAlbumsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_albums);

        mNoAlbumsTextView = findViewById(R.id.tv_no_albums_message);
        mAlbumRecyclerView = findViewById(R.id.rv_albums);
        mAlbumRecyclerView.setHasFixedSize(true);
        int numColumns = numberOfColumns(this);
        mLayoutManager = new StaggeredGridLayoutManager(numColumns,StaggeredGridLayoutManager.VERTICAL);

        updateFromFirebase();
        AlbumsIntentService.startActionUpdateAlbumsWidgets(UserAlbumsActivity.this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        updateFromFirebase();
    }

    private void updateFromFirebase() {
        mAlbumAdapter = new AlbumRecyclerAdapter(this, this);
        mAlbumRecyclerView.setLayoutManager(mLayoutManager);
        mAlbumRecyclerView.setAdapter(mAlbumAdapter);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            DatabaseReference reference = FirebaseInstance.getDatabase().getReference(ALBUMS_DATABASE_REFERENCE_KEY);
            DatabaseReference childRef = reference.child(user.getUid());

            Query query = childRef.limitToLast(50);
            ChildEventListener childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                    Album album = dataSnapshot.getValue(Album.class);
                    if (album != null) {
                        album.key = dataSnapshot.getKey();
                        mAlbumAdapter.addAlbum(album);
                        mNoAlbumsTextView.setVisibility(View.INVISIBLE);
                        Log.d(TAG, "Album added: " + album.title);
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                    Album album = dataSnapshot.getValue(Album.class);
                    if (album != null) {
                        Log.d(TAG, "Album changed: " + album.title);
                    }
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Album album = dataSnapshot.getValue(Album.class);
                    if (album != null) {
                        album.key = dataSnapshot.getKey();
                        mAlbumAdapter.removeAlbum(album);
                        Log.d(TAG, "Album removed: " + album.title);
                    }
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                    Album album = dataSnapshot.getValue(Album.class);
                    if (album != null) {
                        Log.d(TAG, "Album moved: " + album.title);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w(TAG, "Failed to read value.", databaseError.toException());
                }
            };
            query.addChildEventListener(childEventListener);
        }
    }

    @Override
    public void onBackPressed() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        // user is now signed out
                        startActivity(new Intent(UserAlbumsActivity.this, MainActivity.class));
                        finish();
                    }
                });
    }

    public void onClickNewAlbum(View view) {
        startActivity(new Intent(this, AlbumEditionActivity.class));
    }

    @Override
    public void onClickAlbum(Album album) {
        Intent intent = new Intent(this, AlbumDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(ALBUM_KEY, album);
        bundle.putBoolean(IS_NEW_ALBUM_KEY, false);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onClickRemoveAlbum(Album album) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            DatabaseReference reference = FirebaseInstance.getDatabase().getReference(ALBUMS_DATABASE_REFERENCE_KEY);
            reference.child(user.getUid()).child(album.key).removeValue();
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
