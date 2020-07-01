package it.tiburtinavalley.marvelheroes;

import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

import androidx.fragment.app.FragmentTransaction;

import it.tiburtinavalley.marvelheroes.fragment.HomeFragment;
import it.tiburtinavalley.marvelheroes.recyclerviewadapter.FavoriteHeroAdapter;

//classe che implementa la ActionMode.Callback, che rappresenta una modalità contestuale dell'intrefaccia utente
public class HeroActionMode implements ActionMode.Callback {
    private FavoriteHeroAdapter favAdapter;

    public HeroActionMode(FavoriteHeroAdapter fav){
        favAdapter = fav;
    }

    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        actionMode.getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.itemDelete:
                favAdapter.removeSelected();
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
}
