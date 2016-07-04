package com.fanrir.news;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    /**
     * List that stores the books
     */
    private NewsAdapter mNewsAdapter;

    /**
     * List that stores the news
     */
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //create news list where news will be stored
        ArrayList<News> newses = new ArrayList<>();

        //create Adapter for book list
        mNewsAdapter = new NewsAdapter(this, newses);

        // Get a reference to the ListView, and attach this adapter to it.
        mListView = (ListView) findViewById(R.id.news_list_view);
        View emptyView = findViewById(R.id.listview_news_empty);
        mListView.setEmptyView(emptyView);
        mListView.setAdapter(mNewsAdapter);

        // clicking on a story opens the story in the user’s browser.
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                News news = (News) adapterView.getItemAtPosition(position);

                //Start the news in the user’s browser
                Uri webpage = Uri.parse(news.getWebUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        Button refreshButton = (Button) findViewById(R.id.refresh_button);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateNews();
            }
        });

        updateNews();
    }

    private void updateNews() {
        FetchNewsTask bookListTask = new FetchNewsTask(this);
        bookListTask.execute();
    }

    public void refreshNewsList(ArrayList<News> result) {
        mNewsAdapter.clear();
        for (News news : result) {
            mNewsAdapter.add(news);
        }
    }
}
