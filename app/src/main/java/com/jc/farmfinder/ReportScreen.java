package com.jc.farmfinder;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.os.StrictMode;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;


import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;



import java.io.UnsupportedEncodingException;

public class ReportScreen extends AppCompatActivity {


    FusedLocationProviderClient mFusedLocationClient;
    int PERMISSION_ID = 44;

    double latitude;
    double longitude;


    String imageFileURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_screen);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        checkPermissions();

        Button createReportButton = (Button)findViewById(R.id.CreateReportBtn);
        createReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {CreateReport();}
        });

        Button addImgButton = (Button)findViewById(R.id.addImgBtn);
        addImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {UploadImage();}
        });
    }

    private void CreateReport()
    {
        //get all the fields required
        EditText animalDesc = (EditText)findViewById(R.id.animalDescriptionTxt);
        EditText tagNumber = (EditText)findViewById(R.id.TagNumberTxt);
        EditText additionalInfo = (EditText)findViewById(R.id.additionalInfoTxt);
        ImageView thumbnail = (ImageView) findViewById(R.id.ImageThumbnail);

        //this field is required
        if(animalDesc.getText().toString() == "")
        {
            //create a toast informing the user that this field is required
            Toast.makeText(getApplicationContext(), "You must at least enter a description of the animal", Toast.LENGTH_LONG).show();//display the response on screen
        }
        else{

            try {
                //use the details provided to create the report
                //begin by attempting to upload the image to S3:


                //define the request queue and API URLS:

                //create a request using volley:
                RequestQueue queue = Volley.newRequestQueue(this);
                Request<String> mStringRequest;


                Endpoints ep = new Endpoints();



                //define the endpoints for the S3 upload and the create report endpoint
                String S3UploadURL = ep.APIGatewayURL + "/p400imagebucket/" + "imageFileURL";
                String reportCreateURL = ep.APIGatewayURL + "/createreport";

                //define the tomtom create report endpoint
                String tomtomURL = ep.tomtomBaseURL + latitude + "," + longitude + "&range=10000&key=M9ylDwqdBFDHX5EtfVDKFVT8EZDKbRn0&projectId=2dfb6001-ab98-431e-85b3-a498336de91b";


                //String Request initialized
                StringRequest S3UploadRequest = new StringRequest(Request.Method.POST, S3UploadURL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("VOLLEY", response);



                        //get user location
                        getLastLocation();
                        Toast.makeText(getApplicationContext(), "Location data : latitude: " + latitude + " longitude: " + longitude, Toast.LENGTH_LONG).show();

                        JSONObject dummyJSON = new JSONObject();

                        try{
                            dummyJSON.put("requestBody", "requestBody");
                        }
                        catch(JSONException j){
                            Log.e("Error in JSON: ", j.toString());
                        }


                        JsonObjectRequest tStringRequest = new JsonObjectRequest(Request.Method.GET, tomtomURL, dummyJSON, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("VOLLEY", response.toString());

                                try {

                                    //convert the acquired fence data to a string
                                    String response_value = response.toString();

                                    //create a JSON object
                                    JSONObject reportJSON = new JSONObject();

                                    reportJSON.put("animalDesc", animalDesc.getText());
                                    reportJSON.put("tagNumber", tagNumber.getText());
                                    reportJSON.put("additionalInfo", additionalInfo.getText());
                                    reportJSON.put("image", thumbnail);

                                    reportJSON.put("fenceData", response_value);

                                    //convert JSON object to a string to send with the request
                                    final String requestBody = reportJSON.toString();

                                    //String Request initialized
                                    StringRequest StringRequest = new StringRequest(Request.Method.POST, ep.APIGatewayURL, new Response.Listener<String>() {
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
                                                Log.i("Create Request AWS method Response: ", responseString);

                                                // can get more details such as response.headers
                                            }
                                            return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                                        }
                                    };

                                    //add the create report request to the queue
                                    queue.add(StringRequest);

                                } catch (Exception e) {
                                    Log.d("TomTom API callback error", e.getMessage());
                                }

                                //when report is created, return the user to the home screen
                                finish();
                            }


                        },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.d("Error Response", error.toString());
                                    }
                                }
                        );


                        queue.add(tStringRequest);


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
                    protected Response<String> parseNetworkResponse(NetworkResponse response) {
                        String responseString = "";
                        if (response != null) {
                            responseString = String.valueOf(response.statusCode);
                            Log.i("Create Request AWS method Response: ", responseString);

                            // can get more details such as response.headers
                        }
                        return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                    }
                };

                //add the S3 request to the queue
                queue.add(S3UploadRequest);

            }
            catch(Error e){
                Log.i("Error: ", e.toString());
            }
        }

    }

    private void UploadImage()
    {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        imagePickerActivityResult.launch(galleryIntent);
    }

    ActivityResultLauncher<Intent> imagePickerActivityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result != null) {
                        Uri imageUri = result.getData().getData();
                        imageFileURL = imageUri.toString();
                        ImageView thumbnail = (ImageView) findViewById(R.id.ImageThumbnail);
                        thumbnail.setImageURI(imageUri);
                    }
                }
            }
    );













    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        // check if permissions are given
        if (checkPermissions()) {

            // check if location is enabled
            if (isLocationEnabled()) {

                // getting last
                // location from
                // FusedLocationClient
                // object
                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {

                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                });
            } else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            // if permissions aren't available,
            // request for permissions
            requestPermissions();
        }
    }





    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();

            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();
        }
    };

    // method to check for permissions
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        // If we want background location
        // on Android 10.0 and higher,
        // use:
        // ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    // method to request for permissions
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    // method to check
    // if location is enabled
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    // If everything is alright then
    @Override
    public void
    onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }
    }




    //S3 UPLOAD CODE
    private void s3Upload(String msg_type, String path, String thumbnailUrl)
    {







    }








}












