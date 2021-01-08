package net.micode.notes.data.db;

import net.micode.notes.data.model.Note;

import org.litepal.LitePal;

import java.util.List;

public class NoteDBOpenHelper {

    public static boolean save(Note save) {
        return save.save();
    }

    public static Note save(String content) {
        Note note = new Note();
        note.setContent(content);
        note.setAdded(System.currentTimeMillis());
        note.setUpdated(System.currentTimeMillis());
        note.setHaveClock(false);
        note.setClock(0);
        note.save();
        return note;
    }

    public static boolean update(Note update) {
        Note note = LitePal.find(Note.class, update.getId());
        note.setContent(update.getContent());
        note.setHaveClock(update.isHaveClock());
        note.setClock(update.getClock());
        note.setUpdated(System.currentTimeMillis());
        return note.save();
    }

    public static void update(int id, String content) {
        Note note = new Note();
        note.setContent(content);
        note.setUpdated(System.currentTimeMillis());
        note.update(id);
    }

    public static void delete(int id) {
        int row = LitePal.delete(Note.class, id);
    }

    public static void deleteAll() {
        LitePal.deleteAll(Note.class);
    }

    public static Note query(int id) {
        return LitePal.find(Note.class, id);
    }

    public static List<Note> queryAllForNote(Note note) {
        return LitePal.where(
                "content like ? and added = ?", note.getContent(), String.valueOf(note.getAdded()))
                .find(Note.class);
    }

    public static List<Note> queryAll() {
        return LitePal.findAll(Note.class);
    }

    public static List<Note> queryAllForContent(String content) {
        return LitePal.where("content like ?", content)
                .find(Note.class);
    }

}
