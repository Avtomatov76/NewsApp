package com.example.android.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<NewsArticle>> {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    // URL for news article data from The Guardian's database
    public static final String GUARDIAN_REQUEST_URL =
            "https://content.guardianapis.com/search";

    // API Student Key constant
    private static final String API_KEY = "d6910e14-b575-4c24-b407-03f1357beab0";

    // Constant value for the news loader ID
    private static final int NEWS_LOADER_ID = 1;

    // Adapter for the list of news articles
    private NewsAdapter mAdapter;

    private TextView noDataViewTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView newsListView = (ListView) findViewById(R.id.list);

        noDataViewTextView = findViewById(R.id.no_data_view);
        newsListView.setEmptyView(noDataViewTextView);

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

        // Call the ConnectivityManager to check network connectivity state
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Getting details on the currently active data network
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        // Get data if there is a network connection
        if (networkInfo != null && networkInfo.isConnected()) {

            // Get a reference to the LoaderManager in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader.
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        } else {
            // If there is no connection, display an error
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            // Set the No Data TextView with an error message "No Internet Connection"
            noDataViewTextView.setText(R.string.no_internet_connection);
        }
    }

    @Override
    public Loader<List<NewsArticle>> onCreateLoader(int i, Bundle bundle) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        // getString retrieves a String value from the preferences.  The 2nd parameter is the default value for this preference.
        String numberOfArticles = sharedPrefs.getString(
                getString(R.string.settings_number_of_articles_key),
                getString(R.string.settings_number_of_articles_default));

        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default));

        // parse breaks apart the Uri string that's passed into its parameter
        Uri baseUri = Uri.parse(GUARDIAN_REQUEST_URL);

        //buildUpon prepares teh baseUri that we just parsed so we can add query parameters to it.
        Uri.Builder uriBuilder = baseUri.buildUpon();

        // Append query parameter and its value.
        uriBuilder.appendQueryParameter("q", orderBy);
        uriBuilder.appendQueryParameter("show-fields", "thumbnail");
        uriBuilder.appendQueryParameter("show-tags", "contributor");
        uriBuilder.appendQueryParameter("page-size", numberOfArticles);
        uriBuilder.appendQueryParameter("order-by", orderBy);
        uriBuilder.appendQueryParameter("api-key", API_KEY);

        // Log that return the build URI String for trouble-shooting purposes.
        Log.i(LOG_TAG, "Built uri: " + uriBuilder);
        // Create a new loader for a given URL
        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<NewsArticle>> loader, List<NewsArticle> currentNews) {
        // Stop loading indicator after the data is loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // Set noDataTextView to display "NO News Articles Found"
        noDataViewTextView.setText(R.string.no_data);
        // Clear the adapter from previous news data
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

    @Override
    // This method initializes the contents of the Activity's options menu
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    // This method is called whenever an item in the options menu is selected
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
