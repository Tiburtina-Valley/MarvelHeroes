package it.tiburtinavalley.marvelheroes;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class HeroDetailActivity extends AppCompatActivity {

    private HeroModel hm;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hero_detail_layout);
        hm = getIntent().getParcelableExtra("hero");
        Holder holder = new Holder();
        holder.setDetails(hm);
    }

    class Holder{
        private ImageView ivHeroPhoto;
        private TextView tvHeroName;
        private TextView tvHeroId;
        private TextView tvHeroDescription;
        private VolleyClass vMarvel;
        final RecyclerView rvComics;

        public Holder(){
            rvComics = findViewById(R.id.rvComics);
            ivHeroPhoto = findViewById(R.id.ivHeroPhoto);
            tvHeroId = findViewById(R.id.tvHeroId);
            tvHeroName = findViewById(R.id.tvHeroName);
            tvHeroDescription = findViewById(R.id.tvHeroDescription);
            vMarvel = new VolleyClass(getApplicationContext()) {
                @Override
                void fillList(List<HeroModel> heroes) {
                    Log.w("fine", "dummy log");
                }
            };
            fillRecView(hm.getComics());

        }

        private void fillRecView(Comics comics){
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(HeroDetailActivity.this);
            rvComics.setLayoutManager(layoutManager);
            ComicsAdapter cAdapter = new ComicsAdapter(comics.getItems());
            rvComics.setAdapter(cAdapter);
        }

        public void setDetails(HeroModel hm){
            this.tvHeroName.setText(hm.getName());
            this.tvHeroId.setText(hm.getId());
            this.tvHeroDescription.setText(hm.getDescription());
            vMarvel.setHeroesImg(this.ivHeroPhoto);
            vMarvel.imgCall(hm.getThumbnail().getPath()+"."+hm.getThumbnail().getExtension());
        }
    }

    private class ComicsAdapter extends RecyclerView.Adapter<ComicsAdapter.Holder> implements View.OnClickListener {
        private final List<Items> items;
        ComicsAdapter(List<Items> all) {
            items = new ArrayList<>();
            items.addAll(all);
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ConstraintLayout cl;
            cl = (ConstraintLayout) LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.comic_detail_layout, parent, false);
            cl.setOnClickListener(this);
            return new Holder(cl);
        }

        // This method sets the layout of the hero
        @Override
        public void onBindViewHolder(@NonNull HeroDetailActivity.ComicsAdapter.Holder holder, int position) {
            holder.tvResUri.setText(items.get(position).getResourceURI());
            holder.tvName.setText(items.get(position).getName());
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        //
        @Override
        public void onClick(View v) {

        }

        class Holder extends RecyclerView.ViewHolder {
            final TextView tvResUri;
            final TextView tvName;

            Holder(@NonNull View itemView) {
                super(itemView);
                tvResUri = itemView.findViewById(R.id.tvResUri);
                tvName = itemView.findViewById(R.id.tvName);
            }
        }
    }
}
