package com.example.android.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * A custom adapter that creates a list item for each news article
 * It take in the data from {@link NewsArticle} objects.
 */
public class NewsAdapter extends ArrayAdapter<NewsArticle> {

    // A custom NewsAdapter constructor
    public NewsAdapter(Context context, ArrayList<NewsArticle> newsArticles) {
        super(context, 0, newsArticles);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,
                    parent, false);
        }

        NewsArticle currentNewsArticle = getItem(position);

        // Initiating the views and setting appropriate data on them
        TextView titleTextView = (TextView) convertView.findViewById(R.id.article_title);
        titleTextView.setText(currentNewsArticle.getNewsTitle());

        TextView sectionTextView = (TextView) convertView.findViewById(R.id.article_section);
        sectionTextView.setText(currentNewsArticle.getNewsSection());

        // Setting the author's name on the view if there is one
        // Otherwise it sets visibility to "GONE"
        TextView authorTextView = (TextView) convertView.findViewById(R.id.article_author);
        if (currentNewsArticle.getNewsAuthor() != "") {
            authorTextView.setText(currentNewsArticle.getNewsAuthor());

            authorTextView.setVisibility(View.VISIBLE);
        } else {
            authorTextView.setVisibility(View.GONE);
        }

        TextView dateTextView = (TextView) convertView.findViewById(R.id.article_date);
        dateTextView.setText(currentNewsArticle.getNewsDate());

        TextView urlTextView = (TextView) convertView.findViewById(R.id.article_url);
        urlTextView.setText(currentNewsArticle.getUrl());

        // Displaying a list item with its corresponding data
        return convertView;
    }
}
