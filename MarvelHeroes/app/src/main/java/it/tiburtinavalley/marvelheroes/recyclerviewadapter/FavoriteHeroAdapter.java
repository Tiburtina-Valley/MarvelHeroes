package it.tiburtinavalley.marvelheroes.recyclerviewadapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
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
import com.zhukic.sectionedrecyclerview.SectionedRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import it.tiburtinavalley.marvelheroes.HeroSelectMode;
import it.tiburtinavalley.marvelheroes.R;
import it.tiburtinavalley.marvelheroes.activity.FavoriteHeroDetail;
import it.tiburtinavalley.marvelheroes.activity.ToastClass;
import it.tiburtinavalley.marvelheroes.dao.AppDatabase;
import it.tiburtinavalley.marvelheroes.entity.HeroEntity;
import it.tiburtinavalley.marvelheroes.model.HeroModel;

public class FavoriteHeroAdapter extends SectionedRecyclerViewAdapter<FavoriteHeroAdapter.SubheaderHolder, FavoriteHeroAdapter.Holder> implements View.OnClickListener, View.OnLongClickListener {

    public interface OnItemClickListener {
        void onSubheaderClicked(int position);
    }

    OnItemClickListener onItemClickListener;

    private final List<HeroEntity> heroes;
    private Context appContext;
    private SparseBooleanArray selectedHeroesList;
    private HeroSelectMode smListener;
    private boolean selectedMenu = false;

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
        final ConstraintLayout borderLayout;

        Holder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnLongClickListener(FavoriteHeroAdapter.this);
            tvHeroName = itemView.findViewById(R.id.tvHeroName);
            ivHeroPic = itemView.findViewById(R.id.ivHeroPhoto);
            cl = itemView.findViewById(R.id.heroConstraintLayout);
            borderLayout = itemView.findViewById(R.id.borderLayout);
            cl.setBackground(ContextCompat.getDrawable(itemView.getContext(), R.drawable.hero_selector));
        }
    }

    // carica l'activity di dettaglio dell'eroe
    @Override
    public void onClick(View v) {
        if (selectedHeroesList.size() == 0) {
            selectedMenu = false;
        }
        else {
            if (selectedMenu) {
                onLongClick(v);
            }
        }
        if (!selectedMenu) {
            ConnectivityManager cm = (ConnectivityManager) appContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
                int position = ((RecyclerView) v.getParent()).getChildAdapterPosition(v);
                HeroEntity hero = heroes.get(position);
                HeroModel heroModel = new HeroModel();
                heroModel.setHeroModelFromDb(hero);
                Intent i = new Intent(appContext, FavoriteHeroDetail.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("hero", heroModel);
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation((Activity) appContext, (View) v, "profile");
                appContext.startActivity(i, options.toBundle());
            } else {
                ToastClass toast = new ToastClass(appContext);
                toast.showToast(appContext.getString(R.string.internet_required));
            }
        }
    }


    //Attiva un menù che permette di togliere dai preferiti più di un eroe
    @Override
    public boolean onLongClick(View view) {
        selectedMenu = true;
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
        selectedMenu = false;
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


    @Override
    public Holder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        ConstraintLayout cl;
        cl = (ConstraintLayout) LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.favourite_hero_layout, parent, false);
        cl.setOnClickListener(this);
        return new FavoriteHeroAdapter.Holder(cl);
    }

    @Override
    public SubheaderHolder onCreateSubheaderViewHolder(ViewGroup parent, int viewType) {
        return new SubheaderHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.favourites_item_header, parent, false));
    }

    @Override
    public void onBindItemViewHolder(Holder holder, int position) {
        HeroEntity hero = heroes.get(position);
        holder.tvHeroName.setText(hero.getName());
        if (!hero.getPicturePath().equalsIgnoreCase("")
                && !hero.getPicturePath().equalsIgnoreCase("")) {
            String urlThumbnail = hero.getPicturePath().replaceFirst("http", "https")
                    + ".jpg";
            Glide.with(holder.itemView).load(urlThumbnail).into(holder.ivHeroPic);
        }
        if (holder.cl.isSelected()) {

            holder.ivHeroPic.setColorFilter(R.color.selectionColor, PorterDuff.Mode.SRC_ATOP);
        }
        else {
            holder.ivHeroPic.clearColorFilter();
        }

    @Override
    public void onBindSubheaderViewHolder(SubheaderHolder subheaderHolder, int nextItemPosition) {
        final HeroEntity nextHero = heroes.get(nextItemPosition);
        final int sectionSize = getSectionSize(getSectionIndex(subheaderHolder.getAdapterPosition()));
        final String sectionLetter = nextHero.getName().substring(0, 1);
        final String subheaderText;
        if(sectionSize > 1 || sectionSize == 0)
            subheaderText = sectionLetter + "\t(" + sectionSize + " items)";
        else
            subheaderText = sectionLetter + "\t(" + sectionSize + " item)";

        subheaderHolder.mSubheaderText.setText(subheaderText);

        boolean isSectionExpanded = isSectionExpanded(getSectionIndex(subheaderHolder.getAdapterPosition()));

        if (isSectionExpanded) {
            subheaderHolder.mArrow.setImageDrawable(ContextCompat.getDrawable(subheaderHolder.itemView.getContext(), R.drawable.ic_keyboard_arrow_up_black_24dp));
        } else {
            subheaderHolder.mArrow.setImageDrawable(ContextCompat.getDrawable(subheaderHolder.itemView.getContext(), R.drawable.ic_keyboard_arrow_down_black_24dp));
        }

        subheaderHolder.itemView.setOnClickListener(v -> onItemClickListener.onSubheaderClicked(subheaderHolder.getAdapterPosition()));
    }

    @Override
    public boolean onPlaceSubheaderBetweenItems(int position) {
        final char heroNameFirstCharacter = heroes.get(position).getName().charAt(0);
        final char nextHeroNameFirstCharacter = heroes.get(position + 1).getName().charAt(0);

        //The subheader will be placed between two neighboring items if the first characters in movie titles are different.
        return heroNameFirstCharacter != nextHeroNameFirstCharacter;
    }

    static class SubheaderHolder extends RecyclerView.ViewHolder {

        private static Typeface meduiumTypeface = null;

        TextView mSubheaderText;
        ImageView mArrow;

        SubheaderHolder(View itemView) {
            super(itemView);
            this.mSubheaderText = (TextView) itemView.findViewById(R.id.subheaderText);
            this.mArrow = (ImageView) itemView.findViewById(R.id.arrow);
        }

    }

    @Override
    public int getItemSize() {
        return heroes.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
