package com.febapp.febapp.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.febapp.febapp.App.AppConfig;
import com.febapp.febapp.App.AppController;
import com.febapp.febapp.App.Event;
import com.febapp.febapp.R;
import com.febapp.febapp.activity.DetailEvent;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback

{

    private GoogleMap mMap;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private LocationManager locationManager;
    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 1000;
    String gambar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ImageButton back = (ImageButton) toolbar.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */


    private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());

            if (mMap != null) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 8));
//                CameraUpdate cu = CameraUpdateFactory.newLatLng(loc);
//                CameraUpdate zoom = CameraUpdateFactory.zoomTo(7);
//                mMap.moveCamera(cu);
//                mMap.animateCamera(zoom);

            }
        }
    };

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationChangeListener(myLocationChangeListener);


        SharedPreferences pref = getSharedPreferences("data", Context.MODE_PRIVATE);

        final String apikeys = pref.getString("apikey", "");
        final String names = pref.getString("name", "");
        final int group = pref.getInt("group_id", 0);

        String DATA_MAPS;
        if (group == 4) {
            DATA_MAPS = AppConfig.url+"/map";
        }else if(group == 2){
            DATA_MAPS = AppConfig.url+"/mapP3";
        }else{
            DATA_MAPS = AppConfig.url+"/map";
        }
//        RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest arrReq = new JsonArrayRequest(DATA_MAPS,
                new Response.Listener<JSONArray>() {
                    LatLng location;
                    HashMap<String, Event> markerHolderMap = new HashMap<String, Event>();


                    @Override
                    public void onResponse(JSONArray response) {
                        int count = response.length();
                        for (int i = 0; i < count; i++) {
                            try {
                                JSONObject jo = response.getJSONObject(i);
                                double lat = Double.parseDouble(jo.getString("lat"));
                                double lng = Double.parseDouble(jo.getString("lng"));
                                double lats = jo.getDouble("lat");
                                double lngs = jo.getDouble("lng");
                                final String judul = jo.getString("judul");
                                gambar = jo.getString("gambar");
                                final String lokasi = jo.getString("lokasi");
                                final String deskripsi = jo.getString("deskripsi");
                                final String id = jo.getString("id");
                                final String startdate = jo.getString("start_date");
                                final String enddate = jo.getString("end_date");
                                final String email = jo.getString("email");
                                final String phone = jo.getString("phone");
                                final String alamat = jo.getString("alamat");
                                final String web = jo.getString("web");
                                final String youtube = jo.getString("youtube");
                                final String instagram = jo.getString("instagram");
                                final String fb = jo.getString("fb");
                                final String faq = jo.getString("faq");
                                final String kategori = jo.getString("kategori_acara");
                                final String twitter = jo.getString("twitter");
                                final String slug = jo.getString("slug");


                                location = new LatLng(lat, lng);
                                final MarkerOptions options = new MarkerOptions();
                                options.position(location);
                                options.anchor(.5f, .5f);

                                options.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_markertes2));

//                                options.title("lat");
//                                options.snippet("lng");
//                                options.snippet(jo.getString("shop"));

                                Marker marker = mMap.addMarker(options);
                                Event mHolder = new Event(judul, gambar, lokasi, deskripsi, id, startdate, enddate, email, phone, alamat, web, youtube, instagram, fb, faq, kategori, twitter, lats, lngs, slug);
                                markerHolderMap.put(marker.getId(), mHolder); //Add info to HashMap
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }


                        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                            // Use default InfoWindow frame
                            @Override
                            public View getInfoWindow(Marker location) {
                                return null;
                            }

                            // Defines the contents of the InfoWindow
                            @Override
                            public View getInfoContents(final Marker location) {

                                // Getting view from the layout file info_window_layout
                                View v = getLayoutInflater().inflate(R.layout.info_window, null);

                                // Getting the position from the marker
                                LatLng latLng = location.getPosition();
                                String juduls = location.getId();
                                final Event mHolder = markerHolderMap.get(location.getId());


                                gambar = mHolder.getImageUrl();

                                // Getting reference to the TextView to set latitude
                                TextView tvLat = (TextView) v.findViewById(R.id.lat);

                                // Getting reference to the TextView to set longitude
                                TextView tvLng = (TextView) v.findViewById(R.id.lng);

                                ImageView imageView = (ImageView) v.findViewById(R.id.imageViewMap);
                                Picasso.with(getApplicationContext())
                                        .load("http://febapp.com/"+gambar)
                                        .placeholder(R.mipmap.ic_nice)
                                        .error(R.mipmap.ic_nice)
                                        .into(imageView, new MarkerCallback(location));
//                                Glide.with(getApplicationContext()).load(gambar)
//                                        .fitCenter()
//                                        .placeholder(R.mipmap.ic_nice)
//                                        .error(R.mipmap.ic_nice)
//                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
//                                        .into(imageView);

//                                SharedPreferences settings = getApplicationContext().getSharedPreferences("data", Context.MODE_PRIVATE);
//                                boolean Checked = settings.getBoolean(gambar, false);
//
//                                if(!Checked){

//                                final Handler handler = new Handler();
//                                handler.postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//
//                                        location.showInfoWindow();
//                                        SharedPreferences settings = getApplicationContext().getSharedPreferences("data", Context.MODE_PRIVATE);
//                                        settings.edit().putBoolean(gambar, true).commit();
//                                    }
//                                }, 200);
//                                }else{
//
//                                }




//                                imageLoader.get(gambar, new ImageLoader.ImageListener() {
//
//                                    @Override
//                                    public void onErrorResponse(VolleyError error) {
//                                        Toast.makeText(getApplicationContext(), "onErrorResponse", Toast.LENGTH_LONG).show();
//                                    }
//
//                                    @Override
//                                    public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
//                                        if (response.getBitmap() != null) {
//                                            // load image into imageview
//                                            imageView.setImageBitmap(response.getBitmap());
//
//                                        }
//                                    }
//                                });
//                                imageView.setImageUrl(mHolder.getImageUrl(), imageLoader);
//                                imageView.setDefaultImageResId(R.mipmap.default_profil);
//                                imageView.setErrorImageResId(R.mipmap.default_profil);
                                // Setting the latitude
                                tvLat.setText(mHolder.getJudul());

                                // Setting the longitude
                                tvLng.setText(mHolder.getLokasi());

                                // Returning the view containing InfoWindow contents
                                return v;

                            }
                        });

//                        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//
//                            @Override
//                            public boolean onMarkerClick(final Marker mark) {
//
//
//                                mark.showInfoWindow();
//
//                                final Handler handler = new Handler();
//                                handler.postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        mark.showInfoWindow();
//
//                                    }
//                                }, 200);
//
//                                return true;
//                            }
//                        });

                        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                            @Override
                            public void onInfoWindowClick(Marker marker) {

                                Event mHolder = markerHolderMap.get(marker.getId());
                                Intent intent = new Intent(MapsActivity.this, DetailEvent.class);
                                intent.putExtra("event_judul", mHolder.getJudul()); //you can name the keys whatever you like
                                intent.putExtra("event_image", mHolder.getImageUrl());
                                intent.putExtra("event_id", mHolder.getId());
                                intent.putExtra("event_deskripsi", mHolder.getDeskripsi()); //note that all these values have to be primitive (i.e boolean, int, double, String, etc.)
                                intent.putExtra("event_lat", mHolder.getLat());
                                intent.putExtra("event_lng", mHolder.getLng());
                                intent.putExtra("event_email", mHolder.getEmail());
                                intent.putExtra("event_phone", mHolder.getPhone());
                                intent.putExtra("event_alamat", mHolder.getAlamat());
                                intent.putExtra("event_web", mHolder.getWeb());
                                intent.putExtra("event_youtube", mHolder.getYoutube());
                                intent.putExtra("event_instagram", mHolder.getInstagram());
                                intent.putExtra("event_fb", mHolder.getFb());
                                intent.putExtra("event_faq", mHolder.getFaq());
                                intent.putExtra("event_kategori", mHolder.getKategori_acara());
                                intent.putExtra("event_twitter", mHolder.getTwitter());
                                intent.putExtra("event_lokasi", mHolder.getLokasi());
                                intent.putExtra("event_startdate", mHolder.getStart_date());
                                intent.putExtra("event_enddate", mHolder.getEnd_date());
                                intent.putExtra("event_slug", mHolder.getSlug());


                                startActivity(intent);


                            }
                        });
//                        mMap.getUiSettings().setMyLocationButtonEnabled(true);
//                        LatLng sydney = new LatLng(-34, 151);
//                        mMap.addMarker(new MarkerOptions().position(sydney).title("ngetes isinya markery"));
//                        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                        CameraUpdate cu = CameraUpdateFactory.newLatLng(location);
                        CameraUpdate zoom = CameraUpdateFactory.zoomTo(7);
                        mMap.moveCamera(cu);
                        mMap.animateCamera(zoom);
                        mMap.getUiSettings().setZoomControlsEnabled(true);
                        mMap.getUiSettings().isMyLocationButtonEnabled();
                        mMap.getUiSettings().setMapToolbarEnabled(true);
                        mMap.getUiSettings().setMyLocationButtonEnabled(true);








//                        Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "onErrorResponse", Toast.LENGTH_LONG).show();
                    }
                }
        );

        AppController.getInstance().addToRequestQueue(arrReq);
//        Object TAG_REQUEST_QUEUE = new Object();
//        request.setTag(TAG_REQUEST_QUEUE);
//        mRequestQueue.add(request);
//        mRequestQueue.start();
                                // Add a marker in Sydney and move the camera


    }




    public class MarkerCallback implements Callback {
        Marker marker=null;

        MarkerCallback(Marker marker) {
            this.marker=marker;
        }

        @Override
        public void onError() {
            Log.e(getClass().getSimpleName(), "Error loading thumbnail!");
        }

        @Override
        public void onSuccess() {
            if (marker != null && marker.isInfoWindowShown()) {
                marker.hideInfoWindow();
                marker.showInfoWindow();
            }
        }
    }








//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}
