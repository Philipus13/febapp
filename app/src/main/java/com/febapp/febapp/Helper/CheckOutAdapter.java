package com.febapp.febapp.Helper;

/**
 * Created by Philipus on 05/03/2016.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.febapp.febapp.App.AppConfig;
import com.febapp.febapp.App.AppController;
import com.febapp.febapp.App.Event;
import com.febapp.febapp.Fragment.CartFragment;
import com.febapp.febapp.R;
import com.febapp.febapp.activity.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.febapp.febapp.R.id.total;

/**
 * Created by Belal on 11/9/2015.
 */




public class CheckOutAdapter extends BaseAdapter  {

    private CartFragment fragment;
    private Activity activity;
    private LayoutInflater inflater;
    List<Event> eventes;
    List<Event> filterList;
    CustomFilter filter;
    private SQLiteHandler db;
    private SessionManager session;
    ImageLoader imageLoader;
    int checked = 0;
    Handler handler;
    Runnable runnable;
    int check = 0;
    TextView textViewJudul,harga,total, qty ;
    Button plus, min;
    ImageButton del;

    public static ArrayList<Boolean> itemChecked1 = null;


    //List of eventes

    public CheckOutAdapter(Activity activity, List<Event> eventes) {
        this.activity = activity;
        this.eventes = eventes;
        this.filterList= eventes;
//        itemChecked1 = new ArrayList<Boolean>();
//        for (int i = 0; i < eventes.size(); i++) {
//            itemChecked1.add(i, eventes.get(i).isSelected()); // initializes all items value with
//
//        }

    }

    @Override
    public int getCount() {
        return eventes.size();
    }

    @Override
    public Object getItem(int location) {
        return eventes.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {



            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);


            convertView = inflater.inflate(R.layout.checkout_list, null);

        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageViewHero);
        textViewJudul = (TextView) convertView.findViewById(R.id.textViewJudul);
        harga = (TextView) convertView.findViewById(R.id.price);
        qty =(TextView) convertView.findViewById(R.id.quantity);
        total =(TextView) convertView.findViewById(R.id.total);

        final Event event = eventes.get(position);

//        thumbNail.setImageUrl(news.getGambar(), imageLoader);
//        judul.setText(news.getJudul());
//        timestamp.setText(news.getDatetime());
//        isi.setText(Html.fromHtml(news.getIsi()));
//        imageView.setImageUrl(event.getImageUrl(), imageLoader);
//        imageView.setDefaultImageResId(R.mipmap.default_profil);
//        imageView.setErrorImageResId(R.mipmap.default_profil);
//        Picasso.with(activity)
//                .load(event.getImageUrl())
//                .placeholder(R.mipmap.ic_nice)
//                .error(R.mipmap.ic_nice)
//                .into(imageView);
        Glide.with(activity).load("http://ktmonlinesystem.com/febapp/"+event.getImageUrl())
                .fitCenter()
                .placeholder(R.mipmap.ic_nice)
                .error(R.mipmap.ic_nice)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);



        textViewJudul.setText(event.getJudul());
        harga.setText("Rp "+String.valueOf(event.getHarga()));
        qty.setText(String.valueOf(event.getQty()+" barang"));
        total.setText(String.valueOf("Rp "+event.getTotal()));

        return convertView;
    }



    public void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();
        SharedPreferences pref = activity.getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("apikey","");
        editor.putString("name","");
        editor.putString("email", "");
        editor.putString("created_at", "");

        // Launching the login activity
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }
    private void tambahKurang(final String id, String CART) {
        // Tag used to cancel the request

        String tag_string_req = "req_register";
        SharedPreferences pref = activity.getSharedPreferences("data", Context.MODE_PRIVATE);

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
                    Toast.makeText(activity, "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(activity,
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_barang", id);




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
}
