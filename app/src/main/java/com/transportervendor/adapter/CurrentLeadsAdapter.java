package com.transportervendor.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.transportervendor.BidInfoActivity;
import com.transportervendor.BidNowActivity;
import com.transportervendor.ChatActivity;
import com.transportervendor.CustomProgressDialog;
import com.transportervendor.NetworkUtil;
import com.transportervendor.R;
import com.transportervendor.apis.LeadsService;
import com.transportervendor.apis.UserService;
import com.transportervendor.beans.BidWithLead;
import com.transportervendor.beans.Leads;
import com.transportervendor.beans.Transporter;
import com.transportervendor.beans.User;
import com.transportervendor.databinding.CurrentLoadViewBinding;
import com.transportervendor.databinding.UpdateStatusViewBinding;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CurrentLeadsAdapter extends RecyclerView.Adapter<CurrentLeadsAdapter.CurrentLeadsViewHolder> {
    ArrayList<BidWithLead>al;
    Context context;

    public CurrentLeadsAdapter(Context context,ArrayList<BidWithLead>al){
        this.context=context;
        this.al=al;
    }
    @NonNull
    @Override
    public CurrentLeadsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CurrentLoadViewBinding binding=CurrentLoadViewBinding.inflate(LayoutInflater.from(context));
        return new CurrentLeadsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final CurrentLeadsViewHolder holder, int position) {
        final BidWithLead bidWithLead=al.get(position);
        final Leads leads=bidWithLead.getLead();
        holder.binding.tvmaterial.setText(leads.getTypeOfMaterial());
        String str[]=leads.getPickUpAddress().split(",");
        String name=str[str.length-2];
        str=leads.getDeliveryAddress().split(",");
        name +=" to "+str[str.length-2];
        holder.binding.tvfrom.setText(name);
        holder.binding.status.setText(leads.getStatus());
        holder.binding.tvdate.setText("Date of completion: "+leads.getDateOfCompletion());
        holder.binding.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(context, holder.binding.more);
                Menu menu=popup.getMenu();
                menu.add("Update Status");
                menu.add("Chat with Client");
                menu.add("cancel lead");
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        String title=item.getTitle().toString();
                        if(title.equalsIgnoreCase("Update Status")){
                            final AlertDialog ab=new AlertDialog.Builder(context).create();
                            String status="confirmed";
                            final UpdateStatusViewBinding binding=UpdateStatusViewBinding.inflate(LayoutInflater.from(context));
                            ab.setView(binding.getRoot());
                            if(leads.getStatus().equalsIgnoreCase("confirmed")){

                            }else{
                                if(leads.getStatus().equalsIgnoreCase("loaded")){
                                    binding.loaded.setChecked(true);
                                }else if(leads.getStatus().equalsIgnoreCase("in transit")){
                                    binding.loaded.setChecked(true);
                                    binding.intransit.setChecked(true);
                                }else if(leads.getStatus().equalsIgnoreCase("reached")){
                                    binding.loaded.setChecked(true);
                                    binding.intransit.setChecked(true);
                                    binding.reached.setChecked(true);
                                }
                            }
                            binding.btnupdate.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String status="";
                                    if(binding.loaded.isChecked())
                                        status="loaded";
                                    if(binding.intransit.isChecked())
                                        status="in transit";
                                    if(binding.reached.isChecked())
                                        status="reached";
                                    if(binding.delivered.isChecked())
                                        status="completed";
                                    leads.setStatus(status);
                                    LeadsService.LeadsApi leadsApi=LeadsService.getLeadsApiInstance();
                                    Call<Leads> call=leadsApi.updateLeads(leads);
                                    ab.dismiss();
                                    if(NetworkUtil.getConnectivityStatus(context)) {
                                        final CustomProgressDialog pd=new CustomProgressDialog(context,"Please wait...");
                                        pd.show();
                                        call.enqueue(new Callback<Leads>() {
                                            @Override
                                            public void onResponse(Call<Leads> call, Response<Leads> response) {
                                                pd.dismiss();
                                                if(response.code()==200) {
                                                    Toast.makeText(context, "success.", Toast.LENGTH_SHORT).show();
                                                    holder.binding.status.setText(leads.getStatus());
                                                    UserService.UserApi userApi=UserService.getUserApiInstance();
                                                    Call<User>call3=userApi.getUser(response.body().getUserId());
                                                    pd.show();
                                                    call3.enqueue(new Callback<User>() {
                                                        @Override
                                                        public void onResponse(Call<User> call, Response<User> response) {
                                                            pd.dismiss();
                                                            if(response.isSuccessful()){
                                                                try {
                                                                    RequestQueue queue = Volley.newRequestQueue(context);
                                                                    String url = "https://fcm.googleapis.com/fcm/send";
                                                                    JSONObject data = new JSONObject();
                                                                    data.put("title", "status update");
                                                                    SharedPreferences shared = context.getSharedPreferences("Transporter", Context.MODE_PRIVATE);
                                                                    String json=shared.getString("Transporter","");
                                                                    Gson gson = new Gson();
                                                                    Transporter transporter = gson.fromJson(json, Transporter.class);
                                                                    data.put("body", "From : " + transporter.getName());
                                                                    JSONObject notification_data = new JSONObject();
                                                                    notification_data.put("data", data);
                                                                    notification_data.put("to", response.body().getToken());
                                                                    JsonObjectRequest request = new JsonObjectRequest(url, notification_data, new com.android.volley.Response.Listener<JSONObject>() {
                                                                        @Override
                                                                        public void onResponse(JSONObject response) {
                                                                        }
                                                                    }, new com.android.volley.Response.ErrorListener() {
                                                                        @Override
                                                                        public void onErrorResponse(VolleyError error) {
                                                                            Toast.makeText(context, "" + error, Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }) {
                                                                        @Override
                                                                        public Map<String, String> getHeaders() {
                                                                            String api_key_header_value = "Key=AAAA_8lmWnQ:APA91bEYQuN6DDzns0nY2CzXq-FUhVCvv0pGXq0nr3iH_sg27WDB8PcN1RFTz7-If5SNVHOfA3SMuxQyWyPZKb-cns4Sd06iMbIb7vruOHtiBrebRDZAqrMx5Hl5zmHanUFXDCi6ekSr";
                                                                            Map<String, String> headers = new HashMap<>();
                                                                            headers.put("Content-Type", "application/json");
                                                                            headers.put("Authorization", api_key_header_value);
                                                                            return headers;
                                                                        }
                                                                    };
                                                                    queue.add(request);
                                                                }catch (Exception e){
                                                                    Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        }

                                                        @Override
                                                        public void onFailure(Call<User> call, Throwable t) {
                                                            pd.dismiss();
                                                            Toast.makeText(context, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<Leads> call, Throwable t) {
                                                pd.dismiss();
                                                Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }else
                                        Toast.makeText(context, "please enable internet connection.", Toast.LENGTH_SHORT).show();
                                }
                            });
                            binding.btncancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ab.dismiss();
                                }
                            });
                            ab.show();
                        }else if(title.equalsIgnoreCase("Chat with Client")){
                            Intent in=new Intent(context, ChatActivity.class);
                            in.putExtra("id",leads.getUserId());
                            context.startActivity(in);

                        }else if(title.equalsIgnoreCase("cancel lead")) {
                            if (leads.getStatus().equalsIgnoreCase("confirmed")) {
                                leads.setStatus("");
                                leads.setDealLockedWith("");
                                LeadsService.LeadsApi leadsApi = LeadsService.getLeadsApiInstance();
                                Call<Leads> call = leadsApi.updateLeads(leads);
                                if (NetworkUtil.getConnectivityStatus(context)) {
                                    final CustomProgressDialog pd=new CustomProgressDialog(context,"Please wait...");
                                    pd.show();
                                    call.enqueue(new Callback<Leads>() {
                                        @Override
                                        public void onResponse(Call<Leads> call, Response<Leads> response) {
                                            pd.dismiss();
                                            if (response.code() == 200)
                                                Toast.makeText(context, "lead cancelled", Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onFailure(Call<Leads> call, Throwable t) {
                                            pd.dismiss();
                                            Toast.makeText(context, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                } else {
                                    Toast.makeText(context,  "please enable internet connection.", Toast.LENGTH_SHORT).show();
                                }
                            }else
                                Toast.makeText(context, "this lead can't be cancelled.", Toast.LENGTH_SHORT).show();

                        }
                        return false;
                    }
                });
                popup.show();
            }
        });

    }

    @Override
    public int getItemCount() {
       return al.size();
    }

    public class CurrentLeadsViewHolder extends RecyclerView.ViewHolder{
        CurrentLoadViewBinding binding;
        public CurrentLeadsViewHolder(CurrentLoadViewBinding binding)
        {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
