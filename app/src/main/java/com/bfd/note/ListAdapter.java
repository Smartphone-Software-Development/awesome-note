package com.bfd.note;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bfd.note.store.Container;

class ListAdapter extends ArrayAdapter<String> {
    public final static String EDIT_CONTENT = "edit content";
    public final static String EDIT_INDEX = "edit index";
    private final LayoutInflater inflater;
    private final Resources res;
    private final int itemLayoutRes;
    private final Container container;
    private final AppCompatActivity parentActivity;

    public ListAdapter(AppCompatActivity parentActivity, int itemLayoutRes, Container container) {
        super(parentActivity, itemLayoutRes, R.id.text, container.allNoteContents());
        inflater = LayoutInflater.from(parentActivity);
        res = parentActivity.getResources();
        this.container = container;
        this.itemLayoutRes = itemLayoutRes;
        this.parentActivity = parentActivity;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(itemLayoutRes, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(parentActivity, EditorActivity.class);
                    intent.putExtra(EDIT_CONTENT, container.getNoteItem(position).getContent())
                            .putExtra(EDIT_INDEX, position);
                    parentActivity.startActivityForResult(intent, MainActivity.EDIT_RESULT);
                }
            });
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.text.setBackgroundColor(res.getColor(R.color.colorPrimaryDark));

        holder.text.setText(container.getNoteItem(position).getContent());



        return convertView;
    }

    static class ViewHolder {
        final TextView text;

        ViewHolder(View view) {
            text = (TextView) view.findViewById(R.id.text);
        }
    }
}