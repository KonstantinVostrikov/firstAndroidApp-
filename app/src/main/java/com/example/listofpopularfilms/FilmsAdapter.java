package com.example.listofpopularfilms;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.listofpopularfilms.screens.FilmItemActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FilmsAdapter extends RecyclerView.Adapter<FilmsAdapter.FilmViewHolder>{
    private URL url;
    private int pageNumber = 1;
    private int totalPages;
    private JSONObject response;
    private JSONArray jsonArray;
    private Context parent;
    private final int ITEMS_BEFORE_ARRAY_END = 8;
    private final int RESULTS_IN_ONE_GET_REQUEST = 20;
    private List<Bitmap> imageList = new ArrayList<>(20);


    public FilmsAdapter(Context parent) {
        loadListOfFilmsByGetRequest(pageNumber);
        this.parent = parent;
    }



    @Override
    public FilmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.film_list_item;

        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);


        FilmViewHolder viewHolder = new FilmViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FilmsAdapter.FilmViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return totalPages * RESULTS_IN_ONE_GET_REQUEST;
    }

    class FilmViewHolder extends RecyclerView.ViewHolder{
        TextView listItemFilmView;
        ImageView bmImage;
        TextView tvRating;


        public FilmViewHolder(View itemView) {
            super(itemView);

            listItemFilmView = itemView.findViewById(R.id.tv_film_item);
            bmImage = itemView.findViewById(R.id.iv_film_poster);
            tvRating = itemView.findViewById(R.id.tv_rating);
        }

        void bind(int listIndex){
            try {
                JSONObject current = jsonArray.getJSONObject(listIndex);
                String title = current.getString("title");
                String description = current.getString("overview");


                listItemFilmView.setText(title);

                if (listIndex < imageList.size()){
                    bmImage.setImageBitmap(imageList.get(listIndex));
                } else {
                    StringBuilder imagePathBuilder = new StringBuilder("https://image.tmdb.org/t/p/w200");
                    imagePathBuilder.append(current.getString("poster_path"));
                    new DownloadImageTask().execute(imagePathBuilder.toString());
                }

                String rating = "Рейтинг" + "\n" + current.getString("vote_average");
                tvRating.setText(rating);

                itemView.setOnClickListener(new ShowItemFilmActivity(listIndex, title, description));

            } catch (Exception e) {
                listItemFilmView.setText("Ошибка соединения с базой данных");
            }

            if (listIndex == (jsonArray.length() - ITEMS_BEFORE_ARRAY_END)  && pageNumber <= totalPages) {
                loadListOfFilmsByGetRequest(pageNumber);
            }
        }



        private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

            protected Bitmap doInBackground(String... urls) {
                String urldisplay = urls[0];
                Bitmap mIcon11 = null;
                InputStream in = null;
                try {
                    in = new java.net.URL(urldisplay).openStream();
                    mIcon11 = BitmapFactory.decodeStream(in);
                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                } finally {
                    try {
                        in.close();
                    } catch (IOException e) {
                        Log.e("Error", e.getMessage());
                        e.printStackTrace();
                    }
                }
                if (mIcon11 != null) {
                    imageList.add(mIcon11);
                }
                return mIcon11;
            }

            protected void onPostExecute(Bitmap result) {
                bmImage.setImageBitmap(result);
            }
        }



    }

    private void loadListOfFilmsByGetRequest(int pageNumber){
        this.url = NetworkUtils.generateURLOfPopularFilmsList(pageNumber);
        this.pageNumber++;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String s = NetworkUtils.getResponceFromURL(url);

                    if (response == null){
                        response = new JSONObject(s);
                        totalPages = response.getInt("total_pages");
                        jsonArray = response.getJSONArray("results");
                    } else {
                        response = new JSONObject(s);

                        JSONArray moreFilms = response.getJSONArray("results");

                        for (int i = 0; i < moreFilms.length(); i++) {
                            jsonArray.put(moreFilms.get(i));
                        }

                    }



                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    class ShowItemFilmActivity implements View.OnClickListener{

        private int position;
        private String title;
        private String description;

        public ShowItemFilmActivity(int position, String title, String description) {
            this.position = position;
            this.title = title;
            this.description = description;
        }

        @Override
        public void onClick(View v) {
            Class destinationActivity = FilmItemActivity.class;




            Intent childActivityIntent = new Intent(parent, destinationActivity);
            while (position >= imageList.size()) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            childActivityIntent.putExtra("poster", imageList.get(position));
            childActivityIntent.putExtra("title", title);
            childActivityIntent.putExtra("description", description);
            parent.startActivity(childActivityIntent);
        }
    }


}
