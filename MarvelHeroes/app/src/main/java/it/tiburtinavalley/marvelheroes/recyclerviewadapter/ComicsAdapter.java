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
import it.tiburtinavalley.marvelheroes.activity.ComicsActivity;
import it.tiburtinavalley.marvelheroes.activity.ToastClass;
import it.tiburtinavalley.marvelheroes.model.Comics;


public class ComicsAdapter extends RecyclerView.Adapter<ComicsAdapter.ComicsHolder> implements View.OnClickListener {
    private List<Comics> comics;
    Context appContext;

    public ComicsAdapter(List<Comics> all, Context appContext) {
        comics = new ArrayList<>();
        comics.addAll(all);
        this.appContext = appContext;
    }

    @NonNull
    @Override
    public ComicsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ConstraintLayout cl;
        cl = (ConstraintLayout) LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_detail_layout, parent, false);
        cl.setOnClickListener(this);
        return new ComicsHolder(cl);
    }

    // This method sets the layout of the hero
    @Override
    public void onBindViewHolder(@NonNull ComicsHolder holder, int position) {
        String urlThumbnail = comics.get(position).getThumbnail().getPath().replaceFirst("http", "https")
                + "." + comics.get(position).getThumbnail().getExtension();
        Glide.with(holder.itemView).load(urlThumbnail).into(holder.ivComic);
        holder.tvComicName.setText(comics.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return comics.size();
    }

    @Override
    public void onClick(View v) {
        ConnectivityManager cm = (ConnectivityManager)appContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            int position = ((RecyclerView) v.getParent()).getChildAdapterPosition(v);
            Comics comic = comics.get(position);

            Intent i = new Intent(appContext, ComicsActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("comic", comic);
            appContext.startActivity(i);
        }
        else {
            ToastClass toast = new ToastClass(appContext);
            toast.showToast(appContext.getString(R.string.msg_internet_required));
        }
    }

    static class ComicsHolder extends RecyclerView.ViewHolder {
        final ImageView ivComic;
        final TextView tvComicName;

        ComicsHolder(@NonNull View itemView) {
            super(itemView);
            ivComic = itemView.findViewById(R.id.ivCreator);
            tvComicName = itemView.findViewById(R.id.tvCreatorName);
        }
    }
}
