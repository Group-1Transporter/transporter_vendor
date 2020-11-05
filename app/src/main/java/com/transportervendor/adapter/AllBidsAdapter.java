package com.transportervendor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.transportervendor.apis.*;
import com.transportervendor.beans.Bid;
import com.transportervendor.beans.Leads;
import com.transportervendor.databinding.AllBidViewBinding;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllBidsAdapter extends RecyclerView.Adapter<AllBidsAdapter.AllBidsViewHolder> {
    AllBidViewBinding binding;
    Context context;
    ArrayList<Bid>al;
    public AllBidsAdapter(Context context, ArrayList<Bid>al) {
        this.context=context;
        this.al=al;
    }

    @NonNull
    @Override
    public AllBidsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding=AllBidViewBinding.inflate(LayoutInflater.from(context));
        View v=binding.getRoot();
        return new AllBidsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AllBidsViewHolder holder, int position) {
        Bid bid=al.get(position);
        LeadsService.LeadsApi leadApi=LeadsService.getLeadsApiInstance();
        Call<Leads> call=leadApi.getLeads(bid.getLeadId());
        Leads leads= null;
        try {
            leads = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        binding.amount.setText("Amount: "+bid.getAmount());
        binding.material.setText("Material: "+leads.getTypeOfMaterial());
        binding.weight.setText("Weight: "+leads.getWeight());
        String str[]=leads.getPickUpAddress().split(" ");
        String name=str[str.length-2];
        str=leads.getDeliveryAddress().split(" ");
        name +=" to "+str[str.length-2];
        binding.location.setText("Location: "+name);
        if(leads.getStatus().isEmpty()){
            binding.status.setText("Pending");
        }else if(leads.getDealLockedWith()=="username"){
            binding.status.setText("Confirmed");
        }else{
            binding.status.setText("Rejected");
        }
    }

    @Override
    public int getItemCount() {
        return al.size();
    }

    public class AllBidsViewHolder extends RecyclerView.ViewHolder{

        public AllBidsViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
