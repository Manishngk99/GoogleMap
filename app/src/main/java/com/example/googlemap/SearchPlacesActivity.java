package com.example.googlemap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.googlemap.model.LatitudeLongitude;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class SearchPlacesActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private AutoCompleteTextView etCity;
    private Button btnSearch;
    private List<LatitudeLongitude> latitudeLongitudes;
    Marker markername;
    CameraUpdate center, zoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_places);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.FragMap);
        mapFragment.getMapAsync(this);

        etCity = findViewById(R.id.autoCompleteTextView);
        btnSearch = findViewById(R.id.btnSearch);

        fillArrayListAndSetAdapter();

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etCity.getText().toString())){
                    etCity.setError("Please Place the Name");
                    return;
                }
                //get the current location
                int position = SearchArrayList(etCity.getText().toString());
                if (position > -1)
                    loadMap(position);
                else
                    Toast.makeText(SearchPlacesActivity.this, "Location not found by name :"
                            +etCity.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void fillArrayListAndSetAdapter(){

        //3d27.7510825!4d85.2999685

        latitudeLongitudes = new ArrayList<>();
        latitudeLongitudes.add(new LatitudeLongitude(27.706195,85.3300396,"Softwarica College"));
        latitudeLongitudes.add(new LatitudeLongitude(27.7309123,85.2955242,"My Home"));
        latitudeLongitudes.add(new LatitudeLongitude(27.7510825,85.2999685,"Sunflower Academy"));

        String[] data = new String[latitudeLongitudes.size()];

        for (int i = 0; i < data.length; i++){
            data[i] = latitudeLongitudes.get(i).getMarker();
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                SearchPlacesActivity.this,
                android.R.layout.simple_list_item_1,
                data
        );
        etCity.setAdapter(arrayAdapter); //Setting marker values in autoCompleteText
        etCity.setThreshold(1);

    }

    public int SearchArrayList(String name){
        for (int i = 0; i < latitudeLongitudes.size(); i++){
            if (latitudeLongitudes.get(i).getMarker().contains(name)){
                return i;
            }
        }
        return -1;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //loading city ktm when launching app
        mMap = googleMap;
        center = CameraUpdateFactory.newLatLng(new LatLng(27.706195,85.3300396));
        zoom = CameraUpdateFactory.zoomTo(15);
        mMap.moveCamera(center);
        mMap.animateCamera(zoom);
        mMap.getUiSettings().setZoomControlsEnabled(true);

    }

    public void loadMap(int position){
        //remove old marker from map
        if (markername!= null)
        {
            markername.remove();
        }
        double latitude = latitudeLongitudes.get(position).getLat();
        double longitude = latitudeLongitudes.get(position).getLon();
        String marker = latitudeLongitudes.get(position).getMarker();
        center = CameraUpdateFactory.newLatLng(new LatLng(latitude,longitude));
        zoom = CameraUpdateFactory.zoomTo(17);
        markername = mMap.addMarker(new MarkerOptions().position(new LatLng(latitude,latitude))
                .title(marker));
        mMap.moveCamera(center);
        mMap.animateCamera(zoom);
//        mMap.addMarker(markername);
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }

}
