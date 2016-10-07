package com.febapp.febapp.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.febapp.febapp.App.AppConfig;
import com.febapp.febapp.App.AppController;
import com.febapp.febapp.App.Event;
import com.febapp.febapp.Helper.EventAdapter;
import com.febapp.febapp.Helper.SQLiteHandler;
import com.febapp.febapp.Helper.SessionManager;
import com.febapp.febapp.MainActivity;
import com.febapp.febapp.R;
import com.febapp.febapp.activity.DetailEvent;
import com.febapp.febapp.activity.LoginActivity;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EventFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private CountDownTimer countDownTimer;
    Handler handler;
    Runnable runnable;
    ListView list;
    SwipeRefreshLayout swipe;
    List<Event> eventList = new ArrayList<Event>();
    int no;
    EventAdapter adapter;
    private int offSet = 0;
    int selectedPosition;
    SharedPreferences pref;
    private SQLiteHandler db;
    private SessionManager session;
    String DATA_URL;




    private OnFragmentInteractionListener mListener;


    private static final String TAG = MainActivity.class.getSimpleName();
    private SwipeRefreshLayout swipeRefreshLayout;
    public EventFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EventFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EventFragment newInstance(String param1, String param2) {
        EventFragment fragment = new EventFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        View x =  inflater.inflate(R.layout.fragment_event,null);;
        swipe = (SwipeRefreshLayout) x.findViewById(R.id.swipe_refresh_layout);
        list = (ListView) x.findViewById(R.id.list_event);
//        SearchView sv= (SearchView) x.findViewById(R.id.mSearch);

        eventList.clear();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener()

        {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub

                session = new SessionManager(getActivity());

                if (session.isLoggedIn()) {

                    pref = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);

                    final String apikey = pref.getString("apikey", "");
                    String judulss = eventList.get(position).getJudul();
                    String click = pref.getString("click"+judulss, "0");


                    if(click == "0" ) {

                        ((MainActivity)getActivity()).startCountdownTimer(judulss);
//                        startCountdownTimer(judulss);
                        inputKlik(apikey, eventList.get(position).getId());

                        Intent intent = new Intent(getActivity(), DetailEvent.class);
                        intent.putExtra("event_id", eventList.get(position).getId());
                        intent.putExtra("event_judul", eventList.get(position).getJudul()); //you can name the keys whatever you like
                        intent.putExtra("event_deskripsi", eventList.get(position).getDeskripsi()); //note that all these values have to be primitive (i.e boolean, int, double, String, etc.)
                        intent.putExtra("event_image", eventList.get(position).getImageUrl());
                        intent.putExtra("event_lat", eventList.get(position).getLat());
                        intent.putExtra("event_lng", eventList.get(position).getLng());
                        intent.putExtra("event_email", eventList.get(position).getEmail());
                        intent.putExtra("event_phone", eventList.get(position).getPhone());
                        intent.putExtra("event_alamat", eventList.get(position).getAlamat());
                        intent.putExtra("event_web", eventList.get(position).getWeb());
                        intent.putExtra("event_youtube", eventList.get(position).getYoutube());
                        intent.putExtra("event_instagram", eventList.get(position).getInstagram());
                        intent.putExtra("event_fb", eventList.get(position).getFb());
                        intent.putExtra("event_faq", eventList.get(position).getFaq());
                        intent.putExtra("event_kategori", eventList.get(position).getKategori_acara());
                        intent.putExtra("event_twitter", eventList.get(position).getTwitter());
                        intent.putExtra("event_lokasi", eventList.get(position).getLokasi());
                        intent.putExtra("event_startdate", eventList.get(position).getStart_date());
                        intent.putExtra("event_enddate", eventList.get(position).getEnd_date());
                        intent.putExtra("event_slug", eventList.get(position).getSlug());

                        startActivity(intent);
                    }else if(click == "1"){
                        Intent intent = new Intent(getActivity(), DetailEvent.class);
                        intent.putExtra("event_id", eventList.get(position).getId());
                        intent.putExtra("event_judul", eventList.get(position).getJudul()); //you can name the keys whatever you like
                        intent.putExtra("event_deskripsi", eventList.get(position).getDeskripsi()); //note that all these values have to be primitive (i.e boolean, int, double, String, etc.)
                        intent.putExtra("event_image", eventList.get(position).getImageUrl());
                        intent.putExtra("event_lat", eventList.get(position).getLat());
                        intent.putExtra("event_lng", eventList.get(position).getLng());
                        intent.putExtra("event_email", eventList.get(position).getEmail());
                        intent.putExtra("event_phone", eventList.get(position).getPhone());
                        intent.putExtra("event_alamat", eventList.get(position).getAlamat());
                        intent.putExtra("event_web", eventList.get(position).getWeb());
                        intent.putExtra("event_youtube", eventList.get(position).getYoutube());
                        intent.putExtra("event_instagram", eventList.get(position).getInstagram());
                        intent.putExtra("event_fb", eventList.get(position).getFb());
                        intent.putExtra("event_faq", eventList.get(position).getFaq());
                        intent.putExtra("event_kategori", eventList.get(position).getKategori_acara());
                        intent.putExtra("event_twitter", eventList.get(position).getTwitter());
                        intent.putExtra("event_lokasi", eventList.get(position).getLokasi());
                        intent.putExtra("event_startdate", eventList.get(position).getStart_date());
                        intent.putExtra("event_enddate", eventList.get(position).getEnd_date());
                        intent.putExtra("event_slug", eventList.get(position).getSlug());

                        startActivity(intent);
                    }else {
                        Intent intent = new Intent(getActivity(), DetailEvent.class);
                        intent.putExtra("event_id", eventList.get(position).getId());
                        intent.putExtra("event_judul", eventList.get(position).getJudul()); //you can name the keys whatever you like
                        intent.putExtra("event_deskripsi", eventList.get(position).getDeskripsi()); //note that all these values have to be primitive (i.e boolean, int, double, String, etc.)
                        intent.putExtra("event_image", eventList.get(position).getImageUrl());
                        intent.putExtra("event_lat", eventList.get(position).getLat());
                        intent.putExtra("event_lng", eventList.get(position).getLng());
                        intent.putExtra("event_email", eventList.get(position).getEmail());
                        intent.putExtra("event_phone", eventList.get(position).getPhone());
                        intent.putExtra("event_alamat", eventList.get(position).getAlamat());
                        intent.putExtra("event_web", eventList.get(position).getWeb());
                        intent.putExtra("event_youtube", eventList.get(position).getYoutube());
                        intent.putExtra("event_instagram", eventList.get(position).getInstagram());
                        intent.putExtra("event_fb", eventList.get(position).getFb());
                        intent.putExtra("event_faq", eventList.get(position).getFaq());
                        intent.putExtra("event_kategori", eventList.get(position).getKategori_acara());
                        intent.putExtra("event_twitter", eventList.get(position).getTwitter());
                        intent.putExtra("event_lokasi", eventList.get(position).getLokasi());
                        intent.putExtra("event_startdate", eventList.get(position).getStart_date());
                        intent.putExtra("event_enddate", eventList.get(position).getEnd_date());
                        intent.putExtra("event_slug", eventList.get(position).getSlug());

                        startActivity(intent);
                    }
                }
                else{
                    Intent intent = new Intent(getActivity(), DetailEvent.class);
                    intent.putExtra("event_id", eventList.get(position).getId());
                    intent.putExtra("event_judul", eventList.get(position).getJudul()); //you can name the keys whatever you like
                    intent.putExtra("event_deskripsi", eventList.get(position).getDeskripsi()); //note that all these values have to be primitive (i.e boolean, int, double, String, etc.)
                    intent.putExtra("event_image", eventList.get(position).getImageUrl());
                    intent.putExtra("event_lat", eventList.get(position).getLat());
                    intent.putExtra("event_lng", eventList.get(position).getLng());
                    intent.putExtra("event_email", eventList.get(position).getEmail());
                    intent.putExtra("event_phone", eventList.get(position).getPhone());
                    intent.putExtra("event_alamat", eventList.get(position).getAlamat());
                    intent.putExtra("event_web", eventList.get(position).getWeb());
                    intent.putExtra("event_youtube", eventList.get(position).getYoutube());
                    intent.putExtra("event_instagram", eventList.get(position).getInstagram());
                    intent.putExtra("event_fb", eventList.get(position).getFb());
                    intent.putExtra("event_faq", eventList.get(position).getFaq());
                    intent.putExtra("event_kategori", eventList.get(position).getKategori_acara());
                    intent.putExtra("event_twitter", eventList.get(position).getTwitter());
                    intent.putExtra("event_lokasi", eventList.get(position).getLokasi());
                    intent.putExtra("event_startdate", eventList.get(position).getStart_date());
                    intent.putExtra("event_enddate", eventList.get(position).getEnd_date());
                    intent.putExtra("event_slug", eventList.get(position).getSlug());

                    startActivity(intent);
                }
                }
            }

            );
            adapter=new

            EventAdapter(getActivity(),eventList

            );

            list.setAdapter(adapter);


//

            swipe.setOnRefreshListener(this);

            swipe.post(new

                               Runnable() {
                                   @Override
                                   public void run() {
                                       swipe.setRefreshing(true);
//                                   eventList.clear();
                                       adapter.notifyDataSetChanged();
                                       callEvent(0);
                                   }
                               }

            );

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
//        FloatingActionButton fab = (FloatingActionButton) x.findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                    alertSingleChoiceItems();
////                Snackbar.make(view, "Replace with your own action in eventfrag", Snackbar.LENGTH_LONG)
////                        .setAction("Action", null).show();
//            }
//        });


//        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String query) {
//                adapter.getFilter().filter(query);
//                return false;
//            }
//        });

        return x;
    }

//    private void startCountdownTimer(final String judul){
//        countDownTimer = new CountDownTimer(30000, 1000) {
//
//            public void onTick(long millisUntilFinished) {
//                pref = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor = pref.edit();
//                editor.putString("click"+judul, "1");
//                editor.commit();
//            }
//
//            public void onFinish() {
//                pref = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor = pref.edit();
//                editor.putString("click"+judul, "0");
//                editor.commit();
//            }
//        }.start();
//    }
    private void inputKlik(final String apikey,final String id) {
        // Tag used to cancel the request


        String tag_string_req = "req_register";

        pref = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);

        final String apikeys = pref.getString("apikey", "");
        final String names = pref.getString("name", "");
        final int group = pref.getInt("group_id", 0);


        String DATA_CLICK;
        if (group == 4) {
            DATA_CLICK = AppConfig.url+"/changeClick";
        }else if(group == 2){
            DATA_CLICK = AppConfig.url+"/changeClickP3";
        }else{
            DATA_CLICK = AppConfig.url+"/changeClick";
        }

        StringRequest strReq = new StringRequest(Request.Method.POST,
                DATA_CLICK, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jObj = new JSONObject(response);
                    String errorMsg = jObj.getString("message");

                }
                catch (JSONException e){
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getActivity(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id);




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

    public void alertSingleChoiceItems(){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Set the dialog title
        builder.setTitle("Choose Category")

                // specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive call backs when items are selected
                // again, R.array.choices were set in the resources res/values/strings.xml
                .setSingleChoiceItems(R.array.choices, selectedPosition, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Toast.makeText(getActivity(), "Some actions maybe? Selected index: " + arg1, Toast.LENGTH_LONG).show();

                    }

                })

                        // Set the action buttons
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // user clicked OK, so save the mSelectedItems results somewhere
                        // or return them to the component that opened the dialog

                        selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();

                        if(selectedPosition == 0) {

                            adapter.getFilter().filter("karir" ) ;


                        } else if(selectedPosition == 1) {
                            adapter.getFilter().filter("seni");

                        } else if(selectedPosition == 2) {
                            adapter.getFilter().filter("sosial");

                        } else if(selectedPosition == 3) {
                            adapter.getFilter().filter("teknologi");

                        }

                    }
                })

                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // removes the dialog from the screen

                    }
                })

                .show();

    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.grid_default, menu);

        SearchView searchView = (SearchView)menu.findItem(R.id.grid_default_search).getActionView();
        searchView.setQueryHint("ketik tanggal atau lokasi");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                adapter.getFilter().filter(query);//ada disinii kuncinya
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem mItem) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = mItem.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_filter) {
            alertSingleChoiceItems();
            return true;
        }

        return super.onOptionsItemSelected(mItem);
    }




    public void logoutUser() {
        session.setLogin(false);
        pref = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("apikey","");
        editor.putString("name","");
        editor.putString("email","");
        editor.putString("created_at", "");

        // Launching the login activity
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    private void callEvent(int page){

        session = new SessionManager(getActivity());
        swipe.setRefreshing(true);

        pref = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);

        final String apikeys = pref.getString("apikey", "");
        final String names = pref.getString("name", "");
        final int group = pref.getInt("group_id", 0);


        if (group == 4) {
            DATA_URL = AppConfig.url+"/even/";
        }else if(group == 2){
            DATA_URL = AppConfig.url+"/evenP3/";
        }else{
            DATA_URL = AppConfig.url+"/even/";
        }

        JsonArrayRequest arrReq = new JsonArrayRequest( DATA_URL + page,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        if (response.length() > 0) {
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject obj = response.getJSONObject(i);
                                    Event news = new Event();
                                    no = obj.getInt(AppConfig.TAG_NO);
                                    news.setId(obj.getString(AppConfig.TAG_ID));
                                    news.setJudul(obj.getString(AppConfig.TAG_JUDUL));
                                    news.setDeskripsi(obj.getString(AppConfig.TAG_DESKRIPSI));
                                    news.setDuit(obj.getInt(AppConfig.TAG_DUIT));
                                    news.setPersen(obj.getInt(AppConfig.TAG_PERSEN));
                                    news.setSisaHari(obj.getInt(AppConfig.TAG_SISA_HARI));
                                    news.setLat(obj.getDouble(AppConfig.TAG_LAT));
                                    news.setLng(obj.getDouble(AppConfig.TAG_LNG));
                                    news.setEmail(obj.getString(AppConfig.TAG_EMAIL));
                                    news.setPhone(obj.getString(AppConfig.TAG_PHONE));
                                    news.setAlamat(obj.getString(AppConfig.TAG_ALAMAT));
                                    news.setWeb(obj.getString(AppConfig.TAG_WEB));
                                    news.setYoutube(obj.getString(AppConfig.TAG_YOUTUBE));
                                    news.setInstagram(obj.getString(AppConfig.TAG_INSTAGRAM));
                                    news.setFb(obj.getString(AppConfig.TAG_FB));
                                    news.setFaq(obj.getString(AppConfig.TAG_FAQ));
                                    news.setKategori_acara(obj.getString(AppConfig.TAG_KATEGORI_ACARA));
                                    news.setTwitter(obj.getString(AppConfig.TAG_TWITTER));
                                    news.setLokasi(obj.getString(AppConfig.TAG_LOKASI));
                                    news.setStart_date(obj.getString(AppConfig.TAG_START_DATE));
                                    news.setEnd_date(obj.getString(AppConfig.TAG_END_DATE));
                                    news.setSlug(obj.getString(AppConfig.TAG_SLUG));
                                    if (obj.getString(AppConfig.TAG_IMAGE_URL) != "") {
                                        news.setImageUrl(obj.getString(AppConfig.TAG_IMAGE_URL));
                                    }
                                    eventList.add(news);
                                    if (no > offSet)
                                        offSet = no;
                                    Log.d(TAG, "offSet " + offSet);
                                } catch (JSONException e) {
                                    Log.e(TAG, "JSON Parsing error: " + e.getMessage());
                                }
                                adapter.notifyDataSetChanged();
                            }
                        }
                        swipe.setRefreshing(false);
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if ( error instanceof TimeoutError || error instanceof NoConnectionError ||error instanceof NetworkError) {
                    Toast.makeText(getActivity(),
                            "Please Check Your Connection",
                            Toast.LENGTH_LONG).show();
                }
                swipe.setRefreshing(false);
            }
        });
        AppController.getInstance().addToRequestQueue(arrReq);
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onRefresh() {
        if (isNetworkAvailable(getContext())) {
        eventList.clear();
        adapter.notifyDataSetChanged();
        callEvent(0);}
        else{
            swipe.setRefreshing(false);
            Toast.makeText(getActivity(),
                    "Please Check Your Connection",
                    Toast.LENGTH_LONG).show();
        }
    }

    public static interface ClickListener{
        public void onClick(View view, int position);
        public void onLongClick(View view, int position);
    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }



}