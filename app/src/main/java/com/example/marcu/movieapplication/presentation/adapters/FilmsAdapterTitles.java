package com.example.marcu.movieapplication.presentation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.marcu.movieapplication.R;
import com.example.marcu.movieapplication.domain.Film;

import java.util.ArrayList;

/**
 * Created by MarcdenUil on 17-6-2017.
 */

public class FilmsAdapterTitles extends BaseAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<Film> films;

    public FilmsAdapterTitles(Context context, LayoutInflater layoutInflater, ArrayList<Film> films) {
        this.context = context;
        this.layoutInflater = layoutInflater;
        this.films = films;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.single_item_films_titles,parent,false);

            viewHolder = new ViewHolder();
            viewHolder.textViewTitle = (TextView) convertView.findViewById(R.id.title);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Film f = films.get(position);
        viewHolder.textViewTitle.setText("" + f.getTitle());

        return convertView;
    }

    private static class ViewHolder {
        TextView textViewTitle;
    }
}
