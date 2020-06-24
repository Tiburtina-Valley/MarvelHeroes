package it.tiburtinavalley.marvelheroes.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import it.tiburtinavalley.marvelheroes.model.HeroModel;
import it.tiburtinavalley.marvelheroes.R;
import it.tiburtinavalley.marvelheroes.recyclerviewadapter.HeroAdapter;
import it.tiburtinavalley.marvelheroes.volley.ImageApiVolley;
import it.tiburtinavalley.marvelheroes.volley.MarvelApiVolley;

public class MainActivity extends AppCompatActivity {
    MarvelApiVolley volleyMarvel;
    ImageApiVolley imgVolley;
    private Holder holder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        holder = new Holder();
        imgVolley = new ImageApiVolley(getApplicationContext());

    }
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

    class Holder {
        final RecyclerView rvHeroes;
        final EditText etHeroSearch;

        public Holder() {
            this.rvHeroes = findViewById(R.id.rvHeroes);

            volleyMarvel = new MarvelApiVolley(getApplicationContext()) {

                @Override
                public void fillList(List<HeroModel> heroes) {
                    fillRecView(heroes);
                }

                private void fillRecView(List<HeroModel> heroes) {
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
                    rvHeroes.setLayoutManager(layoutManager);
                    HeroAdapter mAdapter = new HeroAdapter(heroes, getApplicationContext());
                    rvHeroes.setAdapter(mAdapter);
                }
            };

            this.etHeroSearch = findViewById(R.id.etHeroSearch);
            this.etHeroSearch.setOnEditorActionListener(new ConfButtonListener());
        }


        public class ConfButtonListener  implements TextView.OnEditorActionListener {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String nameStartsWith = etHeroSearch.getText().toString();
                    volleyMarvel.getCharactersInfo(nameStartsWith);
                    hideSoftKeyboard(MainActivity.this);
                    return true;
                }
                else {
                    return false;
                }
            }
        }

        public void hideSoftKeyboard(Activity activity){
            InputMethodManager imm = (InputMethodManager)activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }

    }
}
