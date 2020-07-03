package it.tiburtinavalley.marvelheroes.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import it.tiburtinavalley.marvelheroes.fragment.FavouritesFragment;
import it.tiburtinavalley.marvelheroes.fragment.HomeFragment;
import it.tiburtinavalley.marvelheroes.fragment.SearchFragment;
import it.tiburtinavalley.marvelheroes.R;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        setFragment(new HomeFragment());
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = item -> {

        switch(item.getItemId()) {
            case R.id.action_home:
                setFragment(new HomeFragment());
                return true;

            case R.id.action_favourites:
                setFragment(new FavouritesFragment());
                return true;

            case R.id.action_search:
                setFragment(new SearchFragment());
                return true;


        }
        return true;
    };

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true; }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.AboutBtn) {
            Context appContext=getApplicationContext();
            Intent i = new Intent(appContext, AboutActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            appContext.startActivity(i); }
        else {
            return super.onContextItemSelected(item); }
        return true; }

    private void setFragment(Fragment fragment) {
        hideKeyboard(this);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.anim_fade_in, R.anim.anim_fade_out);
        fragmentTransaction.replace(R.id.fragment_container,fragment);
        //fragmentTransaction.addToBackStack(null);   cosi da poter poi chiudere l'app direttamente se premuto back nella home
        fragmentTransaction.commit();
    }
    public interface IOnBackPressed {
        boolean onBackPressed();
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        assert imm != null;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (!(fragment instanceof IOnBackPressed) || !((IOnBackPressed) fragment).onBackPressed()) {
            super.onBackPressed();
        }
    }
}