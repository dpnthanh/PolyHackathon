package com.thanh.android.polyhackathon;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    EditText edtUserName, edtPass;
    Button btnLogin, btnRegister;
    TextView txtForgotPass;
    ProgressBar progressBarLoadding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initControls();
        initDisplay();
        initEvents();
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

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_login_loginActivity:
                break;
            case R.id.button_register_loginActivity:
                break;
            case R.id.textView_forgotPassword_loginActivity:
                break;
        }
    }
}
