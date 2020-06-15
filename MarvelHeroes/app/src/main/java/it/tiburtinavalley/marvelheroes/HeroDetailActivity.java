package it.tiburtinavalley.marvelheroes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;


public class HeroDetailActivity extends AppCompatActivity {
    private Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hero_detail_layout);
        i = getIntent();
        HeroModel hm = i.getParcelableExtra("hero");
        Holder holder = new Holder();
        holder.setDetails(hm);
    }

    class Holder{
        private ImageView ivHeroPhoto;
        private TextView tvHeroName;
        private TextView tvHeroId;
        private TextView tvHeroDescription;
        private VolleyClass vMarvel;

        public Holder(){
            ivHeroPhoto = findViewById(R.id.ivHeroPhoto);
            tvHeroId = findViewById(R.id.tvHeroId);
            tvHeroName = findViewById(R.id.tvHeroName);
            tvHeroDescription = findViewById(R.id.tvHeroDescription);
            vMarvel = new VolleyClass(getApplicationContext()) {
                @Override
                void fillList(List<HeroModel> heroes) {
                    Log.w("ok", "Just for abstract class");
                }
            };
        }

        public void setDetails(HeroModel hm){
            this.tvHeroName.setText(hm.getName());
            this.tvHeroId.setText(hm.getId());
            this.tvHeroDescription.setText(hm.getDescription());
            vMarvel.setHeroesImg(this.ivHeroPhoto);
            vMarvel.imgCall(i.getStringExtra("imgUrl")+"."+i.getStringExtra("imgExt"));
        }
    }
}
