package com.febapp.febapp.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.febapp.febapp.App.AppConfig;
import com.febapp.febapp.App.AppController;
import com.febapp.febapp.App.Bookmark;
import com.febapp.febapp.Helper.BookmarkAdapter;
import com.febapp.febapp.Helper.SessionManager;
import com.febapp.febapp.MainActivity;
import com.febapp.febapp.R;
import com.febapp.febapp.activity.DetailEvent;
import com.febapp.febapp.activity.LoginActivity;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Philipus on 25/04/2016.
 */
public class BookmarkFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Handler handler;
    Runnable runnable;
    ListView list;
    SwipeRefreshLayout swipe;
    List<Bookmark> eventList = new ArrayList<Bookmark>();
    int no;
    BookmarkAdapter adapter;
    private int offSet = 0;
    int selectedPosition;

    private SessionManager session;



    private OnFragmentInteractionListener mListener;


    private static final String TAG = MainActivity.class.getSimpleName();
    private SwipeRefreshLayout swipeRefreshLayout;
    public BookmarkFragment() {
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
    public static BookmarkFragment newInstance(String param1, String param2) {
        BookmarkFragment fragment = new BookmarkFragment();
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

        View x =  inflater.inflate(R.layout.fragment_bookmark,null);

        LinearLayout linearLayout= (LinearLayout) x.findViewById(R.id.back);
        session = new SessionManager(getActivity());
        if (!session.isLoggedIn()) {
            linearLayout.setBackgroundResource(R.color.input_login_hint);
        }
        swipe = (SwipeRefreshLayout) x.findViewById(R.id.swipe_refresh_layout);
        list = (ListView) x.findViewById(R.id.list_bookmark);


//        SearchView sv= (SearchView) x.findViewById(R.id.mSearch);

        eventList.clear();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener()

                                    {

                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view,
                                                                int position, long id) {
                                            // TODO Auto-generated method stub
                                            Intent intent = new Intent(getActivity(), DetailEvent.class);
                                            intent.putExtra("event_id", eventList.get(position).getId());
                                            intent.putExtra("event_judul", eventList.get(position).getJudul()); //you can name the keys whatever you like
                                            intent.putExtra("event_deskripsi", eventList.get(position).getDeskripsi()); //note that all these values have to be primitive (i.e boolean, int, double, String, etc.)
                                            intent.putExtra("event_image", eventList.get(position).getImageUrl());
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

        );
        adapter = new BookmarkAdapter(getActivity(), eventList
        );

        list.setAdapter(adapter);



//

        swipe.setOnRefreshListener(this);

        swipe.post(new

                           Runnable() {
                               @Override
                               public void run() {
                                   swipe.setRefreshing(true);
                                   eventList.clear();
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

                    swipe.setRefreshing(true);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.grid_default, menu);


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

            return true;
        }

        return super.onOptionsItemSelected(mItem);
    }




    public void logoutUser() {
        session.setLogin(false);



        // Launching the login activity
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    private void callEvent(int page){

        // session manager
        session = new SessionManager(getActivity());
//        if (!session.isLoggedIn()) {
//            logoutUser();
//        }
        // Fetching user details from sqlite

        SharedPreferences pref = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        final String apikeys = pref.getString("apikey", "");
        final int group = pref.getInt("group_id", 0);

        String DATA_GET_NOTIF;
        if (group == 4) {
            DATA_GET_NOTIF = AppConfig.url+"/getbook/";
        }else if(group == 2){
            DATA_GET_NOTIF = AppConfig.url+"/getbookP3/";
        }else{
            DATA_GET_NOTIF = AppConfig.url+"/getbook/";
        }

        swipe.setRefreshing(true);

        // Creating volley request obj

        JsonArrayRequest arrReq = new JsonArrayRequest(Request.Method.POST, DATA_GET_NOTIF + page,null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        if (response.length() > 0) {
                            // Parsing json
                            for (int i = 0; i < response.length(); i++) {
                                try {

                                    JSONObject obj = response.getJSONObject(i);
                                    Bookmark news = new Bookmark();

                                    no = obj.getInt("no");

                                    news.setId(obj.getString("id"));
                                    news.setJudul(obj.getString("task"));
                                    news.setDeskripsi(obj.getString("deskripsi"));
                                    news.setDuit(obj.getInt("duit"));
                                    news.setPersen(obj.getInt("persen"));
                                    news.setSisaHari(obj.getInt("sisahari"));
                                    news.setLat(obj.getDouble("lat"));
                                    news.setLng(obj.getDouble("lng"));
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

                                    if (obj.getString("gambars") != "") {
                                        news.setImageUrl(obj.getString("gambars"));
                                    }




                                    // adding news to news array
                                    eventList.add(news);

                                    if (no > offSet)
                                        offSet = no;

                                    Log.d(TAG, "offSet " + offSet);

                                } catch (JSONException e) {
                                    Log.e(TAG, "JSON Parsing error: " + e.getMessage());
                                }

                                // notifying list adapter about data changes
                                // so that it renders the list view with updated data
                                adapter.notifyDataSetChanged();
                            }
                        }
                        swipe.setRefreshing(false);
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                swipe.setRefreshing(false);
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


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
        eventList.clear();
        adapter.notifyDataSetChanged();
        callEvent(0);
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