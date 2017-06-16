package com.example.marcu.movieapplication.presentation.activities;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.example.marcu.movieapplication.R;
import com.example.marcu.movieapplication.dataaccess.FilmsGetTask;
import com.example.marcu.movieapplication.domain.Film;
import com.example.marcu.movieapplication.presentation.adapters.FilmsAdapter;

import java.util.ArrayList;

import static com.example.marcu.movieapplication.presentation.activities.LoginActivity.JWT_STR;
import static com.example.marcu.movieapplication.presentation.activities.LoginActivity.USER;

public class MovieOverview extends AppCompatActivity implements FilmsGetTask.OnFilmAvailable {

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

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        jwt = prefs.getString(JWT_STR, "");
        user = prefs.getInt(USER, 0);

        Log.i("jwt", jwt);

        getFilms();
        listViewFilms = (ListView) findViewById(R.id.filmsListview);
    }

    public void getFilms(){
        FilmsGetTask filmsGetTask = new FilmsGetTask(this);
        String[] urls = new String[]{"http://10.0.2.2:8080/api/v1/films/1",jwt};
        filmsGetTask.execute(urls);
    }

    @Override
    public void OnFilmAvailable(Film film) {
        films.add(film);
        filmsAdapter = new FilmsAdapter(this,getLayoutInflater(),films);
        listViewFilms.setAdapter(filmsAdapter);
    }

}
