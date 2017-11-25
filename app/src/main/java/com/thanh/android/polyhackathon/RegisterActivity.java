package com.thanh.android.polyhackathon;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    EditText edtUserName, edtPass, edtCofPass;
    Button btnLogin, btnRegister;
    ProgressBar progressBarLoadding;

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
        progressBarLoadding = (ProgressBar) findViewById(R.id.progressBar_loading_loginActivity);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_login_registerActivity:
                break;
            case R.id.button_register_registerActivity:
                break;
        }
    }
}

