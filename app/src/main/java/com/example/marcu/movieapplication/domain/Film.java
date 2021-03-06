package com.example.marcu.movieapplication.domain;

/**
 * Created by Wallaard on 16-6-2017.
 */

public class Film {
    private String title;
    private int length;
    private int releaseyear;
    private String rating;
    private int inventoryid;
    private Boolean status;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean available) {
        this.status = available;
    }

    public int getInventoryid() {
        return inventoryid;
    }

    public void setInventoryid(int inventoryid) {
        this.inventoryid = inventoryid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getReleaseyear() {
        return releaseyear;
    }

    public void setReleaseyear(int releaseyear) {
        this.releaseyear = releaseyear;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}

