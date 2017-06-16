package com.example.marcu.movieapplication.presentation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.marcu.movieapplication.R;
import com.example.marcu.movieapplication.dataaccess.RegisterPostTask;

/**
 * Created by Wallaard on 16-6-2017.
 */

public class RegisterActivity extends AppCompatActivity implements RegisterPostTask.PutSuccessListener {
    private EditText editTextUser;
    private EditText editTextPassword;
    private String username,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final Button register = (Button) findViewById(R.id.registerButton);

        register.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                editTextUser = (EditText) findViewById(R.id.loginUsername);
                editTextPassword = (EditText) findViewById(R.id.loginPassword);
                username = editTextUser.getText().toString();
                password = editTextPassword.getText().toString();
                if(username.matches("")||password.matches("")){
                    Toast.makeText(RegisterActivity.this, "Vul alles in!", Toast.LENGTH_SHORT).show();
                } else {
                    register("https://programmeren-opdracht.herokuapp.com/api/v1/register",username,password);
                }
            }
        });


    }

    private void register(String url, String username, String password) {
        String[] urls = new String[]{url,username,password};
        RegisterPostTask task = new RegisterPostTask(this);
        task.execute(urls);
    }

    @Override
    public void putSuccessful(Boolean successful) {
        if(successful){
            Toast.makeText(this, "Account aangemaakt!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Registratie mislukt!", Toast.LENGTH_LONG).show();
        }
    }
}

