package it.tiburtinavalley.marvelheroes.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import it.tiburtinavalley.marvelheroes.model.Events;
import it.tiburtinavalley.marvelheroes.R;
import it.tiburtinavalley.marvelheroes.recyclerviewadapter.UrlsRecyclerView;
import it.tiburtinavalley.marvelheroes.volley.ImageApiVolley;

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

    class Holder {
        private ImageView ivEventImage;
        private TextView tvEventName;
        private TextView tvPageCount;
        private RecyclerView rvUrls;
        private RecyclerView.LayoutManager layoutManagerUrls;

        public Holder() {
            ivEventImage = findViewById(R.id.ivStoriesmg);
            tvEventName = findViewById(R.id.tvStoriesName);
            tvPageCount = findViewById(R.id.tvPageCount);
            rvUrls = findViewById(R.id.rvUrls);
            setData();
        }

        private void setData() {
            ImageApiVolley imgVolley = new ImageApiVolley(getApplicationContext());
            imgVolley.addHeroImg(ivEventImage);
          /*  if (event.getImages().size() > 0) {
                String urlThumbnail = event.getImages().get(0).getPath().replaceFirst("http", "https")
                        + "." + event.getImages().get(0).getExtension();
                Glide.with(getApplicationContext()).load(urlThumbnail).into(this.ivEventImage);
            }*/
            tvEventName.setText(event.getTitle());
            tvPageCount.setText(event.getPageCount());
            layoutManagerUrls = new LinearLayoutManager(EventsActivity.this);
            rvUrls.setLayoutManager(layoutManagerUrls);
            urlsAdapter = new UrlsRecyclerView(event.getUrls());
            rvUrls.setAdapter(urlsAdapter);
        }
    }
}
