package net.micode.notes.ui.note;

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.yk.note.R;
import net.micode.notes.data.model.Note;
import net.micode.notes.utils.SnackBarUtils;
import net.micode.notes.utils.markdown.Markdown;

public class NoteActivity extends AppCompatActivity {
    private static final int MODE_PREVIEW = 0;
    private static final int MODE_EDIT = 1;

    private int mode = MODE_PREVIEW;

    private NoteViewModel viewModel;

    private View view;
    private Toolbar toolbar;
    private AppCompatTextView previewTv;
    private AppCompatEditText editText;

    private Menu menu;

    private Note note;

    private String source;

    private int orientation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        viewModel = new ViewModelProvider(this).get(NoteViewModel.class);

        initView();
        event();
    }


    private void initView() {
        view = findViewById(R.id.ac_note_view);
        toolbar = findViewById(R.id.ac_note_toolbar);
        previewTv = findViewById(R.id.ac_note_preview_text);
        editText = findViewById(R.id.ac_note_edit_text);

        //设置编辑页标题
        toolbar.setTitle(R.string.edit_note);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void event() {
        viewModel.note.observe(this, data -> {
            if (data == null) {
                SnackBarUtils.tip(view, getResources().getString(R.string.save_fail));
                return;
            }
            note = data;
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                mode = MODE_PREVIEW;
                enterMode();
            }
        });
        viewModel.info.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String info) {
                SnackBarUtils.tip(view, info);
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.i("JOJO", "s:" + s.toString());
                source = s.toString();
                Markdown.from(source).to(previewTv);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ac_note, menu);
        this.menu = menu;
        initMenu();
        return true;
    }

    private void initMenu() {
        Log.i("JOJO", "initMenu");
        note = (Note) getIntent().getSerializableExtra("note");
        orientation = getResources().getConfiguration().orientation;
        changeOrientation();
    }

    private void changeOrientation() {
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            // 竖屏
            if (!TextUtils.isEmpty(source)) {
                mode = MODE_PREVIEW;
                editText.setText(source);
                Markdown.from(source).to(previewTv);
            } else if (note != null) {
                source = note.getContent();
                mode = MODE_PREVIEW;
                editText.setText(source);
                Markdown.from(source).to(previewTv);
            } else {
                mode = MODE_EDIT;
            }
            enterMode();
        } else {
            // 横屏
            //设置icon
            menu.findItem(R.id.menu_ac_note_fun).setIcon(R.drawable.ic_done_white_24dp);
            editText.setVisibility(View.VISIBLE);
            previewTv.setVisibility(View.VISIBLE);
            editText.setLines(15);

            if (!TextUtils.isEmpty(source)) {
                editText.setText(source);
                Markdown.from(source).to(previewTv);
            } else if (note != null) {
                source = note.getContent();
                editText.setText(source);
                Markdown.from(source).to(previewTv);
            }
        }
    }

    private void enterMode() {
        switch (mode) {
            case MODE_EDIT:
                enterEditMode();
                break;
            case MODE_PREVIEW:
                enterPreviewMode();
                break;
            default:
                break;
        }
    }

    private void enterEditMode() {
        menu.findItem(R.id.menu_ac_note_fun).setIcon(R.drawable.ic_done_white_24dp);
        editText.setVisibility(View.VISIBLE);
        previewTv.setVisibility(View.GONE);
    }

    private void enterPreviewMode() {
        menu.findItem(R.id.menu_ac_note_fun).setIcon(R.drawable.ic_mode_edit_white_24dp);
        editText.setVisibility(View.GONE);
        previewTv.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (isSave()) {
                    finish();
                } else {
                    onBackBefore();
                }
                break;
            case R.id.menu_ac_note_fun:
                fun();
                break;
            default:
                break;
        }
        return true;
    }

    private void fun() {
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            save();
            return;
        }
        switch (mode) {
            case MODE_EDIT:
                save();
                break;
            case MODE_PREVIEW:
                edit();
                break;
            default:
                break;
        }
    }

    private void save() {
        if (note != null) {
            note.setContent(source);
            viewModel.updateNote(note);
        } else {
            viewModel.saveNote(source);
        }
    }

    private void edit() {
        mode = MODE_EDIT;
        enterMode();
    }

    @Override
    public void onBackPressed() {
        if (isSave()) {
            super.onBackPressed();
        } else {
            onBackBefore();
        }
    }

    private void onBackBefore() {
        AlertDialog.Builder builder = new AlertDialog.Builder(NoteActivity.this)
                .setTitle(R.string.save)
                .setMessage(R.string.save_message)
                .setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        save();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });
        builder.show();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        orientation = newConfig.orientation;
        changeOrientation();
    }

    private boolean isSave() {
        if (note == null) {
            return TextUtils.isEmpty(source);
        }
        String content = note.getContent();
        return content.equals(source);
    }
}