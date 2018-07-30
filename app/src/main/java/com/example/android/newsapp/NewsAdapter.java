package com.example.android.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NewsAdapter extends ArrayAdapter<NewsArticle> {

    /**
     * Returns the formatted date string (i.e. "Jul 28, 2018") from a Date Object.
     */
    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

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

        TextView titleTextView = (TextView) convertView.findViewById(R.id.article_title);
        titleTextView.setText(currentNewsArticle.getNewsTitle());

        TextView sectionTextView = (TextView) convertView.findViewById(R.id.article_section);
        sectionTextView.setText(currentNewsArticle.getNewsSection());

        TextView authorTextView = (TextView) convertView.findViewById(R.id.article_author);
        if (currentNewsArticle.getNewsAuthor() != "") {
            authorTextView.setText(currentNewsArticle.getNewsAuthor());

            authorTextView.setVisibility(View.VISIBLE);
        } else {
            authorTextView.setVisibility(View.GONE);
        }

        Date dateObject = new Date(currentNewsArticle.getNewsDate());
        TextView dateTextView = (TextView) convertView.findViewById(R.id.article_date);
        String formattedDate = formatDate(dateObject);
        dateTextView.setText(formattedDate);

        return convertView;
    }
}
