package com.thanh.android.polyhackathon;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.io.ByteArrayOutputStream;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    private static final String TAG = "login";
    EditText edtUserName, edtPass;
    Button btnLogin, btnRegister;
    TextView txtForgotPass;
    ProgressBar progressBarLoadding;
    private static final int RC_SIGN_IN = 9001;

    private GoogleApiClient mGoogleApiClient;
    private TextView mStatusTextView;
    private TextView mStatusIdTextView;
    private ImageView imageViewAvatar;
    private ProgressDialog mProgressDialog;
    private String url = "http://10.200.202.125//serverlocal/post_data_sign_up.php";
    private FirebaseAuth mAuth;
    boolean signout = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initControls();
        initDisplay();
        initEvents();
        initGoogle();
        checkUser();
    }

    private void checkUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
//            Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            String uid = user.getUid();
            if (emailVerified){
                updateUI(user);
            }
        }

    }


    private void initEvents() {
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        txtForgotPass.setOnClickListener(this);
    }

    private void initDisplay() {
        progressBarLoadding.setVisibility(View.GONE);
    }

    private void initControls() {
        //mapped
        edtUserName = (EditText) findViewById(R.id.editText_username_loginActivity);
        edtPass = (EditText) findViewById(R.id.editText_password_loginActivity);
        btnLogin = (Button) findViewById(R.id.button_login_loginActivity);
        btnRegister = (Button) findViewById(R.id.button_register_loginActivity);
        txtForgotPass = (TextView) findViewById(R.id.textView_forgotPassword_loginActivity);
        progressBarLoadding = (ProgressBar) findViewById(R.id.progressBar_loading_loginActivity);
        mAuth = FirebaseAuth.getInstance();


    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser.isEmailVerified()){

        }else{
            return;
        }

    }


    @Override
    public void onClick(View view) {
        if (signout){
            signOut();
        }
        signout = false;
        switch (view.getId()) {

            case R.id.button_login_loginActivity:
                loginWithEmailAccount();

                break;
            case R.id.button_register_loginActivity:
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.textView_forgotPassword_loginActivity:
                break;
            case R.id.sign_in_google_button:
                signIn();
                break;
        }
    }

    private void initGoogle() {

        // Views


        // Button listeners
        findViewById(R.id.sign_in_google_button).setOnClickListener(this);


        // [START configure_signin]
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // [END configure_signin]

        // [START build_client]
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        // [END build_client]

        // [START customize_button]
        // Set the dimensions of the sign-in button.
        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_google_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        // [END customize_button]

    }
    // [END onActivityResult]

    // [START handleSignInResult]
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (signout) return;
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            Toast.makeText(getApplicationContext(),"User ID"+acct.getId(),Toast.LENGTH_LONG).show();

        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }

    }


    // [END handleSignInResult]

    // [START signIn]
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signIn]

    // [START signOut]
    public void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }

    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END revokeAccess]

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("loading");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    private void updateUI(boolean signedIn) {
        if (signedIn) {
            findViewById(R.id.sign_in_google_button).setVisibility(View.GONE);

        } else {


            findViewById(R.id.sign_in_google_button).setVisibility(View.VISIBLE);

        }
    }


    private String decodeImage(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte[] byteImage = outputStream.toByteArray();
        String encodeImage = Base64.encodeToString(byteImage, Base64.DEFAULT);
        return encodeImage;
    }

    @Override
    public void onStart() {
        super.onStart();
        Intent intent = getIntent();
        signout = intent.getBooleanExtra("signout", false);
        edtUserName.setText(intent.getStringExtra("username"));
        edtPass.setText(intent.getStringExtra("pass"));

//        Toast.makeText(this, signout + "", Toast.LENGTH_SHORT).show();
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    // [START onActivityResult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void loginWithEmailAccount() {
        Log.d(TAG, "Createa Account");
        progressBarLoadding.setVisibility(View.VISIBLE);
        String usename = edtUserName.getText().toString().trim();
        String pass = edtPass.getText().toString().trim();
        boolean allowLogin = true;
        if (usename.isEmpty()){
            edtUserName.setError(getResources().getString(R.string.error_null_username));
            allowLogin = false;
        }
        if (pass.isEmpty()){
            edtPass.setError(getResources().getString(R.string.error_null_password));allowLogin = false;
            allowLogin = false;
        }
        if (allowLogin){
            mAuth.signInWithEmailAndPassword(usename, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                }
            });
        }
    }
}
