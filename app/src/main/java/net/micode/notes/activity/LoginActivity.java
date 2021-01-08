package net.micode.notes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import net.micode.notes.MarkdownMainActivity;
import com.yk.note.R;
import net.micode.notes.data.db.UserDBOpenHelper;
import net.micode.notes.data.model.User;

import java.util.ArrayList;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    /**
     * 声明自己写的 UserDBOpenHelper 对象
     * UserDBOpenHelper(extends SQLiteOpenHelper) 主要用来
     * 创建数据表
     * 然后再进行数据表的增、删、改、查操作
     */
    private UserDBOpenHelper mUserDBOpenHelper;
    private TextView mTvLoginActivityRegister;
    private RelativeLayout mRlLoginActivityTop;
    private EditText mEtLoginActivityUsername;
    private EditText mEtLoginActivityPassword;
    private LinearLayout mLlLoginActivityTwo;
    private Button mBtLoginActivityLogin;

    /**
     * 创建 Activity 时先来重写 onCreate() 方法
     * 保存实例状态
     * super.onCreate(savedInstanceState);
     * 设置视图内容的配置文件
     * setContentView(R.layout.activity_login);
     * 上面这行代码真正实现了把视图层 View 也就是 layout 的内容放到 Activity 中进行显示
     * 初始化视图中的控件对象 initView()
     * 实例化 UserDBOpenHelper，待会进行登录验证的时候要用来进行数据查询
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();

        mUserDBOpenHelper = new UserDBOpenHelper(this);
    }

    /**
     * onCreate()中大的布局已经摆放好了，接下来就该把layout里的东西
     * 把视图层View也就是layout 与 控制层 Java 结合起来了
     */
    private void initView() {
        // 初始化控件
        mBtLoginActivityLogin = findViewById(R.id.bt_loginactivity_login);
        mTvLoginActivityRegister = findViewById(R.id.tv_loginActivity_register);
        mRlLoginActivityTop = findViewById(R.id.rl_loginactivity_top);
        mEtLoginActivityUsername = findViewById(R.id.et_loginactivity_username);
        mEtLoginActivityPassword = findViewById(R.id.et_loginactivity_password);
        mLlLoginActivityTwo = findViewById(R.id.ll_loginactivity_two);

        // 设置点击事件监听器
        mBtLoginActivityLogin.setOnClickListener(this);
        mTvLoginActivityRegister.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            // 跳转到注册界面
            case R.id.tv_loginActivity_register:
                Intent intentRegister=new Intent(this,RegisterActivity.class);
                startActivity(intentRegister);
                finish();
                break;
            /**
             * 登录验证：
             *
             * 从EditText的对象上获取文本编辑框输入的数据，并把左右两边的空格去掉
             *  String name = mEtLoginActivityUsername.getText().toString().trim();
             *  String password = mEtLoginActivityPassword.getText().toString().trim();
             *  进行匹配验证,先判断一下用户名密码是否为空，
             *  if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(password))
             *  再进而for循环判断是否与数据库中的数据相匹配
             *  if (name.equals(user.getName()) && password.equals(user.getPassword()))
             *  一旦匹配，立即将match = true；break；
             *  否则 一直匹配到结束 match = false；
             *  登录成功之后，进行页面跳转
             */
            case R.id.bt_loginactivity_login:
                String name = mEtLoginActivityUsername.getText().toString().trim();
                String password = mEtLoginActivityPassword.getText().toString().trim();
                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(password)) {
                    ArrayList<User> data = mUserDBOpenHelper.getAllData();
                    boolean match = false;
                    for (int i = 0; i < data.size(); i++) {
                        User user = data.get(i);
                        if (name.equals(user.getName()) && password.equals(user.getPassword())) {
                            match = true;
                            break;
                        } else {
                            match = false;
                        }
                    }
                    if (match) {
                        Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
                        Intent intentLogin = new Intent(this, MarkdownMainActivity.class);
                        startActivity(intentLogin);
                        finish();//销毁此Activity
                    } else {
                        Toast.makeText(this, "用户名或密码不正确，请重新输入", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "请输入你的用户名或密码", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}



