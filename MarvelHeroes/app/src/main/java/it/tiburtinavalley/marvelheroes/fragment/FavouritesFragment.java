package it.tiburtinavalley.marvelheroes.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import it.tiburtinavalley.marvelheroes.GridDividerDecoration;
import it.tiburtinavalley.marvelheroes.HeroSelectMode;
import it.tiburtinavalley.marvelheroes.R;
import it.tiburtinavalley.marvelheroes.activity.MainActivity;
import it.tiburtinavalley.marvelheroes.dao.AppDatabase;
import it.tiburtinavalley.marvelheroes.entity.HeroEntity;
import it.tiburtinavalley.marvelheroes.recyclerviewadapter.FavoriteHeroAdapter;

public class FavouritesFragment extends Fragment implements MainActivity.IOnBackPressed, FavoriteHeroAdapter.OnItemClickListener {
    private ActionMode mActionMode;
    FavoriteHeroAdapter favoriteAdapter;
    SelectModeListener smListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_favourites, container, false);
        // imposta il titolo della actionbar
        Objects.requireNonNull(((MainActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle(R.string.title_favourites);

        RecyclerView rvHeroes;
        rvHeroes = v.findViewById(R.id.rvFavouriteHeroes);

        // imposta la griglia
        int numberOfColumns = 2;
        GridDividerDecoration gridDividerDecoration = new GridDividerDecoration(getContext());
        rvHeroes.addItemDecoration(gridDividerDecoration);
        GridLayoutManager glm = new GridLayoutManager(getContext(), numberOfColumns);
        rvHeroes.setLayoutManager(glm);

        // carica gli eroi salvati dal db
        List<HeroEntity> heroes = AppDatabase.getInstance(getActivity().getApplicationContext()).heroDao().getHeroList();
        if (heroes != null) {
            smListener = new SelectModeListener();  // listener per le chiamate di callback quando un elemento viene selezionato
            favoriteAdapter = new FavoriteHeroAdapter(heroes, getContext(), smListener);
            favoriteAdapter.setOnItemClickListener(this);
            favoriteAdapter.setGridLayoutManager(glm);
            rvHeroes.setAdapter(favoriteAdapter);
        }

        return v;
    }

    /**
     * permette di tornare alla home quando si preme 'back'
     */
    @Override
    public boolean onBackPressed() {
        Log.w("12", getString(R.string.msg_is_pressed));

        FragmentTransaction fragmentTransaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.anim_fade_in, R.anim.anim_fade_out);
        HomeFragment home = new HomeFragment();
        fragmentTransaction.replace(R.id.fragment_container, home);
        fragmentTransaction.commit();
        return true;
    }

    /**
     * permette di espandere o comprimere le varie sezioni premendo sul header
     * @param position: posizione occupata dal header nella recycler view
     */
    @Override
    public void onSubheaderClicked(int position) {
        if (favoriteAdapter.isSectionExpanded(favoriteAdapter.getSectionIndex(position))) {
            favoriteAdapter.collapseSection(favoriteAdapter.getSectionIndex(position));
        } else {
            favoriteAdapter.expandSection(favoriteAdapter.getSectionIndex(position));
        }
    }

    public class SelectModeListener implements HeroSelectMode {
        /**
         * permette di attivare o disattivare il menù contestuale
         * @param size: numero di elementi selezionati al momento
         */
        @Override
        public void onSelect(int size) {
            if (mActionMode != null) {
                if (size == 0) {
                    mActionMode.finish();
                    mActionMode = null;
                }
            } else
                mActionMode = ((AppCompatActivity) Objects.requireNonNull(getActivity())).startSupportActionMode(hActionModeCallback);
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

        /**
         * permette l'eliminazione degli eroi dai preferiti quando si preme sulla voce 'elimina'
         * @param actionMode
         * @param menuItem
         * @return
         */
        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            if (menuItem.getItemId() == R.id.itemDelete) {
                favoriteAdapter.removeSelected();
                mActionMode.finish();
                FragmentTransaction fragmentTransaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.anim_fade_in, R.anim.anim_fade_out);
                FavouritesFragment favorite = new FavouritesFragment();
                fragmentTransaction.replace(R.id.fragment_container, favorite);
                fragmentTransaction.commit();
                return true;
            }
            return false;
        }

        /**
         * permette di deselezionare tutti gli elementi quando si disattiva il menù contestuale
         * @param actionMode
         */
        @Override
        public void onDestroyActionMode(ActionMode actionMode) {

            favoriteAdapter.resetSelection();
            favoriteAdapter.notifyDataChanged();
            mActionMode = null;
        }
    };

    /**
     * permette di disattivare la barra del menù contestuale, se attivo,
     * quando si esce dalla schermata dei preferiti
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mActionMode != null)
            mActionMode.finish();
    }
}