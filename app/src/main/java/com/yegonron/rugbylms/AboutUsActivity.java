package com.yegonron.rugbylms;

import android.os.Bundle;
import android.widget.ImageButton;

import android.annotation.SuppressLint;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import java.util.Calendar;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

import androidx.appcompat.app.AppCompatActivity;

public class AboutUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        ImageButton backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> onBackPressed());

        Element adsElement = new Element();
        View aboutPage = new AboutPage(this, mehdi.sakout.aboutpage.R.style.about_About)
                .isRTL(false)
                .setDescription(" At the core of RugbyLMS, we are a group of passionate individuals who would like to " +
                        "enhance your Rugby experience. RugbyLMS is designed for those who are talented, hardworking" +
                        " and passionate about the Game. RugbyLMS allows rugby clubs to manage their daily activities," +
                        " coaches to coach their players and manage their teams, athletes to excel at sports, and fans " +
                        "to get connected with their club, team, or athlete.")
                .addItem(new Element().setTitle("Version 1.0"))
                .addGroup("CONNECT WITH US!")
                .addEmail("RugbyLMS@gmail.com ")
                .addWebsite("/RugbyLMS.com")
                .addTwitter("Rugby_LMS")
                .addYoutube("")
                .addPlayStore("com.example.rugbylms")   //Replace all this with your package name
                .addInstagram("RugbyLMS")    //Your instagram id
                .addItem(createCopyright())
                .create();
        setContentView(aboutPage);
    }

    private Element createCopyright() {
        Element copyright = new Element();
        @SuppressLint("DefaultLocale") final String copyrightString = String.format("Copyright %d by Rugby LMS", Calendar.getInstance().get(Calendar.YEAR));
        copyright.setTitle(copyrightString);
        // copyright.setIcon(R.mipmap.ic_launcher);
        copyright.setGravity(Gravity.CENTER);
        copyright.setOnClickListener(v -> Toast.makeText(AboutUsActivity.this, copyrightString, Toast.LENGTH_SHORT).show());
        return copyright;

    }
}