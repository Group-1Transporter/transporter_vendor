package com.transportervendor.fragment;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.transportervendor.CustomProgressDialog;
import com.transportervendor.NetworkUtil;
import com.transportervendor.R;
import com.transportervendor.adapter.AllBidsAdapter;
import com.transportervendor.adapter.CompletedLeadsAdapter;
import com.transportervendor.apis.BidService;
import com.transportervendor.apis.LeadsService;
import com.transportervendor.beans.Bid;
import com.transportervendor.beans.BidWithLead;
import com.transportervendor.beans.Leads;
import com.transportervendor.databinding.FragmentHistoryBinding;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class HistoryFragment extends Fragment {
    FragmentHistoryBinding fragment;
    RecyclerView.Adapter<CompletedLeadsAdapter.CompletedLeadsViewHolder> adapter;
    RecyclerView.Adapter<AllBidsAdapter.AllBidsViewHolder> adapter1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragment = FragmentHistoryBinding.inflate(LayoutInflater.from(getContext()));
        View v = fragment.getRoot();
        LeadsService.LeadsApi leadApi = LeadsService.getLeadsApiInstance();
        Call<ArrayList<BidWithLead>> call = leadApi.getCompletedLeads(FirebaseAuth.getInstance().getCurrentUser().getUid());
        if (NetworkUtil.getConnectivityStatus(getContext())) {
            final CustomProgressDialog pd=new CustomProgressDialog(getContext(),"Please wait...");
            pd.show();
            call.enqueue(new Callback<ArrayList<BidWithLead>>() {
                @Override
                public void onResponse(Call<ArrayList<BidWithLead>> call, Response<ArrayList<BidWithLead>> response) {
                    pd.dismiss();
                    if (response.code() == 200) {
                        ArrayList<BidWithLead> al = response.body();
                        if(al==null)
                            al=new ArrayList<>();
                        adapter = new CompletedLeadsAdapter(getContext(), al);
                        fragment.rv.setAdapter(adapter);
                        fragment.rv.setLayoutManager(new LinearLayoutManager(getContext()));
                        SharedPreferences mPrefs= getActivity().getSharedPreferences("Transporter",MODE_PRIVATE);
                        SharedPreferences.Editor prefsEditor = mPrefs.edit();
                        prefsEditor.putString("completed",al.size()+"");
                        prefsEditor.commit();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<BidWithLead>> call, Throwable t) {
                    pd.dismiss();
                    Toast.makeText(getContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else
            Toast.makeText(getContext(), "Please enable internet connection.", Toast.LENGTH_SHORT).show();
        return v;
    }


    @Override
    public void onStart() {
        super.onStart();

        fragment.rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.complete) {
                    LeadsService.LeadsApi leadApi = LeadsService.getLeadsApiInstance();
                    Call<ArrayList<BidWithLead>> call = leadApi.getCompletedLeads(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    if (NetworkUtil.getConnectivityStatus(getContext())) {
                        final CustomProgressDialog pd=new CustomProgressDialog(getContext(),"Please wait...");
                        pd.show();
                        call.enqueue(new Callback<ArrayList<BidWithLead>>() {
                            @Override
                            public void onResponse(Call<ArrayList<BidWithLead>> call, Response<ArrayList<BidWithLead>> response) {
                                pd.dismiss();
                                if (response.code() == 200) {
                                    ArrayList<BidWithLead> al = response.body();
                                    adapter = new CompletedLeadsAdapter(getContext(), al);
                                    fragment.rv.setAdapter(adapter);
                                    fragment.rv.setLayoutManager(new LinearLayoutManager(getContext()));
                                }
                            }

                            @Override
                            public void onFailure(Call<ArrayList<BidWithLead>> call, Throwable t) {
                                pd.dismiss();
                                Toast.makeText(getContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else
                        Toast.makeText(getContext(), "Please enable internet connection.", Toast.LENGTH_SHORT).show();
                } else if (checkedId == R.id.all) {
                    BidService.BidApi bidApi = BidService.getBidApiInstance();
                    Call<ArrayList<BidWithLead>> call = bidApi.getAllBids(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    if(NetworkUtil.getConnectivityStatus(getContext())) {
                        final CustomProgressDialog pd=new CustomProgressDialog(getContext(),"Please wait...");
                        pd.show();
                        call.enqueue(new Callback<ArrayList<BidWithLead>>() {
                            @Override
                            public void onResponse(Call<ArrayList<BidWithLead>> call, Response<ArrayList<BidWithLead>> response) {
                                pd.dismiss();
                                if (response.code() == 200) {
                                    ArrayList<BidWithLead> al = response.body();
                                    adapter1 = new AllBidsAdapter(getContext(), al);
                                    fragment.rv.setAdapter(adapter1);
                                    fragment.rv.setLayoutManager(new LinearLayoutManager(getContext()));
                                }
                            }

                            @Override
                            public void onFailure(Call<ArrayList<BidWithLead>> call, Throwable t) {
                                pd.dismiss();
                                Toast.makeText(getContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else{
                        Toast.makeText(getContext(), "please enable internet connection.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
