package net.micode.notes.ui.note;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.yk.note.R;
import net.micode.notes.data.db.NoteDBOpenHelper;
import net.micode.notes.data.model.Note;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


//页面之间传递数据
public class NoteViewModel extends AndroidViewModel {
    private final ExecutorService singleThreadExecutor;

    MutableLiveData<Note> note = new MutableLiveData<>();
    MutableLiveData<String> info = new MutableLiveData<>();

    private final Context context;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
        singleThreadExecutor = Executors.newSingleThreadExecutor();
    }

    void saveNote(String content) {
        singleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                Note note1 = NoteDBOpenHelper.save(content);
                note.postValue(note1);
                info.postValue(context.getResources().getString(R.string.save_success));
            }
        });
    }

    void updateNote(Note note) {
        singleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                NoteDBOpenHelper.update(note);
                info.postValue(context.getResources().getString(R.string.update_success));
            }
        });
    }
}
