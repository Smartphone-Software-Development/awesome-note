package com.bfd.note.util;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.model.LatLng;

public class GaodeMapLocation   {
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    private Context context;
    private volatile double latitude, longitude;

    public GaodeMapLocation(Context context) {
        this.context = context;
        init();
    }

    private AMapLocationListener listener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if(amapLocation == null || amapLocation.getErrorCode() != 0){
                latitude = longitude = 0.0;
                return;
            }
            latitude = amapLocation.getLatitude();
            longitude = amapLocation.getLongitude();
        }
    };

    private void init() {
        locationClient = new AMapLocationClient(context);
        locationClient.setLocationListener(listener);

        // 设置定位参数
        locationOption = new AMapLocationClientOption();
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        locationOption.setInterval(1000);
        locationOption.setNeedAddress(false);
        locationClient.setLocationOption(locationOption);

        // 启动定位
        locationClient.startLocation();
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }


}
