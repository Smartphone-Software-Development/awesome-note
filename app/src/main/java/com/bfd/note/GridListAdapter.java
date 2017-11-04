package com.bfd.note;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bfd.note.store.Container;
import com.bfd.note.util.Note;

import static com.bfd.note.EditorActivity.EDIT_CONTENT;
import static com.bfd.note.EditorActivity.EDIT_ID;
import static com.bfd.note.EditorActivity.IS_ADD;

class GridListAdapter extends ArrayAdapter<String> {
    private static final String TAG = "GridListAdapter";
    private final LayoutInflater inflater;
    private final Resources res;
    private final int itemLayoutRes;
    private final Container container;
    private final AppCompatActivity parentActivity;

    public GridListAdapter(AppCompatActivity parentActivity, int itemLayoutRes, Container container) {
        super(parentActivity, itemLayoutRes, R.id.text, container.allNoteContents());
        inflater = LayoutInflater.from(parentActivity);
        res = parentActivity.getResources();
        this.container = container;
        this.itemLayoutRes = itemLayoutRes;
        this.parentActivity = parentActivity;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final ViewHolder holder;
        final Note note = container.getAllNotes().get(position);

        if (convertView == null) {
            convertView = inflater.inflate(itemLayoutRes, null);
            holder = new ViewHolder(convertView);
            holder.text.setBackgroundColor(res.getColor(R.color.colorPrimaryDark));
            holder.setNote(note);
            convertView.setTag(holder);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(parentActivity, EditorActivity.class)
                            .putExtra(EDIT_CONTENT, holder.getNote().getContent())
                            .putExtra(EDIT_ID, holder.getNote().getId())
                            .putExtra(IS_ADD, false);

                    Log.i(TAG, "onClick: note id = " + note.getId());
                    parentActivity.startActivityForResult(intent, MainActivity.EDIT_RESULT);
                }
            });
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.setNote(note);
        holder.text.setText(note.getShortContent());
        return convertView;
    }

    static class ViewHolder {
        final TextView text;
        private Note note;

        ViewHolder(View view) {
            text = (TextView) view.findViewById(R.id.text);
        }

        public void setNote(Note note) {
            this.note = note;
        }

        public Note getNote() {
            return note;
        }
    }
}