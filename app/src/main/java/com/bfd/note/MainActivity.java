package com.bfd.note;

import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bfd.note.store.Container;
import com.bfd.note.store.ContainerImpl;
import com.twotoasters.jazzylistview.JazzyGridView;
import com.twotoasters.jazzylistview.JazzyHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final String KEY_TRANSITION_EFFECT = "transition_effect";
    public static final int EDIT_RESULT = 1;
    private Container container;

    @BindView(android.R.id.list)
    protected JazzyGridView mGrid;
    @BindView(R.id.toolbar)
    protected Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    protected DrawerLayout mDrawLayout;

    private static final int DEFAULT_TRANSITION_EFFECT = JazzyHelper.HELIX;
    private int mCurrentTransitionEffect = DEFAULT_TRANSITION_EFFECT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);
        ButterKnife.bind(this);
        container = createConnection();

        setToolBar();
        mGrid.setAdapter(new ListAdapter(this, R.layout.grid_item, container));

        if (savedInstanceState != null) {
            mCurrentTransitionEffect = savedInstanceState.getInt(KEY_TRANSITION_EFFECT, DEFAULT_TRANSITION_EFFECT);
            setupJazziness(mCurrentTransitionEffect);
        }
    }

    private Container createConnection() {
        // TODO:
        return new ContainerImpl();
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
        // TODO:
        Toast.makeText(this, Integer.toString(item.getItemId()), Toast.LENGTH_SHORT).show();
        switch (item.getItemId()) {
            case R.id.settings:
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);

        }
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_TRANSITION_EFFECT, mCurrentTransitionEffect);
    }

    private void setupJazziness(int effect) {
        mCurrentTransitionEffect = effect;
        mGrid.setTransitionEffect(mCurrentTransitionEffect);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case EDIT_RESULT:
                if (resultCode == RESULT_OK) {
                    int index = data.getIntExtra(EditorActivity.RESULT_INDEX, -1);
                    String content = data.getStringExtra(EditorActivity.RESULT_CONTENT);
                    container.resetNote(index, content);
                }

            default:
        }
    }
}