package net.micode.notes.menu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import net.micode.notes.R;
import net.micode.notes.draw.drawActivity;
import net.micode.notes.me.aboutmeActivity;
import net.micode.notes.ui.NotesListActivity;

public class menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.l1: {
                Intent intent1 = new Intent();
                intent1.setClass(menu.this, NotesListActivity.class);
                startActivity(intent1);
                break;
            }

            case R.id.l3:{
                Intent intent3 = new Intent();
                intent3.setClass(menu.this, drawActivity.class);
                startActivity(intent3);
                break;
            }

            case R.id.imageView2:{
                Intent intent2 = new Intent();
                intent2.setClass(menu.this, aboutmeActivity.class);
                startActivity(intent2);
            }
            default:
                break;
        }
    }
}