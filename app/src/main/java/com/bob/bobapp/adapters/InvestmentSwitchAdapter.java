package com.bob.bobapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bob.bobapp.R;
import com.bob.bobapp.api.response_object.InvestmentCartDetailsResponse;
import com.bob.bobapp.utility.FontManager;
import com.bob.bobapp.utility.Util;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class InvestmentSwitchAdapter extends RecyclerView.Adapter<InvestmentSwitchAdapter.ViewHolder> {
    private Context context;
    private ArrayList<InvestmentCartDetailsResponse> investmentCartDetailsResponseArrayList;
    private DecimalFormat formatter;

    public InvestmentSwitchAdapter(Context context, ArrayList<InvestmentCartDetailsResponse> investmentCartDetailsResponseArrayList) {
        this.context = context;
        this.investmentCartDetailsResponseArrayList = investmentCartDetailsResponseArrayList;
        formatter = new DecimalFormat("###,###,##0.00");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.investment_switch_adapter, parent, false);
        return new InvestmentSwitchAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (investmentCartDetailsResponseArrayList.get(position).getTransactionType().equalsIgnoreCase("switch")) {
            holder.linearData.setVisibility(View.VISIBLE);
            FontManager.markAsIconContainer(holder.txtDelete, Util.iconFont);
            holder.txtSchemeName.setText(investmentCartDetailsResponseArrayList.get(position).getFundName());
            holder.txtAmount.setText(formatter.format(Double.parseDouble(investmentCartDetailsResponseArrayList.get(position).getTxnAmountUnit())));
            holder.txtFrequency.setText(investmentCartDetailsResponseArrayList.get(position).getFrequency());
            holder.txtDay.setText(investmentCartDetailsResponseArrayList.get(position).getSIPStartDate());
            holder.txtCurrentFundValue.setText(formatter.format(Double.parseDouble(investmentCartDetailsResponseArrayList.get(position).getCurrentFundValue())));
            holder.txtCurrentunits.setText(formatter.format(Double.parseDouble(investmentCartDetailsResponseArrayList.get(position).getCurrentUnits())));
            holder.txtOrderBasis.setText(investmentCartDetailsResponseArrayList.get(position).getAmountOrUnit());
            holder.txtSwitchFund.setText(investmentCartDetailsResponseArrayList.get(position).getTargetFundName());
        } else {
            holder.linearData.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return investmentCartDetailsResponseArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtDelete;
        private AppCompatTextView txtSchemeName, txtAmount, txtFolio, txtFrequency, txtDay, txtCurrentFundValue,
                txtCurrentunits, txtOrderBasis, txtNoOfUnitsSwitch, txtSwitchFund;
        private LinearLayoutCompat linearData;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDelete = itemView.findViewById(R.id.txtDelete);
            txtSchemeName = itemView.findViewById(R.id.txtSchemeName);
            txtAmount = itemView.findViewById(R.id.txtAmount);
            txtFolio = itemView.findViewById(R.id.txtFolio);
            txtFrequency = itemView.findViewById(R.id.txtFrequency);
            txtDay = itemView.findViewById(R.id.txtDay);
            txtCurrentFundValue = itemView.findViewById(R.id.txtCurrentFundValue);
            txtCurrentunits = itemView.findViewById(R.id.txtCurrentunits);
            txtOrderBasis = itemView.findViewById(R.id.txtOrderBasis);
            txtNoOfUnitsSwitch = itemView.findViewById(R.id.txtNoOfUnitsSwitch);
            txtSwitchFund = itemView.findViewById(R.id.txtSwitchFund);
            linearData = itemView.findViewById(R.id.linearData);
        }
    }
}
