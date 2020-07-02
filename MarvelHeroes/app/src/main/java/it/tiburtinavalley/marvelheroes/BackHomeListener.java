package it.tiburtinavalley.marvelheroes;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import it.tiburtinavalley.marvelheroes.activity.MainActivity;

public class BackHomeListener implements View.OnClickListener{
    Context appContext;

    public BackHomeListener(Context appContext){
        this.appContext = appContext;
    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent(appContext, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        appContext.startActivity(i);
    }
}
