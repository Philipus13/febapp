package com.febapp.febapp.Helper;

import android.widget.Filter;

import com.febapp.febapp.App.Event;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Philipus on 19/05/2016.
 */
public class CustomNewFilter extends Filter {

    List<Event> filterList;
    EventNewAdapter adapter;

    public CustomNewFilter(List<Event> filterList, EventNewAdapter adapter) {
        this.filterList = filterList;
        this.adapter = adapter;
    }

    //FILTERING
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {

        //RESULTS
        FilterResults results=new FilterResults();

        //VALIDATION
        if(constraint != null && constraint.length()>0)
        {

            //CHANGE TO UPPER FOR CONSISTENCY
            constraint=constraint.toString().toUpperCase();

            ArrayList<Event> filteredEvent=new ArrayList<>();

            //LOOP THRU FILTER LIST
            for(int i=0;i<filterList.size();i++)
            {
                //FILTER (tinggal mainin logic aja mau filter berdasarkan apa nya )
                if(filterList.get(i).getJudul().toUpperCase().contains(constraint)|| filterList.get(i).getDeskripsi().toUpperCase().contains(constraint))
                {
                    filteredEvent.add(filterList.get(i));
                }
            }

            results.count=filteredEvent.size();
            results.values=filteredEvent;
        }else
        {
            results.count=filterList.size();
            results.values=filterList;
        }

        return results;
    }


    //PUBLISH RESULTS

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {

        adapter.eventes= (List<Event>) results.values;
        adapter.notifyDataSetChanged();

    }
}
