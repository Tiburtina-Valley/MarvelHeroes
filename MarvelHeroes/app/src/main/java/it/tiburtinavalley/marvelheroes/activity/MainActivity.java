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

/** MainActivity, la prima Activity che viene mostrata quando l'utente accede all'applicazione*/
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Aggiunge la NavigationBar nella parte bassa della schermata
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        setFragment(new HomeFragment()); // carica per default il Fragment di HomePage
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = item -> {

        // switch-case per decidere quale Fragment mostrare in base all'elemento della navbar cliccato
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

    /** Crea il menu */
    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_about, menu);
        return true; }

    /** Metodo che reagisce al click di un opzione del menu */
    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.AboutBtn) {
            Context appContext=getApplicationContext();
            Intent i = new Intent(appContext, AboutActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            appContext.startActivity(i); }
        else {
            return super.onContextItemSelected(item); }
        return true;
    }

    private void setFragment(Fragment fragment) {
        hideKeyboard(this);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        // Imposta l'animazione da applicare al cambio di fragment
        fragmentTransaction.setCustomAnimations(R.anim.anim_fade_in, R.anim.anim_fade_out);
        // Imposta il fragment dentro l'elemento fragment_container
        fragmentTransaction.replace(R.id.fragment_container,fragment);
        //fragmentTransaction.addToBackStack(null);   cosi da poter poi chiudere l'app direttamente se premuto back nella home
        fragmentTransaction.commit();
    }
    public interface IOnBackPressed {
        boolean onBackPressed();
    }

    /** metodo per nascondere la tastiera*/
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        // Cerca la View su cui c'è attualmente il focus, così da poter prendere il corretto window token
        View view = activity.getCurrentFocus();
        // Se nessuna View ha il focus, ne crea una così da poter estrarvi il window token
        if (view == null) {
            view = new View(activity);
        }
        assert imm != null;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /** metodo chiamato per catturare il click sul tasto back nei fragments*/
    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (!(fragment instanceof IOnBackPressed) || !((IOnBackPressed) fragment).onBackPressed()) {
            super.onBackPressed();
        }
    }
}