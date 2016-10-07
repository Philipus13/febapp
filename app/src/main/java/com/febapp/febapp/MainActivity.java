package com.febapp.febapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.febapp.febapp.App.AppConfig;
import com.febapp.febapp.App.AppController;
import com.febapp.febapp.Fragment.BookmarkFragment;
import com.febapp.febapp.Fragment.CartFragment;
import com.febapp.febapp.Fragment.EventFragment;
import com.febapp.febapp.Fragment.EventListFragment;
import com.febapp.febapp.Fragment.EventNewFragment;
import com.febapp.febapp.Fragment.EventPopularFragment;
import com.febapp.febapp.Fragment.HelpFrag;
import com.febapp.febapp.Fragment.MainTabFragment;
import com.febapp.febapp.Fragment.MapsActivity;
import com.febapp.febapp.Fragment.ProfileFrag;
import com.febapp.febapp.Helper.SessionManager;
import com.febapp.febapp.activity.LoginActivity;
import com.febapp.febapp.activity.RegisterActivity;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

import static android.R.attr.id;
import static android.R.attr.maxLength;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        EventFragment.OnFragmentInteractionListener,
        ProfileFrag.OnFragmentInteractionListener,
        EventNewFragment.OnFragmentInteractionListener,
        EventListFragment.OnFragmentInteractionListener,
        EventPopularFragment.OnFragmentInteractionListener,
        HelpFrag.OnFragmentInteractionListener,
        CartFragment.OnFragmentInteractionListener,
        BookmarkFragment.OnFragmentInteractionListener,

        MainTabFragment.OnFragmentInteractionListener {

    private TextView txtEmail;
    private Button btnLogout;
    private ProgressDialog pDialog;
    SharedPreferences pref;
    Handler handler;
    public String random;
    Runnable runnable;
    CountDownTimer countDownTimer;
    TextView txtName;
    String profil;
    PhotoView imagezoom;
    AlertDialog dialog;
    NavigationView navigationView;
    ImageView circularimageView;
//    private SQLiteHandler db;
    private SessionManager session;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    private static final String TAG = RegisterActivity.class.getSimpleName();

//    private String gambar = "http://192.168.0.13/task_manager/image/not.jpeg";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        //fungsi baru buat manggil yg di header
        View header = navigationView.getHeaderView(0);
//        View headerLayout =navigationView.inflateHeaderView(R.layout.nav_header_main);//ini ngebuat nav headernya ke inflate sekali lagi jadi double

         txtName = (TextView) header.findViewById(R.id.nameVisitor);

         circularimageView = (ImageView) header.findViewById(R.id.netimage);
         circularimageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPhoto();
            }
        });
//        circularimageView.setBorderColor(getResources().getColor(R.color.GrayLight));
//        circularimageView.setBorderWidth(10);
//        circularimageView.setSelectorColor(getResources().getColor(R.color.BlueLightTransparent));
//        circularimageView.setSelectorStrokeColor(getResources().getColor(R.color.BlueDark));
//        circularimageView.setSelectorStrokeWidth(10);
//        circularimageView.addShadow();
//        TextDrawable drawable = TextDrawable.builder()
//                .buildRoundRect("A", Color.RED, 10);


//        NetworkImageView image = (NetworkImageView) headerLayout.findViewById(R.id.netimage);

        //txtEmail = (TextView) findViewById(R.id.email);
        //btnLogout = (Button) findViewById(R.id.btnLogout);



//        image.setImageUrl(gambar, imageLoader);
//        image.setImageDrawable(drawable);

        // SqLite database handler
//        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

//        if (!session.isLoggedIn()) {
//            logoutUser();
//        }

        // Fetching user details from sqlite
//        HashMap<String, String> user = db.getUserDetails();
//
//        final String name = user.get("name");
//        final String apikey = user.get("uid");

        handler = new Handler();

        runnable = new Runnable() {
            public void run() {
                callPhoto();
            }
        };

        handler.postDelayed(runnable, 5000);

        SharedPreferences pref = getSharedPreferences("data", Context.MODE_PRIVATE);
        final String apikeys = pref.getString("apikey", "");
        final String names = pref.getString("name", "");
        final int group = pref.getInt("group_id", 0);
//        final String emails = pref.getString("email", "");
//        final String gambars = pref.getString("gambar", "");

        //String email = user.get("email");
        if (group == 4) {
            navigationView.getMenu().findItem(R.id.nav_notify).setVisible(false);
        }else if(group == 2){
            navigationView.getMenu().findItem(R.id.nav_notify).setVisible(true);
        }else{
            Toast.makeText(getApplicationContext(),
                    "it other than 2 and 4 error", Toast.LENGTH_LONG).show();
        }

        //Displaying the user details on the screen
        if (names == null || names =="") {
            navigationView.getMenu().findItem(R.id.nav_bookmark).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_notify).setVisible(false);
        }else{
            navigationView.getMenu().findItem(R.id.nav_bookmark).setVisible(true);
        }

        //
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            Fragment fragment = null;
            Class fragmentClass = null;
            fragmentClass = MainTabFragment.class;
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        }

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, FilterActivity.class);
//                startActivity(intent);
//                finish();
////                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
////                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

    }

//    public static String random() {
//        Random generator = new Random();
//        StringBuilder randomStringBuilder = new StringBuilder();
//        int randomLength = generator.nextInt(11);
//        char tempChar;
//        for (int i = 0; i < randomLength; i++){
//            tempChar = (char) (generator.nextInt(96) + 32);
//            randomStringBuilder.append(tempChar);
//        }
//        return randomStringBuilder.toString();
//    }

    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     * */
//    private void showDialog() {
//        if (!pDialog.isShowing())
//            pDialog.show();
//    }
//
//    private void hideDialog() {
//        if (pDialog.isShowing())
//            pDialog.dismiss();
//    }

//    @Override
//    protected void onResume() {
//        handler = new Handler();
//
//        runnable = new Runnable() {
//            public void run() {
//                callPhoto();
//            }
//        };
//
//        handler.postDelayed(runnable, 5000);
//        super.onResume();
//    }
    public void callPhoto(){
        SharedPreferences pref = getSharedPreferences("data", Context.MODE_PRIVATE);
        final String apikeys = pref.getString("apikey", "");
        final String names = pref.getString("name", "");
//        final String emails = pref.getString("email", "");
//        final String gambars = pref.getString("gambar", "");

        //String email = user.get("email");

        //Displaying the user details on the screen
        if (names == null || names =="") {
            txtName.setText("GUEST");

        }else{
            txtName.setText(names);
        }
        String tag_string_req = "req_login";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.PROFIL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {

                        // Now store the user
                        profil = jObj.getString("gambar");
                        String name = jObj.getString("name");
                        txtName.setText(name);
                        SharedPreferences pref = getSharedPreferences("data", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("gambars",profil);
                        editor.putString("name", name);
                        editor.commit();
//                        imageLoader.get(profil, new ImageLoader.ImageListener() {
//
//                            @Override
//                            public void onErrorResponse(VolleyError error) {
//                                Log.e(TAG, "Error nih: " + error.getMessage());
//                            }
//
//                            @Override
//                            public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
//                                if (response.getBitmap() != null) {
//                                    // load image into imageview
//                                    circularimageView.setImageBitmap(response.getBitmap());
//                                }
//                            }
//                        });

//                        Picasso.with(MainActivity.this)
//                                .load(profil)
//                                .placeholder(R.mipmap.default_profil)
//                                .into(circularimageView);
//                        Glide.with(MainActivity.this).load(profil)
//                                .thumbnail(0.5f)
//                                .crossFade()
//                                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                                .into(circularimageView);

                        Glide.with(MainActivity.this).load(profil).asBitmap().centerCrop()
                                .placeholder(R.mipmap.ic_profils)
                                .error(R.mipmap.ic_profils)
                                .into(new BitmapImageViewTarget(circularimageView) {
                                    @Override
                                    protected void setResource(Bitmap resource) {
                                        RoundedBitmapDrawable circularBitmapDrawable =
                                                RoundedBitmapDrawableFactory.create(MainActivity.this.getResources(), resource);
                                        circularBitmapDrawable.setCircular(true);
                                        circularimageView.setImageDrawable(circularBitmapDrawable);
                                    }
                                });



                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
//                Toast.makeText(getApplicationContext(),
//                        error.getMessage(), Toast.LENGTH_LONG).show();
                if ( error instanceof TimeoutError || error instanceof NoConnectionError ||error instanceof NetworkError) {
                    Toast.makeText(getApplicationContext(),
                            "Please Check Your Connection",
                            Toast.LENGTH_LONG).show();}

//                hideDialog();
            }
        }) {

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

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = MainActivity.this.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_profile_zoom, null);
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
        Glide.with(MainActivity.this).load(profil).asBitmap()
                .fitCenter()
                .placeholder(R.mipmap.ic_profils)
                .error(R.mipmap.ic_profils)
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
    public void startCountdownTimer(final String judul){
        countDownTimer = new CountDownTimer(1440000, 1000) {

            public void onTick(long millisUntilFinished) {
                pref = getApplicationContext().getSharedPreferences("data", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("click"+judul, "1");
                editor.commit();
            }

            public void onFinish() {
                pref = getApplicationContext().getSharedPreferences("data", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("click"+judul, "0");
                editor.commit();
            }
        }.start();
    }



    public void logoutUser() {
        session.setLogin(false);

//        db.deleteUsers();
        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
//        editor.putString("apikey","");
//        editor.putString("name","");
//        editor.putString("email","");
//        editor.putString("created_at","");
        editor.clear();
        editor.commit();

        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
                logoutUser();
        }else if(id == R.id.action_map) {
            Intent i = new Intent(getApplicationContext(),
                    MapsActivity.class);
            startActivity(i);
        }
//        else if(id == R.id.action_search) {
//
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    protected String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 10) {
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        Class fragmentClass = null;
        if (id == R.id.nav_profil) {
            fragmentClass = ProfileFrag.class;
        } else if (id == R.id.nav_home) {
            fragmentClass = MainTabFragment.class;
        } else if (id == R.id.nav_bookmark) {
            fragmentClass = BookmarkFragment.class;
        } else if (id == R.id.nav_gambar) {
//            random = getSaltString();
//            SharedPreferences pref = getSharedPreferences("data", Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = pref.edit();
//            editor.putString("Order",random);
//            editor.commit();

            fragmentClass = EventListFragment.class;
        } else if (id == R.id.nav_cart) {
            fragmentClass = CartFragment.class;
//        } else if (id == R.id.nav_ticket) {
//            fragmentClass = MainTabFragment.class;
//        } else if (id == R.id.nav_remind) {
//            fragmentClass = BookmarkFragment.class;
//        } else if (id == R.id.nav_notif) {
//            fragmentClass = MainTabFragment.class;
//        } else if (id == R.id.nav_settings) {
//            fragmentClass = MainTabFragment.class;
        }else if (id == R.id.nav_help) {
            fragmentClass = HelpFrag.class;
        }
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
