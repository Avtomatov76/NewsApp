package com.example.android.newsapp;

import android.nfc.Tag;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public final class QueryUtils {

    // Tag for the log messages
    public static final String TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
    }

    public static List<NewsArticle> fetchNewsArticleData(String requestUrl) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ie) {
            Log.e(TAG, "fetchNewsArticleData: Interrupted", ie);
        }

        // Create URL
        URL newsArticleUrl = createUrl(requestUrl);

        // Perform an HTTP request
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(newsArticleUrl);
        } catch (IOException ioe) {
            Log.e(TAG, "fetchNewsArticleData: Problem making and HTTP request", ioe);
        }

        // Extracting news data from JSON response
        List<NewsArticle> currentNews = extractNewsFromJson(jsonResponse);
        return currentNews;
    }

    public static List<NewsArticle> extractNewsFromJson(String jsonResponse) {
        String newsTitle, newsSection, newsDate, url;

        // Check if JSON String is null
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }

        // Create an empty ArrayList that we can add News Articles to.
        List<NewsArticle> currentNews = new ArrayList<>();
        try {
            JSONObject baseJasonResponse = new JSONObject(jsonResponse);

            JSONObject baseJasonResponseResult = baseJasonResponse.getJSONObject("response");

            JSONArray currentArticlesArray = baseJasonResponseResult.getJSONArray("results");

            // Create items from an array of current News Articles.
            for (int i = 0; i < currentArticlesArray.length(); i++) {

                JSONObject currentNewsArticle = currentArticlesArray.getJSONObject(i);

                newsTitle = currentNewsArticle.getString("webTitle");

                newsSection = currentNewsArticle.getString("sectionName");

                newsDate = currentNewsArticle.getString("webPublicationDate");

                url = currentNewsArticle.getString("webUrl");

                String newsAuthor = "";

                JSONArray tagsArray = currentNewsArticle.getJSONArray("tags");
                // Loop through the array that has author's name
                for (int a = 0; a < tagsArray.length(); a++) {
                    JSONObject currentAuthor = tagsArray.getJSONObject(a);

                     newsAuthor = currentAuthor.getString("webTitle");
                }

                NewsArticle newsArticle = new NewsArticle(newsTitle, newsSection, newsAuthor, newsDate, url);
                currentNews.add(newsArticle);
            }
        } catch (JSONException je) {
            Log.e(TAG, "extractNewsFromJson: Problem parsing JSON news results", je);
        }
        return currentNews;
    }

    private static String makeHttpRequest(URL newsArticleUrl) throws IOException {
        String jsonResponse = "";
        // Check for null
        if (newsArticleUrl == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        // Create connection
        try {
            urlConnection = (HttpURLConnection) newsArticleUrl.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(1500);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            // Check for the response
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(TAG, "makeHttpRequest: " + urlConnection.getResponseCode());
            }
        } catch (IOException ioe) {
            Log.e(TAG, "makeHttpRequest: Problem retrieving JSON news results", ioe);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static URL createUrl(String requestUrl) {
        URL url = null;
        try {
            url = new URL(requestUrl);
        } catch (MalformedURLException mue) {
            Log.e(TAG, "createUrl: Problem creating URL", mue);
        }
        return url;
    }


}
