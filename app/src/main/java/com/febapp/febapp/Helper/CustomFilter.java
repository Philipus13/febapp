package com.febapp.febapp.Helper;

import android.widget.Filter;

import com.febapp.febapp.App.Event;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Philipus on 18/04/2016.
 */
public class CustomFilter extends Filter {

    List<Event> filterList;
    EventAdapter adapter;

    public CustomFilter(List<Event> filterList, EventAdapter adapter) {
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
                if(filterList.get(i).getLokasi().toUpperCase().contains(constraint)|| filterList.get(i).getEnd_date().toUpperCase().contains(constraint)|| filterList.get(i).getKategori_acara().toUpperCase().contains(constraint))
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