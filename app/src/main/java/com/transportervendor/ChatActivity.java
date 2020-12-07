package com.transportervendor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.transportervendor.adapter.ChatAdapter;
import com.transportervendor.apis.UserService;
import com.transportervendor.beans.Message;
import com.transportervendor.beans.User;
import com.transportervendor.databinding.ChatActivityBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {
    ChatActivityBinding binding;
    String userId;
    DatabaseReference firebaseDatabase;
    ChatAdapter adapter;
    ArrayList<Message>al;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ChatActivityBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        Intent in = getIntent();
        userId = (String) in.getCharSequenceExtra("id");
        UserService.UserApi userApi = UserService.getUserApiInstance();
        Call<User> call = userApi.getUser(userId);
        if (NetworkUtil.getConnectivityStatus(this)) {
            final ProgressDialog pd=new ProgressDialog(ChatActivity.this);
            pd.setCancelable(true);
            pd.setTitle("Loading...");
            pd.show();
            firebaseDatabase = FirebaseDatabase.getInstance().getReference();
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    pd.dismiss();
                    if (response.code() == 200)
                        getSupportActionBar().setTitle(response.body().getName());
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    pd.dismiss();
                    Toast.makeText(ChatActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "please enable internet connection.", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        binding.rv.setLayoutManager(new LinearLayoutManager(ChatActivity.this));
        binding.btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String etmsg = binding.etmsg.getText().toString();
                if (etmsg.isEmpty()) {
                    binding.etmsg.setError("this field can't be empty.");
                    return;
                }
                Calendar c = Calendar.getInstance();
                SimpleDateFormat sd = new SimpleDateFormat("MMM dd, yyyy");
                String date = sd.format(c.getTime());
                sd = new SimpleDateFormat("hh:mm a");
                String time = sd.format(c.getTime());
                if (NetworkUtil.getConnectivityStatus(ChatActivity.this)) {
                    final String id = FirebaseDatabase.getInstance().getReference().push().getKey();
                    final Message message = new Message(etmsg, date, time, FirebaseAuth.getInstance().getCurrentUser().getUid(), id, userId, c.getTimeInMillis());
                    firebaseDatabase.child("Messages").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(userId).child(id).setValue(message).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                firebaseDatabase.child("Messages").child(userId).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(id).setValue(message).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(ChatActivity.this, "Message sent.", Toast.LENGTH_SHORT).show();
                                            binding.etmsg.setText("");
                                        }
                                    }
                                });
                            }
                        }
                    });
                } else
                    Toast.makeText(ChatActivity.this, "please enable internet connection.", Toast.LENGTH_SHORT).show();

            }
        });
        al=new ArrayList<>();
        firebaseDatabase.child("Messages").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(userId).orderByChild("timestamp").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()){
                    Message msg = snapshot.getValue(Message.class);
                    al.add(msg);
                    adapter=new ChatAdapter(ChatActivity.this,al);
                    binding.rv.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    binding.rv.smoothScrollToPosition(adapter.getItemCount());
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
