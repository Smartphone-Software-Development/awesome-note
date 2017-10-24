package com.bfd.note;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bfd.note.store.Container;

class ListAdapter extends ArrayAdapter<String> {

    private final LayoutInflater inflater;
    private final Resources res;
    private final int itemLayoutRes;
    private final Container container;

    public ListAdapter(Context context, int itemLayoutRes, Container container) {
        super(context, itemLayoutRes, R.id.text, container.allNoteContents());
        inflater = LayoutInflater.from(context);
        res = context.getResources();
        this.container = container;
        this.itemLayoutRes = itemLayoutRes;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(itemLayoutRes, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), EditorActivity.class);
                    getContext().startActivity(intent);
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