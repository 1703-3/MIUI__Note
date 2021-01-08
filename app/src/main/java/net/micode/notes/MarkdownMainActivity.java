package net.micode.notes;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.yk.note.R;

import net.micode.notes.data.adapter.NoteAdapter;
import net.micode.notes.data.model.Note;
import net.micode.notes.ui.note.NoteActivity;
import net.micode.notes.ui.search.SearchActivity;
import net.micode.notes.utils.SnackBarUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MarkdownMainActivity extends AppCompatActivity implements NoteAdapter.OnEventListener {
    private static final String TAG = MarkdownMainActivity.class.getSimpleName();

    private MainViewModel viewModel;

    private View view;
    private Toolbar toolbar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private AppCompatTextView noteEmptyTv;
    private FloatingActionButton addFab;

    private final List<Note> noteList = new ArrayList<>();
    private NoteAdapter noteAdapter;

    private ActionBar actionBar;

    private Menu menu;

    private boolean isFirstLoad = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        findView();
        init();
        event();
    }

    private void findView() {
        view = findViewById(R.id.ac_main_view);
        toolbar = findViewById(R.id.ac_main_toolbar);
        swipeRefreshLayout = findViewById(R.id.ac_main_swipe_refresh_layout);
        recyclerView = findViewById(R.id.ac_main_recycler_view);
        noteEmptyTv = findViewById(R.id.ac_main_note_empty);
        addFab = findViewById(R.id.ac_main_add_fab);
    }

    private void init() {
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();

        initRecyclerView();
    }

    private void initRecyclerView() {
        swipeRefreshLayout.setColorSchemeResources(
                R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorAccent);

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(
                2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        noteAdapter = new NoteAdapter(noteList, this, null);
        recyclerView.setAdapter(noteAdapter);
    }

    private void event() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!noteAdapter.isSelectMode()) {
                    loadNote();
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                    SnackBarUtils.tip(view, getResources().getString(R.string.select_mode_tip));
                }
            }
        });
        viewModel.noteList.observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> list) {
                if (list == null || list.size() == 0) {
                    noteList.clear();
                    noteAdapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);
                    displayEmpty(true);
                    return;
                }
                Collections.reverse(list);
                noteList.clear();
                noteList.addAll(list);
                noteAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
                displayEmpty(false);
                if (isFirstLoad) {
                    SnackBarUtils.tip(view, String.format(getResources().getString(R.string.load_num), list.size()));
                    isFirstLoad = false;
                }
            }
        });
        viewModel.deleteInfo.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String info) {
                SnackBarUtils.tip(view, info);
                closeSelectMode();
                loadNote();
            }
        });
        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNote();
            }
        });
    }

    private void displayEmpty(boolean isEmpty) {
        if (isEmpty) {
            recyclerView.setVisibility(View.GONE);
            noteEmptyTv.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            noteEmptyTv.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadNote();
    }

    private void loadNote() {
        swipeRefreshLayout.setRefreshing(true);
        viewModel.loadNote();
    }

    private void addNote() {
        if (noteAdapter.isSelectMode()) {
            closeSelectMode();
        }
        startActivity(new Intent(MarkdownMainActivity.this, NoteActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ac_main, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                closeSelectMode();
                break;
            case R.id.menu_ac_main_search:
                search();
                break;
            case R.id.menu_ac_main_select_all:
                selectAll(item);
                break;
            case R.id.menu_ac_main_delete:
                deleteSelect();
                break;
            default:
                break;
        }
        return true;
    }

    private void search() {
        startActivity(new Intent(MarkdownMainActivity.this, SearchActivity.class));
    }

    private void enterSelectMode() {
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_clear_white_24dp);
        }
        menu.findItem(R.id.menu_ac_main_search).setVisible(false);
        menu.findItem(R.id.menu_ac_main_delete).setVisible(true);
        menu.findItem(R.id.menu_ac_main_select_all).setVisible(true);
    }

    private void closeSelectMode() {
        noteAdapter.closeSelectMode();
        toolbar.setTitle(R.string.app_name);
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
        menu.findItem(R.id.menu_ac_main_search).setVisible(true);
        menu.findItem(R.id.menu_ac_main_delete).setVisible(false);
        menu.findItem(R.id.menu_ac_main_select_all).setVisible(false);
    }

    private void selectAll(MenuItem item) {
        int num = noteAdapter.getSelectNum();
        if (num == noteList.size()) {
            item.setIcon(R.drawable.ic_select_all_white_24dp);
            closeSelectMode();
            return;
        }
        noteAdapter.selectAll();
        item.setIcon(R.drawable.ic_done_white_24dp);
    }

    private void deleteSelect() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MarkdownMainActivity.this)
                .setTitle(R.string.delete_select_title)
                .setMessage(R.string.delete_select_message)
                .setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        viewModel.deleteNote(noteAdapter.getSelectList());
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.show();
    }

    @Override
    public void onLongClick() {
        enterSelectMode();
        int selectNum = noteAdapter.getSelectNum();
        setSelectAllView(selectNum, noteList.size());
    }

    @Override
    public void onClick() {
        if (noteAdapter.isSelectMode()) {
            int num = noteAdapter.getSelectNum();
            setSelectAllView(num, noteList.size());
        }
    }

    private void setSelectAllView(int num, int size) {
        if (num == size) {
            toolbar.setTitle(R.string.select_all);
            menu.findItem(R.id.menu_ac_main_select_all).setIcon(R.drawable.ic_done_white_24dp);
        } else {
            toolbar.setTitle(String.format(getResources().getString(R.string.select_num), noteAdapter.getSelectNum()));
            menu.findItem(R.id.menu_ac_main_select_all).setIcon(R.drawable.ic_select_all_white_24dp);
        }
    }

    @Override
    public void onBackPressed() {
        if (noteAdapter.isSelectMode()) {
            closeSelectMode();
        } else {
            super.onBackPressed();
        }
    }
}