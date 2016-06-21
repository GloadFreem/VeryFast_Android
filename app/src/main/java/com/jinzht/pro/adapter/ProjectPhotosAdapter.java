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
 * 项目详情中的照片适配器
 */
public class ProjectPhotosAdapter extends RecyclerView.Adapter<ProjectPhotosAdapter.MViewHolder> {

    private LayoutInflater inflater;
    private List<String> images;
    private ItemClickListener mItemClickListener;
    private Context context;

    public ProjectPhotosAdapter(Context context, List<String> images) {
        this.inflater = LayoutInflater.from(context);
        this.images = images;
        this.context = context;
    }

    public class MViewHolder extends RecyclerView.ViewHolder {

        public ImageView image;

        public MViewHolder(final View view) {
            super(view);
            // 为item添加点击事件回调
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemClickListener != null) {
                        mItemClickListener.onItemClick(view, getPosition());
                    }
                }
            });
        }
    }

    @Override
    public MViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_project_photos, parent, false);
        MViewHolder holder = new MViewHolder(view);
        holder.image = (ImageView) view.findViewById(R.id.project_iv_photo);
        return holder;
    }

    @Override
    public void onBindViewHolder(MViewHolder holder, int position) {
        Glide.with(context).load(images.get(position)).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public void setItemClickListener(ItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}
