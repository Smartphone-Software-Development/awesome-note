package com.bfd.note.map;

public interface LocationDetector {
    double getLongitude();

    double getLatitude();

    void onClose();

    void onStop();
}
