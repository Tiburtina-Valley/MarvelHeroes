package it.tiburtinavalley.marvelheroes.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;

import it.tiburtinavalley.marvelheroes.dao.AppDatabase;
import it.tiburtinavalley.marvelheroes.entity.HeroEntity;
import it.tiburtinavalley.marvelheroes.model.Comics;
import it.tiburtinavalley.marvelheroes.model.Events;
import it.tiburtinavalley.marvelheroes.model.HeroModel;
import it.tiburtinavalley.marvelheroes.R;
import it.tiburtinavalley.marvelheroes.model.Series;
import it.tiburtinavalley.marvelheroes.recyclerviewadapter.ComicsAdapter;
import it.tiburtinavalley.marvelheroes.recyclerviewadapter.EventsAdapter;
import it.tiburtinavalley.marvelheroes.recyclerviewadapter.SeriesAdapter;
import it.tiburtinavalley.marvelheroes.volley.ComicsVolley;
import it.tiburtinavalley.marvelheroes.volley.EventsVolley;
import it.tiburtinavalley.marvelheroes.volley.SeriesVolley;


public class FavoriteHeroDetail extends AppCompatActivity{

    private HeroModel hm;
    private ComicsVolley cVolley;
    private SeriesVolley seVolley;
    private EventsVolley eVolley;

    private Boolean isFavorite = false;
    private Holder holder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppThemeNoBar);


        setContentView(R.layout.hero_detail_layout);
        hm = getIntent().getParcelableExtra("hero");
        holder = new Holder();
        holder.setRecyclerViews();
        holder.setDetails(hm);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        holder.dismissLoading();
    }

    class Holder  implements View.OnClickListener {
        private final RecyclerView rvComics;
        private final RecyclerView rvSeries;
        private final RecyclerView rvEvents;
        private ImageView ivHeroPhoto;
        private TextView tvComics;
        private TextView tvSeries;
        private TextView tvEvents;
        private TextView tvHeroDescription;
        private ComicsAdapter cAdapter;
        private SeriesAdapter sAdapter;
        private EventsAdapter eAdapter;
        private FloatingActionButton btnAddFavorite;
        private Toolbar toolbar;
        private ProgressBar loading;
        private ConstraintLayout layout;

        private int loading_count = 0;

        public Holder() {

            toolbar = (Toolbar) findViewById(R.id.anim_toolbar);
            setSupportActionBar(toolbar);
            tvComics=findViewById(R.id.tvComics);
            tvSeries=findViewById(R.id.tvSeries);
            tvEvents=findViewById(R.id.tvEvents);
            rvEvents=findViewById(R.id.rvEvents);
            rvComics = findViewById(R.id.rvComics);
            rvSeries = findViewById(R.id.rvSeries);
            ivHeroPhoto = findViewById(R.id.ivHeroPhoto);
            tvHeroDescription = findViewById(R.id.tvHeroDescription);

            btnAddFavorite = findViewById(R.id.btnAddFavorite);
            btnAddFavorite.setOnClickListener(this);
            loading = findViewById(R.id.progress_loader);
            layout = findViewById(R.id.linearLayout);

            initFavoriteBtn();

            cVolley = new ComicsVolley(getApplicationContext()) {
                @Override
                public void fillComics(List<Comics> comicsList) {
                    //controlla se sono stati trovati dei fumetti legati agli eroi
                    if(comicsList.isEmpty()){
                        TextView tvComics = findViewById(R.id.tvComics); // prende la TextView da oscurare
                        tvComics.setTextSize(0);

                        ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) tvComics.getLayoutParams();
                        marginParams.setMargins(0, 0, 0, 0); // setta i margini per non lasciare spazi in più
                    }
                    cAdapter = new ComicsAdapter(comicsList, getApplicationContext());
                    rvComics.setAdapter(cAdapter);
                    if (cAdapter.getItemCount() == 0) {  //nasconde recyclerView e textView nel caso in cui la ricerca non dia risultati
                        tvComics.setTextSize(0);
                        tvComics.setVisibility(View.INVISIBLE);
                        ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) tvComics.getLayoutParams();
                        marginParams.setMargins(0, 0, 0, 0);}
                    loading_count++;
                    dismissLoading();
                }
            };

            seVolley = new SeriesVolley(getApplicationContext()) {
                @Override
                public void fillSeries(List<Series> seriesList) {
                    sAdapter = new SeriesAdapter(seriesList, getApplicationContext());
                    rvSeries.setAdapter(sAdapter);
                    if (sAdapter.getItemCount() == 0) {  //nasconde recyclerView e textView nel caso in cui la ricerca non dia risultati
                        tvSeries.setTextSize(0);
                        tvSeries.setVisibility(View.INVISIBLE);
                        ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) tvSeries.getLayoutParams();
                        marginParams.setMargins(0, 0, 0, 0);}
                    loading_count++;
                    dismissLoading();
                }
            };

            eVolley= new EventsVolley(getApplicationContext()) {
                @Override
                public void fillEvents(List<Events> eventsList) {
                    eAdapter=new EventsAdapter(eventsList,getApplicationContext());
                    rvEvents.setAdapter(eAdapter);
                    if (eAdapter.getItemCount() == 0) {  //nasconde recyclerView e textView nel caso in cui la ricerca non dia risultati
                        tvEvents.setTextSize(0);
                        tvEvents.setVisibility(View.INVISIBLE);
                        ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) tvEvents.getLayoutParams();
                        marginParams.setMargins(0, 0, 0, 0);}
                    loading_count++;
                    dismissLoading();

                }
            };
        }

        private void setRecyclerViews() {
            LinearLayoutManager layoutManagerComics = new LinearLayoutManager(
                    FavoriteHeroDetail.this, RecyclerView.HORIZONTAL, false);
            rvComics.setLayoutManager(layoutManagerComics);

            LinearLayoutManager layoutManagerSeries = new LinearLayoutManager(
                    FavoriteHeroDetail.this, RecyclerView.HORIZONTAL, false);
            rvSeries.setLayoutManager(layoutManagerSeries);

            LinearLayoutManager layoutManagerEvents = new LinearLayoutManager(
                    FavoriteHeroDetail.this, RecyclerView.HORIZONTAL, false);
            rvEvents.setLayoutManager(layoutManagerEvents);
        }

        public void setDetails(HeroModel hero) {
            Objects.requireNonNull(getSupportActionBar()).setTitle(hm.getName());

            if(hero.getDescription() != null && !hero.getDescription().equalsIgnoreCase("")) {
                this.tvHeroDescription.setText(hero.getDescription());
            }
            else{
                tvHeroDescription.setText(R.string.tv_noDescription);
            }

            if (hero.getResourceURI() != null) {
                Log.w("1","resource");
                String urlThumbnail = hero.getResourceURI().replaceFirst("http", "https")
                        + ".jpg";
                Glide.with(getApplicationContext()).load(urlThumbnail).into(this.ivHeroPhoto);
            }
            // TODO: fill comics, series and stories

            cVolley.getComicsRelatedToHero(hm.getId());
            seVolley.getSeriesRelatedToHero(hm.getId());
            eVolley.getEventInfo(hm.getId());
            dismissLoading();
        }


        public void onClick(View v) {
            if (v.getId() == R.id.btnAddFavorite) {

                if (!isFavorite) {
                    HeroEntity hero = new HeroEntity(Integer.parseInt(hm.getId()), hm.getName(),hm.getThumbnail().getPath(), hm.getDescription());
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

        private void dismissLoading() {
            if (loading_count >= 3) {
                loading.setVisibility(View.GONE);
                layout.setVisibility(View.VISIBLE);
            }
        }
    }
}