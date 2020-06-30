package it.tiburtinavalley.marvelheroes.recyclerviewadapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Parcelable;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import it.tiburtinavalley.marvelheroes.HeroSelectMode;
import it.tiburtinavalley.marvelheroes.R;
import it.tiburtinavalley.marvelheroes.activity.FavoriteHeroDetail;
import it.tiburtinavalley.marvelheroes.activity.HeroDetailActivity;
import it.tiburtinavalley.marvelheroes.activity.ToastClass;
import it.tiburtinavalley.marvelheroes.dao.AppDatabase;
import it.tiburtinavalley.marvelheroes.entity.HeroEntity;
import it.tiburtinavalley.marvelheroes.model.HeroModel;

public class FavoriteHeroAdapter extends RecyclerView.Adapter<FavoriteHeroAdapter.Holder> implements View.OnClickListener, View.OnLongClickListener {
    private final List<HeroEntity> heroes;
    private Context appContext;
    private SparseBooleanArray selectedHeroesList;
    private HeroSelectMode smListener;

    public FavoriteHeroAdapter(List<HeroEntity> all, Context appContext, HeroSelectMode listener) {
        heroes = new ArrayList<>();
        heroes.addAll(all);
        selectedHeroesList = new SparseBooleanArray();
        this.appContext = appContext;
        smListener = listener;
    }

    class Holder extends RecyclerView.ViewHolder {
        final TextView tvHeroName;
        final ImageView ivHeroPic;
        final ConstraintLayout cl;

        Holder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnLongClickListener(FavoriteHeroAdapter.this);
            tvHeroName = itemView.findViewById(R.id.tvHeroName);
            ivHeroPic = itemView.findViewById(R.id.ivHeroPhoto);
            cl = itemView.findViewById(R.id.heroConstraintLayout);
            cl.setBackground(ContextCompat.getDrawable(itemView.getContext(), R.drawable.hero_selector));
        }
    }

    // carica l'activity di dettaglio dell'eroe
    @Override
    public void onClick(View v) {
        ConnectivityManager cm = (ConnectivityManager)appContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            int position = ((RecyclerView) v.getParent()).getChildAdapterPosition(v);
            HeroEntity hero = heroes.get(position);
            HeroModel heroModel=new HeroModel();
            heroModel.setHeroModelFromDb(hero);
            Intent i = new Intent(appContext, FavoriteHeroDetail.class);

            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("hero",  heroModel);
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation((Activity) appContext, (View) v, "profile");
            appContext.startActivity(i, options.toBundle());
        }
        else {
            ToastClass toast = new ToastClass(appContext);
            toast.showToast(appContext.getString(R.string.internet_required));
        }
    }


    //Attiva un menù che permette di togliere dai preferiti più di un eroe
    @Override
    public boolean onLongClick(View view) {
        int pos = ((RecyclerView) view.getParent()).getChildAdapterPosition(view); //acquisisce la posizione dell'elemento della RecyclerView che è stato clickato
        boolean isSelected = selectedHeroesList.get(pos, false);
        if(isSelected) {
            view.setSelected(false);
            selectedHeroesList.delete(pos);
        }
        else{
            view.setSelected(true);
            selectedHeroesList.put(pos,true);
        }
        if(smListener != null){
            smListener.onSelect(selectedHeroesList.size()); //callback verso l'Activity
        }
        notifyDataSetChanged();
        return true; //blocca la catena di chiamate
    }

    //cancella tutti glie elementi selezionati della lista
    public void removeSelected(){
        if(selectedHeroesList.size() > 0){ //controlla se c'è qualcosa da eliminare
            for(int i = heroes.size() -1; i >= 0; i--) { //si procede dal fondo verso la cima
                if(selectedHeroesList.get(i, false)){
                    AppDatabase.getInstance(appContext).heroDao().deleteHero(heroes.get(i)); // cancella dal db
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
                .inflate(R.layout.favourite_hero_layout, parent, false);
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
