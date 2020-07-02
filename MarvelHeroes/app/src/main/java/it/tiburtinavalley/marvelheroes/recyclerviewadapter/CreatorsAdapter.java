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
import java.util.List;
import it.tiburtinavalley.marvelheroes.R;
import it.tiburtinavalley.marvelheroes.activity.CreatorsActivity;
import it.tiburtinavalley.marvelheroes.activity.ToastClass;
import it.tiburtinavalley.marvelheroes.model.Creators;

public class CreatorsAdapter extends RecyclerView.Adapter<CreatorsAdapter.CreatorsHolder> implements View.OnClickListener{
    private List<Creators> creators;
    private Context appContext;

    public CreatorsAdapter(List<Creators> creatorsList, Context appContext){
        this.creators = creatorsList;
        this.appContext = appContext;
    }

    @Override
    public void onClick(View v) {
        ConnectivityManager cm = (ConnectivityManager)appContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            int position = ((RecyclerView) v.getParent()).getChildAdapterPosition(v);
            Creators creator = creators.get(position);

            Intent i = new Intent(appContext, CreatorsActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("creator", creator);
            appContext.startActivity(i);
        }
        else {
                ToastClass toast = new ToastClass(appContext);
                toast.showToast(appContext.getString(R.string.msg_internet_required));
            }
    }

    @NonNull
    @Override
    public CreatorsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ConstraintLayout cl;
        cl = (ConstraintLayout) LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.creator_layout, parent, false);
        cl.setOnClickListener(this);
        return new CreatorsHolder(cl);
    }

    @Override
    public void onBindViewHolder(@NonNull CreatorsHolder holder, int position) {
        if(creators.get(position).getThumbnail() != null) {
            String urlThumbnail = creators.get(position).getThumbnail().getPath().replaceFirst("http", "https")
                    + "." + creators.get(position).getThumbnail().getExtension();
            Glide.with(holder.itemView).load(urlThumbnail).into(holder.ivComic);
        }
        holder.tvComicName.setText(creators.get(position).getFullName());
    }

    @Override
    public int getItemCount() {
        return creators.size();
    }

    static class CreatorsHolder extends RecyclerView.ViewHolder {
        final ImageView ivComic;
        final TextView tvComicName;

        CreatorsHolder(@NonNull View itemView) {
            super(itemView);
            ivComic = itemView.findViewById(R.id.ivCreator);
            tvComicName = itemView.findViewById(R.id.tvCreatorName);
        }
    }
}
