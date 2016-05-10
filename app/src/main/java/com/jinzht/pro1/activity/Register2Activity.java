package com.jinzht.pro1.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.jinzht.pro1.R;
import com.jinzht.pro1.base.BaseActivity;
import com.jinzht.pro1.utils.SuperToastUtils;
import com.jinzht.pro1.utils.UiHelp;

/**
 * 注册的第二个页面
 */
public class Register2Activity extends BaseActivity implements View.OnClickListener {

    private ImageButton register2BtBack;// 返回
    private ImageButton register2BtContactService;// 联系客服
    private EditText register2EdPassword1;// 设置密码输入框
    private EditText register2EdPassword2;// 确认密码输入框
    private Button register2BtConfirm;// 下一步按钮


    @Override
    protected int getResourcesId() {
        return R.layout.activity_register2;
    }

    @Override
    protected void init() {
        UiHelp.setFullScreenStatus(this);// 设置系统状态栏跟随应用背景

        register2BtBack = (ImageButton) findViewById(R.id.register2_bt_back);// 返回
        register2BtBack.setOnClickListener(this);
        register2BtContactService = (ImageButton) findViewById(R.id.register2_bt_contact_service);// 联系客服
        register2BtContactService.setOnClickListener(this);
        register2EdPassword1 = (EditText) findViewById(R.id.register2_ed_password1);// 设置密码输入框
        register2EdPassword2 = (EditText) findViewById(R.id.register2_ed_password2);// 确认密码输入框
        register2BtConfirm = (Button) findViewById(R.id.register2_bt_confirm);// 下一步按钮
        register2BtConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register2_bt_back:// 点击了返回
                finish();
                break;
            case R.id.register2_bt_contact_service:// 点击了联系客服
                SuperToastUtils.showSuperToast(this, 2, "点击了联系客服");
                break;
            case R.id.register2_bt_confirm:// 点击下一步按钮，进入完善信息页面
                Intent intent = new Intent(this, ImproveInfoActivity.class);
                startActivity(intent);
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
