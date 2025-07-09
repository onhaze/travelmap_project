// MainActivity.java
package com.example.travelmap;

import android.os.Bundle;
import com.google.android.gms.maps.GoogleMap;
import android.content.Intent;
import com.example.travelmap.model.GeoRegion;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import com.example.travelmap.fragment.BucketListFragment;
import com.example.travelmap.fragment.GalleryFragment;
import com.example.travelmap.ReviewActivity;
import com.example.travelmap.ReviewListActivity;
import com.example.travelmap.fragment.MapFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private Map<Polygon, GeoRegion> polygonRegionMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        // 초기 화면 → MapFragment 표시
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, new MapFragment())
                    .commit();
        }

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selected = null;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_map) {
                selected = new MapFragment();
            } else if (itemId == R.id.nav_gallery) {
                selected = new GalleryFragment();
            } else if (itemId == R.id.nav_bucketlist) {
                selected = new BucketListFragment();
            }

            if (selected != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout, selected)
                        .commit();
            }

            return true;
        });

    }
    protected void onResume() {
        super.onResume();

        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frame_layout);
        if (currentFragment instanceof com.example.travelmap.fragment.MapFragment) {
            ((com.example.travelmap.fragment.MapFragment) currentFragment).refreshMap();
        }
    }

    public void onMapReady(@NonNull GoogleMap googleMap) {

        GoogleMap mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(36.5, 127.8), 7));
        drawRegions();

        mMap.setOnPolygonClickListener(polygon -> {
            GeoRegion region = polygonRegionMap.get(polygon);
            if (region != null) {
                RegionMenuDialog dialog = new RegionMenuDialog(MainActivity.this, region, new RegionMenuDialog.MenuListener() {
                    @Override
                    public void onRegisterReview() {
                        Intent intent = new Intent(MainActivity.this, ReviewActivity.class);
                        intent.putExtra("region_code", region.getCode());
                        intent.putExtra("region_name", region.getName());
                        startActivity(intent);
                    }

                    @Override
                    public void onViewReviews() {
                        Intent intent = new Intent(MainActivity.this, ReviewListActivity.class);
                        intent.putExtra("region_code", region.getCode());
                        startActivity(intent);
                    }

                    @Override
                    public void onCancel() {
                        // Do nothing
                    }
                });
                dialog.show();
            }
        });

    }

    private void drawRegions() {
        if (!(getSupportFragmentManager().findFragmentById(R.id.frame_layout) instanceof MapFragment)) {
            return;
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.frame_layout);
        if (mapFragment == null) {
            mapFragment.getMapAsync((OnMapReadyCallback) this);
        }

    }


}
