package com.transportervendor;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.transportervendor.databinding.ActivitySplashBinding;

public class SplashScreen extends AppCompatActivity {
    ActivitySplashBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding  = ActivitySplashBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        if(!NetworkUtil.getConnectivityStatus(this))
            Toast.makeText(this, "Please enable internet connection", Toast.LENGTH_SHORT).show();
        Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    boolean isConnected = NetworkUtil.getConnectivityStatus(SplashScreen.this);
                    if (isConnected) {
                        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                        if(user==null) {
                            Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }else{
                            Intent intent = new Intent(SplashScreen.this, HomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                    }else
                        run();

                }
            },5000);

    }
}
