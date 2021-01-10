package net.micode.notes.me;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import net.micode.notes.R;
import net.micode.notes.menu.menu;
import net.micode.notes.welcome.welcome;

public class aboutmeActivity extends AppCompatActivity {
    private ImageView img1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutme);

        img1 = (ImageView) findViewById(R.id.setting_back);
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(aboutmeActivity.this, menu.class);
                startActivity(intent1);
            }
        });
    }
}