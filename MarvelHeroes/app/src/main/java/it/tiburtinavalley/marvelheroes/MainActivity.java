package it.tiburtinavalley.marvelheroes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import it.tiburtinavalley.marvelheroes.Model.HeroModel;
import it.tiburtinavalley.marvelheroes.Volley.ImageApiVolley;
import it.tiburtinavalley.marvelheroes.Volley.MarvelApiVolley;

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

    class Holder implements View.OnClickListener {
        final RecyclerView rvHeroes;
        final EditText etHeroSearch;
        final Button btnSearch;

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
                    HeroAdapter mAdapter = new HeroAdapter(heroes);
                    rvHeroes.setAdapter(mAdapter);
                }
            };

            this.etHeroSearch = findViewById(R.id.etHeroSearch);
            this.btnSearch = findViewById(R.id.btnSearch);
            this.btnSearch.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.btnSearch) {
                String nameStartsWith = etHeroSearch.getText().toString();
                volleyMarvel.getCharactersInfo(nameStartsWith);
            }
        }
    }

    private class HeroAdapter extends RecyclerView.Adapter<HeroAdapter.Holder> implements View.OnClickListener {
        private final List<HeroModel> heroes;

        HeroAdapter(List<HeroModel> all) {
            heroes = new ArrayList<>();
            heroes.addAll(all);
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ConstraintLayout cl;
            cl = (ConstraintLayout) LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.hero_layout, parent, false);
            cl.setOnClickListener(this);
            return new Holder(cl);
        }

        // This method sets the layout of the hero
        @Override
        public void onBindViewHolder(@NonNull Holder holder, int position) {
            HeroModel hero = heroes.get(position);
            holder.tvHeroName.setText(hero.getName());
            imgVolley.addHeroImg(holder.ivHeroPic);
            if (!hero.getThumbnail().getPath().equalsIgnoreCase("")
                    && !hero.getThumbnail().getExtension().equalsIgnoreCase("")) {
                imgVolley.getImageFromUrl(hero.getThumbnail().getPath().replaceFirst("http", "https")
                        + "." + hero.getThumbnail().getExtension());
            }
        }

        @Override
        public int getItemCount() {
            return heroes.size();
        }

        //
        @Override
        public void onClick(View v) {
            int position = ((RecyclerView) v.getParent()).getChildAdapterPosition(v);
            HeroModel hero = heroes.get(position);

            Intent i = new Intent(getApplicationContext(), HeroDetailActivity.class);
            i.putExtra("hero", hero);
            startActivity(i);
        }

        class Holder extends RecyclerView.ViewHolder {
            final TextView tvHeroName;
            final ImageView ivHeroPic;

            Holder(@NonNull View itemView) {
                super(itemView);
                tvHeroName = itemView.findViewById(R.id.tvHeroName);
                ivHeroPic = itemView.findViewById(R.id.ivHeroPic);
            }
        }
    }
}
