package com.transportervendor;

import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.transportervendor.databinding.BidNowViewBinding;

public class BidNowActivity extends AppCompatActivity {
    Toolbar toolbar;
    BidNowViewBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=BidNowViewBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
