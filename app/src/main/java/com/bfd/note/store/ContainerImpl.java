package com.bfd.note.store;

import android.util.Log;

import com.bfd.note.util.Note;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * implement with LitePal
 */
public class ContainerImpl implements Container, Query {
    private static final String TAG = "ContainerImpl";

    public ContainerImpl() {
    }

    @Override
    public int getNoteNumber() {
        return DataSupport.count(Note.class);
    }

    @Override
    public List<Note> getAllNotes() {
        return DataSupport.findAll(Note.class);
    }

    @Override
    public Note getNoteItem(long id) {
        return DataSupport.find(Note.class, id);
    }

    @Override
    public boolean addNote(Note data) {
        return data.save();
    }

    @Override
    public boolean moveNote(long id) {
        int rowsAffected = DataSupport.delete(Note.class, id);
        return rowsAffected != 0;
    }

    @Override
    public boolean resetNote(long id, Note note) {
        int rowAffected = note.update(id);
        return rowAffected != 0;
    }

    @Override
    public boolean resetNote(long id, String content) {
        Note note = DataSupport.find(Note.class, id);
        note.setContent(content);
        return note.save();
    }

    @Override
    public String[] allNoteContents() {
        List<Note> notes = getAllNotes();
        String[] results = new String[notes.size()];
        for (int i = 0; i < notes.size(); i++) {
            results[i] = notes.get(i).getContent();
            Log.i(TAG, "allNoteContents: notes of index " + i + "  with id = " + notes.get(i).getId());
            Log.i(TAG, "allNoteContents: " + (DataSupport.find(Note.class, notes.get(i).getId()) == null));
        }
        return results;
    }

    @Override
    public List<Long> query(String keyWord) {
        // TODO
        return null;
    }
}
