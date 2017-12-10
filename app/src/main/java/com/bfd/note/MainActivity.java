package com.bfd.note;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bfd.note.store.Container;
import com.bfd.note.store.ContainerImpl;
import com.bfd.note.store.MySynchronizer;
import com.bfd.note.util.Note;
import com.twotoasters.jazzylistview.JazzyGridView;
import com.twotoasters.jazzylistview.JazzyHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.bfd.note.EditorActivity.EDIT_CONTENT;
import static com.bfd.note.EditorActivity.EDIT_ID;
import static com.bfd.note.EditorActivity.IS_ADD;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final String KEY_TRANSITION_EFFECT = "transition_effect";
    public static final int EDIT_RESULT = 1;
    public static final int ADD_RESULT = 2;

    @BindView(android.R.id.list)
    protected JazzyGridView mGrid;
    @BindView(R.id.toolbar)
    protected Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    protected DrawerLayout mDrawLayout;


    private static final int DEFAULT_TRANSITION_EFFECT = JazzyHelper.HELIX;
    private int mCurrentTransitionEffect = DEFAULT_TRANSITION_EFFECT;
    private Container container;
    private GridListAdapter listAdapter;
    private static final Handler mHandler = new Handler();  // fetch result when upload or download

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);
        ButterKnife.bind(this);
        container = createConnection();
        listAdapter = new GridListAdapter(this, R.layout.grid_item, container);
        mGrid.setAdapter(listAdapter);

        findViewById(R.id.sync_upload_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MySynchronizer mysync = new MySynchronizer(MainActivity.this);
                mysync.syncDataToCloud("user", "pwd", mHandler);
            }
        });

        findViewById(R.id.sync_download_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: click download button");
                MySynchronizer mysync = new MySynchronizer(MainActivity.this);
                ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setTitle("Updating");
                progressDialog.setMessage("Loading ...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                Log.i(TAG, "onClick: show progress dialog");
                mysync.syncDataToLocal("user", "pwd", mHandler);
                progressDialog.dismiss();
                Log.i(TAG, "onClick: dismiss progress dialog");
                updateListAdapter();
                Log.i(TAG, "onClick: update list adapter");
            }
        });

        findViewById(R.id.add_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class)
                        .putExtra(IS_ADD, true);
                Log.i(TAG, "onOptionsItemSelected: list adapter count = " + listAdapter.getCount());
                startActivityForResult(intent, ADD_RESULT);
            }
        });
        setToolBar();
    }

    private void updateListAdapter(){
        listAdapter = new GridListAdapter(this, R.layout.grid_item, container);
        mGrid.setAdapter(listAdapter);
    }

    private Container createConnection() {
        return ContainerImpl.getContainer();
    }

    private void setToolBar() {
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.toolbar_background));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.main_settings:
                intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.main_search:
                intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
                return true;

        }
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_TRANSITION_EFFECT, mCurrentTransitionEffect);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case EDIT_RESULT:
                if (resultCode == RESULT_OK) {
                    long id = data.getLongExtra(EditorActivity.RESULT_ID, -1);
                    String content = data.getStringExtra(EditorActivity.RESULT_CONTENT);
                    Log.i(TAG, "onActivityResult: id = " + id);
                    Log.i(TAG, "onActivityResult: content = " + content);
                }
                break;
            case ADD_RESULT:
                if (resultCode == RESULT_OK) {
                    // do nothing
                }
                break;
            default:
        }

        // update list adapter
        Log.i(TAG, "onActivityResult: update container");
        if (resultCode == RESULT_OK) {
            updateListAdapter();
        }
    }

    private class GridListAdapter extends ArrayAdapter<String> {
        private static final String TAG = "GridListAdapter";
        private final LayoutInflater inflater;
        private final Resources res;
        private final int itemLayoutRes;
        private final Container container;

        GridListAdapter(AppCompatActivity parentActivity, int itemLayoutRes, Container container) {
            super(MainActivity.this, itemLayoutRes, R.id.text, container.allNoteContents());
            inflater = LayoutInflater.from(parentActivity);
            res = parentActivity.getResources();
            this.container = container;
            this.itemLayoutRes = itemLayoutRes;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
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

                        Intent intent = new Intent(MainActivity.this, EditorActivity.class)
                                .putExtra(EDIT_CONTENT, holder.getNote().getContent())
                                .putExtra(EDIT_ID, holder.getNote().getId())
                                .putExtra(IS_ADD, false);

                        Log.i(TAG, "onClick: note id = " + note.getId());
                        MainActivity.this.startActivityForResult(intent, MainActivity.EDIT_RESULT);
                    }
                });
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.setNote(note);
            holder.text.setText(note.getShortContent());
            return convertView;
        }

        class ViewHolder {
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
}
