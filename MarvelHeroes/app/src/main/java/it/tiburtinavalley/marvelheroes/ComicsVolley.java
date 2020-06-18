package it.tiburtinavalley.marvelheroes;

import android.content.Context;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONException;
import org.json.JSONObject;
import java.lang.reflect.Type;
import java.util.List;
import it.tiburtinavalley.marvelheroes.Model.Comics;
import it.tiburtinavalley.marvelheroes.Model.Series;

/* in questa classe , vengono gestite le ricerche in Internet per cercare le informazioni
   relative a Storie, Fumetti e Serie collegate agli eroi*/

public abstract class ComicsVolley implements Response.ErrorListener, Response.Listener<String>{
    private String urlBase = "http://gateway.marvel.com/v1/public/characters/%s";
    private String apiKey = "ts=1&apikey=467ab31077a4aa2037776afb61241da4&hash=21f601a3255711a8d8bad803d062e9ea";
    private RequestQueue requestQueue;

    abstract void fillComics(List<Comics> comicsList);

    public ComicsVolley(Context context){
        requestQueue = Volley.newRequestQueue(context);
    }

    public void getComicsInfo(String heroId){
        String comic = heroId + "/comics?";
        comicApiCall(comic);
    }

    private void comicApiCall(String comicUrl){
        String url = urlBase + apiKey; // usiamo una stringa di appoggio così da poter ripetere la chiamata
        url = String.format(url, comicUrl);
        StringRequest sr = new StringRequest(Request.Method.GET,
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
        String comics;
        try {
            JSONObject jsonObject = new JSONObject(response);
            comics = jsonObject.getJSONObject("data").getJSONArray("results").toString();
            Type listType = new TypeToken<List<Comics>>() {
            }.getType();
            List<Comics> comicsList = gson.fromJson(comics, listType);
            if (comicsList != null && comicsList.size() > 0) {
                Log.w("CA", "" + comicsList.size());
                //db.cocktailDAO().insertAll(cnt);    // NON OBBLIGATORIO
                fillComics(comicsList);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
