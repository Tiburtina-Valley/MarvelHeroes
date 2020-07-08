package it.tiburtinavalley.marvelheroes.activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import it.tiburtinavalley.marvelheroes.R;
import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutActivity extends AppCompatActivity {
    /**Istanziazione e fill della pagina di about us*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpPage();
    }

    /**Set di tutti gli elementi della pagina*/
    private void setUpPage(){
        Element legalElement = new Element();
        legalElement.setTitle(getString(R.string.label_legal));

        Element developersElement = new Element();
        developersElement.setTitle(getString(R.string.developers));

        Element shareElement = new Element();
        shareElement.setTitle(getString(R.string.share));

        Element thirdPartyLicenses = new Element();
        thirdPartyLicenses.setTitle(getString(R.string.third_party_licenses));

        /**Set di immagine,descrizione,contatti e bottoni*/
        View aboutPage = new AboutPage(this)
                .setDescription(getString(R.string.team_description))
                .isRTL(false)
                .setImage(R.drawable.logo)
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


