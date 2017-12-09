package com.bfd.note;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.bfd.note.store.ContainerImpl;
import com.bfd.note.util.Note;

import java.util.List;


import static com.bfd.note.EditorActivity.EDIT_CONTENT;
import static com.bfd.note.EditorActivity.EDIT_ID;
import static com.bfd.note.EditorActivity.IS_ADD;

public class MapViewActivity extends Activity implements AMap.OnMarkerClickListener {
    private AMap aMap;
    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState); // 此方法必须重写
        init();
    }

    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            addMarkersToMap();// 往地图上添加marker
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    private void addMarkersToMap() {
        List<Note> notes = ContainerImpl.getContainer().getAllNotes();

        for(Note note : notes){
            if(note.getLatitude() == 0.0 || note.getLongitude() == 0.0)
                continue; // no location information

            MarkerOptions markerOption = new MarkerOptions()
                    .icon(BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .position(new LatLng(note.getLatitude(), note.getLongitude()))
                    .title(note.getTitle())
                    .snippet(note.getShortContent())
                    .draggable(false);
            Marker marker = aMap.addMarker(markerOption);
            marker.setObject(note);
            marker.showInfoWindow();
        }
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        if (aMap != null) {
            Note note = (Note)(marker.getObject());
            Intent intent = new Intent(this, EditorActivity.class)
                    .putExtra(EDIT_CONTENT, note.getContent())
                    .putExtra(EDIT_ID, note.getId())
                    .putExtra(IS_ADD, false);
            startActivity(intent);
        }
        return true;
    }
}
