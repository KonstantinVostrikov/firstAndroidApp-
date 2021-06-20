package com.example.listofpopularfilms.screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.listofpopularfilms.R;

public class FilmItemActivity extends AppCompatActivity {
    private ImageView bmImage;
    private TextView title;
    private WebView description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_item);

        bmImage = findViewById(R.id.iv_film_poster_in_item);
        title = findViewById(R.id.tv_particular_film_title);
        description = findViewById(R.id.wv_particular_film_description);
        Intent intentThanStartedThisActivity = getIntent();

        if (intentThanStartedThisActivity.hasExtra("poster")) {
            Bitmap im = intentThanStartedThisActivity.getParcelableExtra("poster");
            bmImage.setImageBitmap(im);
        }

        if (intentThanStartedThisActivity.hasExtra("title")) {
            String titleFromMainActivity = intentThanStartedThisActivity.getStringExtra("title");
            title.setText(titleFromMainActivity);
        }
        if (intentThanStartedThisActivity.hasExtra("description")) {
            String descriptionFromMainActivity = intentThanStartedThisActivity.getStringExtra("description");
            description.loadData(descriptionFromMainActivity, "text/html; charset=utf-8", "utf-8");
        }



    }
}