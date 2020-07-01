package it.tiburtinavalley.marvelheroes.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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


public class FavouritesFragment extends Fragment implements MainActivity.IOnBackPressed {
    private ActionMode mActionMode;
    FavoriteHeroAdapter favoriteAdapter;
    SelectModeListener smListener;
    View v;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_favourites, container, false);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("YOUR HEROES");

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

    @Override
    public boolean onBackPressed() {
        Log.w("12","ispressed");
        
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.anim_fade_in, R.anim.anim_fade_out);
        HomeFragment home= new HomeFragment();
        fragmentTransaction.replace(R.id.fragment_container,home);
        //fragmentTransaction.addToBackStack(null);   cosi da poter poi chiudere l'app direttamente se premuto back nella home
        fragmentTransaction.commit();
        return true;
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
            Log.w("ww","destroy");
            //mActionMode.finish();
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.anim_fade_in, R.anim.anim_fade_out);
            FavouritesFragment favorite = new FavouritesFragment();
            fragmentTransaction.replace(R.id.fragment_container,favorite);
            //fragmentTransaction.addToBackStack(null);   cosi da poter poi chiudere l'app direttamente se premuto back nella home
            fragmentTransaction.commit();
        }
    };
}