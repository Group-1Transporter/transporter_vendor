package com.transportervendor.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.internal.$Gson$Preconditions;
import com.transportervendor.AddVehicleActivity;
import com.transportervendor.R;
import com.transportervendor.adapter.MarketLeadAdapter;
import com.transportervendor.apis.LeadsService;
import com.transportervendor.beans.Leads;
import com.transportervendor.databinding.FilterViewBinding;
import com.transportervendor.databinding.FragmentMarketBinding;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MarketFragment extends Fragment {
    FragmentMarketBinding fragment;
    MarketLeadAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragment=FragmentMarketBinding.inflate(LayoutInflater.from(getContext()));
        View v=fragment.getRoot();
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        LeadsService.LeadsApi leadsApi=LeadsService.getLeadsApiInstance();
        Call<ArrayList<String>>call1=leadsApi.getcurrentLeadsId(FirebaseAuth.getInstance().getCurrentUser().getUid());
        Call<ArrayList<Leads>>call2=leadsApi.getAllLeads();
        call2.enqueue(new Callback<ArrayList<Leads>>() {
            @Override
            public void onResponse(Call<ArrayList<Leads>> call, Response<ArrayList<Leads>> response) {
                ArrayList<Leads>al=response.body();
                fragment.rv.setLayoutManager(new LinearLayoutManager(getContext()));
                adapter=new MarketLeadAdapter(getContext(),al);
            }

            @Override
            public void onFailure(Call<ArrayList<Leads>> call, Throwable t) {

            }
        });
        call1.enqueue(new Callback<ArrayList<String>>() {
            @Override
            public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {
                ArrayList<String>al=response.body();
                adapter.setLid(al);
                fragment.rv.setAdapter(adapter);
                fragment.txt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent in=new Intent(getContext(), AddVehicleActivity.class);
                        startActivity(in);
                    }
                });
            }

            @Override
            public void onFailure(Call<ArrayList<String>> call, Throwable t) {

            }
        });
        fragment.filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ab=new AlertDialog.Builder(getContext());
                FilterViewBinding binding=FilterViewBinding.inflate(LayoutInflater.from(getContext()));
                ab.setView(binding.getRoot());
                ab.show();
            }
        });
    }
}
