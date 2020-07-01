package it.tiburtinavalley.marvelheroes.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import it.tiburtinavalley.marvelheroes.HeroSelectMode;
import it.tiburtinavalley.marvelheroes.R;

import it.tiburtinavalley.marvelheroes.activity.MainActivity;
import it.tiburtinavalley.marvelheroes.dao.AppDatabase;
import it.tiburtinavalley.marvelheroes.entity.HeroEntity;
import it.tiburtinavalley.marvelheroes.recyclerviewadapter.FavoriteHeroAdapter;


public class FavouritesFragment extends Fragment {
    private ActionMode mActionMode;
    FavoriteHeroAdapter favoriteAdapter;
    SelectModeListener smListener;

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
            smListener = new SelectModeListener();
            favoriteAdapter = new FavoriteHeroAdapter(list, getContext(), smListener);
            rvHeroes.setAdapter(favoriteAdapter);
        } else {
            return v;
        }
        return v;
    }

    public void deselectOnBack() {
        favoriteAdapter.unselectElements(); //chiama il metodo nell'Adapter per far si che vengano deselezionati gli elementi
    }

    public class SelectModeListener implements HeroSelectMode {

        @Override
        public void onSelect(int size) {
            if (mActionMode != null) {
                if (size == 0) {
                    mActionMode.finish();
                }
                return;
            }
            mActionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(hActionModeCallback);
        }
    }

    private ActionMode.Callback hActionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            actionMode.getMenuInflater().inflate(R.menu.menu_delete, menu);
            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.itemDelete:
                    favoriteAdapter.removeSelected();
                    actionMode.finish();
                    return true; 
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            actionMode = null;
            mActionMode = null; //fa si che il menù ricompaia se tutti gli elementi sono deselezionati
        }
    };
}