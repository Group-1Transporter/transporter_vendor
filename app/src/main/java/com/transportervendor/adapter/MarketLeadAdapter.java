package com.transportervendor.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.transportervendor.BidNowActivity;
import com.transportervendor.R;
import com.transportervendor.apis.BidService;
import com.transportervendor.apis.UserService;
import com.transportervendor.beans.BidWithLead;
import com.transportervendor.beans.Leads;
import com.transportervendor.beans.User;
import com.transportervendor.databinding.MarketLeadViewBinding;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MarketLeadAdapter extends RecyclerView.Adapter<MarketLeadAdapter.MarketLeadViewHolder> {
    Context context;
    ArrayList<Leads>al;
    ArrayList<String>lid;
    MarketLeadViewBinding binding;
    public MarketLeadAdapter(Context context,ArrayList<Leads>al){
        this.al=al;
        this.context=context;
    }

    public void setLid(ArrayList<String> lid) {
        this.lid = lid;
    }

    @NonNull
    @Override
    public MarketLeadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding=MarketLeadViewBinding.inflate(LayoutInflater.from(context));
        View v=binding.getRoot();
        return new MarketLeadViewHolder(v) ;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull MarketLeadViewHolder holder, int position) {
        final Leads leads=al.get(position);
        String str[] = leads.getPickUpAddress().split(" ");
        String name = str[str.length - 2];
        str = leads.getDeliveryAddress().split(" ");
        name += " to " + str[str.length - 2];
        UserService.UserApi userApi = UserService.getLeadsApiInstance();
        Call<User> call = userApi.getUser(leads.getUserId());
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();
                binding.username.setText(user.getName());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        binding.tvfrom.setText(name);
        binding.username.setText("username");
        binding.lastdate.setText("Last Date: " + leads.getDateOfCompletion());
        binding.tvmaterial.setText("Material: " + leads.getTypeOfMaterial());
        binding.tvweight.setText("Weight:         " + leads.getWeight());
        if(lid.contains(leads.getLeadId())){
            binding.bidbtn.setText("Bidded");
            binding.bidbtn.setBackgroundResource(R.drawable.bg_btn_bid);

        }else {
            binding.bidbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    Intent in = new Intent(context, BidNowActivity.class);
                    in.putExtra("leads", leads);
                    view.getContext().startActivity(in);
                }
            });
        }

    }

    @Override
    public int getItemCount() {

        if(al!=null)
            return al.size();
        else
            Toast.makeText(context, "no data...", Toast.LENGTH_SHORT).show();
        return 0;
    }

    public class MarketLeadViewHolder extends RecyclerView.ViewHolder{
        public MarketLeadViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
