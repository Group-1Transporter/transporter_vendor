package com.transportervendor.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.transportervendor.HomeActivity;
import com.transportervendor.R;
import com.transportervendor.adapter.CompletedLeadsAdapter;
import com.transportervendor.adapter.CurrentLeadsAdapter;
import com.transportervendor.apis.LeadsService;
import com.transportervendor.beans.BidWithLead;
import com.transportervendor.beans.Leads;
import com.transportervendor.databinding.FragmentHistoryBinding;
import com.transportervendor.databinding.FragmentHomeBinding;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {
    FragmentHomeBinding fragment;
    RecyclerView.Adapter<CurrentLeadsAdapter.CurrentLeadsViewHolder>adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragment=FragmentHomeBinding.inflate(LayoutInflater.from(getActivity()));
         View v=fragment.getRoot();
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        LeadsService.LeadsApi leadApi =LeadsService.getLeadsApiInstance();
        Call<ArrayList<BidWithLead>> call = leadApi.getCurrentLeads(FirebaseAuth.getInstance().getCurrentUser().getUid());
        call.enqueue(new Callback<ArrayList<BidWithLead>>() {
            @Override
            public void onResponse(Call<ArrayList<BidWithLead>> call, Response<ArrayList<BidWithLead>> response) {
                ArrayList<BidWithLead> al = response.body();
                adapter = new CurrentLeadsAdapter(getContext(), al);
                fragment.rv.setAdapter(adapter);
                fragment.rv.setLayoutManager(new LinearLayoutManager(getContext()));
            }

            @Override
            public void onFailure(Call<ArrayList<BidWithLead>> call, Throwable t) {
                Toast.makeText(getContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
