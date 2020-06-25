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

import it.tiburtinavalley.marvelheroes.model.Creators;
import it.tiburtinavalley.marvelheroes.model.Events;
import it.tiburtinavalley.marvelheroes.R;
import it.tiburtinavalley.marvelheroes.model.HeroModel;
import it.tiburtinavalley.marvelheroes.recyclerviewadapter.CreatorsAdapter;
import it.tiburtinavalley.marvelheroes.recyclerviewadapter.HeroAdapter;
import it.tiburtinavalley.marvelheroes.recyclerviewadapter.UrlsRecyclerView;
import it.tiburtinavalley.marvelheroes.volley.CreatorsVolley;
import it.tiburtinavalley.marvelheroes.volley.ImageApiVolley;
import it.tiburtinavalley.marvelheroes.volley.MarvelApiVolley;

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
        private MarvelApiVolley heroVolley;
        private CreatorsVolley creatorsVolley;
        private HeroAdapter heroAdapter;
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
            setRecyclerViews();

            heroVolley = new MarvelApiVolley(getApplicationContext()) {
                @Override
                public void fillList(List<HeroModel> heroes) {
                    heroAdapter = new HeroAdapter(heroes, getApplicationContext());
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
    }

        private void setData() {


             tvEventName.setText(event.getTitle());
             startDate.setText(event.getStart());
             endDate.setText(event.getEnd());
             description.setText(event.getDescription());

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

            urlsAdapter = new UrlsRecyclerView(event.getUrls());
            rvUrls.setAdapter(urlsAdapter);

            }

        }
    }

