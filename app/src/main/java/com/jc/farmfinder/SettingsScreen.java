package com.jc.farmfinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SettingsScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_screen);


        Button addFenceButton = (Button)findViewById(R.id.MapScreenBtn);
        addFenceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {switchActivitiesAddFence();}
        });


    }






    private void switchActivitiesAddFence() {
        Intent goToAddFence = new Intent(this, MapScreen.class);
        startActivity(goToAddFence);
    }
}