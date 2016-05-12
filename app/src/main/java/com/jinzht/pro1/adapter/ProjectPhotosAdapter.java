package com.jinzht.pro1.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jinzht.pro1.R;
import com.jinzht.pro1.callback.ItemClickListener;

import java.util.List;

/**
 * 项目详情中的照片适配器
 */
public class ProjectPhotosAdapter extends RecyclerView.Adapter<ProjectPhotosAdapter.MViewHolder> {

    private LayoutInflater inflater;
    private List<Integer> imageViews;
    private ItemClickListener mItemClickListener;

    public ProjectPhotosAdapter(Context context, List<Integer> imageViews) {
        this.inflater = LayoutInflater.from(context);
        this.imageViews = imageViews;
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
        holder.image.setBackgroundResource(imageViews.get(position));
    }

    @Override
    public int getItemCount() {
        return imageViews.size();
    }

    public void setItemClickListener(ItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}
