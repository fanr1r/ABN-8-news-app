package com.fanrir.news;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Eisdrachl on 04.07.2016.
 */
public class NewsAdapter extends ArrayAdapter<News> {

    public NewsAdapter(Activity context, ArrayList<News> news) {
        super(context, 0, news);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        News news = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item_news, parent, false);
        }

        TextView miwokTextView = (TextView) convertView.findViewById(R.id.title_text_view);
        miwokTextView.setText(news.getTitle());

        TextView translationTextView = (TextView) convertView.findViewById(R.id.section_text_view);
        translationTextView.setText(news.getSection());

        return convertView;
    }
}
