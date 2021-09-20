package com.bob.bobapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bob.bobapp.R;

public class NewSetupActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView txtNext;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_setup);
        findView();
    }

    // initialize object here...
    private void findView() {
        txtNext=findViewById(R.id.txtNext);
        txtNext.setOnClickListener(this);
    }

    /// listener
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.txtNext) {
            Intent intent = new Intent(getApplicationContext(), NewWealthMgmtActivity.class);
            startActivity(intent);
        }
    }
}