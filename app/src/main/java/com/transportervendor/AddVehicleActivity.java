package com.transportervendor;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.transportervendor.apis.VehicleService;
import com.transportervendor.beans.Vehicle;
import com.transportervendor.databinding.AddVehicleActivityBinding;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddVehicleActivity extends AppCompatActivity {
    AddVehicleActivityBinding binding;
    Uri imgUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=AddVehicleActivityBinding.inflate(LayoutInflater.from(AddVehicleActivity.this));
        setContentView(binding.getRoot());
        setSupportActionBar(binding.tbToolBar);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==222 && resultCode==RESULT_OK){
            imgUri=data.getData();
        }
//        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && data!=null ) {
//            CropImage.ActivityResult result = CropImage.getActivityResult(data);
//            if (resultCode == RESULT_OK) {
//                imgUri = result.getUri();
//                binding.ivVehicleImage.setImageURI(imgUri);
//
//            }
//        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        binding.ivVehicleImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ActivityCompat.checkSelfPermission(AddVehicleActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PermissionChecker.PERMISSION_GRANTED){
                   ActivityCompat.requestPermissions(AddVehicleActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},111);
                }
                else {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent, 222);
                }
//
//                CropImage.activity()
//                        .setGuidelines(CropImageView.Guidelines.ON)
//                        .setAspectRatio(1,1)
//                        .start(AddVehicleActivity.this);
            }
        });
        binding.btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtil.getConnectivityStatus(AddVehicleActivity.this)) {
                    String etcategory = binding.etcategory.getText().toString();
                    if (etcategory.isEmpty()) {
                        binding.etcategory.setError("this field can't be empty.");
                        return;
                    }
                    String etcount = binding.etcount.getText().toString();
                    if (etcount.isEmpty()) {
                        binding.etcount.setError("this field can't be empty.");
                        return;
                    }
                    String token = "4324343434343432432434343434";
                    if (imgUri != null) {
                        File file = FileUtils.getFile(AddVehicleActivity.this, imgUri);
                        RequestBody requestFile =
                                RequestBody.create(
                                        MediaType.parse(getContentResolver().getType(imgUri)),
                                        file
                                );
                        MultipartBody.Part body =
                                MultipartBody.Part.createFormData("file", file.getName(), requestFile);
                        RequestBody name=RequestBody.create(okhttp3.MultipartBody.FORM, etcategory);
                        RequestBody count=RequestBody.create(okhttp3.MultipartBody.FORM, etcount);
                        RequestBody transporterId=RequestBody.create(okhttp3.MultipartBody.FORM, FirebaseAuth.getInstance().getCurrentUser().getUid());

                        VehicleService.VehicleApi vehicleApi=VehicleService.getVehicleApiInstance();
                        Call<Vehicle>call=vehicleApi.createVehicle(name,count,transporterId,body);
                        call.enqueue(new Callback<Vehicle>() {
                            @Override
                            public void onResponse(Call<Vehicle> call, Response<Vehicle> response) {
                                Log.e("inside response","dfddfddf");
                                Log.e("spanshoe",""+response.body());
                                if (!response.body().getVehicleId().isEmpty()){
                                    Intent in=new Intent();
                                    setResult(222,in);
                                    finish();
                                }
                            }

                            @Override
                            public void onFailure(Call<Vehicle> call, Throwable t) {
                                Toast.makeText(AddVehicleActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.e("spanshoe",""+t.getMessage());
                            }
                        });
                    } else
                        Toast.makeText(AddVehicleActivity.this, "Please select vehicle picture.", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(AddVehicleActivity.this, "please enable internet connection.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.ivBackErroe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
