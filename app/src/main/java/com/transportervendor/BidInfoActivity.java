package com.transportervendor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.transportervendor.apis.BidService;
import com.transportervendor.apis.UserService;
import com.transportervendor.beans.Bid;
import com.transportervendor.beans.BidWithLead;
import com.transportervendor.beans.Leads;
import com.transportervendor.beans.User;
import com.transportervendor.databinding.BidNowViewBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BidInfoActivity extends AppCompatActivity {
    BidNowViewBinding binding;
    BidWithLead bid;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=BidNowViewBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        Intent in=getIntent();
        bid= (BidWithLead) in.getSerializableExtra("leads");
        setSupportActionBar(binding.toolbar);
    }

    @Override
    protected void onStart() {
        super.onStart();
        UserService.UserApi userApi = UserService.getUserApiInstance();
        Call<User> call = userApi.getUser(bid.getLead().getUserId());
        if (NetworkUtil.getConnectivityStatus(this)) {
            final CustomProgressDialog pd=new CustomProgressDialog(BidInfoActivity.this,"Please wait...");
            pd.show();
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    pd.dismiss();
                    if (response.code() == 200) {
                        binding.username.setText("Username: " +response.body().getName());
                        binding.resusername.setText("Send Response to " +response.body().getName());
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    pd.dismiss();
                    Toast.makeText(BidInfoActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        binding.etRemark.setText(bid.getBid().getRemark());
        String str[] = bid.getLead().getPickUpAddress().split(",");
        String name = str[str.length - 2];
        str = bid.getLead().getDeliveryAddress().split(",");
        name += " to " + str[str.length-2];
        binding.tvfrom.setText(name);
        binding.tvmaterial.setText("Material: " +bid.getLead().getTypeOfMaterial());
        binding.tvweight.setText("Weight: " +bid.getLead().getWeight());
        binding.etRate.setText(bid.getBid().getAmount());
        binding.etRemark.setText(bid.getBid().getRemark());
        binding.lastdate.setText("Last Date: " + bid.getLead().getDateOfCompletion());
        binding.pickcontact.setText("Pickup Contact: " +  bid.getLead().getContactForPickup());
        binding.delcontact.setText( "Delivery Contact: " + bid.getLead().getContactForDelivery());
        binding.pickadd.setText("Pickup Address: " +bid.getLead().getPickUpAddress());
        binding.deladd.setText("Delivery Address: " +bid.getLead().getDeliveryAddress());
        binding.btnbidnow.setText("update Bid");
        binding.btnbidnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String etrate = binding.etRate.getText().toString();
                String etremark = binding.etRemark.getText().toString();
                if (etrate.isEmpty()) {
                    binding.etRate.setError("this field can't be empty.");
                    return;
                }
                if (etremark.isEmpty()) {
                    binding.etRemark.setError("this field can't be empty.");
                    return;
                }
                if (NetworkUtil.getConnectivityStatus(BidInfoActivity.this)) {
                    finish();
                    final CustomProgressDialog pd=new CustomProgressDialog(BidInfoActivity.this,"Please wait...");
                    pd.show();
                    final Bid bd = new Bid(bid.getBid().getBidId(), bid.getLead().getLeadId(), FirebaseAuth.getInstance().getCurrentUser().getUid(), "", etrate, etremark, bid.getLead().getDateOfCompletion());
                    BidService.BidApi bidApi = BidService.getBidApiInstance();
                    Call<Bid> call = bidApi.updateBid(bd);
                    call.enqueue(new Callback<Bid>() {
                        @Override
                        public void onResponse(Call<Bid> call, Response<Bid> response) {
                            pd.dismiss();
                            if (response.code() == 200) {
                                Toast.makeText(BidInfoActivity.this, "update success.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Bid> call, Throwable t) {
                            pd.dismiss();
                            Toast.makeText(BidInfoActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }else
                    Toast.makeText(BidInfoActivity.this, "please enable internet connection.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
