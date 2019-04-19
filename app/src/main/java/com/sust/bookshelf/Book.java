package com.sust.bookshelf;

import java.io.Serializable;

public class Book implements Serializable{
    String title;
    String author;
    String topReview;
    String parent;
    String imgurl;
    String category;
    String publisher;
    String listRef;

    public Book(String title, String author, String topReview, String parent, String imgurl, String category, String publisher, String listRef) {
        this.title = title;
        this.author = author;
        this.topReview = topReview;
        this.parent = parent;
        this.imgurl = imgurl;
        this.category = category;
        this.publisher = publisher;
        this.listRef = listRef;
    }

    public String getListRef() {
        return listRef;
    }

    public void setListRef(String listRef) {
        this.listRef = listRef;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
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
