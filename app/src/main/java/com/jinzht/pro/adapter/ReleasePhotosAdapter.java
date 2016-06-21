package com.jinzht.pro.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jinzht.pro.R;
import com.jinzht.pro.callback.ItemClickListener;

import java.util.List;

/**
 * 在朋友圈发布话题时的照片填充器
 */
public class ReleasePhotosAdapter extends RecyclerView.Adapter<ReleasePhotosAdapter.MViewHolder> {

    private LayoutInflater mInflater;
    private ItemClickListener mItemClickListener;
    private List<String> photos;
    private Activity activity;

    public static final int TYPE_ADD = 0;
    public static final int TYPE_IMG = 1;
    private View addView;
    private View imgView;
    private MViewHolder addHold;
    private MViewHolder imgHold;

    public ReleasePhotosAdapter(Activity activity, List<String> photos) {
        this.mInflater = LayoutInflater.from(activity);
        this.photos = photos;
        this.activity = activity;
    }

    @Override
    public int getItemCount() {
        if (photos.size() < 9) {
            return photos.size() + 1;
        } else {
            return 9;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (photos.size() == 9) {
            return TYPE_IMG;
        } else if (photos.size() == 0) {
            return TYPE_ADD;
        } else {
            if (position == photos.size()) {
                return TYPE_ADD;
            } else {
                return TYPE_IMG;
            }
        }
    }

    @Override
    public MViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        addView = mInflater.inflate(R.layout.item_release_topic_add, parent, false);
        imgView = mInflater.inflate(R.layout.item_release_topic_photo, parent, false);
        addHold = new MViewHolder(addView);
        imgHold = new MViewHolder(imgView);
        if (viewType == TYPE_ADD) {
            return addHold;
        } else {
            return imgHold;
        }
    }

    @Override
    public void onBindViewHolder(MViewHolder holder, int position) {
        if (holder == imgHold) {
            holder.img = (ImageView) imgView.findViewById(R.id.img);
            Glide.with(activity).load(photos.get(position)).into(holder.img);
        }
    }

    public class MViewHolder extends RecyclerView.ViewHolder {

        public ImageView img;

        public MViewHolder(final View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemClickListener != null) {
                        mItemClickListener.onItemClick(itemView, getPosition());
                    }
                }
            });
        }
    }

    public void setItemClickListener(ItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}
