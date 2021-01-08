package net.micode.notes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.yk.note.R;
import net.micode.notes.data.db.Code;
import net.micode.notes.data.db.UserDBOpenHelper;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private String realCode;
    private UserDBOpenHelper mUserDBOpenHelper;
    private Button mBtRegisterActivityRegister;
    private RelativeLayout mRlRegisterActivityTop;
    private ImageView mIvRegisterActivityBack;
    private LinearLayout mLlRegisterActivityBody;
    private EditText mEtRegisterActivityUsername;
    private EditText mEtRegisterActivityPassword1;
    private EditText mEtRegisterActivityPassword2;
    private EditText mEtRegisterActivityPhoneCodes;
    private ImageView mIvRegisterActivityShowCode;
    private RelativeLayout mRlRegisterActivityBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();

        mUserDBOpenHelper = new UserDBOpenHelper(this);

        //将验证码用图片的形式显示出来
        mIvRegisterActivityShowCode.setImageBitmap(Code.getInstance().createBitmap());
        realCode = Code.getInstance().getCode().toLowerCase();
    }

    private void initView(){
        mBtRegisterActivityRegister = findViewById(R.id.bt_registeractivity_register);
        mRlRegisterActivityTop = findViewById(R.id.rl_registerActivity_top);
        mIvRegisterActivityBack = findViewById(R.id.iv_registerActivity_back);
        mLlRegisterActivityBody = findViewById(R.id.ll_registeractivity_body);
        mEtRegisterActivityUsername = findViewById(R.id.et_registeractivity_username);
        mEtRegisterActivityPassword1 = findViewById(R.id.et_registeractivity_password1);
        mEtRegisterActivityPassword2 = findViewById(R.id.et_registeractivity_password2);
        mEtRegisterActivityPhoneCodes = findViewById(R.id.et_registeractivity_phoneCodes);
        mIvRegisterActivityShowCode = findViewById(R.id.iv_registeractivity_showCode);
        mRlRegisterActivityBottom = findViewById(R.id.rl_registeractivity_bottom);

        /**
         * 注册页面能点击的就三个地方
         * top处返回箭头、刷新验证码图片、注册按钮
         */
        mIvRegisterActivityBack.setOnClickListener(this);
        mIvRegisterActivityShowCode.setOnClickListener(this);
        mBtRegisterActivityRegister.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            //返回登录页面
            case R.id.iv_registerActivity_back:
                Intent intent1 = new Intent(this, LoginActivity.class);
                startActivity(intent1);
                finish();
                break;
            //改变随机验证码的生成
            case R.id.iv_registeractivity_showCode:
                mIvRegisterActivityShowCode.setImageBitmap(Code.getInstance().createBitmap());
                realCode = Code.getInstance().getCode().toLowerCase();
                break;
            //注册按钮
            case R.id.bt_registeractivity_register:
                //获取用户输入的用户名、密码、验证码
                String username = mEtRegisterActivityUsername.getText().toString().trim();
                String password1 = mEtRegisterActivityPassword1.getText().toString().trim();
                String password2 = mEtRegisterActivityPassword2.getText().toString().trim();
                String phoneCode = mEtRegisterActivityPhoneCodes.getText().toString().toLowerCase();
                //注册验证
                if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password1) && !TextUtils.isEmpty(phoneCode) &&!TextUtils.isEmpty(password2)) {
                    if (phoneCode.equals(realCode)) {
                        //将用户名和密码加入到数据库中
                        mUserDBOpenHelper.add(username, password1);
                        Intent intent2 = new Intent(this, LoginActivity.class);
                        startActivity(intent2);
                        finish();
                        Toast.makeText(this,  "验证通过，注册成功", Toast.LENGTH_SHORT).show();
                    }else if(!password1.equals(password2)){
                        Toast.makeText(this,  "两次密码不一致,注册失败", Toast.LENGTH_SHORT).show();
                        //更改验证码
                        mIvRegisterActivityShowCode.setImageBitmap(Code.getInstance().createBitmap());
                        realCode = Code.getInstance().getCode().toLowerCase();
                    }
                    else {
                        Toast.makeText(this, "验证码错误,注册失败", Toast.LENGTH_SHORT).show();
                        mIvRegisterActivityShowCode.setImageBitmap(Code.getInstance().createBitmap());
                        realCode = Code.getInstance().getCode().toLowerCase();
                    }
                }else {
                    mIvRegisterActivityShowCode.setImageBitmap(Code.getInstance().createBitmap());
                    realCode = Code.getInstance().getCode().toLowerCase();
                    Toast.makeText(this, "未完善信息，注册失败", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}

