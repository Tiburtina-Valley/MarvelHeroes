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
import it.tiburtinavalley.marvelheroes.activity.HeroDetailActivity;
import it.tiburtinavalley.marvelheroes.model.HeroModel;
import it.tiburtinavalley.marvelheroes.volley.ImageApiVolley;

public class HeroAdapter extends RecyclerView.Adapter<HeroAdapter.Holder> implements View.OnClickListener {
    private final List<HeroModel> heroes;
    private ImageApiVolley imgVolley;
    private Context appContext;

    public HeroAdapter(List<HeroModel> all, Context appContext) {
        heroes = new ArrayList<>();
        heroes.addAll(all);
        imgVolley = new ImageApiVolley(appContext);
        this.appContext = appContext;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ConstraintLayout cl;
        cl = (ConstraintLayout) LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.hero_layout, parent, false);
        cl.setOnClickListener(this);
        return new Holder(cl);
    }

    // This method sets the layout of the hero
    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        HeroModel hero = heroes.get(position);
        holder.tvHeroName.setText(hero.getName());
        imgVolley.addHeroImg(holder.ivHeroPic);
        if (!hero.getThumbnail().getPath().equalsIgnoreCase("")
                && !hero.getThumbnail().getExtension().equalsIgnoreCase("")) {
            String urlThumbnail = hero.getThumbnail().getPath().replaceFirst("http", "https")
                    + "." + hero.getThumbnail().getExtension();
            Glide.with(holder.itemView).load(urlThumbnail).into(holder.ivHeroPic);
        }
    }

    @Override
    public int getItemCount() {
        return heroes.size();
    }

    @Override
    public void onClick(View v) {
        RecyclerView rv = (RecyclerView) v.getParent();
        // Fa si che non sia possibile clickare un elemento in un'altra Activity al di fuori della HeroDetailActivtiy
        if(rv.getId() == R.id.rvHeroes) {
            int position = rv.getChildAdapterPosition(v);
            HeroModel hero = heroes.get(position);
            Intent i = new Intent(appContext, HeroDetailActivity.class);

            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("hero", hero);
            appContext.startActivity(i);
        }
    }

    class Holder extends RecyclerView.ViewHolder {
        final TextView tvHeroName;
        final ImageView ivHeroPic;

        Holder(@NonNull View itemView) {
            super(itemView);
            tvHeroName = itemView.findViewById(R.id.tvHeroName);
            ivHeroPic = itemView.findViewById(R.id.ivHeroPhoto);
        }
    }
}
