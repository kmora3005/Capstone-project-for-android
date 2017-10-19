package com.example.android.imagesviewer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.android.imagesviewer.R;
import com.example.android.imagesviewer.object.Album;
import com.example.android.imagesviewer.utils.ImageLoadedCallback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Project name ImagesViewer
 * Created by kenneth on 08/10/2017.
 */

public class AlbumRecyclerAdapter extends RecyclerView.Adapter<AlbumRecyclerAdapter.AlbumViewHolder> {
    private final Context mContext;
    private final AlbumAdapterOnClickHandler mClickHandler;
    private ArrayList<Album> albums = new ArrayList<>();

    public AlbumRecyclerAdapter(Context context, AlbumAdapterOnClickHandler clickHandler) {

        mContext = context;
        mClickHandler = clickHandler;
        albums = new ArrayList<>();
    }

    /*@Override
    protected void populateViewHolder(AlbumViewHolder viewHolder, Album model, int position) {
        String mThumbnailUrl= model.images.get(0).url;
        if (viewHolder.ivThumbnailAlbum != null) {
            Glide.with(context).load(mThumbnailUrl).into(viewHolder.ivThumbnailAlbum);
        }
    }*/

    @Override
    public AlbumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_thumbnail, parent, false);
        return new AlbumViewHolder(view);
    }


    @Override
    public void onBindViewHolder(AlbumViewHolder holder, int position) {
        Album album = albums.get(position);
        String mThumbnailUrl = album.images.get(0).url;
        if (holder.ivThumbnailAlbum != null) {
            Picasso.with(mContext).load(mThumbnailUrl).placeholder(R.drawable.ic_photo_black_24dp)
                    .into(holder.ivThumbnailAlbum, new ImageLoadedCallback(holder.pbLoadingImage) {
                        @Override
                        public void onSuccess() {
                            if (this.progressBar != null) {
                                this.progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
        }
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }

    public void addAlbum(Album album) {
        this.albums.add(album);
        notifyDataSetChanged();
    }

    public void removeAlbum(Album album) {
        Iterator<Album> iterator = this.albums.iterator();
        while (iterator.hasNext()) {
            Album albumToCompare = iterator.next();
            if (album.key.equals(albumToCompare.key)) {
                iterator.remove();
                break;
            }
        }
        notifyDataSetChanged();
    }

    public interface AlbumAdapterOnClickHandler {
        void onClickAlbum(Album album);

        void onClickRemoveAlbum(Album album);
    }

    class AlbumViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public final ImageView ivThumbnailAlbum;
        public final ImageView ivRemoveIcon;
        public final ProgressBar pbLoadingImage;

        public AlbumViewHolder(View itemView) {
            super(itemView);
            ivThumbnailAlbum = itemView.findViewById(R.id.iv_thumbnail);
            ivRemoveIcon = itemView.findViewById(R.id.iv_remove);
            pbLoadingImage = itemView.findViewById(R.id.pb_loading_image);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Album album = albums.get(position);

            ivRemoveIcon.setVisibility(View.INVISIBLE);
            mClickHandler.onClickAlbum(album);
        }

        @Override
        public boolean onLongClick(View view) {
            int position = getAdapterPosition();
            final Album album = albums.get(position);

            ivRemoveIcon.setVisibility(View.VISIBLE);
            ivRemoveIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mClickHandler.onClickRemoveAlbum(album);
                }
            });

            return true;
        }
    }
}
