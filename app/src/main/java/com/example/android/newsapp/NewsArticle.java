package com.example.android.newsapp;

// NewsArticle object that defines the parameters of a single NewArticle object.
public class NewsArticle {
    private String newsTitle, newsSection, newsAuthor, newsDate, mUrl;

    // NewsArticle constructor that takes in specified parameters.
    public NewsArticle(String newsTitle, String newsSection, String newsAuthor, String newsDate, String url) {
        this.newsTitle = newsTitle;
        this.newsSection = newsSection;
        this.newsAuthor = newsAuthor;
        this.newsDate = newsDate;
        this.mUrl = url;
    }

    // Returning corresponding parameters needed for a NewsArticle object creation.
    public String getNewsTitle() {
        return newsTitle;
    }

    public String getNewsSection() {
        return newsSection;
    }

    public String getNewsAuthor() {
        return newsAuthor;
    }

    public String getNewsDate() {
        return newsDate;
    }

    public String getUrl() {
        return mUrl;
    }
}
