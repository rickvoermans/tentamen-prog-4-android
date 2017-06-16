package com.example.marcu.movieapplication.presentation.drawer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.marcu.movieapplication.R;
import com.example.marcu.movieapplication.presentation.activities.LoginActivity;
import com.example.marcu.movieapplication.presentation.activities.MovieOverview;
import com.example.marcu.movieapplication.presentation.activities.MyRentalsActivity;

import static com.example.marcu.movieapplication.presentation.activities.LoginActivity.JWT_STR;
import static com.example.marcu.movieapplication.presentation.activities.LoginActivity.USER;

/**
 * Created by marcu on 6/16/2017.
 */

public class Drawer {
    private final String tag = getClass().getSimpleName();

    private Context context;
    private Intent intent;

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    public Drawer(Context context, int id, String jwt, int user) {

        Log.i(tag, "user: " + user);

        this.context = context;
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        editor = prefs.edit();

        switch (id) {
            case R.id.nav_movie_overview:
                intent = new Intent(context, MovieOverview.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                break;
            case R.id.nav_rentals:
                intent = new Intent(context, MyRentalsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                break;
            case R.id.nav_logout:
                intent = new Intent(context, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);

                editor.remove(JWT_STR);
                editor.apply();
                context.startActivity(intent);
                break;
            default :
                break;
        }
    }
}
