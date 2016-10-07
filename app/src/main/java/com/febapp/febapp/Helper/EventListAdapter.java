package com.febapp.febapp.Helper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.febapp.febapp.App.AppConfig;
import com.febapp.febapp.App.AppController;
import com.febapp.febapp.App.EventList;
import com.febapp.febapp.R;
import com.febapp.febapp.activity.DetailEvent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Created by HANGGI on 25/02/2016.
 */
public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.EventListViewHolder> {
    private List<EventList> eventList;
    private Context mContext;
    private String token;

    public EventListAdapter(Context context, List<EventList> eventList) {
        this.mContext = context;
        this.eventList = eventList;
    }

    public class EventListViewHolder extends RecyclerView.ViewHolder {
        public TextView tvEventName,
                tvDate,
                tvOrganizer,
                tvPrice,
                tvDescription,
                tvCategory,
                tvIsDone;
        public CheckBox cbAttendance, btnSave;
        public Button info; //button info
        public ImageView thumbNail;
        private EventList mEvent;

        public EventListViewHolder(View view) {
            super(view);
//            tvEventName = (TextView) view.findViewById(R.id.tv_event_name);
//            tvCategory = (TextView) view.findViewById(R.id.tv_category);
//            tvDate = (TextView) view.findViewById(R.id.tv_date);
//            tvOrganizer = (TextView) view.findViewById(R.id.tv_organizer);
//            tvPrice = (TextView) view.findViewById(R.id.tv_price);
//            tvDescription = (TextView) view.findViewById(R.id.tv_description);
//            tvIsDone = (TextView) view.findViewById(R.id.tv_isdone);
//            cbAttendance = (CheckBox) view.findViewById(R.id.cb_attendance);
//            btnSave = (CheckBox) view.findViewById(R.id.btn_save);
//            info = (Button) view.findViewById(R.id.btn_info);

//            cbAttendance.setVisibility(View.GONE);

            thumbNail = (ImageView) view.findViewById(R.id.img_thumbnail);




//            info.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    int pos = getLayoutPosition();
//                    int eventId = eventList.get(pos - 1).getId();
//
//                    EventList event = eventList.get(pos - 1);
//
//                    Intent i = new Intent(view.getContext(), EventDetailActivity.class);
//                    i.putExtra("event", event);
//                    i.putExtra("eventid", eventId);
//                    view.getContext().startActivity(i);
//                }
//            });

//            btnSave.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    int pos = getLayoutPosition();
//                    int eventId = eventList.get(pos).getId();
////                    saveEvent(pos, eventId, view);
//                }
//            });
        }

//        public void isDone(EventList event) {
//            mEvent = event;
//
//            if (mEvent.isDone()) {
//                thumbNail.setAlpha(0.2f);
//                tvIsDone.setVisibility(View.VISIBLE);
//            } else {
//                thumbNail.setAlpha(1f);
//                tvIsDone.setVisibility(View.GONE);
//            }
//        }

        public void setView(final EventList event) {
//            tvEventName.setText(event.getName());
//            tvCategory.setText(event.getCategory_name());
//            tvDate.setText(event.getStartDate());
//            tvOrganizer.setText(event.getOrganizer());
//            tvPrice.setText("Rp. " + String.valueOf(event.getPrice()));
//            tvDescription.setText(event.getDescription());

            thumbNail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), DetailEvent.class);
                    intent.putExtra("event_id", event.getId());
                    intent.putExtra("event_judul", event.getName()); //you can name the keys whatever you like
                    intent.putExtra("event_stok", event.getStok()); //note that all these values have to be primitive (i.e boolean, int, double, String, etc.)
                    intent.putExtra("event_image", event.getThumbnail());
                    intent.putExtra("event_price", event.getPrice());

                    view.getContext().startActivity(intent);

                }
            });

            Log.v(TAG, "tes"+event.getThumbnail());
            Glide.with(mContext).load("http://ktmonlinesystem.com/febapp/" + event.getThumbnail())
                    .thumbnail(0.5f)
                    .error(R.drawable.image_error)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(thumbNail);
        }

//        private void saveEvent(int pos, final int eventId, final View view) {
//            SharedPreferences sharedPref = view.getContext().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
//            String id = sharedPref.getString(Config.LOGGEDIN_USER_ID, null);
//            token = sharedPref.getString(Config.TOKEN, null);
//            JSONObject obj = new JSONObject();
//
//            try {
//                obj.put("event_id", eventId);
//                obj.put("user_id", id);
//                obj.put("remind_on", "");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            JsonObjectRequest saveReq = new JsonObjectRequest(Request.Method.POST, Config.SAVE_EVENT_URL, obj, new Response.Listener<JSONObject>() {
//                @Override
//                public void onResponse(JSONObject response) {
//                    try {
//                        if (!response.getString("token").isEmpty()) {
//                            String token = response.getString("token");
//                            SharedPreferences sharedPreferences = view.getContext().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
//                            SharedPreferences.Editor editor = sharedPreferences.edit();
//                            editor.putString(Config.TOKEN, token);
//                            editor.commit();
//                        }
//
//                        Log.d("--TAG-- Save EventList", response.getString("success") + " for event " + eventId + " user " + Config.LOGGEDIN_USER_ID);
//                        if (response.getString("success").equalsIgnoreCase("true")) {
//                            Toast.makeText(view.getContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(view.getContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
//                            ((CheckBox) view).setChecked(false);
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    Toast.makeText(view.getContext(), view.getContext().getString(R.string.connection_failure), Toast.LENGTH_SHORT).show();
//                    ((CheckBox) view).setChecked(false);
//                }
//            }) {
//                @Override
//                protected Map<String, String> getParams() throws AuthFailureError {
//                    Map<String, String> params = new HashMap();
//                    params.put("token", token);
//
//                    return params;
//                }
//            };
//
//            AppController.getInstance().addToRequestQueue(saveReq);
//        }
    }

    @Override
    public EventListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_list_row, parent, false);

        return new EventListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(EventListViewHolder holder, final int position) {
        EventList event = eventList.get(position);

        holder.setView(event);
//        holder.isDone(event);

    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }
}

