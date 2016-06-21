package com.jinzht.pro.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jinzht.pro.R;
import com.jinzht.pro.callback.ItemClickListener;

import java.util.List;

/**
 * 资产账户条目适配器
 */
public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.MViewHolder> {

    private LayoutInflater inflater;
    private List<Integer> icons;
    private List<String> names;
    private ItemClickListener mItemClickListener;
    private Context context;

    public AccountAdapter(Context context, List<Integer> icons, List<String> names) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.icons = icons;
        this.names = names;
    }

    public class MViewHolder extends RecyclerView.ViewHolder {

        public ImageView icons;
        public TextView items;

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
        View view = inflater.inflate(R.layout.item_account_gv, parent, false);
        MViewHolder holder = new MViewHolder(view);
        holder.icons = (ImageView) view.findViewById(R.id.item_account_icon);
        holder.items = (TextView) view.findViewById(R.id.item_account_name);
        return holder;
    }

    @Override
    public void onBindViewHolder(MViewHolder holder, int position) {
        holder.icons.setBackgroundResource(icons.get(position));
        holder.items.setText(names.get(position));
    }

    @Override
    public int getItemCount() {
        return icons.size();
    }

    public void setItemClickListener(ItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}
