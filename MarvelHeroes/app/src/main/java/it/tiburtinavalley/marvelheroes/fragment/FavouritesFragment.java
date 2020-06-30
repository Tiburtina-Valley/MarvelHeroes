package it.tiburtinavalley.marvelheroes.fragment;

import android.os.Bundle;
import android.text.Layout;
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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import it.tiburtinavalley.marvelheroes.HeroSelectMode;
import it.tiburtinavalley.marvelheroes.R;
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
        RecyclerView rvHeroes;
        rvHeroes = v.findViewById(R.id.rvFavouriteHeroes);
        LinearLayoutManager layoutManagerHeroes = new LinearLayoutManager(
                getActivity(), RecyclerView.VERTICAL, false);
        rvHeroes.setLayoutManager(layoutManagerHeroes);
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

    public class SelectModeListener implements HeroSelectMode{

        @Override
        public void onSelect(int size) {
            System.out.println("Qui\n");
            if(mActionMode != null){
                if(size == 0){
                    mActionMode.finish();
                }
                return;
            }
            mActionMode = ((AppCompatActivity)getActivity()).startSupportActionMode(hActionModeCallback);
        }
    }

    private androidx.appcompat.view.ActionMode.Callback hActionModeCallback = new androidx.appcompat.view.ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(androidx.appcompat.view.ActionMode actionMode, Menu menu) {
            actionMode.getMenuInflater().inflate(R.menu.menu_delete, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(androidx.appcompat.view.ActionMode actionMode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(androidx.appcompat.view.ActionMode actionMode, MenuItem menuItem) {
            switch (menuItem.getItemId()){
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
        }
    };
}