package it.tiburtinavalley.marvelheroes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import it.tiburtinavalley.marvelheroes.model.Comics;
import it.tiburtinavalley.marvelheroes.model.HeroModel;
import it.tiburtinavalley.marvelheroes.model.Series;
import it.tiburtinavalley.marvelheroes.R;
import it.tiburtinavalley.marvelheroes.recyclerviewadapters.ComicsAdapter;
import it.tiburtinavalley.marvelheroes.recyclerviewadapters.SeriesAdapter;
import it.tiburtinavalley.marvelheroes.volley.ComicsVolley;
import it.tiburtinavalley.marvelheroes.volley.ImageApiVolley;
import it.tiburtinavalley.marvelheroes.volley.SeriesVolley;


public class HeroDetailActivity extends AppCompatActivity {

    private HeroModel hm;
    private ComicsVolley cVolley;
    private SeriesVolley sVolley;
    private ImageApiVolley imgVolley;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hero_detail_layout);
        imgVolley = new ImageApiVolley(getApplicationContext());
        hm = getIntent().getParcelableExtra("hero");
        Holder holder = new Holder();
        holder.setDetails(hm);
    }

    class Holder {
        private final RecyclerView rvComics;
        private final RecyclerView rvSeries;
        private ImageView ivHeroPhoto;
        private TextView tvHeroName;
        private TextView tvHeroId;
        private TextView tvHeroDescription;
        private ComicsAdapter cAdapter;
        private SeriesAdapter sAdapter;

        public Holder() {
            rvComics = findViewById(R.id.rvComics);
            rvSeries = findViewById(R.id.rvSeries);
            ivHeroPhoto = findViewById(R.id.ivHeroPhoto);
            tvHeroId = findViewById(R.id.tvHeroId);
            tvHeroName = findViewById(R.id.tvHeroName);
            tvHeroDescription = findViewById(R.id.tvHeroDescription);
            LinearLayoutManager layoutManagerComics = new LinearLayoutManager(
                    HeroDetailActivity.this, RecyclerView.HORIZONTAL, false);
            rvComics.setLayoutManager(layoutManagerComics);

            LinearLayoutManager layoutManagerSeries = new LinearLayoutManager(
                    HeroDetailActivity.this, RecyclerView.HORIZONTAL, false);
            rvSeries.setLayoutManager(layoutManagerSeries);
            cVolley = new ComicsVolley(getApplicationContext()) {

                @Override
                public void fillComics(List<Comics> comicsList) {
                    cAdapter = new ComicsAdapter(comicsList, getApplicationContext());
                    rvComics.setAdapter(cAdapter);
                }
            };
            sVolley = new SeriesVolley(getApplicationContext()) {
                @Override
                public void fillSeries(List<Series> seriesList) {
                    sAdapter = new SeriesAdapter(seriesList, getApplicationContext());
                    rvSeries.setAdapter(sAdapter);
                }
            };
        }


        public void setDetails(HeroModel hero) {
            this.tvHeroName.setText(hero.getName());
            this.tvHeroId.setText(hero.getId());
            this.tvHeroDescription.setText(hero.getDescription());
            imgVolley.addHeroImg(this.ivHeroPhoto);
            imgVolley.getImageFromUrl(hero.getThumbnail().getPath().replaceFirst("http", "https")
                    + "." + hero.getThumbnail().getExtension());
            // TODO: fill comics, series and stories
            cVolley.getComicsInfo(hm.getId());
            sVolley.getStoriesInfo(hm.getId());
        }
    }
}
