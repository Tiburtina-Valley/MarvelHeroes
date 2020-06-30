package it.tiburtinavalley.marvelheroes.recyclerviewadapter;

import android.content.Context;
import android.graphics.Color;
import android.util.SparseBooleanArray;
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

import it.tiburtinavalley.marvelheroes.HeroSelectMode;
import it.tiburtinavalley.marvelheroes.R;
import it.tiburtinavalley.marvelheroes.activity.ToastClass;
import it.tiburtinavalley.marvelheroes.dao.AppDatabase;
import it.tiburtinavalley.marvelheroes.entity.HeroEntity;
import it.tiburtinavalley.marvelheroes.model.HeroModel;
import it.tiburtinavalley.marvelheroes.volley.ImageApiVolley;

public class FavoriteHeroAdapter extends RecyclerView.Adapter<FavoriteHeroAdapter.Holder> implements View.OnClickListener, View.OnLongClickListener {
    private final List<HeroEntity> heroes;
    private ImageApiVolley imgVolley;
    private Context appContext;
    private HeroSelectMode heroListener;
    private SparseBooleanArray selectedHeroesList;

    public FavoriteHeroAdapter(List<HeroEntity> all, Context appContext) {
        heroes = new ArrayList<>();
        heroes.addAll(all);
        selectedHeroesList = new SparseBooleanArray();
        this.appContext = appContext;
    }



    class Holder extends RecyclerView.ViewHolder {
        final TextView tvHeroName;
        final ImageView ivHeroPic;

        Holder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnLongClickListener(FavoriteHeroAdapter.this);
            tvHeroName = itemView.findViewById(R.id.tvHeroName);
            ivHeroPic = itemView.findViewById(R.id.ivHeroPhoto);
        }
    }

    // carica l'activity di dettaglio dell'eroe
    @Override
    public void onClick(View v) {

    }

    //Attiva un menù che permette di togliere dai preferiti più di un eroe
    @Override
    public boolean onLongClick(View view) {
        int pos = ((RecyclerView) view.getParent()).getChildAdapterPosition(view); //acquisisce la posizione dell'elemento della RecyclerView che è stato clickato
        boolean isSelected = selectedHeroesList.get(pos, false);
        if(isSelected) { //cancella l'eroe dai preferiti
            view.setSelected(false);
            HeroEntity hero = heroes.get(pos);
            AppDatabase.getInstance(appContext).heroDao().deleteHero(hero); // cancella dal db
            selectedHeroesList.delete(pos);
        }
        else{
            System.out.println("Qui");
            view.setSelected(true);
            view.setBackgroundColor(Color.red(2));
            selectedHeroesList.put(pos,true);
        }
        if(heroListener != null){
            heroListener.onSelect(selectedHeroesList.size()); //callback verso l'Activity
        }
        notifyDataSetChanged();
        return true; //blocca la catena di chiamate
    }

    public void removeSelected(){
        if(selectedHeroesList.size() > 0){ //controlla se c'è qualcosa da eliminare
            for(int i = heroes.size() -1; i >= 0; i--) { //si procede dal fondo verso la cima
                if(selectedHeroesList.get(i, false)){
                    remove(i);
                }
            }
        }
        selectedHeroesList.clear();
    }

    //elimina l'elemento e notifica all'adapter che deve ristrutturare la RecyclerView
    private void remove(int index){
        heroes.remove(index);
        notifyItemRemoved(index);
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
