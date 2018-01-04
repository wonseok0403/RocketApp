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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by wonseok on 17. 7. 27.
 */

public class Fragment1 extends Fragment implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback {
    static final int REQ_PERMISSION = 1000;
    MapView mMapView;
    private GoogleMap googleMap;
    LatLng save_target;
    float save_zoom;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        Log.e("wonseok", "Mapchecked");
        if(requestCode == REQ_PERMISSION){

            Log.e("wonseok", "Authrization checked!");
            if(permissions[0]== Manifest.permission.ACCESS_FINE_LOCATION && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){

                    googleMap.setMyLocationEnabled(true);
                    Log.e("wonseok", "Locaion is enabled!");
                    LatLng sydney = new LatLng(-34, 151);
                    googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"));

                    CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    return;
                }
                Log.e("wonseok", "GoogleMap enabled!");
                googleMap.setMyLocationEnabled(true);
            }
            Log.e("wosneok", "sorry...");
            googleMap.setMyLocationEnabled(true);
            Log.e("wonseok", "Locaion is enabled!");
            LatLng sydney = new LatLng(-34, 151);
            googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"));

            CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            return;
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(savedInstanceState != null){
            Log.d("wonseok", "ActivityCreated!");
        }
    }

    @Override
    public void onMapReady(GoogleMap mMap) {


        googleMap = mMap;
                /*if(ContextCompat.checkSelfPermission(, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                    return;
                }

                if(ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},1001);
                    ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},1001);

                }
                else{
                    return;
                }*/

        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

            googleMap.setMyLocationEnabled(true);
            Log.e("wonseok", "yes!");
        }
        else {
            String[] p = {Manifest.permission.ACCESS_FINE_LOCATION};
            requestPermissions( p, REQ_PERMISSION);
        }
        if(save_target != null){
            CameraPosition cameraPosition = new CameraPosition.Builder().target(save_target).zoom(save_zoom).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //ViewGroup rootView;
        //try {
        //    rootView = (ViewGroup) inflater.inflate(R.layout.fragment1, container, false);
       // }catch(InflateException e){
       //     e.printStackTrace();
        //    return null;
       // }
       // return rootView;


        View rootView = inflater.inflate(R.layout.fragment1, container, false);
        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume(); //JUL 31 02:08

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch(Exception e){
            e.printStackTrace();
        }

        mMapView.getMapAsync(this);
        return rootView;
    }

    @Override
    public void onResume() {
        Log.d("wonseok", "Fragment1 : onResume()");
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

        Log.d("wonseok", "Fragment1 : onDestroy()");
        super.onDestroy();
    }
}
