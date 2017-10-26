package com.bfd.note.store;

import com.bfd.note.util.Note;

import java.util.List;

public interface Container {
    int getNoteNumber();

    List<Note> getAllNotes();

    Note getNoteItem(long id);

    boolean addNote(Note data);

    boolean moveNote(long id);

    boolean resetNote(long id, Note note);

    boolean resetNote(long id, String content);

    String[] allNoteContents();

}
