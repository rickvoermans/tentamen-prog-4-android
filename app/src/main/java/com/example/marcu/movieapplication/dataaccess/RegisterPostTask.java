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
 * Created by Wallaard on 16-6-2017.
 */

public class RegisterPostTask extends AsyncTask<String, Void, Boolean> {

    private final String tag = getClass().getSimpleName();

    private PutSuccessListener listener;

    public RegisterPostTask(PutSuccessListener listener) {
        this.listener = listener;
    }

    @Override
    protected Boolean doInBackground(String... params) {

        int responseCode;
        String balanceUrl = params[0];

        Boolean response = null;

        Log.i(tag, "doInBackground - " + balanceUrl);
        try {
            URL url = new URL(balanceUrl);
            URLConnection urlConnection = url.openConnection();

            if (!(urlConnection instanceof HttpURLConnection)) {
                return null;
            }

            HttpURLConnection httpConnection = (HttpURLConnection) urlConnection;

            httpConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            httpConnection.setRequestMethod("POST");

            JSONObject jsonParam = new JSONObject();
            jsonParam.put("email", params[1]);
            jsonParam.put("password", params[2]);

            Log.i(tag, String.valueOf(jsonParam));

            DataOutputStream localDataOutputStream = new DataOutputStream(httpConnection.getOutputStream());
            localDataOutputStream.writeBytes(jsonParam.toString());
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
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return response;
    }

    protected void onPostExecute(Boolean response) {
        listener.putSuccessful(response);
    }

    public interface PutSuccessListener {
        void putSuccessful(Boolean successful);
    }
}
