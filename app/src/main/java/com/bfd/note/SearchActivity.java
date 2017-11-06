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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.bfd.note.EditorActivity.EDIT_CONTENT;
import static com.bfd.note.EditorActivity.EDIT_ID;
import static com.bfd.note.EditorActivity.IS_ADD;

public class SearchActivity extends AppCompatActivity {
    private static final String TAG = "SearchActivity";
    private static final int EDIT_RESULT = 1;
    @BindView(R.id.search)
    protected SearchView searchView;
    @BindView(R.id.search_result)
    protected ListView searchResultView;

    private Container container;
    private Query query;
    private LayoutInflater inflater;


    private List<Long> searchResult = new ArrayList<>();

    private View.OnClickListener searchResultClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ViewHolder holder = (ViewHolder) v.getTag();
            long id = holder.getId();

            Intent intent = new Intent(SearchActivity.this, EditorActivity.class)
                    .putExtra(EDIT_CONTENT, container.getNoteItem(id).getContent())
                    .putExtra(EDIT_ID, id)
                    .putExtra(IS_ADD, false);
            startActivityForResult(intent, EDIT_RESULT);
        }
    };

    private SearchView.OnQueryTextListener searchViewListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String text) {
            Log.i(TAG, "onQueryTextSubmit: submit");
            searchResult = query.query(text);
            updateListAdapter();
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            Log.i(TAG, "onQueryTextChange: search text change");
            searchResult = query.query(newText);
            updateListAdapter();
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        container = ContainerImpl.getContainer();
        query = ContainerImpl.getQuery();
        inflater = LayoutInflater.from(this);
        searchView.setOnQueryTextListener(searchViewListener);

    }

    @SuppressWarnings("unchecked")
    private void updateListAdapter(){
        final List<String> shortResults = new ArrayList<>();
        for(Long id : searchResult){
            shortResults.add(container.getNoteItem(id).getShortContent());
        }

        searchResultView.setAdapter(new ArrayAdapter(this,
                R.layout.search_item, shortResults) {

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                ViewHolder holder;
                if (convertView == null) {
                    convertView = inflater.inflate(R.layout.search_item, null);
                    holder = new ViewHolder(convertView);
                    convertView.setTag(holder);
                    convertView.setOnClickListener(searchResultClickListener);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                Long id = searchResult.get(position);
                holder.text.setText(shortResults.get(position));
                holder.setId(id);
                return convertView;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case EDIT_RESULT:
                if(resultCode == RESULT_OK){
                    long id = data.getLongExtra(EditorActivity.RESULT_ID, -1);
                    String content = data.getStringExtra(EditorActivity.RESULT_CONTENT);
                    Log.i(TAG, "onActivityResult: id = " + id);
                    Log.i(TAG, "onActivityResult: content = " + content);
                }
        }
    }

    private class ViewHolder {
        final TextView text;
        long id;

        ViewHolder(View view) {
            text = (TextView) view.findViewById(R.id.search_result_text);
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }
    }

}
