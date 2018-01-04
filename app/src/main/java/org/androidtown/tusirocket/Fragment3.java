package org.androidtown.tusirocket;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Date;
import java.text.SimpleDateFormat;

import static java.sql.DriverManager.println;

/**
 * Created by wonseok on 17. 7. 27.
 */

public class Fragment3 extends Fragment {
    Button button_check;
    TextView dbView;
    TextView curView;
    FirebaseDatabase database;
    DatabaseReference myRef;
    DatabaseReference curRef;
    String save_dbView;
    String save_curView;

    @Override
    public void onDestroy() {
        save_curView = curView.getText().toString();
        save_dbView = dbView.getText().toString();
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment3, container, false);
        button_check = (Button) rootView.findViewById(R.id.button_check);
        dbView = (TextView) rootView.findViewById(R.id.dbView);
        curView = (TextView) rootView.findViewById(R.id.curView);

        if(save_dbView != null) dbView.setText(save_dbView);
        if(save_curView != null) curView.setText(save_curView);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        curRef = database.getReference("CurrentLocation");
        dbView.setMovementMethod(new ScrollingMovementMethod());

        button_check.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
            }
        });

        curRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("wonseok", "appended!");
                Location location;
                Log.d("wonseok", "Made!");
                double lat, lon, alt;
                float acur;
                long tim;
                String day;
                if(dataSnapshot.hasChild("latitude") == false){ return; }
                lat = dataSnapshot.child("latitude").getValue(double.class);
                lon = dataSnapshot.child("longitude").getValue(double.class);
                acur = dataSnapshot.child("accuracy").getValue(float.class);
                tim = dataSnapshot.child("time").getValue(long.class);
                day = new SimpleDateFormat("yyyy/MM/dd").format(new Date(tim));
                alt = dataSnapshot.child("altitude").getValue(double.class);
                dbView.append(day + ": " + lat + " " + lon + " " + alt + "\n");
                Log.d("wonseok", "Lat : " + lat);
                curView.setText("Latitude : " + lat + "\n" +
                                "Longitude : " + lon + "\n" +
                                "Accuracy : " + acur + "\n" +
                                "Time : " + day + "\n" +
                                "Altitude : " + alt + "\n");

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                println("Connection Failed");
            }
        });
        return rootView;
    }
}
