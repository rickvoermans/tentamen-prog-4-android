package com.example.marcu.movieapplication.dataaccess;

/**
 * Created by MarcdenUil on 17-6-2017.
 */

import android.os.AsyncTask;
import android.util.Log;

import com.example.marcu.movieapplication.domain.Film;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import static com.example.marcu.movieapplication.dataaccess.FilmsGetTask.getStringFromInputStream;

public class FilmsGetTitleTask extends AsyncTask<String, Void, String> {
    private final String tag = getClass().getSimpleName();
    private OnFilmAvailable listener = null;

    public FilmsGetTitleTask(OnFilmAvailable listener) {
        this.listener = listener;
    }

    public interface OnFilmAvailable{
        void onFilmAvailable(Film film);
    }

    @Override
    protected String doInBackground(String... params) {

        InputStream inputStream = null;
        int responsCode = -1;
        // De URL die we via de .execute() meegeleverd krijgen
        String personUrl = params[0];
        // Het resultaat dat we gaan retourneren
        String response = "";

        Log.i(tag, "doInBackground - " + personUrl);
        try {
            // Maak een URL object
            URL url = new URL(personUrl);
            // Open een connection op de URL
            URLConnection urlConnection = url.openConnection();

            if (!(urlConnection instanceof HttpURLConnection)) {
                return null;
            }

            // Initialiseer een HTTP connectie
            HttpURLConnection httpConnection = (HttpURLConnection) urlConnection;
            httpConnection.setAllowUserInteraction(false);
            httpConnection.setInstanceFollowRedirects(true);
            httpConnection.setRequestMethod("GET");
            httpConnection.setRequestProperty("X-Access-Token", params[1]);

            // Voer het request uit via de HTTP connectie op de URL
            httpConnection.connect();

            // Kijk of het gelukt is door de response code te checken
            responsCode = httpConnection.getResponseCode();
            if (responsCode == HttpURLConnection.HTTP_OK) {
                inputStream = httpConnection.getInputStream();
                response = getStringFromInputStream(inputStream);
            } else {
                Log.e(tag, "Error, invalid response");
            }
        } catch (MalformedURLException e) {
            Log.e(tag, "doInBackground MalformedURLEx " + e.getLocalizedMessage());
            return null;
        } catch (IOException e) {
            Log.e("tag", "doInBackground IOException " + e.getLocalizedMessage());
            return null;
        }

        // Hier eindigt deze methode.
        // Het resultaat gaat naar de onPostExecute methode.
        return response;
    }

    @Override
    protected void onPostExecute(String response){
        try{
            JSONArray jsonArray = new JSONArray(response);
            Log.e(tag, "Response: "+response);

            for (int i = 0; i < jsonArray.length();i++){
                JSONObject film = jsonArray.getJSONObject(i);

                String title = film.getString("title");

                Film f = new Film();
                f.setTitle(title);

                listener.onFilmAvailable(f);

            }
        } catch (JSONException e) {
            Log.e(tag, "JSON execption" + e.getLocalizedMessage());
        }
    }
}

