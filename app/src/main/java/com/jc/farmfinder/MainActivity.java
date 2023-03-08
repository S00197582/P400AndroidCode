package com.jc.farmfinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Button loginButton= (Button)findViewById(R.id.LoginBtn);
        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                switchActivities();
            }
        });



    }

    private void switchActivities() {
        Intent goToHome = new Intent(this, HomeScreen.class);
        startActivity(goToHome);
    }
}