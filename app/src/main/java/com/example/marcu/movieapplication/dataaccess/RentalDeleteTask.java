package com.example.marcu.movieapplication.dataaccess;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by ricky on 16-6-2017.
 */

public class RentalDeleteTask extends AsyncTask<String, Void, Boolean> {

    private final String tag = getClass().getSimpleName();

    private SuccessListener listener;

    public RentalDeleteTask(SuccessListener listener) {
        this.listener = listener;
    }

    @Override
    protected Boolean doInBackground(String... params) {

        int responseCode;
        String rentaldeleteUrl = params[0];

        Boolean response = null;

        Log.i(tag, "doInBackground - " + rentaldeleteUrl);
        try {
            URL url = new URL(rentaldeleteUrl);
            URLConnection urlConnection = url.openConnection();

            if (!(urlConnection instanceof HttpURLConnection)) {
                return false;
            }

            HttpURLConnection httpConnection = (HttpURLConnection) urlConnection;

            httpConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            httpConnection.setRequestMethod("DELETE");
            httpConnection.setRequestProperty("X-Access-Token", params[1]);

            DataOutputStream localDataOutputStream = new DataOutputStream(httpConnection.getOutputStream());
            localDataOutputStream.flush();
            localDataOutputStream.close();
            httpConnection.connect();

            responseCode = httpConnection.getResponseCode();
            response = (responseCode == HttpURLConnection.HTTP_OK);
        } catch (MalformedURLException e) {
            Log.e(tag, "doInBackground MalformedURLEx " + e.getLocalizedMessage());
            return false;
        } catch (IOException e) {
            Log.e(tag, "doInBackground IOException " + e.getLocalizedMessage());
            return false;
        }

        return response;
    }

    @Override
    protected void onPostExecute(Boolean response) {
        listener.successfulDeleted(response);
    }

    public interface SuccessListener {
        void successfulDeleted(Boolean successful);
    }
}

