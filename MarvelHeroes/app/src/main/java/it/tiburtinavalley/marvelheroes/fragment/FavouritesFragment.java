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
    private List<HeroEntity> heroes;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_favourites, container, false);
        Objects.requireNonNull(((MainActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle(R.string.title_favourites);

        RecyclerView rvHeroes;
        rvHeroes = v.findViewById(R.id.rvFavouriteHeroes);
        GridDividerDecoration gridDividerDecoration = new GridDividerDecoration(getContext());

        int numberOfColumns = 2;
        rvHeroes.addItemDecoration(gridDividerDecoration);
        GridLayoutManager glm = new GridLayoutManager(getContext(), numberOfColumns);
        rvHeroes.setLayoutManager(glm);

        heroes = AppDatabase.getInstance(getActivity().getApplicationContext()).heroDao().getHeroList();
        if (heroes != null) {
            smListener = new SelectModeListener();
            favoriteAdapter = new FavoriteHeroAdapter(heroes, getContext(), smListener);
            favoriteAdapter.setOnItemClickListener(this);
            favoriteAdapter.setGridLayoutManager(glm);
            rvHeroes.setAdapter(favoriteAdapter);
        }

        return v;
    }

    @Override
    public boolean onBackPressed() {
        Log.w("12", getString(R.string.msg_is_pressed));

        FragmentTransaction fragmentTransaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.anim_fade_in, R.anim.anim_fade_out);
        HomeFragment home = new HomeFragment();
        fragmentTransaction.replace(R.id.fragment_container, home);
        //fragmentTransaction.addToBackStack(null);   cosi da poter poi chiudere l'app direttamente se premuto back nella home
        fragmentTransaction.commit();
        return true;
    }

    @Override
    public void onSubheaderClicked(int position) {
        if (favoriteAdapter.isSectionExpanded(favoriteAdapter.getSectionIndex(position))) {
            favoriteAdapter.collapseSection(favoriteAdapter.getSectionIndex(position));
        } else {
            favoriteAdapter.expandSection(favoriteAdapter.getSectionIndex(position));
        }
    }

    public class SelectModeListener implements HeroSelectMode {

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

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            if (menuItem.getItemId() == R.id.itemDelete) {
                favoriteAdapter.removeSelected();
                mActionMode.finish();
                FragmentTransaction fragmentTransaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.anim_fade_in, R.anim.anim_fade_out);
                FavouritesFragment favorite = new FavouritesFragment();
                fragmentTransaction.replace(R.id.fragment_container, favorite);
                //fragmentTransaction.addToBackStack(null);   cosi da poter poi chiudere l'app direttamente se premuto back nella home
                fragmentTransaction.commit();
                return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
           FragmentTransaction fragmentTransaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
           fragmentTransaction.setCustomAnimations(R.anim.anim_fade_in, R.anim.anim_fade_out);
           FavouritesFragment favorite = new FavouritesFragment();
           fragmentTransaction.replace(R.id.fragment_container, favorite);
           //fragmentTransaction.addToBackStack(null);   cosi da poter poi chiudere l'app direttamente se premuto back nella homefragmentTransaction.commit();
    }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mActionMode != null)
            mActionMode.finish();
    }
}