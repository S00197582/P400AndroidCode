package com.jc.farmfinder;

import static java.security.AccessController.getContext;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.widget.ToolbarWidgetWrapper;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.HttpResponse;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class HomeScreen extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

//        Button getButton = (Button)findViewById(R.id.GetRequestBtn);
//        getButton.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                getFences();
//            }
//        });


//        Button postButton = (Button)findViewById(R.id.PostRequestBtn);
//        postButton.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                addNewFence();
//            }
//        });
//
//
//        Button registerButton = (Button)findViewById(R.id.RegisterBtn);
//        registerButton.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                RegisterUser();
//            }
//        });

        Button createRequestButton= (Button)findViewById(R.id.CreateRequestBtn);
        createRequestButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                switchActivitiesReport();
            }
        });


        Button settingsButton = (Button)findViewById(R.id.settingsBtn);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {switchActivitiesSettings();}
        });

        Toolbar requestBar = findViewById(R.id.RequestBar);
        requestBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {switchActivitiesRepDetails();}
        });
    }









    //methods

    private void getFences()
    {
        RequestQueue queue = Volley.newRequestQueue(this);
        Request<String> mStringRequest;
        //TextView Displaydata = findViewById(R.id.GetDetails);

        String url = "https://api.tomtom.com/geofencing/1/projects/2dfb6001-ab98-431e-85b3-a498336de91b/fences?key=M9ylDwqdBFDHX5EtfVDKFVT8EZDKbRn0";

        //String Request initialized
        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(getApplicationContext(), "Response :" + response.toString(), Toast.LENGTH_LONG).show();//display the response on screen
                //Displaydata.setText(response.toString());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("VOLLEY", error.toString());
            }
        });

        queue.add(mStringRequest);

    }







    private void addNewFence()
    {
        String UserAdminID = "8V4fHSSXKU2DzYIFpkhgyh7SyXHCUqIdmPWYtEZis1Ik8S6H";
        String ProjectID = "2dfb6001-ab98-431e-85b3-a498336de91b";
        JSONArray coords = new JSONArray();

        //test user details
        //"UserAdminID" : "8V4fHSSXKU2DzYIFpkhgyh7SyXHCUqIdmPWYtEZis1Ik8S6H";
        //"id": "2dfb6001-ab98-431e-85b3-a498336de91b",
        //"name": "Test Project 1"


        RequestQueue queue = Volley.newRequestQueue(this);

        try {
            String URL = "https://api.tomtom.com/geofencing/1/projects/"+ProjectID+"/fence?key=M9ylDwqdBFDHX5EtfVDKFVT8EZDKbRn0&adminKey=" + UserAdminID;

            Log.d("TAG", URL);


        coords.put(-67.137343);
        coords.put(45.137451);


            Log.d("TAG", coords.toString());

            JSONObject jsonBody = new JSONObject();
            JSONObject geometryBody = new JSONObject();

            geometryBody.put("radius",75);
            geometryBody.put("type","Point");
            geometryBody.put("shapeType","Circle");
            geometryBody.put("coordinates", coords);


            jsonBody.put("name", "DemoFence7");
            jsonBody.put("type", "Feature");
            jsonBody.put("geometry", geometryBody);


            final String requestBody = jsonBody.toString();

            Log.d("TAG", requestBody);


            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("VOLLEY", response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);
                        // can get more details such as response.headers
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };

            queue.add(stringRequest);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    private void RegisterAdminkey() {
        RequestQueue queue = Volley.newRequestQueue(this);
        //EditText responseBox = findViewById(R.id.editTextTextPersonName4);

        //String Url = "https://api.tomtom.com/geofencing/1/regenerateKey?key=M9ylDwqdBFDHX5EtfVDKFVT8EZDKbRn0";
        String Url = "https://api.tomtom.com/geofencing/1/register?key=M9ylDwqdBFDHX5EtfVDKFVT8EZDKbRn0";



        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("secret", "mySecretKey");

        } catch (JSONException e) {
            e.printStackTrace();
        }


        //Log.d("TAG", jsonBody.toString());


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST, Url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getApplicationContext(), "Response: " + response, Toast.LENGTH_LONG).show();
                        //responseBox.setText(response.toString());
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error: " + error.getMessage());
            }
        }) {

            /**
             * Passing some request headers
             */

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };


        queue.add(jsonObjReq);

    }













//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Url, jsonBody, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                Toast.makeText(getApplicationContext(), "Response: "+response, Toast.LENGTH_LONG).show();
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//            }
//        });

//make the request to your server as indicated in your request url
        //Volley.newRequestQueue(getContext()).add(stringRequest);




    private void switchActivitiesReport() {
        Intent goToCreateReport = new Intent(this, ReportScreen.class);
        startActivity(goToCreateReport);
    }

    private void switchActivitiesSettings() {
        Intent goToSettings = new Intent(this, SettingsScreen.class);
        startActivity(goToSettings);
    }

    private void switchActivitiesRepDetails() {
        Intent goToReportDetails = new Intent(this, ReportDetailsScreen.class);
        startActivity(goToReportDetails);
    }

}