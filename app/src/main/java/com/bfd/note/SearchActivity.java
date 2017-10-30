package com.bfd.note;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.bfd.note.store.Container;
import com.bfd.note.store.ContainerImpl;
import com.bfd.note.store.Query;
import com.bfd.note.util.Note;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity {
    private static final String TAG = "SearchActivity";
    @BindView(R.id.search)
    protected SearchView searchView;
    @BindView(R.id.search_result)
    protected ListView searchResultView;

    private Container container;
    private Query query;


    private List<Long> searchResult;

    private SearchView.OnQueryTextListener searchViewListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String text) {
            searchResult = query.query(text);
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            searchResult = query.query(newText);
            return false;
        }
    };

    private android.widget.ListAdapter adapter = new ArrayAdapter<String>(this,
            R.layout.search_item, R.id.search_result_text) {
        private final LayoutInflater inflater = LayoutInflater.from(getContext());

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.search_item, parent);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
//                convertView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//
////                        Intent intent = new Intent(parentActivity, EditorActivity.class)
////                                .putExtra(EDIT_CONTENT, note.getContent())
////                                .putExtra(EDIT_ID, note.getId());
//
//                        Log.i(TAG, "onClick: note id = " + note.getId());
////                        parentActivity.startActivityForResult(intent, MainActivity.EDIT_RESULT);
//                    }
//                });
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Long id = searchResult.get(position);
            Note note = container.getNoteItem(id);
            holder.text.setText(note.getContent().substring(0, 20));

            return convertView;
        }

        class ViewHolder {
            final TextView text;

            ViewHolder(View view) {
                text = (TextView) view.findViewById(R.id.text);
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        // TODO
        container = new ContainerImpl();
        query = new ContainerImpl();

        searchResultView.setAdapter(adapter);
        searchView.setOnQueryTextListener(searchViewListener);

    }


}
