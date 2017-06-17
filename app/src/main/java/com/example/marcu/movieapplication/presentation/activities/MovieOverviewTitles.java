package com.example.marcu.movieapplication.presentation.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.marcu.movieapplication.R;
import com.example.marcu.movieapplication.dataaccess.FilmsGetTask;
import com.example.marcu.movieapplication.dataaccess.FilmsGetTitleTask;
import com.example.marcu.movieapplication.domain.Film;
import com.example.marcu.movieapplication.presentation.adapters.FilmsAdapter;
import com.example.marcu.movieapplication.presentation.adapters.FilmsAdapterTitles;
import com.example.marcu.movieapplication.presentation.drawer.Drawer;

import java.util.ArrayList;

import static com.example.marcu.movieapplication.presentation.activities.LoginActivity.JWT_STR;
import static com.example.marcu.movieapplication.presentation.activities.LoginActivity.USER;

public class MovieOverviewTitles extends AppCompatActivity implements AdapterView.OnItemClickListener,NavigationView.OnNavigationItemSelectedListener, FilmsGetTitleTask.OnFilmAvailable{

    private String jwt;
    private int user;
    private SharedPreferences prefs;

    private ArrayList<Film> films = new ArrayList<>();
    private FilmsAdapterTitles filmsAdapter;
    private ListView listViewFilms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_overview_titles);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_home);

        setupToolbar(this, "Home");
        setupDrawer(this);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        jwt = prefs.getString(JWT_STR, "");
        user = prefs.getInt(USER, 0);

        Log.i("jwt", jwt);

        getFilms();
        listViewFilms = (ListView) findViewById(R.id.filmsListview);
        listViewFilms.setOnItemClickListener(this);
    }

    public void getFilms(){
        FilmsGetTitleTask filmsGetTask = new FilmsGetTitleTask(this);
        String[] urls = new String[]{"https://programmeren-opdracht.herokuapp.com/api/v1/films/titles",jwt};
        filmsGetTask.execute(urls);
    }

    @Override
    public void OnFilmAvailable(Film film) {
        films.add(film);
        filmsAdapter = new FilmsAdapterTitles(this, getLayoutInflater(),films);
        listViewFilms.setAdapter(filmsAdapter);
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        Film f = films.get(position);
        Intent intent = new Intent(getApplicationContext(), MovieOverview.class);
        intent.putExtra("TITLE", f.getTitle());
        startActivity(intent);
    }
}
