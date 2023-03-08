package com.jc.farmfinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tomtom.sdk.location.GeoPoint;
import com.tomtom.sdk.map.display.TomTomMap;
import com.tomtom.sdk.map.display.common.Color;
import com.tomtom.sdk.map.display.gesture.OnMapClickListener;
import com.tomtom.sdk.map.display.image.Image;
import com.tomtom.sdk.map.display.image.ImageFactory;
import com.tomtom.sdk.map.display.marker.Label;
import com.tomtom.sdk.map.display.marker.MarkerOptions;
import com.tomtom.sdk.map.display.polygon.PolygonOptions;
import com.tomtom.sdk.map.display.ui.MapFragment;
import com.tomtom.sdk.map.display.ui.OnMapReadyCallback;

import org.json.JSONObject;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class MapScreen extends AppCompatActivity {

    TomTomMap tomTomMap;

    Image pinIconImage;
    Image shieldImage;
    Object tag;
    Label label;
    String balloonText;

    List<GeoPoint> areaPoints = new List<GeoPoint>() {
        @Override
        public int size() {
            return areaPoints.size();
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean contains(@Nullable Object o) {
            return false;
        }

        @NonNull
        @Override
        public Iterator<GeoPoint> iterator() {
            return null;
        }

        @NonNull
        @Override
        public Object[] toArray() {
            return new Object[0];
        }

        @NonNull
        @Override
        public <T> T[] toArray(@NonNull T[] ts) {
            return null;
        }

        @Override
        public boolean add(GeoPoint geoPoint) {
            //Toast.makeText(getApplicationContext(), geoPoint.toString(), Toast.LENGTH_SHORT).show();
            return false;
        }

        @Override
        public boolean remove(@Nullable Object o) {
            return false;
        }

        @Override
        public boolean containsAll(@NonNull Collection<?> collection) {
            return false;
        }

        @Override
        public boolean addAll(@NonNull Collection<? extends GeoPoint> collection) {
            return false;
        }

        @Override
        public boolean addAll(int i, @NonNull Collection<? extends GeoPoint> collection) {
            return false;
        }

        @Override
        public boolean removeAll(@NonNull Collection<?> collection) {
            return false;
        }

        @Override
        public boolean retainAll(@NonNull Collection<?> collection) {
            return false;
        }

        @Override
        public void clear() {

        }

        @Override
        public boolean equals(@Nullable Object o) {
            return false;
        }

        @Override
        public int hashCode() {
            return 0;
        }

        @Override
        public GeoPoint get(int i) {
            return null;
        }

        @Override
        public GeoPoint set(int i, GeoPoint geoPoint) {
            return null;
        }

        @Override
        public void add(int i, GeoPoint geoPoint) {

        }

        @Override
        public GeoPoint remove(int i) {
            return null;
        }

        @Override
        public int indexOf(@Nullable Object o) {
            return 0;
        }

        @Override
        public int lastIndexOf(@Nullable Object o) {
            return 0;
        }

        @NonNull
        @Override
        public ListIterator<GeoPoint> listIterator() {
            return null;
        }

        @NonNull
        @Override
        public ListIterator<GeoPoint> listIterator(int i) {
            return null;
        }

        @NonNull
        @Override
        public List<GeoPoint> subList(int i, int i1) {
            return null;
        }
    };
    Color outlineColor;
    double outlineWidth;
    Color fillColor;
    Image image;
    boolean isImageOverlay;
    boolean isClickable;



    @SuppressLint("UnsafeOptInUsageError")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_screen);

        //TextView area = findViewById(R.id.ShowAreaGeopoints);
        EditText FenceName = findViewById(R.id.FenceName);

        MapFragment mapFragment = (MapFragment)getSupportFragmentManager().findFragmentById(R.id.map_fragment);

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull TomTomMap map) {
                Log.d("TAG", "Map retrieved");
                tomTomMap = map;


                Image pinImage = ImageFactory.INSTANCE.fromResource(R.drawable.marker);

                tomTomMap.addOnMapClickListener(new OnMapClickListener() {
                    @Override
                    public boolean onMapClicked(@NonNull GeoPoint geoPoint) {
                        //Toast.makeText(getApplicationContext(), "Coordinate tapped: " + geoPoint.toString(), Toast.LENGTH_SHORT).show(); //display tapped area coodinate

                        //when a tap is detected, place a marker and add it to a list of coordinates for the fence
                        MarkerOptions markerOptions = new MarkerOptions(
                                geoPoint,
                                pinImage,
                                pinIconImage = null,
                                shieldImage = null,
                                tag         = null,
                                label       = null,
                                balloonText = "Balloon"
                                );

                        tomTomMap.addMarker(markerOptions);

                        areaPoints.add(geoPoint);
                        //area.setText(areaPoints.toString());

                        return false;
                    }
                });


            }
        });


        Button AddArea = findViewById(R.id.GetAreaButton);
        AddArea.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                //Toast.makeText(getApplicationContext(), "Points: " + areaPoints, Toast.LENGTH_LONG).show();

                    PolygonOptions polygonOptions = new PolygonOptions(
                            areaPoints,
                            outlineColor = Color.Companion.getBLUE(),
                            outlineWidth = 2.0,
                            fillColor = Color.Companion.getTRANSPARENT(),
                            image = null,
                            isImageOverlay = false,
                            isClickable = false
                    );

                    //area.setText(polygonOptions.toString());
                    //tomTomMap.addPolygon(polygonOptions);

                if(FenceName.getText().length() != 0)
                {
                    Toast.makeText(getApplicationContext(), "Area Added: " + FenceName.getText().toString(), Toast.LENGTH_LONG).show();
                    finish();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "You must enter a name for this area", Toast.LENGTH_LONG).show();
                }



            }
        });






    }
}