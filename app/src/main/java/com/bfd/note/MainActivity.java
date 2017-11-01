package com.bfd.note;

import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bfd.note.store.Container;
import com.bfd.note.store.ContainerImpl;
import com.bfd.note.util.Note;
import com.twotoasters.jazzylistview.JazzyGridView;
import com.twotoasters.jazzylistview.JazzyHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

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
    private ListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);
        ButterKnife.bind(this);
        container = createConnection();
        listAdapter = new ListAdapter(this, R.layout.grid_item, container);
        mGrid.setAdapter(listAdapter);

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

    private Container createConnection() {
        return ContainerImpl.getContainer();
    }

    private void setToolBar() {
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.toolbar_background));
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.sublime_text2));
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
        Toast.makeText(this, Integer.toString(item.getItemId()), Toast.LENGTH_SHORT).show();
        switch (item.getItemId()) {
            case R.id.settings:
                intent = new Intent(MainActivity.this, SettingsActivity.class);
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
        if (resultCode == RESULT_OK) {
            listAdapter = new ListAdapter(this, R.layout.grid_item, container);
        }

        mGrid.setAdapter(listAdapter);
    }
}
