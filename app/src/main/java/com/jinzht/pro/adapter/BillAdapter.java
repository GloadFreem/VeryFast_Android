package com.jinzht.pro.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinzht.pro.R;
import com.jinzht.pro.callback.ItemClickListener;
import com.jinzht.pro.utils.StringUtils;
import com.jinzht.pro.view.CircleImageView;

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

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;
    private View mHeaderView;

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
        public LinearLayout itemBillPro;// 项目图片和标题布局
        public CircleImageView itemBillImg;// 项目图片
        public TextView itemBillCompName;// 项目标题
        public TextView itemBillEndtag;// 结束标识
        public RelativeLayout itemBillCard;// 卡片背景

        public MViewHolder(final View view) {
            super(view);
            if (view == mHeaderView) {
                return;
            }
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
            itemBillPro = (LinearLayout) view.findViewById(R.id.item_bill_pro);
            itemBillImg = (CircleImageView) view.findViewById(R.id.item_bill_img);
            itemBillCompName = (TextView) view.findViewById(R.id.item_bill_comp_name);
            itemBillEndtag = (TextView) view.findViewById(R.id.item_bill_endtag);
            itemBillCard = (RelativeLayout) view.findViewById(R.id.item_bill_card);

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
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else {
            return TYPE_NORMAL;
        }
    }

    @Override
    public MViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_bill, parent, false);
        mHeaderView = inflater.inflate(R.layout.layout_empty_view_24dp, parent, false);
        MViewHolder holder = new MViewHolder(view);
        MViewHolder headerHolder = new MViewHolder(mHeaderView);
        if (viewType == TYPE_HEADER) {
            return headerHolder;
        } else {
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(MViewHolder holder, int position) {
        String str;// 转换字体的临时字符串
        SpannableString span;// 设置TextView不同字体

        if (getItemViewType(position) == TYPE_HEADER) {
            return;
        }
        if (StringUtils.isBlank(years.get(position))) {
            holder.itemLlYear.setVisibility(View.GONE);
        } else {
            holder.itemLlYear.setVisibility(View.VISIBLE);
            holder.itemBillYear.setText(years.get(position));
        }
        holder.itemBillDay.setText(days.get(position));
        holder.itemBillMinute.setText(mins.get(position));
        holder.itemBillType.setText(types.get(position));
        str = amounts.get(position);
        span = new SpannableString(str);
        span.setSpan(new AbsoluteSizeSpan(14, true), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new AbsoluteSizeSpan(14, true), str.length() - 1, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.itemBillAmount.setText(span);
        holder.itemBillNum.setText(nums.get(position));
        if (!succeed.get(position).contains("成功")) {
            holder.itemBillSucceed.setText(succeed.get(position));
            holder.itemBillSucceed.setTextColor(Color.RED);
        } else {
            holder.itemBillSucceed.setText(succeed.get(position));
            holder.itemBillSucceed.setTextColor(0xff747474);
        }
        if (StringUtils.isBlank(titles.get(position))) {
            holder.itemBillCard.setBackgroundResource(R.mipmap.icon_bill_frame);
            holder.itemBillPro.setVisibility(View.GONE);
            holder.itemBillLine.setBackgroundResource(R.mipmap.line_blue_short);
        } else {
            holder.itemBillCard.setBackgroundResource(R.mipmap.icon_bill_frame_big);
            holder.itemBillPro.setVisibility(View.VISIBLE);
            holder.itemBillLine.setBackgroundResource(R.mipmap.line_blue_long);
            holder.itemBillImg.setImageResource(imgs.get(position));
            holder.itemBillCompName.setText(titles.get(position));
        }
        if (position == getItemCount() - 1) {
            holder.itemBillEndtag.setVisibility(View.VISIBLE);
        } else {
            holder.itemBillEndtag.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return types.size();
    }

    public void setItemClickListener(ItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}
