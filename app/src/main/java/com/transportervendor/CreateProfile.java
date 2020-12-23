package com.transportervendor;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.transportervendor.apis.LeadsService;
import com.transportervendor.apis.TransporterService;
import com.transportervendor.beans.Leads;
import com.transportervendor.beans.Transporter;
import com.transportervendor.beans.Vehicle;
import com.transportervendor.databinding.CreateProfileBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateProfile extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    CreateProfileBinding binding;
    String imgUrl="";
    ArrayList<Vehicle>al=new ArrayList<>();
    Uri imgUri;
    int flag = 2;
    int code=0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = CreateProfileBinding.inflate(LayoutInflater.from(this));
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("Create Profile");
        Intent in=getIntent();
        code=in.getIntExtra("code",0);
        setContentView(binding.getRoot());
        binding.cd1.setVisibility(View.GONE);
        binding.cd2.setVisibility(View.GONE);
        binding.cd3.setVisibility(View.GONE);
        binding.pending.setVisibility(View.GONE);
        binding.completed.setVisibility(View.GONE);
        binding.total.setVisibility(View.GONE);
        if(code==2){
            SharedPreferences mPrefs = getSharedPreferences("Transporter",MODE_PRIVATE);
            String json=mPrefs.getString("Transporter","");
            Gson gson = new Gson();
            String completed=mPrefs.getString("completed","");
            String pending=mPrefs.getString("pending","");
            Transporter transporter=gson.fromJson(json,Transporter.class);
            if(!(pending.equals("")) && !(pending.equals("")) ) {
                int p = Integer.parseInt(pending);
                int c = Integer.parseInt(completed);
                binding.cd1.setVisibility(View.VISIBLE);
                binding.cd2.setVisibility(View.VISIBLE);
                binding.cd3.setVisibility(View.VISIBLE);
                binding.pending.setVisibility(View.VISIBLE);
                binding.completed.setVisibility(View.VISIBLE);
                binding.total.setVisibility(View.VISIBLE);
                binding.pending.setText(""+p);
                binding.completed.setText(""+c);
                p+=c;
                binding.total.setText(""+p);
            }
            if(NetworkUtil.getConnectivityStatus(this)) {
                Picasso.get().load(transporter.getImage()).placeholder(R.drawable.transporter_logo).into(binding.civ);
            }else
                Toast.makeText(this, "please enable internet connection.", Toast.LENGTH_SHORT).show();
            ArrayList<String>a=new ArrayList<>();
            a.add("Truck Owner");
            a.add("Transport Company");
            a.add("Packers and movers");
            final ArrayAdapter<String> ad =new ArrayAdapter<>(CreateProfile.this,android.R.layout.simple_spinner_item,a);
            ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.sp.setAdapter(ad);
            binding.sp.setOnItemSelectedListener(this);
            if(transporter.getType().equalsIgnoreCase("Truck Owner")) {
                a.set(0, "Truck Owner");
                binding.aadhar.setVisibility(View.VISIBLE);
            }
            else if(transporter.getType().equalsIgnoreCase("Transport Company")) {
                a.set(0, "Transport Company");
                binding.gst.setVisibility(View.VISIBLE);
            }
            else if(transporter.getType().equalsIgnoreCase("Packers and Movers")) {
                a.set(0, "Packers and Movers");
                binding.gst.setVisibility(View.VISIBLE);
            }
            ad.notifyDataSetChanged();
            binding.etname.setText(transporter.getName());
            binding.address.setText(transporter.getAddress());
            al=transporter.getVehicleList();
            binding.gst.setText(transporter.getGstNo());
            binding.aadhar.setText(transporter.getAadharCardNumber());
            binding.phone.setText(transporter.getContactNumber());
            imgUrl=transporter.getImage();
            binding.create.setText("update");
        }
        binding.civ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 111);
            }
        });
        if(code!=2) {
            final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.type, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.sp.setAdapter(adapter);
            binding.sp.setOnItemSelectedListener(this);
        }
        binding.create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.etname.getText().toString();
                if (name.isEmpty()) {
                    binding.etname.setError("this field can't be empty.");
                    return;
                }
                final String address = binding.address.getText().toString();
                if (address.isEmpty()) {
                    binding.address.setError("this field can't be empty.");
                    return;
                }
                String phone = binding.phone.getText().toString();
                if (phone.isEmpty() || phone.length()!=10) {
                    binding.phone.setError("this field can't be empty.");
                    return;
                }
                String adhar = "", gst = "", type = "";
                if (flag == 0) {
                    adhar = binding.aadhar.getText().toString();
                    type = "Truck Owner";
                    if (adhar.isEmpty() || adhar.length()!=12) {
                        binding.aadhar.setError("this field can't be empty.");
                        return;
                    }
                } else if (flag == 1 || flag == 3) {
                    type = "Transport Company";
                    if (flag == 3)
                        type = "Packers and Movers";
                    gst = binding.gst.getText().toString();
                    if (gst.isEmpty()) {
                        binding.gst.setError("this field can't be empty.");
                        return;
                    }
                } else if (flag == 2) {
                    Toast.makeText(CreateProfile.this, "please select a category.", Toast.LENGTH_SHORT).show();
                    return;
                }
                String token = FirebaseInstanceId.getInstance().getToken();
                if(code!=2) {
                    if (imgUri == null) {
                        Toast.makeText(CreateProfile.this, "please select profile picture", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    File file = FileUtils.getFile(CreateProfile.this, imgUri);
                    RequestBody requestFile =
                            RequestBody.create(
                                    MediaType.parse(getContentResolver().getType(imgUri)),
                                    file
                            );
                    MultipartBody.Part body =
                            MultipartBody.Part.createFormData("file", file.getName(), requestFile);
                    RequestBody etname = RequestBody.create(okhttp3.MultipartBody.FORM, name);
                    RequestBody typ = RequestBody.create(okhttp3.MultipartBody.FORM, type);
                    RequestBody gs = RequestBody.create(okhttp3.MultipartBody.FORM, gst);
                    RequestBody adh = RequestBody.create(okhttp3.MultipartBody.FORM, adhar);
                    RequestBody pho = RequestBody.create(okhttp3.MultipartBody.FORM, phone);
                    RequestBody add = RequestBody.create(okhttp3.MultipartBody.FORM, address);
                    RequestBody rat = RequestBody.create(okhttp3.MultipartBody.FORM, "");
                    RequestBody tok = RequestBody.create(okhttp3.MultipartBody.FORM, token);
                    RequestBody id = RequestBody.create(okhttp3.MultipartBody.FORM, FirebaseAuth.getInstance().getCurrentUser().getUid());
                    TransporterService.TransporterApi transporterApi = TransporterService.getTransporterApiInstance();
                    Call<Transporter> call = transporterApi.createTransporter(body, typ, etname, pho, add, gs, rat, tok, adh, id);
                    if (NetworkUtil.getConnectivityStatus(CreateProfile.this)) {
                        final CustomProgressDialog pd = new CustomProgressDialog(CreateProfile.this, "Please Wait...");
                        pd.show();
                        call.enqueue(new Callback<Transporter>() {
                            @Override
                            public void onResponse(Call<Transporter> call, Response<Transporter> response) {
                                pd.dismiss();
                                if (response.code() == 200) {
                                    Toast.makeText(CreateProfile.this, "profile created", Toast.LENGTH_SHORT).show();
                                    SharedPreferences mPrefs = getSharedPreferences("Transporter", MODE_PRIVATE);
                                    Transporter t = response.body();
                                    SharedPreferences.Editor prefsEditor = mPrefs.edit();
                                    Gson gson = new Gson();
                                    String json = gson.toJson(t);
                                    prefsEditor.putString("Transporter", json);
                                    prefsEditor.commit();
                                    Intent in = new Intent(CreateProfile.this, ManageVehicle.class);
                                    in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(in);
                                    finish();
                                }
                            }

                            @Override
                            public void onFailure(Call<Transporter> call, Throwable t) {
                                pd.dismiss();
                                Toast.makeText(CreateProfile.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else
                        Toast.makeText(CreateProfile.this, "please enable internet connection.", Toast.LENGTH_SHORT).show();
                }else{
                    Transporter transporter=new Transporter(name,FirebaseAuth.getInstance().getCurrentUser().getUid(),type,imgUrl,phone,address,adhar,gst,"",token,al);
                    TransporterService.TransporterApi transporterApi=TransporterService.getTransporterApiInstance();
                    Call<Transporter>call=transporterApi.updateTransporter(transporter);
                    if(NetworkUtil.getConnectivityStatus(CreateProfile.this)) {
                        final CustomProgressDialog pd=new CustomProgressDialog(CreateProfile.this,"Please wait...");
                        pd.show();
                        call.enqueue(new Callback<Transporter>() {
                            @Override
                            public void onResponse(Call<Transporter> call, Response<Transporter> response) {
                                pd.dismiss();
                                if (response.code() == 200) {
                                    Toast.makeText(CreateProfile.this, "profile updated", Toast.LENGTH_SHORT).show();
                                    SharedPreferences mPrefs = getSharedPreferences("Transporter", MODE_PRIVATE);
                                    Transporter t = response.body();
                                    SharedPreferences.Editor prefsEditor = mPrefs.edit();
                                    Gson gson = new Gson();
                                    String json = gson.toJson(t);
                                    prefsEditor.putString("Transporter", json);
                                    prefsEditor.commit();
                                    Log.e("mprefs", mPrefs.getString("Transporter", "..........."));
                                    Intent in = new Intent(CreateProfile.this, ManageVehicle.class);
                                    in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(in);
                                    finish();
                                }
                            }

                            @Override
                            public void onFailure(Call<Transporter> call, Throwable t) {
                                pd.dismiss();
                                Toast.makeText(CreateProfile.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else
                        Toast.makeText(CreateProfile.this, "please enable internet connection.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imgUri = data.getData();
            if(code==2){
                TransporterService.TransporterApi transporterApi=TransporterService.getTransporterApiInstance();
                RequestBody id = RequestBody.create(okhttp3.MultipartBody.FORM, FirebaseAuth.getInstance().getCurrentUser().getUid());
                File file = FileUtils.getFile(CreateProfile.this, imgUri);
                RequestBody requestFile =
                        RequestBody.create(
                                MediaType.parse(getContentResolver().getType(imgUri)),
                                file
                        );
                MultipartBody.Part body =
                        MultipartBody.Part.createFormData("file", file.getName(), requestFile);
                Call<Transporter>cal=transporterApi.updateImage(id,body);
                final CustomProgressDialog pd=new CustomProgressDialog(CreateProfile.this,"Please wait...");
                pd.show();
                cal.enqueue(new Callback<Transporter>() {
                    @Override
                    public void onResponse(Call<Transporter> call, Response<Transporter> response) {
                        pd.dismiss();
                        if(response.code()==200){
                            imgUrl=response.body().getImage();
                            Picasso.get().load(imgUrl).placeholder(R.drawable.transporter_logo).into(binding.civ);
                            Toast.makeText(CreateProfile.this, "photo uploaded.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Transporter> call, Throwable t) {
                        pd.dismiss();
                        Toast.makeText(CreateProfile.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }else {
                binding.civ.setImageURI(imgUri);
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = (String) parent.getItemAtPosition(position);
        if (item.equalsIgnoreCase("Truck Owner")) {
            flag = 0;
            binding.aadhar.setVisibility(View.VISIBLE);
            binding.gst.setVisibility(View.GONE);
        } else if (item.equalsIgnoreCase("Transport Company")) {
            binding.gst.setVisibility(View.VISIBLE);
            flag = 1;
            binding.aadhar.setVisibility(View.GONE);
        } else if (item.equalsIgnoreCase("Packers and Movers")) {
            flag = 3;
            binding.gst.setVisibility(View.VISIBLE);
            binding.aadhar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
