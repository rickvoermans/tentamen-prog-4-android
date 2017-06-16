package com.example.marcu.movieapplication.presentation.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marcu.movieapplication.R;
import com.example.marcu.movieapplication.dataaccess.RentalsGetTask;
import com.example.marcu.movieapplication.domain.Film;
import com.example.marcu.movieapplication.presentation.adapters.RentalsAdapter;
import com.example.marcu.movieapplication.presentation.drawer.Drawer;

import java.util.ArrayList;

import static com.example.marcu.movieapplication.presentation.activities.LoginActivity.JWT_STR;
import static com.example.marcu.movieapplication.presentation.activities.LoginActivity.USER;

/**
 * Created by Wallaard on 16-6-2017.
 */

public class MyRentalsActivity extends AppCompatActivity implements RentalsGetTask.OnRentalAvailable, NavigationView.OnNavigationItemSelectedListener {
    private final String TAG = getClass().getSimpleName();
    private ArrayList<Film> films = new ArrayList<>();
    private RentalsAdapter rentalsAdapter;
    private ListView listViewFilms;

    private TextView textViewUserId;
    private ImageView deleteRental;
    private String jwt;
    private int user;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_rentals);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_rentals);

        setupToolbar(this, "My rentals");
        setupDrawer(this);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        jwt = prefs.getString(JWT_STR, "");
        user = prefs.getInt(USER, 0);

        getRentals();
        listViewFilms = (ListView) findViewById(R.id.rentalsListView);
        deleteRental = (ImageView)findViewById(R.id.delete_rental_id);
    }

    public void getRentals(){
        RentalsGetTask rentalsGetTask = new RentalsGetTask(this);
        String[] urls = new String[]{"https://programmeren-opdracht.herokuapp.com/api/v1/rental/" + user,jwt};
        Log.i(TAG, "methode wordt aangeroepen");
        rentalsGetTask.execute(urls);
    }

    @Override
    public void OnRentalAvailable(Film film) {
        films.add(film);
        rentalsAdapter = new RentalsAdapter(this,getLayoutInflater(),films);
        listViewFilms.setAdapter(rentalsAdapter);
    }

    public static void setupToolbar(final AppCompatActivity activity, String title) {
        Toolbar toolbar = (Toolbar) activity.findViewById(R.id.my_toolbar);
        activity.setSupportActionBar(toolbar);

        activity.getSupportActionBar().setTitle(title);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        new Drawer(getApplicationContext(), id, jwt, user);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static void setupDrawer(final AppCompatActivity activity) {
        DrawerLayout drawer = (DrawerLayout) activity.findViewById(R.id.drawer_layout);
        Toolbar toolbar = (Toolbar) activity.findViewById(R.id.my_toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                activity, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) activity.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) activity);
    }
}
