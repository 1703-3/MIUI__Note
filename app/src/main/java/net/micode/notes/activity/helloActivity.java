package net.micode.notes.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.yk.note.R;

public class helloActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello);
        initView();
    }

    private void initView() {
        // 初始化控件对象
        Button mBtMainLogout = findViewById(R.id.button_login);
        // 绑定点击监听器
        mBtMainLogout.setOnClickListener(this);
    }
    public void onClick(View view) {
        if (view.getId() == R.id.button_login) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}