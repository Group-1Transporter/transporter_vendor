package com.transportervendor.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.transportervendor.R;
import com.transportervendor.adapter.AllBidsAdapter;
import com.transportervendor.adapter.CompletedLeadsAdapter;
import com.transportervendor.apis.BidService;
import com.transportervendor.beans.Bid;
import com.transportervendor.beans.Leads;
import com.transportervendor.databinding.FragmentHistoryBinding;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryFragment extends Fragment {
    FragmentHistoryBinding fragment;
    RecyclerView.Adapter<CompletedLeadsAdapter.CompletedLeadsViewHolder>adapter;
    RecyclerView.Adapter<AllBidsAdapter.AllBidsViewHolder>adapter1;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragment=FragmentHistoryBinding.inflate(LayoutInflater.from(getContext()));
        View v=fragment.getRoot();
        return v;
    }


    @Override
    public void onStart() {
        super.onStart();
        fragment.rv.setLayoutManager(new LinearLayoutManager(getContext()));
        if (fragment.complete.isChecked()){
            ArrayList<Leads> al = new ArrayList();
            al.add(new Leads("", "", "Steel (2 ton)", "2 ton", "14 clerkcolony indore mp", "14 nagar bhopal mp", "", "", "19-nov-2020", "", "confirmed", "", "", "10"));
            al.add(new Leads("", "", "wood ", "2 ton", "14 clerkcolony gwalior mp", "14 nagar agra up", "", "", "19-nov-2020", "", "loaded", "", "", "10"));
            al.add(new Leads("", "", "home material ", "2 ton", "14 clerkcolony jabalpur mp", "14 nagar ahmedabad gujarat", "", "", "19-nov-2020", "in transit", "", "", "", "10"));
            al.add(new Leads("", "", "cement ", "2 ton", "14 clerkcolony ahmedabad gujrat", "14 nagar mumbai maharashtra", "", "", "19-nov-2020", "", "reached", "", "", "10"));
            al.add(new Leads("", "", "medicines ", "2 ton", "14 clerkcolony delhi delhi", "14 nagar itarsi up", "", "", "19-nov-2020", "", "confirmed", "", "", "10"));
            al.add(new Leads("", "", "petrol ", "2 ton", "14 clerkcolony shimla himachalpradesh", "14 nagar ratlam mp", "", "", "19-nov-2020", "", "loaded", "", "", "10"));
            al.add(new Leads("", "", "soft drinks ", "2 ton", "14 clerkcolony ujjain mp", "14 nagar dewas mp", "", "", "19-nov-2020", "", "in transit", "", "", "10"));
            al.add(new Leads("", "", "clothes ", "2 ton", "14 clerkcolony varanasi gujarat", "14 nagar goa goa", "", "", "19-nov-2020", "", "loaded", "", "", "10"));
            adapter = new CompletedLeadsAdapter(getContext(), al);
            fragment.rv.setAdapter(adapter);
        }
        BidService.BidApi bidApi=BidService.getBidApiInstance();
        Call<ArrayList<Bid>> call= bidApi.getAllBids(FirebaseAuth.getInstance().getCurrentUser().getUid());
        ArrayList<Bid> al=new ArrayList<>();
        try {
            al= call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        adapter1=new AllBidsAdapter(getContext(),al);
        fragment.complete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b==true){
                    fragment.rv.setAdapter(adapter);
                }else{
                    fragment.rv.setAdapter(adapter1);
                }
            }
        });
    }
}
