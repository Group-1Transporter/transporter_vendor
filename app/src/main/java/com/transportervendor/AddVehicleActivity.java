package com.transportervendor;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.gson.Gson;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.transportervendor.apis.VehicleService;
import com.transportervendor.beans.Transporter;
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
        if (requestCode == 111) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    imgUri=data.getData();
                    binding.ivVehicleImage.setImageURI(imgUri);
                }
            }
        }
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

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"),111);
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
                        Call<Transporter>call=vehicleApi.createVehicle(name,count,transporterId,body);
                        final CustomProgressDialog pd=new CustomProgressDialog(AddVehicleActivity.this,"Please wait...");
                        pd.show();
                        call.enqueue(new Callback<Transporter>() {
                            @Override
                            public void onResponse(Call<Transporter> call, Response<Transporter> response) {
                                pd.dismiss();
                                if (response.code()==200){
                                    Transporter t=response.body();
                                    SharedPreferences mPrefs = getSharedPreferences("Transporter",MODE_PRIVATE);
                                    SharedPreferences.Editor prefsEditor = mPrefs.edit();
                                    Gson gson = new Gson();
                                    String json = gson.toJson(t);
                                    Log.e("spanshoe",""+json);
                                    prefsEditor.putString("Transporter", json);
                                    prefsEditor.commit();
                                    Intent in=new Intent();
                                    setResult(222,in);
                                    finish();
                                }
                            }

                            @Override
                            public void onFailure(Call<Transporter> call, Throwable t) {
                                pd.dismiss();
                                Toast.makeText(AddVehicleActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
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
