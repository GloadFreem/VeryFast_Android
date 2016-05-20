package com.jinzht.pro1.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinzht.pro1.R;
import com.jinzht.pro1.callback.ItemClickListener;
import com.jinzht.pro1.view.CircleImageView;

import java.util.List;

/**
 * 交易账单数据填充器
 */
public class BillAdapter extends RecyclerView.Adapter<BillAdapter.MViewHolder> {

    private LayoutInflater inflater;
    private ItemClickListener mItemClickListener;
    private List<String> years;// 年
    private List<String> days;// 月日
    private List<String> mins;// 时分
    private List<String> types;// 交易类型
    private List<String> amounts;// 交易金额
    private List<String> nums;// 流水号
    private List<String> succeed;// 成功与否
    private List<Integer> imgs;// 项目图片
    private List<String> titles;// 项目标题

    public BillAdapter(Context context, List<String> years, List<String> days, List<String> mins, List<String> types, List<String> amounts, List<String> nums, List<String> succeed, List<Integer> imgs, List<String> titles) {
        this.inflater = LayoutInflater.from(context);
        this.years = years;
        this.days = days;
        this.mins = mins;
        this.types = types;
        this.amounts = amounts;
        this.nums = nums;
        this.succeed = succeed;
        this.imgs = imgs;
        this.titles = titles;
    }

    public class MViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout itemLlYear;// 年份布局
        public ImageView itemBillCalendar;// 日历图标
        public TextView itemBillYear;// 年
        public ImageView itemBillLine;// 蓝线
        public ImageView itemBillPoint;// 蓝点
        public TextView itemBillDay;// 月日
        public TextView itemBillMinute;// 时分
        public TextView itemBillType;// 交易类型
        public TextView itemBillAmount;// 金额
        public TextView itemBillNum;// 流水号
        public TextView itemBillSucceed;// 成功与否
        public CircleImageView itemBillImg;// 项目图片
        public TextView itemBillCompName;// 项目标题

        public MViewHolder(final View view) {
            super(view);

            itemLlYear = (LinearLayout) view.findViewById(R.id.item_ll_year);
            itemBillCalendar = (ImageView) view.findViewById(R.id.item_bill_calendar);
            itemBillYear = (TextView) view.findViewById(R.id.item_bill_year);
            itemBillLine = (ImageView) view.findViewById(R.id.item_bill_line);
            itemBillPoint = (ImageView) view.findViewById(R.id.item_bill_point);
            itemBillDay = (TextView) view.findViewById(R.id.item_bill_day);
            itemBillMinute = (TextView) view.findViewById(R.id.item_bill_minute);
            itemBillType = (TextView) view.findViewById(R.id.item_bill_type);
            itemBillAmount = (TextView) view.findViewById(R.id.item_bill_amount);
            itemBillNum = (TextView) view.findViewById(R.id.item_bill_num);
            itemBillSucceed = (TextView) view.findViewById(R.id.item_bill_succeed);
            itemBillImg = (CircleImageView) view.findViewById(R.id.item_bill_img);
            itemBillCompName = (TextView) view.findViewById(R.id.item_bill_comp_name);

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
        View view = inflater.inflate(R.layout.item_bill, parent, false);
        MViewHolder holder = new MViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 8;
    }

    public void setItemClickListener(ItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}
