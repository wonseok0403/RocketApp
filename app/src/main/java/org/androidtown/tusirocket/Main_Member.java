package org.androidtown.tusirocket;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static org.androidtown.tusirocket.R.id.container;

public class Main_Member extends AppCompatActivity implements OnMapReadyCallback {
    android.app.ActionBar abar;
    Fragment fragment1;
    Fragment fragment2;
    Fragment fragment3;
    FirebaseDatabase database;
    DatabaseReference myRef;
    DatabaseReference curRef;
    private AdView mAdView; // jul 29


    double lat = 37, lon = 126;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__member);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        curRef = database.getReference("CurrentLocation");
        abar = getActionBar();
        try {
            abar.hide();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fragment2 = new member_Fragment2();
        fragment3 = new member_Fragment3();
        fragment1 = new member_Fragment1();

        getSupportFragmentManager().beginTransaction().add(container, fragment1).commit();

        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText("위치검색"));
        tabs.addTab(tabs.newTab().setText("데이터뷰"));
        tabs.addTab(tabs.newTab().setText("커뮤니티"));

        /*/JUL 29
        MobileAds.initialize(this, "ca-app-pub-3302287697444700~9323305348");

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("Test Device").build();
        mAdView.loadAd(adRequest);


        //AD MOB ADDED*/

        curRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("latitude") == false || dataSnapshot.hasChild("longtitude") == false){
                    Log.e("wonseok", "there is no childs");
                    return;
                }
                else {
                    lat = dataSnapshot.child("latitude").getValue(double.class);
                    lon = dataSnapshot.child("longitude").getValue(double.class);
                    Log.d("wonseok", "Lat : " + lat + " , Lon : " + lon);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();

                Fragment selected = null;
                if (position == 0) {
                    selected = fragment1;
                } else if (position == 1) {
                    selected = fragment2;
                } else if (position == 2) {
                    selected = fragment3;
                }

                getSupportFragmentManager().beginTransaction().replace(container, selected).commit();
                if (selected == fragment1) {
                }
                if (selected == fragment2) {

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void initilizeMap() {

        //setContentView(R.layout.fragment1);
        //SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
        //        .findFragmentById(R.id.map);
        //mapFragment.getMapAsync(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initilizeMap();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        //LatLng seoul = new LatLng(37.550947, 126.989296);
        //googleMap.addMarker(new MarkerOptions().position(seoul).title("서울타워"));
        //googleMap.moveCamera(CameraUpdateFactory.newLatLng(seoul));

        //RocketMarker = new MarkerOptions();
        //RocketMarker.position(new LatLng(lat, lon))
       //         .title("Rocket")
        //        .snippet("Rocket GPS");

       // googleMap.addMarker(RocketMarker).showInfoWindow();
        //LatLng RocketLatLng = new LatLng(lat, lon);
        //googleMap.moveCamera(CameraUpdateFactory.newLatLng(RocketLatLng));

       // CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
       // googleMap.animateCamera(zoom);

        //MarkerOptions marker = new MarkerOptions();
        //marker.position(new LatLng(37.555744, 126.970431))
        //        .title("서울역")
         //       .snippet("seoul Station");
        //googleMap.addMarker(marker).showInfoWindow();

        //자신의 위치
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMap.setMyLocationEnabled(true);

        //이벤트 처리기


        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(getApplicationContext(), marker.getTitle() + "를 클릭했습니다.", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

}
