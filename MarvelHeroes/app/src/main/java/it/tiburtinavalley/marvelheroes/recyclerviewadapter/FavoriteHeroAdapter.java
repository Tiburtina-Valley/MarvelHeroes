package it.tiburtinavalley.marvelheroes.recyclerviewadapter;

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
import it.tiburtinavalley.marvelheroes.entity.HeroEntity;
import it.tiburtinavalley.marvelheroes.model.HeroModel;
import it.tiburtinavalley.marvelheroes.volley.ImageApiVolley;

public class FavoriteHeroAdapter extends RecyclerView.Adapter<FavoriteHeroAdapter.Holder> implements View.OnClickListener{
    private final List<HeroEntity> heroes;
    private ImageApiVolley imgVolley;
    private Context appContext;

    public FavoriteHeroAdapter(List<HeroEntity> all, Context appContext) {
        heroes = new ArrayList<>();
        heroes.addAll(all);
        this.appContext = appContext;
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



    @Override
    public void onClick(View v) {

    }


    @NonNull
    @Override
    public FavoriteHeroAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ConstraintLayout cl;
        cl = (ConstraintLayout) LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.hero_layout, parent, false);
        cl.setOnClickListener(this);
        return new FavoriteHeroAdapter.Holder(cl);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteHeroAdapter.Holder holder, int position) {
        HeroEntity hero = heroes.get(position);
        holder.tvHeroName.setText(hero.getName());
        if (!hero.getPicturePath().equalsIgnoreCase("")
                && !hero.getPicturePath().equalsIgnoreCase("")) {
            String urlThumbnail = hero.getPicturePath().replaceFirst("http", "https")
                    + ".jpg";
            Glide.with(holder.itemView).load(urlThumbnail).into(holder.ivHeroPic);
    }








}

    @Override
    public int getItemCount() {
        return heroes.size();
    }
}
