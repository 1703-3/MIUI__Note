package net.micode.notes.data.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.yk.note.R;
import net.micode.notes.data.model.Note;
import net.micode.notes.ui.note.NoteActivity;
import net.micode.notes.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;


public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    private Context context;
    private final List<Note> noteList;

    private boolean isSelectMode = false;

    private final OnEventListener onEventListener;
    private final OnLongClickDeleteListener onLongClickDeleteListener;

    public NoteAdapter(List<Note> list, OnEventListener onEventListener, OnLongClickDeleteListener onLongClickDeleteListener) {
        noteList = list;
        this.onEventListener = onEventListener;
        this.onLongClickDeleteListener = onLongClickDeleteListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.item_note, parent, false);
        ViewHolder holder = new ViewHolder(view);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Note note = noteList.get(position);
                if (isSelectMode && onEventListener != null) {
                    note.setSelect(!note.isSelect());
                    notifyDataSetChanged();
                    onEventListener.onClick();
                } else {
                    Intent intent = new Intent(context, NoteActivity.class);
                    intent.putExtra("note", note);
                    context.startActivity(intent);
                }
            }
        });
        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int position = holder.getAdapterPosition();
                Note note = noteList.get(position);
                if (!isSelectMode && onEventListener != null) {
                    note.setSelect(true);
                    notifyDataSetChanged();
                    isSelectMode = true;
                    onEventListener.onLongClick();
                }
                if (onLongClickDeleteListener != null) {
                    onLongClickDeleteListener.onDelete(note);
                }
                return true;
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Note note = noteList.get(position);
        holder.content.setText(note.getContent());
        holder.time.setText(TimeUtils.getTimeStr(note.getAdded()));
        if (note.isSelect()) {
            //设置样式
            holder.view.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            holder.content.setTextColor(context.getResources().getColor(R.color.colorWhite));
            holder.time.setTextColor(context.getResources().getColor(R.color.colorWhite));

        } else {
            holder.view.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
            holder.content.setTextColor(context.getResources().getColor(R.color.colorBlack));
            holder.time.setTextColor(context.getResources().getColor(R.color.colorBlack));
        }
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        AppCompatTextView content;
        AppCompatTextView time;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView.findViewById(R.id.item_note_view);
            content = view.findViewById(R.id.item_note_content);
            time = view.findViewById(R.id.item_note_time);
        }
    }

    public boolean isSelectMode() {
        return isSelectMode;
    }

    public int getSelectNum() {
        List<Note> list = getSelectList();
        return list.size();
    }

    public List<Note> getSelectList() {
        List<Note> list = new ArrayList<>();
        for (Note note : noteList) {
            if (note.isSelect()) {
                list.add(note);
            }
        }
        return list;
    }

    public void closeSelectMode() {
        isSelectMode = false;
        for (Note note : noteList) {
            note.setSelect(false);
        }
        notifyDataSetChanged();
    }

    public void selectAll() {
        for (Note note : noteList) {
            if (!note.isSelect()) {
                note.setSelect(true);
            }
        }
        notifyDataSetChanged();
    }

    public interface OnEventListener {
        void onLongClick();

        void onClick();
    }

    public interface OnLongClickDeleteListener {
        void onDelete(Note note);
    }

}
