package com.febapp.febapp.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.febapp.febapp.App.AppConfig;
import com.febapp.febapp.App.AppController;
import com.febapp.febapp.App.EventList;
import com.febapp.febapp.Helper.DividerDecoration;
import com.febapp.febapp.Helper.EventListAdapter;
import com.febapp.febapp.MainActivity;
import com.febapp.febapp.R;
import com.febapp.febapp.activity.DetailEvent;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

import static com.febapp.febapp.R.id.count;

public class EventListFragment extends Fragment {
    private static final String TAG = EventListFragment.class.getSimpleName();//debug purpose
    private static String CATEGORY = "All";
    private static int CAT_NUM = 0;

    private boolean loading;

    private List<EventList> eventList;
    private LinearLayoutManager mLayoutManager;
    private EventListAdapter eventAdapter;
    private int offset = 0;
    PhotoView imagezoom;
    AlertDialog dialog;
    private int count = 0 ;

    private View view;
    private XRecyclerView xrecyclerView;

    public EventListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_event_list, container, false);
        view = inflater.inflate(R.layout.fragment_event_list, container, false);

        xrecyclerView = (XRecyclerView) view.findViewById(R.id.recycler_view);


        eventList = new ArrayList<>();
        eventAdapter = new EventListAdapter(view.getContext(), eventList);


//        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager = new GridLayoutManager(getContext(), 3);
        xrecyclerView.setLayoutManager(mLayoutManager);
//        xrecyclerView.setHasFixedSize(true);
//        xrecyclerView.setItemAnimator(new DefaultItemAnimator());
//        DividerDecoration decoration = new DividerDecoration(getContext(), DividerDecoration.VERTICAL_LIST);
//        xrecyclerView.addItemDecoration(decoration);
        xrecyclerView.setAdapter(eventAdapter);


        xrecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), xrecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int positions) {


//                Intent intent = new Intent(getActivity(), DetailEvent.class);
//                intent.putExtra("event_id", eventList.get(positions).getId());
//                intent.putExtra("event_judul", eventList.get(positions).getName()); //you can name the keys whatever you like
//                intent.putExtra("event_stok", eventList.get(positions).getStok()); //note that all these values have to be primitive (i.e boolean, int, double, String, etc.)
//                intent.putExtra("event_image", eventList.get(positions).getThumbnail());
//                intent.putExtra("event_price", eventList.get(positions).getPrice());
//
//
//                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {
                int positions = position- 1;
//                Toast.makeText(view.getContext(), "tes aja"+eventList.get(position).getName(), Toast.LENGTH_LONG).show();
                String profil = "http://ktmonlinesystem.com/febapp/"+eventList.get(positions).getThumbnail();
                showPhoto(profil);
            }
        }));

        xrecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                offset = 0;
                eventList.clear();
                loading = false;
                prepareEventData();
            }

            @Override
            public void onLoadMore() {
                prepareEventData();
                loading = true;
            }
        });

        xrecyclerView.setLoadingMoreProgressStyle(ProgressStyle.SquareSpin);

        offset = 0;
        xrecyclerView.setRefreshing(true);

        return view;
    }

    private void showPhoto(String profil){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
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
        Glide.with(getActivity()).load(profil).asBitmap()
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

    @Override
    public void onStart() {
        super.onStart();
        eventAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        eventAdapter.notifyDataSetChanged();
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
            CartFragment secFrag = new CartFragment();
            FragmentTransaction fragTransaction = getFragmentManager().beginTransaction();
            fragTransaction.replace(R.id.flContent,secFrag );
            fragTransaction.addToBackStack(null);
            fragTransaction.commit();
            return true;
        }

        return super.onOptionsItemSelected(mItem);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.detail_event, menu);

        MenuItem menuItem = menu.findItem(R.id.testAction);

        SharedPreferences pref = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        int grandQtys = pref.getInt("getqty", 0);

        count = grandQtys;
        menuItem.setIcon(buildCounterDrawable(count, R.drawable.ic_shopping_cart));


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

    private void filterList(String category) {
        CATEGORY = category;
        eventList.clear();
        offset = 0;
        prepareEventData();
    }

    private void prepareEventData() {

        JsonObjectRequest eventReq = new JsonObjectRequest(Request.Method.GET, AppConfig.DATA_LIST+offset, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    if (response.getString("success").equalsIgnoreCase("true")) {
                        // Parsing json
                        try {
                            JSONArray events = response.getJSONArray("barang");

                            for (int i = 0; i < events.length(); i++) {
                                try {
                                    JSONObject event = events.getJSONObject(i);
//                                    JSONObject user = event.getJSONObject("user");
//                                    JSONObject categori = event.getJSONObject("categori");
//                                    JSONArray att = event.getJSONArray("attendees");

                                    EventList e = new EventList();
                                    e.setId(event.getString("id"));
                                    e.setName(event.getString("nama"));
                                    e.setPrice(event.getInt("harga"));
                                    e.setStok(event.getInt("stok"));

//                                    if (event.getInt("is_done") == 1) {
//                                        e.setIsDone(true);
//                                    } else {
//                                        e.setIsDone(false);
//                                    }

                                    e.setThumbnail(event.getString("gambar"));
//                                    e.setLatitude(event.getDouble("latitude"));
//                                    e.setLongitude(event.getDouble("longitude"));

                                    //used to filter
//                                    if (e.getCategory_name().equalsIgnoreCase(CATEGORY) || CATEGORY.equalsIgnoreCase("All")) {
                                        eventList.add(e);
//                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            offset += events.length();

                            if (events.length() == 0) {
                                xrecyclerView.setIsnomore(true);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        eventAdapter.notifyDataSetChanged();
                        //swipeLayout.setRefreshing(false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (loading) {
                    xrecyclerView.loadMoreComplete();
                } else {
                    xrecyclerView.refreshComplete();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());

                xrecyclerView.refreshComplete();
                final Snackbar snackbar = Snackbar
                        .make(view, R.string.network_unstable, Snackbar.LENGTH_INDEFINITE);

                snackbar.setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        xrecyclerView.setRefreshing(true);
                        snackbar.dismiss();
                    }
                });


                snackbar.show();

            }
        });

        AppController.getInstance().addToRequestQueue(eventReq);
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{

        private GestureDetector gestureDetector;
        private ClickListener clickListener;
        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener){
            this.clickListener=clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onSingleTapUp(MotionEvent e) {

                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if(child!=null && clickListener!=null){
                        clickListener.onLongClick(child, recyclerView.getChildAdapterPosition(child) );
                    }
                }
            });
        }
        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if(child!=null && clickListener!=null && gestureDetector.onTouchEvent(e)){
                clickListener.onClick(child, rv.getChildAdapterPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
    public static interface ClickListener{
        public void onClick(View view, int position);
        public void onLongClick(View view, int position);
    }


}
