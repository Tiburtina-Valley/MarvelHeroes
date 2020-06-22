package it.tiburtinavalley.marvelheroes.recyclerviewadapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import it.tiburtinavalley.marvelheroes.R;
import it.tiburtinavalley.marvelheroes.model.Series;
import it.tiburtinavalley.marvelheroes.volley.ImageApiVolley;

public class SeriesAdapter extends RecyclerView.Adapter<SeriesAdapter.SeriesHolder> implements View.OnClickListener {
    private List<Series> series;
    private Context appContext;
    private ImageApiVolley imgVolley;

    public SeriesAdapter(List<Series> all, Context appContext) {
        series = new ArrayList<>();
        series.addAll(all);
        this.appContext = appContext;
        imgVolley = new ImageApiVolley(appContext);
    }

    @NonNull
    @Override
    public SeriesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ConstraintLayout cl;
        cl = (ConstraintLayout) LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_detail_layout, parent, false);
        cl.setOnClickListener(this);
        return new SeriesHolder(cl);
    }

    // This method sets the layout of the hero
    @Override
    public void onBindViewHolder(@NonNull SeriesHolder holder, int position) {
        imgVolley.addHeroImg(holder.ivComic);
        String urlThumbnail = series.get(position).getThumbnail().getPath().replaceFirst("http", "https")
                + "." + series.get(position).getThumbnail().getExtension();
        Glide.with(holder.itemView).load(urlThumbnail).into(holder.ivComic);
    }

    @Override
    public int getItemCount() {
        return series.size();
    }

    //
    @Override
    public void onClick(View v) {
        //TODO : display new activity based on user search
    }

    class SeriesHolder extends RecyclerView.ViewHolder {
        final ImageView ivComic;
        final TextView tvComicName;

        SeriesHolder(@NonNull View itemView) {
            super(itemView);
            ivComic = itemView.findViewById(R.id.ivComic);
            tvComicName = itemView.findViewById(R.id.tvComicName);
        }
    }
}
