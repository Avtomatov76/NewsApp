package com.example.android.newsapp;

public class NewsArticle {
    private String newsTitle, newsSection, newsAuthor, newsDate, mUrl;

    public NewsArticle(String newsTitle, String newsSection, String newsAuthor, String newsDate, String url) {
        this.newsTitle = newsTitle;
        this.newsSection = newsSection;
        this.newsAuthor = newsAuthor;
        this.newsDate = newsDate;
        this.mUrl = url;
    }

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
