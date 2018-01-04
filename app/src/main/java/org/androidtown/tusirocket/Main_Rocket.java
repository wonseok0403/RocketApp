package org.androidtown.tusirocket;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import static org.androidtown.tusirocket.R.id.container;

public class Main_Rocket extends AppCompatActivity{
    ActionBar abar;
    Fragment1 fragment1;
    Fragment2 fragment2;
    Fragment3 fragment3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__rocket);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            //ActivityCompat.requestPermissions(getActivity(),new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},1001);
            Log.e("wonseok", "GPS REQUEST!");
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},1001);
        }*/
        fragment2 = new Fragment2();
        fragment3 = new Fragment3();
        fragment1 = new Fragment1();

        getSupportFragmentManager().beginTransaction().add(container, fragment1).commit();

        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText("일반모드"));
        tabs.addTab(tabs.newTab().setText("발사모드"));
        tabs.addTab(tabs.newTab().setText("데이터뷰"));

        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();

                Fragment selected = null;
                if(position == 0){
                    selected = fragment1;
                }
                else if(position == 1){
                    selected = fragment2;
                }
                else if(position == 2){
                    selected = fragment3;
                }

                getSupportFragmentManager().beginTransaction().replace(container, selected).commit();
                if(selected == fragment1){
                }
                if(selected == fragment2){

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
    /*
    private void initilizeMap(){

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

        LatLng seoul = new LatLng(37.550947, 126.989296);
        googleMap.addMarker(new MarkerOptions().position(seoul).title("서울타워"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(seoul));

        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
        googleMap.animateCamera(zoom);

        MarkerOptions marker = new MarkerOptions();
        marker.position(new LatLng(37.555744, 126.970431))
                .title("서울역")
                .snippet("seoul Station");
        googleMap.addMarker(marker).showInfoWindow();

        //자신의 위치
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            return;
        }
        googleMap.setMyLocationEnabled(true);

        //이벤트 처리기
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener(){
            @Override
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(getApplicationContext(), marker.getTitle() + "를 클릭했습니다.", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }*/


}
