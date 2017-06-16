package com.example.marcu.movieapplication.presentation.activities;

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
import android.view.View;
import android.widget.AdapterView;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marcu.movieapplication.R;
import com.example.marcu.movieapplication.dataaccess.FilmsGetTask;
import com.example.marcu.movieapplication.dataaccess.RegisterPostTask;
import com.example.marcu.movieapplication.dataaccess.RentalPostTask;
import com.example.marcu.movieapplication.domain.Film;
import com.example.marcu.movieapplication.presentation.adapters.FilmsAdapter;
import com.example.marcu.movieapplication.presentation.drawer.Drawer;

import java.util.ArrayList;

import static com.example.marcu.movieapplication.presentation.activities.LoginActivity.JWT_STR;
import static com.example.marcu.movieapplication.presentation.activities.LoginActivity.USER;

public class MovieOverview extends AppCompatActivity implements FilmsGetTask.OnFilmAvailable, RentalPostTask.PutSuccessListener, AdapterView.OnItemClickListener,NavigationView.OnNavigationItemSelectedListener {

    private TextView textViewUserId;
    private String jwt;
    private int user;
    private SharedPreferences prefs;

    private ArrayList<Film> films = new ArrayList<>();
    private FilmsAdapter filmsAdapter;
    private ListView listViewFilms;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_films);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_movie_overview);

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
        FilmsGetTask filmsGetTask = new FilmsGetTask(this);
        String[] urls = new String[]{"https://programmeren-opdracht.herokuapp.com/api/v1/films/available",jwt};
        filmsGetTask.execute(urls);
    }

    @Override
    public void OnFilmAvailable(Film film) {
        films.add(film);
        filmsAdapter = new FilmsAdapter(this,getLayoutInflater(),films);
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
    private void loan(String url){
        String[] urls = new String[]{url, jwt};
        RentalPostTask task = new RentalPostTask(this);
        task.execute(urls);
    }

    @Override
    public void putSuccessful(Boolean successful) {
        if(successful){
            Toast.makeText(this, "Film has been added too your rentals!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Adding film failed", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        Film f = films.get(position);
        loan("https://programmeren-opdracht.herokuapp.com/api/v1/rental/"+user+"/"+f.getInventory_id());
    }

}
