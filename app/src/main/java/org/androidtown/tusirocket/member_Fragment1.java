package org.androidtown.tusirocket;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by wonseok on 17. 7. 27.
 */

public class member_Fragment1 extends Fragment implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback {
    static final int REQ_PERMISSION = 1011;
    MapView mMapView;
    MarkerOptions RocketMarker;
    MarkerOptions save_Markers[] = new MarkerOptions[100];
    int num_save_marker = 0;
    LatLng RocketLatLng;
    double lat = 36, lon = 126, alt = 0;
    LatLng save_target;
    float save_zoom;
    FirebaseDatabase database;
    DatabaseReference myRef;
    DatabaseReference curRef;
    private GoogleMap googleMap;
    private AdView mAdView; // jul 29

    /*
    @Override
    public void onMapClick(LatLng latLng) {

        RocketMarker.position(new LatLng(lat, lon))
                .title("Rocket")
                .snippet("Rocket GPS");
        Log.d("wonseok", "Rocket LAT : " + lat + "Lon : " + lon);
        RocketLatLng = new LatLng(lat, lon);
        Log.d("wonseok", "RocketLatLng is completed!");
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(RocketLatLng));
        googleMap.addMarker(RocketMarker).showInfoWindow();
        Log.d("wonseok", "animated completed!");
    }*/

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
            Log.e("wonseok", "yes!");
        } else {
            String[] p = {Manifest.permission.ACCESS_FINE_LOCATION};
            requestPermissions(p, REQ_PERMISSION);
        }
        if(save_target != null){
            Log.d("wonseok", "member_Fragment1.onmapready() is run!");
            CameraPosition cameraPosition = new CameraPosition.Builder().target(save_target).zoom(save_zoom).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            int i;
            for(i=0; i<num_save_marker; i++){
                googleMap.addMarker(save_Markers[i]).showInfoWindow();
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        Log.e("wonseok", "Mapchecked");
        if (requestCode == REQ_PERMISSION) {
            Log.e("wonseok", "Authrization checked!");
            // if(permissions[0]== Manifest.permission.ACCESS_FINE_LOCATION && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                googleMap.setMyLocationEnabled(true);
                Log.e("wonseok", "Locaion is enabled!");
                LatLng sydney = new LatLng(-34, 151);
                googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"));

                CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                return;
            }
            Log.e("wonseok", "GoogleMap enabled!");
            //googleMap.setMyLocationEnabled(true);

            googleMap.setMyLocationEnabled(true);

            RocketMarker = new MarkerOptions();
            RocketMarker.position(new LatLng(lat, lon))
                    .title("Rocket")
                    .snippet("Rocket GPS");

            googleMap.addMarker(RocketMarker).showInfoWindow();
            RocketLatLng = new LatLng(lat, lon);
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(RocketLatLng));

            CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
            googleMap.animateCamera(zoom);


        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup
            container, @Nullable Bundle savedInstanceState) {
        //ViewGroup rootView;
        //try {
        //    rootView = (ViewGroup) inflater.inflate(R.layout.fragment1, container, false);
        // }catch(InflateException e){
        //     e.printStackTrace();
        //    return null;
        // }
        // return rootView;

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        curRef = database.getReference("CurrentLocation");
        curRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("latitude") == false || dataSnapshot.hasChild("longitude") == false) {
                    Log.e("wonseok", "there is no childs");
                    //Toast.makeText(,"로켓이 DB와 연결되어있지 않습니다.",Toast.LENGTH_LONG).show();
                    return;
                } else {
                    lat = dataSnapshot.child("latitude").getValue(double.class);
                    lon = dataSnapshot.child("longitude").getValue(double.class);

                    Log.d("wonseok", "Lat : " + lat + "Lon : " + lon);
                    if (RocketMarker == null) {
                        Log.d("wonseok", "Marker is not maked yet!");
                        RocketMarker = new MarkerOptions();
                        RocketMarker.position(new LatLng(lat, lon))
                                .title("Rocket")
                                .snippet("Rocket GPS");
                        Log.d("wonseok", "Rocket LAT : " + lat + "Lon : " + lon);
                        RocketLatLng = new LatLng(lat, lon);
                        save_Markers[num_save_marker++] = new MarkerOptions();
                        save_Markers[num_save_marker-1] = RocketMarker;
                        Log.d("wonseok", "RocketLatLng is completed!");
                        if(googleMap != null) {
                            googleMap.animateCamera(CameraUpdateFactory.newLatLng(RocketLatLng));
                            googleMap.addMarker(RocketMarker).showInfoWindow();
                            Log.d("wonseok", "animated completed!");
                        }
                    } else {
                        RocketMarker.position(new LatLng(lat, lon))
                                .title("Rocket")
                                .snippet("Rocket GPS");
                        Log.d("wonseok", "Rocket LAT : " + lat + "Lon : " + lon);
                        RocketLatLng = new LatLng(lat, lon);
                        Log.d("wonseok", "RocketLatLng is completed!");
                        googleMap.animateCamera(CameraUpdateFactory.newLatLng(RocketLatLng));
                        googleMap.addMarker(RocketMarker).showInfoWindow();
                        Log.d("wonseok", "animated completed!");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        View rootView = inflater.inflate(R.layout.member_fragment1, container, false);
        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMapView.getMapAsync(this);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onDestroy() {
        save_target = googleMap.getCameraPosition().target;
        save_zoom = googleMap.getCameraPosition().zoom;
        super.onDestroy();
    }
}
