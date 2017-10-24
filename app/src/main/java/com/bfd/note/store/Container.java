package com.bfd.note.store;

import com.bfd.note.Note;

public interface Container {
    int getNoteNumber();

    Note[] getAllNotes();

    Note getNoteItem(int index);

    boolean addNote(Note data);

    boolean moveNote(int index);

    boolean resetNote(int index, Note note);

    boolean resetNote(int index, String content);

    String[] allNoteContents();

}
