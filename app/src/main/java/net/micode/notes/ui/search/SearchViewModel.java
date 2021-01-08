package net.micode.notes.ui.search;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.yk.note.R;
import net.micode.notes.data.db.NoteDBOpenHelper;
import net.micode.notes.data.model.Note;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SearchViewModel extends AndroidViewModel {
    private ExecutorService singleThreadExecutor;

    MutableLiveData<List<Note>> noteList = new MutableLiveData<>();
    MutableLiveData<String> deleteInfo = new MutableLiveData<>();

    private Context context;

    public SearchViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
        singleThreadExecutor = Executors.newSingleThreadExecutor();
    }

    void loadNote(String content) {
        singleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                List<Note> list = NoteDBOpenHelper.queryAll();
                List<Note> ifList = new ArrayList<>();
                for (Note note : list) {
                    if (note.getContent().contains(content)) {
                        ifList.add(note);
                    }
                }
                noteList.postValue(ifList);
            }
        });
    }

    void deleteNote(int id) {
        singleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                NoteDBOpenHelper.delete(id);
                deleteInfo.postValue(context.getResources().getString(R.string.delete_success));
            }
        });
    }
}
