package org.androidtown.tusirocket;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    public static final int REQUEST_CODE_ROCKET_MENU = 101;
    public static final int REQUEST_CODE_MEMBER_MENU = 102;
    private static final int RC_SIGN_IN = 10;
    int SelectedMenu = 1;
    String ROCKET_USER = "wonseok786@gmail.com";
    boolean isUser = false;
    SignInButton button;
    Button connect_button;
    Button sign_out_button;

    ProgressBar pbLogin;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("wonseok", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            Toast.makeText(getApplicationContext(),acct.getDisplayName().toString(),Toast.LENGTH_LONG).show();
        } else {
            // Signed out, show unauthenticated UI.
        }
    } // added aug 1
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    } // added aug 1
    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        // [END_EXCLUDE]
                    }
                });
    }//added aug 1
    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        // [END_EXCLUDE]
                    }
                });
    }//aug 1 added

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        pbLogin = (ProgressBar) findViewById(R.id.pbLogin);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestScopes(new Scope(Scopes.PLUS_LOGIN))   // added Jul 29 01:00
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addApi(Plus.API)
                .build();

        button = (SignInButton) findViewById(R.id.loginButton);
        connect_button = (Button) findViewById(R.id.button_connect);
        sign_out_button = (Button) findViewById(R.id.sign_out_button);
        connect_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                switch (SelectedMenu) {
                    case 1:  // rocket
                        Intent intent = new Intent(getApplicationContext(), Main_Rocket.class);
                        if (isUser == true) {
                            startActivityForResult(intent, REQUEST_CODE_ROCKET_MENU);
                        } else {
                            //startActivityForResult(signInIntent, RC_SIGN_IN);
                            //if(isUser == true) { startActivityForResult(intent, REQUEST_CODE_ROCKET_MENU); }
                            Toast.makeText(MainActivity.this, "인가되지 않았습니다.", Toast.LENGTH_LONG).show();
                        }
                        break;
                    case 2:  // graduated
                        Toast.makeText(MainActivity.this, "구현되지 않았습니다.", Toast.LENGTH_LONG).show();
                        break;
                    case 3:  // now
                        Intent intent2 = new Intent(getApplicationContext(), Main_Member.class);
                        // JUL 29 구현 시작
                        // Toast.makeText(MainActivity.this, "구현되지 않았습니다.", Toast.LENGTH_LONG).show();
                        startActivityForResult(intent2, REQUEST_CODE_MEMBER_MENU);
                        break;
                }
            }
        });
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                pbLogin.setVisibility(View.VISIBLE);

                /*
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);*/

                Toast.makeText(MainActivity.this, "FireBase아이디 생성이 완료 되었습니다.", Toast.LENGTH_LONG).show();


                //AUG 1, 16:24
                OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
                if (opr.isDone()) {
                    // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
                    // and the GoogleSignInResult will be available instantly.
                    Log.d("wonseok", "Got cached sign-in");
                    GoogleSignInResult result = opr.get();
                    handleSignInResult(result);
                } else {
                    // If the user has not previously signed in on this device or the sign-in has expired,
                    // this asynchronous branch will attempt to sign in the user silently.  Cross-device
                    // single sign-on will occur in this branch.
                    opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                        @Override
                        public void onResult(GoogleSignInResult googleSignInResult) {
                            Log.d("wonseok", "sign in result");
                            GoogleSignInResult result;
                            result = googleSignInResult;
                            GoogleSignInAccount account = result.getSignInAccount();

                            if (account != null) {
                                Log.d("wonseok", "mail : " + account.getEmail().toString());
                            }
                            else{
                                Log.d("wonseok", "account is null");
                            }
                        }
                    });
                }
                signIn();
                // ADDED AUG 1 16:24
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        pbLogin.setVisibility(View.INVISIBLE);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Log.d("wonseok", "request code is RC_SIGN_IN");
            handleSignInResult(result);
            // Google Sign In was successful, authenticate with Firebase
            GoogleSignInAccount account = result.getSignInAccount();
            if( account != null) {
                String k = account.getEmail();
                Log.d("wonseok", "account.getEmail is " + k);

                if (k.equals(ROCKET_USER)) {
                    Log.d("wonseok", "k = " + ROCKET_USER);
                    Toast.makeText(getApplicationContext(), "k = " + k, Toast.LENGTH_LONG).show();
                    isUser = true;
                }

                firebaseAuthWithGoogle(account);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "계정 생성 실패!.", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(MainActivity.this, "FireBase아이디 생성이 완료 되었습니다.", Toast.LENGTH_LONG).show();
                            switch (SelectedMenu) {
                                case 1:  // rocket
                                    Intent intent = new Intent(getApplicationContext(), Main_Rocket.class);
                                    if (isUser) {
                                        startActivityForResult(intent, REQUEST_CODE_ROCKET_MENU);
                                    } else {
                                        Toast.makeText(MainActivity.this, "인가되지 않았습니다.", Toast.LENGTH_LONG).show();
                                    }
                                    break;
                                case 2:  // graduated
                                    Toast.makeText(MainActivity.this, "구현되지 않았습니다.", Toast.LENGTH_LONG).show();
                                    break;
                                case 3:  // now
                                    Intent intent2 = new Intent(getApplicationContext(), Main_Member.class);
                                    // JUL 29 구현 시작
                                    // Toast.makeText(MainActivity.this, "구현되지 않았습니다.", Toast.LENGTH_LONG).show();
                                    startActivityForResult(intent2, REQUEST_CODE_MEMBER_MENU);
                                    break;
                            }

                        }
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {
        Log.d("wonseok", "failed to connect");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //menu_Rocket
        //menu_Member
        //menu_Old
        TextView textView;
        textView = (TextView) findViewById(R.id.textView_signature);
        int curId = item.getItemId();
        switch (curId) {
            case R.id.menu_Member:
                SelectedMenu = 3;
                textView.setText("Now Members's APK");
                Toast.makeText(this, "회원단만 이용 가능합니다.", Toast.LENGTH_LONG).show();
                break;
            case R.id.menu_Old:
                SelectedMenu = 2;
                textView.setText("Graduated Members's APK");
                Toast.makeText(this, "현 회장에게 연락해주십시오.", Toast.LENGTH_LONG).show();
                break;
            case R.id.menu_Rocket:
                textView.setText("ROCKET'S APK");
                SelectedMenu = 1;
                Toast.makeText(this, "담당자만 이용 가능합니다.", Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
