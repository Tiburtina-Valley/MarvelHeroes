package it.tiburtinavalley.marvelheroes.activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import it.tiburtinavalley.marvelheroes.BuildConfig;
import it.tiburtinavalley.marvelheroes.R;
import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpPage();
    }

    private void setUpPage(){
        Element legalElement = new Element();
        legalElement.setTitle("Legal");

        Element developersElement = new Element();
        developersElement.setTitle(getString(R.string.developers));

        Element shareElement = new Element();
        shareElement.setTitle(getString(R.string.share));

        Element thirdPartyLicenses = new Element();
        thirdPartyLicenses.setTitle(getString(R.string.third_party_licenses));


        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setImage(R.drawable.s)
                .addItem(new Element().setTitle(getString(R.string.version)))
                .addEmail(getString(R.string.mail))
                .addWebsite(getString(R.string.website))
                .addFacebook(getString(R.string.facebookPage))
                .addTwitter(getString(R.string.twitterPage))
                .addYoutube(getString(R.string.youTubePage))
                .addPlayStore(getString(R.string.playStore))
                .addInstagram(getString(R.string.instagramPage))
                .addGitHub(getString(R.string.githubPage))
                .create();

        setContentView(aboutPage);
    }


    }


