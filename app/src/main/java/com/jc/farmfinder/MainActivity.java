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


        Button anonReportButton= (Button)findViewById(R.id.AnonymousReportBtn);
        anonReportButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                goToReport();
            }
        });


        Button signupButton= (Button)findViewById(R.id.signupBtn);
        signupButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                goToSignup();
            }
        });

    }


    private void switchActivities() {

        Cognito cog = new Cognito(this);

        //Log in the user using the details provided. Do not progress if they are incorrect

        EditText emailTbx = findViewById(R.id.EmailTbx);
        EditText passwordTbx = findViewById(R.id.passwordTbx);

        String email = emailTbx.getText().toString();
        String password = passwordTbx.getText().toString();

        cog.userLogin(email, password);
    }

    private void goToSignup() {
        Intent goToSignup = new Intent(this, LoginScreen.class);
        startActivity(goToSignup);
    }

    private void goToReport() {
        Intent goToReport = new Intent(this, ReportScreen.class);
        startActivity(goToReport);
    }

}