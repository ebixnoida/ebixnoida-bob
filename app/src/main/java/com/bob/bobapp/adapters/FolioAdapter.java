package com.bob.bobapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bob.bobapp.R;
import com.bob.bobapp.api.response_object.FolioWisePendingUnitsCollection;

import java.util.ArrayList;

public class FolioAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<FolioWisePendingUnitsCollection> folioWisePendingUnitsCollectionArrayList;


    public FolioAdapter(Context context, ArrayList<FolioWisePendingUnitsCollection> folioWisePendingUnitsCollectionArrayList) {
        this.context = context;
        this.folioWisePendingUnitsCollectionArrayList = folioWisePendingUnitsCollectionArrayList;
    }

    @Override
    public int getCount() {
        return folioWisePendingUnitsCollectionArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return  folioWisePendingUnitsCollectionArrayList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = mInflater.inflate(R.layout.spinner_row_item, parent, false);
        TextView title=convertView.findViewById(R.id.title);
        title.setText(folioWisePendingUnitsCollectionArrayList.get(position).getFolioNo());
        return convertView;
    }
}
