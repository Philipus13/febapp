package com.febapp.febapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.febapp.febapp.App.AppConfig;
import com.febapp.febapp.App.AppController;
import com.febapp.febapp.App.Event;
import com.febapp.febapp.Fragment.CartFragment;
import com.febapp.febapp.Helper.CartAdapter;
import com.febapp.febapp.Helper.CheckOutAdapter;
import com.febapp.febapp.Helper.SessionManager;
import com.febapp.febapp.MainActivity;
import com.febapp.febapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static android.R.attr.x;

import static com.febapp.febapp.App.AppConfig.CART;
import static com.febapp.febapp.R.id.include;
import static com.febapp.febapp.R.id.totalHarga;

public class Booking extends AppCompatActivity {

    int no;
    CheckOutAdapter adapter;
    private int offSet = 0;
    TextView totalHarga;
    int grandTotal,grandQty;
    private static final String TAG = MainActivity.class.getSimpleName();
    ListView list;
    Handler handler;
    Runnable runnable;
    SharedPreferences pref;
    int size,totalz;
    String random;
    RelativeLayout include;
    Button btnProses;
    FrameLayout buyss;
    List<Event> eventList = new ArrayList<Event>();

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        list = (ListView) findViewById(R.id.list_cart);
        totalHarga = (TextView) findViewById(R.id.totalHarga) ;
        btnProses = (Button) findViewById(R.id.btnProses) ;

        buyss = (FrameLayout) findViewById(R.id.flContent);


//        LinearLayout parent=(LinearLayout)findViewById(R.id.parent);
//        parent.setLayoutParams(new LinearLayout.LayoutParams(parent.getLayoutParams().height, dpToPx( eventList.size() *167 )));
//        list.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, theSizeIWant));
        btnProses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearStat(AppConfig.clearStat);
                insertOrder(totalz,AppConfig.insertOrder );
//                include.setVisibility(view.GONE);
//                buyss.setVisibility(view.VISIBLE);
                runnable = new Runnable() {
                    public void run() {
                        random = getSaltString();
                        SharedPreferences pref = getSharedPreferences("data", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("Order",random);
                        editor.commit();
                        Intent intent = new Intent(Booking.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        //nanti gausah manggil fragment lagi tapi panggil aja activity baru yang nunjukin pesanan dan cek email anda
//                        Fragment fragment = null;
//                        Class fragmentClass = null;
//                        fragmentClass = CartFragment.class;
//                        try {
//                            fragment = (Fragment) fragmentClass.newInstance();
//                        } catch (InstantiationException e) {
//                            e.printStackTrace();
//                        } catch (IllegalAccessException e) {
//                            e.printStackTrace();
//                        }
//                        FragmentManager fragmentManager = getSupportFragmentManager();
//                        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
                    }
                };
                handler.postDelayed(runnable, 1000);

            }
        });


//number_of_item_in_list is list side ~ listArray.size();
//per_item_size_in_dp = calculate item size in dp. Ex: 80dp



        adapter=new CheckOutAdapter(Booking.this,eventList);
        callEvent(0);

        handler = new Handler();

        runnable = new Runnable() {
            public void run() {
                list.setLayoutParams(new LinearLayout.LayoutParams(list.getLayoutParams().MATCH_PARENT, size*255 ));
            }
        };

        handler.postDelayed(runnable, 500);


        list.setOnScrollListener(new AbsListView.OnScrollListener()


        {

            private int currentVisibleItemCount;
            private int currentScrollState;
            private int currentFirstVisibleItem;
            private int totalItem;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                this.currentScrollState = scrollState;
                this.isScrollCompleted();
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {
                this.currentFirstVisibleItem = firstVisibleItem;
                this.currentVisibleItemCount = visibleItemCount;
                this.totalItem = totalItemCount;
            }

            private void isScrollCompleted() {
                if (totalItem - currentFirstVisibleItem == currentVisibleItemCount
                        && this.currentScrollState == SCROLL_STATE_IDLE) {

//                    swipe.setRefreshing(true);
                    handler = new Handler();

                    runnable = new Runnable() {
                        public void run() {
                            callEvent(offSet);
                        }
                    };

                    handler.postDelayed(runnable, 3000);
                }
            }

        });


        list.setAdapter(adapter);
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }
    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    private void clearStat(String CART) {
        // Tag used to cancel the request

        String tag_string_req = "req_register";
        SharedPreferences pref = getSharedPreferences("data", Context.MODE_PRIVATE);

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
                    Toast.makeText(Booking.this, "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(Booking.this,
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();

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

    private void insertOrder(final int hargaAkhir, String CART) {
        // Tag used to cancel the request

        String tag_string_req = "req_register";
        SharedPreferences pref = getSharedPreferences("data", Context.MODE_PRIVATE);

        final String apikeys = pref.getString("apikey", "");
        final String orders = pref.getString("Order", "");
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
                    Toast.makeText(Booking.this, "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(Booking.this,
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("harga", String.valueOf(hargaAkhir));
                params.put("id_order", String.valueOf(orders));

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

    private void callEvent(int page){

//        session = new SessionManager();


        pref = getSharedPreferences("data", Context.MODE_PRIVATE);

        final String apikeys = pref.getString("apikey", "");
        final String names = pref.getString("name", "");
        final int group = pref.getInt("group_id", 0);


//        if (group == 4) {
//            DATA_URL = AppConfig.url+"/even/";
//        }else if(group == 2){
//            DATA_URL = AppConfig.url+"/evenP3/";
//        }else{
//            DATA_URL = AppConfig.url+"/even/";
//        }

        JsonArrayRequest arrReq = new JsonArrayRequest(Request.Method.POST, CART + page,null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        if (response.length() > 0) {
                            try {
                                for (int a = 0; a < response.length(); a++) {
                                    JSONObject obj = response.getJSONObject(a);
                                    JSONArray event = obj.getJSONArray("events");

                                    grandTotal = obj.getInt("grand_total");
                                    grandQty = obj.getInt("grand_qty");
                                    pref = getSharedPreferences("data", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = pref.edit();
                                    editor.putInt("getqty", grandQty);
                                    editor.commit();
                                    Log.d("JsonArray",response.toString());

                                    // Parsing json
                                    for (int i = 0; i < event.length(); i++) {

                                        JSONObject jb = (JSONObject) event.get(i);
                                        Event news = new Event();

                                        int total = 0;
                                        no = jb.getInt("no");

                                        news.setId(jb.getString("id"));
                                        news.setJudul(jb.getString("nama"));
                                        news.setHarga(jb.getInt("harga"));
                                        news.setStok(jb.getInt("stok"));
                                        news.setQty(jb.getInt("qty"));
                                        news.setStatus(jb.getInt("status"));
                                        news.setTotal(jb.getInt("total"));


                                        if (jb.getString("gambar") != "") {
                                            news.setImageUrl(jb.getString("gambar"));
                                        }
                                        int si = size+1;
                                        totalz = (si*5000)+grandTotal;
                                        pref = getSharedPreferences("data", Context.MODE_PRIVATE);

                                        final String orders = pref.getString("Order", "");

                                        totalHarga.setText(si+ "Rp "+String.valueOf(totalz));
//                                        +"Rp "+String.valueOf(totalz)

                                        // adding news to news array
                                        eventList.add(news);
                                        size = eventList.size();
                                        if (no > offSet)
                                            offSet = no;

                                        Log.d(TAG, "offSet " + offSet);


                                        // notifying list adapter about data changes
                                        // so that it renders the list view with updated data
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                            }catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());

            }

        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("authorization", apikeys);
                return params;
            }};

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(arrReq);
    }
}
