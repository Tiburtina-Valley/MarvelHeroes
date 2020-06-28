package it.tiburtinavalley.marvelheroes.recyclerviewadapter;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;
import it.tiburtinavalley.marvelheroes.R;
import it.tiburtinavalley.marvelheroes.activity.SeriesActivity;
import it.tiburtinavalley.marvelheroes.activity.ToastClass;
import it.tiburtinavalley.marvelheroes.model.Series;
import it.tiburtinavalley.marvelheroes.volley.ImageApiVolley;

public class SeriesAdapter extends RecyclerView.Adapter<SeriesAdapter.SeriesHolder> implements View.OnClickListener {
    private List<Series> series;
    ImageApiVolley imgVolley;
    Context appContext;

    public SeriesAdapter(List<Series> all, Context appContext) {
        series = new ArrayList<>();
        series.addAll(all);
        imgVolley = new ImageApiVolley(appContext);
        this.appContext = appContext;
    }

    @NonNull
    @Override
    public SeriesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ConstraintLayout cl;
        cl = (ConstraintLayout) LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_detail_layout, parent, false);
        cl.setOnClickListener(this);
        return new SeriesHolder(cl);
    }

    // This method sets the layout of the hero
    @Override
    public void onBindViewHolder(@NonNull SeriesHolder holder, int position) {
        imgVolley.addHeroImg(holder.ivSeries);
        if(series.get(position).getThumbnail() != null) {
            String urlThumbnail = series.get(position).getThumbnail().getPath().replaceFirst("http", "https")
                    + "." + series.get(position).getThumbnail().getExtension();
            Glide.with(holder.itemView).load(urlThumbnail).into(holder.ivSeries);
        }
        holder.tvSeriesName.setText(series.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return series.size();
    }

    public void addSeries(Series series){
        this.series.add(series);
    }

    @Override
    public void onClick(View v) {
        ConnectivityManager cm = (ConnectivityManager)appContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            int position = ((RecyclerView) v.getParent()).getChildAdapterPosition(v);
            Series serie = series.get(position);

            Intent i = new Intent(appContext, SeriesActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("series", serie);
            appContext.startActivity(i);
        }
        else {
                ToastClass toast = new ToastClass(appContext);
                toast.showToast(appContext.getString(R.string.internet_required));
            }
    }

    class SeriesHolder extends RecyclerView.ViewHolder {
        final ImageView ivSeries;
        final TextView tvSeriesName;

        SeriesHolder(@NonNull View itemView) {
            super(itemView);
            ivSeries = itemView.findViewById(R.id.ivCreator);
            tvSeriesName = itemView.findViewById(R.id.tvCreatorName);
        }
    }
}
