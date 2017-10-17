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
 * Created by kenneth on 02/10/2017.
 */

public class ThumbnailRecyclerAdapter extends RecyclerView.Adapter<ThumbnailRecyclerAdapter.ThumbnailViewHolder> {
    private final Context mContext;
    private final ThumbnailAdapterOnClickHandler mClickHandler;
    private ArrayList<String> mImagesUrls;
    private ArrayList<Integer> mSelectedImagesIndices;

    public ThumbnailRecyclerAdapter(Context context, ThumbnailAdapterOnClickHandler clickHandler, ArrayList<String> urls) {
        mContext =context;
        mClickHandler=clickHandler;
        mImagesUrls = urls;
        mSelectedImagesIndices =new ArrayList<>();
    }
    public ThumbnailRecyclerAdapter(Context context, ThumbnailAdapterOnClickHandler clickHandler, ArrayList<String> urls, ArrayList<Integer> indices) {
        this(context,clickHandler,urls);
        mSelectedImagesIndices =indices;
    }

    public ArrayList<Integer> getSelectedIndices()
    {
        return mSelectedImagesIndices;
    }

    public ArrayList<String> getUrls()
    {
        return mImagesUrls;
    }

    public Album getAlbumObject(String title, String key)
    {
        return new Album(title, mImagesUrls, key);
    }

    public void setUrls(ArrayList<String> urls)
    {
        mImagesUrls =urls;
        notifyDataSetChanged();
    }

    public void removeUrl(int pos)
    {
        mImagesUrls.remove(pos);
        notifyDataSetChanged();
    }

    public void setSelectedIndex(int pos)
    {
        if (mSelectedImagesIndices.contains(pos)){
            Iterator<Integer> iterator = mSelectedImagesIndices.iterator();
            while(iterator.hasNext())
            {
                Integer positionToCompare = iterator.next();
                if (positionToCompare.equals(pos))
                {
                    iterator.remove();
                    break;
                }
            }
        }
        else {
            mSelectedImagesIndices.add(pos);
        }
        notifyDataSetChanged();
    }

    public ArrayList<String> getSelectedUrls( ) {
        ArrayList<String> listData=new ArrayList<>();
        for (int index: mSelectedImagesIndices){
            listData.add(mImagesUrls.get(index));
        }
        return listData;
    }

    @Override
    public ThumbnailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_thumbnail, parent, false);
        return new ThumbnailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ThumbnailViewHolder holder, int position) {
        String mThumbnailUrl= mImagesUrls.get(position);
        if (holder.ivThumbnailPicture != null) {
            Picasso.with(mContext).load(mThumbnailUrl).placeholder(R.drawable.ic_photo_black_24dp)
                    .into(holder.ivThumbnailPicture, new ImageLoadedCallback(holder.pbLoadingImage) {
                @Override
                public void onSuccess() {
                    if (this.progressBar != null) {
                        this.progressBar.setVisibility(View.GONE);
                    }
                }
            });
        }

        if( mSelectedImagesIndices.contains(position) )
        {
            holder.ivCheckIcon.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.ivCheckIcon.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return mImagesUrls.size();
    }

    public interface ThumbnailAdapterOnClickHandler {
        void onClickPicture(int position);
        void onClickRemovePicture(int position);
    }

    class ThumbnailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        public final ImageView ivThumbnailPicture;
        public final ImageView ivRemoveIcon;
        public final ImageView ivCheckIcon;
        public final ProgressBar pbLoadingImage;

        public ThumbnailViewHolder(View itemView) {
            super(itemView);
            ivThumbnailPicture = itemView.findViewById(R.id.iv_thumbnail);
            ivRemoveIcon = itemView.findViewById(R.id.iv_remove);
            ivCheckIcon = itemView.findViewById(R.id.iv_check);
            pbLoadingImage= itemView.findViewById(R.id.pb_loading_image);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();

            ivRemoveIcon.setVisibility(View.INVISIBLE);
            mClickHandler.onClickPicture(position);
        }

        @Override
        public boolean onLongClick(View view) {
            final int position = getAdapterPosition();

            ivRemoveIcon.setVisibility(View.VISIBLE);
            ivRemoveIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ivRemoveIcon.setVisibility(View.INVISIBLE);
                    mClickHandler.onClickRemovePicture(position);
                }
            });

            return true;
        }
    }
}
