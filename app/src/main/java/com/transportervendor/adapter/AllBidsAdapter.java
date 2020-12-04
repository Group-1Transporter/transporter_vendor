package com.transportervendor.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.transportervendor.BidInfoActivity;
import com.transportervendor.BidNowActivity;
import com.transportervendor.apis.*;
import com.transportervendor.beans.Bid;
import com.transportervendor.beans.BidWithLead;
import com.transportervendor.beans.Leads;
import com.transportervendor.databinding.AllBidViewBinding;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllBidsAdapter extends RecyclerView.Adapter<AllBidsAdapter.AllBidsViewHolder> {
    Context context;
    ArrayList<BidWithLead>al;
    public AllBidsAdapter(Context context, ArrayList<BidWithLead>al) {
        this.context=context;
        this.al=al;
    }

    @NonNull
    @Override
    public AllBidsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AllBidViewBinding binding=AllBidViewBinding.inflate(LayoutInflater.from(context));
        return new AllBidsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final AllBidsViewHolder holder, int position) {
        final BidWithLead bid=al.get(position);
        Leads leads=bid.getLead();
        holder.binding.material.setText("Material: "+leads.getTypeOfMaterial());
        holder.binding.weight.setText("Weight: "+leads.getWeight());
        String str[]=leads.getPickUpAddress().split(",");
        String name=str[str.length-2];
        str=leads.getDeliveryAddress().split(",");
        name +=" to "+str[str.length-2];
        holder.binding.location.setText("Location: "+name);
        if(leads.getStatus()!=null) {
            if (leads.getStatus().equals("")) {
                holder.binding.status.setText("Pending");
            } else if (leads.getDealLockedWith().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                holder.binding.status.setText("Confirmed");
            } else {
                holder.binding.status.setText("Rejected");
            }
        }
        else
            holder.binding.status.setText("Pending");
        holder.binding.amount.setText("Amount: "+bid.getBid().getAmount());
        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bid.getLead().getStatus().equals("") || (bid.getLead().getDealLockedWith().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()) && bid.getLead().getStatus().equalsIgnoreCase("confirmed"))){
                    Intent in = new Intent(context, BidInfoActivity.class);
                    in.putExtra("leads", bid);
                    context.startActivity(in);
                }else if (bid.getLead().getDealLockedWith().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()) && (!bid.getLead().getStatus().equalsIgnoreCase("confirmed"))){
                    Toast.makeText(context, "you can't edit this bid.", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "this bid is rejected.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return al.size();
    }

    public class AllBidsViewHolder extends RecyclerView.ViewHolder{
        AllBidViewBinding binding;
        public AllBidsViewHolder(@NonNull AllBidViewBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
