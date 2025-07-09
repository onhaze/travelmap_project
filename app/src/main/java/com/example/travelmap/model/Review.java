// Review.java
package com.example.travelmap.model;

public class Review {

    private String regionCode;
    private String regionName;
    private String date;
    private float rating;
    private String photoUri;
    private String comment; // 한줄평 추가

    public Review(String regionCode, String regionName, String date, float rating, String photoUri, String comment) {
        this.regionCode = regionCode;
        this.regionName = regionName;
        this.date = date;
        this.rating = rating;
        this.photoUri = photoUri;
        this.comment = comment;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public String getRegionName() {
        return regionName;
    }

    public String getDate() {
        return date;
    }

    public float getRating() {
        return rating;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public String getComment() {
        return comment;
    }
}
