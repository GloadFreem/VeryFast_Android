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
 * 项目详情中的各类报表适配器
 */
public class ProjectReportsAdapter extends RecyclerView.Adapter<ProjectReportsAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<Integer> imageViews;
    private List<String> names;
    private ItemClickListener mItemClickListener;

    public ProjectReportsAdapter(Context context, List<Integer> imageViews, List<String> names) {
        this.inflater = LayoutInflater.from(context);
        this.imageViews = imageViews;
        this.names = names;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView reportImg;
        public TextView rportName;

        public ViewHolder(final View itemView) {
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

    @Override
    public ProjectReportsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_project_report, parent, false);
        ProjectReportsAdapter.ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.reportImg = (ImageView) view.findViewById(R.id.project_iv_report);
        viewHolder.rportName = (TextView) view.findViewById(R.id.project_tv_report);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ProjectReportsAdapter.ViewHolder holder, int position) {
        holder.reportImg.setBackgroundResource(imageViews.get(position));
        holder.rportName.setText(names.get(position));
    }

    @Override
    public int getItemCount() {
        return imageViews.size();
    }

    public void setItemClickListener(ItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}
