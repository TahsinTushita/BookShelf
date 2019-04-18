package com.sust.bookshelf;

public class Review {
    private String bookTitle;
    private String userName;

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    private String reviewText;

    public Review() {
    }

    public Review(String coverUrl, String bookTitle, String userName, String reviewText) {
        this.bookTitle = bookTitle;
        this.userName = userName;
        this.reviewText = reviewText;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }
}
