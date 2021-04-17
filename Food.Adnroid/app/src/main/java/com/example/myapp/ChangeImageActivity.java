package com.example.myapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapp.R;
import com.example.myapp.application.HomeApplication;
import com.example.myapp.network.image.ChangeImage;
import com.example.myapp.network.profile.ApiWebService;
import com.example.myapp.network.profile.dto.ProfileResultDTO;
import com.example.myapp.utils.CommonUtils;
import com.oginotihiro.cropview.CropView;

import java.io.ByteArrayOutputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeImageActivity extends AppCompatActivity {
    private static int RESULT_LOAD_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_image);
        Intent i = new Intent(
                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            CommonUtils.showLoading(this);
            Uri selectedImage = data.getData();
            CropView cropView = (CropView) findViewById(R.id.cropView);
            cropView.of(selectedImage)
                    //.withAspect(x, y)
                    .withOutputSize(100, 100)
                    .initialize(this);

            CommonUtils.hideLoading();
        }

    }

    public void RotateRightImage(View view) {
        CropView cropView = (CropView) findViewById(R.id.cropView);
        cropView.setRotation(cropView.getRotation()+90);
    }

    public void RotateLeftImage(View view) {
        CropView cropView = (CropView) findViewById(R.id.cropView);
        cropView.setRotation(cropView.getRotation()-90);
    }

    public void ChangeImage(View view) {
        CommonUtils.showLoading(this);
        CropView cropView = (CropView) findViewById(R.id.cropView);
        Bitmap croppedBitmap = cropView.getOutput();
        Matrix matrix = new Matrix();

        matrix.postRotate(cropView.getRotation());
        Bitmap rotatedBitmap = Bitmap.createBitmap(croppedBitmap, 0, 0, croppedBitmap.getWidth(), croppedBitmap.getHeight(), matrix, true);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        rotatedBitmap.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

        ChangeImage m = new ChangeImage();
        m.setImage(encoded);
        m.setUserName(ApiWebService.getInstance().getProfile().getUserName());

        final ChangeImageActivity context =this;
        ApiWebService.getInstance()
                .getJSONApi()
                .changeImage(m)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.errorBody() == null){
                            CommonUtils.hideLoading();
                            Intent intent = new Intent(context, ProfileActivity.class);
                            startActivity(intent);
                        }
                        CommonUtils.hideLoading();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });
    }
}