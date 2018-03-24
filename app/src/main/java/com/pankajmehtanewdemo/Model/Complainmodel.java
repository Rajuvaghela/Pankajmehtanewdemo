package com.pankajmehtanewdemo.Model;

/**
 * Created by admin on 05-Jul-17.
 */
public class Complainmodel {
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    String code;
    String date;
    String status;
    int user;

    public void setuser(int user) {
        this.user = user;
    }
    public int getUser()
    {
        return this.user;
    }
}

