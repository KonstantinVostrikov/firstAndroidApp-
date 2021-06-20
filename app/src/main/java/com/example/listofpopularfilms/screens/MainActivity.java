package com.example.listofpopularfilms.screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.listofpopularfilms.FilmsAdapter;
import com.example.listofpopularfilms.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private RecyclerView filmList;
    private FilmsAdapter filmsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        filmList = findViewById(R.id.rv_films);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        filmList.setLayoutManager(layoutManager);
        filmList.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

        filmsAdapter = new FilmsAdapter(this);
        filmList.setAdapter(filmsAdapter);






    }


}