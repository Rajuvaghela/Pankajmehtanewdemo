package com.pankajmehtanewdemo.Model;

/**
 * Created by raju on 26-07-2017.
 */

public class Newsmodel {
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    String date;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    String title;
    String imageuri;

    public String getImageuri() {
        return imageuri;
    }

    public void setImageuri(String imageuri) {
        this.imageuri = imageuri;
    }

    public String getXtdesc() {
        return xtdesc;
    }

    public void setXtdesc(String xtdesc) {
        this.xtdesc = xtdesc;
    }

    String xtdesc;
}
