// GeoRegion.java
package com.example.travelmap.model;

import com.google.android.gms.maps.model.LatLng;
import java.util.List;

public class GeoRegion {
    private final String code;
    private final String name;
    private final List<LatLng> latLngs;

    public GeoRegion(String code, String name, List<LatLng> latLngs) {
        this.code = code;
        this.name = name;
        this.latLngs = latLngs;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public List<LatLng> getLatLngs() {
        return latLngs;
    }
}