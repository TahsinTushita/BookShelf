package com.sust.bookshelf;

public class Users {

    String username;
    int availability;
    int shareaddress;

    public void setAvailability(int availability) {
        this.availability = availability;
    }

    public int getAvailability() {
        return availability;
    }

    public int getShareaddress() {
        return shareaddress;
    }

    public void setShareaddress(int shareaddress) {
        this.shareaddress = shareaddress;
    }

    public Users(String username, int availability, int shareaddress) {
        this.username = username;
        this.availability = availability;
        this.shareaddress = shareaddress;
    }

    public Users(){

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


}
