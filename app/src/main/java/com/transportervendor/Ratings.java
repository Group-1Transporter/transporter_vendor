package com.transportervendor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;

import com.transportervendor.databinding.ActivityRatingsBinding;

public class Ratings extends AppCompatActivity {
    ActivityRatingsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityRatingsBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
    }
}