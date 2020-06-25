package it.tiburtinavalley.marvelheroes.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import it.tiburtinavalley.marvelheroes.model.Series;
import it.tiburtinavalley.marvelheroes.R;
import it.tiburtinavalley.marvelheroes.recyclerviewadapter.UrlsRecyclerView;
import it.tiburtinavalley.marvelheroes.volley.ImageApiVolley;

public class SeriesActivity extends AppCompatActivity {
    private Series series;
    private UrlsRecyclerView urlsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.series_layout);
        series = getIntent().getParcelableExtra("comic");
        Holder holder = new Holder();
        holder.setData();
    }

    class Holder {
        private ImageView ivSeriesImage;
        private TextView tvSeriesName;
        private TextView tvPageCount;
        private RecyclerView rvUrls;
        private RecyclerView.LayoutManager layoutManagerUrls;

        public Holder() {
            ivSeriesImage = findViewById(R.id.ivStoriesmg);
            tvSeriesName = findViewById(R.id.tvStoriesName);
            tvPageCount = findViewById(R.id.tvPageCount);
            rvUrls = findViewById(R.id.rvUrls);
        }

        private void setData() {
            ImageApiVolley imgVolley = new ImageApiVolley(getApplicationContext());
            imgVolley.addHeroImg(ivSeriesImage);
            if (series.getImages().size() > 0) {
                String urlThumbnail = series.getImages().get(0).getPath().replaceFirst("http", "https")
                        + "." + series.getImages().get(0).getExtension();
                Glide.with(getApplicationContext()).load(urlThumbnail).into(this.ivSeriesImage);
            }
            tvSeriesName.setText(series.getTitle());
            tvPageCount.setText(series.getPageCount());
            layoutManagerUrls = new LinearLayoutManager(SeriesActivity.this);
            rvUrls.setLayoutManager(layoutManagerUrls);
            urlsAdapter = new UrlsRecyclerView(series.getUrls());
            rvUrls.setAdapter(urlsAdapter);
        }
    }
}
