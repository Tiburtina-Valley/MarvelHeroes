package it.tiburtinavalley.marvelheroes.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import it.tiburtinavalley.marvelheroes.model.Events;
import it.tiburtinavalley.marvelheroes.R;
import it.tiburtinavalley.marvelheroes.recyclerviewadapter.UrlsRecyclerView;
import it.tiburtinavalley.marvelheroes.volley.ImageApiVolley;

public class EventsActivity extends AppCompatActivity {
    private Events event;
    private UrlsRecyclerView urlsAdapter;
    //Holder holder=new Holder();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.events_layout);
        event = getIntent().getParcelableExtra("event");
        Log.w("1",event.getTitle());
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
        private RecyclerView.LayoutManager layoutManagerUrls;

        public Holder() {
            ivEventImage = findViewById(R.id.ivEventImg);
            tvEventName = findViewById(R.id.tvEventTitle);
            startDate=findViewById(R.id.tvEventStartDate);
            endDate=findViewById(R.id.tvEventEndDate);
            description=findViewById(R.id.tvEventDescription);
            rvUrls = findViewById(R.id.rvUrlsEvent);
             setData();
        }

        private void setData() {


             tvEventName.setText(event.getTitle());
             Log.w("1",event.getTitle());
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

            layoutManagerUrls = new LinearLayoutManager(EventsActivity.this,RecyclerView.VERTICAL, false);
            rvUrls.setLayoutManager(layoutManagerUrls);
            urlsAdapter = new UrlsRecyclerView(event.getUrls());
            rvUrls.setAdapter(urlsAdapter);
            }
          /*

            layoutManagerUrls = new LinearLayoutManager(EventsActivity.this);
            rvUrls.setLayoutManager(layoutManagerUrls);
            urlsAdapter = new UrlsRecyclerView(event.getUrls());
            rvUrls.setAdapter(urlsAdapter);*/
        }
    }

