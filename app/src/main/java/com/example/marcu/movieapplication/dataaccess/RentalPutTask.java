package com.example.marcu.movieapplication.dataaccess;

/**
 * Created by marcu on 6/19/2017.
 */
import android.os.AsyncTask;
import android.util.Log;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Wallaard on 16-6-2017.
 */

public class RentalPutTask extends AsyncTask<String, Void, Boolean> {

    private final String tag = getClass().getSimpleName();

    private PutSuccessListener listener;

    public RentalPutTask(PutSuccessListener listener) {
        this.listener = listener;
    }

    @Override
    protected Boolean doInBackground(String... params) {

        int responseCode;
        String rentalPostUrl = params[0];

        Boolean response = null;

        Log.i(tag, "doInBackground - " + rentalPostUrl);
        try {
            URL url = new URL(rentalPostUrl);
            URLConnection urlConnection = url.openConnection();

            if (!(urlConnection instanceof HttpURLConnection)) {
                return null;
            }

            HttpURLConnection httpConnection = (HttpURLConnection) urlConnection;

            httpConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            httpConnection.setRequestMethod("PUT");
            httpConnection.setRequestProperty("X-Access-Token", params[1]);

            DataOutputStream localDataOutputStream = new DataOutputStream(httpConnection.getOutputStream());
            localDataOutputStream.flush();
            localDataOutputStream.close();
            httpConnection.connect();

            responseCode = httpConnection.getResponseCode();
            response = (responseCode == HttpURLConnection.HTTP_OK);
        } catch (MalformedURLException e) {
            Log.e(tag, "doInBackground MalformedURLEx " + e.getLocalizedMessage());
            return null;
        } catch (IOException e) {
            Log.e(tag, "doInBackground IOException " + e.getLocalizedMessage());
            return null;
        }

        return response;
    }

    @Override
    protected void onPostExecute(Boolean response) {
        listener.putSuccessful(response);
    }

    public interface PutSuccessListener {
        void putSuccessful(Boolean successful);
    }
}


