package com.example.marcu.movieapplication.presentation.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.example.marcu.movieapplication.R;
import com.example.marcu.movieapplication.dataaccess.FilmsGetTask;
import com.example.marcu.movieapplication.dataaccess.RentalsGetTask;
import com.example.marcu.movieapplication.domain.Film;
import com.example.marcu.movieapplication.presentation.adapters.FilmsAdapter;
import com.example.marcu.movieapplication.presentation.adapters.RentalsAdapter;

import java.util.ArrayList;

import static com.example.marcu.movieapplication.presentation.activities.LoginActivity.JWT_STR;
import static com.example.marcu.movieapplication.presentation.activities.LoginActivity.USER;

/**
 * Created by Wallaard on 16-6-2017.
 */

public class MyRentalsActivity extends AppCompatActivity implements RentalsGetTask.OnRentalAvailable {
    private final String TAG = getClass().getSimpleName();
    private ArrayList<Film> films = new ArrayList<>();
    private RentalsAdapter rentalsAdapter;
    private ListView listViewFilms;

    private TextView textViewUserId;
    private String jwt;
    private int user;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_rentals);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        jwt = prefs.getString(JWT_STR, "");
        user = prefs.getInt(USER, 0);

        Log.i(TAG, "oncreate");
        getRentals();
        listViewFilms = (ListView) findViewById(R.id.rentalsListView);
    }

    public void getRentals(){
        RentalsGetTask rentalsGetTask = new RentalsGetTask(this);
        String[] urls = new String[]{"http://10.0.2.2:8080/api/v1/rental/1",jwt};
        Log.i(TAG, "methode wordt aangeroepen");
        rentalsGetTask.execute(urls);
    }

    @Override
    public void OnRentalAvailable(Film film) {
        films.add(film);
        rentalsAdapter = new RentalsAdapter(this,getLayoutInflater(),films);
        listViewFilms.setAdapter(rentalsAdapter);
    }
}
