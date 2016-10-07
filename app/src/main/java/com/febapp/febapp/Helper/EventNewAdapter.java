package com.febapp.febapp.Helper;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.febapp.febapp.App.AppConfig;
import com.febapp.febapp.App.AppController;
import com.febapp.febapp.App.Event;
import com.febapp.febapp.R;
import com.febapp.febapp.activity.LoginActivity;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Philipus on 19/05/2016.
 */
public class EventNewAdapter extends BaseAdapter implements Filterable {

    private Activity activity;
    private LayoutInflater inflater;
    List<Event> eventes;
    List<Event> filterList;
    CustomNewFilter filter;
    private SQLiteHandler db;
    private SessionManager session;
    ImageLoader imageLoader;
    int checked = 0;
    int check = 0;
    TextView textViewJudul,textStart,textEnd ;
    public static ArrayList<Boolean> itemChecked1 = null;


    //List of eventes

    public EventNewAdapter(Activity activity, List<Event> eventes) {
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


        convertView = inflater.inflate(R.layout.event_list, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();




        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageViewHero);
        final String id = eventes.get(position).getId();
        textViewJudul = (TextView) convertView.findViewById(R.id.textViewJudul);
        textStart =(TextView) convertView.findViewById(R.id.startdate);
        textEnd = (TextView) convertView.findViewById(R.id.enddate);



        final CheckBox love=(CheckBox) convertView.findViewById(R.id.love);;
        love.setTag(position);
        love.setChecked(false);
        final Event item = eventes.get(position);

        SharedPreferences settings = activity.getSharedPreferences("data",Context.MODE_PRIVATE);
        boolean Checked = settings.getBoolean(item.getJudul(), false);
        love.setChecked(Checked);
//        SharedPreferences sharedPref= activity.getSharedPreferences("mypref", 0);
//        int check = sharedPref.getInt("check"+position, 0);
//
//        if (check == 0) {
//            ;
//            love.setChecked(false);
//        }else if(check == 1) {
//
//            love.setChecked(true);
//        }

        session = new SessionManager(activity);

        if (session.isLoggedIn()) {
            love.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                                @Override
                                                public void onCheckedChanged(CompoundButton checkboxes, boolean isChecked) {


                                                    String id = eventes.get(position).getId();
                                                    String task = eventes.get(position).getJudul();
                                                    String gambars = eventes.get(position).getImageUrl();
                                                    String deskripsi = eventes.get(position).getDeskripsi();
                                                    int duit = eventes.get(position).getDuit();
                                                    int persen = eventes.get(position).getPersen();
                                                    int sisahari = eventes.get(position).getSisaHari();
                                                    Double lat = eventes.get(position).getLat();
                                                    Double lng = eventes.get(position).getLng();



                                                    SharedPreferences pref = activity.getSharedPreferences("data", Context.MODE_PRIVATE);
                                                    final String apikeys = pref.getString("apikey", "");


                                                    if (checkboxes.isChecked()==true) {

                                                        checked= 1;

                                                        inputNotif(apikeys, id);
                                                        Toast.makeText(activity,
                                                                "You favorited this event!" + item.getJudul(), Toast.LENGTH_SHORT)
                                                                .show();
                                                        SharedPreferences settings = activity.getSharedPreferences("data", Context.MODE_PRIVATE);
                                                        settings.edit().putBoolean(item.getJudul(), true).commit();
//                                                    SharedPreferences sharedPref= activity.getSharedPreferences("mypref", 0);
//                                                    //now get Editor
//                                                    SharedPreferences.Editor editor= sharedPref.edit();
//                                                    //put your value
//                                                    editor.putInt("check"+position, checked);
//                                                    //commits your edits
//                                                    editor.commit();


                                                    } else {

                                                        checked = 0;
                                                        deleteNotif(apikeys, id);
                                                        Toast.makeText(activity,
                                                                "you unfavorited this event!" + item.getJudul(), Toast.LENGTH_SHORT)
                                                                .show();
                                                        SharedPreferences settings = activity.getSharedPreferences("data", Context.MODE_PRIVATE);
                                                        settings.edit().putBoolean(item.getJudul(),false).commit();
//                                                    SharedPreferences sharedPref= activity.getSharedPreferences("mypref", 0);
//                                                    //now get Editor
//                                                    SharedPreferences.Editor editor= sharedPref.edit();
//                                                    //put your value
//                                                    //triknya ada di penamaan yg beda2, jadi ini keynya jadi check + position(check1,check2 dst)
//                                                    //itu kenapa pake shared preference biar bisa nyimpen multiple prefrences
//                                                    editor.putInt("check"+position, checked);
//                                                    //commits your edits
//                                                    editor.commit();

                                                    }

                                                }
                                            }
            );

        }else{
            love.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(activity,
                            "You must Login to access this!" , Toast.LENGTH_LONG)
                            .show();
                    love.setChecked(false);
                }
            });
        }

//        ImageButton button = (ImageButton) convertView.findViewById(R.id.imageButton);
//        button.setTag(position);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//                int position=(Integer)arg0.getTag();
//                Toast.makeText(activity,
//                        "Please enter your details!" + position, Toast.LENGTH_LONG)
//                        .show();
//                Intent intent = new Intent(activity, DetailEvent.class);
//                intent.putExtra("event_judul", eventes.get(position).getJudul()); //you can name the keys whatever you like
//                intent.putExtra("event_deskripsi", eventes.get(position).getDeskripsi()); //note that all these values have to be primitive (i.e boolean, int, double, String, etc.)
//                intent.putExtra("event_duit", eventes.get(position).getDuit());
//                intent.putExtra("event_persen", eventes.get(position).getPersen()); //note that all these values have to be primitive (i.e boolean, int, double, String, etc.)
//                intent.putExtra("event_sisa_hari", eventes.get(position).getSisaHari());
//                intent.putExtra("event_image", eventes.get(position).getImageUrl());
//                activity.startActivity(intent);
//            }
//        });
        ImageButton shareButton = (ImageButton) convertView.findViewById(R.id.shareButton);
        shareButton.setTag(position);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                int position=(Integer)arg0.getTag();
                String Judul = eventes.get(position).getJudul();
                String Deskripsi = eventes.get(position).getDeskripsi();

                Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, Judul);
                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, Deskripsi);
                activity.startActivity(Intent.createChooser(shareIntent, "Share via"));

                PackageManager pm = parent.getContext().getPackageManager();
                List<ResolveInfo> activityList = pm.queryIntentActivities(shareIntent, 0);
                for (final ResolveInfo app : activityList)
                {
                    if ((app.activityInfo.name).contains("facebook"))
                    {
                        final ActivityInfo activity = app.activityInfo;
                        final ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);
                        shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                        shareIntent.setComponent(name);
                        parent.getContext().startActivity(shareIntent);
                        break;
                    }
                }



            }
        });


        Event event = eventes.get(position);

//        thumbNail.setImageUrl(news.getGambar(), imageLoader);
//        judul.setText(news.getJudul());
//        timestamp.setText(news.getDatetime());
//        isi.setText(Html.fromHtml(news.getIsi()));
//        imageView.setImageUrl(event.getImageUrl(), imageLoader);
//        imageView.setDefaultImageResId(R.mipmap.default_profil);
//        imageView.setErrorImageResId(R.mipmap.default_profil);
//        Picasso.with(activity)
//                .load(event.getImageUrl())
//                .placeholder(R.mipmap.default_profil)
//                .error(R.mipmap.default_profil)
//                .into(imageView);


        textViewJudul.setText(event.getJudul());
        textStart.setText(event.getStart_date());
        textEnd.setText(event.getEnd_date());


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
    private void inputNotif(final String apikey,final String id) {
        // Tag used to cancel the request

        String tag_string_req = "req_register";

        SharedPreferences pref = activity.getSharedPreferences("data", Context.MODE_PRIVATE);

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
                params.put("id_kampanye", id);




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
    private void deleteNotif(final String apikey, final String id) {
        // Tag used to cancel the request



        String tag_string_req = "req_register";

        SharedPreferences pref = activity.getSharedPreferences("data", Context.MODE_PRIVATE);

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
                params.put("id_kampanye", id);

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

    @Override
    public Filter getFilter() {

        if(filter==null)
        {
            filter=new CustomNewFilter(filterList,this);
        }

        return filter;
    }


}
