package it.tiburtinavalley.marvelheroes.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import it.tiburtinavalley.marvelheroes.model.Comics;
import it.tiburtinavalley.marvelheroes.model.Events;
import it.tiburtinavalley.marvelheroes.model.HeroModel;
import it.tiburtinavalley.marvelheroes.model.Series;
import it.tiburtinavalley.marvelheroes.R;
import it.tiburtinavalley.marvelheroes.model.Stories;
import it.tiburtinavalley.marvelheroes.recyclerviewadapter.ComicsAdapter;
import it.tiburtinavalley.marvelheroes.recyclerviewadapter.EventsAdapter;
import it.tiburtinavalley.marvelheroes.recyclerviewadapter.SeriesAdapter;
import it.tiburtinavalley.marvelheroes.recyclerviewadapter.StoriesAdapter;
import it.tiburtinavalley.marvelheroes.volley.ComicsVolley;
import it.tiburtinavalley.marvelheroes.volley.EventsVolley;
import it.tiburtinavalley.marvelheroes.volley.ImageApiVolley;
import it.tiburtinavalley.marvelheroes.volley.SeriesVolley;
import it.tiburtinavalley.marvelheroes.volley.StoriesVolley;


public class HeroDetailActivity extends AppCompatActivity {

    private HeroModel hm;
    private ComicsVolley cVolley;
    private SeriesVolley seVolley;
    private EventsVolley eVolley;
    private StoriesVolley stVolley;
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
        private final RecyclerView rvEvents;
        private final RecyclerView rvStories;
        private ImageView ivHeroPhoto;
        private TextView tvHeroName;
        private TextView tvHeroId;
        private TextView tvHeroDescription;
        private ComicsAdapter cAdapter;
        private SeriesAdapter sAdapter;
        private StoriesAdapter stAdapter;
        private EventsAdapter eAdapter;

        public Holder() {
            rvEvents=findViewById(R.id.rvEvents);
            rvStories=findViewById(R.id.rvStories);
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

            LinearLayoutManager layoutManagerStories = new LinearLayoutManager(
                    HeroDetailActivity.this, RecyclerView.HORIZONTAL, false);
                      rvStories.setLayoutManager(layoutManagerStories);

            LinearLayoutManager layoutManagerEvents = new LinearLayoutManager(
                    HeroDetailActivity.this, RecyclerView.HORIZONTAL, false);
            rvEvents.setLayoutManager(layoutManagerEvents);

            cVolley = new ComicsVolley(getApplicationContext()) {

                @Override
                public void fillComics(List<Comics> comicsList) {
                    cAdapter = new ComicsAdapter(comicsList, getApplicationContext());
                    rvComics.setAdapter(cAdapter);
                }
            };
            seVolley = new SeriesVolley(getApplicationContext()) {
                @Override
                public void fillSeries(List<Series> seriesList) {
                    sAdapter = new SeriesAdapter(seriesList, getApplicationContext());
                    rvSeries.setAdapter(sAdapter);
                }
            };
            eVolley= new EventsVolley(getApplicationContext()) {
                @Override
                public void fillEvents(List<Events> eventsList) {
                    eAdapter=new EventsAdapter(eventsList,getApplicationContext());
                    rvEvents.setAdapter(eAdapter);

                }
            };
            stVolley= new StoriesVolley(getApplicationContext()) {
                @Override
                public void fillStories(List<Stories> storiesList) {
                    stAdapter = new StoriesAdapter(storiesList, getApplicationContext());
                    rvStories.setAdapter(stAdapter);

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
            seVolley.getStoriesInfo(hm.getId());
            stVolley.getStoriesInfo(hm.getId());
            eVolley.getEventInfo(hm.getId());
        }
    }
}
