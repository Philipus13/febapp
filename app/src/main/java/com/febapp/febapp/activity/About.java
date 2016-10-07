package com.febapp.febapp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.febapp.febapp.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * Created by Philipus on 12/06/2016.
 */
public class About extends AppCompatActivity {

    private RadioGroup radioGroup;
    private RadioButton music, sport, arts, science;
    private Button filter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);

        ImageView about= (ImageView) findViewById(R.id.abouts);
//        http://febapp.com/poster/about.png
        Glide.with(About.this).load("http://ktmonlinesystem.com/poster/about.png")
                                .thumbnail(0.5f)
                                .crossFade()
                                .fitCenter()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(about);

    }
}

