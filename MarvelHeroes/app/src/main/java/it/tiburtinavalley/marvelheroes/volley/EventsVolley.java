package it.tiburtinavalley.marvelheroes.volley;


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
import it.tiburtinavalley.marvelheroes.model.Creators;
import it.tiburtinavalley.marvelheroes.model.Events;

/** Classe che gestisce le query legate agli eventi. */

public abstract class EventsVolley implements Response.ErrorListener, Response.Listener<String>{
    //Definisco gli url che andremo ad utilizzare per fare le query
    private String urlBase = "https://gateway.marvel.com/v1/public/%s";

    private String apiKey = "ts=1&apikey=a5f7b1501c40d87b927d3176fe38f22f&hash=dad24154bc30827c2290b5bd86f088fa&limit=100";//"ts=1&apikey=d65eda0ccbbbcc626c35e7de5fdd506b&hash=9c0f64d5214cf16ca91f945f8cfbd5dc&limit=100";//"ts=1&apikey=467ab31077a4aa2037776afb61241da4&hash=21f601a3255711a8d8bad803d062e9ea&limit=100";//"ts=1&apikey=68bdde3ebf9ba45c6c11839bd1f51cc3&hash=6433747692d0e40eaf799ef75ccc78ea";

    private RequestQueue requestQueue;

    //Callback che setterà gli eventi.
    public abstract void fillEvents(List<Events> eventsList);

    /** Costruttore che inizializza la coda delle richieste */
    public EventsVolley(Context context){
        requestQueue = Volley.newRequestQueue(context);
    }

    /** Ottiene tutti gli eventi relativi ad un eroe. */
    public void getEventInfo(String heroId){
        String event = "characters/"+heroId + "/events?";
        eventsApiCall(event);
    }

    /** Ottiene tutti gli eventi relativi ad un comics. */
    public void getEventsFromComics(String comicId){
        String param = "comics/"+comicId+"/events?";
        eventsApiCall(param);
    }

    /** Ottiene tutti gli eventi relativi ad una serie.*/
    public void getEventsBySeries(String seriesId){
        String param = "series/"+seriesId+"/events?";
        eventsApiCall(param);
    }

    /** Inserisce la specifica richiesta nella coda. */
    private void eventsApiCall(String eventUrl){
        String url = urlBase + apiKey; //
        url = String.format(url, eventUrl);
        StringRequest sr = new StringRequest(Request.Method.GET,
                url,
                this,
                this);
        requestQueue.add(sr);
    }

    //Comportamento in caso di fallimento della query
    @Override
    public void onErrorResponse(VolleyError error) {
        if(error != null && error.getMessage() != null)
            Log.w("QueryFail", error.getMessage());
    }

    //Comportamento da seguire dopo aver ricevuto una risposta ad una richiesta fatta.
    @Override
    public void onResponse(String response) {
        Gson gson = new Gson();
        String event;
        try {
            //Usiamo GSON per convertire json in oggetti java(array java)
            JSONObject jsonObject = new JSONObject(response);
            event = jsonObject.getJSONObject("data").getJSONArray("results").toString();
            Type listType = new TypeToken<List<Events>>() {
            }.getType();
            List<Events> eventsList = gson.fromJson(event, listType);
            if (eventsList != null) {
                Log.w("Events", "" + eventsList.size());
                //db.cocktailDAO().insertAll(cnt);    // NON OBBLIGATORIO
                //Callback per settare la view con gli eventi.
                fillEvents(eventsList);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}


