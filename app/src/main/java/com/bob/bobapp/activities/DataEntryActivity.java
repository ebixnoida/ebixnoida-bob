package com.bob.bobapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bob.bobapp.R;

public class DataEntryActivity extends AppCompatActivity {
    private AppCompatEditText edtCustomerId, edtSessionId, edtHeartBeat, edtChannelId;
    private AppCompatButton btnSubmit;
    private String customerId, sessionId="", heartbeat="", channelId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_entry);
        findView();
    }

    // initialize object here...
    private void findView() {
        edtCustomerId = findViewById(R.id.edtCustomerId);
        edtSessionId = findViewById(R.id.edtSessionId);
        edtHeartBeat = findViewById(R.id.edtHeartBeat);
        edtChannelId = findViewById(R.id.edtChannelId);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = validationForm();
                if (result.equalsIgnoreCase("success")) {
                    BOBIntent bobIntent = new BOBIntent(getApplicationContext());
                    bobIntent.putExtra("arg_customer_id", customerId);
                    bobIntent.putExtra("arg_login_session_id", sessionId);
                    bobIntent.putExtra("arg_heartbeat_token", heartbeat);
                    bobIntent.putExtra("arg_channel_id", channelId);

//                    bobIntent.putExtra("arg_customer_id","014325545");
//                    bobIntent.putExtra("arg_login_session_id","wfuXGkxXaGntZ-pRdsDDrHAby5t7z6sqRQbLPrdlsZy1fL1eJESs!-1765069390!1630327688279");
//                    bobIntent.putExtra("arg_heartbeat_token","20210830181937567407");
//                    bobIntent.putExtra("arg_channel_id","017");


                    startActivity(bobIntent);
                } else {
                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    // apply validation here...
    private String validationForm() {
        String result = "";
        customerId = edtCustomerId.getText().toString().trim();
        sessionId = edtSessionId.getText().toString().trim();
        heartbeat = edtHeartBeat.getText().toString().trim();
        channelId = edtChannelId.getText().toString().trim();
        if (customerId.equalsIgnoreCase("")) {
            edtCustomerId.setFocusable(true);
            edtCustomerId.requestFocus();
            return "please enter customer id";
        }
//        if (sessionId.equalsIgnoreCase("")) {
//            edtSessionId.setFocusable(true);
//            edtSessionId.requestFocus();
//            return "please enter session id";
//        }
//
        
//        if (heartbeat.equalsIgnoreCase("")) {
//            edtHeartBeat.setFocusable(true);
//            edtHeartBeat.requestFocus();
//            return "please enter heartbeat token";
//        }
        if (channelId.equalsIgnoreCase("")) {
            edtChannelId.setFocusable(true);
            edtChannelId.requestFocus();
            return "please enter channel id";
        }
        return result = "success";
    }
}