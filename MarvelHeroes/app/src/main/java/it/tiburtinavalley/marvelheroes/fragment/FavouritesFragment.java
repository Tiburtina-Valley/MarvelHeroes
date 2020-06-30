package it.tiburtinavalley.marvelheroes.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.tiburtinavalley.marvelheroes.R;
import it.tiburtinavalley.marvelheroes.activity.EventsActivity;
import it.tiburtinavalley.marvelheroes.activity.MainActivity;
import it.tiburtinavalley.marvelheroes.dao.AppDatabase;
import it.tiburtinavalley.marvelheroes.dao.HeroDao;
import it.tiburtinavalley.marvelheroes.dao.HeroRelatedDao;
import it.tiburtinavalley.marvelheroes.entity.HeroAndAllRelatedEntity;
import it.tiburtinavalley.marvelheroes.entity.HeroEntity;
import it.tiburtinavalley.marvelheroes.model.HeroModel;
import it.tiburtinavalley.marvelheroes.recyclerviewadapter.FavoriteHeroAdapter;


public class FavouritesFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_favourites, container, false);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Your heroes");

        RecyclerView rvHeroes;
        rvHeroes = v.findViewById(R.id.rvFavouriteHeroes);
        LinearLayoutManager layoutManagerHeroes = new LinearLayoutManager(
                getActivity(), RecyclerView.VERTICAL, false);
        int numberOfColumns = 2;
        rvHeroes.setLayoutManager(new GridLayoutManager(getContext(), numberOfColumns));

        List<HeroEntity> list = AppDatabase.getInstance(getActivity().getApplicationContext()).heroDao().getHeroList();
        if (list != null) {
            FavoriteHeroAdapter favoriteAdapter = new FavoriteHeroAdapter(list, getContext());
            rvHeroes.setAdapter(favoriteAdapter);
        } else {
            return v;
        }
        return v;
    }
}