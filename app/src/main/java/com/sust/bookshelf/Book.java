package com.sust.bookshelf;

import java.io.Serializable;

public class Book implements Serializable{
    String title;
    String author;
    String topReview;
    String parent;
    String imgurl;

    public Book(String title, String author, String topReview, String parent, String imgurl) {
        this.title = title;
        this.author = author;
        this.topReview = topReview;
        this.parent = parent;
        this.imgurl = imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getImgurl() {

        return imgurl;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public Book() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }


    public String getTopReview() {
        return topReview;
    }

    public void setTopReview(String topReview) {
        this.topReview = topReview;
    }


}
