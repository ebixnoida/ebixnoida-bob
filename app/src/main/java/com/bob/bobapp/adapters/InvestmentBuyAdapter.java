package com.bob.bobapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bob.bobapp.R;
import com.bob.bobapp.api.response_object.InvestmentCartDetailsResponse;
import com.bob.bobapp.listener.OnItemDeleteListener;
import com.bob.bobapp.utility.FontManager;
import com.bob.bobapp.utility.Util;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class InvestmentBuyAdapter extends RecyclerView.Adapter<InvestmentBuyAdapter.ViewHolder> {
    private Context context;
    private OnItemDeleteListener OnItemDeleteListener;
    private ArrayList<InvestmentCartDetailsResponse> investmentCartDetailsResponseArrayList;
    private DecimalFormat formatter;
    public InvestmentBuyAdapter(Context context, ArrayList<InvestmentCartDetailsResponse> investmentCartDetailsResponseArrayList, OnItemDeleteListener OnItemDeleteListener) {
        this.context = context;
        this.investmentCartDetailsResponseArrayList = investmentCartDetailsResponseArrayList;
        this.OnItemDeleteListener = OnItemDeleteListener;
        formatter = new DecimalFormat("###,###,##0.00");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.investment_cart_adapter, parent, false);
        return new InvestmentBuyAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (investmentCartDetailsResponseArrayList.get(position).getTransactionType().equalsIgnoreCase("buy")) {
            holder.linearData.setVisibility(View.VISIBLE);
            FontManager.markAsIconContainer(holder.txtDelete, Util.iconFont);
            holder.txtSchemeName.setText(investmentCartDetailsResponseArrayList.get(position).getFundName());
            holder.txtAmount.setText(investmentCartDetailsResponseArrayList.get(position).getTxnAmountUnit());
            holder.txtAmount.setText(formatter.format(Double.parseDouble(investmentCartDetailsResponseArrayList.get(position).getTxnAmountUnit())));
        } else {
            holder.linearData.setVisibility(View.GONE);
        }

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnItemDeleteListener.onItemDeleteListener(investmentCartDetailsResponseArrayList.get(position).getICDID(), position,"");
                investmentCartDetailsResponseArrayList.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return investmentCartDetailsResponseArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtDelete;
        private AppCompatTextView txtSchemeName, txtAmount;
        private LinearLayoutCompat linearData;
        private ImageView imgDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDelete = itemView.findViewById(R.id.txtDelete);
            txtSchemeName = itemView.findViewById(R.id.txtSchemeName);
            txtAmount = itemView.findViewById(R.id.txtAmount);
            linearData = itemView.findViewById(R.id.linearData);
            imgDelete = itemView.findViewById(R.id.imgDelete);
        }
    }
}
