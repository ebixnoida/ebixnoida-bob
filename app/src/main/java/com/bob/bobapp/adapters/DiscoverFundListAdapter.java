package com.bob.bobapp.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bob.bobapp.R;
import com.bob.bobapp.activities.BuyActivity;
import com.bob.bobapp.activities.FactSheetActivity;
import com.bob.bobapp.activities.SipActivity;
import com.bob.bobapp.api.response_object.LstRecommandationDebt;

import java.util.ArrayList;

public abstract class DiscoverFundListAdapter extends RecyclerView.Adapter<DiscoverFundListAdapter.ViewHolder> {
    private Context context;
    private ArrayList<LstRecommandationDebt> lstRecommandationDebtArrayList;

    public DiscoverFundListAdapter(Context context, ArrayList<LstRecommandationDebt> lstRecommandationDebtArrayList) {
        this.context = context;
        this.lstRecommandationDebtArrayList = lstRecommandationDebtArrayList;
    }

    public void updateList(ArrayList<LstRecommandationDebt> list) {
        lstRecommandationDebtArrayList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.discover_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtFundName.setText(lstRecommandationDebtArrayList.get(position).getFundName());
        holder.txtThreeMonth.setText(lstRecommandationDebtArrayList.get(position).getReturnIn3Month());
        holder.txtOneYear.setText(lstRecommandationDebtArrayList.get(position).getReturnIn1Year());
        holder.txtSixMonth.setText(lstRecommandationDebtArrayList.get(position).getReturnIn6Month());


    }

    @Override
    public int getItemCount() {
        return lstRecommandationDebtArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txtFundName, txtSixMonth, txtThreeMonth, txtOneYear, txtBuy,txtSip;
        private ImageView imgFactsheet;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtFundName = itemView.findViewById(R.id.txtFundName);
            txtSixMonth = itemView.findViewById(R.id.txtSixMonth);
            txtThreeMonth = itemView.findViewById(R.id.txtThreeMonth);
            txtOneYear = itemView.findViewById(R.id.txtOneYear);
            txtBuy = itemView.findViewById(R.id.txtBuy);
            imgFactsheet = itemView.findViewById(R.id.imgFactsheet);
            txtSip = itemView.findViewById(R.id.txtSip);

            txtBuy.setOnClickListener(this);
            imgFactsheet.setOnClickListener(this);
            txtSip.setOnClickListener(this);
        }

        // click listener
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.txtBuy) {

                Bundle args = new Bundle();

                args.putString("commanScriptCode", lstRecommandationDebtArrayList.get(getAdapterPosition()).getSchemeCode());
                args.putString("schemeName", lstRecommandationDebtArrayList.get(getAdapterPosition()).getFundName());

                Fragment fragment = new BuyActivity();

                fragment.setArguments(args);

                getDetail(fragment);

            }
            if (view.getId() == R.id.imgFactsheet) {
                Bundle args = new Bundle();

                args.putString("commanScriptCode", lstRecommandationDebtArrayList.get(getAdapterPosition()).getSchemeCode());

                //  Fragment fragment = new HoldingDetailActivity();
                Fragment fragment = new FactSheetActivity();

                fragment.setArguments(args);

                getDetail(fragment);
            }

            if (view.getId() == R.id.txtSip) {

                Bundle args = new Bundle();

                args.putString("commanScriptCode", lstRecommandationDebtArrayList.get(getAdapterPosition()).getSchemeCode());
                args.putString("schemeName", lstRecommandationDebtArrayList.get(getAdapterPosition()).getFundName());

                Fragment fragment = new SipActivity();

                fragment.setArguments(args);

                getDetail(fragment);

            }
        }

    }

    public abstract void getDetail(Fragment fragment);
}
