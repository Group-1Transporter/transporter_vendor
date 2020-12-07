package com.transportervendor.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.transportervendor.beans.Bid;
import com.transportervendor.beans.BidWithLead;
import com.transportervendor.beans.Leads;
import com.transportervendor.databinding.HistoryViewBinding;

import java.util.ArrayList;

public class CompletedLeadsAdapter extends RecyclerView.Adapter<CompletedLeadsAdapter.CompletedLeadsViewHolder> {
    Context context;
    ArrayList<BidWithLead>al;
    public CompletedLeadsAdapter(Context context, ArrayList<BidWithLead>al){
        this.al=al;
        this.context=context;
    }
    @NonNull
    @Override
    public CompletedLeadsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        HistoryViewBinding binding=HistoryViewBinding.inflate(LayoutInflater.from(context));
        return new CompletedLeadsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CompletedLeadsAdapter.CompletedLeadsViewHolder holder, int position) {
        BidWithLead bidWithLead=al.get(position);
        Leads leads=bidWithLead.getLead();
        String str[]=leads.getPickUpAddress().split(",");
        String name=str[str.length-2];
        str=leads.getDeliveryAddress().split(",");
        name +=" to "+str[str.length-2];
        holder.binding.location.setText("Location: "+name);
        holder.binding.material.setText("Material: "+leads.getTypeOfMaterial());
        holder.binding.weight.setText("Weight: "+leads.getWeight());
        holder.binding.amount.setText("Amount: "+bidWithLead.getBid().getAmount());
        holder.binding.date.setText(leads.getDateOfCompletion());
    }

    @Override
    public int getItemCount() {
        if(al!=null)
            return al.size();
        else
            Toast.makeText(context, "no data", Toast.LENGTH_SHORT).show();
            return 0;
    }

    public  class CompletedLeadsViewHolder extends RecyclerView.ViewHolder {
        HistoryViewBinding binding;
        public CompletedLeadsViewHolder(@NonNull HistoryViewBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
