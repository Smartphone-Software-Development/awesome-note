package com.bfd.note;

import android.support.test.runner.AndroidJUnit4;

import com.bfd.note.util.Note;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class LocationTest {

    @Test
    public void varyLocationTest() throws Exception {
        int noteNumber = 6;
        double latitude = 40.0, longitude = 116.0, radius = 1;

        for (int i = 0; i < noteNumber; i++) {
            Note note = new Note();
            note.setContent("content " + i);
            note.setTitle("title " + i);
            note.setLongitude(longitude + radius * Math.cos(i / (2 * Math.PI)));
            note.setLatitude(latitude + radius * Math.sin(i / (2 * Math.PI)));
            note.save();
        }
    }

}
