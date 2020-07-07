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

import it.tiburtinavalley.marvelheroes.R;
import it.tiburtinavalley.marvelheroes.activity.ToastClass;
import it.tiburtinavalley.marvelheroes.model.Creators;

/** in questa classe , vengono gestite le ricerche in Internet per cercare le informazioni
   relative a Creatori*/
public abstract class CreatorsVolley implements Response.ErrorListener, Response.Listener<String>{

    private RequestQueue requestQueue;
    private Context context;

    public abstract void fillCreatorsInfo(List<Creators> creatorsList);

    public CreatorsVolley(Context context){
        this.context = context;
        requestQueue = Volley.newRequestQueue(context);
    }

    /** Ottiene tutti gli i creatori di un dato comics.*/
    public void getCreatorsByComics(String comicId){
        String creator = "comics/" + comicId + "/creators?";
        creatorsApiCall(creator);
    }

    /** Ottiene tutti gli i creatori di un dato evento.*/
    public void getCreatorsByEvents(String eventId){
        String creator = "events/" + eventId + "/creators?";
        creatorsApiCall(creator);
    }

    /** Ottiene tutti i creatori di una data serie.*/
    public void getCreatorsBySeries(String serieId){
        String creator = "series/" + serieId + "/creators?";
        creatorsApiCall(creator);
    }
    /** Crea la StringRequest e la inserisce in coda*/
    private void creatorsApiCall(String creatorUrl){
        String urlBase = "https://gateway.marvel.com/v1/public/%s";
        //"ts=1&apikey=d65eda0ccbbbcc626c35e7de5fdd506b&hash=9c0f64d5214cf16ca91f945f8cfbd5dc&limit=100";//"ts=1&apikey=467ab31077a4aa2037776afb61241da4&hash=21f601a3255711a8d8bad803d062e9ea&limit=100";//"ts=1&apikey=68bdde3ebf9ba45c6c11839bd1f51cc3&hash=6433747692d0e40eaf799ef75ccc78ea";
        String apiKey = "ts=1&apikey=467ab31077a4aa2037776afb61241da4&hash=21f601a3255711a8d8bad803d062e9ea&limit=30";
        String url = urlBase + apiKey; // usiamo una stringa di appoggio così da poter ripetere la chiamata
        url = String.format(url, creatorUrl);
        StringRequest sr = new StringRequest(Request.Method.GET, url, this, this);
        requestQueue.add(sr);
    }
    /** In caso ci sia un errore nella query*/
    @Override
    public void onErrorResponse(VolleyError error) {
        //controllo che l'errore non sia dovuto ad una mancanza della connessione ad internet
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            ToastClass toast = new ToastClass(context);
            //mostra un toast che avverte di aver superato il numero di query giornaliere disponibili
            toast.showToast(context.getString(R.string.msg_request_throttled));
        }
        //log del messaggio di errore
        if (error != null && error.getMessage() != null) {
            Log.w("QueryFail", error.getMessage());
        }
    }

    /** Caso in cui la query ha successo : l'oggetto viene spacchettato tramite l'api Gson e passato all'Activity
     tramite il metodo di fillCreatorsInfo*/
    @Override
    public void onResponse(String response) {
        Gson gson = new Gson();
        String creators;
        try {
            JSONObject jsonObject = new JSONObject(response);
            creators = jsonObject.getJSONObject("data").getJSONArray("results").toString();
            Type listType = new TypeToken<List<Creators>>() {
            }.getType();
            List<Creators> creatorsList = gson.fromJson(creators, listType);
            if (creatorsList != null) { //controllo solo se la lista non è vuota, ci penserà il metodo definito nell'Activity a settare o meno la RecyclerView
                fillCreatorsInfo(creatorsList);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
