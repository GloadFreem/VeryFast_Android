package com.jinzht.pro1.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jinzht.pro1.R;
import com.jinzht.pro1.callback.ItemClickListener;

import java.util.List;

/**
 * 项目详情中的团队成员适配器
 */
public class ProjectTeamsAdapter extends RecyclerView.Adapter<ProjectTeamsAdapter.MViewHolder> {

    private LayoutInflater inflater;
    private List<Integer> imageViews;
    private List<String> names;
    private List<String> positions;
    private ItemClickListener mItemClickListener;

    public ProjectTeamsAdapter(Context context, List<Integer> imageViews, List<String> names, List<String> positions) {
        this.inflater = LayoutInflater.from(context);
        this.imageViews = imageViews;
        this.names = names;
        this.positions = positions;
    }

    public class MViewHolder extends RecyclerView.ViewHolder {

        public ImageView image;
        public TextView name;
        public TextView position;

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
        View view = inflater.inflate(R.layout.item_project_team, parent, false);
        MViewHolder viewHolder = new MViewHolder(view);
        viewHolder.image = (ImageView) view.findViewById(R.id.project_iv_favicon);
        viewHolder.name = (TextView) view.findViewById(R.id.project_tv_name);
        viewHolder.position = (TextView) view.findViewById(R.id.project_tv_position);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MViewHolder holder, int position) {
        holder.image.setBackgroundResource(imageViews.get(position));
        holder.name.setText(names.get(position));
        holder.position.setText(positions.get(position));
    }

    @Override
    public int getItemCount() {
        return imageViews.size();
    }

    public void setItemClickListener(ItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}
