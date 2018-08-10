package com.example.android.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

// This method loads a list of news articles on an background thread (AsyncTask).
public class NewsLoader extends AsyncTaskLoader<List<NewsArticle>> {
    // Tag for the log messages
    private static final String TAG = NewsLoader.class.getSimpleName();

    private String mUrl;

    /**
     * Constructs a new {@link NewsLoader}.
     *
     * @param context of the activity
     * @param url to load data from
     */
    public NewsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {forceLoad();}

    // Background thread
    @Override
    public List<NewsArticle> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // Perform network request, parse the response, and extract a list of news articles.
        List<NewsArticle> newsArticles = QueryUtils.fetchNewsArticleData(mUrl);
        return newsArticles;
    }
}
