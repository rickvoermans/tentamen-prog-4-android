package com.example.marcu.movieapplication.dataaccess;

/**
 * Created by Wallaard on 16-6-2017.
 */

import android.os.AsyncTask;
import android.util.Log;

import com.example.marcu.movieapplication.domain.Film;

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
import java.net.URLConnection;

/**
 * Created by Wallaard on 14-6-2017.
 */

public class FilmsGetTask extends AsyncTask<String, Void, String> {
    private final String TAG = getClass().getSimpleName();
    private OnFilmAvailable listener = null;

    public FilmsGetTask(OnFilmAvailable listener) {
        this.listener = listener;
    }

    public interface OnFilmAvailable{
        void OnFilmAvailable(Film film);
    }

    @Override
    protected String doInBackground(String... params) {

        InputStream inputStream = null;
        int responsCode = -1;
        // De URL die we via de .execute() meegeleverd krijgen
        String personUrl = params[0];
        // Het resultaat dat we gaan retourneren
        String response = "";

        Log.i(TAG, "doInBackground - " + personUrl);
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
                // Log.i(TAG, "doInBackground response = " + response);
            } else {
                Log.e(TAG, "Error, invalid response");
            }
        } catch (MalformedURLException e) {
            Log.e(TAG, "doInBackground MalformedURLEx " + e.getLocalizedMessage());
            return null;
        } catch (IOException e) {
            Log.e("TAG", "doInBackground IOException " + e.getLocalizedMessage());
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
            Log.e(TAG, "Response: "+response);

            for (int i = 0; i < jsonArray.length();i++){
                JSONObject film = jsonArray.getJSONObject(i);

                String title = film.getString("title");
                int release = film.getInt("release_year");
                int length = film.getInt("length");
                String rating = film.getString("rating");
                int inventoryid = film.getInt("inventory_id");

                Film f = new Film();
                f.setTitle(title);
                f.setReleaseyear(release);
                f.setLength(length);
                f.setRating(rating);
                f.setInventory_id(inventoryid);

                listener.OnFilmAvailable(f);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();
    }
}
