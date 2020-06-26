package it.tiburtinavalley.marvelheroes.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.internal.$Gson$Preconditions;

import java.util.List;

import it.tiburtinavalley.marvelheroes.model.Comics;
import it.tiburtinavalley.marvelheroes.model.Creators;
import it.tiburtinavalley.marvelheroes.model.Events;
import it.tiburtinavalley.marvelheroes.R;
import it.tiburtinavalley.marvelheroes.model.HeroModel;
import it.tiburtinavalley.marvelheroes.model.Series;
import it.tiburtinavalley.marvelheroes.recyclerviewadapter.ComicsAdapter;
import it.tiburtinavalley.marvelheroes.recyclerviewadapter.CreatorsAdapter;
import it.tiburtinavalley.marvelheroes.recyclerviewadapter.HeroAdapter;
import it.tiburtinavalley.marvelheroes.recyclerviewadapter.HeroDetailAdapter;
import it.tiburtinavalley.marvelheroes.recyclerviewadapter.SeriesAdapter;
import it.tiburtinavalley.marvelheroes.recyclerviewadapter.UrlsRecyclerView;
import it.tiburtinavalley.marvelheroes.volley.ComicsVolley;
import it.tiburtinavalley.marvelheroes.volley.CreatorsVolley;
import it.tiburtinavalley.marvelheroes.volley.ImageApiVolley;
import it.tiburtinavalley.marvelheroes.volley.MarvelApiVolley;
import it.tiburtinavalley.marvelheroes.volley.SeriesVolley;

public class EventsActivity extends AppCompatActivity {
    private Events event;
    private UrlsRecyclerView urlsAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.events_layout);
        event = getIntent().getParcelableExtra("event");
         Holder holder = new Holder();
    }
//start,and,title,description,urls,thumbnail,creators
    class Holder {
        private ImageView ivEventImage;
        private TextView tvEventName;
        private TextView startDate;
        private TextView endDate;
        private TextView description;
        private RecyclerView rvUrls;
        private RecyclerView rvCreators;
        private RecyclerView rvHeroes;
        private RecyclerView rvComics;
        private RecyclerView rvSeries;
        private MarvelApiVolley heroVolley;
        private CreatorsVolley creatorsVolley;
        private ComicsVolley comicsVolley;
        private SeriesVolley seriesVolley;
        private HeroDetailAdapter heroAdapter;
        private ComicsAdapter comicsAdapter;
        private SeriesAdapter seriesAdapter;
        private CreatorsAdapter creatorsAdapter;
        private UrlsRecyclerView urlsAdapter;


        public Holder() {
            ivEventImage = findViewById(R.id.ivEventImg);
            tvEventName = findViewById(R.id.tvEventTitle);
            startDate=findViewById(R.id.tvEventStartDate);
            endDate=findViewById(R.id.tvEventEndDate);
            description=findViewById(R.id.tvEventDescription);
            rvUrls = findViewById(R.id.rvUrlsEvent);
            rvCreators=findViewById(R.id.rvEventCreators);
            rvHeroes=findViewById(R.id.rvEventHeroes);
            rvComics=findViewById(R.id.rvEventComics);
            rvSeries=findViewById(R.id.rvEventSeries);

            setRecyclerViews();

            heroVolley = new MarvelApiVolley(getApplicationContext()) {
                @Override
                public void fillList(List<HeroModel> heroes) {
                    heroAdapter = new HeroDetailAdapter(heroes, getApplicationContext());
                    rvHeroes.setAdapter(heroAdapter);
                }
            };

            creatorsVolley = new CreatorsVolley(getApplicationContext()) {
                @Override
                public void fillCreatorsInfo(List<Creators> creatorsList) {
                    creatorsAdapter = new CreatorsAdapter(creatorsList, getApplicationContext());
                    rvCreators.setAdapter(creatorsAdapter);
                }
            };

            comicsVolley = new ComicsVolley(getApplicationContext()) {
                @Override
                public void fillComics(List<Comics> comicsList) {
                    comicsAdapter = new ComicsAdapter(comicsList, getApplicationContext());
                    rvComics.setAdapter(comicsAdapter);
                }};

            seriesVolley=new SeriesVolley(getApplicationContext()) {
                @Override
                public void fillSeries(List<Series> seriesList) {
                    seriesAdapter=new SeriesAdapter(seriesList,getApplicationContext());
                    rvSeries.setAdapter(seriesAdapter);
                }
            };



            setData();
        }
    private void setRecyclerViews(){
        LinearLayoutManager layoutManagerHeroes = new LinearLayoutManager(
                EventsActivity.this, RecyclerView.HORIZONTAL, false);
        rvHeroes.setLayoutManager(layoutManagerHeroes);

        LinearLayoutManager layoutManagerUrls = new LinearLayoutManager(
                EventsActivity.this, RecyclerView.VERTICAL, false);
        rvUrls.setLayoutManager(layoutManagerUrls);

        LinearLayoutManager layoutManagerCreators = new LinearLayoutManager(
                EventsActivity.this, RecyclerView.HORIZONTAL, false);
        rvCreators.setLayoutManager(layoutManagerCreators);

        LinearLayoutManager layoutManagerComics = new LinearLayoutManager(
                EventsActivity.this, RecyclerView.HORIZONTAL, false);
        rvComics.setLayoutManager(layoutManagerComics);

        LinearLayoutManager layoutManagerSeries = new LinearLayoutManager(
                EventsActivity.this, RecyclerView.HORIZONTAL, false);
        rvSeries.setLayoutManager(layoutManagerSeries);
    }



        private void setData() {


             tvEventName.setText(event.getTitle());
             startDate.setText(event.getStart());
             endDate.setText(event.getEnd());
            if (event.getDescription() != null) {
                description.setText(event.getDescription());
            }
            else {
                description.setText(R.string.noDescription);
            }


             ImageApiVolley imgVolley;
             imgVolley = new ImageApiVolley(getApplicationContext());
             imgVolley.addHeroImg(ivEventImage);

            String urlThumbnail = event.getThumbnail().getPath().replaceFirst("http", "https")
                    + "." + event.getThumbnail().getExtension();
            Log.w("1",urlThumbnail);
            Glide.with(getApplicationContext()).load(urlThumbnail).into(ivEventImage);

            String id = event.getId();
            Log.w("1",id);
             heroVolley.getHeroesFromEvents(id);
             creatorsVolley.getCreatorsByEvents(id);
             seriesVolley.getSeriesByEvent(id);
             comicsVolley.getComicsByEvent(id);


            urlsAdapter = new UrlsRecyclerView(event.getUrls());
            rvUrls.setAdapter(urlsAdapter);

            }

        }
    }

