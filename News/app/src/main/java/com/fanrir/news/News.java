package com.fanrir.news;

/**
 * Created by Eisdrachl on 04.07.2016.
 */
public class News {

    /**
     * The news title
     */
    private String mTitle;

    /**
     * The section of the news
     */
    private String mSection;

    /**
     * The the url to reach the news on the internet
     */
    private String mWebUrl;

    /**
     * Create a new News object.
     *
     * @param title   is the title of the news
     * @param section is the section the news belongs to
     */
    public News(String title, String section, String webUrl) {
        mTitle = title;
        mSection = section;
        mWebUrl = webUrl;
    }

    /**
     * Get the title of the News.
     */
    public String getTitle() {
        return mTitle;
    }

    /**
     * Get the section of the News.
     */
    public String getSection() {
        return mSection;
    }

    /**
     * Get the web url for the News.
     */
    public String getWebUrl() {
        return mWebUrl;
    }

}
