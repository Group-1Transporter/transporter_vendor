package com.transportervendor;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.transportervendor.apis.BidService;
import com.transportervendor.apis.TransporterService;
import com.transportervendor.apis.UserService;
import com.transportervendor.beans.Bid;
import com.transportervendor.beans.Leads;
import com.transportervendor.beans.Transporter;
import com.transportervendor.beans.User;
import com.transportervendor.databinding.BidNowViewBinding;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BidNowActivity extends AppCompatActivity {
    Toolbar toolbar;
    BidNowViewBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent in = getIntent();
        final Leads leads = (Leads) in.getSerializableExtra("leads");
        UserService.UserApi userApi = UserService.getUserApiInstance();
        Call<User> call = userApi.getUser(leads.getUserId());
        binding = BidNowViewBinding.inflate(LayoutInflater.from(BidNowActivity.this));
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        if (NetworkUtil.getConnectivityStatus(this)) {
            final CustomProgressDialog pd=new CustomProgressDialog(BidNowActivity.this,"Please wait...");
            pd.show();
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    pd.dismiss();
                    if (response.code() == 200) {
                        final User user = response.body();
                        Log.e("spanshoe","token"+user.getToken());
                        binding.username.setText("Username: " + user.getName());
                        binding.resusername.setText("Send Response to " + user.getName());
                        String str[] = leads.getPickUpAddress().split(",");
                        String name = str[str.length - 2];
                        str = leads.getDeliveryAddress().split(",");
                        name += " to " + str[str.length - 2];
                        binding.tvfrom.setText(name);
                        binding.tvmaterial.setText("Material: " + leads.getTypeOfMaterial());
                        binding.tvweight.setText("Weight: " + leads.getWeight());
                        binding.pickadd.setText("Pickup Address: " + leads.getPickUpAddress());
                        binding.deladd.setText("Delivery Address: " + leads.getDeliveryAddress());
                        binding.pickcontact.setText("Pickup Contact: " + leads.getContactForPickup());
                        binding.delcontact.setText("Delivery Contact: " + leads.getContactForDelivery());
                        binding.lastdate.setText("Last Date: " + leads.getDateOfCompletion());
                        binding.btnbidnow.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final String rate = binding.etRate.getText().toString();
                                String remark = binding.etRemark.getText().toString();
                                if (rate.isEmpty()) {
                                    binding.etRate.setError("this field can't be empty.");
                                    return;
                                }
                                if (remark.isEmpty()) {
                                    binding.etRemark.setError("this field can't be empty.");
                                    return;
                                }
                                SharedPreferences shared = getSharedPreferences("Transporter", Context.MODE_PRIVATE);
                                String json=shared.getString("Transporter","");
                                Gson gson=new Gson();
                                Transporter transporter=gson.fromJson(json,Transporter.class);
                                final Bid bid = new Bid("", leads.getLeadId(), FirebaseAuth.getInstance().getCurrentUser().getUid(), transporter.getName(), rate, remark, leads.getDateOfCompletion());
                                BidService.BidApi bidApi = BidService.getBidApiInstance();
                                Call<Bid> call = bidApi.createBid(bid);
                                if (NetworkUtil.getConnectivityStatus(BidNowActivity.this)) {
                                    pd.show();
                                    call.enqueue(new Callback<Bid>() {
                                        @Override
                                        public void onResponse(Call<Bid> call, Response<Bid> response) {
                                            pd.dismiss();
                                            if (response.code() == 200) {
                                            try {
                                                RequestQueue queue = Volley.newRequestQueue(BidNowActivity.this);
                                                String url = "https://fcm.googleapis.com/fcm/send";
                                                JSONObject data = new JSONObject();
                                                data.put("title", "new bid");
                                                SharedPreferences shared = getSharedPreferences("Transporter", Context.MODE_PRIVATE);
                                                String json=shared.getString("Transporter","");
                                                Gson gson = new Gson();
                                                Transporter transporter = gson.fromJson(json, Transporter.class);
                                                data.put("body", "From : " + transporter.getName());
                                                JSONObject notification_data = new JSONObject();
                                                notification_data.put("data", data);
                                                notification_data.put("to", user.getToken());
                                                JsonObjectRequest request = new JsonObjectRequest(url, notification_data, new com.android.volley.Response.Listener<JSONObject>() {
                                                    @Override
                                                    public void onResponse(JSONObject response) {
                                                        Log.e("spanshoe","response");
                                                    }
                                                }, new com.android.volley.Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        Log.e("spanshoe","error..."+error.getMessage());
                                                        Toast.makeText(BidNowActivity.this, "" + error, Toast.LENGTH_SHORT).show();
                                                    }
                                                }) {
                                                    @Override
                                                    public Map<String, String> getHeaders() {
                                                        String api_key_header_value = "Key=AAAA_8lmWnQ:APA91bEYQuN6DDzns0nY2CzXq-FUhVCvv0pGXq0nr3iH_sg27WDB8PcN1RFTz7-If5SNVHOfA3SMuxQyWyPZKb-cns4Sd06iMbIb7vruOHtiBrebRDZAqrMx5Hl5zmHanUFXDCi6ekSr";
                                                        Map<String, String> headers = new HashMap<>();
                                                        headers.put("Content-Type", "application/json");
                                                        headers.put("Authorization", api_key_header_value);
                                                        return headers;
                                                    }
                                                };
                                                queue.add(request);
                                            }catch (Exception e){
                                                Toast.makeText(BidNowActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                                finish();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Bid> call, Throwable t) {
                                            pd.dismiss();
                                            Toast.makeText(BidNowActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else
                                    Toast.makeText(BidNowActivity.this, "Please enable internet connection.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    pd.dismiss();
                    Toast.makeText(BidNowActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }

            });

        }else{
            Toast.makeText(this, "please enable internet connection.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}
