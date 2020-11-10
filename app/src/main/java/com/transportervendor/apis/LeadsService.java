package com.transportervendor.apis;

import com.transportervendor.beans.BidWithLead;
import com.transportervendor.beans.Leads;
import com.transportervendor.beans.Transporter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public class LeadsService {
    public static LeadsService.LeadsApi leadsApi=null;
    public static LeadsService.LeadsApi getLeadsApiInstance(){
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(ServerAddress.serverAddress)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        if(leadsApi==null){
            leadsApi=retrofit.create(LeadsService.LeadsApi.class);
        }
        return leadsApi;
    }
    public interface LeadsApi{
        @GET("/lead/transporter/completed-lead/{id}")
        public Call<ArrayList<BidWithLead>> getCompletedLeads(@Path("id") String id);
        @GET("/lead/transporter/current-lead/{id}")
        public Call<ArrayList<BidWithLead>> getCurrentLeads(@Path("id") String id);
        @GET("/lead/all-lead")
        public Call<ArrayList<Leads>> getAllLeads();
        @GET("/lead/transporter/lead-id/{id}")
        public Call<ArrayList<String>> getcurrentLeadsId(@Path("id") String transporterId);
    }
}
