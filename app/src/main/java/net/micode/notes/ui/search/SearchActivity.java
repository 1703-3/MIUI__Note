package net.micode.notes.ui.search;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.yk.note.R;
import net.micode.notes.data.adapter.NoteAdapter;
import net.micode.notes.data.model.Note;
import net.micode.notes.utils.SnackBarUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements NoteAdapter.OnLongClickDeleteListener {
    private SearchViewModel viewModel;

    private View view;
    private Toolbar toolbar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private SearchView searchView;

    private final List<Note> noteList = new ArrayList<>();
    private NoteAdapter noteAdapter;

    private String content;

    private boolean isFirstLoad = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        viewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        initView();
        event();
    }



    private void initView() {
        view = findViewById(R.id.ac_search_view);
        toolbar = findViewById(R.id.ac_search_toolbar);
        swipeRefreshLayout = findViewById(R.id.ac_search_swipe_refresh_layout);
        recyclerView = findViewById(R.id.ac_search_recycler_view);

        toolbar.setTitle(R.string.search);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        initRecyclerView();
    }

    private void initRecyclerView() {
        swipeRefreshLayout.setColorSchemeResources(
                R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorAccent);

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(
                2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        noteAdapter = new NoteAdapter(noteList, null, this);
        recyclerView.setAdapter(noteAdapter);
    }

    private void event() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadNote();
            }
        });
        viewModel.noteList.observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> list) {
                if (list == null || list.size() == 0) {
                    noteList.clear();
                    noteAdapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);
                    SnackBarUtils.tip(view, getResources().getString(R.string.search_empty));
                    return;
                }
                Collections.reverse(list);
                noteList.clear();
                noteList.addAll(list);
                noteAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
                if (isFirstLoad) {
                    SnackBarUtils.tip(view, String.format(getResources().getString(R.string.search_num), list.size()));
                    isFirstLoad = false;
                }
            }
        });
        viewModel.deleteInfo.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String info) {
                loadNote();
                SnackBarUtils.tip(view, info);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ac_search, menu);
        searchView = (SearchView) menu.findItem(R.id.menu_ac_search_view).getActionView();
        initSearchView();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
    }

    private void initSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                content = query;
                loadNote();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
    }

    private void loadNote() {
        if (TextUtils.isEmpty(content)) {
            swipeRefreshLayout.setRefreshing(false);
            return;
        }
        swipeRefreshLayout.setRefreshing(true);
        viewModel.loadNote(content);
    }

    @Override
    public void onDelete(Note note) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this)
                .setTitle(R.string.delete_select_title)
                .setMessage(R.string.delete_select_message)
                .setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        viewModel.deleteNote(note.getId());
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
}
