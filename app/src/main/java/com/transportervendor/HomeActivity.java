package com.transportervendor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.transportervendor.adapter.TabAccessorAdapter;
import com.transportervendor.beans.Transporter;
import com.transportervendor.databinding.ActivityHomeBinding;
import com.transportervendor.databinding.HeaderDrawerBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    TabAccessorAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityHomeBinding homeBinding=ActivityHomeBinding.inflate(LayoutInflater.from(HomeActivity.this));
        setContentView(homeBinding.getRoot());
        setSupportActionBar(homeBinding.toolbar);
        adapter=new TabAccessorAdapter(getSupportFragmentManager(),1);
        homeBinding.drawicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeBinding.drawerlayout.openDrawer(Gravity.RIGHT);
            }
        });
        View view=homeBinding.navigationView.inflateHeaderView(R.layout.header_drawer);
        ImageView iv=view.findViewById(R.id.btnback);
        HeaderDrawerBinding header= HeaderDrawerBinding.inflate(LayoutInflater.from(HomeActivity.this));
                iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HomeActivity.this, "clicked", Toast.LENGTH_SHORT).show();
                homeBinding.drawerlayout.closeDrawer(Gravity.RIGHT);

            }
        });

        homeBinding.viewPager.setAdapter(adapter);
        homeBinding.tabLayout.setupWithViewPager(homeBinding.viewPager);
        homeBinding.navigationView.setItemIconTintList(null);
        homeBinding.navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id=item.getItemId();
                if(id==R.id.profile){

                }
                else if(id==R.id.vehicle){

                }
                else if(id==R.id.terms){
                    Intent in=new Intent(HomeActivity.this,TermsnConditions.class);
                    startActivity(in);
                }else if(id==R.id.privacy){
                    Intent in=new Intent(HomeActivity.this,PrivacyPolicy.class);
                    startActivity(in);
                }else if(id==R.id.about){
                    Intent in=new Intent(HomeActivity.this,AboutUs.class);
                    startActivity(in);
                }else if(id==R.id.contact){
                    Intent in=new Intent(HomeActivity.this,ContactUs.class);
                    startActivity(in);
                }else if(id==R.id.logout){
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
                return true;
            }
        });
    }
}