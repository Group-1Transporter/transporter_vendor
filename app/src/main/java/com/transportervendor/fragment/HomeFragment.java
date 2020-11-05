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

import com.transportervendor.HomeActivity;
import com.transportervendor.R;
import com.transportervendor.adapter.CurrentLeadsAdapter;
import com.transportervendor.beans.Leads;
import com.transportervendor.databinding.FragmentHistoryBinding;
import com.transportervendor.databinding.FragmentHomeBinding;

import java.util.ArrayList;


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
        fragment.rv.setLayoutManager(new LinearLayoutManager(getContext()));

        ArrayList<Leads> al=new ArrayList<Leads>();
        adapter=new CurrentLeadsAdapter(getContext(),al);
        fragment.rv.setAdapter(adapter);
    }
}
