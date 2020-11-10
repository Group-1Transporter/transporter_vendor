package com.transportervendor.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.transportervendor.R;
import com.transportervendor.beans.BidWithLead;
import com.transportervendor.beans.Leads;
import com.transportervendor.databinding.CurrentLoadViewBinding;
import com.transportervendor.databinding.UpdateStatusViewBinding;

import java.util.ArrayList;

public class CurrentLeadsAdapter extends RecyclerView.Adapter<CurrentLeadsAdapter.CurrentLeadsViewHolder> {
    ArrayList<BidWithLead>al;
    Context context;

    public CurrentLeadsAdapter(Context context,ArrayList<BidWithLead>al){
        this.context=context;
        this.al=al;
    }
    @NonNull
    @Override
    public CurrentLeadsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CurrentLoadViewBinding binding=CurrentLoadViewBinding.inflate(LayoutInflater.from(context));
        return new CurrentLeadsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final CurrentLeadsViewHolder holder, int position) {
        final BidWithLead bidWithLead=al.get(position);
        final Leads leads=bidWithLead.getLead();
        holder.binding.tvmaterial.setText("Material:"+leads.getTypeOfMaterial());
        String str[]=leads.getPickUpAddress().split(" ");
        String name=str[str.length-2];
        str=leads.getDeliveryAddress().split(" ");
        name +=" to "+str[str.length-2];
        holder.binding.tvfrom.setText(name);
        holder.binding.status.setText(holder.binding.status.getText()+" "+leads.getStatus());
        holder.binding.tvdate.setText("Date of completion: "+leads.getDateOfCompletion());
        holder.binding.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"You Clicked : ", Toast.LENGTH_SHORT).show();
                PopupMenu popup = new PopupMenu(context, holder.binding.more);
                Menu menu=popup.getMenu();
                menu.add("Update Status");
                menu.add("Chat with Client");
                menu.add("cancel lead");
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        String title=item.getTitle().toString();
                        if(title.equalsIgnoreCase("Update Status")){
                            AlertDialog.Builder ab=new AlertDialog.Builder(context);
                            UpdateStatusViewBinding binding=UpdateStatusViewBinding.inflate(LayoutInflater.from(context));
                            ab.setView(binding.getRoot());
                            if(!leads.getStatus().equals("confirmed")){
                                if(leads.getStatus().equalsIgnoreCase("loaded")){
                                    binding.loaded.setChecked(true);
                                }else if(leads.getStatus().equalsIgnoreCase("in transit")){
                                    binding.intransit.setChecked(true);
                                }else if(leads.getStatus().equalsIgnoreCase("reached")){
                                    binding.reached.setChecked(true);
                                }
                            }else{

                            }
                            binding.loaded.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    if(isChecked){

                                    }
                                }
                            });
                            ab.show();
                        }
                        return false;
                    }
                });
                popup.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return al.size();
    }

    public class CurrentLeadsViewHolder extends RecyclerView.ViewHolder{
        CurrentLoadViewBinding binding;
        public CurrentLeadsViewHolder(CurrentLoadViewBinding binding)
        {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
