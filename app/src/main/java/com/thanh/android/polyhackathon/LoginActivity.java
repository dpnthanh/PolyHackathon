package com.thanh.android.polyhackathon;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "login";
    EditText edtUserName, edtPass;
    Button btnLogin, btnRegister;
    TextView txtForgotPass;
    ProgressBar progressBarLoadding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initControls();
        initDisplay();
        initEvents();
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

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser.isEmailVerified()){

        }else{
            return;
        }

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_login_loginActivity:
                loginWithEmailAccount();
                break;
            case R.id.button_register_loginActivity:
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.textView_forgotPassword_loginActivity:
                break;
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
