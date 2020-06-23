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


public abstract class EventsVolley implements Response.ErrorListener, Response.Listener<String>{
    private String urlBase = "https://gateway.marvel.com/v1/public/characters/%s";
    private String apiKey = "ts=1&apikey=467ab31077a4aa2037776afb61241da4&hash=21f601a3255711a8d8bad803d062e9ea";
    private RequestQueue requestQueue;

    public abstract void fillEvents(List<Events> eventsList);

    public EventsVolley(Context context){
        requestQueue = Volley.newRequestQueue(context);
    }

    public void getEventInfo(String heroId){
        String event= heroId + "/events?";
        eventsApiCall(event);
    }

    private void eventsApiCall(String comicUrl){
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
        String event;
        try {
            JSONObject jsonObject = new JSONObject(response);
            event = jsonObject.getJSONObject("data").getJSONArray("results").toString();
            Type listType = new TypeToken<List<Events>>() {
            }.getType();
            List<Events> eventsList = gson.fromJson(event, listType);
            if (eventsList != null && eventsList.size() > 0) {
                Log.w("CA", "" + eventsList.size());
                //db.cocktailDAO().insertAll(cnt);    // NON OBBLIGATORIO
                fillEvents(eventsList);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}


