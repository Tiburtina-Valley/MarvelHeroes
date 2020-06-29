package it.tiburtinavalley.marvelheroes.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import it.tiburtinavalley.marvelheroes.dao.AppDatabase;
import it.tiburtinavalley.marvelheroes.entity.HeroEntity;
import it.tiburtinavalley.marvelheroes.entity.RelatedEntity;
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


public class HeroDetailActivity extends AppCompatActivity{

    private HeroModel hm;
    private ComicsVolley cVolley;
    private SeriesVolley seVolley;
    private EventsVolley eVolley;
    private StoriesVolley stVolley;
    private ImageApiVolley imgVolley;

    private Boolean isFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppThemeNoBar);


        setContentView(R.layout.hero_detail_layout);
        imgVolley = new ImageApiVolley(getApplicationContext());
        hm = getIntent().getParcelableExtra("hero");
        Holder holder = new Holder();
        holder.setDetails(hm);
    }

    class Holder  implements View.OnClickListener { //implements View.OnClickListener {
        private final RecyclerView rvComics;
        private final RecyclerView rvSeries;
        private final RecyclerView rvEvents;
        private final RecyclerView rvStories;
        private ImageView ivHeroPhoto;
        private TextView tvHeroName;
        private TextView tvHeroDescription;
        private ComicsAdapter cAdapter;
        private SeriesAdapter sAdapter;
        private StoriesAdapter stAdapter;
        private EventsAdapter eAdapter;
        private ImageView ivStar;
        private FloatingActionButton btnAddFavorite;
        private Toolbar toolbar;

        public Holder() {

            toolbar = (Toolbar) findViewById(R.id.anim_toolbar);
            setSupportActionBar(toolbar);

            rvEvents=findViewById(R.id.rvEvents);
            rvStories=findViewById(R.id.rvStories);
            rvComics = findViewById(R.id.rvComics);
            rvSeries = findViewById(R.id.rvSeries);
            ivHeroPhoto = findViewById(R.id.ivHeroPhoto);
            tvHeroName = findViewById(R.id.tvHeroName);
            //ivStar = findViewById(R.id.ivStar);
            //ivStar.setOnClickListener(this);
            tvHeroDescription = findViewById(R.id.tvHeroDescription);

            btnAddFavorite = findViewById(R.id.btnAddFavorite);
            btnAddFavorite.setOnClickListener(this);

            initFavoriteBtn();

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
                    //controlla se sono stati trovati dei fumetti legati agli eroi
                    if(comicsList.isEmpty()){
                        TextView tvComics = findViewById(R.id.tvComicsHeroes); // prende la TextView da oscurare
                        tvComics.setTextSize(0);

                        ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) tvComics.getLayoutParams();
                        marginParams.setMargins(0, 0, 0, 0); // setta i margini per non lasciare spazi in più
                    }
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
            getSupportActionBar().setTitle(hm.getName());

            if(hero.getDescription() != null && !hero.getDescription().equalsIgnoreCase("")) {
                this.tvHeroDescription.setText(hero.getDescription());
            }
            else{
                tvHeroDescription.setText(R.string.noDescription);
            }
            imgVolley.addHeroImg(this.ivHeroPhoto);
            imgVolley.getImageFromUrl(hero.getThumbnail().getPath().replaceFirst("http", "https")
                    + "." + hero.getThumbnail().getExtension());
            // TODO: fill comics, series and stories
            cVolley.getComicsInfo(hm.getId());
            seVolley.getSeriesInfo(hm.getId());
            stVolley.getStoriesInfo(hm.getId());
            eVolley.getEventInfo(hm.getId());
        }


        public void onClick(View v) {
            if (v.getId() == R.id.btnAddFavorite) {

                if (!isFavorite) {
                    HeroEntity hero = new HeroEntity(Integer.parseInt(hm.getId()), hm.getName(), "", hm.getDescription());
                    AppDatabase.getInstance(getApplicationContext()).heroDao().insertHero(hero);


                    Toast toast = Toast.makeText(getApplicationContext(), "hero saved", Toast.LENGTH_LONG);
                    toast.show();

                } else {
                    AppDatabase.getInstance(getApplicationContext()).heroDao().deleteHeroFromId(Integer.parseInt(hm.getId()));

                    Toast toast = Toast.makeText(getApplicationContext(), "hero removed", Toast.LENGTH_LONG);
                    toast.show();
                }

                initFavoriteBtn();
            }
        }


        private void initFavoriteBtn()
        {
            isFavorite = AppDatabase.getInstance(getApplicationContext()).heroDao().hasHero(Integer.parseInt(hm.getId()));

            if (isFavorite){
                 btnAddFavorite.setImageResource(R.drawable.ic_action_favourites);
            } else {
                btnAddFavorite.setImageResource(R.drawable.ic_action_favourite_border);
            }
        }


        /*@Override
        public void onClick(View v) {
            if (v.getId() == R.id.ivStar) {
                if (ivStar.getImageAlpha() == android.R.drawable.btn_star_big_on) {
                    ivStar.setImageResource(android.R.drawable.btn_star_big_off);
                }
                else {
                    ivStar.setImageResource(android.R.drawable.btn_star_big_on);
                }
            }
        }*/
    }
}
