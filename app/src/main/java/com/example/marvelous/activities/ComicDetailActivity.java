package com.example.marvelous.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideModule;
import com.example.marvelous.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Console;

public class ComicDetailActivity extends AppCompatActivity {
    TextView tvTitle;
    TextView tvPublished;
    TextView tvPenciler;
    TextView tvWriter;
    TextView tvCoverArtist;
    TextView tvDescription;
    ImageView imageView;

    final String TS = "thesoer";
    final String API_KEY = "8ad698beaf7b0053b3a973063acca16d";
    final String HASH = "d98af00cec0d8d6a4d217723b7ea5e7b";
    String base_url ="https://gateway.marvel.com:443/v1/public/";
    String imageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comic_detail);

        tvTitle = findViewById(R.id.lblTitle);
        tvPublished = findViewById(R.id.lblPublished);
        tvPenciler = findViewById(R.id.lblPenciler);
        tvWriter = findViewById(R.id.lblWriter);
        tvCoverArtist = findViewById(R.id.lblCoverArtist);
        tvDescription = findViewById(R.id.lblDescription);
        imageView = (ImageView)findViewById(R.id.ivComic);

        RequestQueue queue = Volley.newRequestQueue(ComicDetailActivity.this);
        String id = "89680";
        String url = base_url + "comics" + "/" + id + "?ts=" + TS + "&apikey=" + API_KEY + "&hash=" + HASH;

        Log.i("URL", url);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject object) {
                //String copyright = null;
                try {
                    tvTitle.setText(object.getJSONObject("data").getJSONArray("results").getJSONObject(0).getString("title"));
                    tvPublished.setText(object.getJSONObject("data").getJSONArray("results").getJSONObject(0).getString("modified"));

                    String imagePath = object.getJSONObject("data").getJSONArray("results").getJSONObject(0)
                            .getJSONArray("images").getJSONObject(0).getString("path");
                    String imageExtension = object.getJSONObject("data").getJSONArray("results").getJSONObject(0)
                            .getJSONArray("images").getJSONObject(0).getString("extension");
                    imageName = imagePath + "/portrait_xlarge" + "." + imageExtension;
                    Log.i("test", imageName);
                    Glide.with(ComicDetailActivity.this)
                            .load("http://i.annihil.us/u/prod/marvel/i/mg/a/10/619e637b7fe1f/portrait_xlarge.jpg")
                            .into(imageView);

                    JSONArray creators = object.getJSONObject("data").getJSONArray("results").getJSONObject(0)
                            .getJSONObject("creators").getJSONArray("items");

                    String role, name;
                    for(int i = 0; i < creators.length(); i++){
                        JSONObject item = creators.getJSONObject(i);
                        name = item.getString("name");
                        role = item.getString("role");
                        if(role.equals("writer"))
                            tvWriter.setText(name);
                        else if(role.equals("letterer"))
                            tvPenciler.setText(name);
                        else if(role.equals("painter (cover)"))
                            tvCoverArtist.setText(name);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", "an error occured");
            }
        });
        queue.add(request);
    }
}