package com.sust.bookshelf;

import java.io.Serializable;

public class ExistingBooklist implements Serializable {
    String listname;

    public ExistingBooklist() {
    }

    public String getListname() {
        return listname;
    }

    public void setListname(String listname) {
        this.listname = listname;
    }

    public ExistingBooklist(String listname) {
        this.listname = listname;
    }
}
