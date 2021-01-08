package net.micode.notes.data.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

public class Note extends LitePalSupport implements Serializable {
    private int id;
    private String content;
    private long added;
    private long updated;
    private boolean haveClock;
    private long clock;

    @Column(ignore = true)
    private boolean isSelect;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getAdded() {
        return added;
    }

    public void setAdded(long added) {
        this.added = added;
    }

    public long getUpdated() {
        return updated;
    }

    public void setUpdated(long updated) {
        this.updated = updated;
    }

    public boolean isHaveClock() {
        return haveClock;
    }

    public void setHaveClock(boolean haveClock) {
        this.haveClock = haveClock;
    }

    public long getClock() {
        return clock;
    }

    public void setClock(long clock) {
        this.clock = clock;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    @NonNull
    @Override
    public String toString() {
        return "id:" + id + "content:" + content + "added:" + added;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof Note) {
            Note note = (Note) obj;
            return note.id == this.id;
        }
        return false;
    }
}
