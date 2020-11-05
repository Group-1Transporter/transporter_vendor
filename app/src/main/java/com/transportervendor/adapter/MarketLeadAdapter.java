package com.transportervendor.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.transportervendor.BidNowActivity;
import com.transportervendor.beans.Leads;
import com.transportervendor.databinding.MarketLeadViewBinding;

import java.util.ArrayList;

public class MarketLeadAdapter extends RecyclerView.Adapter<MarketLeadAdapter.MarketLeadViewHolder> {
    Context context;
    ArrayList<Leads>al;
    MarketLeadViewBinding binding;
    public MarketLeadAdapter(Context context,ArrayList<Leads>al){
        this.al=al;
        this.context=context;
    }
    @NonNull
    @Override
    public MarketLeadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding=MarketLeadViewBinding.inflate(LayoutInflater.from(context));
        View v=binding.getRoot();
        return new MarketLeadViewHolder(v) ;
    }

    @Override
    public void onBindViewHolder(@NonNull MarketLeadViewHolder holder, int position) {
        Leads leads=al.get(position);
        String str[]=leads.getPickUpAddress().split(" ");
        String name=str[str.length-2];
        str=leads.getDeliveryAddress().split(" ");
        name +=" to "+str[str.length-2];
        binding.tvfrom.setText(name);
        binding.username.setText("username");
        binding.lastdate.setText("Last Date: "+leads.getDateOfCompletion());
        binding.tvmaterial.setText("Material: "+leads.getTypeOfMaterial());
        binding.tvweight.setText("Weight:         "+leads.getWeight());
        binding.bidbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.getContext().startActivity(new Intent(context,BidNowActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return al.size();
    }

    public class MarketLeadViewHolder extends RecyclerView.ViewHolder{
        public MarketLeadViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
