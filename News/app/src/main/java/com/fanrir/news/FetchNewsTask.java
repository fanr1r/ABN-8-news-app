package com.fanrir.news;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;

/**
 * Created by Eisdrachl on 04.07.2016.
 */
public class FetchNewsTask extends AsyncTask<String, Void, ArrayList<News>> {

    public MainActivity mMainActivity;

    private final String LOG_TAG = FetchNewsTask.class.getSimpleName();

    public FetchNewsTask(MainActivity mainActivity) {
        mMainActivity = mainActivity;
    }

    /**
     * Takes the String representing the complete news list in JSON Format and
     * pull out the data we need to construct the Strings needed for the news list.
     */
    private ArrayList<News> getNewsDataFromJson(String newsJsonStr)
            throws JSONException {

        // The newsList to fill with results
        ArrayList<News> newsList = new ArrayList();

        // The key to pass on the JSON Object
        final String NEWS_RESPONSE = "response";
        final String NEWS_RESULTS = "results";
        final String NEWS_TITLE = "webTitle";
        final String NEWS_SECTION = "sectionName";
        final String NEWS_URL = "webUrl";

        try {
            JSONObject newsJson = new JSONObject(newsJsonStr);
            JSONObject newsResponse = newsJson.getJSONObject(NEWS_RESPONSE);
            JSONArray itemsArray = newsResponse.getJSONArray(NEWS_RESULTS);

            for (int i = 0; i < itemsArray.length(); i++) {

                // These are the values that will be collected.
                String title = "";
                String section = "";
                String webUrl = "";

                // Get the JSON object representing a news
                JSONObject newsResult = itemsArray.getJSONObject(i);

                title = newsResult.getString(NEWS_TITLE);
                section = newsResult.getString(NEWS_SECTION);
                webUrl = newsResult.getString(NEWS_URL);

                // Add news entry for newsList if not already in newsList
                News news = new News(title, section, webUrl);
                newsList.add(news);
            }

            Log.d(LOG_TAG, "FetchBooksTask Complete.");

        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        return newsList;
    }

    @Override
    protected ArrayList<News> doInBackground(String... params) {

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String newsJsonStr = null;

        String tag = "politics/politics";
        String apiKey = "8e6c6fe9-3dba-47df-851d-edb957c1a4d8";

        try {
            // Construct the URL for the Guardian News API query
            // http://open-platform.theguardian.com/documentation/
            final String GUARDIAN_BASE_URL =
                    "http://content.guardianapis.com/search?";
            final String TAG_PARAM = "tag";
            final String APPID_PARAM = "api-key";

            Uri builtUri = Uri.parse(GUARDIAN_BASE_URL).buildUpon()
                    .appendQueryParameter(TAG_PARAM, tag)
                    .appendQueryParameter(APPID_PARAM, apiKey)
                    .build();

            // use this version becaue of '/' in the TAG_PARAM
            URL url = new URL(URLDecoder.decode(builtUri.toString(), "UTF-8"));

            // Create the request to Google Books API, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            newsJsonStr = buffer.toString();
            return getNewsDataFromJson(newsJsonStr);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attempting
            // to parse it.
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        // This will only happen if there was an error getting or parsing the book list.
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<News> result) {
        if (result != null) {
            mMainActivity.refreshNewsList(result);
        }
    }
}
