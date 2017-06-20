package com.example.marcu.movieapplication.presentation.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.marcu.movieapplication.R;
import com.example.marcu.movieapplication.dataaccess.RentalsGetTask;
import com.example.marcu.movieapplication.domain.Film;
import com.example.marcu.movieapplication.presentation.adapters.RentalsAdapter;
import com.example.marcu.movieapplication.presentation.drawer.Drawer;

import java.util.ArrayList;

import static com.example.marcu.movieapplication.presentation.activities.LoginActivity.JWT_STR;
import static com.example.marcu.movieapplication.presentation.activities.LoginActivity.USER;
import static com.example.marcu.movieapplication.presentation.activities.MovieOverview.setupDrawer;
import static com.example.marcu.movieapplication.presentation.activities.MovieOverview.setupToolbar;

/**
 * Created by Wallaard on 16-6-2017.
 */

public class MyRentalsActivity extends AppCompatActivity implements RentalsGetTask.OnRentalAvailable, NavigationView.OnNavigationItemSelectedListener {
    private final String tag = getClass().getSimpleName();
    private ArrayList<Film> films = new ArrayList<>();
    private ListView listViewFilms;
    private String jwt;
    private int user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_rentals);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_rentals);

        setupToolbar(this, "My rentals");
        setupDrawer(this);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        jwt = prefs.getString(JWT_STR, "");
        user = prefs.getInt(USER, 0);

        getRentals();
        listViewFilms = (ListView) findViewById(R.id.rentalsListView);
    }

    public void getRentals(){
        RentalsGetTask rentalsGetTask = new RentalsGetTask(this);
        String[] urls = new String[]{"https://programmeren-opdracht.herokuapp.com/api/v1/rental/" + user,jwt};
        Log.i(tag, "methode wordt aangeroepen");
        rentalsGetTask.execute(urls);
    }

    @Override
    public void onRentalAvailable(Film film) {
        films.add(film);
        RentalsAdapter rentalsAdapter = new RentalsAdapter(this,getLayoutInflater(),films);
        listViewFilms.setAdapter(rentalsAdapter);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        new Drawer(getApplicationContext(), id, user);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
