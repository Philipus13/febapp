package com.febapp.febapp.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.febapp.febapp.App.AppConfig;
import com.febapp.febapp.App.AppController;
import com.febapp.febapp.App.Event;
import com.febapp.febapp.Helper.CartAdapter;
import com.febapp.febapp.Helper.EventAdapter;
import com.febapp.febapp.Helper.SQLiteHandler;
import com.febapp.febapp.Helper.SessionManager;
import com.febapp.febapp.MainActivity;
import com.febapp.febapp.R;
import com.febapp.febapp.activity.Booking;
import com.febapp.febapp.activity.DetailEvent;
import com.febapp.febapp.activity.LoginActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.febapp.febapp.App.AppConfig.DATA_GET_NOTIF;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CartFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
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
    TextView totalHarga;
    ListView list;
    SwipeRefreshLayout swipe;
    List<Event> eventList = new ArrayList<Event>();
    int no;
    CartAdapter adapter;
    private int offSet = 0;
    int selectedPosition;
    SharedPreferences pref;
    private SQLiteHandler db;
    private SessionManager session;
    String DATA_URL;
    private int count = 2;
    int grandTotal,grandQty;
    MenuItem menuItem;
    Button checkout;



    private OnFragmentInteractionListener mListener;


    private static final String TAG = MainActivity.class.getSimpleName();
    private SwipeRefreshLayout swipeRefreshLayout;
    public CartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
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
        View x =  inflater.inflate(R.layout.fragment_cart,null);;
        swipe = (SwipeRefreshLayout) x.findViewById(R.id.swipe_refresh_layout);
        list = (ListView) x.findViewById(R.id.list_cart);
        totalHarga = (TextView) x.findViewById(R.id.totalHarga) ;
        checkout = (Button) x.findViewById(R.id.checkOut);

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Booking.class);

                startActivity(intent);
            }
        });

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
                intent.putExtra("event_stok", eventList.get(position).getStok()); //note that all these values have to be primitive (i.e boolean, int, double, String, etc.)
                intent.putExtra("event_price", eventList.get(position).getHarga());
                intent.putExtra("event_image", eventList.get(position).getImageUrl());

                startActivity(intent);

                }
            }

            );
            adapter=new CartAdapter(getActivity(),eventList, CartFragment.this);

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
                                       getActivity().invalidateOptionsMenu();
                                       totalHarga.setText("Rp "+String.valueOf(grandTotal));
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
                                getActivity().invalidateOptionsMenu();
                                totalHarga.setText("Rp "+String.valueOf(grandTotal));
                            }
                        };

                        handler.postDelayed(runnable, 3000);
                    }
                }

            });

        handler = new Handler();

        runnable = new Runnable() {
            public void run() {
                totalHarga.setText("Rp "+String.valueOf(grandTotal));
            }
        };

        handler.postDelayed(runnable, 500);


        return x;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.detail_event, menu);
        menuItem = menu.findItem(R.id.testAction);
        handler = new Handler();

        runnable = new Runnable() {
            public void run() {

                pref = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
                int grandQtys = pref.getInt("getqty", 0);
                count = grandQtys;
                menuItem.setIcon(buildCounterDrawable(count, R.drawable.ic_shopping_cart));
            }
        };

        handler.postDelayed(runnable, 200);


        super.onCreateOptionsMenu(menu, inflater);

    }

    private Drawable buildCounterDrawable(int count, int backgroundImageId) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.counter_menuitem_layout, null);
        view.setBackgroundResource(backgroundImageId);

        if (count == 0) {
            View counterTextPanel = view.findViewById(R.id.counterValuePanel);
            counterTextPanel.setVisibility(View.GONE);
        } else {
            TextView textView = (TextView) view.findViewById(R.id.count);
            textView.setText("" + count);
        }

        view.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);

        return new BitmapDrawable(getResources(), bitmap);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem mItem) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = mItem.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.testAction) {
//            alertSingleChoiceItems();

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

//        session = new SessionManager(getActivity());
        swipe.setRefreshing(true);

        pref = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);

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

        JsonArrayRequest arrReq = new JsonArrayRequest(Request.Method.POST, AppConfig.CART + page,null,
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
                                    pref = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
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


                                        // adding news to news array
                                        eventList.add(news);

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
        callEvent(0);
        handler = new Handler();

        runnable = new Runnable() {
            public void run() {
                totalHarga.setText("Rp "+String.valueOf(grandTotal));
                getActivity().invalidateOptionsMenu();
            }
        };

        handler.postDelayed(runnable, 500);
        }
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