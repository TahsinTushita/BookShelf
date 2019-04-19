package com.sust.bookshelf;

import java.io.Serializable;

public class BuyRequest implements Serializable {
    private String username;
    private String title;


    public void setStatus(long status) {
        this.status = status;
    }

    private long status;

    public long getStatus() {
        return status;
    }

    private String parent;

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public BuyRequest(String username, String title, long status) {
        this.username = username;
        this.title = title;
        this.status = status;
    }

    public BuyRequest() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBoookTitle() {
        return title;
    }

    public void setBookTitle(String title) {
        this.title = title;
    }
}
