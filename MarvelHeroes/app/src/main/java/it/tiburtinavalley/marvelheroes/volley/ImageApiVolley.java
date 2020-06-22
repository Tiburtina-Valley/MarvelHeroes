package it.tiburtinavalley.marvelheroes.volley;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import java.util.ArrayList;
import java.util.List;


public class ImageApiVolley implements Response.ErrorListener, Response.Listener<Bitmap>{
    List<ImageView> imgList;
    private int pos = 0;
    private RequestQueue requestQueue;

    public ImageApiVolley(Context context) {
        requestQueue = Volley.newRequestQueue(context);
        imgList = new ArrayList<>();
    }

    public void getImageFromUrl(String url) {
        ImageRequest stringRequest = new ImageRequest(url, this,0, 0, ImageView.ScaleType.CENTER_CROP,
                Bitmap.Config.RGB_565, this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.w("QueryFail", error.getMessage());
    }

    @Override
    public void onResponse(Bitmap response) {
        imgList.get(pos).setImageBitmap(response);
        pos += 1;
    }

    public void addHeroImg(ImageView img) {
        this.imgList.add(img); // sets the ImageView to load the Bitmap in
    }

    public void resetAll(){
        /* this method will reset the volley attribute for another call*/
        this.pos = 0;
        this.imgList.clear();
    }
}
