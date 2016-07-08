package com.jinzht.pro.adapter;

import android.content.Context;
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
 * 圈子列表中条目的照片适配器
 */
public class CirclePhotosAdapter extends RecyclerView.Adapter<CirclePhotosAdapter.MViewHolder> {

    private LayoutInflater mInflater;
    private List<String> photos;
    private ItemClickListener mItemClickListener;
    private Context context;

    public CirclePhotosAdapter(Context context, List<String> photos) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.photos = photos;
    }

    @Override
    public MViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_circle_photo, parent, false);
        MViewHolder holder = new MViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MViewHolder holder, int position) {
        Glide.with(context).load(photos.get(position)).placeholder(R.mipmap.ic_default).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    public class MViewHolder extends RecyclerView.ViewHolder {

        public ImageView img;

        public MViewHolder(final View itemView) {
            super(itemView);
            this.img = (ImageView) itemView.findViewById(R.id.item_circle_photo);
            // 为item添加点击事件回调
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
