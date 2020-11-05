package com.transportervendor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.transportervendor.beans.Leads;
import com.transportervendor.databinding.HistoryViewBinding;

import java.util.ArrayList;

public class CompletedLeadsAdapter extends RecyclerView.Adapter<CompletedLeadsAdapter.CompletedLeadsViewHolder> {
    HistoryViewBinding binding;
    Context context;
    ArrayList<Leads>al;
    public CompletedLeadsAdapter(Context context,ArrayList<Leads>al){
        this.al=al;
        this.context=context;
    }
    @NonNull
    @Override
    public CompletedLeadsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding=HistoryViewBinding.inflate(LayoutInflater.from(context));
        View v=binding.getRoot();
        return new CompletedLeadsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CompletedLeadsAdapter.CompletedLeadsViewHolder holder, int position) {
        Leads leads=al.get(position);
        String str[]=leads.getPickUpAddress().split(" ");
        String name=str[str.length-2];
        str=leads.getDeliveryAddress().split(" ");
        name +=" to "+str[str.length-2];
        binding.location.setText("Location: "+name);
        binding.material.setText("Material: "+leads.getTypeOfMaterial());
        binding.weight.setText("Weight: "+leads.getWeight());
        binding.amount.setText("Amount: Loading...");
        binding.date.setText(leads.getDateOfCompletion());
    }

    @Override
    public int getItemCount() {
        return al.size();
    }

    public  class CompletedLeadsViewHolder extends RecyclerView.ViewHolder {
        public CompletedLeadsViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
