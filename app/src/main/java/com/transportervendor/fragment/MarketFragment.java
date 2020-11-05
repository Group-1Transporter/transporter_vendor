package com.transportervendor.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.transportervendor.R;
import com.transportervendor.adapter.MarketLeadAdapter;
import com.transportervendor.beans.Leads;
import com.transportervendor.databinding.FragmentMarketBinding;

import java.util.ArrayList;

public class MarketFragment extends Fragment {
    FragmentMarketBinding fragment;
    RecyclerView.Adapter<MarketLeadAdapter.MarketLeadViewHolder>adapter;
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
        fragment.rv.setLayoutManager(new LinearLayoutManager(getContext()));
        ArrayList<Leads> al=new ArrayList<Leads>();
        al.add(new Leads("","","Steel (2 ton)","2 ton","14 clerkcolony indore mp","14 nagar bhopal mp","","","19-nov-2020","","confirmed","","","10"));
        al.add(new Leads("","","wood ","2 ton","14 clerkcolony gwalior mp","14 nagar agra up","","","19-nov-2020","","loaded","","","10"));
        al.add(new Leads("","","home material ","2 ton","14 clerkcolony jabalpur mp","14 nagar ahmedabad gujarat","","","19-nov-2020","in transit","","","","10"));
        al.add(new Leads("","","cement ","2 ton","14 clerkcolony ahmedabad gujrat","14 nagar mumbai maharashtra","","","19-nov-2020","","reached","","","10"));
        al.add(new Leads("","","medicines ","2 ton","14 clerkcolony delhi delhi","14 nagar itarsi up","","","19-nov-2020","","confirmed","","","10"));
        al.add(new Leads("","","petrol ","2 ton","14 clerkcolony shimla himachalpradesh","14 nagar ratlam mp","","","19-nov-2020","","loaded","","","10"));
        al.add(new Leads("","","soft drinks ","2 ton","14 clerkcolony ujjain mp","14 nagar dewas mp","","","19-nov-2020","","in transit","","","10"));
        al.add(new Leads("","","clothes ","2 ton","14 clerkcolony varanasi gujarat","14 nagar goa goa","","","19-nov-2020","","loaded","","","10"));
        adapter=new MarketLeadAdapter(getContext(),al);
        fragment.rv.setAdapter(adapter);
    }
}
