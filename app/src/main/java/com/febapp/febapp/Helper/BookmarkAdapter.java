package com.febapp.febapp.Helper;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.febapp.febapp.App.AppController;
import com.febapp.febapp.App.Bookmark;
import com.febapp.febapp.R;
import com.android.volley.toolbox.ImageLoader;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

/**
 * Created by Philipus on 25/04/2016.
 */
public class BookmarkAdapter extends BaseAdapter  {

    private Activity activity;
    private LayoutInflater inflater;
    List<Bookmark> eventess;

    private SessionManager session;
    ImageLoader imageLoader;


    //List of eventes

    public BookmarkAdapter(Activity activity, List<Bookmark> eventess) {
        this.activity = activity;
        this.eventess = eventess;

    }

    @Override
    public int getCount() {
        return eventess.size();
    }

    @Override
    public Object getItem(int location) {
        return eventess.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView,  ViewGroup parent) {


        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
            convertView = inflater.inflate(R.layout.bookmark_list, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();




        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageBookmark);

        TextView textViewJudul = (TextView) convertView.findViewById(R.id.judul_bookmark);
        TextView startdates = (TextView) convertView.findViewById(R.id.startsdate);
        TextView enddates = (TextView) convertView.findViewById(R.id.endsdate);





        Bookmark event = eventess.get(position);

//        thumbNail.setImageUrl(news.getGambar(), imageLoader);
//        judul.setText(news.getJudul());
//        timestamp.setText(news.getDatetime());
//        isi.setText(Html.fromHtml(news.getIsi()));


//        Picasso.with(activity)
//                .load(event.getImageUrl())
//                .placeholder(R.mipmap.default_profil)
//                .into(imageView);
        Glide.with(activity).load("http://febapp.com/"+event.getImageUrl())
                .fitCenter()
                .placeholder(R.mipmap.ic_nice)
                .error(R.mipmap.ic_nice)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);

        textViewJudul.setText(event.getJudul());
        startdates.setText(event.getStart_date());
        enddates.setText(event.getEnd_date());


        return convertView;
    }

}

