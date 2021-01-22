package com.transportervendor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.LayoutInflater;

import com.transportervendor.adapter.RatingsAdapter;
import com.transportervendor.beans.Rating;
import com.transportervendor.databinding.ActivityRatingsBinding;

import java.util.ArrayList;

public class RatingsActivity extends AppCompatActivity {
    ActivityRatingsBinding binding;
    ArrayList<Rating>al;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityRatingsBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        al=new ArrayList<>();
        binding.rv.setLayoutManager(new LinearLayoutManager(this));
        binding.rv.setAdapter(new RatingsAdapter(RatingsActivity.this,al));
    }
}