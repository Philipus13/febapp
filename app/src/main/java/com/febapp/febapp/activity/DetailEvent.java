package com.febapp.febapp.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.febapp.febapp.App.AppConfig;
import com.febapp.febapp.App.AppController;
import com.febapp.febapp.Fragment.CartFragment;
import com.febapp.febapp.Fragment.EventListFragment;
import com.febapp.febapp.Fragment.HelpFrag;
import com.febapp.febapp.Helper.SessionManager;
import com.febapp.febapp.R;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

import static android.R.attr.fragment;
import static com.febapp.febapp.R.id.enddate;
import static com.febapp.febapp.R.id.lat;
import static com.febapp.febapp.R.id.lng;
import static com.febapp.febapp.R.id.startdate;

//import com.daimajia.slider.library.SliderLayout;

public class DetailEvent extends AppCompatActivity implements
        CartFragment.OnFragmentInteractionListener
{

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
//    SliderLayout sliderShow;
    private GoogleMap mMap;
    String judul,gambar, id;

    int stok,price;
    private SessionManager session;
    PhotoView imagezoom;
    ImageView image;
    private AlertDialog dialog;
    Button buy;
    Handler handler;
    Runnable runnable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_event);




        judul = getIntent().getExtras().getString("event_judul");
        gambar = getIntent().getExtras().getString("event_image");
        id = getIntent().getExtras().getString("event_id");
        stok = getIntent().getExtras().getInt("event_stok");
        price = getIntent().getExtras().getInt("event_price");


        final FrameLayout buys = (FrameLayout) findViewById(R.id.flContent);


        buy =  (Button) findViewById(R.id.buy) ;
        final View include = (View) findViewById(R.id.include);
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buys.setVisibility(view.VISIBLE);
                include.setVisibility(view.GONE);
                buy.setVisibility(view.GONE);
                SharedPreferences pref = getSharedPreferences("data", Context.MODE_PRIVATE);
                final String orders = pref.getString("Order", "");

                tambahKurang(id, AppConfig.INS_JUAL, orders);
                handler = new Handler();

                runnable = new Runnable() {
                    public void run() {
                        Fragment fragment = null;
                        Class fragmentClass = null;
                        fragmentClass = CartFragment.class;
                        try {
                            fragment = (Fragment) fragmentClass.newInstance();
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
                    }
                };
                handler.postDelayed(runnable, 500);

            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Event Detail");


        ImageButton shareButton = (ImageButton) toolbar.findViewById(R.id.shareButton);

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, judul);
                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Datang yuk! ke acara "+judul+'\n'
                        +"Klik http://febapp.com/Kampanye/kampanyeDetail/"+"/visitor");
                startActivity(Intent.createChooser(shareIntent, "Share via"));
            }
        });
        final CheckBox love =(CheckBox) toolbar.findViewById(R.id.loves);

        SharedPreferences settings = getSharedPreferences("data", Context.MODE_PRIVATE);
        boolean Checked = settings.getBoolean(judul, false);
        love.setChecked(Checked);

        session = new SessionManager(getApplicationContext());

        if (session.isLoggedIn()) {
            love.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                                @Override
                                                public void onCheckedChanged(CompoundButton checkboxes, boolean isChecked) {

                                                    SharedPreferences pref = getSharedPreferences("data", Context.MODE_PRIVATE);
                                                    final String apikeys = pref.getString("apikey", "");

                                                    if (checkboxes.isChecked()==true) {

                                                        inputNotif(apikeys, id);
                                                        Toast.makeText(getApplicationContext(),
                                                                "You favorited this event!" + judul, Toast.LENGTH_SHORT)
                                                                .show();
                                                        SharedPreferences settings = getSharedPreferences("data", Context.MODE_PRIVATE);
                                                        settings.edit().putBoolean(judul,true).commit();

                                                    } else {


                                                        deleteNotif(apikeys, id);
                                                        Toast.makeText(getApplicationContext(),
                                                                "you unfavorited this event!" + judul, Toast.LENGTH_SHORT)
                                                                .show();
                                                        SharedPreferences settings = getSharedPreferences("data", Context.MODE_PRIVATE);
                                                        settings.edit().putBoolean(judul,false).commit();

                                                    }

                                                }
                                            }
            );

        }else{
            love.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(),
                            "You must Login to access this!" , Toast.LENGTH_LONG)
                            .show();
                    love.setChecked(false);
                }
            });
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView txtname = (TextView) findViewById(R.id.judul_event);
        txtname.setText(judul);
//        TextView startdates = (TextView) findViewById(startdate);
//        startdates.setText(startdate);
//        TextView enddates = (TextView) findViewById(enddate);
//        enddates.setText(enddate);
//
//        WebView webview = (WebView)this.findViewById(R.id.deskripsi);
//        webview.getSettings().setJavaScriptEnabled(true);
//        webview.loadData(deskripsi, "text/html", "utf-8");
//
//        LinearLayout youtubes = (LinearLayout ) findViewById(R.id.youtube);
//        youtubes.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(youtube)));
//            }
//        })
//        ;
//        LinearLayout instagrams = (LinearLayout ) findViewById(R.id.instagram);
//        instagrams.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(instagram)));
//            }
//        });
//        final LinearLayout facebooks = (LinearLayout ) findViewById(R.id.facebook);
//        facebooks.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(fb)));
//            }
//        });
//        final LinearLayout twitters = (LinearLayout ) findViewById(R.id.twitter);
//        twitters.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(twitter)));
//            }
//        });

        image = (ImageView) findViewById(R.id.gambar_event);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPhoto();
            }
        });
//        final PhotoViewAttacher attacher = new PhotoViewAttacher(image);
//        attacher.setOnPhotoTapListener(new PhotoTapListener());
//        Picasso.with(DetailEvent.this).load(gambar).resize(200, 200)
//                .centerCrop()
//                .placeholder(R.mipmap.ic_nice)
//                .error(R.mipmap.ic_nice)
//                .into(image);

//        SimpleTarget target = new SimpleTarget<Bitmap>() {
//            @Override
//            public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
//                // do something with the bitmap
//                // for demonstration purposes, let's just set it to an ImageView
//                image.setImageBitmap( bitmap );
//                attacher.update();
//            }
//        };
        Glide.with(DetailEvent.this).load("http://ktmonlinesystem.com/febapp/"+gambar)
                .fitCenter()
                .placeholder(R.mipmap.ic_nice)
                .error(R.mipmap.ic_nice)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(image);
//        NetworkImageView image = (NetworkImageView) findViewById(R.id.gambar_event);
//        image.setImageUrl(gambar, imageLoader);
    }
//    @Override
//    protected void onStop() {
//        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
//        sliderShow.stopAutoCycle();
//        super.onStop();
//    }
private void tambahKurang(final String id, String CART, final String id_order) {
    // Tag used to cancel the request

    String tag_string_req = "req_register";
    SharedPreferences pref = DetailEvent.this.getSharedPreferences("data", Context.MODE_PRIVATE);

    final String apikeys = pref.getString("apikey", "");
    final String names = pref.getString("name", "");
    final int group = pref.getInt("group_id", 0);

//        String DATA_NOTIF;
//        if (group == 4) {
//            DATA_NOTIF = AppConfig.url+"/postbook";
//        }else if(group == 2){
//            DATA_NOTIF = AppConfig.url+"/postbookP3";
//        }else{
//            DATA_NOTIF = AppConfig.url+"/postbook";
//        }

    StringRequest strReq = new StringRequest(Request.Method.POST,
            CART, new Response.Listener<String>() {

        @Override
        public void onResponse(String response) {

            try {
                JSONObject jObj = new JSONObject(response);
                String errorMsg = jObj.getString("message");
            }
            catch (JSONException e){
                e.printStackTrace();
                Toast.makeText(DetailEvent.this, "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }, new Response.ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError error) {

            Toast.makeText(DetailEvent.this,
                    error.getMessage(), Toast.LENGTH_LONG).show();
        }
    }) {

        @Override
        protected Map<String, String> getParams() {
            // Posting params to register url
            Map<String, String> params = new HashMap<String, String>();
            params.put("id_barang", id);
            params.put("qty", "1");
            params.put("id_order", id_order);
            return params;
        }
        @Override
        public Map<String, String> getHeaders() {
            // Posting parameters to login url
            Map<String, String> params = new HashMap<String, String>();
            params.put("authorization", apikeys);
            return params;
        }

    };

    // Adding request to request queue
    AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
}

private void showPhoto(){

    AlertDialog.Builder builder = new AlertDialog.Builder(DetailEvent.this);
    LayoutInflater inflater = DetailEvent.this.getLayoutInflater();
    View view = inflater.inflate(R.layout.dialog_detail_zoom, null);
    imagezoom = (PhotoView) view.findViewById(R.id.imagezooms);
    final PhotoViewAttacher attachers = new PhotoViewAttacher(imagezoom);

    SimpleTarget target = new SimpleTarget<Bitmap>() {
        @Override
        public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
            // do something with the bitmap
            // for demonstration purposes, let's just set it to an ImageView
            imagezoom.setImageBitmap( bitmap );
            attachers.update();
        }
    };
    Glide.with(DetailEvent.this).load("http://ktmonlinesystem.com/febapp/"+gambar).asBitmap()
            .fitCenter()
            .placeholder(R.mipmap.ic_nice)
            .error(R.mipmap.ic_nice)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(target);

    builder.setView(view);
    builder.setTitle("Detail Photo");
    builder.setPositiveButton("", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

        }
    });
    builder.setNegativeButton("Back", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    });

    dialog = builder.create();

    dialog.show();
    int width = (int)(getResources().getDisplayMetrics().widthPixels*1.00);
    int height = (int)(getResources().getDisplayMetrics().heightPixels*1.00);
    dialog.getWindow().setLayout(width, height);


    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {



        }
    });
}
//    private class PhotoTapListener implements PhotoViewAttacher.OnPhotoTapListener {
//
//        @Override
//        public void onPhotoTap(View view, float x, float y) {
//            float xPercentage = x * 100f;
//            float yPercentage = y * 100f;
//            showPhoto();
//
//        }
//
//        @Override
//        public void onOutsidePhotoTap() {
//            Toast.makeText(getApplicationContext(), "diluar", Toast.LENGTH_LONG).show();
//        }
//    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            super.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void inputNotif(final String apikey,final String idd) {
        // Tag used to cancel the request

        String tag_string_req = "req_register";

        SharedPreferences pref = getSharedPreferences("data", Context.MODE_PRIVATE);

        final String apikeys = pref.getString("apikey", "");
        final String names = pref.getString("name", "");
        final int group = pref.getInt("group_id", 0);

        String DATA_NOTIF;
        if (group == 4) {
            DATA_NOTIF = AppConfig.url+"/postbook";
        }else if(group == 2){
            DATA_NOTIF = AppConfig.url+"/postbookP3";
        }else{
            DATA_NOTIF = AppConfig.url+"/postbook";
        }

        StringRequest strReq = new StringRequest(Request.Method.POST,
                DATA_NOTIF, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jObj = new JSONObject(response);
                    String errorMsg = jObj.getString("message");

                }
                catch (JSONException e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_kampanye", idd);




                return params;
            }
            @Override
            public Map<String, String> getHeaders() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("authorization", apikey);


                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void deleteNotif(final String apikey, final String idd) {
        // Tag used to cancel the request

        String tag_string_req = "req_register";

        SharedPreferences pref = getSharedPreferences("data", Context.MODE_PRIVATE);

        final String apikeys = pref.getString("apikey", "");
        final String names = pref.getString("name", "");
        final int group = pref.getInt("group_id", 0);

        String NOTIF_HAPUS;
        if (group == 4) {
            NOTIF_HAPUS = AppConfig.url+"/hapusbook";
        }else if(group == 2){
            NOTIF_HAPUS = AppConfig.url+"/hapusbookP3";
        }else{
            NOTIF_HAPUS = AppConfig.url+"/hapusbook";
        }

        StringRequest strReq = new StringRequest(Request.Method.POST,
                NOTIF_HAPUS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jObj = new JSONObject(response);
                    String errorMsg = jObj.getString("message");

                }
                catch (JSONException e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_kampanye", idd);



                return params;
            }
            @Override
            public Map<String, String> getHeaders() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("authorization", apikey);


                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            // Respond to the action bar's Up/Home button
//            case android.R.id.home:
//                finish();
//                return true;
//
//        }
//        return super.onOptionsItemSelected(item);
//    }

//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//
//
//        CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(lat, lng));
//        CameraUpdate zoom = CameraUpdateFactory.zoomTo(9);
//        mMap.moveCamera(center);
//        mMap.animateCamera(zoom);
//        mMap.addMarker(new MarkerOptions()
//                .position(new LatLng(lat, lng))
//                .title(judul)
//                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_markertes2))
//                .snippet(alamat));
//    }
@Override
public void onFragmentInteraction(Uri uri) {

}
}
