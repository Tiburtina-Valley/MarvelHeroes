package it.tiburtinavalley.marvelheroes;

/* this class is in charge on internet queries*/
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONException;
import org.json.JSONObject;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import it.tiburtinavalley.marvelheroes.Model.HeroModel;

public abstract class VolleyClass implements Response.ErrorListener, Response.Listener<String>{
    List<ImageView> heroesImg;
    private int pos = 0;
    StringRequest sr;
    private RequestQueue requestQueue;
    abstract void fillList(List<HeroModel> heroes);

    public VolleyClass(Context context){
        requestQueue = Volley.newRequestQueue(context);
        heroesImg = new ArrayList<>();
    }

    public void characterApiCall(String parameter) {
        String url = "http://gateway.marvel.com/v1/public/%s&ts=1&apikey=467ab31077a4aa2037776afb61241da4&hash=21f601a3255711a8d8bad803d062e9ea";
        url = String.format(url, parameter);

        sr = new StringRequest(Request.Method.GET,
                url,
                this,
                this);
        requestQueue.add(sr);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.w("QueryFail", error.getMessage());
    }

    @Override
    public void onResponse(String response) {
        Gson gson = new Gson();
        String heroes;
        try {
            JSONObject jsonObject = new JSONObject(response);
            heroes = jsonObject.getJSONObject("data").getJSONArray("results").toString();
            Type listType = new TypeToken<List<HeroModel>>() {
            }.getType();
            List<HeroModel> heroesList = gson.fromJson(heroes, listType);
            if (heroesList != null && heroesList.size() > 0) {
                Log.w("CA", "" + heroesList.size());
                //db.cocktailDAO().insertAll(cnt);    // NON OBBLIGATORIO
                fillList(heroesList);
                System.out.println(heroesList.get(0).getComics());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void imgCall(String url) {
        ImageRequest stringRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                heroesImg.get(pos).setImageBitmap(response);
                pos+=1;
            }
        }, 0, 0,
                ImageView.ScaleType.CENTER_CROP,
                Bitmap.Config.RGB_565,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.w("ImgFail", error.getMessage());
                    }
                });
        requestQueue.add(stringRequest);
    }

    public void setHeroesImg(ImageView img){
        this.heroesImg.add(img);
    }
}
