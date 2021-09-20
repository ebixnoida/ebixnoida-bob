package com.bob.bobapp.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bob.bobapp.Home.BaseContainerFragment;
import com.bob.bobapp.R;
import com.bob.bobapp.activities.BuyActivity;
import com.bob.bobapp.activities.BuySIPRedeemSwitchActivity;
import com.bob.bobapp.activities.FactSheetActivity;
import com.bob.bobapp.activities.STPActivity;
import com.bob.bobapp.activities.SipActivity;
import com.bob.bobapp.activities.SwitchActivity;
import com.bob.bobapp.api.bean.ClientHoldingObject;
import com.bob.bobapp.utility.IntentKey;
import com.bob.bobapp.utility.Util;
import com.google.gson.Gson;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public abstract class AddTransactListAdapter extends RecyclerView.Adapter<AddTransactListAdapter.ViewHolder> {

    private Context context;

    private ArrayList<ClientHoldingObject> clientHoldingObjectArrayList;

    private Util util;
    private DecimalFormat formatter;

    public AddTransactListAdapter(Context context, ArrayList<ClientHoldingObject> clientHoldingObjectArrayList) {
        this.context = context;

        util = new Util(context);
        this.clientHoldingObjectArrayList = clientHoldingObjectArrayList;

        formatter = new DecimalFormat("###,###,##0.00");
    }

    public void updateList(ArrayList<ClientHoldingObject> list) {
        clientHoldingObjectArrayList = list;

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.add_transact_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ClientHoldingObject clientHoldingObject = clientHoldingObjectArrayList.get(position);

        holder.tvSchemeName.setText(clientHoldingObject.getSchemeName());

        holder.tvFolioNumber.setText(clientHoldingObject.getFolioNumber());

        holder.tvUnits.setText(clientHoldingObject.getQuantity());

        holder.tvUnits.setText(clientHoldingObject.getQuantity());

        holder.tvCost.setText(formatter.format(Double.parseDouble(clientHoldingObject.getValueOfCost())));

        holder.tvNetGain.setText(clientHoldingObject.getNetGain());

        double netGainPercent = util.truncateDecimal(Double.parseDouble(clientHoldingObject.getGainLossPercentage())).doubleValue();

        holder.tvNetGainPercent.setText(String.valueOf(netGainPercent) + "%");



        holder.tvMarketValue.setText(formatter.format(Double.parseDouble(clientHoldingObject.getMarketValue())));


        double xiir = util.truncateDecimal(Double.parseDouble(clientHoldingObject.getGainLossPercentage())).doubleValue();

        holder.tvXIIR.setText(String.valueOf(xiir) + "%");

        holder.transact.setTag(clientHoldingObject);

        holder.transact.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                ClientHoldingObject holdingObjectSelected = (ClientHoldingObject) view.getTag();

                showDialog(view.getId(), holdingObjectSelected);
            }
        });

        holder.tvExit.setTag(clientHoldingObject);

        holder.tvExit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                ClientHoldingObject holdingObjectSelected = (ClientHoldingObject) view.getTag();

                showDialog(view.getId(), holdingObjectSelected);
            }
        });
    }

    @Override
    public int getItemCount() {
        return clientHoldingObjectArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView transact, tvExit, tvSchemeName, tvFolioNumber, tvUnits, tvCost, tvNetGain, tvNetGainPercent, tvMarketValue, tvXIIR;
        ImageView imgDetails;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            transact = itemView.findViewById(R.id.transact);
            tvExit = itemView.findViewById(R.id.tvExit);
            imgDetails = itemView.findViewById(R.id.imgDetails);

            tvSchemeName = itemView.findViewById(R.id.tv_scheme_name);
            tvFolioNumber = itemView.findViewById(R.id.tv_folio_no);
            tvUnits = itemView.findViewById(R.id.tv_units);
            tvCost = itemView.findViewById(R.id.tv_cost);
            tvNetGain = itemView.findViewById(R.id.tv_net_gain);
            tvNetGainPercent = itemView.findViewById(R.id.tv_gain_or_loss_percent);
            tvMarketValue = itemView.findViewById(R.id.tv_market_value);
            tvXIIR = itemView.findViewById(R.id.tv_xiir);

            transact.setOnClickListener(this);
            tvExit.setOnClickListener(this);

            imgDetails.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {

            if (view.getId() == R.id.imgDetails) {

                Bundle args = new Bundle();

                args.putString("commanScriptCode", clientHoldingObjectArrayList.get(getAdapterPosition()).getCommonScripCode());

                //  Fragment fragment = new HoldingDetailActivity();
                Fragment fragment = new FactSheetActivity();

                fragment.setArguments(args);

                getDetail(fragment);


              //  getDetail(new FactSheetActivity());
            }
        }
    }

    public abstract void getDetail(Fragment fragment);

    private void showDialog(int id, ClientHoldingObject holdingObjectSelected) {
        Dialog dialog = new Dialog(context);

        dialog.setContentView(R.layout.dialog_transact);

        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        TextView buy = dialog.findViewById(R.id.buy);

        TextView sip = dialog.findViewById(R.id.sip);

        TextView redeem = dialog.findViewById(R.id.redeem);

        TextView tvSwitch = dialog.findViewById(R.id.tvSwitch);

        TextView swp = dialog.findViewById(R.id.swp);

        TextView stp = dialog.findViewById(R.id.stp);

        String response = new Gson().toJson(holdingObjectSelected);

        if (id == R.id.transact) {

            buy.setVisibility(View.VISIBLE);

            sip.setVisibility(View.VISIBLE);

            tvSwitch.setVisibility(View.VISIBLE);

            stp.setVisibility(View.VISIBLE);

            redeem.setVisibility(View.GONE);

            swp.setVisibility(View.GONE);

        } else {
            buy.setVisibility(View.GONE);

            sip.setVisibility(View.GONE);

            tvSwitch.setVisibility(View.GONE);

            stp.setVisibility(View.GONE);

            redeem.setVisibility(View.VISIBLE);

            swp.setVisibility(View.VISIBLE);
        }

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();

                Bundle bundle = new Bundle();

                bundle.putString(IntentKey.RESPONSE_KEY, response);

                BuyActivity fragment = new BuyActivity();

                fragment.setArguments(bundle);

                getDetail(fragment);


                //((BaseContainerFragment)getParentFragment()).replaceFragment(fragment, true);

            }
        });


        sip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();

                Bundle args = new Bundle();

                args.putString(IntentKey.RESPONSE_KEY, response);

                Fragment fragment = new SipActivity();

                fragment.setArguments(args);

                getDetail(fragment);

            }
        });

        redeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Bundle args = new Bundle();

                args.putString(IntentKey.TRANSACTION_TYPE_KEY, "redeem");

                args.putString(IntentKey.RESPONSE_KEY, response);

                Fragment fragment = new SwitchActivity();

                fragment.setArguments(args);

                getDetail(fragment);
            }
        });

        tvSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Bundle args = new Bundle();

                args.putString(IntentKey.TRANSACTION_TYPE_KEY, "switch");

                args.putString(IntentKey.RESPONSE_KEY, response);

                Fragment fragment = new SwitchActivity();

                fragment.setArguments(args);

                getDetail(fragment);
            }
        });

        swp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle args = new Bundle();

                args.putString(IntentKey.TRANSACTION_TYPE_KEY, "swp");

                args.putString(IntentKey.RESPONSE_KEY, response);

                Fragment fragment = new BuySIPRedeemSwitchActivity();

                fragment.setArguments(args);

                getDetail(fragment);
            }
        });

        stp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();

                Bundle args = new Bundle();

                args.putString(IntentKey.TRANSACTION_TYPE_KEY, "stp");

                args.putString(IntentKey.RESPONSE_KEY, response);

                Fragment fragment = new STPActivity();

                fragment.setArguments(args);

                getDetail(fragment);
            }
        });


        dialog.show();
    }
}
