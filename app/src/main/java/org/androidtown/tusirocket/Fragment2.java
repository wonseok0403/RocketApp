package org.androidtown.tusirocket;

import android.content.Intent;
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

/**
 * Created by wonseok on 17. 7. 27.
 *
        button_connectDB
        button_eraseDB
        button_GPSSending */
public class Fragment2 extends Fragment {
    //ScrollView scrollView;
    Button button_connectDB;
    Button button_eraseDB;
    Boolean GPS_ON = false;
    Button button_GPSSending;
    String save_textView;
    TextView textView;
    FirebaseDatabase database;
    DatabaseReference myRef;
    DatabaseReference erRef;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment2, container, false);
        Log.d("wonseok", "Fragment2.onCreatedView(), GPS : " + GPS_ON);

        button_connectDB = (Button) rootView.findViewById(R.id.button_connectDB);
        button_eraseDB = (Button) rootView.findViewById(R.id.button_eraseDB);
        button_GPSSending = (Button) rootView.findViewById(R.id.button_GPSSending);
        textView = (TextView) rootView.findViewById(R.id.textView_log);
        textView.setMovementMethod(new ScrollingMovementMethod());

        if(GPS_ON == true){
            button_GPSSending.setText("3. TURN GPS OFF");
        }
        if(save_textView != null){
            textView.setText(save_textView);
        }

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Connection");
        erRef = database.getReference();
        button_connectDB.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                println("Connecting Starts....");
                if(myRef != null){
                    println("OKAY!");
                }
               // myRef.child("Connection").setValue("Succeed!").toString();
                myRef.setValue("CONNECTED");
                //println("connected!");
            }
        });
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);

                println("Data changed : " );
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                println("Connection Failed");
            }
        });
        button_eraseDB.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                erRef.removeValue();
                println("Erased!");
            }
        });
        button_GPSSending.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                println("GPS Sent!");
                if(GPS_ON == false) {
                    button_GPSSending.setText("3. TURN GPS OFF");
                    Intent intent = new Intent(getContext(), GpsSending.class);
                    getContext().startService(intent);
                    GPS_ON = true;
                }
                else{
                    button_GPSSending.setText("3. GPS SENDING");
                    Intent intent = new Intent(getContext(), GpsSending.class);
                    getContext().stopService(intent);
                    GPS_ON = false;
                }
            }
        });

        return rootView;
    }
    public void println(String data){
        textView.append(data + "\n");
        //scrollView.scrollTo(0,textView.getHeight());
        //scrollView.setScrollBarSize(textView.getScrollBarSize());
    }

    @Override
    public void onDestroy() {
        save_textView = textView.getText().toString();
        super.onDestroy();
    }
}
