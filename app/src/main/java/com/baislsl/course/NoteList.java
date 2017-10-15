package com.baislsl.course;

import android.app.ListActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class NoteList extends ListActivity {

    private String[] titles = new String[]{
            "bai", "a", "b"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_note_list);
        setListAdapter(new ArrayAdapter<>(
                this, R.layout.activity_list_item, android.R.id.text1, titles
        ));

    }


}
