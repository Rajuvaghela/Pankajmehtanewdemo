package com.pankajmehtanewdemo.Model;

/**
 * Created by admin on 29-Jul-17.
 */
public class ItemData {

    String text;
    String imageId;

    public String getTt_id() {
        return tt_id;
    }

    public void setTt_id(String tt_id) {
        this.tt_id = tt_id;
    }

    String tt_id;

    public ItemData(String text, String imageId, String tt_id) {
        this.text = text;
        this.imageId = imageId;
        this.tt_id = tt_id;
    }

    public String getText() {
        return text;
    }

    public String getImageId() {
        return imageId;
    }
}