package org.androidtown.tusirocket;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthProvider;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by wonseok on 17. 7. 27.
 */

public class member_Fragment3 extends Fragment implements FirebaseAuthProvider{
    Button button;
    TextView textView;
    EditText editText;
    FirebaseDatabase database;
    String save;
    int num_contents;
    int readed_content = 0;
    private FirebaseAuth mAuth;

    private boolean mIntentInProgress;
    public static final int RC_SIGN_IN = 0;
    /**
     * True if the sign-in button was clicked.  When true, we know to resolve all
     * issues preventing sign-in without waiting.
     */
    private boolean mSignInClicked;

    DatabaseReference myRef;
    DatabaseReference conRef;
    DatabaseReference numRef;

    public void println(String s){
        textView.append(s + "\n");
    }

    @Override
    public void onDestroy() {
        save = textView.getText().toString();
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.member_fragment3, container, false);
        textView = rootView.findViewById(R.id.textView_chattingbox);
        editText = rootView.findViewById(R.id.editText_chat);
        button = rootView.findViewById(R.id.button_send);

        textView.setMovementMethod(new ScrollingMovementMethod());
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        Log.d("wonseok", "Current user's Email address : " + currentUser.getEmail());
        Log.d("wonseok", "Current user's Display name : " + currentUser.getDisplayName());


        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        conRef = database.getReference("Contents");
        numRef = database.getReference("Contents");

        //load files
        if( save != null){
            textView.setText(save);
        }

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String tmp;
                tmp = editText.getText().toString();
                if(tmp == null) return;
                else{
                    numRef.child("Contents").child(String.valueOf(num_contents+1)).child("Content").setValue(tmp);
                    numRef.child("Contents").child(String.valueOf(num_contents+1)).child("Writter").setValue(currentUser.getEmail());

                    numRef.child("NumContents").setValue(num_contents+1);
                    editText.setText("");
                }
            }
        });
        numRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                num_contents = dataSnapshot.child("NumContents").getValue(int.class);
                Log.d("wonseok", "k = " + num_contents);
                if( num_contents == 0 ){
                    textView.append("채팅 목록이 없어요 >.,<\n");
                }
                else {
                    String tmp_user;
                    String tmp_contents;
                    int i;
                    if(num_contents >= 50 && readed_content != num_contents-1) readed_content = num_contents - 50;
                    for(i=readed_content + 1; i<=num_contents; i++){
                        tmp_contents = dataSnapshot.child("Contents").child(String.valueOf(i)).child("Content").getValue(String.class);
                        tmp_user = dataSnapshot.child("Contents").child(String.valueOf(i)).child("Writter").getValue(String.class);
                        println(tmp_user + ": ");
                        println(tmp_contents);
                    }
                }
                readed_content = num_contents;

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                println("Connection Failed");
            }
        });
        return rootView;
    }
}
