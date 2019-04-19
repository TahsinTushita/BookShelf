package com.sust.bookshelf;

import java.io.Serializable;

public class BookMarks implements Serializable {
    String title,author,imgurl,pageno;

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

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getPageno() {
        return pageno;
    }

    public void setPageno(String pageno) {
        this.pageno = pageno;
    }

    public BookMarks() {
    }

    public BookMarks(String title, String author, String imgurl, String pageno) {
        this.title = title;
        this.author = author;
        this.imgurl = imgurl;
        this.pageno = pageno;
    }
}
