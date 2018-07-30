package com.example.android.newsapp;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<NewsArticle>> {


    private static final String TAG = MainActivity.class.getSimpleName();

    // URL for news article data from The Guardian's database
    public static final String GUARDIAN_REQUEST_URL =
            "https://content.guardianapis.com/search?show-fields=thumbnail&show-tags=contributor&tag=politics/politics&order-by=newest&from-date=2018-06-10&api-key=test";

    // Constant value for the news loader ID
    private static final int NEWS_LOADER_ID = 1;

    // Adapter for the list of news articles
    private NewsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView newsListView = (ListView) findViewById(R.id.list);

        mAdapter = new NewsAdapter(this, new ArrayList<NewsArticle>());

        newsListView.setAdapter(mAdapter);

        // Set and item click listener (when a ListView is clicked) with the intent of opening
        // a website with more info on a specific new article.
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current news article that was clicked
                NewsArticle currentNewArticle = mAdapter.getItem(position);

                // Convert the String URL into a URI object to pass into the Intent constructor
                Uri newsUri = Uri.parse(currentNewArticle.getUrl());

                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);

                startActivity(websiteIntent);
            }
        });

        // Get a reference to the LoaderManager in order to interact with loaders.
        LoaderManager loaderManager = getLoaderManager();

        // Initialize the loader.
        loaderManager.initLoader(NEWS_LOADER_ID, null, this);
    }

    @Override
    public Loader<List<NewsArticle>> onCreateLoader(int i, Bundle bundle) {
        // Create a new loader for a given URL
        return new NewsLoader(this, GUARDIAN_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<NewsArticle>> loader, List<NewsArticle> currentNews) {
        // Clear teh adapter from previous news data
        mAdapter.clear();

        // If there is a valid list of {@link NewsArticle}s, then add them to the adapter's
        // data set.
        if (currentNews != null && !currentNews.isEmpty()) {
            mAdapter.addAll(currentNews);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<NewsArticle>> loader) {
        // Loader reset, so that we can clear out existing data.
        mAdapter.clear();
    }
}
