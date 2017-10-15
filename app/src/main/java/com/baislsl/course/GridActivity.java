package com.baislsl.course;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.twotoasters.jazzylistview.JazzyGridView;
import com.twotoasters.jazzylistview.JazzyHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GridActivity extends AppCompatActivity {

    private static final String KEY_TRANSITION_EFFECT = "transition_effect";

    @BindView(android.R.id.list) protected JazzyGridView mGrid;
    @BindView(R.id.toolbar) protected Toolbar toolbar;
    private Map<String, Integer> mEffectMap;
    private int mCurrentTransitionEffect = JazzyHelper.HELIX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);
        ButterKnife.bind(this);

        setToolBar();
        mGrid.setAdapter(new ListAdapter(this, R.layout.grid_item));

        if (savedInstanceState != null) {
            mCurrentTransitionEffect = savedInstanceState.getInt(KEY_TRANSITION_EFFECT, JazzyHelper.HELIX);
            setupJazziness(mCurrentTransitionEffect);
        }
    }

    private void setToolBar(){
        toolbar.setBackgroundColor(getResources().getColor(R.color.toolbar_background));
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.sublime_text));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mEffectMap = buildEffectMap(this);
        populateEffectMenu(menu, new ArrayList<>(mEffectMap.keySet()), this);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String strEffect = item.getTitle().toString();
        Toast.makeText(this, strEffect, Toast.LENGTH_SHORT).show();
        setupJazziness(mEffectMap.get(strEffect));
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

    public static Map<String, Integer> buildEffectMap(Context context) {
        Map<String, Integer> effectMap = new LinkedHashMap<>();
        int i = 0;
        String[] effects = context.getResources().getStringArray(R.array.jazzy_effects);
        for (String effect : effects) {
            effectMap.put(effect, i++);
        }
        return effectMap;
    }
    public static void populateEffectMenu(Menu menu, List<String> effectNames, Context context) {
        Collections.sort(effectNames);
        effectNames.remove(context.getString(R.string.standard));
        effectNames.add(0, context.getString(R.string.standard));
        for (String effectName : effectNames) {
            menu.add(effectName);
        }
    }
}
