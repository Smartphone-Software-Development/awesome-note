package com.bfd.note.map;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.Marker;
import com.bfd.note.EditorActivity;
import com.bfd.note.util.Note;

import static com.bfd.note.EditorActivity.EDIT_CONTENT;
import static com.bfd.note.EditorActivity.EDIT_ID;
import static com.bfd.note.EditorActivity.IS_ADD;

public class NoteMarkerClickListener implements AMap.OnMarkerClickListener {
    private static final String TAG = "NoteMarkerClickListener";
    public static final int EDIT_RESULT = 1;
    private FragmentActivity mContext;  // enable start activity for result
    private AMap aMap;

    public NoteMarkerClickListener(FragmentActivity context, AMap aMap) {
        this.mContext = context;
        this.aMap = aMap;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Note note;
        if (aMap == null || (note = (Note) (marker.getObject())) == null) {
            Log.e(TAG, "onMarkerClick: Null note ");
            return false;
        }
        Intent intent = new Intent(mContext, EditorActivity.class)
                .putExtra(EDIT_CONTENT, note.getContent())
                .putExtra(EDIT_ID, note.getId())
                .putExtra(IS_ADD, false);
        mContext.startActivityForResult(intent, EDIT_RESULT);
        return true;
    }
}
