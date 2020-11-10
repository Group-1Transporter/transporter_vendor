package com.transportervendor;

import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.transportervendor.databinding.TermsConditionsBinding;

public class TermsnConditions extends AppCompatActivity {
    TermsConditionsBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=TermsConditionsBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
    }
}
