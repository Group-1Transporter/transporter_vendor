package com.transportervendor;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.transportervendor.apis.BidService;
import com.transportervendor.apis.TransporterService;
import com.transportervendor.apis.UserService;
import com.transportervendor.beans.Bid;
import com.transportervendor.beans.Leads;
import com.transportervendor.beans.Transporter;
import com.transportervendor.beans.User;
import com.transportervendor.databinding.BidNowViewBinding;

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
        UserService.UserApi userApi = UserService.getLeadsApiInstance();
        Call<User> call = userApi.getUser(leads.getUserId());
        binding = BidNowViewBinding.inflate(LayoutInflater.from(BidNowActivity.this));
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        if (NetworkUtil.getConnectivityStatus(this)) {
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    User user = response.body();
                    binding.username.setText("Username: " + user.getName());
                    binding.resusername.setText("Send Response to " + user.getName());
                    String str[] = leads.getPickUpAddress().split(" ");
                    String name = str[str.length - 2];
                    str = leads.getDeliveryAddress().split(" ");
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
                            final Bid bid = new Bid("", leads.getLeadId(), FirebaseAuth.getInstance().getCurrentUser().getUid(), "", rate, remark, leads.getDateOfCompletion());
                            BidService.BidApi bidApi = BidService.getBidApiInstance();
                            Call<Bid> call = bidApi.createBid(bid);
                            call.enqueue(new Callback<Bid>() {
                                @Override
                                public void onResponse(Call<Bid> call, Response<Bid> response) {
                                    if (rate == response.body().getAmount()) {
                                        finish();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Bid> call, Throwable t) {
                                }
                            });
                        }
                    });
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
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
