package com.transportervendor;

import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.transportervendor.databinding.AboutUsBinding;

public class AboutUs extends AppCompatActivity {
    AboutUsBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=AboutUsBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
    }
}
