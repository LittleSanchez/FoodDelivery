package com.example.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.toolbox.NetworkImageView;
import com.example.myapp.constants.Urls;
import com.example.myapp.network.ImageRequester;
import com.example.myapp.network.profile.ApiWebService;
import com.example.myapp.network.profile.dto.ProfileResultDTO;
import com.example.myapp.utils.CommonUtils;
import com.github.orangegangsters.lollipin.lib.PinActivity;
import com.github.orangegangsters.lollipin.lib.managers.AppLock;
import com.github.orangegangsters.lollipin.lib.managers.LockManager;
import com.google.android.material.textfield.TextInputEditText;

import java.util.concurrent.locks.Lock;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfileActivity extends PinActivity {


    private ImageRequester imageRequester;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        LockManager<CustomPinActivity> lockManager = LockManager.getInstance();
        if (!lockManager.getAppLock().isPasscodeSet()) {
            Intent intent = new Intent(this, CustomPinActivity.class);
            intent.putExtra(AppLock.EXTRA_TYPE, AppLock.ENABLE_PINLOCK);
            startActivityForResult(intent, 0);
        }



        final TextView email = findViewById(R.id.email);
        final TextView displayName = findViewById(R.id.displayName);
        final NetworkImageView photo = findViewById(R.id.profile_image);
        imageRequester = ImageRequester.getInstance();

        CommonUtils.showLoading(this);

        ApiWebService.getInstance()
                .getJSONApi()
                .profile()
                .enqueue(new Callback<ProfileResultDTO>() {
                    @Override
                    public void onResponse(Call<ProfileResultDTO> call, Response<ProfileResultDTO> response) {
//                        Log.d("super","Ok result good");
                        CommonUtils.hideLoading();
                        if(response.isSuccessful())
                        {
                            ProfileResultDTO result = response.body();
                            String image = result.getImage();
                            String url = Urls.BASE + "/images/" + image;

                            Log.d("[GOT PROFILE]: ", "UserName: " + result.getUserName());

                            ApiWebService.getInstance().setProfile(result);

                            email.setText(result.getUserName());
                            displayName.setText(result.getDisplayName());
                            imageRequester.setImageFromUrl(photo, url);
                        }
                        else
                        {


                        }
                    }

                    @Override
                    public void onFailure(Call<ProfileResultDTO> call, Throwable t) {
                        Log.e("problem","problem API"+ t.getMessage());
                        CommonUtils.hideLoading();
                    }
                });
    }
    public void SelectImage(View view) {
        Intent intent = new Intent(this, ChangeImageActivity.class);
        startActivity(intent);
    }
}