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

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "register";
    EditText edtUserName, edtPass, edtCofPass;
    Button btnLogin, btnRegister;
    ProgressBar progressBarLoadding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        
        initControls();
        initDisplay();
        initEvents();
    }

    private void initEvents() {
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }

    private void initDisplay() {
        progressBarLoadding.setVisibility(View.GONE);
    }

    private void initControls() {
        //mapped
        edtUserName = (EditText) findViewById(R.id.editText_username_registerActivity);
        edtPass = (EditText) findViewById(R.id.editText_password_registerActivity);
        edtCofPass = (EditText) findViewById(R.id.editText_confirmPassword_registerActivity);
        btnLogin = (Button) findViewById(R.id.button_login_registerActivity);
        btnRegister = (Button) findViewById(R.id.button_register_registerActivity);
        progressBarLoadding = (ProgressBar) findViewById(R.id.progressBar_loading_registerActivity);
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

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_login_registerActivity:
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.button_register_registerActivity:
                createAccount();
                break;
        }
    }

    private void createAccount() {
        Log.d(TAG, "Createa Account");
        progressBarLoadding.setVisibility(View.VISIBLE);
        String usename = edtUserName.getText().toString().trim();
        String pass = edtPass.getText().toString().trim();
        String cofPass = edtCofPass.getText().toString().trim();
        boolean allowLogin = true;
        if (usename.isEmpty()){
            edtUserName.setError(getResources().getString(R.string.error_null_username));
            allowLogin = false;
        }
        if (pass.isEmpty()){
            edtPass.setError(getResources().getString(R.string.error_null_password));allowLogin = false;
            allowLogin = false;
        }
        if (cofPass.isEmpty()){
            edtCofPass.setError(getResources().getString(R.string.error_null_confirmPassword));
            allowLogin = false;
        }
        if (!pass.equals(cofPass)){
            edtCofPass.setError(getResources().getString(R.string.error_confirmPasswordNotTrue));
        }
        if (allowLogin){
            mAuth.createUserWithEmailAndPassword(usename, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                }
            });
        }
        progressBarLoadding.setVisibility(View.GONE);
    }
}

