package com.bob.bobapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bob.bobapp.R;
import com.bob.bobapp.api.response_object.TranferSchemeResponse;

import java.util.ArrayList;

public class SwitchFundAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<TranferSchemeResponse> frequencyList;


    public SwitchFundAdapter(Context context,ArrayList<TranferSchemeResponse> frequencyList) {
        this.context = context;
        this.frequencyList = frequencyList;
    }

    @Override
    public int getCount() {
        return frequencyList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return  frequencyList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = mInflater.inflate(R.layout.switch_fund_item, parent, false);
        TextView title=convertView.findViewById(R.id.title);
        title.setText(frequencyList.get(position).getName());
        return convertView;
    }
}
