package net.micode.notes.welcome;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import net.micode.notes.R;
import net.micode.notes.menu.menu;
import net.micode.notes.model.Note;
import net.micode.notes.ui.NotesListActivity;

import java.util.Timer;
import java.util.TimerTask;

public class welcome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Timer timer=new Timer();
        TimerTask timerTask=new TimerTask() {
            @Override
            public void run() {
                Intent intent1=new Intent(welcome.this, menu.class);
                startActivity(intent1);
                welcome.this.finish();
            }
        };
        timer.schedule(timerTask,1000*2);
    }
}