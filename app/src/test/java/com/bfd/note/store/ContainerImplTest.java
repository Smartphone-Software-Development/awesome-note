package com.bfd.note.store;

import org.junit.Test;
import org.litepal.crud.DataSupport;

import static org.junit.Assert.*;
import com.bfd.note.store.Container;
import com.bfd.note.util.Note;
import com.bfd.note.store.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ryan on 2017/11/3.
 */
public class ContainerImplTest {

    Container container = new Container() {
        @Override
        public int getNoteNumber() {
            return 0;
        }

        @Override
        public List<Note> getAllNotes() {
            return null;
        }

        @Override
        public Note getNoteItem(long id) {
            return null;
        }

        @Override
        public boolean addNote(Note data) {
            return false;
        }

        @Override
        public boolean moveNote(long id) {
            return false;
        }

        @Override
        public boolean resetNote(long id, Note note) {
            return false;
        }

        @Override
        public boolean resetNote(long id, String content) {
            return false;
        }

        @Override
        public String[] allNoteContents() {
            return new String[0];
        }
    };
    Query query = new Query() {
        @Override
        public List<Long> query(String keyWord) {

            List<Note> allNote;
            allNote = DataSupport.findAll(Note.class);

            List<Long> selectIndex = new ArrayList<>();
            for(int i = 0; i < allNote.size(); i++){
                long index;
                if((index = allNote.get(i).getContent().indexOf(keyWord)) >= 0){
                    selectIndex.add(index);
                }
            }

            if(selectIndex.size() > 0){
                return selectIndex;
            }

            return null;
        }
    };

    @Test
    public void queryIsCorrect() throws Exception{
        Note testNote1 = new Note("hello");
        Note testNote2 = new Note("world");




    }
}