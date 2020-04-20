package mpd.coursework;
/*
 Name                 Adam Hosie
 Student ID           S1624519
*/
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    RSSModelClass rssModelClass;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        intent = getIntent();
        rssModelClass = intent.getParcelableExtra("rssModelClass");

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Bundle extras = getIntent().getExtras();
        mMap = googleMap;

        String title = extras.getString("title");
        Double lat = extras.getDouble("lat");
        Double glong = extras.getDouble("geolong");
        LatLng mapPos1 = new LatLng(lat, glong);
        LatLng mapPos2 = new LatLng(55.8668,-4.2500);
        mMap.addMarker(new MarkerOptions().position(mapPos1).title(title).visible(true));
        mMap.addMarker(new MarkerOptions().position(mapPos2).title("GCU Building").visible(true).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        builder.include(mapPos1);
        builder.include(mapPos2);

        LatLngBounds bounds = builder.build();

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.10); // offset from edges of the map 10% of screen

        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);

        mMap.animateCamera(cu);


        //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mapPos1, 14));
    }


    public void onZoom(View view){
        if(view.getId()==R.id.zoomInButton){
            mMap.animateCamera(CameraUpdateFactory.zoomIn());
        }
        if(view.getId()==R.id.zoomOutButton){
            mMap.animateCamera(CameraUpdateFactory.zoomOut());
        }
    }

    public void ChangeType(View view)
    {
        if(mMap.getMapType() == GoogleMap.MAP_TYPE_NORMAL)
        {
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        }
        else if(mMap.getMapType() == GoogleMap.MAP_TYPE_SATELLITE)
        {
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        }
        else if(mMap.getMapType() == GoogleMap.MAP_TYPE_HYBRID)
        {
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
    }




}
