package com.jinzht.pro.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jinzht.pro.R;
import com.jinzht.pro.base.BaseActivity;
import com.jinzht.pro.bean.ProCenter4InvestorBean;
import com.jinzht.pro.bean.ProCenter4ProBean;
import com.jinzht.pro.utils.AESUtils;
import com.jinzht.pro.utils.Constant;
import com.jinzht.pro.utils.FastJsonTools;
import com.jinzht.pro.utils.MD5Utils;
import com.jinzht.pro.utils.NetWorkUtils;
import com.jinzht.pro.utils.OkHttpUtils;
import com.jinzht.pro.utils.SuperToastUtils;
import com.jinzht.pro.utils.UiHelp;
import com.jinzht.pro.view.CircleImageView;
import com.jinzht.pro.view.PullToRefreshLayout;
import com.jinzht.pro.view.PullableListView;
import com.jinzht.pro.view.RoundProgressBar;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目中心界面
 */
public class ProCenterActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout btnBack;// 返回
    private TextView title;// 标题
    private PullToRefreshLayout refreshView;// 刷新布局
    private PullableListView listview;// 列表

    private int usertype;// 用户类型，1项目方，2投资人，3投资机构，4智囊团

    private ProAdapter proAdapter;// 项目方的数据列表填充器
    private InvestorAdapter investorAdapter;// 投资人的数据列表填充器
    private BrainAdapter brainAdapter;// 智囊团的数据列表填充器
    private int pages = 0;
    private List<ProCenter4ProBean.DataBean> proDatas = new ArrayList<>();// 项目方上传的项目
    private List<ProCenter4InvestorBean.DataBean.InvestBean> investorInvestDatas = new ArrayList<>();// 投资人和投资机构投资的项目
    private List<ProCenter4InvestorBean.DataBean.CommitBean> investorCommitDatas = new ArrayList<>();// 投资人和投资机构收到的项目

    @Override
    protected int getResourcesId() {
        return R.layout.activity_pro_center;
    }

    @Override
    protected void init() {
        UiHelp.setSameStatus(true, this);// 设置系统状态栏与应用标题栏背景一致

        btnBack = (LinearLayout) findViewById(R.id.btn_back);// 返回
        btnBack.setOnClickListener(this);
        title = (TextView) findViewById(R.id.tv_title);// 标题
        title.setText("项目中心");
        refreshView = (PullToRefreshLayout) findViewById(R.id.refresh_view);// 刷新布局
        listview = (PullableListView) findViewById(R.id.listview);// 列表

        usertype = getIntent().getIntExtra("usertype", -1);// 项目类型

        refreshView.setOnRefreshListener(new PullListener());
        proAdapter = new ProAdapter();// 项目方的数据列表填充器
        investorAdapter = new InvestorAdapter();// 投资人的数据列表填充器
        brainAdapter = new BrainAdapter();// 智囊团的数据列表填充器
        listview.addHeaderView(LayoutInflater.from(mContext).inflate(R.layout.layout_empty_view_9dp, null), null, false);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (usertype) {
                    case 1:// 项目方
                        if (position == proAdapter.getCount()) {
                            Intent intent = new Intent(mContext, UploadActivity.class);
                            startActivity(intent);
                        } else {
                            if ("预选项目".equals(proDatas.get(position - 1).getFinancestatus().getName())) {
                                Intent intent = new Intent(mContext, PreselectionDetailsActivity.class);
                                intent.putExtra("id", String.valueOf(proDatas.get(position - 1).getProjectId()));
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(mContext, RoadshowDetailsActivity.class);
                                intent.putExtra("id", String.valueOf(proDatas.get(position - 1).getProjectId()));
                                startActivity(intent);
                            }
                        }
                        break;
                    case 2:// 投资人
                        break;
                    case 3:// 投资机构
                        break;
                    case 4:// 智囊团
                        break;
                    default:
                        break;
                }
            }
        });
//        switch (usertype) {
//            case 1:// 项目方
//                GetProject1List getProject1List = new GetProject1List(0);
//                getProject1List.execute();
//                break;
//            case 2:// 投资人
//                GetProject2List getProject2List = new GetProject2List(0);
//                getProject2List.execute();
//                break;
//            case 3:// 投资机构
//                break;
//            case 4:// 智囊团
//                break;
//            default:
//                break;
//        }

        GetProject2List getProject2List = new GetProject2List(0);
        getProject2List.execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:// 返回上一页
                finish();
                break;
        }
    }

    // 项目方的数据列表填充器
    private class ProAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return proDatas.size() + 1;
        }

        @Override
        public Object getItem(int position) {
            return proDatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getViewTypeCount() {
            return 3;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == getCount() - 1) {
                return 0;// 添加项目
            } else if ("预选项目".equals(proDatas.get(position).getFinancestatus().getName())) {
                return 2;// 预选项目
            } else {
                return 1;// 路演项目
            }
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                if (getItemViewType(position) == 0) {// 添加项目
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.item_pro_center_for_pro_add, null);
                } else if (getItemViewType(position) == 1) {// 路演项目
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.item_pro_center_for_pro_roadshow, null);
                    holder.itemProjectImg = (CircleImageView) convertView.findViewById(R.id.item_project_img);
                    holder.itemProjectTitle = (TextView) convertView.findViewById(R.id.item_project_title);
                    holder.itemProjectAddr = (TextView) convertView.findViewById(R.id.item_project_addr);
                    holder.itemProjectTag = (ImageView) convertView.findViewById(R.id.item_project_tag);
                    holder.itemProjectCompname = (TextView) convertView.findViewById(R.id.item_project_compname);
                    holder.itemProjectField1 = (TextView) convertView.findViewById(R.id.item_project_field1);
                    holder.itemProjectField2 = (TextView) convertView.findViewById(R.id.item_project_field2);
                    holder.itemProjectField3 = (TextView) convertView.findViewById(R.id.item_project_field3);
                    holder.itemProjectPopularity = (TextView) convertView.findViewById(R.id.item_project_popularity);
                    holder.itemProjectTime = (TextView) convertView.findViewById(R.id.item_project_time);
                    holder.itemProjectAmount = (TextView) convertView.findViewById(R.id.item_project_amount);
                    holder.itemProjectProgress = (RoundProgressBar) convertView.findViewById(R.id.item_project_progress);
                    holder.itemBtnRecord = (ImageView) convertView.findViewById(R.id.item_btn_record);
                    holder.itemBtnDetail = (ImageView) convertView.findViewById(R.id.item_btn_detail);
                } else {// 预选项目
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.item_pro_center_for_pro_preselection, null);
                    holder.itemProjectImg = (CircleImageView) convertView.findViewById(R.id.item_project_img);
                    holder.itemProjectTitle = (TextView) convertView.findViewById(R.id.item_project_title);
                    holder.itemProjectAddr = (TextView) convertView.findViewById(R.id.item_project_addr);
                    holder.itemProjectCompname = (TextView) convertView.findViewById(R.id.item_project_compname);
                    holder.itemProjectField1 = (TextView) convertView.findViewById(R.id.item_project_field1);
                    holder.itemProjectField2 = (TextView) convertView.findViewById(R.id.item_project_field2);
                    holder.itemProjectField3 = (TextView) convertView.findViewById(R.id.item_project_field3);
                    holder.itemProjectPopularity = (TextView) convertView.findViewById(R.id.item_project_popularity);
                    holder.itemProjectAmount = (TextView) convertView.findViewById(R.id.item_project_amount);
                    holder.itemBtnRecord = (ImageView) convertView.findViewById(R.id.item_btn_record);
                    holder.itemBtnDetail = (ImageView) convertView.findViewById(R.id.item_btn_detail);
                }
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            // 路演项目
            if (getItemViewType(position) == 1) {
                Glide.with(mContext).load(proDatas.get(position).getStartPageImage()).into(holder.itemProjectImg);
                holder.itemProjectTitle.setText(proDatas.get(position).getAbbrevName());
                holder.itemProjectAddr.setText(proDatas.get(position).getAddress());
                switch (proDatas.get(position).getFinancestatus().getName()) {
                    case "待路演":
                        holder.itemProjectTag.setImageResource(R.mipmap.tag_dailuyan);
                        break;
                    case "融资中":
                        holder.itemProjectTag.setImageResource(R.mipmap.tag_rongzizhong);
                        break;
                    case "融资成功":
                        holder.itemProjectTag.setImageResource(R.mipmap.tag_rongziwancheng);
                        break;
                    case "融资失败":
                        holder.itemProjectTag.setImageResource(R.mipmap.tag_rongzishibai);
                        break;
                }
                holder.itemProjectCompname.setText(proDatas.get(position).getFullName());
                String[] fields = proDatas.get(position).getIndustoryType().split("，");
                if (fields.length == 0) {
                    holder.itemProjectField1.setVisibility(View.INVISIBLE);
                    holder.itemProjectField2.setVisibility(View.INVISIBLE);
                    holder.itemProjectField3.setVisibility(View.INVISIBLE);
                } else if (fields.length == 1) {
                    holder.itemProjectField1.setText(fields[0]);
                    holder.itemProjectField2.setVisibility(View.INVISIBLE);
                    holder.itemProjectField3.setVisibility(View.INVISIBLE);
                } else if (fields.length == 2) {
                    holder.itemProjectField1.setText(fields[0]);
                    holder.itemProjectField2.setText(fields[1]);
                    holder.itemProjectField3.setVisibility(View.INVISIBLE);
                } else if (fields.length == 3) {
                    holder.itemProjectField1.setText(fields[0]);
                    holder.itemProjectField2.setText(fields[1]);
                    holder.itemProjectField3.setText(fields[2]);
                }
                holder.itemProjectPopularity.setText(String.valueOf(proDatas.get(position).getCollectionCount()));
                holder.itemProjectTime.setText(String.valueOf(proDatas.get(position).getTimeLeft()));
                holder.itemProjectAmount.setText(proDatas.get(position).getRoadshows().get(0).getRoadshowplan().getFinanceTotal() + "万");
                int progress = (int) ((double) (proDatas.get(position).getRoadshows().get(0).getRoadshowplan().getFinancedMount()) / (double) (proDatas.get(position).getRoadshows().get(0).getRoadshowplan().getFinanceTotal()) * 100);
                holder.itemProjectProgress.setProgress(progress);
                holder.itemBtnRecord.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SuperToastUtils.showSuperToast(mContext, 2, "查看" + position + "提交记录");
                    }
                });
                holder.itemBtnDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, RoadshowDetailsActivity.class);
                        intent.putExtra("id", String.valueOf(proDatas.get(position).getProjectId()));
                        startActivity(intent);
                    }
                });
            } else if (getItemViewType(position) == 2) {
                // 预选项目
                Glide.with(mContext).load(proDatas.get(position).getStartPageImage()).into(holder.itemProjectImg);
                holder.itemProjectTitle.setText(proDatas.get(position).getAbbrevName());
                holder.itemProjectAddr.setText(proDatas.get(position).getAddress());
                holder.itemProjectCompname.setText(proDatas.get(position).getFullName());
                String[] fields = proDatas.get(position).getIndustoryType().split("，");
                if (fields.length == 0) {
                    holder.itemProjectField1.setVisibility(View.INVISIBLE);
                    holder.itemProjectField2.setVisibility(View.INVISIBLE);
                    holder.itemProjectField3.setVisibility(View.INVISIBLE);
                } else if (fields.length == 1) {
                    holder.itemProjectField1.setText(fields[0]);
                    holder.itemProjectField2.setVisibility(View.INVISIBLE);
                    holder.itemProjectField3.setVisibility(View.INVISIBLE);
                } else if (fields.length == 2) {
                    holder.itemProjectField1.setText(fields[0]);
                    holder.itemProjectField2.setText(fields[1]);
                    holder.itemProjectField3.setVisibility(View.INVISIBLE);
                } else if (fields.length == 3) {
                    holder.itemProjectField1.setText(fields[0]);
                    holder.itemProjectField2.setText(fields[1]);
                    holder.itemProjectField3.setText(fields[2]);
                }
                holder.itemProjectPopularity.setText(String.valueOf(proDatas.get(position).getCollectionCount()));
                holder.itemProjectAmount.setText(proDatas.get(position).getRoadshows().get(0).getRoadshowplan().getFinanceTotal() + "万");
                holder.itemBtnRecord.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SuperToastUtils.showSuperToast(mContext, 2, "查看" + position + "提交记录");
                    }
                });
                holder.itemBtnDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, PreselectionDetailsActivity.class);
                        intent.putExtra("id", String.valueOf(proDatas.get(position).getProjectId()));
                        startActivity(intent);
                    }
                });
            } else {
                // 添加项目
            }
            return convertView;
        }

        class ViewHolder {
            private CircleImageView itemProjectImg;
            private TextView itemProjectTitle;
            private TextView itemProjectAddr;
            private ImageView itemProjectTag;// 路演独有
            private TextView itemProjectCompname;
            private TextView itemProjectField1;
            private TextView itemProjectField2;
            private TextView itemProjectField3;
            private TextView itemProjectPopularity;
            private TextView itemProjectTime;// 路演独有
            private TextView itemProjectAmount;
            private RoundProgressBar itemProjectProgress;// 路演独有
            private ImageView itemBtnRecord;
            private ImageView itemBtnDetail;
        }
    }

    // 投资人的数据列表填充器
    private class InvestorAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return investorInvestDatas.size() + investorCommitDatas.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getViewTypeCount() {
            return 3;
        }

        @Override
        public int getItemViewType(int position) {
            if (position < investorInvestDatas.size()) {
                return 0;// 认投的项目
            } else {
                if ("预选项目".equals(investorCommitDatas.get(position - investorInvestDatas.size()).getFinancestatus().getName())) {
                    return 2;// 收到的预选项目
                } else {
                    return 1;// 收到的路演项目
                }
            }
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                if (getItemViewType(position) == 0) {
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.item_pro_center_for_investor_for_invested, null);
                    holder.tvType = (TextView) convertView.findViewById(R.id.tv_type);
                    holder.itemProjectImg = (CircleImageView) convertView.findViewById(R.id.item_project_img);
                    holder.itemProjectTitle = (TextView) convertView.findViewById(R.id.item_project_title);
                    holder.itemProjectAddr = (TextView) convertView.findViewById(R.id.item_project_addr);
                    holder.itemProjectTag = (ImageView) convertView.findViewById(R.id.item_project_tag);
                    holder.itemProjectCompname = (TextView) convertView.findViewById(R.id.item_project_compname);
                    holder.itemProjectField1 = (TextView) convertView.findViewById(R.id.item_project_field1);
                    holder.itemProjectField2 = (TextView) convertView.findViewById(R.id.item_project_field2);
                    holder.itemProjectField3 = (TextView) convertView.findViewById(R.id.item_project_field3);
                    holder.itemProjectPopularity = (TextView) convertView.findViewById(R.id.item_project_popularity);
                    holder.itemProjectTime = (TextView) convertView.findViewById(R.id.item_project_time);
                    holder.itemProjectAmount = (TextView) convertView.findViewById(R.id.item_project_amount);
                    holder.itemProjectProgress = (RoundProgressBar) convertView.findViewById(R.id.item_project_progress);
                } else if (getItemViewType(position) == 1) {
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.item_pro_center_for_investor_for_receved_roadshow, null);
                    holder.tvType = (TextView) convertView.findViewById(R.id.tv_type);
                    holder.itemProjectImg = (CircleImageView) convertView.findViewById(R.id.item_project_img);
                    holder.itemProjectTitle = (TextView) convertView.findViewById(R.id.item_project_title);
                    holder.itemProjectAddr = (TextView) convertView.findViewById(R.id.item_project_addr);
                    holder.itemProjectTag = (ImageView) convertView.findViewById(R.id.item_project_tag);
                    holder.itemProjectCompname = (TextView) convertView.findViewById(R.id.item_project_compname);
                    holder.itemProjectField1 = (TextView) convertView.findViewById(R.id.item_project_field1);
                    holder.itemProjectField2 = (TextView) convertView.findViewById(R.id.item_project_field2);
                    holder.itemProjectField3 = (TextView) convertView.findViewById(R.id.item_project_field3);
                    holder.itemProjectPopularity = (TextView) convertView.findViewById(R.id.item_project_popularity);
                    holder.itemProjectTime = (TextView) convertView.findViewById(R.id.item_project_time);
                    holder.itemProjectAmount = (TextView) convertView.findViewById(R.id.item_project_amount);
                    holder.itemProjectProgress = (RoundProgressBar) convertView.findViewById(R.id.item_project_progress);
                    holder.itemProjectBtnIgnore = (ImageView) convertView.findViewById(R.id.item_project_btn_ignore);
                    holder.itemProjectBtnLookPro = (ImageView) convertView.findViewById(R.id.item_project_btn_look_pro);
                } else {
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.item_pro_center_for_investor_for_receved_preselection, null);
                    holder.tvType = (TextView) convertView.findViewById(R.id.tv_type);
                    holder.itemProjectImg = (CircleImageView) convertView.findViewById(R.id.item_project_img);
                    holder.itemProjectTitle = (TextView) convertView.findViewById(R.id.item_project_title);
                    holder.itemProjectAddr = (TextView) convertView.findViewById(R.id.item_project_addr);
                    holder.itemProjectCompname = (TextView) convertView.findViewById(R.id.item_project_compname);
                    holder.itemProjectField1 = (TextView) convertView.findViewById(R.id.item_project_field1);
                    holder.itemProjectField2 = (TextView) convertView.findViewById(R.id.item_project_field2);
                    holder.itemProjectField3 = (TextView) convertView.findViewById(R.id.item_project_field3);
                    holder.itemProjectPopularity = (TextView) convertView.findViewById(R.id.item_project_popularity);
                    holder.itemProjectBtnIgnore = (ImageView) convertView.findViewById(R.id.item_project_btn_ignore);
                    holder.itemProjectBtnLookPro = (ImageView) convertView.findViewById(R.id.item_project_btn_look_pro);
                }
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (getItemViewType(position) == 0) {// 认投的项目
                Glide.with(mContext).load(investorInvestDatas.get(position).getStartPageImage()).into(holder.itemProjectImg);
                holder.itemProjectTitle.setText(investorInvestDatas.get(position).getAbbrevName());
                holder.itemProjectAddr.setText(investorInvestDatas.get(position).getAddress());
                switch (investorInvestDatas.get(position).getFinancestatus().getName()) {
                    case "待路演":
                        holder.itemProjectTag.setImageResource(R.mipmap.tag_dailuyan);
                        break;
                    case "融资中":
                        holder.itemProjectTag.setImageResource(R.mipmap.tag_rongzizhong);
                        break;
                    case "融资成功":
                        holder.itemProjectTag.setImageResource(R.mipmap.tag_rongziwancheng);
                        break;
                    case "融资失败":
                        holder.itemProjectTag.setImageResource(R.mipmap.tag_rongzishibai);
                        break;
                }
                holder.itemProjectCompname.setText(investorInvestDatas.get(position).getFullName());
                String[] fields = investorInvestDatas.get(position).getIndustoryType().split("，");
                if (fields.length == 0) {
                    holder.itemProjectField1.setVisibility(View.INVISIBLE);
                    holder.itemProjectField2.setVisibility(View.INVISIBLE);
                    holder.itemProjectField3.setVisibility(View.INVISIBLE);
                } else if (fields.length == 1) {
                    holder.itemProjectField1.setText(fields[0]);
                    holder.itemProjectField2.setVisibility(View.INVISIBLE);
                    holder.itemProjectField3.setVisibility(View.INVISIBLE);
                } else if (fields.length == 2) {
                    holder.itemProjectField1.setText(fields[0]);
                    holder.itemProjectField2.setText(fields[1]);
                    holder.itemProjectField3.setVisibility(View.INVISIBLE);
                } else if (fields.length == 3) {
                    holder.itemProjectField1.setText(fields[0]);
                    holder.itemProjectField2.setText(fields[1]);
                    holder.itemProjectField3.setText(fields[2]);
                }
                holder.itemProjectPopularity.setText(String.valueOf(investorInvestDatas.get(position).getCollectionCount()));
                holder.itemProjectTime.setText(String.valueOf(investorInvestDatas.get(position).getTimeLeft()));
                holder.itemProjectAmount.setText(investorInvestDatas.get(position).getRoadshows().get(0).getRoadshowplan().getFinanceTotal() + "万");
                int progress = (int) ((double) (investorInvestDatas.get(position).getRoadshows().get(0).getRoadshowplan().getFinancedMount()) / (double) (investorInvestDatas.get(position).getRoadshows().get(0).getRoadshowplan().getFinanceTotal()) * 100);
                holder.itemProjectProgress.setProgress(progress);
            } else if (getItemViewType(position) == 1) {
                // 收到的路演项目
                final int newPosition = position - investorInvestDatas.size();
                Glide.with(mContext).load(investorCommitDatas.get(newPosition).getStartPageImage()).into(holder.itemProjectImg);
                holder.itemProjectTitle.setText(investorCommitDatas.get(newPosition).getAbbrevName());
                holder.itemProjectAddr.setText(investorCommitDatas.get(newPosition).getAddress());
                switch (investorCommitDatas.get(newPosition).getFinancestatus().getName()) {
                    case "待路演":
                        holder.itemProjectTag.setImageResource(R.mipmap.tag_dailuyan);
                        break;
                    case "融资中":
                        holder.itemProjectTag.setImageResource(R.mipmap.tag_rongzizhong);
                        break;
                    case "融资成功":
                        holder.itemProjectTag.setImageResource(R.mipmap.tag_rongziwancheng);
                        break;
                    case "融资失败":
                        holder.itemProjectTag.setImageResource(R.mipmap.tag_rongzishibai);
                        break;
                }
                holder.itemProjectCompname.setText(investorCommitDatas.get(newPosition).getFullName());
                String[] fields = investorCommitDatas.get(newPosition).getIndustoryType().split("，");
                if (fields.length == 0) {
                    holder.itemProjectField1.setVisibility(View.INVISIBLE);
                    holder.itemProjectField2.setVisibility(View.INVISIBLE);
                    holder.itemProjectField3.setVisibility(View.INVISIBLE);
                } else if (fields.length == 1) {
                    holder.itemProjectField1.setText(fields[0]);
                    holder.itemProjectField2.setVisibility(View.INVISIBLE);
                    holder.itemProjectField3.setVisibility(View.INVISIBLE);
                } else if (fields.length == 2) {
                    holder.itemProjectField1.setText(fields[0]);
                    holder.itemProjectField2.setText(fields[1]);
                    holder.itemProjectField3.setVisibility(View.INVISIBLE);
                } else if (fields.length == 3) {
                    holder.itemProjectField1.setText(fields[0]);
                    holder.itemProjectField2.setText(fields[1]);
                    holder.itemProjectField3.setText(fields[2]);
                }
                holder.itemProjectPopularity.setText(String.valueOf(investorCommitDatas.get(newPosition).getCollectionCount()));
                holder.itemProjectTime.setText(String.valueOf(investorCommitDatas.get(newPosition).getTimeLeft()));
                holder.itemProjectAmount.setText(investorCommitDatas.get(newPosition).getRoadshows().get(0).getRoadshowplan().getFinanceTotal() + "万");
                int progress = (int) ((double) (investorCommitDatas.get(newPosition).getRoadshows().get(0).getRoadshowplan().getFinancedMount()) / (double) (investorCommitDatas.get(newPosition).getRoadshows().get(0).getRoadshowplan().getFinanceTotal()) * 100);
                holder.itemProjectProgress.setProgress(progress);
                holder.itemProjectBtnIgnore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        investorCommitDatas.remove(newPosition);
                        investorAdapter.notifyDataSetInvalidated();
                    }
                });
                holder.itemProjectBtnLookPro.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, RoadshowDetailsActivity.class);
                        intent.putExtra("id", String.valueOf(investorCommitDatas.get(newPosition).getProjectId()));
                        startActivity(intent);
                    }
                });
            } else {
                // 收到的预选项目
                final int newPosition = position - investorInvestDatas.size();
                Glide.with(mContext).load(investorCommitDatas.get(newPosition).getStartPageImage()).into(holder.itemProjectImg);
                holder.itemProjectTitle.setText(investorCommitDatas.get(newPosition).getAbbrevName());
                holder.itemProjectAddr.setText(investorCommitDatas.get(newPosition).getAddress());
                holder.itemProjectCompname.setText(investorCommitDatas.get(newPosition).getFullName());
                String[] fields = investorCommitDatas.get(newPosition).getIndustoryType().split("，");
                if (fields.length == 0) {
                    holder.itemProjectField1.setVisibility(View.INVISIBLE);
                    holder.itemProjectField2.setVisibility(View.INVISIBLE);
                    holder.itemProjectField3.setVisibility(View.INVISIBLE);
                } else if (fields.length == 1) {
                    holder.itemProjectField1.setText(fields[0]);
                    holder.itemProjectField2.setVisibility(View.INVISIBLE);
                    holder.itemProjectField3.setVisibility(View.INVISIBLE);
                } else if (fields.length == 2) {
                    holder.itemProjectField1.setText(fields[0]);
                    holder.itemProjectField2.setText(fields[1]);
                    holder.itemProjectField3.setVisibility(View.INVISIBLE);
                } else if (fields.length == 3) {
                    holder.itemProjectField1.setText(fields[0]);
                    holder.itemProjectField2.setText(fields[1]);
                    holder.itemProjectField3.setText(fields[2]);
                }
                holder.itemProjectPopularity.setText(String.valueOf(investorCommitDatas.get(newPosition).getCollectionCount()));
                holder.itemProjectAmount.setText(investorCommitDatas.get(newPosition).getRoadshows().get(0).getRoadshowplan().getFinanceTotal() + "万");
                holder.itemProjectBtnIgnore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        investorCommitDatas.remove(newPosition);
                        investorAdapter.notifyDataSetInvalidated();
                    }
                });
                holder.itemProjectBtnLookPro.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, PreselectionDetailsActivity.class);
                        intent.putExtra("id", String.valueOf(investorCommitDatas.get(newPosition).getProjectId()));
                        startActivity(intent);
                    }
                });
            }

            return convertView;
        }

        class ViewHolder {
            private TextView tvType;
            private CircleImageView itemProjectImg;
            private TextView itemProjectTitle;
            private TextView itemProjectAddr;
            private ImageView itemProjectTag;// 路演项目独有
            private TextView itemProjectCompname;
            private TextView itemProjectField1;
            private TextView itemProjectField2;
            private TextView itemProjectField3;
            private TextView itemProjectPopularity;
            private TextView itemProjectTime;// 路演项目独有
            private TextView itemProjectAmount;// 路演项目独有
            private RoundProgressBar itemProjectProgress;// 路演项目独有
            private ImageView itemProjectBtnIgnore;// 收到的项目独有
            private ImageView itemProjectBtnLookPro;// 收到的项目独有
        }
    }

    // 智囊团数据列表
    private class BrainAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return null;
        }
    }

    // 获取项目方项目列表
    private class GetProject1List extends AsyncTask<Void, Void, ProCenter4ProBean> {
        private int page;

        public GetProject1List(int page) {
            this.page = page;
        }

        @Override
        protected ProCenter4ProBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.GETPROCENTERLIST)),
                            "type", "0",
                            "page", String.valueOf(page),
                            Constant.BASE_URL + Constant.GETPROCENTERLIST,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("项目方的项目列表", body);
                return FastJsonTools.getBean(body, ProCenter4ProBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(ProCenter4ProBean proCenter4ProBean) {
            super.onPostExecute(proCenter4ProBean);
            if (proCenter4ProBean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, "请先联网");
                refreshView.refreshFinish(PullToRefreshLayout.FAIL);// 告诉控件刷新失败
                refreshView.loadmoreFinish(PullToRefreshLayout.FAIL);// 告诉控件加载失败
            } else {
                if (proCenter4ProBean.getStatus() == 200) {
                    refreshView.refreshFinish(PullToRefreshLayout.SUCCEED);// 告诉控件刷新成功
                    refreshView.loadmoreFinish(PullToRefreshLayout.SUCCEED);// 告诉控件加载成功
                    if (page == 0) {
                        proDatas = proCenter4ProBean.getData();
                        if (proDatas != null) {
                            listview.setAdapter(proAdapter);
                        }
                    } else {
                        for (ProCenter4ProBean.DataBean dataBean : proCenter4ProBean.getData()) {
                            proDatas.add(dataBean);
                        }
                        proAdapter.notifyDataSetChanged();
                    }
                } else if (proCenter4ProBean.getStatus() == 201) {
                    pages--;
                    refreshView.loadmoreFinish(PullToRefreshLayout.LAST);// 告诉控件加载到最后一页
                } else {
                    refreshView.refreshFinish(PullToRefreshLayout.FAIL);// 告诉控件刷新失败
                    refreshView.loadmoreFinish(PullToRefreshLayout.FAIL);// 告诉控件加载失败
                    SuperToastUtils.showSuperToast(mContext, 2, proCenter4ProBean.getMessage());
                }
            }
        }
    }

    // 获取投资人项目列表
    private class GetProject2List extends AsyncTask<Void, Void, ProCenter4InvestorBean> {
        private int page;

        public GetProject2List(int page) {
            this.page = page;
        }

        @Override
        protected ProCenter4InvestorBean doInBackground(Void... params) {
            String body = "";
            if (!NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(NetWorkUtils.getNetWorkType(mContext))) {
                try {
                    body = OkHttpUtils.post(
                            MD5Utils.encode(AESUtils.encrypt(Constant.PRIVATE_KEY, Constant.GETPROCENTERLIST)),
                            "type", "1",
                            "page", String.valueOf(page),
                            Constant.BASE_URL + Constant.GETPROCENTERLIST,
                            mContext
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("投资人的项目列表", body);
                return FastJsonTools.getBean(body, ProCenter4InvestorBean.class);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(ProCenter4InvestorBean proCenter4InvestorBean) {
            super.onPostExecute(proCenter4InvestorBean);
            if (proCenter4InvestorBean == null) {
                SuperToastUtils.showSuperToast(mContext, 2, "请先联网");
                refreshView.refreshFinish(PullToRefreshLayout.FAIL);// 告诉控件刷新失败
                refreshView.loadmoreFinish(PullToRefreshLayout.FAIL);// 告诉控件加载失败
            } else {
                if (proCenter4InvestorBean.getStatus() == 200) {
                    refreshView.refreshFinish(PullToRefreshLayout.SUCCEED);// 告诉控件刷新成功
                    refreshView.loadmoreFinish(PullToRefreshLayout.SUCCEED);// 告诉控件加载成功
                    if (page == 0) {
                        investorInvestDatas = proCenter4InvestorBean.getData().getInvest();
                        investorCommitDatas = proCenter4InvestorBean.getData().getCommit();
                        if (investorInvestDatas != null && investorCommitDatas != null) {
                            listview.setAdapter(investorAdapter);
                        }
                    } else {
                        for (ProCenter4InvestorBean.DataBean.InvestBean investBean : proCenter4InvestorBean.getData().getInvest()) {
                            investorInvestDatas.add(investBean);
                        }
                        for (ProCenter4InvestorBean.DataBean.CommitBean commitBean : proCenter4InvestorBean.getData().getCommit()) {
                            investorCommitDatas.add(commitBean);
                        }
                        investorAdapter.notifyDataSetChanged();
                    }
                } else if (proCenter4InvestorBean.getStatus() == 201) {
                    pages--;
                    refreshView.loadmoreFinish(PullToRefreshLayout.LAST);// 告诉控件加载到最后一页
                } else {
                    refreshView.refreshFinish(PullToRefreshLayout.FAIL);// 告诉控件刷新失败
                    refreshView.loadmoreFinish(PullToRefreshLayout.FAIL);// 告诉控件加载失败
                    SuperToastUtils.showSuperToast(mContext, 2, proCenter4InvestorBean.getMessage());
                }
            }
        }
    }

    // 获取投资机构项目列表


    // 获取智囊团项目列表


    // 下拉刷新和上拉加载
    private class PullListener implements PullToRefreshLayout.OnRefreshListener {

        @Override
        public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
            // 下拉刷新
            pages = 0;
            switch (usertype) {
                case 1:// 项目方
                    break;
                case 2:// 投资人
                    break;
                case 3:// 投资机构
                    break;
                case 4:// 智囊团
                    break;
                default:
                    break;
            }
//            GetProjectList getProjectList = new GetProjectList(0);
//            getProjectList.execute();
        }

        @Override
        public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
            // 上拉加载
            pages++;
            Log.i("页码", String.valueOf(pages));
            switch (usertype) {
                case 1:// 项目方
                    break;
                case 2:// 投资人
                    break;
                case 3:// 投资机构
                    break;
                case 4:// 智囊团
                    break;
                default:
                    break;
            }
//            GetProjectList getProjectList = new GetProjectList(pages);
//            getProjectList.execute();
        }
    }

    @Override
    public void errorPage() {

    }

    @Override
    public void blankPage() {

    }
}
