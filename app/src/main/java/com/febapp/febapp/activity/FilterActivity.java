package com.febapp.febapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.febapp.febapp.Fragment.EventFragment;
import com.febapp.febapp.MainActivity;
import com.febapp.febapp.R;

public class FilterActivity extends AppCompatActivity {

    private RadioGroup radioGroup;
    private RadioButton music, sport, arts, science;
    private Button filter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.music) {

                } else if(checkedId == R.id.sport) {

                } else if(checkedId == R.id.arts){

                } else if(checkedId == R.id.science){

                }
            }
        });

        music = (RadioButton) findViewById(R.id.music);
        sport = (RadioButton) findViewById(R.id.sport);
        arts = (RadioButton) findViewById(R.id.arts);
        science = (RadioButton) findViewById(R.id.science);

        filter = (Button)findViewById(R.id.filter);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                if(selectedId == music.getId()) {

                    Toast.makeText(getApplicationContext(),
                            "music", Toast.LENGTH_LONG)
                            .show();

                } else if(selectedId == sport.getId()) {
                    Bundle bundle = new Bundle();
                    bundle.putString("sport", "sport");

                    EventFragment fragobj = new EventFragment();
                    fragobj.setArguments(bundle);
                    Intent intent = new Intent(FilterActivity.this, MainActivity.class);
                    startActivity(intent);

                    Toast.makeText(getApplicationContext(),
                            "sports", Toast.LENGTH_LONG)
                            .show();

                }  else if(selectedId == arts.getId()) {

                    Toast.makeText(getApplicationContext(),
                            "arts", Toast.LENGTH_LONG)
                            .show();
                }else if(selectedId == science.getId()) {

                    Toast.makeText(getApplicationContext(),
                            "science", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });


    }

}
