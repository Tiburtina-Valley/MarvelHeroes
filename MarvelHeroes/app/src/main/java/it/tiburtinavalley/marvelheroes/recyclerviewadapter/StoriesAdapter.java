package it.tiburtinavalley.marvelheroes.recyclerviewadapter;

import android.content.Context;
import android.content.Intent;
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
import it.tiburtinavalley.marvelheroes.activity.StoriesActivity;
import it.tiburtinavalley.marvelheroes.model.Stories;
import it.tiburtinavalley.marvelheroes.volley.ImageApiVolley;

public class StoriesAdapter extends RecyclerView.Adapter<StoriesAdapter.StoriesHolder> implements View.OnClickListener {
    private List<Stories> stories;
    private Context appContext; //servirà per poter lanciare con successo la nuova activity nella onClick
    private ImageApiVolley imgVolley;

    public StoriesAdapter(List<Stories> all, Context appContext) {
        stories = new ArrayList<>();
        stories.addAll(all);
        this.appContext = appContext;
        imgVolley = new ImageApiVolley(appContext);
    }

    @NonNull
    @Override
    public StoriesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ConstraintLayout cl;
        cl = (ConstraintLayout) LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_detail_layout, parent, false);
        cl.setOnClickListener(this);
        return new StoriesHolder(cl);
    }

    @Override
    public void onBindViewHolder(@NonNull StoriesHolder holder, int position) {
        imgVolley.addHeroImg(holder.ivStories);
        String urlThumbnail = stories.get(position).getThumbnail().getPath().replaceFirst("http", "https")
                + "." + stories.get(position).getThumbnail().getExtension();
        Glide.with(holder.itemView).load(urlThumbnail).into(holder.ivStories);
        holder.tvStoriesName.setText(stories.get(position).getTitle());
    }





    @Override
    public int getItemCount() {
        return stories.size();
    }

    @Override
    public void onClick(View v) {
        int position = ((RecyclerView) v.getParent()).getChildAdapterPosition(v);
        Stories story = stories.get(position);

        Intent i = new Intent(appContext, StoriesActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("story", story);
        appContext.startActivity(i);
    }

    class StoriesHolder extends RecyclerView.ViewHolder {
        final ImageView ivStories;
        final TextView tvStoriesName;

        StoriesHolder(@NonNull View itemView) {
            super(itemView);
            ivStories = itemView.findViewById(R.id.ivCreator);
            tvStoriesName = itemView.findViewById(R.id.tvCreatorName);
        }
    }
}
