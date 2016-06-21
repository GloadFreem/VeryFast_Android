package com.jinzht.pro.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jinzht.pro.R;
import com.jinzht.pro.bean.ActivityDetailBean;
import com.jinzht.pro.callback.ItemClickListener;

import java.util.List;

/**
 * 活动页面中照片适配器
 */
public class ActivityPhotosAdapter extends RecyclerView.Adapter<ActivityPhotosAdapter.MViewHolder> {

    private LayoutInflater mInflater;
    private List<ActivityDetailBean.DataBean.ActionimagesBean> photos;
    private ItemClickListener mItemClickListener;
    private Context context;

    public ActivityPhotosAdapter(Context context, List<ActivityDetailBean.DataBean.ActionimagesBean> photos) {
        this.mInflater = LayoutInflater.from(context);
        this.photos = photos;
        this.context = context;
    }

    @Override
    public MViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_activity_photos, parent, false);
        MViewHolder mViewHolder = new MViewHolder(view);
        mViewHolder.img = (ImageView) view.findViewById(R.id.activity_iv_photo);
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(MViewHolder holder, int position) {
        Glide.with(context).load(photos.get(position).getUrl()).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    public void setItemClickListener(ItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public class MViewHolder extends RecyclerView.ViewHolder {

        public ImageView img;

        public MViewHolder(final View itemView) {
            super(itemView);
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
}
