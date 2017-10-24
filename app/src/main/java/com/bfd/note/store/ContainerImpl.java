package com.bfd.note.store;

import com.bfd.note.Note;

public class ContainerImpl implements Container {

    // TODO
    private Note[] testContent = {
            new Note("one"),
            new Note("two"),
            new Note("three")
    };

    @Override
    public int getNoteNumber() {
        return testContent.length;
    }

    @Override
    public Note[] getAllNotes() {
        return testContent;
    }

    @Override
    public Note getNoteItem(int index) {
        return testContent[index];
    }

    @Override
    public boolean addNote(Note data) {
        return false;
    }

    @Override
    public boolean moveNote(int index) {
        return false;
    }

    @Override
    public boolean resetNote(int index, Note note) {
        return false;
    }

    @Override
    public String[] allNoteContents() {
        String[] ans = new String[getNoteNumber()];
        for(int i= 0;i<ans.length;i++){
            ans[i] = getNoteItem(i).getContent();
        }
        return ans;
    }
}
