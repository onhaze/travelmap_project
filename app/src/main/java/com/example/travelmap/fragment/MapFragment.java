// MapFragment.java
package com.example.travelmap.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.travelmap.RegionMenuDialog;
import com.example.travelmap.model.GeoRegion;
import com.example.travelmap.storage.ReviewStorage;
import com.example.travelmap.util.GeoUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.example.travelmap.R;

import java.util.HashMap;
import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private List<GeoRegion> geoRegions;
    private HashMap<Polygon, GeoRegion> polygonRegionMap = new HashMap<>();
    private ReviewStorage reviewStorage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        reviewStorage = new ReviewStorage(requireContext());
        geoRegions = GeoUtils.loadGeoRegions(requireContext());

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Apply custom map style (white background)
        try {
            MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.map_style_white);
            mMap.setMapStyle(style);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(36, 127.8), 7));

        mMap.getUiSettings().setZoomControlsEnabled(true);

        drawRegions();

        mMap.setOnPolygonClickListener(polygon -> {
            GeoRegion region = polygonRegionMap.get(polygon);
            if (region != null) {
                RegionMenuDialog dialog = new RegionMenuDialog(requireContext(), region, new RegionMenuDialog.MenuListener() {
                    @Override
                    public void onRegisterReview() {
                        Intent intent = new Intent(requireContext(), com.example.travelmap.ReviewActivity.class);
                        intent.putExtra("region_code", region.getCode());
                        intent.putExtra("region_name", region.getName());
                        startActivity(intent);
                    }
                    @Override
                    public void onViewReviews() {
                        requireActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frame_layout, new GalleryFragment())
                                .commit();
                    }

                    @Override
                    public void onCancel() {
                    }
                });
                dialog.show();
            }
        });

    }

    private void drawRegions() {
        mMap.clear(); // 기존 Polygon 클리어

        for (GeoRegion region : geoRegions) {
            boolean hasReview = reviewStorage.hasReview(region.getCode());

            PolygonOptions options = new PolygonOptions()
                    .addAll(region.getLatLngs())
                    .strokeColor(getResources().getColor(R.color.black, null))
                    .strokeWidth(2f)
                    .fillColor(hasReview ? getResources().getColor(R.color.light_blue, null) : android.graphics.Color.TRANSPARENT)
                    .clickable(true);

            Polygon polygon = mMap.addPolygon(options);
            polygonRegionMap.put(polygon, region);
        }
    }

    private LatLng getPolygonCenter(List<LatLng> latLngs) {
        double lat = 0;
        double lng = 0;
        for (LatLng point : latLngs) {
            lat += point.latitude;
            lng += point.longitude;
        }
        int size = latLngs.size();
        return new LatLng(lat / size, lng / size);
    }

    public void refreshMap() {
        if (mMap != null) {
            drawRegions();
        }
    }

}