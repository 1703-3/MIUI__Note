package net.micode.notes;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.yk.note.R;

import net.micode.notes.data.db.NoteDBOpenHelper;
import net.micode.notes.data.model.Note;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainViewModel extends AndroidViewModel {
    private ExecutorService singleThreadExecutor;

    MutableLiveData<List<Note>> noteList = new MutableLiveData<>();

    MutableLiveData<String> deleteInfo = new MutableLiveData<>();

    private Context context;

    public MainViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
        singleThreadExecutor = Executors.newSingleThreadExecutor();
    }

    void loadNote() {
        singleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                List<Note> list = NoteDBOpenHelper.queryAll();
                noteList.postValue(list);
            }
        });
    }

    void deleteNote(List<Note> selectList) {
        singleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                for (Note note : selectList) {
                    NoteDBOpenHelper.delete(note.getId());
                }
                deleteInfo.postValue(context.getResources().getString(R.string.delete_success));
            }
        });
    }

}
