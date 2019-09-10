package inggitsemut.adminapps2.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import inggitsemut.adminapps2.R;
import inggitsemut.adminapps2.api.ConfigUtils;
import inggitsemut.adminapps2.api.Service;
import inggitsemut.adminapps2.model.Result;
import inggitsemut.adminapps2.storage.SharedPrefManager;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static inggitsemut.adminapps2.api.ConfigUtils.BASE_URL;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etEmail, etPassword;
    private boolean doubleBackToExitPressedOnce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        etEmail.setText(getIntent().getStringExtra("message"));


        findViewById(R.id.btn_sign_in).setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        //if user is already logged in
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    private void userSignIn(){
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty()) {
            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Enter a valid email");
            etEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            etPassword.setError("Password required");
            etPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            etPassword.setError("Password should be at least 6 character");
            etPassword.requestFocus();
            return;
        }
//        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
//        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
//        OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(logging).build();

        //Defining retrofit api service
        Service service = ConfigUtils.retrofit.create(Service.class);

        //defining the call
        Call<Result> call = service.loginAdmin(email, password);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Signing In...");
        progressDialog.show();

        //calling the api
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(@NonNull Call<Result> call, @NonNull Response<Result> response) {
                progressDialog.dismiss();
//                Log.i("mki", "errorrnih: "+ response.body().toString());
                if (response.body().getStatuscode() == 200) {
                    finish();
                    SharedPrefManager.getInstance(getApplicationContext()).loginAdmin(response.body().getData());
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "Login Successfully!", Toast.LENGTH_LONG).show();

                } else{
                    Toast.makeText(getApplicationContext(), "Invalid Email or Password", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Result> call,Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_sign_in:
                userSignIn();
                break;
        }
    }

    //double click to exit
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            finishAffinity();
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 3000);
    }
    //end exit

}
