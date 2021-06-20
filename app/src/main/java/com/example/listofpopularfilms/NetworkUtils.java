package com.example.listofpopularfilms;

import android.content.Context;
import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {
    private final static String TMBD_API_BASE_URL = "https://api.themoviedb.org/3";
    private final static String TMBD_FILM_GET = "/discover/movie";
    private final static String PARAM_API_KEY = "api_key";
    private final static String PARAM_LANGUAGE = "language";
    private final static String PARAM_SORT_BY = "sort_by";
    private final static String PARAM_PAGE = "page";


    public static URL generateURLOfPopularFilmsList(int pageNumber){
        Uri builtUri = Uri.parse(TMBD_API_BASE_URL + TMBD_FILM_GET).buildUpon()
                .appendQueryParameter(PARAM_API_KEY, BuildConfig.CONSUMER_SECRET)
                .appendQueryParameter(PARAM_LANGUAGE, "ru-RU")
                .appendQueryParameter(PARAM_SORT_BY, "popularity.desc")
                .appendQueryParameter(PARAM_PAGE, String.valueOf(pageNumber))
                .build();

        URL urlOfPopularFilmsList = null;

        try {
            urlOfPopularFilmsList = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return urlOfPopularFilmsList;
    }


    public static String getResponceFromURL(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        InputStream in = urlConnection.getInputStream();

        try {
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();

            if (hasInput){
                String result = scanner.next();
                return result;
            } else {
                return null;
            }

        } finally {
            urlConnection.disconnect();
        }
    }


}
