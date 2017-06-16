package com.example.marcu.movieapplication.presentation.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.auth0.android.jwt.Claim;
import com.auth0.android.jwt.JWT;

import com.example.marcu.movieapplication.R;
import com.example.marcu.movieapplication.dataaccess.LoginActivityRequests;

public class LoginActivity extends AppCompatActivity implements LoginActivityRequests.LoginActivityListener {
    private final String tag = getClass().getSimpleName();

    public static final String JWT_STR = "jwt_str";
    public static final String USER = "sub";

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonSignIn;

    private ProgressDialog dialog;
    private TextView textViewRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = prefs.edit();

        editTextEmail = (EditText) findViewById(R.id.editTextEmailaddress);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonSignIn = (Button)findViewById(R.id.buttonSignIn);

        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidEmail(editTextEmail.getText().toString())) {
                    handleLogin(editTextEmail.getText().toString(), editTextPassword.getText().toString());
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid email address", Toast.LENGTH_SHORT).show();
                }
            }
        });

        textViewRegister = (TextView) findViewById(R.id.textViewNoAccountYet);
        textViewRegister.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void handleLogin(String email, String password) {
        dialog = new ProgressDialog(this);
        setupDialog(dialog);
        LoginActivityRequests request = new LoginActivityRequests(getApplicationContext(), this);
        request.handleLogin(email, password);
    }

    private void setupDialog(ProgressDialog dialog) {
        dialog.setMessage("Authenticating. Please wait..");
        dialog.show();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
    }

    public static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    @Override
    public void onLoginSuccessful(String response) {
        dialog.dismiss();
        Toast.makeText(getApplicationContext(), "Logged in", Toast.LENGTH_LONG).show();
        JWT jwt = new JWT(response);
        Claim customer = jwt.getClaim(USER);
        Intent intent = new Intent(getApplicationContext(), MovieOverview.class);

        Log.i(tag, "customer.asInt(): " + customer.asInt());
        editor.putString(JWT_STR, jwt.toString().replace("\"", ""));
        editor.putInt(USER, customer.asInt());
        editor.apply();

        startActivity(intent);
        finish();
    }

    @Override
    public void onError(String message) {
        dialog.dismiss();
        Log.e(tag, "onError " + message);
        Toast.makeText(getApplicationContext(), "Log in failed. Please try again", Toast.LENGTH_LONG).show();
    }
}
