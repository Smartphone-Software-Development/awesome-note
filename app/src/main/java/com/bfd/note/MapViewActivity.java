package com.bfd.note;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.bfd.note.map.NoteMarkerClickListener;
import com.bfd.note.store.ContainerImpl;
import com.bfd.note.util.Note;

import java.util.List;

public class MapViewActivity extends AppCompatActivity {
    private AMap aMap;
    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState); // 此方法必须重写
        updateMarker();
    }

    private void updateMarker() {
        if (aMap == null) {
            aMap = mapView.getMap();
            aMap.setOnMarkerClickListener(new NoteMarkerClickListener(this, aMap));
            addMarkersToMap();// 往地图上添加marker
        } else {    // update marker info
            aMap.clear();
            addMarkersToMap();
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

        for (Note note : notes) {
            if (note.getLatitude() == 0.0 && note.getLongitude() == 0.0)
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case NoteMarkerClickListener.EDIT_RESULT:
                if (resultCode == RESULT_OK) {
                    updateMarker();
                }
            default:
                    // nothing
        }
    }
}
