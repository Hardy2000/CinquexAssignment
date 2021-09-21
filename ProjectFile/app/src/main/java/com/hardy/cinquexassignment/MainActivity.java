package com.hardy.cinquexassignment;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.input.InputManager;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText eMail, passWord;
    private Button btnLogin;
    private ProgressBar progressBar;
    private TextView signUp, forgotPass;
    RelativeLayout fRelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }

        eMail = findViewById(R.id.emailInputText);
        passWord = findViewById(R.id.passInputText);
        btnLogin = findViewById(R.id.signUpButton);
        progressBar = findViewById(R.id.progressBar);
        signUp = findViewById(R.id.signupText);
        forgotPass = findViewById(R.id.forgotPassword);
        fRelativeLayout = findViewById(R.id.fButton);
        fRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, FacebookLoginActivity.class));
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String name = eMail.getText().toString().trim();
                String password = passWord.getText().toString().trim();
                if (name.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter email id", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter password", Toast.LENGTH_SHORT).show();
                    return;
                }
                hideKeyBoard(view);
                postData(name, password);

            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SignupActivity.class));
            }
        });

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ForgotPassword.class));
            }
        });
    }

    private void hideKeyBoard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
    }

    private void postData(String email, String password) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        progressBar.setVisibility(View.VISIBLE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.cinquex.com/api/internshala/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<JsonObject> call = apiInterface.register(email, password);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                progressBar.setVisibility(View.INVISIBLE);

                if (response.isSuccessful() && response.body() != null) {
                    JsonObject object = response.body();
                    try {
                        JSONObject object1 = new JSONObject(String.valueOf(object));
                        String msg = object1.getString("message");
                        builder.setTitle("Success");
                        builder.setMessage(msg);
                        eMail.setText("");
                        passWord.setText("");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    builder.setTitle("Error Found !");
                    if (response.code() == 400) {
                        builder.setMessage("Validation Error");
                    } else if (response.code() == 403) {
                        builder.setMessage("Wrong Credentials");
                    }

                }

                builder.setCancelable(true);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage().toString(), Toast.LENGTH_LONG).show();

            }
        });
    }
}