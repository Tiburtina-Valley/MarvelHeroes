package it.tiburtinavalley.marvelheroes.activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.tiburtinavalley.marvelheroes.model.Comics;
import it.tiburtinavalley.marvelheroes.model.Series;
import it.tiburtinavalley.marvelheroes.model.Stories;
import it.tiburtinavalley.marvelheroes.R;
import it.tiburtinavalley.marvelheroes.recyclerviewadapter.ComicsAdapter;
import it.tiburtinavalley.marvelheroes.recyclerviewadapter.SeriesAdapter;
import it.tiburtinavalley.marvelheroes.recyclerviewadapter.UrlsRecyclerView;
import it.tiburtinavalley.marvelheroes.volley.ComicsVolley;
import it.tiburtinavalley.marvelheroes.volley.SeriesVolley;

public class StoriesActivity extends AppCompatActivity {
    private Stories story;
    private UrlsRecyclerView urlsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stories_detail_layout);
        story = getIntent().getParcelableExtra("story");
        Holder holder = new Holder();
    }

    class Holder {
        private TextView tvStoryTitle;
        private TextView tvType;
        private RecyclerView rvComics;
        private RecyclerView rvSeries;

        public Holder() {
            tvStoryTitle = findViewById(R.id.tvStoryTitle);
            tvType = findViewById(R.id.tvType);
            rvComics = findViewById(R.id.rvStoriesComics);
            rvSeries = findViewById(R.id.rvStoriesSeries);
            setData();
        }

        private void setData() {

            tvStoryTitle.setText(story.getTitle());
            tvType.setText("Type of story : "+story.getType());
            LinearLayoutManager layoutManagerComics = new LinearLayoutManager(StoriesActivity.this);
            rvComics.setLayoutManager(layoutManagerComics);

            LinearLayoutManager layoutManagerSeries = new LinearLayoutManager(StoriesActivity.this);
            rvSeries.setLayoutManager(layoutManagerSeries);

            ComicsVolley cv = new ComicsVolley(getApplicationContext()) {
                @Override
                public void fillComics(List<Comics> comicsList) {
                    ComicsAdapter ca = new ComicsAdapter(comicsList, getApplicationContext());
                    rvComics.setAdapter(ca);
                }
            };

            SeriesVolley sv = new SeriesVolley(getApplicationContext()) {
                @Override
                public void fillSeries(List<Series> seriesList) {
                    SeriesAdapter sa = new SeriesAdapter(seriesList, getApplicationContext());
                    rvSeries.setAdapter(sa);
                }
            };

            cv.getComicsByStories(story.getId());
            sv.getSeriesByStories(story.getId());
        }
    }
}
