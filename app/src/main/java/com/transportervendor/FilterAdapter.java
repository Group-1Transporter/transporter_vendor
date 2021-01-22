package com.transportervendor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.transportervendor.beans.State;
import com.transportervendor.databinding.StateViewBinding;

import java.util.ArrayList;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.FilterViewHolder> {
    Context context;
    ArrayList<State>al;

    public FilterAdapter(Context context, ArrayList<State> al) {
        this.context = context;
        this.al = al;
        Filter.getInstance().clear();
    }

    @NonNull
    @Override
    public FilterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        StateViewBinding binding=StateViewBinding.inflate(LayoutInflater.from(context),parent,false);
        return new FilterViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FilterViewHolder holder, int position) {
        final State state=al.get(position);
        holder.binding.name.setText(state.getStateName());
        holder.binding.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Filter.getInstance().add(state.getStateName());
                }else{
                    Filter.getInstance().remove(state.getStateName());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return al.size();
    }

    class FilterViewHolder extends RecyclerView.ViewHolder{
        StateViewBinding binding;
        public FilterViewHolder(@NonNull StateViewBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
