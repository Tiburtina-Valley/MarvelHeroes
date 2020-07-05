package it.tiburtinavalley.marvelheroes.volley;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import java.util.Random;
import it.tiburtinavalley.marvelheroes.R;
import it.tiburtinavalley.marvelheroes.activity.ToastClass;
import it.tiburtinavalley.marvelheroes.model.Comics;

/* in questa classe , vengono gestite le ricerche in Internet per cercare le informazioni
   relative ai Fumetti */

public abstract class ComicsVolley implements Response.ErrorListener, Response.Listener<String>{

    private Context context;
    private RequestQueue requestQueue;

    public abstract void fillComics(List<Comics> comicsList);

    public ComicsVolley(Context context){
        this.context = context;
        requestQueue = Volley.newRequestQueue(context);
    }
    /** Ottiene un comics random basato sull'inziale del titolo.*/
    public void getRandomComic(){
        Random r = new Random();
        char randomLetter = (char) (r.nextInt(26) + 'a');
        String param = "comics?titleStartsWith=" + randomLetter + "&limit=1&";
        comicApiCall(param);
    }

    /** Ottiene i dati di un comics a partire dall'id.*/
    public void getComicFromId(String comicId){
        String param = "comics/" + comicId + "?";
        comicApiCall(param);
    }

    /** Ottiene tutti i comics relativi ad un eroe.*/
    public void getComicsRelatedToHero(String heroId){
        String comic = "characters/"+ heroId + "/comics?";
        comicApiCall(comic);
    }

    /** Ottiene tutti i comics relativi ad un creator.*/
    public void getComicsByCreators(String creatorId){
        String creator = "creators/" + creatorId + "/comics?";
        comicApiCall(creator);
    }

    /** Ottiene tutti i comics relativi ad un evento.*/
    public void getComicsByEvent(String eventId){
        String comics = "events/" + eventId + "/comics?";
        comicApiCall(comics);
    }

    /** Ottiene tutti i comics relativi ad una serie.*/
    public void getComicsBySeries(String seriesId) {
        String comics = "series/" + seriesId + "/comics?";
        comicApiCall(comics);
    }

    /** Crea la StringRequest e la inserisce in coda*/
    private void comicApiCall(String comicUrl){
        String urlBase = "https://gateway.marvel.com/v1/public/%s";
        //"ts=1&apikey=d65eda0ccbbbcc626c35e7de5fdd506b&hash=9c0f64d5214cf16ca91f945f8cfbd5dc&limit=100";//"ts=1&apikey=467ab31077a4aa2037776afb61241da4&hash=21f601a3255711a8d8bad803d062e9ea&limit=100";//"ts=1&apikey=68bdde3ebf9ba45c6c11839bd1f51cc3&hash=6433747692d0e40eaf799ef75ccc78ea";
        String apiKey = "ts=1&apikey=a5f7b1501c40d87b927d3176fe38f22f&hash=dad24154bc30827c2290b5bd86f088fa&limit=50";
        String url = urlBase + apiKey; // usiamo una stringa di appoggio così da poter ripetere la chiamata
        url = String.format(url, comicUrl);
        StringRequest sr = new StringRequest(Request.Method.GET,
                url,
                this,
                this);
        requestQueue.add(sr);
    }

    /** In caso ci sia un errore nella query*/
    @Override
    public void onErrorResponse(VolleyError error) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            ToastClass toast = new ToastClass(context);
            toast.showToast(context.getString(R.string.msg_request_throttled));
        }
        if (error != null && error.getMessage() != null) {
            Log.w("QueryFail", error.getCause());
        }
    }

    /** Caso in cui la query ha successo : l'oggetto viene spacchettato tramite l'api Gson e passato all'Activity
        tramite il metodo di fillComics*/
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
            if (comicsList != null) {
                Log.w("CA", "" + comicsList.size());
                fillComics(comicsList);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
