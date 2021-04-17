package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


import com.android.volley.toolbox.NetworkImageView;
import com.example.myapp.application.HomeApplication;
import com.example.myapp.constants.Urls;
import com.example.myapp.network.ImageRequester;
import com.example.myapp.network.account.AccountService;
import com.example.myapp.network.account.dto.LoginDto;
import com.example.myapp.network.account.dto.LoginResultDto;
import com.example.myapp.security.JwtSecurityService;
import com.example.myapp.utils.CommonUtils;
import com.github.orangegangsters.lollipin.lib.managers.AppLock;
import com.github.orangegangsters.lollipin.lib.managers.LockManager;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private final int REQUEST_FIRST_RUN_PIN = 234;


    private ImageRequester imageRequester;
    private NetworkImageView my_logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LockManager<CustomPinActivity> lockManager = LockManager.getInstance();
        if (!HomeApplication.getInstance().getToken().isEmpty()) {
            Intent i = new Intent(this, ProfileActivity.class);
            startActivity(i);
        }
        String url = Urls.BASE + "/images/1.jpg";//        "https://cdn.mindful.org/What-I-Learned-From-My-Dog-Pandemic.jpg?q=80&fm=jpg&fit=crop&w=1400&h=875";

        imageRequester = ImageRequester.getInstance();
        my_logo = findViewById(R.id.my_logo_image);
        imageRequester.setImageFromUrl(my_logo, url);
    }

    public void OnClickLogin(View view) {
        final TextView txError = findViewById(R.id.txError);
        final TextInputEditText email = findViewById(R.id.textInputEmail);
        final TextInputLayout emailLayout = findViewById(R.id.textFieldEmail);

        final TextInputEditText password = findViewById(R.id.textInputPassword);
        final TextInputLayout passwordLayout = findViewById(R.id.textFieldPassword);

        LoginDto dto = new LoginDto(email.getText().toString(),
                password.getText().toString());

        CommonUtils.showLoading(this);

        AccountService.getInstance()
                .getJSONApi()
                .login(dto)
                .enqueue(new Callback<LoginResultDto>() {
                    @Override
                    public void onResponse(Call<LoginResultDto> call, Response<LoginResultDto> response) {
                        CommonUtils.hideLoading();
                        emailLayout.setError("");
                        passwordLayout.setError("");
                        txError.setText("");
                        if (response.isSuccessful()) {
                            LoginResultDto result = response.body();
                            JwtSecurityService jwtService = HomeApplication.getInstance();
                            jwtService.saveJwtToken(result.getToken());
                            Intent intent = new Intent(HomeApplication.getAppContext(),
                                    ProfileActivity.class);
                            startActivity(intent);
                        } else {
                            try {
                                int code = response.raw().code();
                                if (code == 401) {

                                    txError.setText("Помилка вводу даних");
                                    return;
                                }
                                String json = response.errorBody().string();
                                if (!json.isEmpty()) {
                                    JSONObject jsonObject = new JSONObject(json);

                                    String aJsonString = jsonObject.getString("errors");
                                    JSONObject jsonErrors = new JSONObject(aJsonString);

                                    if (jsonErrors.has("Email")) {
                                        String emailErrors = "";
                                        JSONArray emailArray = jsonErrors.getJSONArray("Email");
                                        for (int i = 0; i < emailArray.length(); i++) {
                                            emailErrors += emailArray.getString(i) + "\n";
                                        }
                                        emailLayout.setError(emailErrors);
                                    }

                                    if (jsonErrors.has("Password")) {
                                        String passwordErrors = "";
                                        JSONArray passwordArray = jsonErrors.getJSONArray("Password");
                                        for (int i = 0; i < passwordArray.length(); i++) {
                                            passwordErrors += passwordArray.getString(i) + "\n";
                                        }
                                        passwordLayout.setError(passwordErrors);
                                    }
                                }

                            } catch (Exception ex) {
                                CommonUtils.hideLoading();
                                Log.e("errorRead", "Error read json");
                            }

                        }
                        //Log.d("request", "IS GOOD");
                    }

                    @Override
                    public void onFailure(Call<LoginResultDto> call, Throwable t) {
                        Log.d("request", "IS Problem");
                    }
                });
    }

    public void OnClickGoToRegister(View view) {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }
}