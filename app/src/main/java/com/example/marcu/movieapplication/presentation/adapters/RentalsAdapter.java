package com.example.marcu.movieapplication.presentation.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marcu.movieapplication.R;
import com.example.marcu.movieapplication.dataaccess.RentalPutTask;
import com.example.marcu.movieapplication.domain.Film;

import java.util.List;

import static com.example.marcu.movieapplication.presentation.activities.LoginActivity.JWT_STR;
import static com.example.marcu.movieapplication.presentation.activities.LoginActivity.USER;

/**
 * Created by Wallaard on 16-6-2017.
 */

public class RentalsAdapter extends BaseAdapter implements RentalPutTask.PutSuccessListener{
    private Context context;
    private LayoutInflater layoutInflater;
    private List<Film> films;
    private String jwt;
    private final int user;

    public RentalsAdapter(Context context, LayoutInflater layoutInflater, List<Film> films) {
        this.context = context;
        this.layoutInflater = layoutInflater;
        this.films = films;

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        this.jwt = prefs.getString(JWT_STR, "");
        this.user = prefs.getInt(USER, 0);
    }

    @Override
    public int getCount() {
        return films.size();
    }

    @Override
    public Object getItem(int position) {
        return films.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        View view = convertView;

        if(view == null){
            viewHolder = new ViewHolder();

            view = layoutInflater.inflate(R.layout.single_item_rental,parent,false);

            viewHolder.textViewTitle = (TextView) view.findViewById(R.id.title);
            viewHolder.textViewRelease = (TextView) view.findViewById(R.id.release_year);
            viewHolder.textViewLength = (TextView) view.findViewById(R.id.length);
            viewHolder.textViewRating = (TextView) view.findViewById(R.id.rating);
            viewHolder.removeRental = (ImageView) view.findViewById(R.id.delete_rental_id);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Film f = films.get(position);

        viewHolder.textViewTitle.setText("Title: "+f.getTitle());
        viewHolder.textViewLength.setText("Length: "+f.getLength());
        viewHolder.textViewRating.setText("Rating: "+f.getRating());
        viewHolder.textViewRelease.setText("Release Date: "+f.getReleaseyear());

        viewHolder.removeRental.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Rental Adapter", "Removed: " + films.get(position).getTitle());
                putRental("https://programmeren-opdracht.herokuapp.com/api/v1/rental/" + user + "/" + films.get(position).getInventoryid());
                films.remove(position);
                notifyDataSetChanged();
            }
        });

        return view;
    }

    private static class ViewHolder {
        TextView textViewTitle;
        TextView textViewRelease;
        TextView textViewLength;
        TextView textViewRating;
        ImageView removeRental;
    }

    private void putRental(String apiUrl) {
        String[] urls = new String[]{apiUrl, jwt};
        RentalPutTask task = new RentalPutTask(this);
        task.execute(urls);
    }

    @Override
    public void putSuccessful(Boolean successful) {
        if (successful) {
            Log.i("RentalsAdapter", "Rental removed");
        } else {
            Toast.makeText(context, "Rental couldn't be removed", Toast.LENGTH_SHORT).show();
        }
    }
}