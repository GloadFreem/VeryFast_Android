package com.jinzht.pro.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinzht.pro.R;
import com.jinzht.pro.base.BaseActivity;
import com.jinzht.pro.utils.StringUtils;
import com.jinzht.pro.utils.UiHelp;

/**
 * 资金账户中银行卡界面
 */
public class BankCardActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout btnBack;// 返回
    private TextView tvTitle;// 标题
    private TextView tvBankName;// 银行卡名
    private TextView tvBankNo;// 银行卡号
    private ImageView btnBind;// 绑卡or解绑

    private String[] bankNames;

    @Override
    protected int getResourcesId() {
        return R.layout.activity_bank_card;
    }

    @Override
    protected void init() {
        UiHelp.setSameStatus(true, this);// 设置系统状态栏与应用标题栏背景一致

        bankNames = getResources().getStringArray(R.array.banks);
        btnBack = (LinearLayout) findViewById(R.id.btn_back);// 返回
        btnBack.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.tv_title);// 标题
        tvTitle.setText("银行卡");
        tvBankName = (TextView) findViewById(R.id.tv_bank_name);// 银行名
        tvBankNo = (TextView) findViewById(R.id.tv_bank_no);// 银行卡号
        btnBind = (ImageView) findViewById(R.id.btn_bind);// 绑卡or解绑
        btnBind.setOnClickListener(this);

        initData();
    }

    private void initData() {
        if (StringUtils.isBlank(getIntent().getStringExtra("bankNo"))) {
            tvBankName.setText("您还没有绑定银行卡");
            tvBankNo.setText("XXXX XXXX XXXX XXXX");
            btnBind.setImageResource(R.mipmap.icon_bind_card);
        } else {
            tvBankNo.setText(getIntent().getStringExtra("bankNo"));
            switch (getIntent().getStringExtra("bankName")) {
                case "BOCO":
                    tvBankName.setText(bankNames[0]);
                    break;
                case "CEB":
                    tvBankName.setText(bankNames[1]);
                    break;
                case "SPDB":
                    tvBankName.setText(bankNames[2]);
                    break;
                case "ABC":
                    tvBankName.setText(bankNames[3]);
                    break;
                case "ECITIC":
                    tvBankName.setText(bankNames[4]);
                    break;
                case "CCB":
                    tvBankName.setText(bankNames[5]);
                    break;
                case "CMBC":
                    tvBankName.setText(bankNames[6]);
                    break;
                case "SDB":
                    tvBankName.setText(bankNames[7]);
                    break;
                case "PSBC":
                    tvBankName.setText(bankNames[8]);
                    break;
                case "CMBCHINA":
                    tvBankName.setText(bankNames[9]);
                    break;
                case "CIB":
                    tvBankName.setText(bankNames[10]);
                    break;
                case "ICBC":
                    tvBankName.setText(bankNames[11]);
                    break;
                case "BOC":
                    tvBankName.setText(bankNames[12]);
                    break;
                case "BCCB":
                    tvBankName.setText(bankNames[13]);
                    break;
                case "GDB":
                    tvBankName.setText(bankNames[14]);
                    break;
                case "HX":
                    tvBankName.setText(bankNames[15]);
                    break;
                case "XAYH":
                    tvBankName.setText(bankNames[16]);
                    break;
                case "SHYH":
                    tvBankName.setText(bankNames[17]);
                    break;
                case "TJYH":
                    tvBankName.setText(bankNames[18]);
                    break;
                case "SZNCSYYH":
                    tvBankName.setText(bankNames[19]);
                    break;
                case "BJNCSYYH":
                    tvBankName.setText(bankNames[20]);
                    break;
                case "HZYH":
                    tvBankName.setText(bankNames[21]);
                    break;
                case "KLYH":
                    tvBankName.setText(bankNames[22]);
                    break;
                case "ZHENGZYH":
                    tvBankName.setText(bankNames[23]);
                    break;
                case "WZYH":
                    tvBankName.setText(bankNames[24]);
                    break;
                case "HKYH":
                    tvBankName.setText(bankNames[25]);
                    break;
                case "NJYH":
                    tvBankName.setText(bankNames[26]);
                    break;
                case "XMYH":
                    tvBankName.setText(bankNames[27]);
                    break;
                case "NCYH":
                    tvBankName.setText(bankNames[28]);
                    break;
                case "JISYH":
                    tvBankName.setText(bankNames[29]);
                    break;
                case "HKBEA":
                    tvBankName.setText(bankNames[30]);
                    break;
                case "CDYH":
                    tvBankName.setText(bankNames[31]);
                    break;
                case "NBYH":
                    tvBankName.setText(bankNames[32]);
                    break;
                case "CSYH":
                    tvBankName.setText(bankNames[33]);
                    break;
                case "HBYH":
                    tvBankName.setText(bankNames[34]);
                    break;
                case "GUAZYH":
                    tvBankName.setText(bankNames[35]);
                    break;
            }
            btnBind.setImageResource(R.mipmap.icon_unbind_card);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_bind:
                if (StringUtils.isBlank(getIntent().getStringExtra("bankNo"))) {
                    // 去绑卡
                    
                } else {
                    // 解绑
                }
                break;
        }
    }

    @Override
    public void errorPage() {

    }

    @Override
    public void blankPage() {

    }

}
