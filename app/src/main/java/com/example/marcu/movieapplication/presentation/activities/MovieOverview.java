package com.example.marcu.movieapplication.presentation.activities;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marcu.movieapplication.R;
import com.example.marcu.movieapplication.dataaccess.FilmsGetTask;
import com.example.marcu.movieapplication.dataaccess.RegisterPostTask;
import com.example.marcu.movieapplication.dataaccess.RentalPostTask;
import com.example.marcu.movieapplication.domain.Film;
import com.example.marcu.movieapplication.presentation.adapters.FilmsAdapter;

import java.util.ArrayList;

import static com.example.marcu.movieapplication.presentation.activities.LoginActivity.JWT_STR;
import static com.example.marcu.movieapplication.presentation.activities.LoginActivity.USER;

public class MovieOverview extends AppCompatActivity implements FilmsGetTask.OnFilmAvailable, RentalPostTask.PutSuccessListener, AdapterView.OnItemClickListener {

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
        listViewFilms.setOnItemClickListener(this);
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

    private void loan(String url){
        String[] urls = new String[]{url};
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
    }

}
