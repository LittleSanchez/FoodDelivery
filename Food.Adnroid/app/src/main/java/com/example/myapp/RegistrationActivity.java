package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.toolbox.NetworkImageView;
import com.example.myapp.application.HomeApplication;
import com.example.myapp.constants.Urls;
import com.example.myapp.network.ImageRequester;
import com.example.myapp.network.account.AccountService;
import com.example.myapp.network.account.dto.LoginDto;
import com.example.myapp.network.account.dto.LoginResultDto;
import com.example.myapp.network.account.dto.RegistrationDTO;
import com.example.myapp.network.account.dto.RegistrationResultDTO;
import com.example.myapp.security.JwtSecurityService;
import com.example.myapp.utils.CommonUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity {

    private ImageRequester imageRequester;
    private NetworkImageView my_logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        String url = Urls.BASE + "/images/1.jpg";

        imageRequester = ImageRequester.getInstance();
        my_logo = findViewById(R.id.my_logo_image);
        imageRequester.setImageFromUrl(my_logo, url);
    }

    public void OnClickRegister(View view) {
        final TextInputEditText displayName = findViewById(R.id.textInputDisplayName);
        final TextInputLayout displayNameLayout = findViewById(R.id.textFieldDisplayName);

        final TextInputEditText email = findViewById(R.id.textInputEmailRegister);
        final TextInputLayout emailLayout = findViewById(R.id.textFieldEmailRegister);

        final TextInputEditText password = findViewById(R.id.textInputPasswordRegister);
        final TextInputLayout passwordLayout = findViewById(R.id.textFieldPasswordRegister);

        RegistrationDTO dto = new RegistrationDTO(displayName.getText().toString(),
                email.getText().toString(), email.getText().toString(),
                password.getText().toString());

        CommonUtils.showLoading(this);

        AccountService.getInstance()
                .getJSONApi()
                .register(dto)
                .enqueue(new Callback<RegistrationResultDTO>() {
                    @Override
                    public void onResponse(Call<RegistrationResultDTO> call, Response<RegistrationResultDTO> response) {
                        CommonUtils.hideLoading();
                        displayNameLayout.setError("");
                        emailLayout.setError("");
                        passwordLayout.setError("");
                        if(response.isSuccessful()) {
                            Log.d("request", "IS GOOD!");
                            RegistrationResultDTO result = response.body();
                            JwtSecurityService jwtService = HomeApplication.getInstance();
                            jwtService.saveJwtToken(result.getToken());
                            Intent intent = new Intent(HomeApplication.getAppContext(),
                                    ProfileActivity.class);
                            startActivity(intent);
                        } else {
                            try {
                                String json = response.errorBody().string();
                                if(!json.isEmpty()) {
                                    JSONObject jsonObject = new JSONObject(json);

                                    String aJsonString = jsonObject.getString("errors");
                                    JSONObject jsonErrors = new JSONObject(aJsonString);

                                    if (jsonErrors.has("DisplayName")) {
                                        String displayNameErrors = "";
                                        JSONArray displayNameArray = jsonErrors.getJSONArray("DisplayName");
                                        for (int i=0; i < displayNameArray.length(); i++) {
                                            displayNameErrors += displayNameArray.getString(i) + "\n";
                                        }
                                        displayNameLayout.setError(displayNameErrors);
                                    }

                                    if (jsonErrors.has("Email")) {
                                        String emailErrors = "";
                                        JSONArray emailArray = jsonErrors.getJSONArray("Email");
                                        for (int i=0; i < emailArray.length(); i++) {
                                           emailErrors += emailArray.getString(i) + "\n";
                                        }
                                        emailLayout.setError(emailErrors);
                                    }

                                    if (jsonErrors.has("Password")) {
                                        String passwordErrors = "";
                                        JSONArray passwordArray = jsonErrors.getJSONArray("Password");
                                        for (int i=0; i < passwordArray.length(); i++) {
                                            passwordErrors += passwordArray.getString(i) + "\n";
                                        }
                                        passwordLayout.setError(passwordErrors);
                                    }
                                }
                            } catch (Exception ex) {
                                CommonUtils.hideLoading();
                                Log.d("request", "Something went wrong!");
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<RegistrationResultDTO> call, Throwable t) {
                        Log.d("request", "IS Problem");
                    }
                });
    }
}