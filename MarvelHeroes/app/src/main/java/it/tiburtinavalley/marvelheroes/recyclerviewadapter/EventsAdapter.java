package it.tiburtinavalley.marvelheroes.recyclerviewadapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
import it.tiburtinavalley.marvelheroes.activity.EventsActivity;
import it.tiburtinavalley.marvelheroes.model.Events;
import it.tiburtinavalley.marvelheroes.volley.ImageApiVolley;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.EventsHolder> implements View.OnClickListener {
    private List<Events> events;
    private Context appContext; //servirà per poter lanciare con successo la nuova activity nella onClick
    private ImageApiVolley imgVolley;



    public EventsAdapter(List<Events> all, Context appContext) {
        events = new ArrayList<>();
        events.addAll(all);
        this.appContext = appContext;
        imgVolley = new ImageApiVolley(appContext);
    }

    @NonNull
    @Override
    public EventsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ConstraintLayout cl;
        cl = (ConstraintLayout) LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_detail_layout, parent, false);
        cl.setOnClickListener(this);
        return new EventsHolder(cl);
    }


    @Override
    public void onBindViewHolder(@NonNull EventsAdapter.EventsHolder holder, int position) {
        imgVolley.addHeroImg(holder.ivEvent);
        String urlThumbnail = events.get(position).getThumbnail().getPath().replaceFirst("http", "https")
                + "." + events.get(position).getThumbnail().getExtension();
        Glide.with(holder.itemView).load(urlThumbnail).into(holder.ivEvent);
        holder.tvEventName.setText(events.get(position).getTitle());

    }


    @Override
    public int getItemCount() {
        return events.size();
    }

    @Override
    public void onClick(View v) {
        int position = ((RecyclerView) v.getParent()).getChildAdapterPosition(v);
        Events event = events.get(position);
        Intent i = new Intent(appContext, EventsActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("event", event);
        appContext.startActivity(i);
    }

    class EventsHolder extends RecyclerView.ViewHolder {
        final ImageView ivEvent;
        final TextView tvEventName;

        EventsHolder(@NonNull View itemView) {
            super(itemView);
            ivEvent = itemView.findViewById(R.id.ivComic);
            tvEventName = itemView.findViewById(R.id.tvStoriesName);
        }
    }
}
