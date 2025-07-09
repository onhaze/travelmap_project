// GeoUtils.java
package com.example.travelmap.util;

import android.content.Context;
import android.content.res.AssetManager;
import com.example.travelmap.model.GeoRegion;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.android.gms.maps.model.LatLng;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class GeoUtils {

    public static List<GeoRegion> loadGeoRegions(Context context) {
        List<GeoRegion> regionList = new ArrayList<>();
        try {
            AssetManager assetManager = context.getAssets();
            InputStream inputStream = assetManager.open("TL_SCCO_SIG.json");
            JsonObject jsonObject = JsonParser.parseReader(new InputStreamReader(inputStream)).getAsJsonObject();

            JsonArray features = jsonObject.getAsJsonArray("features");
            for (JsonElement element : features) {
                JsonObject feature = element.getAsJsonObject();

                String sigCd = feature.getAsJsonObject("properties").get("SIG_CD").getAsString();
                String sigKorNm = feature.getAsJsonObject("properties").get("SIG_KOR_NM").getAsString();

                JsonArray coordinates = feature.getAsJsonObject("geometry").getAsJsonArray("coordinates").get(0).getAsJsonArray();

                List<LatLng> latLngList = new ArrayList<>();
                for (JsonElement coordElement : coordinates) {
                    JsonArray coord = coordElement.getAsJsonArray();
                    double lng = coord.get(0).getAsDouble();
                    double lat = coord.get(1).getAsDouble();
                    latLngList.add(new LatLng(lat, lng));
                }

                GeoRegion region = new GeoRegion(sigCd, sigKorNm, latLngList);
                regionList.add(region);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return regionList;
    }
}